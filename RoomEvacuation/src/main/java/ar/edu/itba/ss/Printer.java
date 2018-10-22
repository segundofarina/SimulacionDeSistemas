package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Printer {

    BufferedWriter bw;
    double L;
    double W;
    double door;
    public Printer(String outPath, double L, double W, double door) {

        this.L = L;
        this.W = W;
        this.door = door;
        try {
            bw = new BufferedWriter(new FileWriter(outPath  + LocalDateTime.now() + ".txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (bw != null) try {
            bw.flush();
            bw.close();
        } catch (IOException ioe2) {
            // just ignore it
        }
    }


    private String generateFileString(Set<Particle> allParticles) {
        Set<Particle> limits = new HashSet<>();
        addBorders(limits);
        StringBuilder builder = new StringBuilder()
                .append(allParticles.size() + limits.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t vx\t vy\t \r\n");

        appendParticles(allParticles, builder);
        appendParticles(limits, builder);
        return builder.toString();
    }

    private int addBorders(Set<Particle> limits) {
        double delta= 1/10.0;
        int count=1;
        double radius = 0.15;
        for(double i =-radius ; i< L+radius;i+=delta){
            limits.add(new Particle(-count++,Vector.of(-radius,i),Vector.ZERO,radius,0,0,Vector.ZERO));
            limits.add(new Particle(-count++,Vector.of(W+radius,i),Vector.ZERO,radius,0,0,Vector.ZERO));
        }
        for (double j= -radius; j< W+radius;j+=delta){
            if(j < W/2 - door/2 || j > (W/2 + door/2)){
                limits.add(new Particle(-count++,Vector.of(j,-radius),Vector.ZERO,radius,0,0,Vector.ZERO));
            }
            limits.add(new Particle(-count++,Vector.of(j,L+radius),Vector.ZERO,radius,0,0,Vector.ZERO));

        }
        return count;
    }

    private void appendParticles(Set<Particle> allParticles, StringBuilder builder) {
        for (Particle current : allParticles) {
            double vx = current.getSpeed().x;
            double vy = current.getSpeed().y;
            double fn = current.getTotalFn()/Math.PI*2*current.getRadius();
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getPosition().x)
                    .append(" ")
                    .append(current.getPosition().y)
                    .append(" ")
                    .append(current.getRadius())
                    .append(" ")
                    .append(new Double(vx).floatValue())
                    .append(" ")
                    .append(new Double(vy).floatValue())
                    .append("\r\n");


        }
    }


    public void appendToAnimation(Set<Particle> allParticles){
        appendToFile(generateFileString(allParticles));
    }
    public void appendToFile( String data) {
        try {
            bw.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void flush() {
        try {
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
