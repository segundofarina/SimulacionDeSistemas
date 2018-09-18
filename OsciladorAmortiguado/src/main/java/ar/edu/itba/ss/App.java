package ar.edu.itba.ss;

public class App {

    public static void main(String args[]) {

        Simulation s = new Simulation(0.01);

        s.startVerlet("/Users/segundofarina/TP/TP-SS/out");

        System.out.println("Done");

        Simulation s2 = new Simulation(0.01);

        s2.startBeeman("/Users/segundofarina/TP/TP-SS/out");

    }

}
