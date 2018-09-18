package ar.edu.itba.ss;

public class Simulation {
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

    public void startVerlet() {
        double time = 0;

        while(time < tf) {

            updateParticleWithVerlet();

            // Print particle position
            System.out.println(particle.getxPosition());

            time += dTime;
        }

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


    public void startBeeman() {
        double time = 0;

        while(time < tf) {

            updateParticleWithBeeman();

            // Print particle position
            System.out.println(particle.getxPosition());

            time += dTime;
        }
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


    public void startGearPredictor() {
        double time = 0;

        while(time < tf) {

            updateParticleWithGearPredictor();

            // Print particle position
            System.out.println(particle.getxPosition());

            time += dTime;
        }
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
        double A = 1;

        return A * Math.exp(-(gama / (2*mass)) * time) * Math.cos( Math.sqrt((k / mass) - (gama*gama / (4 * mass * mass) )) * time );
    }
}
