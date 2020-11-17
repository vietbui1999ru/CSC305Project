package application;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

public class enemyCar extends ImageView implements hackable{
	private boolean isHacked;
	public enemyCar(Image carPic) {
		super(carPic);
		isHacked = false;
	}

	//Method that "hacks" the player car, provided that the proper conditions in main have been met. The hack begins! 
	public void hack(playerCar target) {
		target.setHacked(true);
	}

	@Override
	public void setHacked(boolean status) {
		isHacked = status;
		
	}

	@Override
	public boolean checkHacked() {
		return this.isHacked;
	}
}
