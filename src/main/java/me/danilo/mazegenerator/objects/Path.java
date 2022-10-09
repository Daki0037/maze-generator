package me.danilo.mazegenerator.objects;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Path {

	private Rectangle rect;
	private int matrixI = 0, matrixJ = 0;
	private double boxWidth, boxHeight;
	private double posX, posY;
	
	public Path(double posX, double posY, double boxWidth, double boxHeight) {
		rect = new Rectangle(posX, posY, boxWidth, boxHeight);
		rect.setFill(Color.BLACK);
		
		this.posX = posX;
		this.posY = posY;
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
	}
	
	public void setFill(Color color) {
		rect.setFill(color);
	}
	
	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public double getPosX() {
		return posX;
	}
	
	public double getPosY() {
		return posY;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public void setMatrix(int matrixI, int matrixJ) {
		this.matrixI = matrixI;
		this.matrixJ = matrixJ;
	}
	
	public int getMatrixI() {
		return matrixI;
	}
	
	public int getMatrixJ() {
		return matrixJ;
	}
	
	public Paint getFill() {
		return rect.getFill();
	}
}
