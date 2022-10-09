package me.danilo.mazegenerator.controllers;

import java.util.LinkedList;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.danilo.mazegenerator.objects.Path;

public class MazeController {

	public double WIDTH = 500, HEIGHT = 500;
	private int matrixI = 0, matrixJ = 0;
	
	//Length oznacava broj polja koja lavirint ima, Depth povecava kompleksnost.
	public int length = 4, depth = 15, whileCounter = 0, scoreCounting = 0;
	
	public double boxWidth = WIDTH / length, boxHeight = HEIGHT / length;
	
	private boolean left, right, up, down;
	
	private int startX = 1, startY = 1, currentX = startX, currentY = startY;
	private Stage stage;
	
	@FXML
	private Pane mazePane;
	@FXML
	private Text scoreCounter;
	
	Path matrix[][];
	LinkedList<Path> openFields = new LinkedList<Path>();
	LinkedList<Integer> availableDirections = new LinkedList<Integer>();
	int endPointX = 0, endPointY = 0;
	
	private GameController game;
	
	@FXML
	public void initialize() {
		
		matrix = new Path[length][length];
		for(int i = 0; i < WIDTH; i += boxWidth) {
			for(int j = 0; j < HEIGHT; j += boxHeight) {
				Path path = new Path(i, j, boxWidth, boxHeight);
				path.setMatrix(matrixI, matrixJ);
				matrix[matrixI][matrixJ++] = path;
				mazePane.getChildren().add(path.getRect());
			}
			matrixI++;
			matrixJ = 0;
		}
		
		matrix[1][1].setFill(Color.WHITE);
		matrix[length-2][length-1].setFill(Color.WHITE);
		openFields.add(matrix[1][1]);
		
		//Podesavanje igre
		game = new GameController(mazePane, matrix, length);
		game.setLength(length);
		
		new AnimationTimer() {

			private long lastUpdate = 0;
			
			@Override
			public void handle(long now) {
				if (now - lastUpdate >= 50_000_000) {
					if(game.shouldReset) {
						for(int i = 0; i < length; i++)
							for(int j = 0; j < length; j++)
								matrix[i][j].setFill(Color.BLACK);
								
						game.shouldReset = false;
						if(length < 500)
							length++;
						game.stop();
						scoreCounting++;
						onGenerate();
						scoreCounter.setText(Integer.toString(scoreCounting));
						game.start();
					}
                    lastUpdate = now ;
                }
			}
			
		}.start();
	}
	
	//Ova funkcija se poziva klikom na dugme Generate, gde se za pocetak generise glavni put, a nakon toga svi ostali sporedni putevi.
	@FXML
	public void onGenerate() {
		openFields.clear();
		
		//Pocetak
		matrixI = matrixJ = 0;
		
		matrix = null;
		
		boxWidth = Math.floor(WIDTH/length);
		boxHeight = Math.floor(HEIGHT/length);
		
		matrix = new Path[length][length];
		for(double i = 0; i < length; i++) {
			for(double j = 0; j < length; j++) {
				Path path = new Path(i*boxWidth, j*boxHeight, boxWidth, boxHeight);
				path.setMatrix(matrixI, matrixJ);
				matrix[matrixI][matrixJ++] = path;
				mazePane.getChildren().add(path.getRect());
			}
			matrixI++;
			matrixJ = 0;
		}
		
		game.setMatrix(matrix);
		game.setLength(length);
		//Kraj
		
		for(int i = 0; i < length; i++)
			for(int j = 0; j < length; j++)
				matrix[i][j].setFill(Color.BLACK);
		
		startX = startY = 1;
		currentX = currentY = 1;
		
		//Postavljanje i dodavanje pocetne tacke
		matrix[1][1].setFill(Color.WHITE);
		openFields.add(matrix[1][1]);
		
		while(currentX != length-2 || currentY != length-2)
			mazeGeneration();
		matrix[length-2][length-1].setFill(Color.LIME);
		
		//Dodavanje praznih putanja
		addSubPaths();
		
		game.setStage(stage);
		//Pokretanje game loop-a
		game.start();
	}
	
	//Ova funkcija vrsi generisanje glavnog puta, koji vodi od pocetka do kraja. U drugoj funkciji se generisu sporedni putevi.
	public void mazeGeneration() {
		
		whileCounter++;
		
		if(whileCounter > (length*length)) {
			hardReset();
			return;
		}
		
		checkDirections(startX, startY);
		int direction = generateDirection(),
			pathLength = (int)(Math.random()*8)+2;
		
		if(direction == 0 && currentY >= endPointY) {
			for(int i = 0; i < pathLength; i++) {
				if(checkBorders(currentX-i, currentY) || isBlockWall(currentX-i, currentY))
					break;
				matrix[currentX-i][currentY].setFill(Color.WHITE);
				startX = currentX-i;
			}
			currentX = startX;
		}
		else if(direction == 1) {
			for(int i = 1; i < pathLength; i++) {
				if(checkBorders(currentX+i, currentY) || isBlockWall(currentX+i, currentY))
					break;
				matrix[currentX+i][currentY].setFill(Color.WHITE);
				openFields.add(matrix[currentX+i][currentY]);
				startX = currentX+i;
			}
			currentX = startX;
			endPointX = currentX;
		}
		else if(direction == 2 && currentX >= endPointX) {
			for(int i = 1; i < pathLength; i++) {
				if(checkBorders(currentX, currentY-i) || isBlockWall(currentX, currentY-i))
					break;
				matrix[currentX][currentY-i].setFill(Color.WHITE);
				openFields.add(matrix[currentX][currentY-i]);
				startY = currentY-i;
			}
			currentY = startY;
		}
		else if(direction == 3) {
			for(int i = 1; i < pathLength; i++) {
				if(checkBorders(currentX, currentY+i) || isBlockWall(currentX, currentY+i))
					break;
				matrix[currentX][currentY+i].setFill(Color.WHITE);
				openFields.add(matrix[currentX][currentY+i]);
				startY = currentY+i;
			}
			currentY = startY;
			endPointY = currentY;
		}
	}
	
