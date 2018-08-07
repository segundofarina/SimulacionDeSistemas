package ar.edu.itba.ss;

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

    public Map<Molecule,Set<Molecule>> start(Set<Molecule> molecules){
            getNeighborsOfMolecule();
        return neighbors;
    }

    public Map<Molecule,Set<Molecule>> bruteForce(Set<Molecule> molecules){
        Map<Molecule,Set<Molecule>> ans = new HashMap<>();

        for(Molecule m1: molecules){
            ans.put(m1,new HashSet<Molecule>());
            for(Molecule m2 : molecules){
                if(Molecule.distanceBetweenMolecules(m1,m2)<=grid.getRc()){
                    ans.get(m1).add(m2);
                }
            }
        }

        return ans;
    }

    public static void main (String [ ] args) {

        Parser parser = new Parser(args[0],args[1]);

        int L = parser.getL();

        int N = parser.getN();

        int M = parser.getM();

        double Rc = parser.getRc();

        boolean periodic = parser.isPeriodic();

        System.out.println(periodic);

        Set<Molecule> molecules = parser.getMolecules();

        Engine engine = new Engine(L,N,M,Rc,periodic,molecules);

        Map<Molecule,Set<Molecule>> ans = engine.start(molecules);

        for(Map.Entry<Molecule,Set<Molecule>> a : ans.entrySet()){

            System.out.print(a.getKey().getId()+" : ");

            for(Molecule m: a.getValue()){
                System.out.print(m.getId()+" ");
            }

            System.out.println();
        }
    }
}
