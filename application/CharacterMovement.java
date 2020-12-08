package application;


import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Hold down an arrow key to have your car drive around the screen. Make sure to
 * avoid the other car! Hold down the shift key to have the driver step on the
 * gas.
 */
public class CharacterMovement extends Application {

	private static final double W = 800, H = 600;

	private static final String CAR_IMAGE_LOC = "https://i.imgur.com/V4G07Q8.png";
	private static final String CAR_IMAGE_LOC_2 = "https://i.imgur.com/MrFg7OU.png";

	private static final String RACETRACK_IMAGE_LOC = "https://i.imgur.com/ZLTZyqQ.png";
	private static final String TITLE_IMAGE_LOC = "https://i.imgur.com/Ma5LQ7t.png";
	private static final String GAMEOVER_IMAGE_LOC = "https://i.imgur.com/VRim9Ll.png";
	private static final String DIRECTIONS_IMAGE_LOC = "https://i.imgur.com/7HmavNl.png";
	private static final String HACKED_IMAGE_LOC = "https://i.imgur.com/2mXVzoA.png";
	
	private static final String MEDIA_URL = "music.wav";

	private Image car1Image;
	private playerCar playerCar;
	private Image car2Image;
	private enemyCar enemyCar;

	private Image racetrackImage;
	private ImageView racetrack;
	
	private Image titleImage;
	private ImageView titleScreen;
	private Image gameoverImage;
	private ImageView gameoverScreen;
	
	private Image directionsImage;
	private ImageView directions;
	
	private Image hackedImage;
	private ImageView hacked;

	// a surprise tool that will help us later...
	int hackCounter = 0;
	boolean running, goNorth, goSouth, goEast, goWest;

