package application;




import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class playerCar extends ImageView implements hackable{
	private boolean isHacked;
	
	public playerCar(Image carPic) {
		super(carPic);
		this.isHacked = false;
	}
	public void setHacked(boolean hacked) {
		this.isHacked = hacked;
	}
	@Override
	public boolean checkHacked() {
		return this.isHacked;
	}
	
	public void hack(playerCar car) {
		//this part left blank because no use for the hack method on a playercar has yet been determined
	}
}
