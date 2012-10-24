package bluebot.ui;


import java.util.EventListener;



/**
 * 
 * @author Ruben Feyen
 */
public interface JoystickListener extends EventListener {
	
	public void onJoystickBackward(boolean flag, boolean mod);
	
	public void onJoystickForward(boolean flag, boolean mod);
	
	public void onJoystickLeft(boolean flag, boolean mod);
	
	public void onJoystickRight(boolean flag, boolean mod);
	
	public void onJoystickStop();
	
}