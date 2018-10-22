package ar.edu.itba.ss;

import java.util.Set;
import java.util.function.Function;

public class SocialForce {

    private final double A;
    private final double B;

    public SocialForce(double A, double B){
        this.A = A;
        this.B = B;
    }

    public Vector calculate(Particle p, Set<Particle> neighbours, Function<Particle,Vector> position, Function<Particle,Vector> speed) {
        Vector force = Vector.ZERO;
        double overlapping;

        for(Particle other: neighbours) {
            if(!p.equals(other)) {
                overlapping= overlapping(p, other, position);
                double fn = getFn(overlapping);

                Vector en = position.apply(other).subtract(position.apply(p))
                        .dividedBy(position.apply(other).subtract(position.apply(p)).abs());

                force = force.add(Vector.of(fn * en.x ,fn * en.y ));

            }
        }
        return force;
    }

    private double getFn(double overlaping){
        return A*Math.exp(- overlaping /B);
    }

    private double overlapping(Particle i, Particle j, Function<Particle,Vector> position){
        //TODO check overlapping signs
        double result =  position.apply(i).subtract(position.apply(j)).abs() - ( i.getRadius() + j.getRadius());
        return result ;
    }
}
