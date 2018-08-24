package ar.edu.itba.ss;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.awt.Point;
import java.util.List;

import static ar.edu.itba.ss.Dimensions.*;

public class Board extends Pane{

	private final Canvas[] board;
	private StoneImg img;
	private double dimension;
	List<Molecule> moleculeList;
	
	public Board(double dimension, List<Molecule> moleculeList){
		this.dimension = dimension;
		this.setPrefSize(TABLERO_ANCHO, TABLERO_ALTO);
		this.moleculeList = moleculeList;

		board = new Canvas[moleculeList.size()+1];
		img = new StoneImg();
		showBoard(-1);



	}



	public void showBoard(int selected) {
		
		for(Molecule molecule : moleculeList){
				drawMolecule(molecule,selected);
			}
		}


	private void drawMolecule(Molecule molecule, int selected) {
		double casilleroAncho = molecule.getRadius()/dimension*TABLERO_ANCHO;
		double casilleroAlto = molecule.getRadius()/dimension*TABLERO_ALTO;


		this.getChildren().remove(board[molecule.getId()]);
		board[molecule.getId()]=new Canvas(casilleroAncho,casilleroAlto);
		board[molecule.getId()].setTranslateX((double) molecule.getPosition().x/dimension*500.0);
		board[molecule.getId()].setTranslateY((double) molecule.getPosition().y/dimension*500.0);
		board[molecule.getId()].setOnMouseClicked(e->{
			double x=e.getSceneX();
			double y=e.getSceneY();
			System.out.println("clicked on "+x+" "+y);
			//consumeClick(x,y);
			showBoard(molecule.getId());
		} );

		this.getChildren().add(board[molecule.getId()]);
		Image img;
		if(molecule.getId() == selected){
			img = this.img.getRedStone();
		}
		else if(molecule.getNeighbours().contains(selected)) {
			 img = this.img.getBlueStone();
		}else {
			img = this.img.getBlackStone();
		}
		board[molecule.getId()].getGraphicsContext2D().drawImage(img, casilleroAncho*0.10, casilleroAlto*0.10, casilleroAncho*0.80, casilleroAlto*0.8);

	}

}
