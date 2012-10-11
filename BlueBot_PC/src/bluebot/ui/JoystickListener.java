package bluebot.ui;


import java.util.EventListener;



/**
 * 
 * @author Ruben Feyen
 */
public interface JoystickListener extends EventListener {
	
	public void onControllerBackward(boolean flag);
	
	public void onControllerForward(boolean flag);
	
	public void onControllerLeft(boolean flag);
	
	public void onControllerRight(boolean flag);
	
	public void onControllerStop();
	
}