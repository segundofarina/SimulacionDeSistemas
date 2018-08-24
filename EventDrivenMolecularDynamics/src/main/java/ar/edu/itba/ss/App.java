package ar.edu.itba.ss;

import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }



    public double nextParticleCrash(Particle particle, Set<Particle> others){
        double min=Double.MAX_VALUE;
        Particle last =null;

        if(particle == null)
        for(Particle other : others){
            last=other;
            double time = claculateTime();
            if(time<min){
                min = time;
            }
        }
        others.stream().min((part));
        return nextParticleCrash(last,others.remove(last));
    }
}

 class Particle{

}
