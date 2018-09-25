package ar.edu.itba.ss;

public class Body {
    private int id;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double mass;
    private double radius;
    private String name;


    public Body(int id, double x, double y, double vx, double vy, double mass, double radius,String name) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.radius = radius;
        this.name = name;
    }
    public Body(Body p){
        this.id = p.id;
        this.x = p.x;
        this.y = p.y;
        this.vx = p.vx;
        this.vy = p.vy;
        this.mass = p.mass;
        this.radius = p.radius;
        this.name = p.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Body)) return false;

        Body body = (Body) o;

        if (getId() != body.getId()) return false;
        if (Double.compare(body.getX(), getX()) != 0) return false;
        if (Double.compare(body.getY(), getY()) != 0) return false;
        if (Double.compare(body.getVx(), getVx()) != 0) return false;
        if (Double.compare(body.getVy(), getVy()) != 0) return false;
        if (Double.compare(body.getMass(), getMass()) != 0) return false;
        return Double.compare(body.getRadius(), getRadius()) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId();
        temp = Double.doubleToLongBits(getX());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getY());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVx());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVy());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMass());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getRadius());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
