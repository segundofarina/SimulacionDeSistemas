//package ar.edu.itba.ss;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class Parser {
//    private BufferedReader buffer;
//    int n;
//    int l;
//    int m;
//    double rc;
//    List<Molecule> molecules;
//    int time;
//    boolean periodic;
//
//    public Parser(String s, String d) {
//
//        File staticFile = new File(s);
//        File dinamicFile = new File(d);
//        time=0;
//        molecules = new ArrayList<>();
//
//        try {
//            buffer = new BufferedReader(new FileReader(staticFile));
//            dynamicBuffer = new BufferedReader(new FileReader(dinamicFile));
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//
//        }
//
//        try {
//            n = Integer.parseInt(staticBuffer.readLine());
//            l = Integer.parseInt(staticBuffer.readLine());
//            m = Integer.parseInt(staticBuffer.readLine());
//            rc = Double.parseDouble(staticBuffer.readLine());
//            periodic = staticBuffer.readLine().equals("periodic");
//
//            for (int i=0; i<n; i++){
//                String[] array= staticBuffer.readLine().split(" ");
//                molecules.add(new Molecule(Double.parseDouble(array[0]),new Property<String>(array[1]),null,null));
//            }
//            staticBuffer.close();
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        System.out.println("N:"+n+" L:"+l+" m:"+m+" rc:"+rc);
//
//
//    }
//
//    public int getN() {
//        return n;
//    }
//
//    public int getL() { return l; }
//
//    public int getM() {
//        return m;
//    }
//
//    public double getRc() {
//        return rc;
//    }
//
//    public Set<Molecule> getMolecules() {
//        Set<Molecule> response = new HashSet<>();
//
//        try{
//            if (!dynamicBuffer.readLine().equals("t"+time)){
//                throw new IllegalArgumentException();
//            }
//            for(int i =0; i<n ; i++){
//                String line = dynamicBuffer.readLine();
//                String[] data = line.split(" ");
//                Molecule current = molecules.get(i);
//                double x,y,vx,vy;
//                x=Double.parseDouble(data[0]);
//                y=Double.parseDouble(data[1]);
//                vx=Double.parseDouble(data[2]);
//                vy=Double.parseDouble(data[3]);
//                response.add(new Molecule(current.getId(),current.getRatio(),null,new Point(x,y), new Point(vx,vy)));
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return response;
//    }
//
//    public boolean isPeriodic() {
//        return periodic;
//    }
//}
