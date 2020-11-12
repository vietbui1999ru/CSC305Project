package application;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

public class enemyCar extends ImageView{
	// onAttack is designed to be the switch for the computer car/cars to either come
	// close but not touch the player or to run them off the road! If true, ATTACK!
	private boolean onAttack;
	public enemyCar(Image carPic) {
		super(carPic);
		this.onAttack = false;
	}

	public boolean getAttack() {
		return this.onAttack;
	}

	public void setAttack(boolean input) {
		this.onAttack = input;
	}
	//Method that "hacks" the player car, provided that the proper conditions in main have been met. The hack begins! 
	public void hack(playerCar target) {
		target.setHacked(true);
		this.setAttack(true);
	}
}
