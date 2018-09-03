package ar.edu.itba.ss;

import java.util.Objects;
import java.util.Optional;

public class Particle{
    private int id;
    private double xPosition;
    private double yPosition;
    private double xSpeed;
    private double ySpeed;
    private double radius;
    private double mass;


    private int crashesAmount;

    public Particle(int id, double xPosition, double yPosition, double xSpeed, double ySpeed, double radius, double mass) {
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.radius = radius;
        this.mass = mass;
        this.crashesAmount = 0;
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

    public void addCrash(){
        crashesAmount++;
    }

    public int getCrashesAmount(){
        return crashesAmount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return Double.compare(particle.xPosition, xPosition) == 0 &&
                Double.compare(particle.yPosition, yPosition) == 0 &&
                Double.compare(particle.xSpeed, xSpeed) == 0 &&
                Double.compare(particle.ySpeed, ySpeed) == 0 &&
                Double.compare(particle.radius, radius) == 0 &&
                Double.compare(particle.mass, mass) == 0;
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
