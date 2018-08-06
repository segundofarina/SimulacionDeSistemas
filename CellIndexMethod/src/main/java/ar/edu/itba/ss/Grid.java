package ar.edu.itba.ss;

import java.util.*;

public class Grid {
    private int m;
    private double rc;
    private double fieldSize;
    private HashMap<Point,Set<Molecule>> grid;

    public Grid(int l, int n, int m, double rc, Set<Molecule> molecules) {
        this.m = m;
        this.rc = rc;
        this.fieldSize = l/m;
        this.grid = new HashMap<>();

        for(int i = 0; i < m ; i++){
            for(int j = 0 ; j < m ; j++) {
                grid.put(new Point(i, j), new HashSet<Molecule>());
            }
        }

        for(Molecule molecule : molecules){
            Point j = getField(molecule.getLocation());
            //System.out.println("Id: "+molecule.getId()+" Field:"+j);
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
            return getMoleculesInCell(x+m,y);
        } if(x >= m){
            return getMoleculesInCell(x-m,y);
        }
        if (y <0){
            return getMoleculesInCell(x,y+m);
        }
        if( y>=m){
            return getMoleculesInCell(x,y-m);
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

    public Set<Molecule> getNeighborsOfMolecule(Molecule molecule){
        final Point filed = getField(molecule.getLocation());
        Set<Molecule> nearMolecules = getNearMolecules(filed);
        nearMolecules.remove(molecule);
        Set<Molecule> ans = new HashSet<>();

        for(Molecule other : nearMolecules){
            if(Molecule.distanceBetweenMolecules(molecule,other)<=rc){
                ans.add(other);
            }
        }

        return ans;
    }


}
