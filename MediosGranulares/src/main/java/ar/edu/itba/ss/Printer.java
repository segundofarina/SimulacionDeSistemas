package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Printer {

    BufferedWriter bw;
    double L;
    double W;
    double D;
    public Printer(String outPath,double L, double W,double D) {
        this.L = L;
        this.W = W;
        this.D = D;
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
//        limits.addAll(
//                Arrays.asList(new Particle(-1, 0, 0, 0, 0, 0, 0),
//                        new Particle(-2, W, 0, 0, 0, 0, 0),
//                        new Particle(-3, 0, L, 0, 0, 0, 0),
//                        new Particle(-4, W, L, 0, 0, 0, 0)));
        addBorders(limits);
        StringBuilder builder = new StringBuilder()
                .append(allParticles.size() + limits.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t vx\t vy\t FN\t \r\n");

        appendParticles(allParticles, builder);
        appendParticles(limits, builder);
        return builder.toString();
    }

    private int addBorders(Set<Particle> limits) {
        double delta= 1/200.0;
        int count=1;
        double radius = 0.01;
        for(double i =-radius ; i< L+radius;i+=delta){
            limits.add(new Particle(-4-count++,-radius,i,0,0,radius,0));
            limits.add(new Particle(-4-count++,W+radius,i,0,0,radius,0));
        }
        for (double j= -radius; j< W+radius;j+=delta){
            if(j < W/2 - D/2 || j > W - (W/2 - D/2))
            limits.add(new Particle(-4-count++,j,-radius,0,0,radius,0));
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
                    .append(" ")
                    .append(new Double(fn).floatValue())
                    .append("\r\n");


        }
    }


    public void appendToFile(Set<Particle> allParticles){
        appendToFile(generateFileString(allParticles));
    }
    private void appendToFile( String data) {
        try {
            bw.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
