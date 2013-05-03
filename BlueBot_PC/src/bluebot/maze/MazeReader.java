package bluebot.maze;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bluebot.graph.Graph;
import bluebot.graph.Tile;

public class MazeReader {
	
	
	private Map<Byte,String> tileDb;
	
	public static void main(String ... arg0){
		MazeReader mr = new MazeReader();
		mr.parseMaze("/Users/h4oxer/Desktop/Maze.txt");
	}
	/**
	 * Parse a text file with maze information for a given path.
	 * 
	 * @param path
	 * @return
	 * 			A graph representation of the maze.
	 */
	public Graph parseMaze(String path){
		
		Graph maze = new Graph();
		tileDb = new HashMap<Byte,String>();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(path));
			String[] size = in.readLine().split("\\s+");
			int xLimit = Integer.valueOf(size[0]);
			int yLimit = Integer.valueOf(size[1])-1;
			String str;
		    while ((str = in.readLine()) != null && yLimit >= 0) {
		    	if(!str.startsWith("#") && !str.equals("")){
			      for(Tile t : this.parseRow(str,xLimit,yLimit)){
			    	  maze.addVertex(t);
			      }
			      yLimit--;
		    	}
		    }
		    in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maze;
		
		
	}
	/**
	 * Parse a single row from the text file. i.e. a row in the maze.
	 * 
	 * @param row
	 * @param xLimit
	 * @param y
	 * @return
	 * 			A list containing all the tiles for one row (one fixed y value)
	 */
	private List<Tile> parseRow(String row,int xLimit, int y){
		List<Tile> rowList = new ArrayList<Tile>();
		int x = 0;
		int i = 0;
		String[] tilesources = row.split("\\s+");
		while(x < xLimit){
			if(tilesources[i].startsWith("#")){
				return null;
			}
			if(!tilesources[i].equals("")){
				Tile tile = TileBuilder.getTile(tilesources[i], x, y);
				rowList.add(tile);
				tileDb.put(tile.getByteRepresentation(),tilesources[i]);
				x++;
			}
			
			i++;
		}
//		for(Tile t : rowList){
//			System.out.println(t);
//		}
		return rowList;
	}
	public Map<Byte, String> getTileDb() {
		return tileDb;
	}
	public void setTileDb(Map<Byte, String> tileDb) {
		this.tileDb = tileDb;
	}
	
	
}
