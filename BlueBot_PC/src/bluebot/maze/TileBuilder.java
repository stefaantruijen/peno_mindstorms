package bluebot.maze;

import bluebot.graph.Orientation;
import bluebot.graph.Tile;

public class TileBuilder {
	/**
	 * Build a tile for a given source string, x value and y value.
	 * 
	 * @param source
	 * @param x
	 * @param y
	 * @return
	 * 			A tile conform the source string convention.
	 */
	public static Tile getTile(String source,int x, int y){
		String[] tileElements = source.split("\\.");
		TileType tt = TileType.getType(tileElements[0]);
		if(tt != TileType.CROSS){
			return setUpTile(new Tile(x,y),tt,Orientation.getOrientation(tileElements[1]));
		}
		
		return new Tile(x,y);
		
		
	}
	/**
	 * Set up a given tile for a given TileType and a given orientation value
	 * @param tile
	 * @param type
	 * @param o
	 * @return
	 * 			The tile that has been set up.
	 */
	private static Tile setUpTile(Tile tile,TileType type,Orientation o){
		switch(type){
		case CORNER:
			tile.setAllBordersOpen(true);
			tile.setBorder(o,false);
			tile.setBorder(o.rotateCCW(),false);
			break;
		case CROSS:
			tile.setAllBordersOpen(true);
			break;
		case DEADEND:
			tile.setAllBordersOpen(false);
			tile.setBorder(o.getOpposite(), true);
			break;
		case STRAIGHT:
			tile.setAllBordersOpen(false);
			tile.setBorder(o, true);
			tile.setBorder(o.getOpposite(), true);
			break;
		case TSPLIT:
			tile.setAllBordersOpen(true);
			tile.setBorder(o, false);
			break;
		default:
			throw new IllegalArgumentException("Tiletype not supported");
		
		}
		
		return tile;
	}

}
