package ar.edu.itba.ss;

import java.util.Optional;

public class Particle {
    private final int id;
    private final double radius;
    private final double mass;

    private final Vector previousAcc;

    private final Vector position;
    private final Vector speed;

    private Optional<Vector> acceleration;

    private Optional<Vector> nextPosition;
    private Optional<Vector> nextSpeedPredicted;

    private Optional<Vector> nextAcceleration;

    private Optional<Vector> nextSpeedCorrected;

    private double totalFn;




    private Particle(Particle p, Vector position, Vector speed, Vector previousAcc){
        this.id=p.id;
        this.mass=p.mass;
        this.radius=p.radius;

        this.previousAcc=previousAcc;
        this.speed=speed;
        this.position=position;

        this.nextPosition = Optional.empty();
        this.nextSpeedPredicted = Optional.empty();
        this.nextSpeedCorrected = Optional.empty();
        this.acceleration = Optional.empty();
        this.nextAcceleration = Optional.empty();
        this.totalFn = 0;
    }
    public Particle(int id, double xPosition, double yPosition, double xSpeed, double ySpeed, double radius, double mass) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;

        this.previousAcc = Vector.of(0,0);
        this.position = Vector.of(xPosition,yPosition);
        this.speed =  Vector.of(xSpeed,ySpeed);
        this.nextPosition = Optional.empty();
        this.nextSpeedPredicted = Optional.empty();
        this.nextSpeedCorrected = Optional.empty();
        this.acceleration = Optional.empty();
        this.nextAcceleration = Optional.empty();

    }



    public static Particle of(Particle p, Vector position, Vector speed, Vector previousAcc){
        return new Particle(p,position,speed,previousAcc);
    }

    public int getId() {
        return id;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSpeed() {
        return speed;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public Vector getPreviousAcc() {
        return previousAcc;
    }

    public Vector getAcceleration() {
        return acceleration.orElseThrow(IllegalStateException::new);
    }

    public Vector getNextAcceleration() {
        return nextAcceleration.orElseThrow(IllegalStateException::new);
    }

    public Vector getNextPosition() {
        return nextPosition.orElseThrow(IllegalStateException::new);
    }

    public Vector getNextSpeedPredicted() {
        return nextSpeedPredicted.orElseThrow(IllegalStateException::new);
    }
    public Vector getNextSpeedCorrected() {
        return nextSpeedCorrected.orElseThrow(IllegalStateException::new);
    }



    public void setNextPosition(Vector nextPosition){
        if(this.nextPosition.isPresent()){
            throw new IllegalStateException();
        }else{
            this.nextPosition = Optional.of(nextPosition);
        }
    }

    public void setNextSpeedCorrected(Vector nextVelocity){
        if(this.nextSpeedCorrected.isPresent()){
            throw new IllegalStateException();
        }else{
            this.nextSpeedCorrected = Optional.of(nextVelocity);
        }
    }

    public void setNextSpeedPredicted(Vector nextVelocity){
        if(this.nextSpeedPredicted.isPresent()){
            throw new IllegalStateException();
        }else{
            this.nextSpeedPredicted = Optional.of(nextVelocity);
        }
    }

    public void setNextAcceleration(Vector nextAcceleration){
        if(this.nextAcceleration.isPresent()){
            throw new IllegalStateException();
        }else{
            this.nextAcceleration = Optional.of(nextAcceleration);
        }
    }

    public boolean overlaps(Particle other) {
        double distance = this.position.distance(other.position);

        if(distance < radius + other.radius) {
            return true;
        }

        return false;
    }

    public void setAcceleration(Vector acceleration){
        if(this.acceleration.isPresent()){
            throw new IllegalStateException();
        }else{
            this.acceleration = Optional.of(acceleration);
        }
    }

    public double getTotalFn() {
        return totalFn;
    }

    public void setTotalFn(double totalFn) {
        if (this.totalFn == 0) {
            this.totalFn = totalFn;
        }
    }

    public void clearFn() {
        this.totalFn = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle)) return false;

        Particle particle = (Particle) o;

        if (getId() != particle.getId()) return false;
        if (Double.compare(particle.getRadius(), getRadius()) != 0) return false;
        return Double.compare(particle.getMass(), getMass()) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId();
        temp = Double.doubleToLongBits(getRadius());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getMass());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
