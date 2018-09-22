package ar.edu.itba.ss;

import java.awt.geom.Point2D;

public class EulerCalculator {

    private double dTime;
    private ForceCalculator forceCalculator;

    public EulerCalculator(double dTime, ForceCalculator forceCalculator) {
        this.dTime = dTime;
        this.forceCalculator = forceCalculator;
    }

    public void update(Particle p, Point2D force) {
        // Get particle next position
        Point2D pos = getNextPosition(p, force);

        // Get particle next speed
        Point2D speed = getNextSpeed(p, force);

        // Update particle
        p.setxPosition(pos.getX());
        p.setyPosition(pos.getY());
        p.setxSpeed(speed.getX());
        p.setySpeed(speed.getY());
    }

    public void update(Particle p) {
        // Calculate force
        Point2D force = forceCalculator.calculate(p);

        update(p, force);
    }

    private Point2D getNextPosition(Particle p, Point2D force) {
        double x = getNextPosition(p.getxPosition(), p.getxSpeed(), force.getX() / p.getMass());
        double y = getNextPosition(p.getyPosition(), p.getySpeed(), force.getY() / p.getMass());

        return new Point2D.Double(x, y);
    }

    private double getNextPosition(double r, double v, double a) {
        return r + v * dTime + 0.5 * a * Math.pow(dTime, 2);
    }

    private Point2D getNextSpeed(Particle p, Point2D force) {
        double x = getNextSpeed(p.getxSpeed(), force.getX() / p.getMass());
        double y = getNextSpeed(p.getySpeed(), force.getY() / p.getMass());

        return new Point2D.Double(x, y);
    }

    private double getNextSpeed(double v, double a) {
        return v + a * dTime;
    }

}
