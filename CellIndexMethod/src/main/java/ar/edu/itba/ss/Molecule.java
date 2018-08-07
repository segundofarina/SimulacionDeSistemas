package ar.edu.itba.ss;

public class Molecule {
    private static int quantity = 0;
    private int id;
    private double ratio;
    private Property<String> property;
    private Point location;
    private Point velocity;

    public Molecule(double ratio, Property<String> property, Point location, Point velocity) {
        quantity++;
        this.id = quantity;
        this.ratio = ratio;
        this.property = property;
        this.location = location;
        this.velocity = velocity;
    }
    public Molecule(int id,double ratio, Property<String> property, Point location, Point velocity) {
        this.id = id;
        this.ratio = ratio;
        this.property = property;
        this.location = location;
        this.velocity = velocity;
    }

    public int getId() {
        return id;
    }

    public double getRatio() {
        return ratio;
    }

    public Point getLocation() {
        return location;
    }

    public static double distanceBetweenMolecules(Molecule m1, Molecule m2){
        return Point.distanceBetween(m1.location,m2.location);
    }

    @Override
    public String toString() {
        return "Molecule{" +
                "id=" + id +
                '}';
    }
}
