package bluebot.simulator;



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
	 * @throws 	IllegalArgumentException
	 * 			If there is no proper model specified.
	 */
	public void addToModel(IRModel model) throws IllegalArgumentException;
	
	/**
	 * Should be called only once to remove itself from the specified model 
	 * 	if the IR source no longer exists.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			If there is no proper model specified.
	 */
	public void removeFromModel(IRModel model) throws IllegalArgumentException;

	
	/**
	 * Returns the x coordinate of this infrared source.
	 */
	public int getIRSourceX();
	
	/**
	 * Returns the y coordinate of this infrared source.
	 */
	public int getIRSourceY();

}
