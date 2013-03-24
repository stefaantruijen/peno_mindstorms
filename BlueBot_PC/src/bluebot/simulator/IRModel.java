package bluebot.simulator;

import java.util.ArrayList;


/**
 * A singleton class that temporairly functions as InfraRed model to hold all IR sources.
 * 
 * This can (an should) be implemented in the WorldModel.
 * @author Dieter
 *
 */
public class IRModel {

	private static IRModel model = new IRModel();
	private ArrayList<InfraRedSource> sources;
	
	/**
	 * Makes a new IRModel.
	 * 
	 * @throws 	IllegalStateException
	 * 			If there is already an IRModel. 
	 * 			(since this class is implemented as a singleton)
	 */
	public IRModel() throws IllegalStateException{
		if(getInstance() != null){
			throw new IllegalStateException("The IRModel should only be created once!");
		}
		this.sources = new ArrayList<InfraRedSource>();
	}
	
	/**
	 * Returns the single existing instance of the IRModel.
	 */
	public static IRModel getInstance(){
		return model;
	}
	
	/**
	 * Returns a list with all the know sources of this IRModel.
	 */
	public ArrayList<InfraRedSource> getAllIRSoucres(){
		return new ArrayList<InfraRedSource>(sources);
	}
	
	/**
	 * Adds the given infrared source to this model.
	 * 
	 * @param 	irs
	 * 			The source to add.
	 */
	public void addSource(InfraRedSource irs){
		sources.add(irs);
	}
	
	/**
	 * Removes the given infrared source from this model.
	 * 
	 * @param 	irs
	 * 			The source to remove.
	 */
	public void removeSource(InfraRedSource irs){
		sources.remove(irs);
	}
}
