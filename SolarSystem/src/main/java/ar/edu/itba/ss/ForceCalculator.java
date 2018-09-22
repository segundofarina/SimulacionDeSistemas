package ar.edu.itba.ss;

import java.awt.geom.Point2D;
import java.util.Set;

public class ForceCalculator {
    private Set<Particle> particles;

    private static double G = 6.693 * Math.pow(10, -11); // m3 / (kg * s2)

    public ForceCalculator(Set<Particle> particles) {
        this.particles = particles;
    }

    public Point2D calculate(Particle p) {

        double x = 0, y = 0;

        for(Particle other : particles) {
            if(!p.equals(other)) {
                double f = calculateForce(p, other);

                x += getXForce(f, p, other);
                y += getYForce(f, p, other);
            }
        }

        return new Point2D.Double(x, y);
    }


    private double calculateForce(Particle pi, Particle pj) {
        return G * pi.getMass() * pj.getMass() / Math.pow(distanceBetween(pi, pj), 2);
    }

    private double distanceBetween(Particle pi, Particle pj) {
        return Math.sqrt( Math.pow(pi.getxPosition() - pj.getxPosition(), 2) + Math.pow(pi.getyPosition() - pj.getyPosition(), 2) );
    }

    private double getXForce(double f, Particle pi, Particle pj) {
        return f * (pj.getxPosition() - pi.getxPosition()) / distanceBetween(pj, pi);
    }

    private double getYForce(double f, Particle pi, Particle pj) {
        return f * (pj.getyPosition() - pi.getyPosition()) / distanceBetween(pj, pi);
    }

}
