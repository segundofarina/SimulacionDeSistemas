package ar.edu.itba.ss;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.*;

public class Engine {

    private Grid grid;

    private Map<Molecule,Set<Molecule>> neighbors;
    int m;

    public Engine(int l, int n, int m, double rc,boolean periodic, Set<Molecule> molecules) {
        this.m=m;
        grid = new Grid(l,n,m,rc,molecules,periodic);
        neighbors = new HashMap<>();
    }

    public void getNeighborsOfMolecule(){
        for(int i=0;i<m;i++){
            for(int j=0 ; j<m ;j++){
                Map<Molecule,Set<Molecule>> map = grid.analyzeCell(new Point(i,j));
                for(Molecule molecule : map.keySet()) {


                    if (neighbors.containsKey(molecule)) {
                        neighbors.get(molecule).addAll(map.get(molecule));
                    } else {
                        neighbors.put(molecule, map.get(molecule));
                    }

                    for (Molecule m : map.get(molecule)) {
                        if (neighbors.containsKey(m)) {
                            neighbors.get(m).add(molecule);
                        } else {
                            HashSet<Molecule> set = new HashSet<>();
                            set.add(molecule);
                            neighbors.put(m, set);
                        }
                    }
                }
            }
        }

    }

    public Map<Molecule,Set<Molecule>> start(){
            getNeighborsOfMolecule();
        return neighbors;
    }

    public static void writeToFile(String data, int inedx, String path){
        try {
            Files.write(Paths.get(path + "/results" + inedx + ".txt"), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String generateFileString(Molecule molecule, Set<Molecule> neighbours,Set<Molecule> allMolcules){
        StringBuilder builder = new StringBuilder()
                .append(allMolcules.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t R\t G\t B\t\r\n");
        for(Molecule current: allMolcules){
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getLocation().getX())
                    .append(" ")
                    .append(current.getLocation().getY())
                    .append(" ")
                    .append(current.getRatio())
                    .append(" ");
            if(molecule.getId() == current.getId()){
                builder.append("1 0 0\r\n");
            }else if(neighbours.contains(current)){
                builder.append("0 1 0\r\n");
            }else{
                builder.append("1 1 1\r\n");
            }
        }
        return builder.toString();
    }

    public Map<Molecule,Set<Molecule>> bruteForce(Set<Molecule> molecules){
        Map<Molecule,Set<Molecule>> ans = new HashMap<>();

        for(Molecule m1: molecules){
            ans.put(m1,new HashSet<Molecule>());
            for(Molecule m2 : molecules){
                if(Molecule.distanceBetweenMolecules(m1,m2)-m1.getRatio()-m2.getRatio()<=grid.getRc()){
                    ans.get(m1).add(m2);
                }
            }
        }

        return ans;
    }



}
