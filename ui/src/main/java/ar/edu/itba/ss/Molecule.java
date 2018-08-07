package ar.edu.itba.ss;


import java.awt.Point;
import java.util.List;

public class Molecule {
    private Point position;
    private  int id;
    private  List<Integer> neighbours;
    private double radius;

    public double getRadius() {
        return radius;
    }

    public Molecule(Point position, int id,int radius, List<Integer> neighbours) {
        this.position = position;
        this.id = id;
        this.neighbours = neighbours;
        this.radius = radius;
    }

    public Point getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }
}
