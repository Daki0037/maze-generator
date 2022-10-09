package me.danilo.mazegenerator.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.danilo.mazegenerator.objects.Path;
import me.danilo.mazegenerator.objects.Player;

public class GameController {

	@FXML
	private Pane mazePane;
	
	public int WIDTH = 500, HEIGHT = 500;
	private Path matrix[][];
	AnimationTimer timer;
	
	private double speed = 1;
	
	private boolean running = false;
	private double startX, startY;
	private Player player;
	private Stage stage;
	public boolean shouldReset = false;
	
	private boolean left = false, right = false, up = false, down = false;
	int currentX = 1, currentY = 1, length, boxWidth, boxHeight;
	
	int posX = 0;
	
	public GameController(Pane mazePane, Path matrix[][], int length) {
		this.mazePane = mazePane;
		this.matrix = matrix;
		this.length = length;
		
		startX = matrix[1][1].getPosX();
		startY = matrix[1][1].getPosY();
		
		boxWidth = boxHeight = WIDTH/length;
		
		player = new Player(startX, startY, boxWidth, boxHeight);
		mazePane.getChildren().add(player.getRectangle());
	}
	
	public void start() {
		if(running)
			timer.stop();
		
		player.setCurrentXY(1, 1);
		player.setPosX(matrix[1][1].getPosX());
		player.setPosY(matrix[1][1].getPosY());
		running = true;
		run();
	}
	
	public void stop() {
		if(!running)
			return;
		running = false;
		timer.stop();
		left = right = up = down = false;
	}
	
	public void run() {
		
		timer = new AnimationTimer() {

			private long lastUpdate = 0;
			
			@Override
			public void handle(long now) {
				if (now - lastUpdate >= 50_000_000) {
					update();
                    lastUpdate = now ;
                }
			}
		};
		
		timer.start();
	}

	public void update() {
		
		currentX = player.getCurrentX();
		currentY = player.getCurrentY();
		matrix[currentX][currentY].setFill(Color.ORANGE);
		
		if(currentX >= length-2 && currentY >= length-1) {
			shouldReset = true;
			return;
		}
		
		if(left) {
			int currentX = player.getCurrentX(),
				currentY = player.getCurrentY();
			
			if(matrix[currentX-1][currentY].getFill() == Color.WHITE || matrix[currentX-1][currentY].getFill() == Color.LIME) {
				currentX -= 1;
				player.setPosX(matrix[currentX][currentY].getPosX());
				player.setCurrentX(currentX);
			}
			else if(matrix[currentX-1][currentY].getFill() == Color.ORANGE) {
				matrix[currentX][currentY].setFill(Color.WHITE);
				currentX -= 1;
				player.setPosX(matrix[currentX][currentY].getPosX());
				player.setCurrentX(currentX);
			}
		}
		else if(right) {
			int currentX = player.getCurrentX(),
				currentY = player.getCurrentY();
				
				if(matrix[currentX+1][currentY].getFill() == Color.WHITE || matrix[currentX+1][currentY].getFill() == Color.LIME) {
					currentX += 1;
					player.setPosX(matrix[currentX][currentY].getPosX());
					player.setCurrentX(currentX);
				}
				else if(matrix[currentX+1][currentY].getFill() == Color.ORANGE) {
					matrix[currentX][currentY].setFill(Color.WHITE);
					currentX += 1;
					player.setPosX(matrix[currentX][currentY].getPosX());
					player.setCurrentX(currentX);
				}
		}
		else if(up) {
			int currentX = player.getCurrentX(),
				currentY = player.getCurrentY();
				
				if(matrix[currentX][currentY-1].getFill() == Color.WHITE || matrix[currentX][currentY-1].getFill() == Color.LIME) {
					currentY -= 1;
					player.setPosY(matrix[currentX][currentY].getPosY());
					player.setCurrentY(currentY);
				}
				else if(matrix[currentX][currentY-1].getFill() == Color.ORANGE) {
					matrix[currentX][currentY].setFill(Color.WHITE);
					currentY -= 1;
					player.setPosY(matrix[currentX][currentY].getPosY());
					player.setCurrentY(currentY);
				}
		}
		else if(down) {
			int currentX = player.getCurrentX(),
				currentY = player.getCurrentY();
				
				if(matrix[currentX][currentY+1].getFill() == Color.WHITE || matrix[currentX][currentY+1].getFill() == Color.LIME) {
					currentY += 1;
					player.setPosY(matrix[currentX][currentY].getPosY());
					player.setCurrentY(currentY);
				}
				else if(matrix[currentX][currentY+1].getFill() == Color.ORANGE) {
					matrix[currentX][currentY].setFill(Color.WHITE);
					currentY += 1;
					player.setPosY(matrix[currentX][currentY].getPosY());
					player.setCurrentY(currentY);
				}
		}
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
		
		keyListener();
	}
	
	public void keyListener() {
		
		stage.getScene().setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.W)
				up = true;
			if(e.getCode() == KeyCode.S)
				down = true;
			if(e.getCode() == KeyCode.A)
				left = true;
			if(e.getCode() == KeyCode.D)
				right = true;
			if(e.getCode() == KeyCode.R)
				reset();
		});
		
		stage.getScene().setOnKeyReleased(e -> {
			if(e.getCode() == KeyCode.W)
				up = false;
			if(e.getCode() == KeyCode.S)
				down = false;
			if(e.getCode() == KeyCode.A)
				left = false;
			if(e.getCode() == KeyCode.D)
				right = false;
		});
		
	}
	
	public void setLength(int length) {
		this.length = length;
		player.getRectangle().setWidth(WIDTH/length);
		player.getRectangle().setHeight(HEIGHT/length);
	}
	
	public void setMatrix(Path[][] matrix) {
		this.matrix = matrix;
	}
	
	public void reset() {
		player.setPosX(boxWidth);
		player.setPosY(boxHeight);
		player.setCurrentXY(1, 1);
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < length; j++) {
				if(matrix[i][j].getFill() == Color.ORANGE)
					matrix[i][j].setFill(Color.WHITE);
			}
		}
	}
}
