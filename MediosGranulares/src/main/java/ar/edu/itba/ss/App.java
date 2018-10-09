package ar.edu.itba.ss;

public class App {
    public static void main(String args[]) {
        Silo silo = new Silo(25, 10, 0.1);
        silo.start("/Users/martin/Documents/ITBA/SimulacionDeSistemas/out/MediosGranulares",1000);
    }
}
