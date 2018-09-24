package ar.edu.itba.ss;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class Simulation {
    private double dTime;
    private Map<String, Particle> planets;
    private Particle voyager;
    private BufferedWriter bw;

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
        voyager = new Particle(planets.values().size(), voyagerPos.getX(), voyagerPos.getY(), voyagerSpeed.getX(), voyagerSpeed.getY(), voyagerMass,1.0E+7);

        // Initialize beeman
        Set<Particle> particles = new HashSet<Particle>(planets.values());
        particles.add(voyager);

        ForceCalculator fc = new ForceCalculator(particles);
        beemanCalculator = new BeemanCalculator(dTime, fc);

    }

    public Point2D start(String outPath) {
        double time = 0, minDistanceJupiter = Double.POSITIVE_INFINITY, minDistanceSaturn = Double.POSITIVE_INFINITY;
        boolean done = false;

        initalizeBW(outPath);

        Particle jupiter = planets.get("jupiter");
        Particle saturn = planets.get("saturn");

        Set<Particle> planetsParticles = new HashSet<Particle>(planets.values());

        int iter = 0;

        while(!done && iter < 100000) {

            appendToFile(bw,generateFileString(planets.values(),voyager));
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
                //System.out.println("done" + distanceToJupiter);
                //System.out.println("done" + distanceToSaturn);
                //done = true;
            }

            //System.out.println("Distance to jupiter: " + distanceToJupiter);
            //System.out.println("Distance to saturn: " + distanceToSaturn);

            time += dTime;
            iter++;
        }

        //System.out.println("Min distance to jupiter: " + minDistanceJupiter);
        //System.out.println("Min distance to saturn: " + minDistanceSaturn);

        closeBW();
        return new Point2D.Double(minDistanceJupiter, minDistanceSaturn);
    }

    private Point2D getVoyagerInitialPosition(double L) {
        Particle sun = planets.get("sun");
        Particle earth = planets.get("earth");

        double xSunEarth = earth.getxPosition() - sun.getxPosition();
        double ySunEarth = earth.getyPosition() - sun.getyPosition();

        double angle = Math.atan(ySunEarth / xSunEarth);

        double x = earth.getxPosition() + Math.cos(angle) * (L + earthRadius);
        double y = earth.getyPosition() + Math.sin(angle) * (L + earthRadius);

        return new Point2D.Double(x, y);
    }

    private Point2D getVoyagerInitialSpeed(double v0) {
        double angle = sunEarthAngle();

        double vx = - v0 * Math.sin(angle);
        double vy = v0 * Math.cos(angle);

        System.out.println("Vx"+vx+" Vy"+vy);
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

        planets.put("sun", new Particle(0, 3.541021921578323E+05,-6.578268133977889E+05,1.396466697359526E-02,4.212108884730581E-05,1988500E+24,696000*60));
        planets.put("earth", new Particle(1,1.443040359985483E+08,-4.566821691926755E+07,8.429276455862507,2.831601955976786E+01,5.97219E+24,6378.137*1400));
        planets.put("jupiter", new Particle(2,1.061950341671551E+08,7.544955348409320E+08,-1.309157032053854E+01,2.424744678419164,1898.13E+24,71492*200));
        planets.put("saturn", new Particle(3,-1.075238877886715E+09,8.538222924091074E+08,-6.527515746018062,-7.590526046562251,5.6834E+26,60268*200));

        return planets;
    }

    private void closeBW() {
        if (bw != null) try {
            bw.flush();
            bw.close();
        } catch (IOException ioe2) {
            // just ignore it
        }
    }

    private boolean initalizeBW(String outPath) {
        try {

            bw = new BufferedWriter(new FileWriter(outPath+"/solarSystem" + LocalDateTime.now() + ".txt", true));
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String generateFileString(Collection<Particle> allPlanets,Particle voyager ){

        StringBuilder builder = new StringBuilder()
                .append(allPlanets.size()+1)
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t vx\t vy\t\r\n");
        Set<Particle> extra = new HashSet<>();
        extra.add(voyager);

        appendParticles(allPlanets, builder);
        appendParticles(extra, builder);
        return builder.toString();
    }

    private void appendParticles(Collection<Particle> allParticles, StringBuilder builder) {
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

    public static void appendToFile (BufferedWriter bw , String data) {
        try {
            bw.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
