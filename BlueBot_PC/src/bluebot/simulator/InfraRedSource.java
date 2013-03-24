package bluebot.simulator;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Interface providing InfraRedSource functionality.
 * 
 * The IR source is modeled as a point source radiating an IR signal uniformly in all directions.
 * 
 * Idea is that an object that implements this interface adds 
 * itself to a model at construction. That way it can be retrieved at any time.
 *  
 * @author Dieter
 */
public interface InfraRedSource {
	
	/**
	 * Should be called only once to add itself to the specified model. 
	 * 
	 * @throws 	NoSuchElementException
	 * 			If there is no proper model specified.
	 */
	public void addToModel() throws NoSuchElementException;
	
	/**
	 * Should be called only once to remove itself from the specified model 
	 * 	if the IR source no longer exists.
	 * 
	 * @throws 	NoSuchElementException
	 * 			If there is no proper model specified.
	 */
	public void removeFromModel() throws NoSuchElementException;
	
	/**
	 * Returns the x coordinate of this infrared source.
	 */
	public int getIRSourceX();
	
	/**
	 * Returns the y coordinate of this infrared source.
	 */
	public int getIRSourceY();
}
