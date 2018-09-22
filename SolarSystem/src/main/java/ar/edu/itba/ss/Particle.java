package ar.edu.itba.ss;

import java.util.Objects;

public class Particle {
    private int id;
    private double xPosition;
    private double yPosition;
    private double xSpeed;
    private double ySpeed;
    private double mass;

    public Particle(int id, double xPosition, double yPosition, double xSpeed, double ySpeed, double mass) {
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.mass = mass;
    }

    public Particle(Particle p) {
        this.id = p.id;
        this.xPosition = p.xPosition;
        this.yPosition = p.yPosition;
        this.xSpeed = p.xSpeed;
        this.ySpeed = p.ySpeed;
        this.mass = p.mass;
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

    public double getMass() {
        return mass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
