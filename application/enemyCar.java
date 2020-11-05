package application;

import javafx.scene.image.ImageView;
import javafx.scene.Node;

public class enemyCar {
	// onAttack is designed to be the switch for the ai car/cars to either come
	// close but not touch the player or to run them off the road! If true, ATTACK!
	private boolean onAttack;
	public enemyCar(Node car) {
		this.onAttack = false;
	}

	public boolean getAttack() {
		return this.onAttack;
	}

	public void setAttack(boolean input) {
		this.onAttack = input;
	}
	//Method that "hacks" the player car, provided that the proper conditions in main have been met. The hack begins! 
	public void hack(double distance) {
		
	}
}
