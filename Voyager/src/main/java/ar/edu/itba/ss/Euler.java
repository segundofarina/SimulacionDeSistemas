package ar.edu.itba.ss;

import java.awt.geom.Point2D;

public class Euler {


    public static double getNextVelocity(double v,double dt,double force, double mass){
        return v+dt/mass*force;
    }
    public static double getNextPosition(double r ,double dt,double updatedVelocity,double force, double mass){
        return r+dt*updatedVelocity+dt*dt*force/(2*mass);
    }

    public static double getLastPosition(double r, double v,double f,double dTime,double mass){
        return  r + v * (-dTime) + ( Math.pow(dTime, 2) / (2 * mass) ) * f;
    }
    public static double getLastVelocity(double r, double v,double f,double dTime,double mass){
        return  v + (-dTime / mass) * f;
    }




}
