package ar.edu.itba.ss;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ForceCalculator {
    private Set<Particle> particles;

    private static double G = 6.693 * Math.pow(10, -11); // m3 / (kg * s2)
    //private static double G = 6.693 * Math.pow(10, -20); // m3 / (kg * s2)

    public ForceCalculator(Set<Particle> particles) {
        this.particles = particles;
    }

    public Point2D calculate(Particle p) {

        double x = 0, y = 0;

        for(Particle other : particles) {
            if(!p.equals(other)) {

                double f = calculateForce(p, other);

                x += getXForce(f, p, other)*0.001;
                y += getYForce(f, p, other)*0.001;
            }
        }

        return new Point2D.Double(x, y);
    }


    private double calculateForce(Particle pi, Particle pj) {
        return G * pi.getMass() * pj.getMass() / Math.pow(distanceBetween(pi, pj), 2);
    }

    private double distanceBetween(Particle pi, Particle pj) {
        return Math.sqrt( Math.pow((pj.getxPosition() - pi.getxPosition())*1000, 2) + Math.pow((pj.getyPosition() - pi.getyPosition())*1000, 2) );
    }

    private double getXForce(double f, Particle pi, Particle pj) {
        return f * (pj.getxPosition() - pi.getxPosition())*1000 / distanceBetween(pj, pi);
    }

    private double getYForce(double f, Particle pi, Particle pj) {
        return f * (pj.getyPosition() - pi.getyPosition())*1000 / distanceBetween(pj, pi);
    }

    public void updateParticles(Set<Particle> p) {
        this.particles = p;
    }
/*
    public Point2D calculate(Particle p) {
        double x = 0, y = 0;

        //List<Point2D> forces = new ArrayList<>();

        for(Particle other : particles) {
            if(!p.equals(other)) {

                //forces.add(calculateForce(p, other));
                Point2D f = calculateForce(p, other);
                x += f.getX();
                y += f.getY();

            }
        }

        return new Point2D.Double(x, y);
    }

    private Point2D calculateForce(Particle p1, Particle p2) {
        Point2D vector = new Point2D.Double(p2.getxPosition() - p1.getxPosition(), p2.getyPosition() - p1.getyPosition());
        double r = Math.hypot(vector.getX(), vector.getY());

        return new Point2D.Double(vector.getX() * G * p1.getMass() * p2.getMass() /(r * r * r),
                vector.getY() * G * p1.getMass() * p2.getMass() /(r * r * r));
    }*/
}