	@Override
	public void start(Stage stage) throws Exception {

		// creating the car images
		car1Image = new Image(CAR_IMAGE_LOC);
		playerCar = new playerCar(car1Image);

		car2Image = new Image(CAR_IMAGE_LOC_2);
		enemyCar = new enemyCar(car2Image);

		// creating the game background
		racetrackImage = new Image(RACETRACK_IMAGE_LOC);
		racetrack = new ImageView(racetrackImage);

		hackedImage = new Image(HACKED_IMAGE_LOC);
		hacked = new ImageView(hackedImage);
		hacked.setX(305);
		hacked.setY(0);
		hacked.setVisible(false);
		
		// creating the game group
		Group game = new Group(racetrack, playerCar, enemyCar, hacked);

		// moving cars to proper place and creating the game scene
		moveCarTo(W / 1.3, H / 2);
		enemyCar.relocate(W / 6, H / 2);
		Scene gameScene = new Scene(game, W, H);
		
		// it's been 84 years to figure this out...
	    // transition to scroll background vertically
	    TranslateTransition scrollingBackground = new TranslateTransition(Duration.seconds(1), racetrack);
	    scrollingBackground.setFromY(-600.0);
	    scrollingBackground.setToY(0.0);
	    scrollingBackground.setInterpolator(Interpolator.LINEAR);
	    scrollingBackground.setCycleCount(Animation.INDEFINITE);
	    ParallelTransition scrollingBackgroundTransition = new ParallelTransition(scrollingBackground);
	    scrollingBackgroundTransition.play();
		
		// creating the title screen with image background
		stage.setTitle("DRIFT STAGE");
		
		titleImage = new Image(TITLE_IMAGE_LOC);
		titleScreen = new ImageView(titleImage);
		directionsImage = new Image(DIRECTIONS_IMAGE_LOC);
		directions = new ImageView(directionsImage);
		Group title = new Group(titleScreen, directions);
		
		// creating the play button and adding it into the title screen
		Button startButton = new Button("PLAY");
		startButton.setMinSize(150, 45);
		startButton.setLayoutX(320);
		startButton.setLayoutY(370);
		startButton.setStyle("-fx-background-color: #ee2364;" + "-fx-font-size: 30;" + "-fx-text-fill: white;");
		startButton.setOnAction(e -> stage.setScene(gameScene));
		
		// creating the second quit button and adding it into the title screen
		Button quitButton = new Button("QUIT");
		quitButton.setMinSize(100, 45);
		quitButton.setLayoutX(340);
		quitButton.setLayoutY(450);
		quitButton.setStyle("-fx-background-color: #ee2364;" + "-fx-font-size: 30;" + "-fx-text-fill: white;");
		quitButton.setOnAction(e -> stage.close());
		title.getChildren().addAll(startButton, quitButton);
		
		Scene titleScreenScene = new Scene(title, 800, 600);
		
		// transition to scroll directions vertically
		directions.setX(550);
		TranslateTransition scrollingDirections = new TranslateTransition(Duration.seconds(10), directions);
	    scrollingDirections.setFromY(0);
	    scrollingDirections.setToY(-1200.0);
	    scrollingDirections.setInterpolator(Interpolator.LINEAR);
	    scrollingDirections.setCycleCount(Animation.INDEFINITE);
	    ParallelTransition scrollingDirectionsTransition = new ParallelTransition(scrollingDirections);
	    scrollingDirectionsTransition.play();

		// creating the game over screen with image background
		gameoverImage = new Image(GAMEOVER_IMAGE_LOC);
		gameoverScreen = new ImageView(gameoverImage);
		Group gameover = new Group(gameoverScreen);

		// creating the try again button and adding it into the game over screen
		Button gameOverButton = new Button("TRY AGAIN");
		gameOverButton.setMinSize(100, 100);
		gameOverButton.setLayoutX(275);
		gameOverButton.setLayoutY(300);
		gameOverButton.setStyle("-fx-background-color: white;" + "-fx-font-size: 40;" + "-fx-text-fill: blue;");
		gameOverButton.setOnAction(e -> stage.setScene(titleScreenScene));
		
		// creating the second quit button and adding it into the game over screen
		Button quit2Button = new Button("QUIT");
		quit2Button.setMinSize(80, 80);
		quit2Button.setLayoutX(325);
		quit2Button.setLayoutY(430);
		quit2Button.setStyle("-fx-background-color: white;" + "-fx-font-size: 40;" + "-fx-text-fill: blue;");
		quit2Button.setOnAction(e -> stage.close());
		gameover.getChildren().addAll(gameOverButton, quit2Button);
		
		Scene gameoverScreenScene = new Scene(gameover, 800, 600);
		
		// call to play music
		music(MEDIA_URL);
		
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					goNorth = true;
					break;
				case DOWN:
					goSouth = true;
					break;
				case LEFT:
					goWest = true;
					break;
				case RIGHT:
					goEast = true;
					break;
				case SHIFT:
					running = true;
					break;
				}
			}
		});

		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					goNorth = false;
					break;
				case DOWN:
					goSouth = false;
					break;
				case LEFT:
					goWest = false;
					break;
				case RIGHT:
					goEast = false;
					break;
				case SHIFT:
					running = false;
					break;
				}
			}
		});

		// should display title screen first in latest revision
		stage.setScene(titleScreenScene);
		stage.show();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				int dx = 0, dy = 0;
				if (!playerCar.checkHacked()) {
					if (goNorth)
						dy -= 1;
					if (goSouth)
						dy += 1;
					if (goEast)
						dx += 1;
					if (goWest)
						dx -= 1;
					if (running) {
						dx *= 3;
						dy *= 3;
					}
					
					// flips controls if playerCar is hacked
				} else {
					if (goNorth)
						dy += 1;
					if (goSouth)
						dy -= 1;
					if (goEast)
						dx -= 1;
					if (goWest)
						dx += 1;
					if (running) {
						dx *= 3;
						dy *= 3;
					}
				}
				
				moveCarBy(dx, dy);
				if (!playerCar.checkHacked()) {
					double distance = giveChase();
					// checks to see if player is within hacking distance, and adds to hacking
					// counter if so.
					if (distance <= 100.0) {
						hackCounter++;
					}
					if (hackCounter >= 1000) {
						(enemyCar).hack(playerCar);
						hackCounter = 0;
					}
					
				} else {
					hacked.setVisible(true);
					crash(playerCar);
					reCenter(enemyCar);
					hackCounter++;
					
					// checks if car has crashed (game over condition)
					if (playerCar.getLayoutX() <= 5 || playerCar.getLayoutX() >= 800) {
						// Ideally this next section will properly play the game-over screen, but no
						// promises.
						hacked.setVisible(false);
						stage.setScene(gameoverScreenScene);
						stage.show();
					}
					
					// resets hacked status if the player manages not to crash for long enough,
					// allowing game to continue w/o game over screen
					if (hackCounter >= 500) {
						hacked.setVisible(false);
						playerCar.setHacked(false);
					}
				}
			}
		};

		timer.start();

	}

	// method to play music from file
	private void music(String musicLocation) {

        try {
            //take WAV file as input for background music
            File musicPath = new File(musicLocation);
            
            //checks if the music file exists in project's path
            if(musicPath.exists()) {
                System.out.println("The game is playing: " + musicPath);
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else {
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
	
	private void moveCarBy(int dx, int dy) {
		if (dx == 0 && dy == 0)
			return;

		final double cx = playerCar.getBoundsInLocal().getWidth() / 2;
		final double cy = playerCar.getBoundsInLocal().getHeight() / 2;

		double x = cx + playerCar.getLayoutX() + dx;
		double y = cy + playerCar.getLayoutY() + dy;

		moveCarTo(x, y);
	}

	private void moveCarTo(double x, double y) {
		final double cx = playerCar.getBoundsInLocal().getWidth() / 2;
		final double cy = playerCar.getBoundsInLocal().getHeight() / 2;

		if (x - cx >= 0 && x + cx <= W && y - cy >= 0 && y + cy <= H) {
			playerCar.relocate(x - cx, y - cy);
		}
	}

	// Method designed to crash the player car as a result of being hacked. May be
	// changed as other hacking conditions are designed/implemented
	private void crash(playerCar playerCar) {
		double xPosition = playerCar.getLayoutX();
		double half = W / 2;
		double goal;
		
		if (xPosition <= half) {
			goal = 0;
		} else {
			goal = 956;
		}
		
		double borderDistance = goal - xPosition;
		int max = 300;
		int min = 100;
		Random randomNum = new Random();
		int randDistance = min + randomNum.nextInt(max);
		playerCar.relocate(xPosition + (borderDistance / randDistance), playerCar.getLayoutY());
	}

	private void reCenter(enemyCar enemyCar) {
		double xPosition = enemyCar.getLayoutX();
		double yPosition = enemyCar.getLayoutY();
		
		double xGoal = W / 6;
		double yGoal = H / 2;
		
		double xDistance = xGoal - xPosition;
		double yDistance = yGoal - yPosition;
		
		enemyCar.relocate(xPosition + (xDistance / 100), yPosition + (yDistance / 100));
	}

	// Method used to calculate how far the second car needs to move to chase the
	// player
	// Method then relocates second car accordingly. Returns the direct distance
	// between the center points as well
	// Needs to be modified to maintain distance from playerCar bc it's way easier
	// if they never touch. WIP at the moment.
	private double giveChase() {
		double C2x = enemyCar.getLayoutX();
		double C2y = enemyCar.getLayoutY();
		double C1x = playerCar.getLayoutX();
		double C1y = playerCar.getLayoutY();
		double xDistance = C1x - C2x;
		double yDistance = C1y - C2y;
		
		if (xDistance >= 0) {
			xDistance = xDistance - 45;
		} else {
			xDistance = xDistance + 45;
		}
		if (yDistance >= 0) {
			yDistance = yDistance - 45;
		} else {
			yDistance = yDistance + 45;
		}
		
		double compDistance = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
		enemyCar.relocate(C2x + (xDistance / 200), C2y + (yDistance / 200));
		return compDistance;
	}

	public static void main(String[] args) {
		launch(args);
	}
}