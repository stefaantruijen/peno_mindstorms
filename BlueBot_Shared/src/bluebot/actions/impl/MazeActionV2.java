package bluebot.actions.impl;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import algorithms.Dijkstra;
import bluebot.DriverException;
import bluebot.actions.Action;
import bluebot.actions.ActionException;
import bluebot.graph.Border;
import bluebot.graph.Graph;
import bluebot.graph.Orientation;
import bluebot.graph.Tile;
import bluebot.maze.Maze;
import bluebot.util.Timer;
import bluebot.util.Utils;



/**
 * {@link Action} implementation for the maze exploration algorithm
 */
public class MazeActionV2 extends Action {
	
	private Tile current,rendez_vous=null;
	private Maze maze;
	private Movement moves;
	private final int playerNumber,objectNumber;
	private final Graph graph = new Graph();
	private int twist;
	private final static int[] seesawBarcodes = new int[]{11,13,15,17,19,21};
	private final static int[] itemBarcodes = new int[]{0,1,2,3,4,5,6,7};
	private int teamNumber = -1;
	
	public MazeActionV2(final int playerNumber,final int objectNumber) {
		this.playerNumber = playerNumber;
		this.objectNumber = objectNumber;
		getDriver().setStartLocation(this.playerNumber);
		
	}
	/**
	 * Create a seesaw tile. 
	 * 
	 * @param tile
	 * @param dir
	 * @return
	 */
	private final Tile createSeesawTile(Tile tile,final Orientation dir,int seesawBarcode){
		Tile seesawTile = null;
		int x = tile.getX();
		int y = tile.getY();
		switch(dir){
			case EAST:
				seesawTile = maze.addTile(x+1, y);
				break;
			case NORTH:
				seesawTile = maze.addTile(x, y+1);
				break;
			case SOUTH:
				seesawTile = maze.addTile(x, y-1);
				break;
			case WEST:
				seesawTile = maze.addTile(x-1, y);
				break;
			default:
				break;
		
		}
		tile.setSeesaw(true);
		tile.setBarCode(seesawBarcode);
		return seesawTile;
	}
	
	private final Tile createItem(Tile tile, final Orientation dir, final int id) {
		tile = maze.addTile(tile.getX(), tile.getY(), dir);
		
		tile.setAllBordersOpen(false);
		switch (dir) {
			case NORTH:
				tile.setBorderSouth(Border.OPEN);
				break;
			case EAST:
				tile.setBorderWest(Border.OPEN);
				break;
			case SOUTH:
				tile.setBorderNorth(Border.OPEN);
				break;
			case WEST:
				tile.setBorderEast(Border.OPEN);
				break;
			default:
				throw new IllegalArgumentException("Invalid direction:  " + dir);
		}
		
		tile.setItemId(id);
		getDriver().sendTile(tile);
		
		final int x = tile.getX();
		final int y = tile.getY();
		if (dir != Orientation.NORTH) {
			final Tile neighbor = maze.addTile(x, y - 1);
			neighbor.setBorderNorth(Border.CLOSED);
			getDriver().sendTile(neighbor);
		}
		if (dir != Orientation.EAST) {
			final Tile neighbor = maze.addTile(x - 1, y);
			neighbor.setBorderEast(Border.CLOSED);
			getDriver().sendTile(neighbor);
		}
		if (dir != Orientation.SOUTH) {
			final Tile neighbor = maze.addTile(x, y + 1);
			neighbor.setBorderSouth(Border.CLOSED);
			getDriver().sendTile(neighbor);
		}
		if (dir != Orientation.WEST) {
			final Tile neighbor = maze.addTile(x + 1, y);
			neighbor.setBorderWest(Border.CLOSED);
			getDriver().sendTile(neighbor);
		}
		return tile;
	}
	
	private final boolean detectWall() throws InterruptedException {
		int distance = readSensorUltraSonic();
		if (distance < 25) {
			return true;
		}
		if (distance > 30) {
			return false;
		}
		
		getDriver().turnHeadCounterClockWise(5);
		distance = readSensorUltraSonic();
		if (distance < 25) {
			getDriver().turnHeadClockWise(5);
			return true;
		}
		
		getDriver().turnHeadClockWise(10);
		distance = readSensorUltraSonic();
		getDriver().turnHeadCounterClockWise(5);
		return (distance < 25);
	}
	
