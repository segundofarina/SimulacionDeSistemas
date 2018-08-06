package ar.edu.itba.ss;

import java.io.*;
import java.util.*;

public class Parser {
    private  BufferedReader staticBuffer;
    private  BufferedReader dynamicBuffer;
    int n;
    int l;
    int m;
    double rc;
    List<Molecule> molecules;
    int time;

    public Parser(String s, String d) {

        File staticFile = new File(s);
        File dinamicFile = new File(d);
        time=0;
        molecules = new ArrayList<>();

        String staticString ="11\n" +
                "12\n" +
                "3\n" +
                "3\n" +
                "1 pr1 \n" +
                "1 pr2\n" +
                "1 pr3 \n" +
                "1 pr4\n" +
                "1 pr5 \n" +
                "1 pr6\n" +
                "1 pr7 \n" +
                "1 pr8\n" +
                "1 pr9 \n" +
                "1 pr10\n" +
                "1 pr11";
        String dynamicString = "t0\n" +
                "1 1 0 0\n" +
                "4 5 0 0\n" +
                "6 0 0 0\n" +
                "6 7 0 0\n" +
                "8 9 0 0\n" +
                "9 2 0 0\n" +
                "9 5 0 0\n" +
                "10 8 0 0";

//        try {
//            staticBuffer = new BufferedReader(new FileReader(staticFile));
//            dinamicBuffer = new BufferedReader(new FileReader(dinamicFile));
            staticBuffer = new BufferedReader(new StringReader(staticString));
            dynamicBuffer = new BufferedReader(new StringReader(dynamicString));

//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//
//        }

        try {
            n = Integer.parseInt(staticBuffer.readLine());
            l = Integer.parseInt(staticBuffer.readLine());
            m = Integer.parseInt(staticBuffer.readLine());
            rc = Double.parseDouble(staticBuffer.readLine());

            for (int i=0; i<n; i++){
               String[] array= staticBuffer.readLine().split(" ");
               molecules.add(new Molecule(Integer.parseInt(array[0]),new Property<String>(array[1]),null,null));
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("N:"+n+" L:"+l+" m:"+m+" rc:"+rc);


    }

    public int getN() {
        return n;
    }

    public int getL() { return l; }

    public int getM() {
        return m;
    }

    public double getRc() {
        return rc;
    }

    public Set<Molecule> getMolecules() {
    Set<Molecule> response = new HashSet<>();

        try{
            System.out.println(dynamicBuffer.readLine());
            for(int i =0; i<n ; i++){
                String line = dynamicBuffer.readLine();
                String[] data = line.split(" ");
                Molecule current = molecules.get(i);
                double x,y,vx,vy;
                x=Double.parseDouble(data[0]);
                y=Double.parseDouble(data[1]);
                vx=Double.parseDouble(data[2]);
                vy=Double.parseDouble(data[3]);
                response.add(new Molecule(current.getId(),current.getRatio(),null,new Point(x,y), new Point(vx,vy)));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return response;
//        Set<Molecule> molecules = new HashSet<>();
//        int ratio;
//        double x, y, vx, vy;
//        Property<String> property;
//        Point location;
//        Point velocity;
//        Molecule molecule;
//        int n = Integer.parseInt(staticFile[0]);
//        dinamicFile.next();
//
//        for (int i = 5; i < n; i += 2) {
//            ratio = Integer.parseInt(staticFile[i]);
//            property = new Property<>(staticFile[i + 1]);
//            x = Double.parseDouble(dinamicFile.next());
//            y = Double.parseDouble(dinamicFile.next());
//            vx = Double.parseDouble(dinamicFile.next());
//            vy = Double.parseDouble(dinamicFile.next());
//            location = new Point(x, y);
//            velocity = new Point(vx, vy);
//            molecule = new Molecule(ratio, property, location, velocity);
//            molecules.add(molecule);
//        }
//
//        return molecules;
    }
}