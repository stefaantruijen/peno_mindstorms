package bluebot.maze;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Vector;

import bluebot.graph.Direction;
import bluebot.graph.Tile;

public class MazeMerger {
	
	/**
	 * HOW TO USE THE MAZE MERGER
	 * --------------------------
	 * Provide the maze merger with data (tiles) using the methods:
	 * - addTileFromTeammate(tile)
	 * - addTileFromSelf(tile)
	 * The mazemerger will decide for itself whether tiles are useful data or not.
	 * When the mazemerger has found 2 matching couples of tiles (aka: 2 matching barcodes)(nbOfMatchesFound==2)
	 * then it can merge the 2 maps using: tryToMerge();
	 * This method will return TRUE if the merge has been successful, FALSE otherwise.
	 * 
	 * GETTING RESULTS FROM THE MAZE MERGER
	 * ------------------------------------
	 * The mazemerger provides a mergeRotationAngle and a mergeTranslationVector that reflect the difference between the 2 maps.
	 * These can be used to transform a tile (from our teammates different point of view) to our view using:
	 * tile.transform(mazeMerger.getMergeRotationAngle(), mazeMerger.getMergeTranslationVector()).
	 * 
	 * The MazeMerger also transforms all known tiles from our teammate, this list can be obtained using getTilesFromTeammate().
	 */
	
	private ArrayList<Tile> tilesFromTeammate; //ALL the tiles from our teammate, even those that do not have a barcode
	private ArrayList<Tile> tilesFromSelf; //ALL tiles from ourselves, even those that do not have a barcode
	private ArrayList<Tile> dataFromTeammate; //All tiles from our teammate that DO contain a barcode
	private ArrayList<Tile> dataFromSelf; //All tiles from ourselves that DO contain a barcode
	private ArrayList<Tile> matchesFromTeammate; //All tiles from our teammate that have a barcode AND of which we also have a tile with that same barcode
	private ArrayList<Tile> matchesFromSelf; //All tiles from ourselves that contain a barcode AND for which our teammate also has a tile with that same barcode
	private int nbOfMatchesFound; //The number of tiles-with-barcode couples that we have found that can be used to calculate merge vectors
	private double mergeRotationAngle; //The rotational difference between our teammates map and our map
	private Vector<Integer> mergeTranslationVector; //The translational difference, after rotating, between our teammates map and our map
	private boolean receivedNewTileSinceLastCheck; //True if new tiles have been added after the last call of tryToMerge()
	private boolean hasMerged; //True if this mazemerger has successfully calculated the rotation and translation vectors
	
	public MazeMerger(){
		this.tilesFromTeammate = new ArrayList<Tile>();
		this.tilesFromSelf = new ArrayList<Tile>();
		this.dataFromTeammate = new ArrayList<Tile>();
		this.dataFromSelf = new ArrayList<Tile>();
		this.matchesFromTeammate = new ArrayList<Tile>();
		this.matchesFromSelf = new ArrayList<Tile>();
		this.nbOfMatchesFound = 0;
		this.mergeRotationAngle = -1;
		mergeTranslationVector = new Vector<Integer>();
		this.receivedNewTileSinceLastCheck = false;
		this.hasMerged = false;
	}
	
	/**
	 * Get all the tiles the teammate has sent us.
	 * If hasMerged()==true, the tiles returned will be the transformed versions!
	 */
	public ArrayList<Tile> getTilesFromTeammate() {
		//Uncomment this if an empty array should be returned if no merge has been successful yet
//		if(this.hasMerged){
//			return tilesFromTeammate;
//		}
//		else{
//			return new ArrayList<Tile>();
//		}
		return tilesFromTeammate;
	}
	
	public ArrayList<Tile> getTilesFromTeammateTranslated(){
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		if(!hasMerged){
			return tiles;
		}
		for(Tile t : this.tilesFromTeammate){
			Tile tile = t;
			tile.transform(this.getMergeRotationDirection(), this.getMergeTranslationVector());
			tiles.add(tile);
		}
		return tiles;
	}

	/**
	 * Get all tiles we found ourselves.
	 */
	public ArrayList<Tile> getTilesFromSelf() {
		return tilesFromSelf;
	}
	
	/**
	 * Returns true if this MazeMerger has successfully merged 2 maps.
	 */
	public boolean hasMerged(){
		return this.hasMerged;
	}

	/**
	 * Gets the found matches from our teammate.
	 * A match is a common tile that has been found in the list of tiles of the teammate and ourself.
	 * In other words: if we, and our teammate, have found a tile with the same barcode, this is a match.
	 * => The tile from our teammate is added to "matchesFromTeammate"
	 * => Our tile is added to "matchesFromSelf"
	 */
	public ArrayList<Tile> getMatchesFromTeammate() {
		return matchesFromTeammate;
	}
	