	private final boolean detectWallEast() throws InterruptedException {
		final Orientation head = getDirectionHead();
		switch (head) {
			case NORTH:
				lookRight();
				break;
			case EAST:
				// ignored
				break;
			case SOUTH:
				lookLeft();
				break;
			case WEST:
				lookAround();
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + head);
		}
		return detectWall();
	}
	
	private final boolean detectWallNorth() throws InterruptedException {
		final Orientation head = getDirectionHead();
		switch (head) {
			case NORTH:
				// ignored
				break;
			case EAST:
				lookLeft();
				break;
			case SOUTH:
				lookAround();
				break;
			case WEST:
				lookRight();
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + head);
		}
		return detectWall();
	}
	
	private final boolean detectWallSouth() throws InterruptedException {
		final Orientation head = getDirectionHead();
		switch (head) {
			case NORTH:
				lookAround();
				break;
			case EAST:
				lookRight();
				break;
			case SOUTH:
				// ignored
				break;
			case WEST:
				lookLeft();
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + head);
		}
		return detectWall();
	}
	
	private final boolean detectWallWest() throws InterruptedException {
		final Orientation head = getDirectionHead();
		switch (head) {
			case NORTH:
				lookLeft();
				break;
			case EAST:
				lookAround();
				break;
			case SOUTH:
				lookRight();
				break;
			case WEST:
				// ignored
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + head);
		}
		return detectWall();
	}
	
	protected void execute()
			throws ActionException, DriverException, InterruptedException {
		final Timer timer = new Timer();
		timer.reset();
		
		getDriver().resetOrientation();
		
		moves = new Movement();
		maze = new Maze();
		twist = 0;
		

		
		scanBorders(current = maze.addTile(0, 0));
		
		
		graph.setRootTile(current);
		
		
		for (Tile[] path; (path = getPathToNextTile(false)) != null;) {
			if (path.length == 1) {
				scanBorders(current);
				continue;
			}
			
			for (int i = 1; i < (path.length - 1); i++) {
				checkAborted();
				Tile next = path[i];
				if(barcodeCanBeSeesaw(next.getBarCode())){
					new SeesawAction().execute();
					i = i+3;
					this.current = path[i];
				}else{
					moveTo(path[i]);
				}
			}
			
			checkAborted();
			moveTo(path[path.length - 1]);

			Tile tile = current;
			

			checkAborted();
			scanBorders(tile);
			
			
			checkAborted();
			if (tile.canHaveBarcode()) {
				final int barcode = scanBarcode(tile);
				checkAborted();
				if (barcode > 0) {
					if(barcodeCanBeItemBarcode(barcode)){
						tile = createItem(tile, getDirectionBody(), barcode);
						
						checkAborted();
						
						if (isOurItem(barcode)) {
							this.pickUp();
							getDriver().sendItemFound(this.teamNumber);
						}
					}else if(barcodeCanBeSeesaw(barcode)){
						
						checkAborted();
						Tile tile1 = createSeesawTile(tile, getDirectionBody(),barcode);
						tile1.setSeesaw(true);
						getDriver().sendTile(tile1);
						Tile tile2 = createSeesawTile(tile1, getDirectionBody(),barcode);
						tile2.setSeesaw(true);
						getDriver().sendTile(tile2);
						Tile tile3 = createEndSeesawTile(tile2,getDirectionBody(),barcode);
						checkAborted();
						getDriver().sendTile(tile3);
						
						
					}else{
						//TODO:Checkpoint logica ?
					}
					
				}
			}
		}
		moveToRendezVous();
		/**final long timeExploration = timer.read();
		
		
		
		
		Tile a = graph.getCheckpointVertex();
		if (a == null) {
			a = current;
		} else {
			checkAborted();
			followPath(pathfinder.findShortestPath(current, a));
		}
		
		Tile b = graph.getFinishVertex();
		if (b == null) {
			b = graph.getVertex(0, 0);
		}
		
		checkAborted();
		timer.reset();
		followPath(pathfinder.findShortestPath(a, b));
		final long timeShortestPath = timer.read();
		
		final String msg = new StringBuilder()
				.append(Utils.formatDuration(timeExploration))
				.append(" to explore the maze.\n")
				.append(Utils.formatDuration(timeShortestPath))
				.append(" to reach the finish.").toString();
		getDriver().sendMessage(msg, "Finished");
		**/
	}
	/**
	 * Move to a specified rendez-vous tile.
	 * 
	 * @throws InterruptedException
	 * @throws ActionException
	 * @throws DriverException
	 * @throws IllegalStateException thrown when no path was found to the specified rendez-vous tile.
	 */
	private void moveToRendezVous()
			throws InterruptedException, ActionException, DriverException {
		resetHead();
		
		graph.addVerticies(maze.getTiles());
		final Dijkstra pathfinder = new Dijkstra(graph);
		while(rendez_vous == null){
			Thread.sleep(1000);
			System.out.println("Waiting for rendez vous tile");
		}
		
		List<Tile> path = pathfinder.findShortestPath(current, rendez_vous);
		if(path == null){
			throw new IllegalStateException("Path to rendez-vous tile was not found, graph incomplete or rendez-vous == null ?");
		}
		
		for (int i = 1; i < (path.size() - 1); i++) {
			checkAborted();
			Tile next = path.get(i);
			if(barcodeCanBeSeesaw(next.getBarCode())){
				new SeesawAction().execute();
				i = i+3;
				this.current = path.get(i);
			}else{
				moveTo(path.get(i));
			}
		}
		//TODO: at this point if everything went correctly we should have reached the rendez-vous tile and the game is finished.
	}
	
	/**
	 * Specify a rendez-vous tile.
	 * 
	 * @param t
	 */
	public synchronized void setRendezVousTile(Tile t){
		this.rendez_vous = t;
	}
	/**
	 * Interrupt this mazeaction and move to an earlier specified rendez-vous tile. see setRendezVousTile()
	 * @throws InterruptedException
	 * @throws ActionException
	 * @throws DriverException
	 * @throws IllegalStateException if a rendez-vous tile is not set. i.e. rendez-vous == null or if the rendez-vous tile is not in the graph.
	 */
	public synchronized void interruptExplorerMoveToRendezVous() throws InterruptedException, ActionException, DriverException{
		if(this.rendez_vous == null){
			throw new IllegalStateException("No rendez-vous tile was specified");
		}
		this.abort();
		this.moveToRendezVous();
	}
	
	/**
	 * Create the tile after the seesaw.
	 * 
	 * @param tile2
	 * @param directionBody
	 * @param barcode
	 * @return
	 */
	private Tile createEndSeesawTile(Tile tile2, Orientation directionBody,
			int barcode) {
		Tile endTile = maze.addTile(tile2.getX(), tile2.getY(), directionBody);
		endTile.setAllBordersOpen(true);
		switch(directionBody){
			case EAST:
				endTile.setBorderNorth(Border.CLOSED);
				endTile.setBorderSouth(Border.CLOSED);
				break;
			case NORTH:
				endTile.setBorderEast(Border.CLOSED);
				endTile.setBorderWest(Border.CLOSED);
				break;
			case SOUTH:
				endTile.setBorderEast(Border.CLOSED);
				endTile.setBorderWest(Border.CLOSED);
				break;
			case WEST:
				endTile.setBorderNorth(Border.CLOSED);
				endTile.setBorderSouth(Border.CLOSED);
				break;
			default:
				break;
		
		}
		
		
	    getDriver().sendTile(endTile);
		
		if(barcode==11||barcode == 13 ||barcode ==15){
			endTile.setBarCode(barcode+2);
		}else{
			endTile.setBarCode(barcode-2);
		}
		
		return endTile;
	}
	/**
	 * Check if this barcode points to our item.
	 * 
	 * @param barcode
	 * @return
	 */
	private boolean isOurItem(int barcode) {
		int ballnumber=-1;
		int teamNumber=-1;
		switch(barcode){
			case 0:
				ballnumber = 0;
				teamNumber = 0;
				break;
			case 1:
				ballnumber = 1;
				teamNumber = 0;
				break;
			case 2:
				ballnumber = 2;
				teamNumber = 0;
				break;
			case 3:
				ballnumber = 3;
				teamNumber = 0;
				break;
			case 4:
				ballnumber = 0;
				teamNumber = 1;
				break;
			case 5:
				ballnumber = 1;
				teamNumber = 1;
				break;
			case 6:
				ballnumber = 2;
				teamNumber = 1;
				break;
			case 7:
				ballnumber = 3;
				teamNumber = 1;
				break;
		}
		
		if(ballnumber == this.objectNumber){
			this.teamNumber=teamNumber;
			return true;
		}else{
			return false;
		}
		
	}
	/**
	 * Check if this barcode is a possible seesaw barcode, regardin the specified barcodes.
	 * 
	 * @param barcode
	 * @return
	 */
	private boolean barcodeCanBeSeesaw(int barcode) {
		for(int i : seesawBarcodes){
			if(i == barcode){
				return true;
			}
		}
		return false;
	}


	/**
	 * Check if this barcode can point to an item.
	 * 
	 * @param itemBarcode
	 * @return
	 */
	private boolean barcodeCanBeItemBarcode(int itemBarcode) {
		for (final int code : itemBarcodes) {
			if (code == itemBarcode) {
				return true;
			}
		}
		return false;
//		return (possiblePlayerId >= 7 && possiblePlayerId <= 10);
	}



	private void pickUp() throws ActionException, DriverException, InterruptedException {
		this.resetHead();
		this.executePickUp();
		//because the robot is turned aroud, the moves.turns added with 2
		moves.turns += 2;
		
//		this.executeWhiteLine();
//		this.getDriver().modifyOrientation();
//		
//		
//		int speed = this.getDriver().getSpeed();
//		this.getDriver().setSpeed(30);
//		this.getDriver().moveForward();
//		while(!getDriver().isPressed()){
//			
//		}
//		this.getDriver().stop();
//		//robot rijdt naar achter zodat hij vrij 180 graden kan draaien
//		this.getDriver().moveBackward(100F,true);
//		this.turnAround();
//		this.executeWhiteLine();
//		this.getDriver().setSpeed(speed);
//		this.getDriver().moveForward(200F, true);
//		this.getDriver().sendMQMessage("Got the package, to the choppa!");
		
	}



	private final List<Orientation> findBordersToScan(final Tile tile) {
		final LinkedList<Orientation> borders = new LinkedList<Orientation>();
		
		Orientation dir = getDirectionHead();
		if (tile.getBorder(dir.rotateCW()) == Border.UNKNOWN) {
			for (int i = 4; i > 0; i--) {
				if (tile.getBorder(dir) == Border.UNKNOWN) {
					borders.add(dir);
				}
				dir = dir.rotateCW();
			}
		} else {
			for (int i = 3; i > 0; i--) {
				if (tile.getBorder(dir) == Border.UNKNOWN) {
					borders.add(dir);
				}
				dir = dir.rotateCCW();
			}
		}
		
		return borders;
	}
	@Deprecated
	private void followPath(final List<Tile> path)
			throws ActionException, DriverException, InterruptedException {
		getDriver().setSpeed(100);
		
		ArrayList<Tile> straightLine = new ArrayList<Tile>();
		for (Tile t : path) {
			Tile currentTileInList = null;
			if (straightLine.size() > 0) {
				currentTileInList = straightLine.get(straightLine.size() - 1);
			} else {
				currentTileInList = this.current;
			}

			if (this.isOnStraighLine(currentTileInList, t)) {
				straightLine.add(t);
			} else {
				if (straightLine.size() > 0) {
					int distanceForward = (straightLine.size()) * 400;
					this.getDriver().moveForward(distanceForward, true);
					this.current = straightLine.get(straightLine.size() - 1);
				}
				this.moveTo(t);
				straightLine.clear();
				this.current = t;
			}

		}
		if (straightLine.size() > 0) {
			int distanceForward = (straightLine.size()) * 400;
			this.getDriver().moveForward(distanceForward, true);
			this.current = straightLine.get(straightLine.size() - 1);
		}
	}
	
	private final Orientation getDirectionBody() {
		return Orientation.forHeading(getDriver().getOrientation().getHeadingBody());
	}
	
	private final Orientation getDirectionHead() {
		final bluebot.util.Orientation pos = getDriver().getOrientation();
		
		final Orientation body = Orientation.forHeading(pos.getHeadingBody());
		switch (Orientation.forHeading(pos.getHeadingHead())) {
			case NORTH:
				return body;
			case EAST:
				return body.rotateCW();
			case SOUTH:
				return body.getOpposite();
			case WEST:
				return body.rotateCCW();
			default:
				throw new RuntimeException();
		}
	}
	
	private final Tile getNeighbor(final Tile tile, final Orientation dir) {
		final int x = tile.getX();
		final int y = tile.getY();
		switch (dir) {
			case NORTH:
				if (tile.getBorderNorth() == Border.OPEN) {
					return getTile(x, (y + 1));
				}
				break;
				
			case EAST:
				if (tile.getBorderEast() == Border.OPEN) {
					return getTile((x + 1), y);
				}
				break;
				
			case SOUTH:
				if (tile.getBorderSouth() == Border.OPEN) {
					return getTile(x, (y - 1));
				}
				break;
				
			case WEST:
				if (tile.getBorderWest() == Border.OPEN) {
					return getTile((x - 1), y);
				}
				break;
				
			default:
				throw new RuntimeException("Invalid direction:  " + dir);
		}
		return null;
	}
	
	private final Tile[] getPathToNextTile(boolean canGoOverSeesaw) {
		boolean wantsToGoOverSeesaw = false;
		Tile tile = current;
		if (needsExploration(tile)) {
			return new Tile[] { tile };
		}
		
		final LinkedList<Node> nodes = new LinkedList<Node>();
		final LinkedList<Tile> tiles = new LinkedList<Tile>();
		tiles.add(tile);
		
		Orientation dir = getDirectionBody();
		Node node = new Node(tile, dir);
		
		Orientation[] dirs = new Orientation[] {
				dir,
				dir.rotateCW(),
				dir.rotateCCW(),
				null
		};
		dirs[3] = dirs[1].rotateCW();
		for (final Orientation d : dirs) {
			final Tile t = getNeighbor(tile, d);
			if (t != null) {
				if(canGoOverSeesaw){
					nodes.add(new Node(t, d, node));
					tiles.add(t);
				} else if (!canGoOverSeesaw){
					if(t.isSeesaw()){
						wantsToGoOverSeesaw = true;
					} else if (!t.isSeesaw()){
						nodes.add(new Node(t, d, node));
						tiles.add(t);
					}
				}
			}
		}
		
		
		
		while (!nodes.isEmpty()) {
			node = nodes.remove(0);
			tile = node.tile;
			if (needsExploration(tile)) {
				return node.getPath();
			}
			
			dirs[0] = dir = node.dir;
			dirs[1] = dir.rotateCW();
			dirs[2] = dir.rotateCCW();
			for (final Orientation d : dirs) {
				final Tile t = getNeighbor(tile, d);
				if ((t != null) && !tiles.contains(t)) {
					nodes.add(new Node(t, d, node));
					tiles.add(t);
				}
			}
		}
		
		if(!canGoOverSeesaw && wantsToGoOverSeesaw){
			return this.getPathToNextTile(true);
		}
		
		return null;
	}
	
	private final Tile getTile(final int x, final int y) {
		return maze.getTile(x, y);
	}
	
	private final void lookAround() {
		if (twist > 0) {
			getDriver().turnHeadCounterClockWise(180);
			twist -= 2;
		} else {
			getDriver().turnHeadClockWise(180);
			twist += 2;
		}
	}
	
	private final void lookLeft() {
		if (twist > -2) {
			getDriver().turnHeadCounterClockWise(90);
			twist--;
		} else {
			getDriver().turnHeadClockWise(270);
			twist = 1;
		}
	}
	
	private final void lookRight() {
		if (twist < 2) {
			getDriver().turnHeadClockWise(90);
			twist++;
		} else {
			getDriver().turnHeadCounterClockWise(270);
			twist = -1;
		}
	}
	
	private final void moveForward()
			throws ActionException, DriverException, InterruptedException {
		moves.moveForward();
	}
	
	private final void moveTo(final Tile tile)
			throws ActionException, DriverException, InterruptedException {
		final int dx = (tile.getX() - this.current.getX());
		final int dy = (tile.getY() - this.current.getY());
		
		if (dy > 0) {
			travelNorth();
		} else if (dx > 0) {
			travelEast();
		} else if (dy < 0) {
			travelSouth();
		} else {
			travelWest();
		}
		
		this.current = tile;
	}
	
	private final boolean needsExploration(final Tile tile) {
		if (!tile.isExplored()) {
			return true;
		}
		if (tile.canHaveBarcode()) {
			return (tile.getBarCode() < 0);
		}
		return false;
	}
	
	private final void resetHead() {
		final Orientation body = getDirectionBody();
		final Orientation head = getDirectionHead();
		if (head != body) {
			if (head.getOpposite() == body) {
				lookAround();
			} else if (head.rotateCW() == body) {
				lookRight();
			} else {
				lookLeft();
			}
		}
	}
	
	private final int scanBarcode(final Tile tile) throws ActionException,
			DriverException, InterruptedException {
		int barcode = tile.getBarCode();
		if (barcode == 0) {
			// The tile has been checked before,
			// and it has no barcode
			return -1;
		}
		if (barcode > 0) {
			// The tile has been checked before,
			// and it has a valid barcode
			return barcode;
		}

		final ReadBarcodeAction reader = new ReadBarcodeAction(tile);

		final int speed = getDriver().getSpeed();
		reader.execute(getDriver());
		getDriver().setSpeed(speed);

		barcode = reader.getBarcode();
		if (barcode <= 0) {
			// Remember to not check this tile again
			tile.setBarCode(0);
			return -1;
		}

		tile.setBarCode(barcode);
		getDriver().sendTile(tile);
		
		return barcode;
	}
	
	private final void scanBorder(final Tile tile, final Orientation dir)
			throws InterruptedException {
		final int x = tile.getX();
		final int y = tile.getY();
		
		final Border border;
		final Tile neighbor;
		switch (dir) {
			case NORTH:
				border = (detectWallNorth() ? Border.CLOSED : Border.OPEN);
				tile.setBorderNorth(border);
				neighbor = maze.addTile(x, y + 1);
				neighbor.setBorderSouth(border);
				break;
			case EAST:
				border = (detectWallEast() ? Border.CLOSED : Border.OPEN);
				tile.setBorderEast(border);
				neighbor = maze.addTile(x + 1, y);
				neighbor.setBorderWest(border);
				break;
			case SOUTH:
				border = (detectWallSouth() ? Border.CLOSED : Border.OPEN);
				tile.setBorderSouth(border);
				neighbor = maze.addTile(x, y - 1);
				neighbor.setBorderNorth(border);
				break;
			case WEST:
				border = (detectWallWest() ? Border.CLOSED : Border.OPEN);
				tile.setBorderWest(border);
				neighbor = maze.addTile(x - 1, y);
				neighbor.setBorderEast(border);
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + dir);
		}
		updateTiles(tile, neighbor);
	}
	
	private final void scanBorders(final Tile tile)
			throws InterruptedException {
		for (final Orientation dir : findBordersToScan(tile)) {
			scanBorder(tile, dir);
		}
	}
	
	private boolean isOnStraighLine(Tile t1, Tile t2) {
		switch (getDirectionBody()) {
			case NORTH:
				return (t2.getY() == (t1.getY() + 1));
			case EAST:
				return (t2.getX() == (t1.getX() + 1));
			case SOUTH:
				return (t2.getY() == (t1.getY() - 1));
			case WEST:
				return (t2.getX() == (t1.getX() - 1));
			default:
				throw new RuntimeException();
		}
	}
	
	private final int readSensorUltraSonic() throws InterruptedException {
//		Thread.sleep(200);
		return getDriver().readSensorUltraSonic();
	}
	
	private final void travelEast()
			throws ActionException, DriverException, InterruptedException {
		final Orientation body = getDirectionBody();
		switch (body) {
			case NORTH:
				turnRight();
				break;
			case EAST:
				// ignored
				break;
			case SOUTH:
				turnLeft();
				break;
			case WEST:
				turnAround();
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + body);
		}
		moveForward();
	}
	
	private final void travelNorth()
			throws ActionException, DriverException, InterruptedException {
		final Orientation body = getDirectionBody();
		switch (body) {
			case NORTH:
				// ignored
				break;
			case EAST:
				turnLeft();
				break;
			case SOUTH:
				turnAround();
				break;
			case WEST:
				turnRight();
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + body);
		}
		moveForward();
	}
	
	private final void travelSouth()
			throws ActionException, DriverException, InterruptedException {
		final Orientation body = getDirectionBody();
		switch (body) {
			case NORTH:
				turnAround();
				break;
			case EAST:
				turnRight();
				break;
			case SOUTH:
				// ignored
				break;
			case WEST:
				turnLeft();
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + body);
		}
		moveForward();
	}
	
	private final void travelWest()
			throws ActionException, DriverException, InterruptedException {
		final Orientation body = getDirectionBody();
		switch (body) {
			case NORTH:
				turnLeft();
				break;
			case EAST:
				turnAround();
				break;
			case SOUTH:
				turnRight();
				break;
			case WEST:
				// ignored
				break;
			default:
				throw new RuntimeException("Invalid direction:  " + body);
		}
		moveForward();
	}
	
	private final void turnAround() {
		getDriver().turnRight(180F, true);
		moves.turns += 2;
	}
	
	private final void turnLeft() {
		getDriver().turnLeft(90F, true);
		moves.turns++;
	}
	
	private final void turnRight() {
		getDriver().turnRight(90F, true);
		moves.turns++;
	}
	
	private final void updateTiles(final Tile... tiles) {
		for (final Tile tile : tiles) {
			getDriver().sendTile(tile);
		}
	}
	
	
	
	
	
	
	
	
	
	
	/*
	private final class BarcodeScanner extends Thread {
		
		private final Object lock = new Object();
		
		private boolean alive = true;
		private Brightness lastColor;
		private float lastPos;
		private boolean scanning;
		private ArrayList<BarcodeStrip> strips;
		
		
		
		private final void execute() {
			if (strips == null) {
				strips = new ArrayList<BarcodeStrip>();
			} else {
				strips.clear();
			}
			
			lastColor = null;
			
			Brightness nextColor;
			float nextPos;
			for (scanning = true; scanning;) {
				nextColor = readColor();
				nextPos = getPosition();
				if (lastColor == null) {
					lastColor = nextColor;
					lastPos = nextPos;
				} else if (nextColor != lastColor) {
					if (lastColor != Brightness.GRAY) {
						strips.add(new BarcodeStrip(lastColor, lastPos, nextPos));
					}
					lastColor = nextColor;
					lastPos = nextPos;
				}
			}
		}
		
		private final float getPosition() {
			final bluebot.util.Orientation pos = getDriver().getOrientation();
			switch (Orientation.forHeading(pos.getHeadingBody())) {
				case NORTH:
				case SOUTH:
					return pos.getY();
				case EAST:
				case WEST:
					return pos.getX();
				default:
					throw new RuntimeException();
			}
		}
		
		private final Brightness readColor() {
			try {
				return getDriver().readSensorLightBrightness();
			} catch (final CalibrationException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public void run() {
			while (alive) {
				try {
					synchronized (lock) {
						lock.wait();
					}
					execute();
				} catch (final InterruptedException e) {
					alive = false;
					return;
				}
			}
		}
		
		public synchronized void startScanning() {
			scanning = true;
			synchronized (lock) {
				lock.notifyAll();
			}
		}
		
		@SuppressWarnings("unused")
		public synchronized int stopScanning() {
			scanning = false;
			
			if (strips.isEmpty()) {
				return -1;
			}
			
			final Barcode barcode = new Barcode();
			
			final BarcodeStrip first = strips.remove(0);
			if (first.color != Brightness.BLACK) {
				return -1;
			}
			for (int i = (first.width - 1); i > 0; i--) {
				barcode.addBlack();
				if (barcode.bits() == 6) {
					// The code 000000 is invalid
					return -1;
				}
			}
			
			for (final BarcodeStrip strip : strips) {
				for (int i = strip.width; i > 0; i--) {
					switch (strip.color) {
						case BLACK:
							barcode.addBlack();
							break;
						case WHITE:
							barcode.addWhite();
							break;
						default:
							throw new RuntimeException();
					}
					if (barcode.bits() == 6) {
						return barcode.getValue();
					}
				}
			}
			
			if (lastColor != Brightness.GRAY) {
				final BarcodeStrip last =
						new BarcodeStrip(lastColor, lastPos, getPosition());
				for (int i = last.width; i > 0; i--) {
					switch (last.color) {
						case BLACK:
							barcode.addBlack();
							break;
						case WHITE:
							barcode.addWhite();
							break;
						default:
							throw new RuntimeException();
					}
					if (barcode.bits() == 6) {
						return barcode.getValue();
					}
				}
			}
			
			return -1;
		}
		
	}
	
	
	
	
	
	private static final class BarcodeStrip {
		
		private static final float SINGLE = 20F;
		
		private Brightness color;
		private int width;
		
		
		public BarcodeStrip(final Brightness color,
				final float a, final float b) {
			this.color = color;
			this.width = Math.round(Math.abs(b - a) / SINGLE);
		}
		
	}
	*/
	
	
	
	
	
	private final class Movement {
		
		private int horizontal;
		private int turns;
		private int vertical;
		
		
		
		public void moveForward()
				throws ActionException, DriverException, InterruptedException {
			if (isTranslated()) {
				// The error on the rotation is too big
				// Execute white-line to correct this
				getDriver().moveForward(70F, true);
				resetHead();
				executeWhiteLine();
//				if (scan) {
//					scanner.startScanning();
//				}
				getDriver().moveForward(200F, true);
				reset(true);
/*
			} else if (isTranslated()) {
				// The error on the translation (X/Y) is too big
				// Find a white line, then use it to move back
				// to the center of a tile
				final int speed = getDriver().getSpeed();
				getDriver().moveForward(40F, true);
				getDriver().setSpeed(25);
				getDriver().moveForward();
				waitForLightSensor(Brightness.WHITE, true);
//				waitForLightSensor(Brightness.WHITE, false);
				getDriver().stop();
				getDriver().setSpeed(50);
				getDriver().moveForward(Robot.OFFSET_SENSOR_LIGHT, true);
				getDriver().setSpeed(speed);
//				if (scan) {
//					scanner.startScanning();
//				}
				getDriver().moveForward(200F, true);
				reset(false);
*/
			} else {
//				if (scan) {
//					getDriver().moveForward(Tile.SIZE, false);
//					waitForLightSensor(Brightness.WHITE, true);
//					waitForLightSensor(Brightness.WHITE, false);
//					scanner.startScanning();
//					waitForMoving(false);
//				} else {
					getDriver().moveForward(Tile.SIZE, true);
//				}
				/*
				switch (getDirectionBody()) {
					case NORTH:
					case SOUTH:
						horizontal++;
//						vertical++;
						break;
					case EAST:
					case WEST:
//						horizontal++;
						vertical++;
						break;
					default:
						throw new RuntimeException();
				}
				*/
			}
			horizontal++;
			vertical++;
			getDriver().modifyOrientation();
		}
		
		@SuppressWarnings("unused")
		private final boolean isRotated() {
			// 1 turn equals 90°
			return ((turns >= 4)
					|| (horizontal >= 3)
					|| (vertical >= 3));
		}
		
		private final boolean isTranslated() {
			// 1 move equals 400mm
			switch (getDirectionBody()) {
				case NORTH:
				case SOUTH:
					return (vertical >= 3);
				case EAST:
				case WEST:
					return (horizontal >= 3);
				default:
					throw new RuntimeException();
			}
		}
		
		private final void reset(final boolean rotation) {
			switch (getDirectionBody()) {
				case NORTH:
				case SOUTH:
					vertical = 0;
					break;
				case EAST:
				case WEST:
					horizontal = 0;
					break;
				default:
					throw new RuntimeException();
			}
			if (rotation) {
				turns = 0;
			}
		}
		
	}
	
	
	
	
	
	private static final class Node {
		
		private final Orientation dir;
		private final Node parent;
		private final Tile tile;
		
		
		private Node(final Tile tile, final Orientation dir) {
			this(tile, dir, null);
		}
		private Node(final Tile tile, final Orientation dir, final Node parent) {
			this.dir = dir;
			this.parent = parent;
			this.tile = tile;
		}
		
		
		
		public Tile[] getPath() {
			final int length = getPathLength();
			final Tile[] path = new Tile[length];
			
			Node node = this;
			for (int i = (length - 1); i >= 0; i--) {
				path[i] = node.tile;
				node = node.parent;
			}
			
			return path;
		}
		
		private final int getPathLength() {
			int length = 1;
			if (parent != null) {
				length += parent.getPathLength();
			}
			return length;
		}
		
	}
	
}