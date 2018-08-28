package ar.edu.itba.ss;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
        particles.add(new Particle(0,boxSize/2, boxSize/2, 0, 0, largeRadius, largeMass));

        int id = 1;
        // Add all small particles
        while(particles.size() < smallParticlesAmount + 1) {
            double xPosition = rand.nextDouble() * (boxSize - 2*smallRadius) + smallRadius;
            double yPosition = rand.nextDouble() * (boxSize - 2*smallRadius) + smallRadius;

            double speed = rand.nextDouble() * maxSpeed;
            double angle = rand.nextDouble() * 2 * Math.PI;
            double xSpeed = speed * Math.cos(angle);
            double ySpeed = speed * Math.sin(angle);

            if(!isOnExistentParticle(xPosition, yPosition, smallRadius)) {
                particles.add(new Particle(id, xPosition, yPosition, xSpeed, ySpeed, smallRadius, smallMass));
                id++;
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


    public void start(int iterations) {
        final Set<Particle> crashedParticles = new HashSet<>();

        for(int i = 0; i < iterations; i++) {
            double nextCrashTime = getNextCrashTime(crashedParticles);

            System.out.println(crashedParticles);

            updateParticlesPosition(nextCrashTime);

            updateSpeedCrashedParticles(crashedParticles);

            System.out.println(nextCrashTime);
            System.out.println(crashedParticles);

            writeToFile(generateFileString(particles),i,"/Users/segundofarina/TP/TP-SS/out");
        }

    }

    public void startForAnimation(int animationTime){
        double jump = (double)1/60;
        double nextFrame = jump;
        double time = 0;
        int i=0;
        final Set<Particle> crashedParticles = new HashSet<>();
        while (time<animationTime && i<800){
            double nextCrashTime = getNextCrashTime(crashedParticles);

            if(nextCrashTime +time > nextFrame){
                updateParticlesPosition(nextFrame-time);

                writeToFile(generateFileString(particles),i++,"/Users/segundofarina/TP/TP-SS/out");
                //updateParticlesPosition(nextCrashTime+time-nextFrame);
                time=nextFrame;
                nextFrame+=jump;

            }else{
                updateParticlesPosition(nextCrashTime);
                updateSpeedCrashedParticles(crashedParticles);
                time+=nextCrashTime;
            }








        }
        System.out.print("Time"+time+" i"+i);

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

    static double particlesCrash(Particle pi, Particle pj) {
//        double dVdR = (p2.getxSpeed() - p1.getxSpeed()) * (p2.getxPosition() - p2.getxPosition()) + (p2.getySpeed() - p1.getySpeed()) * (p2.getyPosition() - p2.getyPosition());
//        if(dVdR >= 0) {
//            return Double.POSITIVE_INFINITY;
//        }
//
//        double dVdV = Math.pow(p2.getxSpeed() - p1.getxSpeed(), 2) + Math.pow(p2.getySpeed() - p1.getySpeed(), 2);
//        double dRdR = Math.pow(p2.getxPosition() - p1.getxPosition(), 2) + Math.pow(p2.getyPosition() - p1.getyPosition(), 2);
//        double d = Math.pow(dVdR, 2) - dVdV * (dRdR - Math.pow(p2.getRadius() + p1.getRadius(), 2));
//
//        if(d < 0) {
//            return Double.POSITIVE_INFINITY;
//        }
//
//        return - (dVdR + Math.sqrt(d)) / (dVdV);
        double dvx = pj.getxSpeed()-pi.getxSpeed();
        double dvy = pj.getySpeed()-pi.getySpeed();

        double dx = pj.getxPosition()-pi.getxPosition();
        double dy = pj.getyPosition()-pi.getyPosition();

        double dvdr = dvx*dx + dvy*dy;
        if(dvdr >= 0) {
            return Double.POSITIVE_INFINITY;
        }

        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double phi = pi.getRadius()+pj.getRadius();

        double d = dvdr*dvdr - dvdv*(drdr-phi*phi);

        if(d < 0) {
            return Double.POSITIVE_INFINITY;
        }


        return -(dvdr+Math.sqrt(d))/dvdv;
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

    private void updateSpeedCrashedParticles(Set<Particle> crashedParticles) {
        Iterator<Particle> it = crashedParticles.iterator();

        /* Crashed against wall */
        if(crashedParticles.size() == 1) {
            Particle p = it.next();
            if(crashedAgainstVerticalWall(p)) {
                p.setxSpeed(-p.getxSpeed());
            } else {
                p.setySpeed(-p.getySpeed());
            }
        }

        /* Crashed against other particle */
        if(crashedParticles.size() == 2) {
            Particle pi = it.next();
            Particle pj = it.next();

            //double dVdR = (p2.getxSpeed() - p1.getxSpeed()) * (p2.getxPosition() - p2.getxPosition()) + (p2.getySpeed() - p1.getySpeed()) * (p2.getyPosition() - p2.getyPosition());
            double dvx = pj.getxSpeed()-pi.getxSpeed();
            double dvy = pj.getySpeed()-pi.getySpeed();

            double dx = pj.getxPosition()-pi.getxPosition();
            double dy = pj.getyPosition()-pi.getyPosition();

            double dvdr = dvx*dx + dvy*dy;

            double phi = pi.getRadius() + pj.getRadius();
            double J = (2 * pi.getMass() * pj.getMass() * dvdr) / ( phi * (pi.getMass() + pj.getMass()) );
            double Jx = J * (pj.getxPosition() - pi.getxPosition()) / phi;
            double Jy = J * (pj.getyPosition() - pi.getyPosition()) / phi;

            pi.setxSpeed(pi.getxSpeed() + Jx / pi.getMass());
            pi.setySpeed(pi.getySpeed() + Jy / pi.getMass());
            pj.setxSpeed(pj.getxSpeed() - Jx / pj.getMass());
            pj.setySpeed(pj.getySpeed() - Jy / pj.getMass());
        }
    }

    private boolean crashedAgainstVerticalWall(Particle p) {
        if(p.getxPosition() <= p.getRadius()) {
            return true;
        }
        if(p.getxPosition() >= boxSize - p.getRadius()) {
            return true;
        }
        return false;
    }

    private String generateFileString(Set<Particle> allParticles){

        StringBuilder builder = new StringBuilder()
                .append(allParticles.size()+4)
                .append("\r\n")
                //.append("//ID\t X\t Y\t Radius\t R\t G\t B\t vx\t vy\t \r\n");
                .append("//ID\t X\t Y\t Radius\t vx\t vy\t\r\n");
        Set<Particle> limits =new HashSet<>();
        limits.addAll(
                Arrays.asList(new Particle(-1,0,0,0,0,0,0),
        new Particle(-2,0,boxSize,0,0,0,0),
        new Particle(-3,boxSize,0,0,0,0,0),
        new Particle(-4,boxSize,boxSize,0,0,0,0)));
        appendParticles(allParticles, builder);
        appendParticles(limits,builder);
        return builder.toString();
    }

    private void appendParticles(Set<Particle> allParticles, StringBuilder builder) {
        for(Particle current: allParticles){
            double vx = current.getxSpeed();
            double vy = current.getySpeed();
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getxPosition())
                    .append(" ")
                    .append(current.getyPosition())
                    .append(" ")
                    .append(current.getRadius())
                    .append(" ")
                    .append(new Double(vx).floatValue())
                    .append(" ")
                    .append(new Double(vy).floatValue())
                    .append("\r\n");


        }
    }

    public static void writeToFile(String data, int inedx, String path){
        try {
            Files.write(Paths.get(path + "/eventDriven" + inedx + ".txt"), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
