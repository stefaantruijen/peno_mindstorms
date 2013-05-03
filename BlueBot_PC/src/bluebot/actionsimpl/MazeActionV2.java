package bluebot.actionsimpl;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lejos.robotics.pathfinding.DijkstraPathFinder;

import algorithms.Dijkstra;
import bluebot.actions.Action;
import bluebot.core.Controller;
import bluebot.graph.Border;
import bluebot.graph.Direction;
import bluebot.graph.Graph;
import bluebot.graph.Orientation;
import bluebot.graph.Tile;
import bluebot.maze.Maze;
import bluebot.maze.MazeCallback;
import bluebot.maze.MazeListener;
import bluebot.maze.MazeMerger;
import bluebot.maze.TileBuilder;
import bluebot.operations.Operation;
import bluebot.operations.OperationException;
import bluebot.sensors.CalibrationException;
import bluebot.util.Timer;



/**
 * {@link Action} implementation for the maze exploration algorithm
 */
public class MazeActionV2 extends Operation{
	
	/**
	 * DEFAULT NUMBERS
	 */
	private static final int DEFAULT_TEAMNUMBER = -1;
	private static final boolean DEFAULT_MERGESUCCESS = false;
	//-------------
	private Tile current=null;
	private Maze maze;
	private Movement moves;
	private final int playerNumber,objectNumber;
	private final Graph graph = new Graph();
	private int twist;
	private final static int[] seesawBarcodes = new int[]{11,13,15,17,19,21};
	private final static int[] itemBarcodes = new int[]{0,1,2,3,4,5,6,7};
	private int teamNumber = DEFAULT_TEAMNUMBER;
	private MazeMerger mazeMerger;
	private boolean mergeSuccess = DEFAULT_MERGESUCCESS;
	
	private MazeCallback mazeListener;
	
	public MazeActionV2(final int playerNumber,final int objectNumber, final MazeCallback mazeListener) {
		this.playerNumber = playerNumber;
		this.objectNumber = objectNumber;
		this.mazeMerger = new MazeMerger();
		this.mazeListener = mazeListener;
	}
	
	/**
	 * Get this mazeMerger
	 * Used to add tiles from teammate.
	 */
	public MazeMerger getMazeMerger() {
		return this.mazeMerger;
	}
	
	/**
	 * Create a tile that has an item on it (with itemid = id)
	 * and sends tile and the neighbor of the tile to the mazelistener
	 * and to the mazemerger.
	 * @param tile
	 * @param dir
	 * @param id
	 * @return
	 */
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
		mazeListener.sendTile(tile);
		
		final int x = tile.getX();
		final int y = tile.getY();
		if (dir != Orientation.NORTH) {
			final Tile neighbor = maze.addTile(x, y - 1);
			neighbor.setBorderNorth(Border.CLOSED);
			addTileToMazeMergerAndTeammate(neighbor);
		}
		if (dir != Orientation.EAST) {
			final Tile neighbor = maze.addTile(x - 1, y);
			neighbor.setBorderEast(Border.CLOSED);
			addTileToMazeMergerAndTeammate(neighbor);
		}
		if (dir != Orientation.SOUTH) {
			final Tile neighbor = maze.addTile(x, y + 1);
			neighbor.setBorderSouth(Border.CLOSED);
			addTileToMazeMergerAndTeammate(neighbor);
		}
		if (dir != Orientation.WEST) {
			final Tile neighbor = maze.addTile(x + 1, y);
			neighbor.setBorderWest(Border.CLOSED);
			addTileToMazeMergerAndTeammate(neighbor);
		}
		addTileToMazeMergerAndTeammate(tile);
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
		
		getOperator().turnHeadCounterClockWise(5);
		distance = readSensorUltraSonic();
		if (distance < 25) {
			getOperator().turnHeadClockWise(5);
			return true;
		}
		
		getOperator().turnHeadClockWise(10);
		distance = readSensorUltraSonic();
		getOperator().turnHeadCounterClockWise(5);
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
	
