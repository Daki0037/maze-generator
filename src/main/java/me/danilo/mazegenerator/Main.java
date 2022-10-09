package me.danilo.mazegenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.danilo.mazegenerator.controllers.MazeController;

import java.net.URL;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		URL fxmlLocation = getClass().getResource("/fxml/Maze.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlLocation);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		
		MazeController mazeController = loader.getController();
		mazeController.setStage(primaryStage);
		
		primaryStage.setResizable(false);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		mazeController.onGenerate();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
}
