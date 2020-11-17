package application;

public interface hackable {
  
	void setHacked(boolean status);
  
	boolean checkHacked();
	
	void hack(playerCar car);
}