	/**
	 * Gets the found matches from our teammate.
	 * A match is a common tile that has been found in the list of tiles of the teammate and ourself.
	 * In other words: if we, and our teammate, have found a tile with the same barcode, this is a match.
	 * => The tile from our teammate is added to "matchesFromTeammate"
	 * => Our tile is added to "matchesFromSelf"
	 */
	public ArrayList<Tile> getMatchesFromSelf() {
		return matchesFromSelf;
	}

	/**
	 * Number of matches found is equal to the size of matchesFromTeammate and matchesFromSelf.
	 * If we have 2 matching tiles, then we have enough data to start merging.
	 */
	public int getNbOfMatchesFound() {
		return nbOfMatchesFound;
	}

	/**
	 * This is the rotation needed to make our map equal to the map of our teammate (combined with translation ofcourse).
	 * This is one of the 2 output values of the merging algorithm.
	 * The other one is the Translation Vector.
	 */
	public double getMergeRotationAngle() {
		return mergeRotationAngle;
	}
	
	/**
	 * This is the rotation needed to make our map equal to the map of our teammate (combined with translation ofcourse).
	 * This is one of the 2 output values of the merging algorithm.
	 * The other one is the Translation Vector.
	 */
	public Direction getMergeRotationDirection() {
		double angle = getMergeRotationAngle();
		Direction result = Direction.UP;
		if(angle>0){
			if(angle==90){
				result = Direction.RIGHT;
			}
			else if(angle==180){
				result = Direction.DOWN;
			}
			else if(angle==270){
				result = Direction.LEFT;
			}
		}
		return result;
	}

	/**
	 * This is the translation needed to make our map equal to the map of our teammate (combined with rotation ofcourse).
	 * This is one of the 2 output values of the merging algorithm.
	 * The other one is the Rotation Angle.
	 */
	public Vector<Integer> getMergeTranslationVector() {
		return mergeTranslationVector;
	}

	/**
	 * Add a tile that our robot has scanned.
	 * If the tile contains a barcode, it will be used as data to calculate the mergevectors.
	 */
	public boolean addTileFromSelf(Tile tile){
		if(!tilesFromSelf.contains(tile)){
			this.tilesFromSelf.add(tile);
		}
		if(!isValidTile(tile)){
			return false;
		}
		else{
			receivedNewTileSinceLastCheck = true;
			dataFromSelf.add(tile);
			return true;
		}
	}
	
	/**
	 * Add a tile that our teammate has scanned.
	 * If the tile contains a barcode, it will be used as data to calculate the mergevectors.
	 * Returns true if the tile was useful data.
	 */
	public boolean addTileFromTeammate(Tile tile){
		if(hasMerged){
			tile.transform(this.getMergeRotationDirection(), this.getMergeTranslationVector());
			if(!tilesFromTeammate.contains(tile)){
				this.tilesFromTeammate.add(tile);
			}
			return false;
		}
		else{
			if(!tilesFromTeammate.contains(tile)){
				this.tilesFromTeammate.add(tile);
			}
			if(!isValidTile(tile)){
				return false;
			}
			else{
				receivedNewTileSinceLastCheck = true;
				dataFromTeammate.add(tile);
				return true;
			}
		}
	}
	
	/**
	 * This method tells us whether any new tiles have been received after the last time a merge was attempted.
	 * ---
	 * More detail:
	 * Users should call the method 'tryToMerge()' in order to try to calculate rotation and translation vectors.
	 * There is however a chance that this fails (not enough data probably).
	 * If this is the case, it is unwise to keep trying to merge before any new data has arrived, as the algorithm will keep failing.
	 * Thus a user should call this method first to check if new tiles have arrived since the last merge attempt.
	 * @return
	 */
	public boolean hasReceivedNewTileSinceLastCheck(){
		return receivedNewTileSinceLastCheck;
	}
	
	/**
	 * Attempt to merge (= calculate rotation and translation vector).
	 * Remeber to first call hasReceivedNewTileSinceLastCheck() before recalling this method when a merge fails
	 * to not perform useless operations.
	 * @return result	TRUE when the merge has been successful and a rotation and translation vector have been calculated.
	 * 					FALSE when the merge failed (probably insufficient data)
	 */
	public boolean tryToMerge() {
		boolean result = false;
		if(this.hasMerged){
			//Do nothing, just return success
			result = true;
		}
		else{
			searchForMatches();
			if(this.getNbOfMatchesFound()==2){
				result = calculateRotationAndTranslation();
			}
			receivedNewTileSinceLastCheck = false;
			if(result){
				//Merge has been successful
				this.hasMerged = true;
				//Transform all tiles from our teammate
				for(Tile t: tilesFromTeammate){
					t.transform(this.getMergeRotationDirection(), this.getMergeTranslationVector());
				}
			}
		}
		return result;
	}
	
