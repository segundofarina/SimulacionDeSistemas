package ar.edu.itba.ss;

import java.util.Set;
import java.util.function.Function;

public class GranularForce {
    private final double kn;
    private final double kt;

    public GranularForce(double kn, double kt){
        this.kn = kn;
        this.kt = kt;
    }



    public Vector calculate(Particle p, Set<Particle> neighbours, Function<Particle,Vector> position, Function<Particle,Vector> speed) {
        Vector force = Vector.ZERO;
        double overlapping;


        for(Particle other: neighbours) {
            if(!p.equals(other)) {
                overlapping= overlaping(p, other, position);
                if(overlapping<0){
                    double fn = getFn(overlapping);
                    double ft = getFt(overlapping, vrel(p, other, speed, position));


                    Vector en = position.apply(other).subtract(position.apply(p)).versor();
                    Vector et = en.tangent();

                    force = force.add(en.multiplyBy(fn).add(et.multiplyBy(ft)));
                }

            }


        }


        //TODO add wall forces
        //force = force.add(getWallForces(p,position,speed));
        return force;
    }

    private double getFn(double overlaping){
        return - overlaping * kn;
    }
    private double getFt(double tanSpeed, double overlaping){
        return tanSpeed * overlaping * kt;
    }


    private double overlaping(Particle i, Particle j, Function<Particle,Vector> position){

        // Equation on slide 31 of ppt is with  the sign altered from previos overlapping implementation
        double result =  position.apply(i).subtract(position.apply(j)).abs() - ( i.getRadius() + j.getRadius());


        return result < 0 ? result  : 0;
    }

    private double vrel(Particle i, Particle j, Function<Particle, Vector> speed, Function<Particle, Vector> position) {
        Vector direction = position.apply(j).subtract(position.apply(i)).tangent();
        return speed.apply(i).subtract(speed.apply(j)).projectedOn(direction);
    }

}
