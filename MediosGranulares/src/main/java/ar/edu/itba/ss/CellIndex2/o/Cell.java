package ar.edu.itba.ss.CellIndex2.o;

import ar.edu.itba.ss.Particle;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final int x;
    private final int y;
    private Set<Particle> particles;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;

        this.particles = new HashSet<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Set<Particle> getParticles() {
        return particles;
    }

    public void addParticle(Particle p) {
        particles.add(p);
    }

    public void clearParticles() {
        particles.clear();
    }
}
