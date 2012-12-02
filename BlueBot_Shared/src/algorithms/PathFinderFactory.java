package algorithms;

import bluebot.graph.Graph;

public class PathFinderFactory {
	/**
	 * Create a PathFinder for a given maze and pathfinder choice.
	 * 
	 * @param maze
	 * @param choice
	 * @return
	 */
	public static PathFinder createPathFinder(Graph maze,int choice){
		switch(choice){
			case 1:
				return new Dijkstra(maze);
			case 2:
				return new AStar(maze,new ManhattanHeuristic());
		}
		
		throw new IllegalArgumentException("Give a valid pathfinder choice.");
	}

}