	/**
	 * This method will search the known tiles in dataFromTeammate && dataFromSelf
	 * for barcodes that are the same and will call addFoundMatch() when it finds a match.
	 * note: the method will skip tiles that have already been matched.
	 */
	private void searchForMatches(){
		int size1 = dataFromTeammate.size();
		int size2 = dataFromSelf.size();
		if((size1+size2)>=4){
			for(Tile t1:dataFromTeammate){
				if(!matchesFromTeammate.contains(t1)){
					for(Tile t2:dataFromSelf){
						if(!matchesFromSelf.contains(t2)){
							if(t1.getBarCode()==t2.getBarCode()){
								//Match found
								try {
									addFoundMatch(t1,t2);
								} catch (UnexpectedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Add the found match.
	 * This means that we have found 2 tiles that have identical barcode and thus can be used as data to merge.
	 * The tile from our teammate is added to matchesFromTeammate.
	 * Our tile is added to matchesFromSelf.
	 * (This method should only be called on 2 tiles that have identical barcodes)
	 * @param t1 The tile with barcode from your teammate
	 * @param t2 Own tile with barcode
	 * @throws UnexpectedException 
	 */
	private void addFoundMatch(Tile t1, Tile t2) throws UnexpectedException {
		if(nbOfMatchesFound>=2){
			//Nothing needs to be done, we already have enough tiles.
		}
		else{
			//Doublecheck
			int size1 = matchesFromTeammate.size();
			int size2 = matchesFromSelf.size();
			if(size1==nbOfMatchesFound && size2==nbOfMatchesFound){
				matchesFromTeammate.add(t1);
				matchesFromSelf.add(t2);
				nbOfMatchesFound++;
			}
			else{
				//Something went wrong
				System.out.println("MazeMerger.addFoundMatch inconsistent number of tiles in matched arrays.");
				throw new UnexpectedException("Error in MazeMerger.addFoundMatch");
			}
		}
	}
	
	private boolean isValidTile(Tile tile){
		return tile.getBarCode() != -1;
	}

	/**
	 * Attempt to calculate rotation and translation vectors using the data in matchesFromTeammate and matchesFromSelf.
	 * (In this method only rotation is actually calculated, see calculateTranslation for translation calculation)
	 * @return
	 */
	private boolean calculateRotationAndTranslation(){
		boolean result = false;
		if(nbOfMatchesFound==2){
			Tile tt1 = matchesFromTeammate.get(0);
			Tile tt2 = matchesFromTeammate.get(1);
			Tile ts1 = matchesFromSelf.get(0);
			Tile ts2 = matchesFromSelf.get(1);
			Vector<Integer> vSelf = new Vector<Integer>(2);
			vSelf.add(ts2.getX()-ts1.getX());
			vSelf.add(ts2.getY()-ts1.getY());
			Vector<Integer> vTeammate = new Vector<Integer>(2);
			vTeammate.add(tt2.getX()-tt1.getX());
			vTeammate.add(tt2.getY()-tt1.getY());
			mergeRotationAngle = Math.toDegrees(Math.atan2(vTeammate.get(1),vTeammate.get(0)) - Math.atan2(vSelf.get(1),vSelf.get(0)));
			if(mergeRotationAngle<0)
			{
				mergeRotationAngle+=360;
			}
			/*
			 * Now we will rotate the first tile from our teammate and compare it to our corresponding tile,
			 * to get the translation vector.
			 */
			calculateTranslation(ts1, tt1, mergeRotationAngle);
			result = true;
		}
		return result;
	}
	
	/**
	 * Ride-by method to convert the DOUBLE rotation into a DIRECTION rotation.
	 * See calculateTranslation(..., Diraction rotation) for actual calculation.
	 * @param ts1		One of our matched tiles
	 * @param tt1		The correspoding tile from our teammate (unrotated)
	 * @param rotation	The rotation angle between our tile and our teammates tile.
	 */
	private void calculateTranslation(Tile ts1, Tile tt1, double rotation){
		Direction direction = Direction.UP; //is ignored
		if(rotation==90){
			direction = Direction.RIGHT;
		}
		else if(rotation==180){
			direction = Direction.DOWN;
		}
		else if(rotation==270){
			direction = Direction.LEFT;
		}
		calculateTranslation(ts1, tt1, direction);
	}

	/**
	 * Calculates the translation vector.
	 * To do this, first the tile from our teammate is rotated using the previously calculated rotation angle.
	 * @param ts1		One of our matched tiles
	 * @param tt1		The correspoding tile from our teammate (unrotated)
	 * @param rotation	The rotation angle between our tile and our teammates tile.
	 */
	private void calculateTranslation(Tile ts1, Tile tt1, Direction rotation) {
			//Copy so dont alter tt1
			Tile copy = new Tile(tt1.getX(),tt1.getY());
			copy.rotate(rotation);
			//Order of subtraction is important! we want the vector that changes teammates tiles to our system
			//not the other way around!
			mergeTranslationVector.add(ts1.getX()-copy.getX());
			mergeTranslationVector.add(ts1.getY()-copy.getY());
	}

}
