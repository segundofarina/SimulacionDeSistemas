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
        this.fieldSize = l/m;
        this.grid = new HashMap<>();
        this.periodic = periodic;

        for(int i = 0; i < m ; i++){
            for(int j = 0 ; j < m ; j++) {
                grid.put(new Point(i, j), new HashSet<Molecule>());
            }
        }

        for(Molecule molecule : molecules){
            Point j = getField(molecule.getLocation());
            grid.get(j).add(molecule);
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

    public Set<Molecule> getNeighborsOfMolecule(Molecule molecule, Set<Molecule> analyzed){
        final Point filed = getField(molecule.getLocation());
        Set<Molecule> nearMolecules = getNearMolecules(filed);
        nearMolecules.remove(molecule);
        nearMolecules.removeAll(analyzed);
        Set<Molecule> ans = new HashSet<>();

        for(Molecule other : nearMolecules){
            if(calculateDistanceBetweenMolecules(molecule,other)){
                ans.add(other);
            }
        }

        return ans;
    }

    private boolean calculateDistanceBetweenMolecules(Molecule molecule, Molecule other) {

        boolean ans = Molecule.distanceBetweenMolecules(molecule,other)-molecule.getRatio()-other.getRatio()<=rc;
        if(ans) {
            return true;
        }
        if(!periodic){
            return false;
        }
        Point mfield = getField(molecule.getLocation());
        Point ofield = getField(other.getLocation());

        if(mfield.getX() == ofield.getX()) {
            if(mfield.getY() < ofield.getY()){
                ans = Point.distanceBetween(other.getLocation(), new Point(molecule.getLocation().getX(), molecule.getLocation().getY() + l)) - molecule.getRatio() - other.getRatio() <= rc;
            }else{
                ans = Point.distanceBetween(molecule.getLocation(), new Point(other.getLocation().getX(), other.getLocation().getY() + l)) - molecule.getRatio() - other.getRatio() <= rc;
            }
        }
        if(ans){
            return true;
        }
        if(mfield.getY() == ofield.getY()) {
            if (mfield.getX() < ofield.getX()) {
                ans = Point.distanceBetween(other.getLocation(), new Point(molecule.getLocation().getX() + l, molecule.getLocation().getY())) - molecule.getRatio() - other.getRatio() <= rc;
            }else{
                ans = Point.distanceBetween(molecule.getLocation(), new Point(other.getLocation().getX() + l, other.getLocation().getY())) - molecule.getRatio() - other.getRatio() <= rc;
            }
        }
        if(ans) {
            return true;
        }
        if(mfield.getX() < ofield.getX()) {
            if (mfield.getY() < ofield.getY()) {
                ans = Point.distanceBetween(other.getLocation(), new Point(molecule.getLocation().getX() + l, molecule.getLocation().getY() + l)) - molecule.getRatio() - other.getRatio() <= rc;
            }else{
                ans = Point.distanceBetween(new Point(molecule.getLocation().getX() + l, molecule.getLocation().getY()), new Point(other.getLocation().getX(), other.getLocation().getY() + l)) - molecule.getRatio() - other.getRatio() <= rc;
            }
        }else{
            if (mfield.getY() < ofield.getY()) {
                ans = Point.distanceBetween(new Point(other.getLocation().getX()+l, other.getLocation().getY()), new Point(molecule.getLocation().getX(), molecule.getLocation().getY() + l)) - molecule.getRatio() - other.getRatio() <= rc;
            }else{
                ans = Point.distanceBetween(new Point(molecule.getLocation().getX(), molecule.getLocation().getY()+l), new Point(other.getLocation().getX()+l, other.getLocation().getY())) - molecule.getRatio() - other.getRatio() <= rc;
            }
        }
        return ans;
    }

    public Map<Molecule,Set<Molecule>> analyzeCell(Point cell){
        Set<Molecule> molecules = grid.get(cell);
        Set<Molecule> analyzed = new HashSet<>();
        Map<Molecule,Set<Molecule>> map = new HashMap<>();

        for(Molecule molecule : molecules){
            Set<Molecule> neighbors =getNeighborsOfMolecule(molecule,analyzed);
            analyzed.add(molecule);
            map.put(molecule,neighbors);
        }
        return map;
    }

    public double getRc() {
        return rc;
    }
}
