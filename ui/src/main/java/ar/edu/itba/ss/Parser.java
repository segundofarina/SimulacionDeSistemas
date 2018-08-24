package ar.edu.itba.ss;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    private BufferedReader buffer;
    int n;
    List<Molecule> molecules;


    public Parser() {

        File file = new File("/Users/segundofarina/TP/TP-SS/TP/ui/target/classes/data.txt");
        molecules = new ArrayList<>();

        try {
            buffer = new BufferedReader(new FileReader(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        try {
            n = Integer.parseInt(buffer.readLine());

            for (int i=0; i<n; i++) {
                String[] array = buffer.readLine().split(" ");
                int id = Integer.parseInt(array[0]);
                Point location = new Point(Integer.parseInt(array[1]), Integer.parseInt(array[2]));
                double radius = Double.parseDouble(array[3]);
                List<Integer> neighbors = new ArrayList<>();
                for (int j = 4; j < array.length; j++) {
                    neighbors.add(Integer.parseInt(array[j]));
                }
                molecules.add(new Molecule(location, id, radius, neighbors));
                System.out.println(location+" "+ id+" "+radius+" "+neighbors);
            }
            buffer.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("N:"+n);


    }

    public List<Molecule> getMolecules() {
        return molecules;
    }
}
