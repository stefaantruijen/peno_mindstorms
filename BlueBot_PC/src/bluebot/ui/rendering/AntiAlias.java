package bluebot.ui.rendering;



/**
 * 
 * @author Ruben Feyen
 */
public enum AntiAlias {
	OFF	(1),
	X2	(2),
	X4	(4);
	
	private int count;
	
	
	private AntiAlias(final int count) {
		this.count = count;
	}
	
	
	
	public int getCount() {
		return count;
	}
	
}