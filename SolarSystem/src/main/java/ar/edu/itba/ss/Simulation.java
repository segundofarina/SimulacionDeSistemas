package ar.edu.itba.ss;

import java.util.HashSet;
import java.util.Set;

public class Simulation {
    private double dTime;
    private Set<Particle> planets;
    private Particle voyager;

    private BeemanCalculator beemanCalculator;

    private static double voyagerMass = 721.9; // kg
    private static int earthRadius = 6378; // km


    public Simulation(double dTime, double L, double v0) {
        this.dTime = dTime;

        // Get data from NASA to fill planets;
        planets = new HashSet<Particle>();

        // Get voyager position and speed acording to the earth and sun
        voyager = new Particle(0, 0, 0, 0, 0, voyagerMass);

        beemanCalculator = new BeemanCalculator();

    }

    public void start() {
        double time = 0;
        boolean done = false;

        while(!done && time < 100000) {

            // Update planets position

            // Update voyager position
            beemanCalculator.update(voyager);

            // Check minimum distance to jupiter

            // Check minimum distance to saturn

            // Stop when the voyager gets away from saturn

            time += dTime;
        }
    }


}
