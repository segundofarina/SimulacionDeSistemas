package ar.edu.itba.ss;

public class App {

    public static void main(String args[]) {

        Simulation s = new Simulation(0.1);

        s.startVerlet();

        System.out.println("Done");

        Simulation s2 = new Simulation(0.1);

        s2.startBeeman();

    }

}
