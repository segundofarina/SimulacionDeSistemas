package ar.edu.itba.ss;

public class App {
    public static void main(String args[]) {
        Silo silo = new Silo(2, 0.5, 0.15);
        String segundo= "/Users/segundofarina/TP/TP-SS/out";
        String martin = "/Users/martin/Documents/ITBA/SimulacionDeSistemas/out/MediosGranulares";
        silo.start(martin,1000);
    }
}
