package bluebot.maze;



/**
 * 
 * @author Ruben Feyen
 */
public class MazeGenerator {
	
	public Maze generateMaze() {
		/*
		 * START
		 * 		 ___________________
		 * 		|   |       |   |   |
		 * 		|   |    ___|   |   |
		 * 		|   |           |   |
		 * 		|   |        ___|   |
		 * 		|   |   |           |
		 * 		|   |   |    ___    |
		 * 		|   |   |   |   |   |
		 * 		|   |___|   |   |   |
		 * 		|               |   |
		 * 		|_______________|___|
		 * 		                     GOAL
		 */
		
		final Maze maze = new Maze();
		
		Tile tile;
		
		tile = maze.addTile(0, 4);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(1, 4);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.OPEN);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(2, 4);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.OPEN);
		
		tile = maze.addTile(3, 4);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(4, 4);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(0, 3);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(1, 3);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.OPEN);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(2, 3);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.OPEN);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.OPEN);
		
		tile = maze.addTile(3, 3);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.OPEN);
		
		tile = maze.addTile(4, 3);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(0, 2);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(1, 2);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(2, 2);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.OPEN);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(3, 2);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.OPEN);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.OPEN);
		
		tile = maze.addTile(4, 2);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.OPEN);
		
		tile = maze.addTile(0, 1);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(1, 1);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(2, 1);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(3, 1);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(4, 1);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.OPEN);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(0, 0);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.OPEN);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.CLOSED);
		
		tile = maze.addTile(1, 0);
		tile.setBorderNorth(Border.CLOSED);
		tile.setBorderEast(Border.OPEN);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.OPEN);
		
		tile = maze.addTile(2, 0);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.OPEN);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.OPEN);
		
		tile = maze.addTile(3, 0);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.OPEN);
		
		tile = maze.addTile(4, 0);
		tile.setBorderNorth(Border.OPEN);
		tile.setBorderEast(Border.CLOSED);
		tile.setBorderSouth(Border.CLOSED);
		tile.setBorderWest(Border.CLOSED);
		
		return maze;
	}
	
	public long time() {
		final Maze maze = new MazeGenerator().generateMaze();
		
		final Tile start = maze.getTile(0, 4);
		final Tile goal = maze.getTile(4, 0);
		
		final int iterations = 1;
		
		long time = 0;
		for (int i = iterations; i > 0; i--) {
			time += timePathFinder(maze, start, goal);
		}
		
		return (time / iterations);
	}
	
	private static final long timePathFinder(final Maze maze,
			final Tile start, final Tile goal) {
		final long time = System.nanoTime();
		new PathFinder(maze).findPath(start, goal);
		return ((System.nanoTime() - time) / 1000L);
	}
	
}