package bluebot.sound;


import lejos.nxt.Sound;



/**
 * 
 * @author Ruben Feyen
 */
public class Instrument {
	
	private int[] definition;
	
	
	private Instrument(final int[] definition) {
		this.definition = definition;
	}
	
	
	
	public static Instrument createFlute() {
		return new Instrument(Sound.FLUTE);
	}
	
	public static Instrument createPiano() {
		return new Instrument(Sound.PIANO);
	}
	
	public static Instrument createXylophone() {
		return new Instrument(Sound.XYLOPHONE);
	}
	
	private final void playNote(final int length, final int frequency) {
		Sound.playNote(definition, frequency, length);
	}
	
	public void playNote(final int length, final Note note) {
		playNote(length, note.getFrequency());
	}
	
}