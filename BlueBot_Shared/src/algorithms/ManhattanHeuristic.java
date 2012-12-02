package algorithms;

import bluebot.graph.Tile;
/**
 * Represents a Manhattan heuristic.
 * 
 * @author Incalza Dario
 *
 */
public class ManhattanHeuristic implements HeuristicFunction{

	@Override
	public int determineHeuristicValue(Tile from, Tile to) {
		return (Math.abs(from.getX()-to.getX()) + Math.abs(from.getY()-to.getY())*5);
	}

}
