package bluebot.simulator;

import java.util.NoSuchElementException;


/**
 * A virtual infrared ball implementation that has a position.
 * 
 * @author Dieter
 */
public class VirtualInfraredBall implements InfraRedSource {
	/**
	 * The x of this VirtualInfraredBall object.
	 */
	private int x;

	/**
	 * The y of this VirtualInfraredBall object.
	 */
	private int y;


	/**
	 * Creates a new VirtualInfraredBall with given x and y coordinates;
	 * 
	 * @param 	x
	 * 			The x coordinate of the new IRball.
	 * @param	y
	 * 			The y coordinate of the new IRball.
	 */
	public VirtualInfraredBall(int x, int y) {
		setX(x);
		setY(y);
		addToModel();
	}

	/**
	 * Sets the value of the x of VirtualInfraredBall if the given value is valid. 
	 * 
	 * @param 	x
	 *			The x to set.
	 * @post 	The given value is the current value of the x of this VirtualInfraredBall.
	 * @throws 	IllegalArgumentException
	 *			If the given argument is not a valid x.
	 *			| !isValidX(x)
	 */
	public void setX(int x)
			throws IllegalArgumentException {
		if (!isValidX(x)) {
			throw new IllegalArgumentException(
					"The argument ("
							+ x
							+ ") is not a valid agrument of the field x from the class VirtualInfraredBall");
		}
		this.x = x;
	};

	/**
	 * Check whether the given x is a valid x for all the objects of VirtualInfraredBall.
	 * @param 	x
	 *			The x to check.
	 * @return	True if and only if the given value is not null, has the correct type, ...
	 */
	public static boolean isValidX(int x) {
		//TODO: specific constraints for this field?
		return true;
	}

	/**
	 * Sets the value of the y of VirtualInfraredBall if the given value is valid. 
	 * 
	 * @param 	y
	 *			The y to set.
	 * @post 	The given value is the current value of the y of this VirtualInfraredBall.
	 * @throws 	IllegalArgumentException
	 *			If the given argument is not a valid y.
	 *			| !isValidY(y)
	 */
	public void setY(int y)
			throws IllegalArgumentException {
		if (!isValidY(y)) {
			throw new IllegalArgumentException(
					"The argument ("
							+ y
							+ ") is not a valid agrument of the field y from the class VirtualInfraredBall");
		}
		this.y = y;
	};

	/**
	 * Check whether the given y is a valid y for all the objects of VirtualInfraredBall.
	 * @param 	y
	 *			The y to check.
	 * @return	True if and only if the given value is not null, has the correct type, ...
	 */
	public static boolean isValidY(int y) {
		//TODO: specific constraints for this field?
		return true;
	}
	
	@Override
	public void addToModel() throws NoSuchElementException {
		IRModel model = IRModel.getInstance();
		if(model == null){
			throw new NoSuchElementException("There is no model to add myself to!");
		}
		model.addSource(this);
	}

	@Override
	public void removeFromModel() throws NoSuchElementException {
		IRModel model = IRModel.getInstance();
		if(model == null){
			throw new NoSuchElementException("There is no model to remove myself from!");
		}
		model.removeSource(this);
	}

	/**
	 * Returns the X coordinate of this VirtualInfraredBall.
	 */
	@Override
	public int getIRSourceX() {
		return x;
	}

	/**
	 * Returns the Y coordinate of this VirtualInfraredBall.
	 */
	@Override
	public int getIRSourceY() {
		return y;
	}

}
