package ar.edu.itba.ss;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Silo {
    private Set<Particle> particles;
    private double L, W, D;

    private static double minRadius = 0.02; // m
    private static double maxRadius = 0.03; // m
    private static double mass = 0.01; // kg

    // mu = 0.7
    // gama = 100 kg/s

    public Silo(double L, double W, double D) {
        particles = new HashSet<>();

        this.L = L;
        this.W = W;
        this.D = D;

        int i = 0;
        while(i < 10000) {
            if(addPartilce()) {
                i++;
            }
        }

    }

    private boolean addPartilce() {
        Random rand = new Random();

        double x = rand.nextDouble() * W;
        double y = rand.nextDouble() * L;
        double radius = rand.nextDouble() * (maxRadius - minRadius) + minRadius;

        Particle p = new Particle(particles.size(), x, y, 0,0, radius, mass);

        for(Particle other : particles) {
            if(p.overlaps(other)) {
                return false;
            }
        }

        particles.add(p);
        return true;
    }
}
