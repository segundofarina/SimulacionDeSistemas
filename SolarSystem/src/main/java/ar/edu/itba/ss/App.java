package ar.edu.itba.ss;

import java.awt.geom.Point2D;

public class App {
    public static void main(String[] args) {

        new Simulation(100, 1100, 15).start("/Users/martin/Documents/ITBA/SimulacionDeSistemas/out");
/*
        double minDistance = Double.POSITIVE_INFINITY;
        int minL = 0;
        int minV0 = 0;

        for(int L = 0; L < 10000; L += 10) {
            for(int v0 = 1; v0 < 20; v0++) {
                System.out.println("-- Simulation with L: " + L + " and v0: " + v0 + " --");
                Point2D distance = new Simulation(10, L, v0).start();

                if((distance.getX() + distance.getY()) / 2 < minDistance) {
                    minDistance = (distance.getX() + distance.getY()) / 2;
                    minL = L;
                    minV0 = v0;
                }
            }
        }

        System.out.println("Min L: " + minL);
        System.out.println("Min v0: " + minV0);
        System.out.println("Distance: " + minDistance);
*/
    }
}
