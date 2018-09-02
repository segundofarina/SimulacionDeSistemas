package ar.edu.itba.ss;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WallCollision extends Collision{
    Particle p1;


    public WallCollision (Particle p, double time){
        super(time);
        this.p1=p;
    }

    @Override
    public String toString() {
        return "WallCollsion{" +
                "p1=" + p1.getId() +
                ", time=" + this.getTime() +
                '}';
    }

    @Override
    public Set<Particle> getParticles() {
        return Stream.of(p1).collect(Collectors.toSet());
    }

    @Override
    public boolean contains(Particle p) {
        return p1.equals(p);
    }
}
