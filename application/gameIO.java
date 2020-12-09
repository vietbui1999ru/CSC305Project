package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

interface gameIO {
	
	void music(String musicLocation);
	
	Button quitButton(Stage stage);
	
	Button playButton(Stage stage, Scene gameScene);
	
	Button gameOverButton(Stage stage, Scene titleSceneScreen);
	
	Button quitOverButton(Stage stage);
	
	void imageLoop(ImageView image, int secs);

	

	
	
}
