package ar.edu.itba.ss;

import java.util.Objects;

public class Particle {
    private int id;
    private double xPosition;
    private double yPosition;
    private double xSpeed;
    private double ySpeed;
    private double radius;
    private double mass;

    public Particle(int id, double xPosition, double yPosition, double xSpeed, double ySpeed, double radius, double mass) {
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.radius = radius;
        this.mass = mass;
    }

    public int getId() {
        return id;
    }


    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public boolean overlaps(Particle other) {
        double distance = distanceTo(other);

        if(distance < radius + other.radius) {
            return true;
        }

        return false;
    }

    public double distanceTo(Particle other) {
        return Math.sqrt( Math.pow( other.getxPosition() - xPosition ,2) + Math.pow( other.getyPosition() - yPosition ,2) );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return particle.getId() == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPosition, yPosition, xSpeed, ySpeed, radius, mass);
    }

    @Override
    public String toString() {

        return "Particle{" +
                "id=" + id +
                ", xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                ", xSpeed=" + xSpeed +
                ", ySpeed=" + ySpeed +
                ", radius=" + radius +
                ", mass=" + mass +
                '}';
    }

}
