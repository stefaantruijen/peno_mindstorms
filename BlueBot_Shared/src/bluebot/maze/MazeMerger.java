package bluebot.maze;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Vector;

import bluebot.graph.Direction;
import bluebot.graph.Tile;

public class MazeMerger {
	
	private ArrayList<Tile> tilesFromTeammate;
	private ArrayList<Tile> tilesFromSelf;
	private ArrayList<Tile> matchesFromTeammate;
	private ArrayList<Tile> matchesFromSelf;
	private int nbOfMatchesFound;
	private double mergeRotationAngle;
	private Vector<Integer> mergeTranslationVector;
	
	public MazeMerger(){
		this.tilesFromTeammate = new ArrayList<Tile>();
		this.tilesFromSelf = new ArrayList<Tile>();
		this.matchesFromTeammate = new ArrayList<Tile>();
		this.matchesFromSelf = new ArrayList<Tile>();
		this.nbOfMatchesFound = 0;
		mergeTranslationVector = new Vector<Integer>();
	}
	
	public boolean addTileFromTeammate(Tile tile){
		if(!isValidTile(tile)){
			return false;
		}
		else{
			tilesFromTeammate.add(tile);
			return true;
		}
	}
	
	public ArrayList<Tile> getTilesFromTeammate() {
		return tilesFromTeammate;
	}

	public ArrayList<Tile> getTilesFromSelf() {
		return tilesFromSelf;
	}

	public ArrayList<Tile> getMatchesFromTeammate() {
		return matchesFromTeammate;
	}
	
	public ArrayList<Tile> getMatchesFromSelf() {
		return matchesFromSelf;
	}

	public int getNbOfMatchesFound() {
		return nbOfMatchesFound;
	}

	public double getMergeRotationAngle() {
		return mergeRotationAngle;
	}

	public Vector<Integer> getMergeTranslationVector() {
		return mergeTranslationVector;
	}

	public boolean addTileFromSelf(Tile tile){
		if(!isValidTile(tile)){
			return false;
		}
		else{
			tilesFromSelf.add(tile);
			return true;
		}
	}
	
	/**
	 * This method will search the known tiles in tilesFromTeammate && tilesFromSelf
	 * for barcodes that are the same and will call addFoundMatch() when it finds a match.
	 * This method should be called each time a new barcode is added to the lists to check
	 * for possible matches.
	 * note: the method will skip tiles that have already been matched.
	 */
	public void searchForMatches(){
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
	 * This method may only be called on 2 tiles that have identical barcodes!
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
	
	public boolean calculateRotation(){
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
			System.out.println(mergeRotationAngle);
			calculateTranslation(ts1, tt1, mergeRotationAngle);
		}
		return result;
	}
	
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

	private void calculateTranslation(Tile ts1, Tile tt1, Direction rotation) {
			tt1.rotate(rotation);
			mergeTranslationVector.add(tt1.getX()-ts1.getX());
			mergeTranslationVector.add(tt1.getY()-ts1.getY());
			System.out.println(mergeTranslationVector.toString());
	}

}
