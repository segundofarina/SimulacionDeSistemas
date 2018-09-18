package ar.edu.itba.ss;

public class App {

    public static void main(String args[]) {

        Simulation s = new Simulation(0.1);

        s.startVerlet();

        System.out.println("Done");

        Simulation s2 = new Simulation(0.1);

        s2.startBeeman();

        System.out.println("Done");

        Simulation s3 = new Simulation(0.1);

        s3.startGearPredictor();

        System.out.println("Done");

        Simulation s4 = new Simulation(0.1);

        s4.startRealSolution();

    }

}
