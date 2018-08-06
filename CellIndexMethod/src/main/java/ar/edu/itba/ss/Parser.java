package ar.edu.itba.ss;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Parser {
    private static String[] staticFile;
    private static Scanner dinamicFile;

    public Parser(String[] staticFile, Scanner dinamicFile) {
        this.staticFile = staticFile;
        this.dinamicFile = dinamicFile;
    }

    public int parseN() {
        return Integer.parseInt(staticFile[1]);
    }

    public int parseL() { return Integer.parseInt(staticFile[2]); }

    public int parseM() {
        return Integer.parseInt(staticFile[3]);
    }

    public double parseRc() {
        return Double.parseDouble(staticFile[4]);
    }

    public Set<Molecule> parseMolecules() {

        Set<Molecule> molecules = new HashSet<>();
        int ratio;
        double x, y, vx, vy;
        Property<String> property;
        Point location;
        Point velocity;
        Molecule molecule;
        int n = Integer.parseInt(staticFile[0]);
        dinamicFile.next();

        for (int i = 5; i < n; i += 2) {
            ratio = Integer.parseInt(staticFile[i]);
            property = new Property<>(staticFile[i + 1]);
            x = Double.parseDouble(dinamicFile.next());
            y = Double.parseDouble(dinamicFile.next());
            vx = Double.parseDouble(dinamicFile.next());
            vy = Double.parseDouble(dinamicFile.next());
            location = new Point(x, y);
            velocity = new Point(vx, vy);
            molecule = new Molecule(ratio, property, location, velocity);
            molecules.add(molecule);
        }

        return molecules;
    }
}