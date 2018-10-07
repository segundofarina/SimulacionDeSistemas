package ar.edu.itba.ss;

import java.util.Set;

public class GearPredictorCorrector implements Integrator {





    public Vector integrate(double force, double dt,double pos, double vel, double mass) {
        double r0 = pos;
        double r1 = vel;
        double r2 = force / mass;
        double r3 = 0;
        double r4 = 0;
        double r5 = 0;


        double rp0 = r0 + r1 *dt + r2 *Math.pow(dt,2)/2 + r3 *Math.pow(dt,3)/6 + r4 *Math.pow(dt,4)/24 + r5 *Math.pow(dt,2)/120;
        double rp1 = r1 + r2 *dt + r3 *Math.pow(dt,2)/2 + r4 *Math.pow(dt,3)/6 + r4 *Math.pow(dt,4)/24;
        double rp2 = r2 + r3 *dt + r4 *Math.pow(dt,2)/2 + r5 *Math.pow(dt,3)/6;
        double rp3 = r3 + r4 *dt + r5 *Math.pow(dt,2)/2;
        double rp4 = r4 + r5 *dt;
        double rp5 = r5;



        return null;
    }


    public Vector integrate(double force, double dt) {
        return null;
    }

    @Override
    public Set<Particle> integrate(Set<Particle> allParticles) {
        return null;
    }
}
