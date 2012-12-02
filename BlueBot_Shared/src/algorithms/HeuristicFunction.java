package algorithms;

import bluebot.graph.Tile;
/**
 * Interface for a heuristic function.
 * @author h4oxer
 *
 */
public interface HeuristicFunction {
	
	public int determineHeuristicValue(Tile from,Tile to);

}
