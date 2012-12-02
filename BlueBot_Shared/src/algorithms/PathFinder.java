package algorithms;

import java.util.List;

import bluebot.graph.Tile;

public interface PathFinder {
	
	public static int DIJKSTRA = 1;
	public static int ASTAR = 2;
	/**
	 * Find the shortestpath from a given tile to another given tile.
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Tile> findShortestPath(Tile from,Tile to);
	
	

}
