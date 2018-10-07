package ar.edu.itba.ss;

import ar.edu.itba.ss.CellIndex.Engine;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Beeman implements Integrator {

    private ForceCalculator forceCalculator;
    private double dt;


    public Beeman(ForceCalculator forceCalculator, double dt) {
        this.forceCalculator = forceCalculator;
        this.dt = dt;
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
        Map<Particle, Set<Particle>> neighbours = Engine.bruteForce(allParticles, 0,Particle::getPosition);

        for (Particle p : allParticles) {
            Vector acceleration = forceCalculator.calculate(p, neighbours.get(p), Particle::getPosition,Particle::getSpeed)
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

            double nextPx = pos.x + sp.x * dt + 2.0 / 3.0 * ac.x * dt * dt - 1.0 / 6.0 * prAc.x * dt * dt;
            double nextPy = pos.y + sp.y * dt + 2.0 / 3.0 * ac.y * dt * dt - 1.0 / 6.0 * prAc.y * dt * dt;
            if(p.getId()==10) {
                System.out.println("Acceleration :" + ac);
                System.out.println("Next position :" + nextPx + " , " + nextPy);
            }

            p.setNextPosition(Vector.of(nextPx, nextPy));
        }
    }

    private void calculateNextSpeedPredicted(Set<Particle> allParticles) {
        for (Particle p : allParticles) {
            Vector sp = p.getSpeed();
            Vector ac = p.getAcceleration();
            Vector prAc = p.getPreviousAcc();

            double nextVx = sp.x + 3.0 / 2.0 * ac.x * dt - 1.0 / 2.0 * prAc.x * dt;
            double nextVy = sp.y + 3.0 / 2.0 * ac.y * dt - 1.0 / 2.0 * prAc.y * dt;


            p.setNextSpeedPredicted(Vector.of(nextVx, nextVy));
        }

    }

    private void calculateNextAcceleration(Set<Particle> allParticles) {

        Map<Particle, Set<Particle>> neighbours = Engine.bruteForce(allParticles, 0,Particle::getNextPosition);
        for (Particle p : allParticles) {
            Vector acceleration = forceCalculator.calculate(p, neighbours.get(p),Particle::getNextPosition,Particle::getNextSpeedPredicted)
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

            double nextVx = sp.x + 1.0 / 3.0 * neAc.x * dt + 5.0 / 6.0 * ac.x * dt - 1.0 / 6.0 * prAc.x * dt;
            double nextVy = sp.y + 1.0 / 3.0 * neAc.y * dt + 5.0 / 6.0 * ac.y * dt - 1.0 / 6.0 * prAc.y * dt;


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
