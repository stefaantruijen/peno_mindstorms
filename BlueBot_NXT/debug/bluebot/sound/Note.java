package bluebot.sound;



/**
 * Represents a musical note
 * 
 * @author Ruben Feyen
 */
public enum Note {
	C4	(262),
	Cc4	(277),
	Db4	(277),
	D4	(294),
	Dc4	(311),
	Eb4	(311),
	E4	(329),
	F4	(349),
	Fc4	(370),
	Gb4	(370),
	G4	(392),
	Gc4	(415),
	Ab4	(415),
	A4	(440),
	Ac4	(466),
	Bb4	(466),
	B4	(494),
	C5	(523);
	
	
	
	private int frequency;
	
	
	private Note(final int frequency) {
		this.frequency = frequency;
	}
	
	
	
	public int getFrequency() {
		return frequency;
	}
	
}