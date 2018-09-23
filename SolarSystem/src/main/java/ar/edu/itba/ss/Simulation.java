package ar.edu.itba.ss;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Simulation {
    private double dTime;
    private Map<String, Particle> planets;
    private Particle voyager;

    private BeemanCalculator beemanCalculator;

    private static double voyagerMass = 721.9; // kg
    private static int earthRadius = 6378; // km


    public Simulation(double dTime, double L, double v0) {
        this.dTime = dTime;

        // Get data from NASA to fill planets;
        planets = loadPlanetsFromNASA();

        // Get voyager position and speed acording to the earth and sun
        Point2D voyagerPos = getVoyagerInitialPosition(L);
        Point2D voyagerSpeed = getVoyagerInitialSpeed(v0);
        voyager = new Particle(planets.values().size(), voyagerPos.getX(), voyagerPos.getY(), voyagerSpeed.getX(), voyagerSpeed.getY(), voyagerMass);


        // Initialize beeman
        Set<Particle> particles = new HashSet<Particle>(planets.values());
        particles.add(voyager);

        ForceCalculator fc = new ForceCalculator(particles);
        beemanCalculator = new BeemanCalculator(dTime, fc);
    }

    public void start() {
        double time = 0, minDistanceJupiter = Double.POSITIVE_INFINITY, minDistanceSaturn = Double.POSITIVE_INFINITY;
        boolean done = false;

        Particle jupiter = planets.get("jupiter");
        Particle saturn = planets.get("saturn");

        Set<Particle> planetsParticles = new HashSet<Particle>(planets.values());

        while(!done && time < 100000) {

            // Update planets position
            beemanCalculator.update(planetsParticles);

            // Update voyager position
            beemanCalculator.update(voyager);

            // Check minimum distance to jupiter
            double distanceToJupiter = distanceBetween(voyager, jupiter);
            if(distanceToJupiter < minDistanceJupiter) {
                minDistanceJupiter = distanceToJupiter;
            }

            // Check minimum distance to saturn
            double distanceToSaturn = distanceBetween(voyager, saturn);
            if(distanceToSaturn < minDistanceSaturn) {
                minDistanceSaturn = distanceToSaturn;
            } else {
                // Stop when the voyager gets away from saturn
                System.out.println("done" + distanceToJupiter);
                System.out.println("done" + distanceToSaturn);
                done = true;
            }

            System.out.println("Min distance to jupiter: " + minDistanceJupiter);
            System.out.println("Min distance to saturn: " + minDistanceSaturn);

            time += dTime;
        }
    }

    private Point2D getVoyagerInitialPosition(double L) {
        Particle sun = planets.get("sun");
        Particle earth = planets.get("earth");

        double xSunEarth = earth.getxPosition() - sun.getxPosition();
        double ySunEarth = earth.getyPosition() - sun.getyPosition();

        double angle = Math.atan(ySunEarth / xSunEarth);

        double x = xSunEarth + Math.cos(angle) * (L + earthRadius);
        double y = ySunEarth + Math.sin(angle) * (L + earthRadius);

        return new Point2D.Double(x, y);
    }

    private Point2D getVoyagerInitialSpeed(double v0) {
        double angle = sunEarthAngle();

        double vx = - v0 * Math.sin(angle);
        double vy = v0 * Math.cos(angle);

        return new Point2D.Double(vx, vy);
    }


    private double sunEarthAngle() {
        Particle sun = planets.get("sun");
        Particle earth = planets.get("earth");

        double xSunEarth = earth.getxPosition() - sun.getxPosition();
        double ySunEarth = earth.getyPosition() - sun.getyPosition();

        return Math.atan(ySunEarth / xSunEarth);
    }

    private double distanceBetween(Particle pi, Particle pj) {
        return Math.sqrt( Math.pow(pj.getxPosition() - pi.getxPosition(), 2) + Math.pow(pj.getyPosition() - pi.getyPosition(), 2) );
    }

    private Map<String, Particle> loadPlanetsFromNASA() {
        // Load planets into a map: PlanetName -> Planet
        // PlanetName in lowercase: "sun", "earth", "jupiter", "saturn"
        // Id's should be different

        Map<String, Particle> planets = new HashMap<String, Particle>();

        // dummy loading to avoid null pointer exeption
        planets.put("sun", new Particle(0,0,0,0,0,1000));
        planets.put("earth", new Particle(0,1,1,0,0,1000));
        planets.put("jupiter", new Particle(0,2,3,0,0,1000));
        planets.put("saturn", new Particle(0,3,3,0,0,1000));

        return planets;
    }

}
