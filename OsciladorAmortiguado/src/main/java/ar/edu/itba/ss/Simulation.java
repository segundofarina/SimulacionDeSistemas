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
        int iterations = 0;
        initalizeBW(outPath,"Verlet");

        while(iterations <= tf/dTime) {

            updateParticleWithVerlet();

            // Print particle position
            System.out.println(particle.getxPosition());
            appendToFile(bw,generateFileString(particle));

            time += dTime;
            iterations++;
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
        int iterations = 0;
        initalizeBW(outPath,"Beeman");
        while(iterations <= tf/dTime) {

            updateParticleWithBeeman();

            // Print particle position
            System.out.println(particle.getxPosition());
            appendToFile(bw,generateFileString(particle));

            time += dTime;
            iterations++;
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
        double vDt = v + (-dTime / mass) * f;

        return getForce(rDt, vDt) / mass;
    }


    public void startGearPredictor(String outPath) {
        double time = 0;
        initalizeBW(outPath,"GearPredictor");
        int iterations = 0;
        while(iterations <= tf/dTime) {

            updateParticleWithGearPredictor();

            // Print particle position
            System.out.println(particle.getxPosition());
            appendToFile(bw,generateFileString(particle));
            time += dTime;
            iterations++;
        }
        closeBW();
    }

    private void updateParticleWithGearPredictor() {
        // Busco r1 a r5
        double r = particle.getxPosition();
        double r1 = particle.getxSpeed();
        double r2 = getForce(r, r1) / mass;
        double r3 = getForce(r1, r2) / mass;
        double r4 = getForce(r2, r3) / mass;
        double r5 = getForce(r3, r4) / mass;

        // Predigo r, r1, r2
        double rP = r + r1 * dTime + r2 * (Math.pow(dTime, 2) / 2) + r3 * (Math.pow(dTime, 3) / 6) + r4 * (Math.pow(dTime, 4) / 12) + r5 * (Math.pow(dTime, 5) / 60);
        double r1P = r1 + r2 * dTime + r3 * (Math.pow(dTime, 2) / 2) + r4 * (Math.pow(dTime, 3) / 6) + r5 * (Math.pow(dTime, 4) / 12);
        double r2P = r2 + r3 * dTime + r4 * (Math.pow(dTime, 2) / 2) + r5 * (Math.pow(dTime, 3) / 6);

        // Calculo dA
        double r2Dt = getForce(rP, r1P) / mass;

        double dA = r2Dt - r2P;

        // Calculo dR2
        double dR2 = dA * Math.pow(dTime, 2) / 2;

        // Corrijo r, r1
        double rC = rP + (3.0/20) * dR2;
        double r1C = r1P + (251.0/360) * dR2 / dTime;

        particle.setxPosition(rC);
        particle.setxSpeed(r1C);
    }


    private double getForce(double r, double v) {
        return -k * r - gama * v;
    }

    public void startRealSolution(String outPath) {
        double time = 0;
        initalizeBW(outPath,"Real");
        int iterations = 0;
        while(iterations <= tf/dTime) {

            double pos = getParticleRealPosition(time);

            // Print particle postion
            System.out.println(pos);
            particle.setxPosition(pos);
            appendToFile(bw,generateFileString(particle));
            time += dTime;
            iterations++;
        }
        closeBW();
    }

    private double getParticleRealPosition(double time) {
        double A = 1;

        return A * Math.exp(-(gama / (2*mass)) * time) * Math.cos( Math.sqrt((k / mass) - (gama*gama / (4 * mass * mass) )) * time );
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

            bw = new BufferedWriter(new FileWriter(outPath+algType+"-dt:"+dTime +"-tf:"+tf + ".txt", true));
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