	public Void execute() throws InterruptedException, CalibrationException, OperationException {
		final Timer timer = new Timer();
		timer.reset();
		moves = new Movement();
		maze = new Maze();
		twist = 0;
		int seesaws = 0;
		
		mazeListener.updatePosition(0, 0, 0);
		scanBorders(current = maze.addTile(0, 0));
		graph.setRootTile(current);
		getOperator().setSpeed(80);
		for (Tile[] path; (path = getPathToNextTile(false)) != null;) {
			System.out.println(path.length);
			if (path.length == 1) {
				scanBorders(current);
				continue;
			}
			for (int i = 1; i < (path.length); i++) {
				checkAborted();
				Tile next = path[i];
				System.out.println(next.toString());
				if(next.isSeesaw()){
					System.out.println("next is seesaw");
					boolean crossedSeesaw = false;
					while(!crossedSeesaw){
						if(!this.getOperator().detectInfrared() 
								&& mazeListener.lockSeesaw(current.getBarCode())){
							int j = i+3;
							//i+3 zet de robot op de tile achter de barcode achter de wip
							while(i!=j){
								i++;
								Tile goTo = path[i];
								mazeListener.updatePosition(goTo.getX(), goTo.getY(), getDirectionBody().getDouble());
							}
							getOperator().doSeesaw();
							this.current = path[i];
							mazeListener.unlockSeesaw();
							crossedSeesaw = true;
						}else{
							if(seesaws > 1){
								List<Tile> seesaw = getSeesaw(current, next);
								graph.addVerticies(maze.getTiles());
								Dijkstra dijkstra = new Dijkstra(graph);
								next = seesaw.get(0);
								List<Tile> wayToFollow = dijkstra.findShortestPath(current,next);
								i=path.length;
								path = new Tile[wayToFollow.size()+1];
								path[0] = current;
								for(int a=0; a<wayToFollow.size(); a++){
									path[a+1] = wayToFollow.get(a);
								}	
								break;
							}
							else{
								Tile barcodeTile = current;
								Tile safeZoneTile = sideStepFromSeesaw(current);
								Thread.sleep(5000);
								returnToSeesaw(safeZoneTile, barcodeTile);
							}
						}
					}
				}else{
					moveTo(path[i]);
				}
			}
			Tile tile = current;
			checkAborted();
			scanBorders(tile);
			
			checkAborted();
			if (tile.canHaveBarcode()) {
				final int barcode = scanBarcode(tile);
				System.out.println("I scanned barcode "+barcode);
				checkAborted();
				if (barcode >= 0) {
					System.out.println("barcode > 0");
					if(barcodeCanBeItemBarcode(barcode)){
						System.out.println("can be item barcode");
						tile = createItem(tile, getDirectionBody(), barcode);
						//send barcode to merger
						addTileToMazeMergerAndTeammate(tile);
						checkAborted();
						
						ArrayList<Integer> numbers = getItemnumber(barcode);
						int itemNumber = numbers.get(0);
						int teamNb = numbers.get(1);
						//If this is our object AND we haven't picked up our object already
						if (itemNumber == this.objectNumber && teamNb== this.teamNumber) {
							this.pickUp();
							mazeListener.notifyObjectFound();
						}
					}else if(barcodeCanBeSeesaw(barcode)){
						checkAborted();
						seesaws++;
						handleSeesawBarcode(barcode);
					}else{
						//TODO:Checkpoint logica ?
					}
					
				}
			}
			
			if(this.found && this.teamMateKnown){
				//Try merge
				if(mazeMerger.hasReceivedNewTileSinceLastCheck()){
					mergeSuccess = mazeMerger.tryToMerge();
					if(mergeSuccess && this.canGoToTeammate()){
						break;
					}
				}
			}
		}
		/*
		 * Reaching this point means we have either merged maps and can go to our teammate
		 * OR
		 * We have no teammate yet and have fully explored the maze
		 */
		if(this.found && this.teamMateKnown && mergeSuccess){
		//---READY TO GO TO TEAMMATE
			System.out.println("Object found, teammate known, merge succes: to the choppa!");
			this.GoToRobot();
			mazeListener.notifyGameOver();
		}
		else{
			//---MAZE FULLY EXPLORED--- (canGoToTeammte() always true)
			System.out.println("Maze fully explored, waiting for data to merge...");
			while(!mergeSuccess){
				if(mazeMerger.hasReceivedNewTileSinceLastCheck()){
					mergeSuccess = mazeMerger.tryToMerge();
				}
			}
			System.out.println("Merged succesfully! To the choppa!");
			this.GoToRobot();
			mazeListener.notifyGameOver();
		}
		
		
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
		getOperator().sendMessage(msg, "Finished");
		**/
		
		return null;
	}
	
	
	/**
	 * When the robot has stepped away from a seesaw in order to let another robot pass,
	 * he should return to the seesaw after a while, using this method.
	 * @param tile
	 */
	private void returnToSeesaw(Tile safeZone,Tile barcodeTile) {
		try {
			graph.addVerticies(maze.getTiles());
			Dijkstra dijkstra = new Dijkstra(graph);
			List<Tile> path = dijkstra.findShortestPath(safeZone, barcodeTile);
			this.followPath(path);
		} catch (CalibrationException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (OperationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When the seesaw is locked or we cannot pass it for any reason.
	 * The robot should move away from the entrance in order to let another robot pass over the seesaw
	 * in the other direction.
	 * This method will search for a 'safe zone' tile to move away to,
	 * if the robot is currently standing on 'current' tile.
	 * @param current	The tile the robot is currently standing on.
	 */
	private Tile sideStepFromSeesaw(Tile current) {
		List<Tile> neighbors = getNeighborsOfTile(current);
		Tile currentTile = null;
		Tile lastTile = current;
		for(Tile t: neighbors){
			if(!t.isSeesaw()){
				currentTile = t;
			}
		}
		neighbors = searchForTileWithMoreThanThreeNeighbors(currentTile, lastTile);
		Tile toTile = neighbors.get(0);
		try {
			graph.addVerticies(maze.getTiles());
			Dijkstra dijkstra = new Dijkstra(graph);
			List<Tile> path = dijkstra.findShortestPath(current, toTile);
			this.followPath(path);
		} catch (CalibrationException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (OperationException e) {
			e.printStackTrace();
		}
		return toTile;
	}
	
	/**
	 * This method will recursively search for a tile that has more than 3 or just 3 neighbors.
	 * It will return those 3 neighbors.
	 * @param tile		The tile from which the search starts.
	 * @param without	This neighbor will not be added to the neighbors of tile.
	 * @return
	 */
	private List<Tile> searchForTileWithMoreThanThreeNeighbors(Tile tile, Tile without){
		List<Tile> neighbors = getNeighborsWithout(tile, without);
		for(Tile n : neighbors){
			List<Tile> neighbors2 = getNeighborsWithout(tile, without);
			if(neighbors2.size()>=2 && neighbors2!=null){
				return neighbors2;
			}
			else{
				return searchForTileWithMoreThanThreeNeighbors(n,tile);
			}
		}
		//shouldnt reach this code
		System.out.println("recursief = null in mazeV2");
		return null;
	}
	public void addTileToMazeMergerAndTeammate(Tile tile){
		mazeMerger.addTileFromSelf(tile);
		mazeListener.sendTile(tile);
	}
	
	/**
	 * Get all neighbors of tile, but exclude 'without'.
	 * This ofcourse means that 'without' has to be a neighbor of tile.
	 */
	private List<Tile> getNeighborsWithout(Tile tile, Tile without){
		List<Tile> neighbors = getNeighborsOfTile(tile);
		neighbors.remove(without);
		return neighbors;
	}
	
	/**
	 * Searches a seesaw closest to the given tile. The given tile is the current
	 * and the without is a seesawTile that will not be reached
	 */
	public List<Tile> getSeesaw(Tile tile, Tile without){
		List<Tile> neighbors = getNeighborsWithout(tile, without);
		
		for(Tile n : neighbors){
			if(n.isSeesaw()){
				List<Tile> returnList = new ArrayList<Tile>();
				returnList.add(n);
				return returnList;
			}
		}
		List<Tile> list = new ArrayList<Tile>();
		for(Tile n : neighbors){
			List<Tile> list2 = getSeesaw(n, tile);
			if(list2 !=null){
				list.addAll(list2);
			}
		}
		for(Tile n : list){
			if(n != null && n.isSeesaw()){
				List<Tile> returnList = new ArrayList<Tile>();
				returnList.add(n);
				return returnList;
			}
		}
		return null;
	}
	
	/**
	 * Get the neighbors of this tile.
	 * 
	 * IllegalStateException is thrown : if(!isExplored)
	 * @return
	 */
	public List<Tile> getNeighborsOfTile(Tile tile){
			List<Tile> neighbors = new ArrayList<Tile>();
			if(tile.getBorderEast()==Border.OPEN){
				neighbors.add(getNeighbor(tile, Orientation.EAST));
			}
			if(tile.getBorderNorth()==Border.OPEN){
				neighbors.add(getNeighbor(tile, Orientation.NORTH));
			}
			
			if(tile.getBorderSouth()==Border.OPEN){
				neighbors.add(getNeighbor(tile, Orientation.SOUTH));
			}
			
			if(tile.getBorderWest()==Border.OPEN){
				neighbors.add(getNeighbor(tile, Orientation.WEST));
			}
			return neighbors;
	}

	public boolean isAborted() {
		return aborted;
	}


	private boolean found = false;
	private boolean teamMateKnown = false;
	
	private void setTeamMateKnown(){
		this.teamMateKnown = true;
	}
	
	private void setFound(){
		this.found=true;
	}

	
	
	public void setTeammatePosition(final long x, final long y, final double angle) {
		
		if( mazeMerger.hasMerged()){
			this.otherRobotTile = new Tile(Math.round(x),Math.round(y));
			otherRobotTile.transform(mazeMerger.getMergeRotationDirection(), mazeMerger.getMergeTranslationVector());
		}
		
		//	TODO
		
		/*
		 * @ Dario
		 * 
		 * X en Y zijn relatieve coordinaten voor de teammate
		 * (Moeten dus op een of andere manier worden vertaald naar eigen coordinaten)
		 * 
		 * Ik heb geen idee hoe de angle precies gedefinieerd is,
		 * maar die is normaal gezien irrelevant voor u.
		 */
	}
	
	
	public void newTile(Tile tile){
		this.maze.addTile(tile.getX(), tile.getY()).copyBorders(tile);
		this.maze.getTile(tile.getX(), tile.getY()).setSeesaw(tile.isSeesaw());
		if(tile.canHaveBarcode()){
			this.maze.getTile(tile.getX(), tile.getY()).setBarCode(tile.getBarCode());
		}
	}
	
	/**
	 * Alle tiles die aangemaakt worden zijn tiles die reeds beredeneerd kunnen worden.
	 * 
	 * @param bar
	 */
	private void handleSeesawBarcode(int bar) {
		current.setBarCode(bar);
		
		Tile firstSeesawTile = maze.addTile(current.getX(),current.getY(), this.getDirectionBody());
		firstSeesawTile.setSeesaw(true);
		firstSeesawTile.setAllBordersOpen(true);
		firstSeesawTile.setBorder(getDirectionBody().rotateCW(), false);
		firstSeesawTile.setBorder(getDirectionBody().rotateCCW(),false);
		
		Tile firstNeighbor1 = maze.addTile(firstSeesawTile.getX(), firstSeesawTile.getY(),this.getDirectionBody().rotateCW());
		firstNeighbor1.setBorder(this.getDirectionBody().rotateCW().getOpposite(), false);
		
		Tile firstNeighbor2 = maze.addTile(firstSeesawTile.getX(), firstSeesawTile.getY(),this.getDirectionBody().rotateCCW());
		firstNeighbor2.setBorder(this.getDirectionBody().rotateCCW().getOpposite(), false);

		Tile secondSeesawTile = maze.addTile(firstSeesawTile.getX(),firstSeesawTile.getY(),this.getDirectionBody());
		secondSeesawTile.setAllBordersOpen(true);
		secondSeesawTile.setBorder(getDirectionBody().rotateCW(), false);
		secondSeesawTile.setBorder(getDirectionBody().rotateCCW(),false);
		secondSeesawTile.setSeesaw(true);
		Tile secondNeighbor1 = maze.addTile(secondSeesawTile.getX(), secondSeesawTile.getY(),this.getDirectionBody().rotateCW());
		secondNeighbor1.setBorder(this.getDirectionBody().rotateCW().getOpposite(), false);
		Tile secondNeighbor2 = maze.addTile(secondSeesawTile.getX(), secondSeesawTile.getY(),this.getDirectionBody().rotateCCW());
		secondNeighbor2.setBorder(this.getDirectionBody().rotateCCW().getOpposite(), false);
		Tile endingSeesawTile = maze.addTile(secondSeesawTile.getX(), secondSeesawTile.getY(),this.getDirectionBody());
		endingSeesawTile.setAllBordersOpen(true);
		endingSeesawTile.setBorder(getDirectionBody().rotateCW(), false);
		
		
		
		endingSeesawTile.setBorder(getDirectionBody().rotateCCW(),false);
		if(bar == 11 || bar == 15 || bar == 17){
			endingSeesawTile.setBarCode(bar+2);
		}else{
			endingSeesawTile.setBarCode(bar-2);
		}
		
		Tile last = maze.addTile(endingSeesawTile.getX(), endingSeesawTile.getY(),this.getDirectionBody());
		last.setBorder(getDirectionBody().getOpposite(), true);
		updateTiles(new Tile[]{current,firstSeesawTile,secondSeesawTile,endingSeesawTile,last,secondNeighbor1,secondNeighbor2,firstNeighbor1,firstNeighbor2});
	}
	
	
	/**
	 * Create a seesaw tile. 
	 * 
	 * @param tile
	 * @param dir
	 * @return
	 */
	private final Tile createSeesawTile(Tile tile,final Orientation dir){
		//TODO CHECK IF CORRECT?
		Tile seesawTile = maze.addTile(tile.getX(), tile.getY(), dir);
		seesawTile.setAllBordersOpen(true);
		switch(dir){
			case EAST:
				seesawTile.setBorderNorth(Border.CLOSED);
				seesawTile.setBorderSouth(Border.CLOSED);
				break;
			case NORTH:
				seesawTile.setBorderEast(Border.CLOSED);
				seesawTile.setBorderWest(Border.CLOSED);
				break;
			case SOUTH:
				seesawTile.setBorderEast(Border.CLOSED);
				seesawTile.setBorderWest(Border.CLOSED);
				break;
			case WEST:
				seesawTile.setBorderNorth(Border.CLOSED);
				seesawTile.setBorderSouth(Border.CLOSED);
				break;
			default:
				break;
		
		}
		
		mazeListener.sendTile(seesawTile);
		//tile.setBarCode(seesawBarcode);
		return seesawTile;
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
		
		
	    mazeListener.sendTile(endTile);
		
		if(barcode==11||barcode == 13 ||barcode ==15){
			endTile.setBarCode(barcode+2);
		}else{
			endTile.setBarCode(barcode-2);
		}
		
		return endTile;
	}
	/**
	 * Get the itemnumber and teamnumber that belong to the given barcode.
	 * If the itemNumber is our objectNumber AND we haven't received a teamnumber yet
	 * our teamnumber is set to the corresponding teamnumber.
	 * 
	 * @param barcode
	 * @return
	 */
	private ArrayList<Integer> getItemnumber(int barcode) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int ballNumber=-1;
		int teamNumber=-1;
		switch(barcode){
			case 0:
				ballNumber = 0;
				teamNumber = 0;
				break;
			case 1:
				ballNumber = 1;
				teamNumber = 0;
				break;
			case 2:
				ballNumber = 2;
				teamNumber = 0;
				break;
			case 3:
				ballNumber = 3;
				teamNumber = 0;
				break;
			case 4:
				ballNumber = 0;
				teamNumber = 1;
				break;
			case 5:
				ballNumber = 1;
				teamNumber = 1;
				break;
			case 6:
				ballNumber = 2;
				teamNumber = 1;
				break;
			case 7:
				ballNumber = 3;
				teamNumber = 1;
				break;
		}
		//If we didnt get a teamnumber yet, assign it
		if(ballNumber==this.objectNumber && this.teamNumber == DEFAULT_TEAMNUMBER){
			this.teamNumber = teamNumber;
		}
		System.out.println("barcode="+barcode+"    ballnumber="+ballNumber+"  ournumber="+this.objectNumber);
		result.add(ballNumber);
		result.add(teamNumber);
		return result;	
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



	private void pickUp() throws InterruptedException, OperationException, CalibrationException {
		this.resetHead();
		System.out.println("I am really gonna do pickup! :)");
		getOperator().doPickUp();
		this.setFound();
		//because the robot is turned aroud, the moves.turns added with 2
		moves.turns += 2;
		
//		this.executeWhiteLine();
//		this.getOperator().modifyOrientation();
//		
//		
//		int speed = this.getOperator().getSpeed();
//		this.getOperator().setSpeed(30);
//		this.getOperator().moveForward();
//		while(!getOperator().isPressed()){
//			
//		}
//		this.getOperator().stop();
//		//robot rijdt naar achter zodat hij vrij 180 graden kan draaien
//		this.getOperator().moveBackward(100F,true);
//		this.turnAround();
//		this.executeWhiteLine();
//		this.getOperator().setSpeed(speed);
//		this.getOperator().moveForward(200F, true);
//		this.getOperator().sendMQMessage("Got the package, to the choppa!");
		
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
	
	/**
	 * Kan NIET over een seesaw
	 * @param path
	 * @throws InterruptedException
	 * @throws CalibrationException
	 * @throws OperationException
	 */
	private void followPath(final List<Tile> path)
			throws  InterruptedException, CalibrationException, OperationException {
		getOperator().setSpeed(100);
		System.out.println(path);
//		ArrayList<Tile> straightLine = new ArrayList<Tile>();
		for (Tile t : path) {
			System.out.println("moving to: "+t);
//			Tile currentTileInList = null;
//			if (straightLine.size() > 0) {
//				currentTileInList = straightLine.get(straightLine.size() - 1);
//			} else {
//				currentTileInList = t;
//			}
//
//			if (this.isOnStraighLine(currentTileInList, t)) {
//				straightLine.add(t);
//			} else {
//				if (straightLine.size() > 0) {
//					int distanceForward = (straightLine.size()) * 400;
//					this.getOperator().moveForward(distanceForward, true);
//					this.current = straightLine.get(straightLine.size() - 1);
//				}
				this.moveTo(t);
//				straightLine.clear();
//				this.current = t;
//			}
		}
//		if (straightLine.size() > 0) {
//			int distanceForward = (straightLine.size()) * 400;
//			this.getOperator().moveForward(distanceForward, true);
//			this.current = straightLine.get(straightLine.size() - 1);
//		}
	}
	
	private final Orientation getDirectionBody() {
		return Orientation.forHeading(getOperator().getOrientation().getHeadingBody());
	}
	
	private final Orientation getDirectionHead() {
		
		final Orientation body = Orientation.forHeading(getOperator().getOrientation().getHeadingBody());
		switch (Orientation.forHeading(getOperator().getOrientation().getHeadingHead())) {
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
					if(t.isSeesaw()){
						Node n1 = new Node(t, d, node);
//						nodes.add(n1);
//						tiles.add(t);
						Tile t2 = getNeighbor(t, d);
						Node n2 = new Node(t2, d, n1);
//						nodes.add(n2);
//						tiles.add(t2);
						Tile t3 = getNeighbor(t2, d);
						Node n3 = new Node(t3, d, n2);
						nodes.add(n3);
						tiles.add(t3);
					}else{
						nodes.add(new Node(t, d, node));
						tiles.add(t);
					}
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
			getOperator().turnHeadCounterClockWise(180);
			twist -= 2;
		} else {
			getOperator().turnHeadClockWise(180);
			twist += 2;
		}
	}
	
	private final void lookLeft() {
		if (twist > -2) {
			getOperator().turnHeadCounterClockWise(90);
			twist--;
		} else {
			getOperator().turnHeadClockWise(270);
			twist = 1;
		}
	}
	
	private final void lookRight() {
		if (twist < 2) {
			getOperator().turnHeadClockWise(90);
			twist++;
		} else {
			getOperator().turnHeadCounterClockWise(270);
			twist = -1;
		}
	}
	
	private final void moveForward()
			throws   InterruptedException, CalibrationException, OperationException {
		moves.moveForward();
	}
	
	private final void moveTo(final Tile tile)
			throws InterruptedException, CalibrationException, OperationException {
		final int dx = (tile.getX() - this.current.getX());
		final int dy = (tile.getY() - this.current.getY());
		//mazeListener.updatePosition(dx, dy, this.getDirectionBody().getDouble());
		mazeListener.updatePosition(tile.getX(),tile.getY(),getDirectionBody().getDouble());
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
		if(tile.isSeesaw()){
			return false;
		}
		if (tile.canHaveBarcode()) {
			int bar = tile.getBarCode();
			return (bar < 0 && bar!=-2);
		}
		if (!tile.isExplored()) {
			return true;
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
	
	private final int scanBarcode(final Tile tile) throws InterruptedException, OperationException, CalibrationException {
		int barcode = tile.getBarCode();
		if (barcode == -2) {
			// The tile has been checked before,
			// and it has no barcode
			return -2;
		}
		if (barcode >= 0) {
			// The tile has been checked before,
			// and it has a valid barcode
			return barcode;
		}
		final int speed = getOperator().getSpeed();

		int bar = getOperator().scanBarcode();
		getOperator().setSpeed(speed);
		
		

		

		if (bar == -1) {
			// Remember to not check this tile again
			tile.setBarCode(-2);
			return -2;
		}

		tile.setBarCode(bar);
		mazeListener.sendTile(tile);
		
		return bar;
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
		return getOperator().readSensorUltrasonic();
	}
	
	private final void travelEast()
			throws  InterruptedException, CalibrationException, OperationException {
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
			throws  InterruptedException, CalibrationException, OperationException {
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
			throws  InterruptedException, CalibrationException, OperationException {
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
			throws  InterruptedException, CalibrationException, OperationException {
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
		getOperator().turnRight(180F, true);
		moves.turns += 2;
	}
	
	private final void turnLeft() {
		getOperator().turnLeft(90F, true);
		moves.turns++;
	}
	
	private final void turnRight() {
		getOperator().turnRight(90F, true);
		moves.turns++;
	}
	
	private final void updateTiles(final Tile... tiles) {
		for (final Tile tile : tiles) {
			mazeListener.sendTile(tile);
			addTileToMazeMergerAndTeammate(tile);
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
			final bluebot.util.Orientation pos = getOperator().getOrientation();
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
				return getOperator().readSensorLightBrightness();
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
	
	private boolean canGoToTeammate(){
		graph.addVerticies(maze.getTiles());
		final Dijkstra pathfinder = new Dijkstra(graph);
		List<Tile> path = pathfinder.findShortestPath(current, otherRobotTile);
		if(path==null){
			return false;
		}
		return true;
	}
	
	private Tile otherRobotTile;
	private boolean aborted; 
	
	private void GoToRobot() throws  InterruptedException, OperationException, CalibrationException{
		
		while(true){
			checkAborted();
			graph.addVerticies(maze.getTiles());
			final Dijkstra pathfinder = new Dijkstra(graph);
			while(otherRobotTile == null){
				checkAborted();
				Thread.sleep(1000);
				//.println("Waiting for tile from other robot");
			}
			

			List<Tile> path = pathfinder.findShortestPath(current, otherRobotTile);
			if(path == null){
				throw new IllegalStateException("Path to otherRobotTile tile was not found, graph incomplete or otherRobotTile == null ?");
			}
			checkAborted();
			
			if(path.get(0).equals(otherRobotTile)){
				System.out.println("Game is done");
				//this.getOperator().sendMessage("Teammate found", "Done");
				break;
			}
			checkAborted();
			
			if(path.get(0).isSeesaw()){
				while(this.getOperator().detectInfrared()){
					//this.getOperator().sendDebug("Waiting...");
					this.wait(1000);
					checkAborted();
				}
				getOperator().doSeesaw();
				this.current = path.get(2);

			}
			else{
				this.moveTo(path.get(0));
			}
			checkAborted();
		}
		
	}
	
	
	
	
	
	private final class Movement {
		
		private int horizontal;
		private int turns;
		private int vertical;
		
		
		
		public void moveForward()
				throws  InterruptedException, OperationException, CalibrationException {
			if (isTranslated()) {
				// The error on the rotation is too big
				// Execute white-line to correct this
				getOperator().moveForward(70F, true);
				resetHead();
				getOperator().doWhiteLine();
//				if (scan) {
//					scanner.startScanning();
//				}
				getOperator().moveForward(200F, true);
				reset(true);
/*
			} else if (isTranslated()) {
				// The error on the translation (X/Y) is too big
				// Find a white line, then use it to move back
				// to the center of a tile
				final int speed = getOperator().getSpeed();
				getOperator().moveForward(40F, true);
				getOperator().setSpeed(25);
				getOperator().moveForward();
				waitForLightSensor(Brightness.WHITE, true);
//				waitForLightSensor(Brightness.WHITE, false);
				getOperator().stop();
				getOperator().setSpeed(50);
				getOperator().moveForward(Robot.OFFSET_SENSOR_LIGHT, true);
				getOperator().setSpeed(speed);
//				if (scan) {
//					scanner.startScanning();
//				}
				getOperator().moveForward(200F, true);
				reset(false);
*/
			} else {
//				if (scan) {
//					getOperator().moveForward(Tile.SIZE, false);
//					waitForLightSensor(Brightness.WHITE, true);
//					waitForLightSensor(Brightness.WHITE, false);
//					scanner.startScanning();
//					waitForMoving(false);
//				} else {
					getOperator().moveForward(Tile.SIZE, true);
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
			getOperator().modifyOrientation();
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
	
	/**
	 * Nested class, wordt ENKEL gebruikt door de GUI om de positie op te vragen. NIET gebruiken voor logica omtrent positiebepaling etc. Enkel een bundel aan info (x,y,body,head)
	 */
	public class MazePosition{
		
		float x;
		private final float y;
		private final float body,head;
		
		public MazePosition(float f,float g,float h, float i){
			this.x = f;
			this.y = g;
			this.body = h;
			this.head = i;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public float getBody() {
			return body;
		}

		public float getHead() {
			return head;
		}
		
		
	}

	public void abort() {
		this.aborted = true;
	}
	
}