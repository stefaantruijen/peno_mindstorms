package bluebot.simulator;

import java.util.ArrayList;


/**
 * A singleton class that temporarily functions as InfraRed model to hold all IR sources.
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
	 */
	public IRModel(){
		this.sources = new ArrayList<InfraRedSource>();
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
