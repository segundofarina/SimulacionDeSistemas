package ar.edu.itba.ss;

import ar.edu.itba.ss.CellIndex2.o.NeighbourCalculator;
import ar.edu.itba.ss.Integration.Beeman;
import ar.edu.itba.ss.Integration.Integrator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Silo {
    private Set<Particle> particles;
    private double L, W, D,time,dt;

    private static double minRadius = 0.01; // m
    private static double maxRadius = 0.015; // m
    private static double mass = 0.01; // kg

    // mu = 0.7
    // gama = 100 kg/s

    public Silo(double L, double W, double D) {
        particles = new HashSet<>();

        this.L = L;
        this.W = W;
        this.D = D;
        this.time = 0;
        this.dt = 0.1 * Math.sqrt(mass/Math.pow(10, 5));

        System.out.println("Dt is :"+dt);

        System.out.println("Adding particles...");
        int i = 0;
        while(i < 1000) {
            if(addPartilce()) {
                i++;
                System.out.println(i);
            }
        }

        System.out.println(particles.size() + " particles added.");


    }

    public void start(String outPath,double finalTime){
        Printer printer = new Printer(outPath, L, W);
        Integrator integrator = new Beeman(new ForceCalculator(L, W, D), new NeighbourCalculator(L,W,0,maxRadius), dt,particles);

        int iterations = 0;
        while(time < finalTime && iterations < 100000) {
            if(iterations % 100 == 0) {
                printer.appendToFile(particles);
                System.out.println("Time: " + time + "\t iterations: " + iterations);
            }
            this.particles = integrator.integrate(particles);

            this.particles = removeFallenParticles();

            time += dt;
            iterations++;
        }

        printer.close();
    }

    private boolean addPartilce() {
        Random rand = new Random();

        double radius = rand.nextDouble() * (maxRadius - minRadius) + minRadius;
        double x = rand.nextDouble() * (W - 2 * radius) + radius;
        double y = rand.nextDouble() * (L - 2 * radius) + radius;

        Particle p = new Particle(particles.size(), x, y, 0,0, radius, mass);

        for(Particle other : particles) {
            if(p.overlaps(other)) {
                return false;
            }
        }

        particles.add(p);
        return true;
    }

    private void addParticle(Particle oldParticle, Set<Particle> newParticles) {
        Random rand = new Random();
        boolean done = false;
        double radius = oldParticle.getRadius();
        double x = 0, y = 0;
        Particle p = oldParticle;

        while(!done) {
            x = rand.nextDouble() * (W - 2 * radius) + radius;
            y = rand.nextDouble() * (L/10 - 2 * radius) + radius + L * 9.0/10;

            p = new Particle(oldParticle.getId(), x, y, 0, 0, radius, mass);

            done = !isOverlapingOtherParticle(p, newParticles);
        }

        newParticles.add(p);
    }

    private boolean isOverlapingOtherParticle(Particle p, Set<Particle> newParticles) {
        for(Particle other : newParticles) {
            if(p.overlaps(other)) {
                return true;
            }
        }
        return false;
    }

    private Set<Particle> removeFallenParticles() {
        Set<Particle> newParticles = new HashSet<>();
        for(Particle p : this.particles) {
            if(p.getPosition().getY() > -L/10) {
                newParticles.add(p);
            } else {
                addParticle(p, newParticles);
            }
        }
        return newParticles;
    }
}
