package ar.edu.itba.ss;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Silo {
    private Set<Particle> particles;
    private double L, W, D,time,dt;

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
        this.time =0;
        this.dt=0.1*Math.sqrt(mass/Math.pow(10, 5));

        System.out.println("Dt is :"+dt);

        System.out.println("Adding particles...");
        int i = 0;
        while(i < 600) {
            if(addPartilce()) {
                i++;
                System.out.println(i);
            }
        }

        System.out.println(particles.size()+" particles added.");


    }

    public void start(String outPath,double finalTime){
        Printer printer = new Printer(outPath,L,W);
        Integrator integrator = new Beeman(new ForceCalculator(L,W,D),dt);

        int iterations= 0;
        while(time< finalTime && iterations<1000){
            printer.appendToFile(particles);
            this.particles=integrator.integrate(particles);

            System.out.println("Time: "+time+"\t iterations: "+iterations);

            time+=dt;
            iterations++;
        }
        printer.close();

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
