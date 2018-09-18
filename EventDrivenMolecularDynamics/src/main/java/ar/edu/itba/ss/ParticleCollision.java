package ar.edu.itba.ss;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParticleCollision extends Collision{
    Particle p1;
    Particle p2;

    public ParticleCollision (Particle p1, Particle p2, double time){
        super(time);
        this.p1 = p1;
        this.p2 = p2;

    }

    @Override
    public String toString() {
        return "ParticleCollision{" +
                "p1=" + p1.getId() +
                ", p2=" + p2.getId() +
                ", time=" + this.getTime() +
                '}';
    }

    @Override
    public Set<Particle> getParticles() {
        return Stream.of(p1,p2).collect(Collectors.toSet());
    }

    @Override
    public boolean contains(Particle p) {
        return p1.equals(p) || p2.equals(p);
    }
}
