package ar.edu.itba.ss;

import java.awt.geom.Point2D;
import java.util.*;

public class BeemanCalculator {

    private ForceCalculator forceCalculator;
    private EulerCalculator eulerCalculator;
    private double dTime;

    private Map<Integer, Point2D> oldForce;

    public BeemanCalculator(double dTime, ForceCalculator forceCalculator, Set<Particle> particles) {
        this.forceCalculator = forceCalculator;
        this.dTime = dTime;

        this.eulerCalculator = new EulerCalculator(-dTime, forceCalculator);

        oldForce = new HashMap<>();
        fillOldForce(particles);
    }

    public void update(Particle p) {
        // Calculate force
        Point2D force = forceCalculator.calculate(p);

        // Create last dt particle with euler
        Particle lastDtParticle = new Particle(p);
        eulerCalculator.update(lastDtParticle, force);

        // Calculate last dt force
        Point2D forceLastDt = forceCalculator.calculate(lastDtParticle);

        // Get particle next position
        Point2D pos = getNextPosition(p, force, forceLastDt);

        // Create new updated particle to get new force
        Particle updatedParticle = new Particle(p);
        updatedParticle.setxPosition(pos.getX());
        updatedParticle.setyPosition(pos.getY());

        // Get particle corrected speed
        //Point2D speed = getNextSpeed(p, updatedParticle, force, forceLastDt);

        // Update particle
        p.setxPosition(pos.getX());
        p.setyPosition(pos.getY());
        //p.setxSpeed(speed.getX());
        //p.setySpeed(speed.getY());
    }

    public void update(Set<Particle> particles) {
        for(Particle p : particles) {
            update(p);
        }
    }

    public void updateAll(Set<Particle> particles) {
        Map<Integer, Particle> updatedParticles = new HashMap<>();
        Map<Integer, Point2D> forces = new HashMap<>();

        forceCalculator.updateParticles(particles);

        for(Particle p : particles) {
            Point2D f = forceCalculator.calculate(p);
            forces.put(p.getId(), f);

            Point2D lastF = oldForce.get(p.getId());

            Point2D r = getNextPosition(p, f, lastF);

            Particle updatedParticle = new Particle(p);
            updatedParticle.setxPosition(r.getX());
            updatedParticle.setyPosition(r.getY());

            updatedParticles.put(p.getId(), updatedParticle);
        }

        forceCalculator.updateParticles(new HashSet<>(updatedParticles.values()));

        for(Particle p : updatedParticles.values()) {
            Point2D f = forces.get(p.getId());
            Point2D lastF = oldForce.get(p.getId());
            Point2D nextF = forceCalculator.calculate(p);

            Point2D v = getNextSpeed(p, f, lastF, nextF);

            p.setxSpeed(v.getX());
            p.setySpeed(v.getY());
        }

        for(Particle p : particles) {
            Particle updatedParticle = updatedParticles.get(p.getId());
            p.setxPosition(updatedParticle.getxPosition());
            p.setyPosition(updatedParticle.getyPosition());
            p.setxSpeed(updatedParticle.getxSpeed());
            p.setySpeed(updatedParticle.getySpeed());
        }
    }

    private Point2D getNextPosition(Particle p, Point2D force, Point2D forceLastDt) {
        double x = getNextPosition(p.getxPosition(), p.getxSpeed(), force.getX()/p.getMass(), forceLastDt.getX()/p.getMass());
        double y = getNextPosition(p.getyPosition(), p.getySpeed(), force.getY()/p.getMass(), forceLastDt.getY()/p.getMass());

        return new Point2D.Double(x, y);
    }

    private double getNextPosition(double x, double v, double a, double aLastDt) {
        return x + v * dTime + ( (2.0/3) * a - (1.0/6) * aLastDt ) * Math.pow(dTime, 2);
    }

    private Point2D getNextSpeed(Particle p, Point2D force, Point2D forceLastDt, Point2D forceNextDt) {
        double x = getNextSpeed(p.getxSpeed(), force.getX()/p.getMass(), forceLastDt.getX()/p.getMass(), forceNextDt.getX()/p.getMass());
        double y = getNextSpeed(p.getySpeed(), force.getY()/p.getMass(), forceLastDt.getY()/p.getMass(), forceNextDt.getY()/p.getMass());

        return new Point2D.Double(x, y);
    }

    private double getNextSpeed(double v, double a, double aLastDt, double aNextDt) {
        return v + ( (1.0/3) * aNextDt + (5.0/6) * a - (1.0/6) * aLastDt ) * dTime;
    }


    private void fillOldForce(Set<Particle> particles) {

        forceCalculator.updateParticles(particles);

        for(Particle p : particles) {
            Particle lastParticle = new Particle(p);
            oldForce.put(p.getId(), forceCalculator.calculate(lastParticle));
        }

    }
}
