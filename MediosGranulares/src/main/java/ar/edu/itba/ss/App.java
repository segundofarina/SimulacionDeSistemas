package ar.edu.itba.ss;

public class App {
    public static void main(String args[]) {
        double D = 0.3;
        Silo silo = new Silo(2, 0.5, D);
        String segundo= "/Users/segundofarina/TP/TP-SS/out";
        String martin = "/Users/martin/Documents/ITBA/SimulacionDeSistemas/out/MediosGranulares/MediosGranulares_D" + D;
        silo.start(martin,1000);
    }
}
