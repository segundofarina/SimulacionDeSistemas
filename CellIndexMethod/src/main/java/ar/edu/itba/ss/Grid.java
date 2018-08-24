package ar.edu.itba.ss;

import java.util.*;

public class Grid {
    private int l;
    private int m;
    private double rc;
    private double fieldSize;
    private HashMap<Point,Set<Molecule>> grid;
    private boolean periodic;

    public Grid(int l, int n, int m, double rc, Set<Molecule> molecules,boolean periodic) {
        this.l = l;
        this.m = m;
        this.rc = rc;
        this.fieldSize = (double)l/m;
        this.grid = new HashMap<>();
        this.periodic = periodic;

        for(int i = 0; i < m ; i++){
            for(int j = 0 ; j < m ; j++) {
                grid.put(new Point(i, j), new HashSet<Molecule>());
            }
        }

        for(Molecule molecule : molecules){
            Point j = getField(molecule.getLocation());
            try {
                grid.get(j).add(molecule);
            }catch (Exception e){
                System.out.println(j + " " +molecule);
            }

        }

    }

    private Point getField(Point location){
        int x = (int)(location.getX()/fieldSize);
        int y = (int)(location.getY()/fieldSize);
        return new Point(x,y);
    }

    public Set<Molecule> getMoleculesInCell(int x , int y){
        if(x < 0){
            if(periodic){
                return getMoleculesInCell(x+m,y);
            }else{
                return Collections.EMPTY_SET;
            }

        } if(x >= m){
            if(periodic){
                return getMoleculesInCell(x-m,y);
            }else{
                return Collections.EMPTY_SET;
            }
        }
        if (y <0){
            if(periodic){
                return getMoleculesInCell(x,y+m);
            }else{
                return Collections.EMPTY_SET;
            }
        }
        if( y>=m){
            if(periodic){
                return getMoleculesInCell(x,y-m);
            }else{
                return Collections.EMPTY_SET;
            }

        }
        return grid.get(new Point(x,y));
    }

    private Set<Molecule> getNearMolecules(Point field){
        Set<Molecule> nearMolecules = new HashSet<>();
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()      ,   (int) field.getY()));
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()+1 ,   (int) field.getY()));
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()+1 ,(int) field.getY()+1));
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()      ,(int) field.getY()+1));
        nearMolecules.addAll(getMoleculesInCell((int )field.getX()+1 ,(int) field.getY()-1));
        return nearMolecules;
    }

    public Set<Molecule> getNeighborsOfMolecule(Molecule molecule, Set<Molecule> analyzed, Set<Molecule> nearMolecules){
        final Point filed = getField(molecule.getLocation());
        //Set<Molecule> nearMolecules = getNearMolecules(filed);
        //nearMolecules.remove(molecule);
        //nearMolecules.removeAll(analyzed);
        Set<Molecule> ans = new HashSet<>();

        for(Molecule other : nearMolecules){
            if(!other.equals(molecule) && !analyzed.contains(other)) {
                if(calculateDistanceBetweenMolecules(molecule,other)){
                    ans.add(other);
                }
            }
        }

        return ans;
    }

    private boolean calculateDistanceBetweenMolecules(Molecule molecule, Molecule other) {

        if(Molecule.distanceBetweenMolecules(molecule,other)-molecule.getRatio()-other.getRatio()<=rc) {
            return true;
        }
        if(!periodic){
            return false;
        }

        double mx = molecule.getLocation().getX();
        double my = molecule.getLocation().getY();
        double ox = other.getLocation().getX();
        double oy = other.getLocation().getY();

        return Point.distanceBetween(new Point(mx,my+l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx+l,my), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx+l,my+l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx+l,my-l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx,my-l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx-l,my-l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx-l,my), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc
        || Point.distanceBetween(new Point(mx-l,my+l), new Point(ox,oy)) - molecule.getRatio() - other.getRatio() <= rc;
    }

    public Map<Molecule,Set<Molecule>> analyzeCell(Point cell){
        Set<Molecule> molecules = grid.get(cell);
        Set<Molecule> nearCellMolecules = getNearMolecules(cell);

        Set<Molecule> analyzed = new HashSet<>();
        Map<Molecule,Set<Molecule>> map = new HashMap<>();

        for(Molecule molecule : molecules){
            Set<Molecule> neighbors = getNeighborsOfMolecule(molecule,analyzed, nearCellMolecules);

            analyzed.add(molecule);
            map.put(molecule,neighbors);
        }
        return map;
    }

    public double getRc() {
        return rc;
    }
}
