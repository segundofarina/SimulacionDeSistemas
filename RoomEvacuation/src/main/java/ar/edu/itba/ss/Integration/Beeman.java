package ar.edu.itba.ss.Integration;


import ar.edu.itba.ss.*;
import ar.edu.itba.ss.Vector;

import java.util.*;

@SuppressWarnings("Duplicates")
public class Beeman implements Integrator {

    private final SocialForce socialForce;
    private final GranularForce granularForce;
    private final DrivingForce drivingForce;
    private final InteractionForce interactionForce;
    private final double dt;
   // private NeighbourCalculator neighbourCalculator;
    private Map<Particle, Set<Particle>> neighbours;

    public Beeman( GranularForce granularForce,SocialForce socialForce,InteractionForce interactionForce, DrivingForce drivingForce, double dt, Set<Particle> allparticles) {
        this.granularForce = granularForce;
        this.socialForce   = socialForce;
        this.drivingForce  = drivingForce;
        this.interactionForce = interactionForce;
        this.dt = dt;
        //this.neighbourCalculator =neighbourCalculator;
        initializeNeighbours(allparticles);
    }

    private void initializeNeighbours(Set<Particle> allparticles) {
        neighbours = new HashMap<>();
        for(Particle p : allparticles){
            neighbours.put(p, Collections.emptySet());
        }

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
        //Map<Particle, Set<Particle>> neighbours = neighbourCalculator.getNeighbours(allParticles,Particle::getPosition);
        //TODO calculate neighbours
        for (Particle p : allParticles) {
//            Vector force = granularForce.calculate(p, /*neighbours.get(p)*/allParticles, Particle::getPosition, Particle::getSpeed)
//                    .add(socialForce.calculate(p, /*neighbours.get(p)*/allParticles, Particle::getPosition, Particle::getSpeed))
            Vector force = interactionForce.calculate(p, allParticles, Particle::getPosition, Particle::getSpeed)
                    .add(drivingForce.calculate(p,Particle::getPosition, Particle::getSpeed));

            Vector acceleration = force.dividedBy(p.getMass());
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

        //TODO calculate neighbours
        for (Particle p : allParticles) {
            //Vector force = granularForce.calculate(p, /*neighbours.get(p)*/allParticles, Particle::getNextPosition, Particle::getNextSpeedPredicted)
            Vector force = interactionForce.calculate(p, /*neighbours.get(p)*/allParticles, Particle::getNextPosition, Particle::getNextSpeedPredicted)
                    //.add(socialForce.calculate(p, /*neighbours.get(p)*/allParticles, Particle::getNextPosition, Particle::getNextSpeedPredicted))
                    .add(drivingForce.calculate(p,Particle::getNextPosition, Particle::getNextSpeedPredicted));
            Vector acceleration = force.dividedBy(p.getMass());
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
            Particle newP =Particle.of(p, p.getNextPosition(), p.getNextSpeedCorrected(), p.getAcceleration());
            newP.setTotalFn(p.getTotalFn());
            updatedParticles.add(newP);

        }
        return updatedParticles;
    }


}
