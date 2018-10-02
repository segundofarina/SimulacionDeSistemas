package ar.edu.itba.ss;


import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static ar.edu.itba.ss.Printer.*;


public class Simulation {

    Map<String,Body> bodies;

    Body voyager;

    double dt;


    public Simulation(double dt, double L,double v0){
        loadPlanets();
        voyager=new Body(bodies.size(),0,0,0,0,721.9,100,"Voyager");
        loadVoyagerPosition(voyager, bodies.get("Earth"),L);
        loadVoyagerVelocity(voyager, bodies.get("Earth"),v0);
        bodies.put(voyager.getName(),voyager);
        this.dt=dt;

    }

    public void start(String outPath){
        BufferedWriter bw=initalizeBW(outPath);
        long it=0;
        while (it<10000000){
            HashMap<String,Body> actual= cloneBodies();
            HashMap<String,Body> last = new HashMap<>();
            HashMap<String,Body> next = new HashMap<>();

            if(it%100==0){
                appendToFile(bw,generateFileString(bodies.values()));
            }
            for(Body b : bodies.values()){
                if(b.getId()!=0) {
                    Point2D force = ForceCalculator.getForceAppliedTo(b, actual.values());
                    double lastX = Euler.getLastPosition(b.getX(), b.getVx(), force.getX(), dt, b.getMass());
                    double lastY = Euler.getLastPosition(b.getY(), b.getVy(), force.getY(), dt, b.getMass());

                    double lastVX = Euler.getLastVelocity(b.getX(), b.getVx(), force.getX(), dt, b.getMass());
                    double lastVY = Euler.getLastVelocity(b.getY(), b.getVy(), force.getY(), dt, b.getMass());

                    last.put(b.getName(), new Body(b.getId(), lastX, lastY, lastVX, lastVY, b.getMass(), b.getRadius(), b.getName()));

                    double nextVX = Euler.getNextVelocity(b.getVx(), dt, force.getX(), b.getMass());
                    double nextVY = Euler.getNextVelocity(b.getVy(), dt, force.getY(), b.getMass());

                    double nextX = Euler.getNextPosition(b.getX(), dt, nextVX, force.getX(), b.getMass());
                    double nextY = Euler.getNextPosition(b.getY(), dt, nextVY, force.getY(), b.getMass());

                    next.put(b.getName(), new Body(b.getId(), nextX, nextY, nextVX, nextVY, b.getMass(), b.getRadius(), b.getName()));
                }else{
                    last.put(b.getName(), new Body(b.getId(), 0, 0, 0, 0, b.getMass(), b.getRadius(), b.getName()));
                    next.put(b.getName(), new Body(b.getId(), 0, 0, 0, 0, b.getMass(), b.getRadius(), b.getName()));
                }

            }

            for(Body b : bodies.values()){
                Point2D prevForce= ForceCalculator.getForceAppliedTo(last.get(b.getName()),last.values());
                Point2D force = ForceCalculator.getForceAppliedTo(b,actual.values());
                Point2D nextforce = ForceCalculator.getForceAppliedTo(b,actual.values());

                double nextX=Beeman.getNextPosition(b.getX(),b.getVx(),dt,force.getX()/b.getMass(),prevForce.getX()/b.getMass());
                double nextY=Beeman.getNextPosition(b.getY(),b.getVy(),dt,force.getY()/b.getMass(),prevForce.getY()/b.getMass());

                double nextVX=Beeman.getNextVelocity(b.getVx(),dt,force.getX()/b.getMass(),prevForce.getX()/b.getMass(),nextforce.getX()/b.getMass());
                double nextVY=Beeman.getNextVelocity(b.getVy(),dt,force.getY()/b.getMass(),prevForce.getY()/b.getMass(),nextforce.getY()/b.getMass());

                if(b.getId()!=0) {
                    b.setVx(nextVX);
                    b.setX(nextX);
                    b.setVy(nextVY);
                    b.setY(nextY);
                }
            }


            it++;

        }

        closeBW(bw);

    }

    private HashMap<String,Body> cloneBodies(){
        HashMap<String,Body> ans = new HashMap();
        for(Body b : bodies.values()){
            ans.put(b.getName(),new Body(b));
        }
        return ans;
    }

    private void loadPlanets() {
        bodies = new HashMap<>();
        bodies.put("Sun",new Body(0, 0,0,0,0,1988500E+24,696000*100*1.24,"Sun"));
        bodies.put("Earth",new Body(1,1.443040359985483E+11,-4.566821691926755E+10,8.429276455862507E+03,2.831601955976786E+04,5.97219E+24,6378.137*1000,"Earth"));
        bodies.put("Jupiter", new Body(2,1.061950341671551E+11,7.544955348409320E+11,-1.309157032053854E+04,2.424744678419164E+03,1898.13E+24,71492*1000,"Jupiter"));
        bodies.put( "Saturn",new Body(3,-1.075238877886715E+12,8.538222924091074E+11,-6.527515746018062E+03,-7.590526046562251E+03,5.6834E+26,60268*1000,"Saturn"));

    }

    private void loadVoyagerPosition(Body voyager, Body earth,double L){
        double angle = getEarthAngle(earth);
        voyager.setX(earth.getX()+L*Math.cos(angle));
        voyager.setY(earth.getY()+L*Math.sin(angle));
    }

    private void loadVoyagerVelocity(Body voyager, Body earth, double v0){
        double angle = getEarthAngle(earth);
        voyager.setVx(v0*Math.cos(Math.PI/2+angle));
        voyager.setVy(v0*Math.sin(Math.PI/2+angle));
    }

    private double getEarthAngle(Body earth){
        return Math.atan(earth.getY()/earth.getX());
    }




}
