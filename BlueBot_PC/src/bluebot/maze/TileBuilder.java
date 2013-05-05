package bluebot.maze;


import bluebot.graph.Orientation;
import bluebot.graph.Tile;

import java.util.Map;



public class TileBuilder {
	
	private static Map<Byte,String> tileDb = null;
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
		Tile tile = new Tile(x,y);
		if( tt == TileType.CLOSED){
			tile.setAllBordersOpen(false);
		}
		else if(tt == TileType.CROSS){
			tile.setAllBordersOpen(true);
			
		}else{
			if( tt == TileType.STRAIGHT){
				if(tileElements.length > 2){
					tile.setBarCode(Integer.parseInt(tileElements[2]));
				}else{
					tile.setBarCode(-1); //if a straight tile has no barcode the 3th element in the source string is not given
				}
				
			}else if( tt == TileType.DEADEND && tileElements.length>2){
				//tile is deadend and has object on it
				tile.setHasItem(true);
			}
			tile = setUpTile(tile,tt,Orientation.getOrientation(tileElements[1]));
		}
		String possibleStartPosition = tileElements[tileElements.length-1];
		if(possibleStartPosition.length()==3){
			if(tt != TileType.STRAIGHT && tt!= TileType.CLOSED && tt != TileType.SEESAW ){
				tile.setStartPlayerId(Integer.parseInt(new String(new char[]{possibleStartPosition.charAt(1)})));
				char orient = possibleStartPosition.charAt(2);
				if(orient == 'N'){
					tile.setStartOrientation(0);
				}else if(orient == 'E'){
					tile.setStartOrientation(1);
				}else if(orient == 'S'){
					tile.setStartOrientation(2);
				}else if(orient == 'W'){
					tile.setStartOrientation(3);
				}else{
					throw new IllegalStateException("Parse error : wrong orientation or orientation not given on a start position.");
				}
				tile.setStartPosition(true);
			}else{
				throw new IllegalStateException("Parse error : starting position is on a straigh/closed/seesaw tile.");
			}
		}
		
		return tile;
		
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
		case CLOSED:
			tile.setAllBordersOpen(false);
			break;
		case SEESAW:
			tile.setAllBordersOpen(false);
			tile.setBorder(o,true);
			tile.setBorder(o.getOpposite(),true);
			break;
		default:
			throw new IllegalArgumentException("TileBuilder error : Tiletype not supported");
		
		}
		
		return tile;
	}
	
	public static String fromTileToString(Tile t){
		if(tileDb == null){
			MazeReader reader = new MazeReader();
			reader.parseMaze("allTiles.txt");
			tileDb = reader.getTileDb();
		}
		
		String tile = tileDb.get(t.getByteRepresentation());
		if(t.isSeesaw() && tile.contains("Straight")){
			tile = tile.replace("Straight", "Seesaw");
		}
		
		if(!t.isSeesaw() && !tile.contains("Straight")){
			tile = tile.replace("Seesaw", "Straight");
		}
		
		if(t.getBarCode()>-1){
			tile = tile.concat("."+t.getBarCode());
		}
		System.out.println(tile);
		return tile;
		
		
	}

}
