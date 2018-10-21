package ar.edu.itba.ss.Integration;

import ar.edu.itba.ss.Particle;

import java.util.Set;

public interface Integrator {

    public Set<Particle> integrate(Set<Particle> allParticles);

}
