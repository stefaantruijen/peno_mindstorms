package algorithms;
import bluebot.graph.Tile;

public class Predecessor {
	
	private Tile key,value;
	
	public Predecessor(Tile k,Tile v){
		this.key =k;
		this.value=v;
	}

	public Tile getKey() {
		return key;
	}

	public void setKey(Tile key) {
		this.key = key;
	}

	public Tile getValue() {
		return value;
	}

	public void setValue(Tile value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object arg0){
		if(!(arg0 instanceof Predecessor)){
			return false;
		}
		
		Predecessor p = (Predecessor) arg0;
		return(p.getKey().equals(this.getKey())&&p.getValue().equals(this.getValue()));
	}

}