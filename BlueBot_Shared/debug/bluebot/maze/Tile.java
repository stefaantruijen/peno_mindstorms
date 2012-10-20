package bluebot.maze;



/**
 * 
 * @author Ruben Feyen
 */
public class Tile {
	
	private byte borders;
	private int x, y;
	
	
	public Tile(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Tile) {
			final Tile tile = (Tile)obj;
			return ((tile.x == x) && (tile.y == y));
		}
		return false;
	}
	
	private final Border getBorder(final int shift) {
		switch ((borders & (0x3 << shift)) >>> shift) {
			case 0:
				return Border.UNKNOWN;
			case 1:
				return Border.CLOSED;
			case 2:
				return Border.OPEN;
			default:
				// It should not be possible to reach this
				throw new RuntimeException("The universe has collapsed!");
		}
	}
	
	public Border getBorderEast() {
		return getBorder(2);
	}
	
	public Border getBorderNorth() {
		return getBorder(0);
	}
	
	public Border getBorderSouth() {
		return getBorder(4);
	}
	
	public Border getBorderWest() {
		return getBorder(6);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public int hashCode() {
		return hashCode(x, y);
	}
	
	public static int hashCode(final int x, final int y) {
		return (((y & 0xFFFF) << 16) | (x & 0xFFFF));
	}
	
	public boolean matches(final int x, final int y) {
		return ((this.x == x) && (this.y == y));
	}
	
	private final void setBorder(final int shift, final Border border) {
//		if (getBorder(shift) == border) {
//			return;
//		}
		
		final int mask = (~(0x3 << shift) & 0xFF);
		
		final int value;
		switch (border) {
			case CLOSED:
				value = 1;
				break;
			case OPEN:
				value = 2;
				break;
			case UNKNOWN:
				value = 0;
				break;
			default:
				throw new IllegalArgumentException("Invalid border:  " + border);
		}
		
		borders = (byte)((borders & mask) | (value << shift));
	}
	
	public void setBorderEast(final Border border) {
		setBorder(2, border);
	}
	
	public void setBorderNorth(final Border border) {
		setBorder(0, border);
	}
	
	public void setBorderSouth(final Border border) {
		setBorder(4, border);
	}
	
	public void setBorderWest(final Border border) {
		setBorder(6, border);
	}
	
}