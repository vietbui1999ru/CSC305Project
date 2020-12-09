package application;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IO implements gameIO {

	public IO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void music(String musicLocation) {

		try {
			// take WAV file as input for background music
			File musicPath = new File(musicLocation);

			// checks if the music file exists in project's path
			if (musicPath.exists()) {
				System.out.println("The game is playing: " + musicPath);
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				System.out.println("Can't find file");
			}

		} catch (UnsupportedAudioFileException ex) {
			System.out.println("The specified audio file is not supported.");
			ex.printStackTrace();
		} catch (LineUnavailableException ex) {
			System.out.println("Audio line for playing back is unavailable.");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Error playing the audio file.");
			ex.printStackTrace();
		}
	}

	public Button quitButton(Stage stage) {
		// TODO Auto-generated method stub
		Button quitButton = new Button("QUIT");
		quitButton.setMinSize(100, 45);
		quitButton.setLayoutX(340);
		quitButton.setLayoutY(450);
		quitButton.setStyle("-fx-background-color: #ee2364;" + "-fx-font-size: 30;" + "-fx-text-fill: white;");
		quitButton.setOnAction(e -> stage.close());

		return quitButton;
	}

	@Override
	public Button playButton(Stage stage, Scene gameScene) {
		Button startButton = new Button("PLAY");
		startButton.setMinSize(150, 45);
		startButton.setLayoutX(320);
		startButton.setLayoutY(370);
		startButton.setStyle("-fx-background-color: #ee2364;" + "-fx-font-size: 30;" + "-fx-text-fill: white;");
		startButton.setOnAction(e -> stage.setScene(gameScene));

		// TODO Auto-generated method stub
		return startButton;
	}

	@Override
	public void imageLoop(ImageView racetrack, int secs) {
		
		TranslateTransition scrollingBackground = new TranslateTransition(Duration.seconds(secs), racetrack);
		scrollingBackground.setFromY(-600.0);
		scrollingBackground.setToY(0.0);
		scrollingBackground.setInterpolator(Interpolator.LINEAR);
		scrollingBackground.setCycleCount(Animation.INDEFINITE);
		ParallelTransition scrollingBackgroundTransition = new ParallelTransition(scrollingBackground);
		scrollingBackgroundTransition.play();
	}

	@Override
	public Button gameOverButton(Stage stage, Scene titleScreenScene) {
		// TODO Auto-generated method stub
		Button gameOverButton = new Button("TRY AGAIN");
		gameOverButton.setMinSize(100, 100);
		gameOverButton.setLayoutX(275);
		gameOverButton.setLayoutY(300);
		gameOverButton.setStyle("-fx-background-color: white;" + "-fx-font-size: 40;" + "-fx-text-fill: blue;");
		gameOverButton.setOnAction(e -> stage.setScene(titleScreenScene));
		return gameOverButton;
	}

	@Override
	public Button quitOverButton(Stage stage) {
		Button quit2Button = new Button("QUIT");
		quit2Button.setMinSize(80, 80);
		quit2Button.setLayoutX(325);
		quit2Button.setLayoutY(430);
		quit2Button.setStyle("-fx-background-color: white;" + "-fx-font-size: 40;" + "-fx-text-fill: blue;");
		quit2Button.setOnAction(e -> stage.close());
		return quit2Button;
	}

	

}
