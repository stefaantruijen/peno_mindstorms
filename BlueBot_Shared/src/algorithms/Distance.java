package algorithms;


import bluebot.graph.Tile;

class Distance {
	
	private Tile tile;
	private Integer integ;
	
	public Distance(Tile t,int i){
		this.setTile(t);
		this.setInteg(i);
	}

	public Integer getInteg() {
		return integ;
	}

	public void setInteg(Integer integ) {
		this.integ = integ;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
	@Override
	public boolean equals(Object arg0){
		if(!(arg0 instanceof Distance)){
			return false;
		}
		
		Distance d = (Distance) arg0;
		
		return(d.getTile().equals(this.getTile()) && this.getInteg()==d.getInteg());
	}

}