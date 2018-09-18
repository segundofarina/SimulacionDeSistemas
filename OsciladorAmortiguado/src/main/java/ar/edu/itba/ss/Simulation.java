package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.Locale;

public class Simulation {
    private BufferedWriter bw;
    private double dTime;
    private Particle particle;

    private static double gama = 100; // gama = 100 kg/seg
    private static double k = Math.pow(10, 4); // k = 10^4 N/m
    private static double tf = 5;

    private static double initialPosition = 1;
    private static double C = 1;
    private static double mass = 70;


    // f = m * a = m * r2 = -k * r - gama * r1


    public Simulation(double dTime) {
        this.dTime = dTime;

        // C constant??
        double C = 1;
        double v = -C * gama / (2 * mass);

        particle = new Particle(0, initialPosition, 0, v,0,mass);
    }

    public void startVerlet(String outPath) {
        double time = 0;
        initalizeBW(outPath,"Verlet");

        while(time < tf) {

            updateParticleWithVerlet();

            // Print particle position
            System.out.println(particle.getxPosition());
            appendToFile(bw,generateFileString(particle));

            time += dTime;
        }
        closeBW();

    }

    private void updateParticleWithVerlet() {
        double r = particle.getxPosition();
        double v = particle.getxSpeed();

        double f = getForce(r, v);

        // Position
        double rDt = r + dTime * v + ( Math.pow(dTime, 2) / particle.getMass() ) * f;

        double vHalfDt = v + ( dTime / (2 * particle.getMass()) ) * f;

        double fDt = getForce(rDt, vHalfDt);

        // Speed
        double vDt = vHalfDt + ( dTime / (2 * particle.getMass()) ) * fDt;

        particle.setxPosition(rDt);
        particle.setxSpeed(vDt);
    }


    public void startBeeman(String outPath) {
        double time = 0;
        initalizeBW(outPath,"Beeman");
        while(time < tf) {

            updateParticleWithBeeman();

            // Print particle position
            System.out.println(particle.getxPosition());
            appendToFile(bw,generateFileString(particle));

            time += dTime;
        }
        closeBW();

    }


    private void updateParticleWithBeeman() {
        double r = particle.getxPosition();
        double v = particle.getxSpeed();

        double a = getForce(r, v) / mass;

        double aLastDt = getLastAcelerationWithEuler(r, v);

        double rDt = r + v * dTime + ( (2.0/3) * a - (1.0/6) * aLastDt) * Math.pow(dTime, 2);

        double vDtPredicted = v + (3.0/2) * a * dTime - 0.5 * aLastDt * dTime;

        double aDt = getForce(rDt, vDtPredicted) / mass;

        double vDt = v + (1.0/3) * aDt * dTime + (5.0/6) * a * dTime - (1.0/6) * aLastDt * dTime;

        particle.setxPosition(rDt);
        particle.setxSpeed(vDt);
    }

    private double getLastAcelerationWithEuler(double r, double v) {
        double f = getForce(r, v);

        double rDt = r + v * (-dTime) + ( Math.pow(dTime, 2) / (2 * mass) ) * f;
        double vDt = v + (dTime / mass) * f;

        return getForce(rDt, vDt) / mass;
    }


    private void startGearPredictor() {
        double time = 0;

        while(time < tf) {

            updateParticleWithGearPredictor();

            // Print particle position
            System.out.println(particle.getxPosition());

            time += dTime;
        }
    }

    private void updateParticleWithGearPredictor() {

    }


    private double getForce(double r, double v) {
        return -k * r - gama * v;
    }

    public void startRealSolution() {
        double time = 0;

        while(time < tf) {

            double pos = getParticleRealPosition(time);

            // Print particle postion
            System.out.println(pos);

            time += dTime;
        }
    }

    private double getParticleRealPosition(double time) {
        // A ??
        double A = 1;

        //return A *
        return 0;
    }






    private void closeBW() {
        if (bw != null) try {
            bw.flush();
            bw.close();
        } catch (IOException ioe2) {
            // just ignore it
        }
    }

    private boolean initalizeBW(String outPath,String algType) {
        try {

            bw = new BufferedWriter(new FileWriter(outPath+"/Oscilador-"+algType+"-dt:"+dTime +"-tf:"+tf+ LocalDateTime.now() + ".txt", true));
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String generateFileString(Particle particle){

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(12);
        df.setMinimumIntegerDigits(1);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        StringBuilder builder = new StringBuilder()
                .append(df.format(particle.getxPosition()))
                .append("\n");
        return builder.toString();
    }



    public static void appendToFile (BufferedWriter bw , String data) {
        try {
            bw.write(data);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
