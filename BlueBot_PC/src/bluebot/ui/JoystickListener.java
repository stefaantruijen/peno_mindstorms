package bluebot.ui;


import java.util.EventListener;



/**
 * 
 * @author Ruben Feyen
 */
public interface JoystickListener extends EventListener {
	
	public void onJoystickBackward(boolean flag);
	
	public void onJoystickForward(boolean flag);
	
	public void onJoystickLeft(boolean flag);
	
	public void onJoystickRight(boolean flag);
	
	public void onJoystickStop();
	
}