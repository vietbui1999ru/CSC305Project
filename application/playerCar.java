package application;



import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class playerCar {
	private boolean isHacked;
	
	public playerCar(Node car) {
		this.isHacked = false;
	}
	public void setHacked(boolean hacked) {
		this.isHacked = hacked;
	}
	public boolean getHacked() {
		return this.isHacked;
	}
}
