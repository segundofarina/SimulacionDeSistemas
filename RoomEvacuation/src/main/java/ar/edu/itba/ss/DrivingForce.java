package ar.edu.itba.ss;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.Set;
import java.util.function.Function;

public class DrivingForce {


    private final double tau;

    public DrivingForce(double tau){
        this.tau=tau;
    }

    public Vector calculate(Particle p, Function<Particle,Vector> position, Function<Particle,Vector> speed) {
        //TODO check desiredSpeed calculation
        Vector desiredSpeed = p.getTarget().subtract(position.apply(p)).versor().multiplyBy(p.getDesiredSpeed());

        Vector force = desiredSpeed.subtract(speed.apply(p)).dividedBy(tau).multiplyBy(p.getMass());


        return force;
    }
}
