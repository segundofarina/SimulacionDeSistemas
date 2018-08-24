package ar.edu.itba.ss;


import javafx.scene.control.Button;

public class MyButton extends Button{
	
	public MyButton(String text, int x, int y, int width, int height){
		super();
		this.setText(text);
		this.setPrefSize(width,height);
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
	
}
