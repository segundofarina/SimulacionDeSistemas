package ar.edu.itba.ss;

public class App {

    public static void main(String args[]) {

        String outPath = "/Users/segundofarina/TP/TP-SS/out/Oscilador/";

        for(int i=1 ; i<=6; i++) {

            double dTime = Math.pow(10,-i);
            Simulation s = new Simulation(dTime);

            s.startVerlet(outPath);

            System.out.println("Done");

            Simulation s2 = new Simulation(dTime);

            s2.startBeeman(outPath);

            System.out.println("Done");

            Simulation s3 = new Simulation(dTime);

            s3.startGearPredictor(outPath);

            System.out.println("Done");

            Simulation s4 = new Simulation(dTime);

            s4.startRealSolution(outPath);
        }

    }

}
