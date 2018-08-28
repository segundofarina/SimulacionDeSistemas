package ar.edu.itba.ss;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Simulation {
    private double boxSize;
    private Set<Particle> particles;

    private final double smallRadius = 0.005; // meters
    private final double smallMass = 0.1; // grams
    private final double largeRadius = 0.05; // meters
    private final double largeMass = 100; // grams

    private final double maxSpeed = 0.1;

    public Simulation(double boxSize, int smallParticlesAmount) {
        this.boxSize = boxSize;
        this.particles = new HashSet<>();

        addParticles(smallParticlesAmount);
    }

    /* Add random particles to the box */
    private void addParticles(int smallParticlesAmount) {
        Random rand = new Random();

        // Add large particle
        particles.add(new Particle(boxSize/2, boxSize/2, 0, 0, largeRadius, largeMass));

        // Add all small particles
        while(particles.size() < smallParticlesAmount + 1) {
            double xPosition = rand.nextDouble() * (boxSize - 2*smallRadius) + smallRadius;
            double yPosition = rand.nextDouble() * (boxSize - 2*smallRadius) + smallRadius;

            double speed = rand.nextDouble() * maxSpeed;
            double angle = rand.nextDouble() * 2 * Math.PI;
            double xSpeed = speed * Math.cos(angle);
            double ySpeed = speed * Math.sin(angle);

            if(!isOnExistentParticle(xPosition, yPosition, smallRadius)) {
                particles.add(new Particle(xPosition, yPosition, xSpeed, ySpeed, smallRadius, smallMass));
            }
        }
    }

    private boolean isOnExistentParticle(double xPosition, double yPosition, double radius) {
        for(Particle p : particles) {
            if( Math.pow((p.getxPosition() - xPosition),2) + Math.pow((p.getyPosition() - yPosition),2) <= Math.pow((p.getRadius() + radius),2) ) {
                return true;
            }
        }

        return false;
    }


    public void start() {
        final int iterations = 100;
        final Set<Particle> crashedParticles = new HashSet<>();

        for(int i = 0; i < iterations; i++) {
            double nextCrashTime = getNextCrashTime(crashedParticles);

            updateParticlesPosition(nextCrashTime);

            updateCrashedParticles(crashedParticles);
        }

        //System.out.println(getNextCrashTime());

    }

    private double getNextCrashTime(Set<Particle> crashedParticles) {
        double time = Double.POSITIVE_INFINITY;
        for(Particle p : particles) {
            // get wall crash time
            double wallCrashTime = wallCrash(p);
            if(time > wallCrashTime) {
                time = wallCrashTime;
                saveCrashedParticle(crashedParticles, p);
            }

            // get other particles crash
            for(Particle other : particles) {
                if(!other.equals(p)) {
                    double particleCrashTime = particlesCrash(p, other);
                    if(particleCrashTime < time) {
                        time = particleCrashTime;
                        saveCrashedParticle(crashedParticles, p, other);
                    }
                }
            }
        }

        return time;
    }

    private double particlesCrash(Particle p1, Particle p2) {
        double dVdR = (p2.getxSpeed() - p1.getxSpeed()) * (p2.getxPosition() - p2.getxPosition()) + (p2.getySpeed() - p1.getySpeed()) * (p2.getyPosition() - p2.getyPosition());
        if(dVdR >= 0) {
            return Double.POSITIVE_INFINITY;
        }

        double dVdV = Math.pow(p2.getxSpeed() - p1.getxSpeed(), 2) + Math.pow(p2.getySpeed() - p1.getySpeed(), 2);
        double dRdR = Math.pow(p2.getxPosition() - p1.getxPosition(), 2) + Math.pow(p2.getyPosition() - p1.getyPosition(), 2);
        double d = Math.pow(dVdR, 2) - dVdV * (dRdR - Math.pow(p2.getRadius() + p1.getRadius(), 2));

        if(d < 0) {
            return Double.POSITIVE_INFINITY;
        }

        return - (dVdR + Math.sqrt(d)) / (dVdV);
    }

    private double wallCrash(Particle p) {
        double minTimeX = Double.POSITIVE_INFINITY;

        if(p.getxSpeed() > 0) {
            minTimeX = (boxSize - p.getRadius() - p.getxPosition()) / p.getxSpeed();
        } else if(p.getxSpeed() < 0) {
            minTimeX = (p.getRadius() - p.getxPosition()) / p.getxSpeed();
        }

        double minTimeY = Double.POSITIVE_INFINITY;
        if(p.getySpeed() > 0) {
            minTimeY = (boxSize - p.getRadius() - p.getyPosition()) / p.getySpeed();

        } else if(p.getySpeed() < 0) {
            minTimeY = (p.getRadius() - p.getyPosition()) / p.getySpeed();
        }

        return (minTimeX < minTimeY) ? minTimeX : minTimeY;
    }

    private void updateParticlesPosition(double time) {
        for(Particle p : particles) {
            p.setxPosition(p.getxPosition() + p.getxSpeed() * time);
            p.setyPosition(p.getyPosition() + p.getySpeed() * time);
        }
    }

    private void saveCrashedParticle(Set<Particle> crashedParticles, Particle p) {
        crashedParticles.clear();
        crashedParticles.add(p);
    }

    private void saveCrashedParticle(Set<Particle> crashedParticles, Particle p, Particle o) {
        crashedParticles.clear();
        crashedParticles.add(p);
        crashedParticles.add(o);
    }

    private void updateCrashedParticles(Set<Particle> crashedParticles) {
        /* Crashed against wall */
        if(crashedParticles.size() == 1) {

        }

        /* Crashed against other particle */
        if(crashedParticles.size() == 2) {

        }
    }
}
