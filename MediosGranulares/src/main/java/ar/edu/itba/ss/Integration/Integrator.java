package ar.edu.itba.ss;


import java.awt.*;
import java.util.Set;

public interface Integrator {

    public Set<Particle> integrate(Set<Particle> allParticles);

}
