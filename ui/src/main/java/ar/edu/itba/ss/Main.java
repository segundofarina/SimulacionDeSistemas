package ar.edu.itba.ss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

//        List<Molecule> list = new ArrayList<>();
//
//
//        List<Integer> neighbors = new ArrayList<Integer>();
//
//        neighbors.add(2);
//        neighbors.add(3);
//        neighbors.add(4);
//
//        list.add(new Molecule(new Point(0,2),0,1, neighbors));
//        list.add(new Molecule(new Point(7,4),1,1, Collections.EMPTY_LIST));
//        list.add(new Molecule(new Point(11,2),2, 1,Collections.EMPTY_LIST));
//        list.add(new Molecule(new Point(11,3),3,1, Collections.EMPTY_LIST));
//        list.add(new Molecule(new Point(11,4),4,1, Collections.EMPTY_LIST));
//        list.add(new Molecule(new Point(4,9),5,1, Collections.EMPTY_LIST));
//        list.add(new Molecule(new Point(11,11),6,1, Collections.EMPTY_LIST));

        primaryStage.setScene(new Scene(new Board(12,new Parser().getMolecules())));
        primaryStage.setTitle("CellIndex");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
