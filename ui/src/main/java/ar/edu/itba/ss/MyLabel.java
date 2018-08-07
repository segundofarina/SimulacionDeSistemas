package ar.edu.itba.ss;

import javafx.scene.control.Label;

public class MyLabel extends Label {

	public MyLabel(String text, int x, int y, int width, int height){
		super();
		this.setText(text);
		this.setPrefSize(width,height);
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
}
