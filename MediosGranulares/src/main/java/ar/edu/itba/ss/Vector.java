package ar.edu.itba.ss;

public class Vector {

    public static final Vector ZERO = Vector.of(0.0, 0.0);

    protected final double x;
    protected final double y;

    public static Vector of(final double x, final double y) {
        return new Vector(x, y);
    }

    public Vector(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector add(final Vector vector) {
        return Vector.of(x + vector.x, y + vector.y);
    }

    public Vector subtract(final Vector vector) {
        return Vector.of(x - vector.x, y - vector.y);
    }

    public Vector multiplyBy(final double value) {
        return Vector.of(x*value, y*value);
    }

    public Vector dividedBy(final double value) {
        return Vector.of(x/value, y/value);
    }

    public Vector versor() {
        return Vector.of(x, y).dividedBy(abs());
    }

    public double abs() {
        return Math.hypot(x, y);
    }

    public Vector tangent() {
        return Vector.of(-y, x);
    }

    public double distance(Vector other){
        return this.subtract(other).abs();
    }
    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;

        Vector vector = (Vector) o;

        if (Double.compare(vector.getX(), getX()) != 0) return false;
        return Double.compare(vector.getY(), getY()) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getX());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getY());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
