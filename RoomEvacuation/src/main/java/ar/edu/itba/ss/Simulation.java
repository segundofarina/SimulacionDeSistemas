package ar.edu.itba.ss;

import ar.edu.itba.ss.Integration.Beeman;
import ar.edu.itba.ss.Integration.Integrator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Simulation {

    //Granular Force parameters
    private final double kn;
    private final double kt;
    private final double g;

    //Driving force parameters
    private final double tau;

    //Social force Parameters
    private final double A;
    private final double B;

    //Room Dimensions
    private final double L;
    private final double W;
    private final double door;

    //Pedestrians parameters
    private final double minRadiusPed;
    private final double maxRadiusPed;
    private final double maxSpeed;
    private final double minSpeed;
    private final double mass;

    //Simuulation parameters
    private final double dt;
    private double evacuated;
    private Set<Particle> pedestrians;


    //Default parameters
    public Simulation(){
        this.kn  = 1.2*Math.pow(10,5);
        this.kt  =  2*kn;
        this.g   = 9.8;

        this.tau = 0.5;

        this.A   = 2000;
        this.B   = 0.08;

        this.L   = 20;
        this.W   = 20;
        this.door= 1.2;

        this.minRadiusPed = 0.25;
        this.maxRadiusPed = 0.29;
        this.maxSpeed     = 6;
        this.minSpeed     = 0.8;

        //TODO check mass parameter
        this.mass         = 70;

        this.dt = 0.1 * Math.sqrt(mass/kn);

        System.out.println("Dt is: "+dt);
    }

    public void start(int pedestriansAmount){
        double time=0;
        double FPS = 60;
        double jump = 1/FPS;
        double nextTime = 0;
        double evacuated = 0;
        initializePedestrians(pedestriansAmount);


        SocialForce socialForce = new SocialForce(A,B);
        GranularForce granularForce = new GranularForce(kn,kt,g);
        DrivingForce drivingForce = new DrivingForce(tau);
        Printer animationPrinter = new Printer("./out/RoomEvacuation/animation_",L,W,door);
        Integrator integrator = new Beeman(granularForce,socialForce,drivingForce,dt,pedestrians);
        animationPrinter.appendToAnimation(pedestrians);
        while(evacuated< pedestriansAmount && time <1){
            this.pedestrians = integrator.integrate(pedestrians);

            if(time>nextTime){
                animationPrinter.appendToAnimation(pedestrians);
                nextTime+=jump;
            }

            time+=dt;
        }


    }

    private void initializePedestrians(int pedestriansAmount){

        Random rand = new Random();
        pedestrians = new HashSet<>(pedestriansAmount);

        //TODO check if target should be outside the room
        Vector target = Vector.of(W/2,0);

        System.out.println("Initalizing pedestrians...");

        while (pedestrians.size()<pedestriansAmount){
            double radius = rand.nextDouble()*(maxRadiusPed- minRadiusPed)+ minRadiusPed;

            double x = rand.nextDouble()*(W-2*radius)+radius;
            double y = rand.nextDouble()*(L-2*radius)+radius;

            double desiredSpeed = rand.nextDouble()*(maxSpeed-minSpeed)+maxSpeed;

            Particle ped = new Particle(pedestrians.size(),Vector.of(x,y),Vector.ZERO,radius,mass,desiredSpeed,target);
            if(!isOverlapingOtherParticle(ped,pedestrians)){
                pedestrians.add(ped);
            }
        }

        System.out.println(pedestriansAmount+" pedestrians added to simulation.");
    }

    private boolean isOverlapingOtherParticle(Particle p, Set<Particle> newParticles) {
        for(Particle other : newParticles) {
            if(p.overlaps(other)) {
                return true;
            }
        }
        return false;
    }
}
