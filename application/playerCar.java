package application;




import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class playerCar extends ImageView{
	private boolean isHacked;
	
	public playerCar(Image carPic) {
		super(carPic);
		this.isHacked = false;
	}
	public void setHacked(boolean hacked) {
		this.isHacked = hacked;
	}
	public boolean getHacked() {
		return this.isHacked;
	}
}
