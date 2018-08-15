package ar.edu.itba.ss;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String [ ] args){
        System.out.println("Iteration\t0.0\t0.5\t1.0\t1.5\t2.0\t2.5\t3.0\t3.5\t4.0\t4.5\t5.0\t");
        for(int iteration=0 ; iteration<20;iteration++) {
            System.out.println();
            System.out.print(iteration+"\t");
            Parser parser = new Parser(args[0], args[1]);

            int L = parser.getL();
            int N = parser.getN();
            int M = parser.getM();
            double Rc = parser.getRc();
            boolean periodic = parser.isPeriodic();
            //double noise = 3;

            Set<Molecule> molecules = parser.getMolecules();

            Engine engine = new Engine(L, N, M, Rc, periodic, molecules);
            Map<Molecule, Set<Molecule>> moleculesNeighbours = engine.start();

            for (double noise = 0; noise <= 5; noise += 0.5) {
                final int maxT = 2000;
                long start = System.currentTimeMillis();
                boolean done = false;
                double va = 0;
                for (int i = 0; i < maxT && !done; i++) {

                    Set<Molecule> newMolecules = new HashSet<>();

                    for (Molecule m : moleculesNeighbours.keySet()) {
                        double newAngle = getAngleFromNeighbours(m, moleculesNeighbours.get(m), noise);
                        Point newLocation = getLocationFromAngle(m, newAngle, L);
                        Molecule newMolecule = new Molecule(m.getId(), m.getRatio(), null, newLocation, m.getVelocity(), newAngle);
                        newMolecules.add(newMolecule);
                    }

                    //System.out.println(getVa(newMolecules, N));
                    va = getVa(newMolecules);


                    engine = new Engine(L, N, M, Rc, periodic, newMolecules);
                    moleculesNeighbours = engine.start();
//                    String fileString = generateFileString(newMolecules);
//                    writeToFile(fileString, i, args[2]);

//            if(va > 0.999) {
//                done = true;
//            }
                }
                long end = System.currentTimeMillis();
                System.out.print( va+"\t");

                //System.out.print(" time: " + (end - start) + "ms.");
            }
        }

    }

    public static String generateFileString(Set<Molecule> allMolcules){

        StringBuilder builder = new StringBuilder()
                .append(allMolcules.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t R\t G\t B\t vx\t vy\t \r\n");
        for(Molecule current: allMolcules){
            double vx = current.getVelocity()*Math.cos(current.getAngle());
            double vy = current.getVelocity()*Math.sin(current.getAngle());
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getLocation().getX())
                    .append(" ")
                    .append(current.getLocation().getY())
                    .append(" ")
                    .append(current.getRatio())
                    .append(" ")
                    .append(getRGBDouuble(current.getAngle()))
                    .append(vx*1000)
                    .append(" ")
                    .append(vy*1000)
                    .append(" ")
                    .append(current.getAngle())
                    .append("\r\n");


        }
        return builder.toString();
    }

    public static void writeToFile(String data, int inedx, String path){
        try {
            Files.write(Paths.get(path + "/offLattice" + inedx + ".txt"), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Point getLocationFromAngle(Molecule m, double newAngle, int L) {

        double x = m.getLocation().getX()+m.getVelocity()*Math.cos(newAngle);
        double y = m.getLocation().getY()+m.getVelocity()*Math.sin(newAngle);
        x = x%L;
        y= y%L;

        if(y<0){
            y+=L;
        }
        if(x<0){
            x+=L;
        }
        return new Point(x,y);
    }

    private static double getAngleFromNeighbours(Molecule m, Set<Molecule> neighbours, double noise) {
        double sinTotal = Math.sin(m.getAngle());
        double cosTotal = Math.cos(m.getAngle());

        for(Molecule n : neighbours) {
            sinTotal += Math.sin(n.getAngle());
            cosTotal += Math.cos(n.getAngle());
        }
        double n= new Random().nextDouble()*noise-noise/2;
        return Math.atan2(sinTotal/(neighbours.size() + 1), cosTotal/(neighbours.size() + 1) )+n;

    }


//    private static String getRGB(double radius){
//        int r,g,b;
//        if(radius <Math.PI/3){
//            r=255;
//            g=(int)(radius/(Math.PI/3)*255);
//            b=0;
//        }else if( radius < Math.PI*2/3){
//            r=255-(int)((radius-Math.PI/3)/(Math.PI/3)*255);
//            g=255;
//            b=0;
//        }else if(radius < Math.PI){
//            r=0;
//            g=255;
//            b=(int)((radius-2*Math.PI/3)/(Math.PI/3)*255);
//
//        }else if(radius< Math.PI*4/3){
//            r=0;
//            g=255-(int)((radius-Math.PI)/(Math.PI/3)*255);
//            b=255;
//        }else if(radius < Math.PI*5/3){
//            r=(int)((radius-4*Math.PI/3)/(Math.PI/3)*255);
//            g=0;
//            b=255;
//        }else if(radius<= Math.PI*2){
//            r=255;
//            g=0;
//            b=255-(int)((radius-5*Math.PI/3)/(Math.PI/3)*255);
//        }else {
//            r=255;
//            g=255;
//            b=255;
//        }
//        return r+" "+g+" "+b+" ";
//    }

    private static String getRGBDouuble(double radius){
        while(radius<0){
            radius+=Math.PI*2;
        }
        double r,g,b;
        if(radius <Math.PI/3){
            r=1;
            g=(radius/(Math.PI/3));
            b=0;
        }else if( radius < Math.PI*2/3){
            r=1-((radius-Math.PI/3)/(Math.PI/3));
            g=1;
            b=0;
        }else if(radius < Math.PI){
            r=0;
            g=1;
            b=((radius-2*Math.PI/3)/(Math.PI/3));

        }else if(radius< Math.PI*4/3){
            r=0;
            g=1-((radius-Math.PI)/(Math.PI/3));
            b=255;
        }else if(radius < Math.PI*5/3){
            r=((radius-4*Math.PI/3)/(Math.PI/3));
            g=0;
            b=255;
        }else if(radius<= Math.PI*2){
            r=255;
            g=0;
            b=1-((radius-5*Math.PI/3)/(Math.PI/3));
        }else {
            r=1;
            g=1;
            b=1;
        }
        return r+" "+g+" "+b+" ";
    }

    private static double getVa(Set<Molecule> molecules) {
        double totalVx = 0;
        double totalVy = 0;
        double velocity = 0;

        for(Molecule m : molecules) {
            totalVx += m.getVelocity()*Math.cos(m.getAngle());
            totalVy += m.getVelocity()*Math.sin(m.getAngle());

            velocity = m.getVelocity();
        }

        totalVx /= molecules.size();
        totalVy /= molecules.size();

        double totalVi = Math.sqrt(Math.pow(totalVx, 2) + Math.pow(totalVy, 2));

        return totalVi / ( velocity);
    }
}
