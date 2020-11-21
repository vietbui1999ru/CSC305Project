package application;


import java.util.Random;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Hold down an arrow key to have your car drive around the screen. Make sure to
 * avoid the other car! Hold down the shift key to have the driver step on the
 * gas.
 */
public class CharacterMovement extends Application {

	private static final double W = 956, H = 740;

	private static final String CAR_IMAGE_LOC = "https://i.imgur.com/V4G07Q8.png";

	private static final String CAR_IMAGE_LOC_2 = "https://i.imgur.com/MrFg7OU.png";

//    blue car - https://i.imgur.com/9bLztIl.png

	private static final String SCENE_IMAGE_LOC = "https://i.imgur.com/Vh3pzrL.png";

//    private static final String PAUSE_LOC = 
//    		"https://i.imgur.com/n7HbHdB.png";

	private Image carImage;
	private playerCar car;

	private Image car2Image;
	private enemyCar car2;

	private Image raceImage;
	private Node race;

//    private Image pauseImage;
//    private Node pause;

	// a surprise tool that will help us later...
	int hackCounter = 0;
	boolean running, goNorth, goSouth, goEast, goWest;

	@Override
	public void start(Stage stage) throws Exception {

		// creating the car images
		carImage = new Image(CAR_IMAGE_LOC);
		car = new playerCar(carImage);

		car2Image = new Image(CAR_IMAGE_LOC_2);
		car2 = new enemyCar(car2Image);

		// creating the game background
		raceImage = new Image(SCENE_IMAGE_LOC);
		race = new ImageView(raceImage);
		
//        pauseImage = new Image(PAUSE_LOC);
//        pause = new ImageView(pauseImage);

		// creating the game group
		Group game = new Group(race, car, car2);

		// moving cars to proper place and creating the game scene
		moveCarTo(W / 1.3, H / 2);
		car2.relocate(W / 6, H / 2);
		Scene gameScene = new Scene(game, W, H);
		
		// it's been 84 years to figure this out...
	    // translation to scroll background vertically
	    TranslateTransition scrollingBackground = new TranslateTransition(Duration.seconds(1), race);
	    scrollingBackground.setFromY(-740);
	    scrollingBackground.setToY(0.0);
	    scrollingBackground.setInterpolator(Interpolator.LINEAR);
	    scrollingBackground.setCycleCount(Animation.INDEFINITE);
	    ParallelTransition scrollingBackgroundTransition = new ParallelTransition(scrollingBackground);
	    scrollingBackgroundTransition.play();
		
		// creating the title screen with image background
		stage.setTitle("DRIFT STAGE");
		Group root1 = new Group();
		ImageView titleScreenBackground = new ImageView(getClass().getResource("titlescreen.png").toExternalForm());
		root1.getChildren().add(titleScreenBackground);

		// creating the play button and adding it into the title screen
		Button startButton = new Button("PLAY");
		startButton.setMinSize(200, 100);
		startButton.setLayoutX(335);
		startButton.setLayoutY(550);
		startButton.setStyle("-fx-background-color: #ee2364;" + "-fx-font-size: 40;" + "-fx-text-fill: white;");
		startButton.setOnAction(e -> stage.setScene(gameScene));
		root1.getChildren().add(startButton);
		Scene titleScreen = new Scene(root1, 956, 740);

		// creating the game over screen with image background
		Group root2 = new Group();
		ImageView gameOverBackground = new ImageView(getClass().getResource("gameoverscreen.jpg").toExternalForm());
		root2.getChildren().add(gameOverBackground);

		// creating the try again button and adding it into the game over screen
		Button gameOverButton = new Button("TRY AGAIN");
		gameOverButton.setMinSize(200, 100);
		gameOverButton.setLayoutX(345);
		gameOverButton.setLayoutY(550);
		gameOverButton.setStyle("-fx-background-color: white;" + "-fx-font-size: 40;" + "-fx-text-fill: blue;");
		gameOverButton.setOnAction(e -> stage.setScene(titleScreen));
		root2.getChildren().add(gameOverButton);

		// creating the music and failing spectacularly
//		Media music = new Media(getClass().getResource("music.mp3").toExternalForm()); 
//        MediaPlayer player = new MediaPlayer(music);
//        MediaView mediaView = new MediaView(player);
//        root2.getChildren().add(mediaView);
//        player.play();
		Scene gameOverScreen = new Scene(root2, 956, 740);

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
		stage.setScene(titleScreen);
		stage.show();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				int dx = 0, dy = 0;
				if (!car.checkHacked()) {
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
				if (!car.checkHacked()) {
					double distance = giveChase();
					// checks to see if player is within hacking distance, and adds to hacking
					// counter if so.
					if (distance <= 100.0) {
						hackCounter++;
					}
					if (hackCounter >= 1000) {
						(car2).hack(car);
						hackCounter = 0;
					}
					
				} else {
					crash(car);
					reCenter(car2);
					hackCounter++;
					
					// checks if car has crashed (game over condition)
					if (car.getLayoutX() <= 50 || car.getLayoutX() >= 906) {
						// Ideally this next section will properly play the game-over screen, but no
						// promises.
						stage.setScene(gameOverScreen);
						stage.show();
					}
					
					// resets hacked status if the player manages not to crash for long enough,
					// allowing game to continue w/o game over screen
					if (hackCounter >= 500) {
						car.setHacked(false);
					}
				}
			}
		};

		timer.start();

	}

	private void moveCarBy(int dx, int dy) {
		if (dx == 0 && dy == 0)
			return;

		final double cx = car.getBoundsInLocal().getWidth() / 2;
		final double cy = car.getBoundsInLocal().getHeight() / 2;

		double x = cx + car.getLayoutX() + dx;
		double y = cy + car.getLayoutY() + dy;

		moveCarTo(x, y);
	}

	private void moveCarTo(double x, double y) {
		final double cx = car.getBoundsInLocal().getWidth() / 2;
		final double cy = car.getBoundsInLocal().getHeight() / 2;

		if (x - cx >= 0 && x + cx <= W && y - cy >= 0 && y + cy <= H) {
			car.relocate(x - cx, y - cy);
		}
	}

	// Method designed to crash the player car as a result of being hacked. May be
	// changed as other hacking conditions are designed/implemented
	private void crash(playerCar car) {
		double xPosition = car.getLayoutX();
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
		car.relocate(xPosition + (borderDistance / randDistance), car.getLayoutY());
	}

	private void reCenter(enemyCar car) {
		double xPosition = car.getLayoutX();
		double yPosition = car.getLayoutY();
		
		double xGoal = W / 6;
		double yGoal = H / 2;
		
		double xDistance = xGoal - xPosition;
		double yDistance = yGoal - yPosition;
		
		car.relocate(xPosition + (xDistance / 100), yPosition + (yDistance / 100));
	}

	// Method used to calculate how far the second car needs to move to chase the
	// player
	// Method then relocates second car accordingly. Returns the direct distance
	// between the center points as well
	// Needs to be modified to maintain distance from playerCar bc it's way easier
	// if they never touch. WIP at the moment.
	private double giveChase() {
		double C2x = car2.getLayoutX();
		double C2y = car2.getLayoutY();
		double C1x = car.getLayoutX();
		double C1y = car.getLayoutY();
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
		car2.relocate(C2x + (xDistance / 200), C2y + (yDistance / 200));
		return compDistance;
	}

	public static void main(String[] args) {
		launch(args);
	}
}