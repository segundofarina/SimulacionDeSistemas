package ar.edu.itba.ss;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Simulation s = new Simulation(0.5, 1000);

        s.start(100);
    }
}
