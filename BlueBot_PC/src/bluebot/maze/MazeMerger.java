package bluebot.maze;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Vector;

import bluebot.graph.Direction;
import bluebot.graph.Tile;

public class MazeMerger {
	/**
	 * General comment: when speaking of a tile, obviously we speak of a tile that contains a barcode.
	 */
	
	private ArrayList<Tile> tilesFromTeammate;
	private ArrayList<Tile> tilesFromSelf;
	private ArrayList<Tile> matchesFromTeammate;
	private ArrayList<Tile> matchesFromSelf;
	private int nbOfMatchesFound;
	private double mergeRotationAngle;
	private Vector<Integer> mergeTranslationVector;
	private boolean receivedNewTileSinceLastCheck;
	
	public MazeMerger(){
		this.tilesFromTeammate = new ArrayList<Tile>();
		this.tilesFromSelf = new ArrayList<Tile>();
		this.matchesFromTeammate = new ArrayList<Tile>();
		this.matchesFromSelf = new ArrayList<Tile>();
		this.nbOfMatchesFound = 0;
		mergeTranslationVector = new Vector<Integer>();
		this.receivedNewTileSinceLastCheck = false;
	}
	
	/**
	 * Get all the tiles the teammate has sent us.
	 */
	public ArrayList<Tile> getTilesFromTeammate() {
		return tilesFromTeammate;
	}

	/**
	 * Get all tiles we found ourselves.
	 */
	public ArrayList<Tile> getTilesFromSelf() {
		return tilesFromSelf;
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
	 * This is the translation needed to make our map equal to the map of our teammate (combined with rotation ofcourse).
	 * This is one of the 2 output values of the merging algorithm.
	 * The other one is the Rotation Angle.
	 */
	public Vector<Integer> getMergeTranslationVector() {
		return mergeTranslationVector;
	}

	/**
	 * Add a tile that our robot has scanned.
	 * This has to be a tile that contains a barcode.
	 * So this method should be called every time our robot reads a new barcode.
	 */
	public boolean addTileFromSelf(Tile tile){
		if(!isValidTile(tile)){
			return false;
		}
		else{
			receivedNewTileSinceLastCheck = true;
			tilesFromSelf.add(tile);
			return true;
		}
	}
	
	/**
	 * Add a tile that our teammate has scanned.
	 * This has to be a tile that contains a barcode.
	 * So this method should be called every time our teammate tells us he/she scanned a tile with a barcode.
	 */
	public boolean addTileFromTeammate(Tile tile){
		if(!isValidTile(tile)){
			return false;
		}
		else{
			receivedNewTileSinceLastCheck = true;
			tilesFromTeammate.add(tile);
			return true;
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
		searchForMatches();
		if(this.getNbOfMatchesFound()==2){
			result = calculateRotationAndTranslation();
		}
		receivedNewTileSinceLastCheck = false;
		return result;
	}
	
	/**
	 * This method will search the known tiles in tilesFromTeammate && tilesFromSelf
	 * for barcodes that are the same and will call addFoundMatch() when it finds a match.
	 * note: the method will skip tiles that have already been matched.
	 */
	private void searchForMatches(){
		int size1 = tilesFromTeammate.size();
		int size2 = tilesFromSelf.size();
		if((size1+size2)>4){
			for(Tile t1:tilesFromTeammate){
				if(!matchesFromTeammate.contains(t1)){
					for(Tile t2:tilesFromSelf){
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
	 * (This method may only be called on 2 tiles that have identical barcodes!)
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
			tt1.rotate(rotation);
			mergeTranslationVector.add(tt1.getX()-ts1.getX());
			mergeTranslationVector.add(tt1.getY()-ts1.getY());
			System.out.println(mergeTranslationVector.toString());
	}

}
