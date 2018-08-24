package ar.edu.itba.ss;

import java.util.Map;
import java.util.Set;

public class Main {

    //Sample in parameters
    // /Users/segundofarina/TP/TP-SS/TP/CellIndexMethod/target/classes/randStatic.txt /Users/segundofarina/TP/TP-SS/TP/CellIndexMethod/target/classes/randDyn.txt /Users/segundofarina/TP/TP-SS/out
    // /Users/martin/Documents/ITBA/SimulacionDeSistemas/TP1/SimulacionDeSistemas/CellIndexMethod/target/classes/staticfile.txt /Users/martin/Documents/ITBA/SimulacionDeSistemas/TP1/SimulacionDeSistemas/CellIndexMethod/target/classes/dynamicfile.txt /Users/martin/Documents/ITBA/SimulacionDeSistemas/TP1/SimulacionDeSistemas/out
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

        long start = System.currentTimeMillis();
        Map<Molecule,Set<Molecule>> ans = engine.start();
        //Map<Molecule,Set<Molecule>> ans = engine.bruteForce(molecules);
        long end = System.currentTimeMillis();

        System.out.println(end-start);

        for(Molecule molecule :ans.keySet()){
            String toWrite = Engine.generateFileString(molecule,ans.get(molecule),molecules);
            Engine.writeToFile(toWrite,molecule.getId(),args[2]);
        }

        for(Map.Entry<Molecule,Set<Molecule>> a : ans.entrySet()){

            System.out.print(a.getKey().getId()+" : ");

            for(Molecule m: a.getValue()){
                System.out.print(m.getId()+" ");
            }

            System.out.println();
        }
    }
}
