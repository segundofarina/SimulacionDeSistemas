package ar.edu.itba.ss;



import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Set;

public class ForceCalculator {
    private static final double G= 6.993*Math.pow(10,-11);

    public static Point2D getForceAppliedTo(Body p, Collection<Body> others){
        double x=0;
        double y=0;
        for(Body other:others){
            if(!p.equals(other)){
                double force= getForce(p,other);
                x+=getForceX(force,p,other);
                y+=getForceY(force,p,other);
            }
        }
        return new Point2D.Double(x,y);
    }

    private static double getForce(Body pi, Body pj){
        return G* pi.getMass()*pj.getMass()/Math.pow(distance(pi,pj),2);
    }

    private static double distance(Body pi, Body pj) {
        double diffX= pj.getX()-pi.getX();
        double diffY=pj.getY()-pi.getY();
        return Math.sqrt(diffX*diffX+diffY*diffY);
    }

    private static double getForceX(double force, Body pi, Body pj){
        double diffX= pj.getX()-pi.getX();
        return force * diffX/distance(pi, pj);
    }


    private static double getForceY(double force, Body pi, Body pj){
        double diffY= pj.getY()-pi.getY();
        return force * diffY/distance(pi, pj);
    }


}
