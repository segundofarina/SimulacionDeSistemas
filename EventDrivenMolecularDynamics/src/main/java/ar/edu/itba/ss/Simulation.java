package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Simulation {
    private double boxSize;
    private Set<Particle> particles;
    private  BufferedWriter bw;
    private PriorityQueue<Collision> queue;

    private final double smallRadius = 0.005; // meters
    private final double smallMass = 0.1; // grams
    private final double largeRadius = 0.05; // meters
    private final double largeMass = 100; // grams

    private final double maxSpeed = 0.1;

    private double time;


    public Simulation(double boxSize, int smallParticlesAmount) {
        this.boxSize = boxSize;
        this.particles = new HashSet<>();
        this.bw=null;
        this.queue = new PriorityQueue<>();
        this.time =0;
        addParticles(smallParticlesAmount);
    }

    /* Add random particles to the box */
    private void addParticles(int smallParticlesAmount) {
        Random rand = new Random();

        // Add large particle
        particles.add(new Particle(0,boxSize/2, boxSize/2, 0, 0, largeRadius, largeMass));

        int id = 1;
        // Add all small particles
        while(particles.size() < smallParticlesAmount + 1) {
            double xPosition = rand.nextDouble() * (boxSize - 2*smallRadius) + smallRadius;
            double yPosition = rand.nextDouble() * (boxSize - 2*smallRadius) + smallRadius;

            double speed = rand.nextDouble() * maxSpeed;
            double angle = rand.nextDouble() * 2 * Math.PI;
            double xSpeed = speed * Math.cos(angle);
            double ySpeed = speed * Math.sin(angle);

            if(!isOnExistentParticle(xPosition, yPosition, smallRadius)) {
                particles.add(new Particle(id, xPosition, yPosition, xSpeed, ySpeed, smallRadius, smallMass));
                id++;
            }
        }
    }

    private boolean isOnExistentParticle(double xPosition, double yPosition, double radius) {
        for(Particle p : particles) {
            if( Math.pow((p.getxPosition() - xPosition),2) + Math.pow((p.getyPosition() - yPosition),2) <= Math.pow((p.getRadius() + radius),2) ) {
                return true;
            }
        }

        return false;
    }




    public void start(int iterations, String outPath) {
        long start= System.currentTimeMillis();

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        if (!initalizeBW(outPath)) return;

        appendToFile(bw,"Time between collisions: \n");
        appendToFile(bw,"particles: "+particles.size()+ " iterations: "+iterations+"\n");

        calculateNextCrashTimeForEveryone();


        for(int i = 0; i < iterations; i++) {
            Collision nextCollision = queue.poll();

            updateParticlesPosition(nextCollision.getTime()-time);

            updateSpeedCrashedParticles(nextCollision.getParticles());

            appendToFile(bw,df.format((nextCollision.getTime()-time))+"\n");

            System.out.println(new Double(nextCollision.getTime()-time));

            time=nextCollision.getTime();

            for(Particle p : nextCollision.getParticles()){
                updateQueue(p);
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("Simulated time: "+time+"s");
        appendToFile(bw,"Simulated time: "+time+"s\n");
        appendToFile(bw,"Proccesing time:"+(end-start)+"ms\n");

        System.out.println("Proccesing time:"+(end-start)+"ms");
        closeBW();

    }

    public void startUntilCrash( String outPath) {
        long start= System.currentTimeMillis();
        double nextTime =1;
        int collisions =0;

        if (!initalizeBW(outPath)) return;

        appendToFile(bw,"particles: "+particles.size()+ " end UntilCrash\n");


        StringBuilder builder = new StringBuilder();
        particles.stream().forEach((x)->builder.append(Math.sqrt(Math.pow(x.getxSpeed(),2)+Math.pow(x.getySpeed(),2))+"\n"));
        appendToFile(bw,builder.toString());


        Particle largePaticle=null;
        for(Particle p : particles){
            if(p.getRadius() == largeRadius){
                largePaticle = p;
            }
        }
        calculateNextCrashTimeForEveryone();


        while(true) {

            Collision nextCollision = queue.poll();



            updateParticlesPosition(nextCollision.getTime()-time);

            updateSpeedCrashedParticles(nextCollision.getParticles());

            time=nextCollision.getTime();
            //appendToFile(bw,collisions+ "\t"+nextTime+" s\n");
            if(time > nextTime){
                System.out.println(collisions+ "\t"+nextTime+" s");

                nextTime++;
                collisions=0;
            }
            collisions++;

            for(Particle p : nextCollision.getParticles()){
                updateQueue(p);
            }

            if(nextCollision.contains(largePaticle) && nextCollision.getParticles().size()==1){
                break;
            }

        }

        appendToFile(bw,"kjbkjbkbjkjkjbkjbkjhkljhljkhkjhkljhlkjhkjhkhlkjhlkjhkjhkjhkjhkjlhkljhkljhklj");

        StringBuilder builder2 = new StringBuilder();
        particles.stream().forEach((x)->builder2.append(Math.sqrt(Math.pow(x.getxSpeed(),2)+Math.pow(x.getySpeed(),2))+"\n"));
        appendToFile(bw,builder2.toString());
        long end = System.currentTimeMillis();
        System.out.println("Simulated time: "+time+"s");
        appendToFile(bw,"Simulated time: "+time+"s\n");
        appendToFile(bw,"Proccesing time:"+(end-start)+"ms\n");

        System.out.println("Proccesing time:"+(end-start)+"ms");
        closeBW();

    }


    public void startForTime(double endTime, String outPath) {
        long start= System.currentTimeMillis();
        double nextTime =1;
        int collisions =0;
        int iterations =0;

        if (!initalizeBW(outPath)) return;

        appendToFile(bw,"particles: "+particles.size()+ " endTime: "+endTime+"\n");


        calculateNextCrashTimeForEveryone();

        while(time<endTime) {
            Collision nextCollision = queue.poll();

            updateParticlesPosition(nextCollision.getTime()-time);

            updateSpeedCrashedParticles(nextCollision.getParticles());

            time=nextCollision.getTime();

            if(time > nextTime){
                System.out.println(collisions+ "\t"+nextTime+" s");
                appendToFile(bw,collisions+ "\t"+nextTime+" s\n");
                nextTime++;
                collisions=0;
            }
            collisions++;

            for(Particle p : nextCollision.getParticles()){
                updateQueue(p);
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("Simulated time: "+time+"s");
        appendToFile(bw,"Simulated time: "+time+"s\n");
        appendToFile(bw,"Proccesing time:"+(end-start)+"ms\n");

        System.out.println("Proccesing time:"+(end-start)+"ms");
        closeBW();

    }

    public void startForTimeBruteForce(double endTime,String outPath) {
        long start= System.currentTimeMillis();
        final Set<Particle> crashedParticles = new HashSet<>();
        if (!initalizeBW(outPath)) return;
        double nextTime =1;
        int collisions =0;


        while (time<endTime) {
            double nextCrashTime = getNextCrashTime(crashedParticles);


            updateParticlesPosition(nextCrashTime);

            if(time > nextTime){
                System.out.println(collisions+ "\t"+nextTime+" s");
                appendToFile(bw,collisions+ "\t"+nextTime+" s\n");
                nextTime++;
                collisions=0;
            }
            collisions++;

            updateSpeedCrashedParticles(crashedParticles);
            time+=nextCrashTime;

            appendToFile(bw,generateFileString(particles));
        }
        long end = System.currentTimeMillis();
        System.out.println("Simulated time: "+time+"s");

        System.out.println("Proccesing time:"+(end-start)+"ms");

        closeBW();

    }

    public void startBruteForce(int iterations,String outPath) {
        long start= System.currentTimeMillis();
        final Set<Particle> crashedParticles = new HashSet<>();
        if (!initalizeBW(outPath)) return;


        for(int i = 0; i < iterations; i++) {
            double nextCrashTime = getNextCrashTime(crashedParticles);


            updateParticlesPosition(nextCrashTime);

            updateSpeedCrashedParticles(crashedParticles);
            time+=nextCrashTime;

            appendToFile(bw,generateFileString(particles));
        }
        long end = System.currentTimeMillis();
        System.out.println("Simulated time: "+time+"s");

        System.out.println("Proccesing time:"+(end-start)+"ms");

        closeBW();

    }

    public void startForAnimation(int animationTime, String outPath){
        int framesPerSecond = 10;
        double jump = (double)1/framesPerSecond;
        double nextFrame = jump;

        calculateNextCrashTimeForEveryone();

        if (!initalizeBW(outPath)) return;


        while (time<animationTime){


            Collision nextCollision = queue.poll();


            if(nextCollision.getTime() > nextFrame){
                updateParticlesPosition(nextFrame-time);

                appendToFile(bw,generateFileString(particles));
                time=nextFrame;
                nextFrame+=jump;
                System.out.println(time);
                queue.add(nextCollision);

            }else{

                updateParticlesPosition(nextCollision.getTime()-time);

                updateSpeedCrashedParticles(nextCollision.getParticles());

                time=nextCollision.getTime();

                for(Particle p : nextCollision.getParticles()){
                    updateQueue(p);
                }


            }


        }
        System.out.print("Time: "+time);

        closeBW();

    }

    public void startForAnimationBruteForce(int animationTime, String outPath){
        int framesPerSecond = 10;
        double jump = (double)1/framesPerSecond;
        double nextFrame = jump;
        double time = 0;
        final Set<Particle> crashedParticles = new HashSet<>();

        if (!initalizeBW(outPath)) return;


        while (time<animationTime){
            double nextCrashTime = getNextCrashTime(crashedParticles);

            if(nextCrashTime +time > nextFrame){
                updateParticlesPosition(nextFrame-time);

                appendToFile(bw,generateFileString(particles));
                time=nextFrame;
                nextFrame+=jump;

            }else{
                updateParticlesPosition(nextCrashTime);
                updateSpeedCrashedParticles(crashedParticles);
                time+=nextCrashTime;
            }


        }
        System.out.print("Time: "+time);

        closeBW();

    }

    private void updateQueue(Particle particle) {


        queue.removeIf((x)-> x.contains(particle));

        double wallCrashTime = wallCrash(particle);
        if(wallCrashTime != Double.POSITIVE_INFINITY){
            queue.add(new WallCollision(particle,wallCrashTime+time));
        }

        for(Particle other : particles) {
            if(!other.equals(particle)) {
                double particleCrashTime = particlesCrash(particle, other);
                if (particleCrashTime != Double.POSITIVE_INFINITY) {
                    queue.add(new ParticleCollision(particle, other, particleCrashTime+time));
                }
            }
        }


    }

    private void calculateNextCrashTimeForEveryone(){
        Set<Particle> aux = new HashSet<>(particles);
        Particle current = aux.iterator().next();
        aux.remove(current);
        calculateNextCrashTimeForEveryone(current,aux);

    }

    private void calculateNextCrashTimeForEveryone(Particle p, Set<Particle> others){
        if(others.size()==0){
            return;
        }
            double time = Double.POSITIVE_INFINITY;
            //Particle crashAgainst = null;
            // get wall crash time
            double wallCrashTime = wallCrash(p);
            if(wallCrashTime != Double.POSITIVE_INFINITY){
                queue.add(new WallCollision(p,wallCrashTime));
            }


            // get other particles crash
            for(Particle other : others) {
                    double particleCrashTime = particlesCrash(p, other);
                    if(particleCrashTime != Double.POSITIVE_INFINITY) {
                        queue.add(new ParticleCollision(p, other, particleCrashTime));
                    }
            }
           Particle next = others.iterator().next();
            others.remove(next);
            calculateNextCrashTimeForEveryone(next,others);

    }

    private double getNextCrashTime(Set<Particle> crashedParticles) {
        double time = Double.POSITIVE_INFINITY;
        for(Particle p : particles) {
            // get wall crash time
            double wallCrashTime = wallCrash(p);
            if(time > wallCrashTime) {
                time = wallCrashTime;
                saveCrashedParticle(crashedParticles, p);
            }

            // get other particles crash
            for(Particle other : particles) {
                if(!other.equals(p)) {
                    double particleCrashTime = particlesCrash(p, other);
                    if(particleCrashTime < time) {
                        time = particleCrashTime;
                        saveCrashedParticle(crashedParticles, p, other);
                    }
                }
            }
        }

        return time;
    }

    static double particlesCrash(Particle pi, Particle pj) {
        double dvx = pj.getxSpeed()-pi.getxSpeed();
        double dvy = pj.getySpeed()-pi.getySpeed();

        double dx = pj.getxPosition()-pi.getxPosition();
        double dy = pj.getyPosition()-pi.getyPosition();

        double dvdr = dvx*dx + dvy*dy;
        if(dvdr >= 0) {
            return Double.POSITIVE_INFINITY;
        }

        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double phi = pi.getRadius()+pj.getRadius();

        double d = dvdr*dvdr - dvdv*(drdr-phi*phi);

        if(d < 0) {
            return Double.POSITIVE_INFINITY;
        }


        return -(dvdr+Math.sqrt(d))/dvdv;
    }

    private double wallCrash(Particle p) {
        double minTimeX = Double.POSITIVE_INFINITY;

        if(p.getxSpeed() > 0) {
            minTimeX = (boxSize - p.getRadius() - p.getxPosition()) / p.getxSpeed();
        } else if(p.getxSpeed() < 0) {
            minTimeX = (p.getRadius() - p.getxPosition()) / p.getxSpeed();
        }

        double minTimeY = Double.POSITIVE_INFINITY;
        if(p.getySpeed() > 0) {
            minTimeY = (boxSize - p.getRadius() - p.getyPosition()) / p.getySpeed();

        } else if(p.getySpeed() < 0) {
            minTimeY = (p.getRadius() - p.getyPosition()) / p.getySpeed();
        }

        return (minTimeX < minTimeY) ? minTimeX : minTimeY;
    }

    private void updateParticlesPosition(double time) {
        for(Particle p : particles) {
            p.setxPosition(p.getxPosition() + p.getxSpeed() * time);
            p.setyPosition(p.getyPosition() + p.getySpeed() * time);
        }
    }

    private void saveCrashedParticle(Set<Particle> crashedParticles, Particle p) {
        crashedParticles.clear();
        crashedParticles.add(p);
    }

    private void saveCrashedParticle(Set<Particle> crashedParticles, Particle p, Particle o) {
        crashedParticles.clear();
        crashedParticles.add(p);
        crashedParticles.add(o);
    }

    private void updateSpeedCrashedParticles(Set<Particle> crashedParticles) {
        Iterator<Particle> it = crashedParticles.iterator();

        /* Crashed against wall */
        if(crashedParticles.size() == 1) {
            Particle p = it.next();
            if(crashedAgainstVerticalWall(p)) {
                p.setxSpeed(-p.getxSpeed());
            } else {
                p.setySpeed(-p.getySpeed());
            }
        }

        /* Crashed against other particle */
        if(crashedParticles.size() == 2) {
            Particle pi = it.next();
            Particle pj = it.next();

            //double dVdR = (p2.getxSpeed() - p1.getxSpeed()) * (p2.getxPosition() - p2.getxPosition()) + (p2.getySpeed() - p1.getySpeed()) * (p2.getyPosition() - p2.getyPosition());
            double dvx = pj.getxSpeed()-pi.getxSpeed();
            double dvy = pj.getySpeed()-pi.getySpeed();

            double dx = pj.getxPosition()-pi.getxPosition();
            double dy = pj.getyPosition()-pi.getyPosition();

            double dvdr = dvx*dx + dvy*dy;

            double phi = pi.getRadius() + pj.getRadius();
            double J = (2 * pi.getMass() * pj.getMass() * dvdr) / ( phi * (pi.getMass() + pj.getMass()) );
            double Jx = J * (pj.getxPosition() - pi.getxPosition()) / phi;
            double Jy = J * (pj.getyPosition() - pi.getyPosition()) / phi;

            pi.setxSpeed(pi.getxSpeed() + Jx / pi.getMass());
            pi.setySpeed(pi.getySpeed() + Jy / pi.getMass());
            pj.setxSpeed(pj.getxSpeed() - Jx / pj.getMass());
            pj.setySpeed(pj.getySpeed() - Jy / pj.getMass());
        }
    }

    private boolean crashedAgainstVerticalWall(Particle p) {
        double delta = 0.00000001;
        if(p.getxPosition()-delta <= p.getRadius()) {
            return true;
        }
        if(p.getxPosition() +delta>= boxSize - p.getRadius()) {
            return true;
        }
        return false;
    }

    private void closeBW() {
        if (bw != null) try {
            bw.flush();
            bw.close();
        } catch (IOException ioe2) {
            // just ignore it
        }
    }

    private boolean initalizeBW(String outPath) {
        try {

            bw = new BufferedWriter(new FileWriter(outPath+"/eventDriven" + LocalDateTime.now() + ".txt", true));
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String generateFileString(Set<Particle> allParticles){

        StringBuilder builder = new StringBuilder()
                .append(allParticles.size()+4)
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t vx\t vy\t\r\n");
        Set<Particle> limits =new HashSet<>();
        limits.addAll(
                Arrays.asList(new Particle(-1,0,0,0,0,0,0),
        new Particle(-2,0,boxSize,0,0,0,0),
        new Particle(-3,boxSize,0,0,0,0,0),
        new Particle(-4,boxSize,boxSize,0,0,0,0)));
        appendParticles(allParticles, builder);
        appendParticles(limits,builder);
        return builder.toString();
    }

    private void appendParticles(Set<Particle> allParticles, StringBuilder builder) {
        for(Particle current: allParticles){
            double vx = current.getxSpeed();
            double vy = current.getySpeed();
            builder.append(current.getId())
                    .append(" ")
                    .append(current.getxPosition())
                    .append(" ")
                    .append(current.getyPosition())
                    .append(" ")
                    .append(current.getRadius())
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
