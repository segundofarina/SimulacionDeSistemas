package ar.edu.itba.ss;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Simulation s = new Simulation(0.5, 500);

        //s.startForTime(200,"/Users/segundofarina/TP/TP-SS/out");
        //s.startUntilCrash("/Users/segundofarina/TP/TP-SS/out");
        s.startForAnimation(200,"/Users/segundofarina/TP/TP-SS/out");

//        Simulation.particlesCrash(new Particle(5,0.433172,0.404138,0.00166689,0.0553541,0.005,0.1),new Particle(2,0.433816,0.416097,-0.0576948,-0.0734142,0.005,0.1));
//
//        Simulation.particlesCrash(new Particle(5,0.43327,0.407378,0.00166689,0.0553541,0.005,0.1),new Particle(2,0.43044,0.411801,-0.0576948,-0.0734142,0.005,0.1));








    }
}
