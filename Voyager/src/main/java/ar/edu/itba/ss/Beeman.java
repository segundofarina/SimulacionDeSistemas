package ar.edu.itba.ss;

public class Beeman {

    public static double getNextPosition(double r,double v,double dt,double a, double preva){
        return r+v*dt+2.0/3.0*a*dt*dt-1.0/6*preva*dt*dt;
    }

    public static double getNextVelocity(double v,double dt,double a, double preva,double nexta){
        return v+1.0/3.0*nexta*dt+5.0/6.0*a*dt-1/6*preva*dt;
    }
}
