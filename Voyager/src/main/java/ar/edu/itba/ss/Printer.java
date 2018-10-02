package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

public class Printer {

    public static void closeBW(BufferedWriter bw) {
        if (bw != null) try {
            bw.flush();
            bw.close();
        } catch (IOException ioe2) {
            // just ignore it
        }
    }

    public static BufferedWriter initalizeBW(String outPath) {
        BufferedWriter bw;
        try {

            bw = new BufferedWriter(new FileWriter(outPath+"/solarSystem" + LocalDateTime.now() + ".txt", true));
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return bw;
    }

    public static String generateFileString(Collection<Body> allPlanets){

        StringBuilder builder = new StringBuilder()
                .append(allPlanets.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t vx\t vy\t\r\n");

        appendParticles(allPlanets, builder);
        return builder.toString();
    }

    public static void appendParticles(Collection<Body> allParticles, StringBuilder builder) {
        for(Body current: allParticles){
            double vx = current.getVx();
            double vy = current.getVy();
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getX()/1000)
                    .append(" ")
                    .append(current.getY()/1000)
                    .append(" ")
                    .append(current.getRadius()/10)
                    .append(" ")
                    .append(new Double(vx).floatValue())
                    .append(" ")
                    .append(new Double(vy).floatValue())
                    .append("\r\n");


        }
    }

    public static void appendToFile (BufferedWriter bw , String data) {
        try {
            bw.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
