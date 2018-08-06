package ar.edu.itba.ss;

import java.util.*;

public class Engine {

    private Grid grid;

    private Map<Molecule,Set<Molecule>> neighbors;

    public Engine(int l, int n, int m, double rc, Set<Molecule> molecules) {
        grid = new Grid(l,n,m,rc,molecules);
        neighbors = new HashMap<>();
    }

    public void getNeighborsOfMolecule(Molecule molecule){
        Set<Molecule> ans = grid.getNeighborsOfMolecule(molecule);

        if(neighbors.containsKey(molecule)){
            neighbors.get(molecule).addAll(ans);
        }else{
            neighbors.put(molecule,ans);
        }

        for(Molecule m : ans){
            if(neighbors.containsKey(m)){
                neighbors.get(m).add(molecule);
            }else{
                HashSet<Molecule> set = new HashSet<Molecule>();
                set.add(molecule);
                neighbors.put(m,set);
            }
        }
    }

    public Map<Molecule,Set<Molecule>> start(Set<Molecule> molecules){
        for(Molecule m: molecules){
            getNeighborsOfMolecule(m);
        }
        return neighbors;
    }

    public static void main (String [ ] args) {

        Scanner scan = new Scanner(System.in);

        Parser parser = new Parser(args,scan);

        int L = parser.parseL();

        int N = parser.parseN();

        int M = parser.parseM();

        double Rc = parser.parseRc();

        Set<Molecule> molecules = parser.parseMolecules();

        Engine engine = new Engine(L,N,M,Rc,molecules);

//            Set<Molecule> molecules = new HashSet<>();
//
//            Molecule molecule1 = new Molecule(1,null,new Point(1,2),null);
//            molecules.add(molecule1);
//            molecules.add(new Molecule(1,null,new Point(1,5),null));
//            molecules.add(new Molecule(1,null,new Point(1,11),null));
//            molecules.add(new Molecule(1,null,new Point(4,5),null));
//            molecules.add(new Molecule(1,null,new Point(5,9),null));
//            molecules.add(new Molecule(1,null,new Point(6,0),null));
//            molecules.add(new Molecule(1,null,new Point(6,7),null));
//            molecules.add(new Molecule(1,null,new Point(8,9),null));
//            molecules.add(new Molecule(1,null,new Point(9,2),null));
//            molecules.add(new Molecule(1,null,new Point(9,5),null));
//            molecules.add(new Molecule(1,null,new Point(10,8),null));
//
//            Engine engine = new Engine(12,11,3,3,molecules);
//
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
