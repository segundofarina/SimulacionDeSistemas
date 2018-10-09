package ar.edu.itba.ss.CellIndex2.o;

import ar.edu.itba.ss.Particle;
import ar.edu.itba.ss.Vector;

import java.util.*;
import java.util.function.Function;

public class NeighbourCalculator {
    private double cellWidth;
    private Cell[][] grid;
    private int gridWidth;
    private int gridHeight;

    public NeighbourCalculator(double height, double width, double interactionRadius, double maxRadius) {
        this.cellWidth = 2 * maxRadius + interactionRadius;

        this.gridWidth = (int) Math.ceil(width / cellWidth);
        this.gridHeight = (int) Math.ceil(height / cellWidth);

        grid = new Cell[gridWidth][gridHeight];
        initializeGrid();
    }

    private void initializeGrid() {
        for(int i = 0; i < gridWidth; i++) {
            for(int j = 0; j <  gridHeight; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public Map<Particle, Set<Particle>> getNeighbours(Set<Particle> allParticles, Function<Particle, Vector> position) {
        Map<Particle, Set<Particle>> neighbours = new HashMap<>();

        clearGrid();

        addParticlesToGrid(allParticles, position);

        for(int i = 0; i < gridWidth; i++) {
            for(int j = 0; j < gridHeight; j++) {
                Set<Particle> cellNeighbours = getNearMolecules(grid[i][j]);
                for(Particle p : grid[i][j].getParticles()) {
                    if(neighbours.containsKey(p)) {
                        neighbours.get(p).addAll(cellNeighbours);
                    } else {
                        neighbours.put(p, cellNeighbours);
                    }

                    for(Particle other: cellNeighbours) {
                        if(neighbours.containsKey(other)) {
                            neighbours.get(other).add(p);
                        } else {
                            Set<Particle> aux = new HashSet<>();
                            aux.add(p);
                            neighbours.put(other, aux);
                        }
                    }
                }
            }
        }

        return neighbours;
    }

    private void addParticlesToGrid(Set<Particle> particles, Function<Particle, Vector> postiion) {
        for(Particle p : particles) {
            int x = (int)(postiion.apply(p).getX() / cellWidth);
            int y = (int)(postiion.apply(p).getY() / cellWidth);

            grid[x][y].addParticle(p);
        }
    }

    private Set<Particle> getNearMolecules(Cell field){
        Set<Particle> nearMolecules = new HashSet<>();
        nearMolecules.addAll(field.getParticles());

        if(field.getX() + 1 < gridWidth) {
            nearMolecules.addAll(grid[field.getX()+1][field.getY()].getParticles());
        }

        if(field.getY() + 1 < gridWidth) {
            nearMolecules.addAll(grid[field.getX()][field.getY()+1].getParticles());
        }

        if(field.getX() + 1 < gridWidth && field.getY() + 1 < gridHeight) {
            nearMolecules.addAll(grid[field.getX()+1][field.getY()+1].getParticles());
        }

        if(field.getX() + 1 < gridWidth && field.getY() - 1 >= 0) {
            nearMolecules.addAll(grid[field.getX()+1][field.getY()-1].getParticles());
        }

        return nearMolecules;
    }

    private void clearGrid() {
        for(int i = 0; i < gridWidth; i++) {
            for(int j = 0; j < gridHeight; j++) {
                grid[i][j].clearParticles();
            }
        }
    }
}
