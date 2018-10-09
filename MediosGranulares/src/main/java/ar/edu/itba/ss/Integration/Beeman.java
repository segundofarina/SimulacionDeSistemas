package ar.edu.itba.ss.Integration;

import ar.edu.itba.ss.CellIndex.Engine;
import ar.edu.itba.ss.CellIndex2.o.NeighbourCalculator;
import ar.edu.itba.ss.ForceCalculator;
import ar.edu.itba.ss.Particle;
import ar.edu.itba.ss.Vector;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Beeman implements Integrator {

    private ForceCalculator forceCalculator;
    private double dt;
    private NeighbourCalculator neighbourCalculator;

    public Beeman(ForceCalculator forceCalculator, NeighbourCalculator neighbourCalculator, double dt) {
        this.forceCalculator = forceCalculator;
        this.dt = dt;
        this.neighbourCalculator =neighbourCalculator;
    }


    @Override
    public Set<Particle> integrate(Set<Particle> allParticles) {

        calculateAcceleration(allParticles);

        calculateNextPosition(allParticles);

        calculateNextSpeedPredicted(allParticles);

        calculateNextAcceleration(allParticles);

        calculateNextSpeedCorrected(allParticles);

        return getUpdatedParticles(allParticles);
    }

    private void calculateAcceleration(Set<Particle> allParticles) {
       // Map<Particle, Set<Particle>> neighbours = Engine.bruteForce(allParticles, 0, Particle::getPosition);
        Map<Particle, Set<Particle>> neighbours = neighbourCalculator.getNeighbours(allParticles,Particle::getPosition);
        for (Particle p : allParticles) {
            Vector acceleration = forceCalculator.calculate(p, neighbours.get(p), Particle::getPosition, Particle::getSpeed)
                    .dividedBy(p.getMass());
            p.setAcceleration(acceleration);
        }
    }

    private void calculateNextPosition(Set<Particle> allParticles) {
        for (Particle p : allParticles) {
            Vector pos = p.getPosition();
            Vector sp = p.getSpeed();
            Vector ac = p.getAcceleration();
            Vector prAc = p.getPreviousAcc();

            double nextPx = pos.getX() + sp.getX() * dt + 2.0 / 3.0 * ac.getX() * dt * dt - 1.0 / 6.0 * prAc.getX() * dt * dt;
            double nextPy = pos.getY() + sp.getY() * dt + 2.0 / 3.0 * ac.getY() * dt * dt - 1.0 / 6.0 * prAc.getY() * dt * dt;

            p.setNextPosition(Vector.of(nextPx, nextPy));
        }
    }

    private void calculateNextSpeedPredicted(Set<Particle> allParticles) {
        for (Particle p : allParticles) {
            Vector sp = p.getSpeed();
            Vector ac = p.getAcceleration();
            Vector prAc = p.getPreviousAcc();

            double nextVx = sp.getX() + 3.0 / 2.0 * ac.getX() * dt - 1.0 / 2.0 * prAc.getX() * dt;
            double nextVy = sp.getY() + 3.0 / 2.0 * ac.getY() * dt - 1.0 / 2.0 * prAc.getY() * dt;


            p.setNextSpeedPredicted(Vector.of(nextVx, nextVy));
        }

    }

    private void calculateNextAcceleration(Set<Particle> allParticles) {

        //Map<Particle, Set<Particle>> neighbours = Engine.bruteForce(allParticles, 0,Particle::getNextPosition);

        Map<Particle, Set<Particle>> neighbours = neighbourCalculator.getNeighbours(allParticles,Particle::getNextPosition);
        for (Particle p : allParticles) {
            Vector acceleration = forceCalculator.calculate(p, neighbours.get(p), Particle::getNextPosition,Particle:: getNextSpeedPredicted)
                    .dividedBy(p.getMass());
            p.setNextAcceleration(acceleration);
        }


    }


    private void calculateNextSpeedCorrected(Set<Particle> allParticles) {
        for (Particle p : allParticles) {
            Vector sp = p.getSpeed();
            Vector ac = p.getAcceleration();
            Vector prAc = p.getPreviousAcc();
            Vector neAc = p.getNextAcceleration();

            double nextVx = sp.getX() + 1.0 / 3.0 * neAc.getX() * dt + 5.0 / 6.0 * ac.getX() * dt - 1.0 / 6.0 * prAc.getX() * dt;
            double nextVy = sp.getY() + 1.0 / 3.0 * neAc.getY() * dt + 5.0 / 6.0 * ac.getY() * dt - 1.0 / 6.0 * prAc.getY() * dt;


            p.setNextSpeedCorrected(Vector.of(nextVx, nextVy));
        }

    }




    private Set<Particle> getUpdatedParticles(Set<Particle> allParticles) {
        Set<Particle> updatedParticles = new HashSet<>();

        for (Particle p : allParticles) {
            updatedParticles.add(Particle.of(p, p.getNextPosition(), p.getNextSpeedCorrected(), p.getAcceleration()));
        }
        return updatedParticles;
    }


}
