package ar.edu.itba.ss;

import java.awt.geom.Point2D;
import java.util.Set;

public class ForceCalculator {
    private static double g = 9.8; // m/seg
    private static double Kn = Math.pow(10, 5); // N/m
    private static double Kt = 2 * Kn; // N/m
    private static double Mu = 0.7;
    private static double Gama = 100; // Kg/s

    private double L, W, D;

    public ForceCalculator(double L, double W, double D) {
        this.L = L;
        this.W = W;
        this.D = D;
    }


    public Point2D calculate(Particle p, Set<Particle> particles) {
        double fx = 0;
        double fy = p.getMass() * g;

        for(Particle other: particles) {
            if(!p.equals(other)) {
                double fn = getFn(overlaping(p, other));
                double ft = getFt(fn, vrel(p, other));

                double enx = getEnx(p, other);
                double eny = getEny(p, other);

                fx += fn * enx - ft * eny;
                fy += fn * eny + ft * enx;
            }
        }

        // add forces to walls
        Point2D wallForce = getWallForces(p);
        fx += wallForce.getX();
        fy += wallForce.getY();

        return new Point2D.Double(fx, fy);
    }

    private double getFn(double overlaping) {
        return -Kn * overlaping - Gama * 0;//overlaping derivado?
    }

    private double getFt(double Fn, double vrel) {
        return - Mu * Math.abs(Fn) * sign(vrel);
    }

    private double getEnx(Particle i, Particle j) {
        return (j.getxPosition() - i.getxPosition()) / distance(i, j);
    }

    private double getEny(Particle i, Particle j) {
        return (j.getyPosition() - i.getyPosition()) / distance(i, j);
    }

    private double distance(Particle i, Particle j) {
        return Math.sqrt( Math.pow( j.getxPosition() - i.getxPosition(), 2) + Math.pow( j.getyPosition() - i.getyPosition(), 2) );
    }

    private double overlaping(Particle i, Particle j) {
        double result = i.getRadius() + j.getRadius() - distance(i, j) ;
        return result > 0 ? result  : 0;
    }

    private int sign(double x) {
        if(x == 0) {
            return 0;
        }
        if(x < 0) {
            return -1;
        }
        return 1;
    }

    private double vrel(Particle i, Particle j) {
        return getAbsSpeed(i) - getAbsSpeed(j);
    }

    private double getAbsSpeed(Particle i) {
        return Math.sqrt(Math.pow(i.getxSpeed(), 2) + Math.pow(i.getySpeed(), 2));
    }

    private double getAbsPos(Particle i) {
        return Math.sqrt(Math.pow(i.getxPosition(), 2) + Math.pow(i.getyPosition(), 2));
    }

    private Point2D getWallForces(Particle p) {
        double overlaping = 0;

        if(p.getxPosition() + p.getRadius() > L){
            overlaping = p.getxPosition() + p.getRadius() - L;
        }
        if(p. getxPosition()- p.getRadius() < 0){
            overlaping = p.getRadius() - p.getxPosition();
        }

        double fn = getFn(overlaping);
        double ft = getFt(fn,getAbsSpeed(p));

        double enx = 1;
        double eny = 0;

        return new Point2D.Double( fn * enx - ft * eny, fn * eny + ft * enx);
    }
}
