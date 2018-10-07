package ar.edu.itba.ss;

import java.util.Set;
import java.util.function.Function;

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


    public Vector calculate(Particle p, Set<Particle> neighbours, Function<Particle,Vector> position, Function<Particle,Vector> speed) {
        Vector force = Vector.of(0,- p.getMass() * g);

        for(Particle other: neighbours) {
            if(!p.equals(other)) {
                double fn = getFn(overlaping(p, other,position),derivateOverlap(p,other,position,speed));
                double ft = getFt(fn, vrel(p, other,speed));

                Vector en = position.apply(other).subtract(position.apply(p))
                        .dividedBy(position.apply(other).subtract(position.apply(p)).abs());

                force=force.add(Vector.of(fn * en.x - ft * en.y,fn * en.y + ft * en.x));
            }
        }


        //force=force.add(getWallForces(p,position,speed));

        return force;


    }




    private double getFn(double overlaping, double derivOverlap) {
        return -Kn * overlaping - Gama * derivOverlap;
    }

    private double getFt(double Fn, double vrel) {
        return - Mu * Math.abs(Fn) * sign(vrel);
    }




    private double overlaping(Particle i, Particle j, Function<Particle,Vector> position){
        double result = i.getRadius() + j.getRadius() - position.apply(i).subtract(position.apply(j)).abs();
        return result > 0 ? result  : 0;
    }

    private double derivateOverlap(Particle i, Particle j, Function<Particle,Vector> position , Function<Particle,Vector> speed){
        double result = - sign(position.apply(j).abs() - position.apply(j).abs()) * (speed.apply(j).abs()- speed.apply(i).abs());
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



    private double vrel(Particle i, Particle j,Function<Particle,Vector> speed) {
        return speed.apply(i).abs() - speed.apply(j).abs();
    }




    private Vector getWallForces(Particle p,Function<Particle,Vector> position , Function<Particle,Vector> speed ) {

        Vector right = rightWall(p, position,speed);

        Vector left = leftWall(p,position,speed);

        Vector horizontal = horizontalWall(p,position,speed);

        return right.add(left).add(horizontal);
    }

    private Vector leftWall(Particle p,Function<Particle,Vector> position , Function<Particle,Vector> speed){
        double overlaping,dervOver, enx, eny,fn,ft;
        if(position.apply(p).x- p.getRadius() < 0){
            overlaping = p.getRadius() - position.apply(p).x;
            dervOver = - speed.apply(p).abs();
            enx = -1;
            eny = 0;
        }else{
            dervOver=overlaping=enx=eny=0;
        }
        fn = getFn(overlaping,dervOver);
        ft = getFt(fn,speed.apply(p).abs());
        return Vector.of(fn * enx - ft * eny, fn * eny + ft * enx);
    }

    private Vector rightWall(Particle p, Function<Particle, Vector> position,Function<Particle, Vector> speed) {
        double dervOver,overlaping, enx, eny,fn,ft;
        if(position.apply(p).x + p.getRadius() > W){
            overlaping = position.apply(p).x + p.getRadius() - W;
            dervOver = - speed.apply(p).abs();
            enx = 1;
            eny = 0;
        }else{
            dervOver=overlaping=enx=eny=0;
        }
        fn = getFn(overlaping,dervOver);
        ft = getFt(fn,speed.apply(p).abs());
        return Vector.of(fn * enx - ft * eny, fn * eny + ft * enx);
    }

    private Vector horizontalWall(Particle p, Function<Particle, Vector> position,Function<Particle, Vector> speed){
        double dervOver,overlaping, enx, eny,fn,ft;

        if(position.apply(p).y - p.getRadius()<0){
            overlaping = p.getRadius() - position.apply(p).x;
            dervOver = - speed.apply(p).abs();
            enx = 0;
            eny = -1;
        }else{
            dervOver=overlaping=enx=eny=0;
        }

        fn = getFn(overlaping,dervOver);
        ft = getFt(fn,speed.apply(p).abs());
        return  Vector.of( fn * enx - ft * eny, fn * eny + ft * enx);

    }


}