	//Dodavanje sporednih putanja na glavni put kroz lavirint
	public void addSubPaths() {

			for(int z = 0; z < depth; z++) {
				for(int j = 0; j < openFields.size(); j += 2) {
					Path p = openFields.get(j);
					checkDirections(p.getMatrixI(), p.getMatrixJ());
					
					if(left == false && right == false && up == false && down == false)
						continue;
					
					currentX = p.getMatrixI();
					currentY = p.getMatrixJ();
					startX = currentX;
					startY = currentY;
					
					//System.out.println("Levo: " + left + " Desno: " + right + " Gore: " + up + " Dole: " + down);
					
					int pathLength = (int)(Math.random()*8)+2;
					openFields.remove(j);
					
					if(left) {
						for(int i = 1; i < pathLength; i++) {
							if(checkBorders(currentX-i, currentY) || isBlockWall(currentX-i, currentY))
								break;
							else if(matrix[currentX-i-1][currentY+1].getFill() == Color.WHITE || matrix[currentX-i-1][currentY-1].getFill() == Color.WHITE)
								break;
							matrix[currentX-i][currentY].setFill(Color.WHITE);
							openFields.add(matrix[currentX-i][currentY]);
							startX = currentX-i;
						}
						currentX = startX;
					}
					if(right) {
						for(int i = 1; i < pathLength; i++) {
							if(checkBorders(currentX+i, currentY) || isBlockWall(currentX+i, currentY))
								break;
							else if(matrix[currentX+i+1][currentY+1].getFill() == Color.WHITE || matrix[currentX+i+1][currentY-1].getFill() == Color.WHITE)
								break;
							matrix[currentX+i][currentY].setFill(Color.WHITE);
							openFields.add(matrix[currentX+i][currentY]);
							startX = currentX+i;
						}
						currentX = startX;
					}
					if(up) {
						for(int i = 1; i < pathLength; i++) {
							if(checkBorders(currentX, currentY-i) || isBlockWall(currentX, currentY-i))
								break;
							else if(matrix[currentX-1][currentY-i-1].getFill() == Color.WHITE || matrix[currentX+1][currentY-i-1].getFill() == Color.WHITE)
								break;
							matrix[currentX][currentY-i].setFill(Color.WHITE);
							openFields.add(matrix[currentX][currentY-i]);
							startY = currentY-i;
						}
						currentY = startY;
					}
					if(down) {
						for(int i = 1; i < pathLength; i++) {
							if(checkBorders(currentX, currentY+i) || isBlockWall(currentX, currentY+i))
								break;
							else if(matrix[currentX+1][currentY+i+1].getFill() == Color.WHITE || matrix[currentX-1][currentY+i+1].getFill() == Color.WHITE)
								break;
							matrix[currentX][currentY+i].setFill(Color.WHITE);
							openFields.add(matrix[currentX][currentY+i]);
							startY = currentY+i;
						}
						currentY = startY;
					}
				}
			}
	}
	
	//Proveravanje mogucih puteva oko koordinate
	public void checkDirections(int i, int j) {
		up = down = left = right = true;
		
		if(checkBorders(i-1, j) || matrix[i-1][j].getFill() == Color.WHITE)
			left = false;
		if(checkBorders(i+1, j) || matrix[i+1][j].getFill() == Color.WHITE)
			right = false;
		if(checkBorders(i, j-1) || matrix[i][j-1].getFill() == Color.WHITE)
			up = false;
		if(checkBorders(i, j+1) || matrix[i][j+1].getFill() == Color.WHITE)
			down = false;
	}
		
	public boolean checkBorders(int i, int j) {
		if(i == 0 || i == length-1 || j == 0 || j == length-1)
			return true;
		return false;
	}
	
	public int generateDirection() {
		availableDirections.clear();

		/*if(currentX == length-2)
			return 3;*/
		
		if(left && currentY < length-3 && currentY > 3)
			availableDirections.add(0);
		if(right)
			availableDirections.add(1);
		if(up && currentX < 22)
			availableDirections.add(2);
		if(down)
			availableDirections.add(3);
		
		if(availableDirections.isEmpty())
			return -1;
		
		int randNum = (int)(Math.random()*availableDirections.size());
		
		return availableDirections.get(randNum);
	}
	
	public boolean isBlockWall(int i, int j) {
		
		if(i-1 < 0 || j-1 < 0 || i == length-1)
			return false;
		
		if(matrix[i-1][j].getFill() == Color.WHITE && matrix[i][j-1].getFill() == Color.WHITE)
			return true;
		else if(matrix[i+1][j].getFill() == Color.WHITE && matrix[i][j+1].getFill() == Color.WHITE)
			return true;
		else if(matrix[i+1][j].getFill() == Color.WHITE && matrix[i][j-1].getFill() == Color.WHITE)
			return true;
		else if(matrix[i-1][j].getFill() == Color.WHITE && matrix[i][j+1].getFill() == Color.WHITE)
			return true;
		else if(matrix[i+1][j].getFill() == Color.WHITE && matrix[i-1][j].getFill() == Color.WHITE)
			return true;
		else if(matrix[i][j+1].getFill() == Color.WHITE && matrix[i][j-1].getFill() == Color.WHITE)
			return true;
		
		return false;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void hardReset() {
		openFields.clear();
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < length; j++) {
				matrix[i][j].setFill(Color.BLACK);
			}
		}
		whileCounter = 0;
	}
	
}
