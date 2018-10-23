package ar.edu.itba.ss;

import java.util.Set;
import java.util.function.Function;

public class InteractionForce {

    private final double A;
    private final double B;
    private final double kn;
    private final double kt;

    private final double W;
    private final double door;

    public InteractionForce(double A, double B,double kn, double kt, double W, double door){
        this.A = A;
        this.B = B;
        this.kn = kn;
        this.kt = kt;

        this.W = W;
        this.door = door;
    }

    public Vector calculate(Particle p, Set<Particle> neighbours, Function<Particle,Vector> position, Function<Particle,Vector> speed) {
        Vector force = Vector.ZERO;
        double distance;


        for(Particle other: neighbours) {
            if(!p.equals(other)) {
                distance = distance(p,other,position);


                Vector en = position.apply(p).subtract(position.apply(other)).versor();
                Vector et = en.tangent();


                double fn = socialFn(distance);
                double ft = 0;
                if(distance > 0){
                    fn += granularFn(distance);
                    ft  = granularFt(distance,relSpeed(p,other,speed,et));
                }



//                overlapping= overlaping(p, other, position);
//                if(overlapping<0){
//                    double fn = getFn(overlapping);
//                    double ft = getFt(overlapping, vrel(p, other, speed, position));
//
//
//                    Vector en = position.apply(other).subtract(position.apply(p)).versor();
//                    Vector et = en.tangent();
//
//                    force = force.add(en.multiplyBy(fn).add(et.multiplyBy(ft)));
//                }

                force = force.add(en.multiplyBy(fn).add(et.multiplyBy(ft)));

            }


        }


        //TODO add wall forces
        //force = force.add(getWallForces(p,position,speed));
        force = force.add(bottomWallForce(p, position, speed));
        return force;
    }


    private double distance(Particle i, Particle j, Function<Particle,Vector> position){
        return i.getRadius()+j.getRadius() - position.apply(i).subtract(position.apply(j)).abs();
    }

    private double socialFn(double distance){
        return A*Math.exp( distance /B);
    }

    private double granularFn(double distance){
        return kn * distance;
    }
    private double granularFt(double distance, double relSpeed){
        return kt * distance * relSpeed;
    }

    private double relSpeed(Particle i, Particle j, Function<Particle,Vector> speed,Vector et){
        return speed.apply(j).subtract(speed.apply(i)).dot(et);
    }

    private Vector bottomWallForce(Particle p, Function<Particle, Vector> position, Function<Particle, Vector> speed) {
        if(position.apply(p).x > W/2 - door/2 && position.apply(p).x < (W/2 + door/2)) {
            return Vector.ZERO;
        }

        double distance = p.getRadius() - position.apply(p).y;

        Vector en = Vector.of(0, 1);
        Vector et = en.tangent();

        double relSpeed = speed.apply(p).dot(et);

        double fn = socialFn(distance);
        double ft = 0;

        if(distance > 0) {
            fn += granularFn(distance);
            ft -= granularFt(distance, relSpeed);
        }

        return en.multiplyBy(fn).add(et.multiplyBy(ft));
    }

}
