package ar.edu.itba.ss.CellIndex;

import ar.edu.itba.ss.Particle;
import ar.edu.itba.ss.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Engine {

    private Grid grid;

    private Map<Particle,Set<Particle>> neighbors;
    int m;

    public Engine(int l, int n, int m, double rc, boolean periodic, Set<Particle> molecules) {
        this.m=m;
        grid = new Grid(l,n,m,rc,molecules,periodic);
        neighbors = new HashMap<>();
    }

    public void getNeighborsOfMolecule(){
        for(int i=0;i<m;i++){
            for(int j=0 ; j<m ;j++){
                Map<Particle,Set<Particle>> map = grid.analyzeCell(new Vector(i,j));
                for(Particle molecule : map.keySet()) {


                    if (neighbors.containsKey(molecule)) {
                        neighbors.get(molecule).addAll(map.get(molecule));
                    } else {
                        neighbors.put(molecule, map.get(molecule));
                    }

                    for (Particle m : map.get(molecule)) {
                        if (neighbors.containsKey(m)) {
                            neighbors.get(m).add(molecule);
                        } else {
                            HashSet<Particle> set = new HashSet<>();
                            set.add(molecule);
                            neighbors.put(m, set);
                        }
                    }
                }
            }
        }

    }

    public Map<Particle,Set<Particle>> start(){
            getNeighborsOfMolecule();
        return neighbors;
    }


    public static Map<Particle,Set<Particle>> bruteForce(Set<Particle> molecules, double rc, Function<Particle,Vector> position){
        Map<Particle,Set<Particle>> ans = new HashMap<>();

        for(Particle m1: molecules){
            ans.put(m1,new HashSet<>());
            for(Particle m2 : molecules){
                if(position.apply(m1).distance(position.apply(m2)) <= rc + m1.getRadius() + m2.getRadius()){
                    ans.get(m1).add(m2);
                }
            }
        }

        return ans;
    }




}
