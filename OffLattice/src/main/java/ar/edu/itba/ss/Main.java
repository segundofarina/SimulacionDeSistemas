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
//        for(int iteration=1;iteration<=50;iteration++) {
        //System.out.println("Iteration\t0.0\t0.5\t1.0\t1.5\t2.0\t2.5\t3.0\t3.5\t4.0\t4.5\t5.0\t");
            //System.out.println();
           // System.out.print(iteration+"\t");
//            startSimulationForResults(20,1200,1.5);
//        }
//        startRandomSimulation(7,147,0.1,args[2]);
        startSimulationWritingFiles(args[0],args[1],args[2]);
        }

        public static void startSimulationForResults(int L, int N, double noise){
        int M = L;
        double Rc = 1;
            Set<Molecule> molecules = randomMolecules(L,N);


            Engine engine = new Engine(L, N, M, Rc, true, molecules);
            Map<Molecule, Set<Molecule>> moleculesNeighbours = engine.start();

            final int maxT = 2000;
            long start = System.currentTimeMillis();
            double va = 0;
            for (int i = 0; i < maxT ; i++) {

                Set<Molecule> newMolecules = new HashSet<>();

                getNewMolecules(L, noise, moleculesNeighbours, newMolecules);

                va = getVa(newMolecules);


                engine = new Engine(L, N, M, Rc, true, newMolecules);
                moleculesNeighbours = engine.start();


            }
                long end = System.currentTimeMillis();
                System.out.println(new Double(va).toString().replace('.',',') + "\t");

                //System.out.println(" time: " + (end - start) + "ms.");
        }

    private static void getNewMolecules(int L, double noise, Map<Molecule, Set<Molecule>> moleculesNeighbours, Set<Molecule> newMolecules) {
        for (Molecule m : moleculesNeighbours.keySet()) {
            double newAngle = getAngleFromNeighbours(m, moleculesNeighbours.get(m), noise);
            Point newLocation = getLocationFromAngle(m, newAngle, L);
            Molecule newMolecule = new Molecule(m.getId(), m.getRatio(), null, newLocation, m.getVelocity(), newAngle);
            newMolecules.add(newMolecule);
        }
    }


    public static void startRandomSimulation(int L, int N, double noise,String outPath){
        int M = L;
        double Rc = 1;
        Set<Molecule> molecules = randomMolecules(L,N);


        Engine engine = new Engine(L, N, M, Rc, true, molecules);
        Map<Molecule, Set<Molecule>> moleculesNeighbours = engine.start();

        final int maxT = 2000;
        long start = System.currentTimeMillis();
        boolean done = false;
        double va = 0;
        for (int i = 0; i < maxT && !done; i++) {

            Set<Molecule> newMolecules = new HashSet<>();

            getNewMolecules(L, noise, moleculesNeighbours, newMolecules);

            va = getVa(newMolecules);


            engine = new Engine(L, N, M, Rc, true, newMolecules);
            moleculesNeighbours = engine.start();

            String fileString = generateFileString(newMolecules);
            writeToFile(fileString, i, outPath);

            if(va > 0.999) {
                done = true;
            }


        }
        long end = System.currentTimeMillis();
        System.out.print(va + "\t");

        System.out.println(" time: " + (end - start) + "ms.");
    }

    public static Set<Molecule> randomMolecules(int L, int N){
        Random random= new Random();
        Set<Molecule> molecules = new HashSet<Molecule>();
            for(int i=0; i<N; i++){
                double x = random.nextDouble()*L;
                double y = random.nextDouble()*L;
                double velocity = 0.03;
                double angle = random.nextDouble()*2*Math.PI;
                molecules.add(new Molecule(i,0,null, new Point(x,y),velocity,angle));
            }
            return molecules;
        }

    public static void startSimulationWritingFiles(String staticPath, String dynamicPath, String outPath) {
        Parser parser = new Parser(staticPath, dynamicPath);

        int L = parser.getL();
        int N = parser.getN();
        int M = parser.getM();
        double Rc = parser.getRc();
        boolean periodic = parser.isPeriodic();
        double noise = 2;

        Set<Molecule> molecules = parser.getMolecules();

        Engine engine = new Engine(L, N, M, Rc, periodic, molecules);
        Map<Molecule, Set<Molecule>> moleculesNeighbours = engine.start();

            final int maxT = 4000;
            long start = System.currentTimeMillis();
            boolean done = false;
            double va = 0;
            for (int i = 0; i < maxT && !done; i++) {

                Set<Molecule> newMolecules = new HashSet<>();

                getNewMolecules(L, noise, moleculesNeighbours, newMolecules);
                va = getVa(newMolecules);


                engine = new Engine(L, N, M, Rc, periodic, newMolecules);
                moleculesNeighbours = engine.start();
                String fileString = generateFileString(newMolecules);
                writeToFile(fileString, i, outPath);

            if(va > 0.999) {
                done = true;
            }

            long end = System.currentTimeMillis();
            System.out.print(va + "\t");

            System.out.println(" time: " + (end - start) + "ms.");
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
                    .append(getRGBDouble(current.getAngle()))
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




    private static String getRGBDouble(double radius){
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

    private static double getVa (Set<Molecule> molecules) {
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
