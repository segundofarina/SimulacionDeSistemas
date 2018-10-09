package ar.edu.itba.ss;

public class App {
    public static void main(String args[]) {
        Silo silo = new Silo(2, 0.5, 0.1);
        String segundo= "/Users/segundofarina/TP/TP-SS/out/MediosGranulares";
        String martin = "/Users/martin/Documents/ITBA/SimulacionDeSistemas/out/MediosGranulares";
        silo.start(segundo,1000);
    }
}
