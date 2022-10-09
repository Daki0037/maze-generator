package me.danilo.mazegenerator.objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {

	private int WIDTH = 500, HEIGHT = 500;
	
	private double posX, posY, width, height;
	private int currentX = 1, currentY = 1;
	private Rectangle rectangle;
	
	public Player(double posX, double posY, double playerWidth, double playerHeight) {
		this.width = playerWidth;
		this.height = playerHeight;
		rectangle = new Rectangle(posX, posY, playerWidth, playerHeight);
		rectangle.setFill(Color.ORANGE);
	}
	
	public double getPosX() {
		return posX;
	}
	
	public double getPosY() {
		return posY;
	}
	
	public void setPosX(double posX) {
		this.posX = posX;
		rectangle.setX(posX);
	}
	
	public void setPosY(double posY) {
		this.posY = posY;
		rectangle.setY(posY);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public int getCurrentX() {
		return currentX;
	}
	
	public int getCurrentY() {
		return currentY;
	}
	
	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}
	
	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}
	
	public void setCurrentXY(int currentX, int currentY) {
		this.currentX = currentX;
		this.currentY = currentY;
	}
}
