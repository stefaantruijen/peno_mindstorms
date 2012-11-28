package bluebot;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.maze.BarcodeValidator;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Dieter, Michiel,Dario
 *
 */
public class BarcodeExecuter {
	private final Driver driver;
	private final Graph graph;
	private Tile currentTile;
	private String pathToMusic = "data\\Bells.wav";
	private int lowSpeed = 20;
	private int highSpeed = 80;

	public BarcodeExecuter(Driver driver,Graph g) {
		this.driver = driver;
		this.graph = g;
		
	}
	
	/**
	 * 
	 * @param binaryCode
	 * @param currentTile
	 */
	public void executeBarcode(String binaryCode,Tile currentTile) {
		int code = convertBinaryToInt(binaryCode);
		executeBarcode(code, currentTile);
	}

	/**
	 * Executes the appropriate action for the given code. 
	 * If a non-valid code is given the method send and error message.
	 * 	
	 * In every case there should be a:
	 * 	 sendDebugAndMessage(msg, bool);
	 * The bool should depend on the action. If the action has clear feedback in de GUI (sound, turning of car, ...) bool should be false.
	 * Otherwise bool should be true;
	 * 
	 * @param code
	 * @param currentTile
	 */
	public void executeBarcode(int code, Tile currentTile) {
		this.currentTile = currentTile;
		String msg;
		int validatedCode = BarcodeValidator.validate(code);
		if(validatedCode == -1){
			driver.sendError(convertIntToBinary(code) + " is an illegal barcode. No action will be undertaken.");
			return;
		}
		//validatedCode is a valid code so try executing.
		switch (validatedCode) {
		case 5: 
			// "000101": Draai een rondje naar links
			msg = "Executing " + convertIntToBinary(validatedCode)+": Turning 360 degrees left.";
			sendDebugAndMessage(msg, false);
			driver.turnLeft(360, true);
			break;
		case 9: 
			// "001001": Draai een rondje naar rechts
			msg = "Executing " + convertIntToBinary(validatedCode)+": Turning 360 degrees right.";
			sendDebugAndMessage(msg, false);
			driver.turnRight(360, true);
			break;
		case 13:
			msg = "Executing" + convertIntToBinary(validatedCode)+": Checkpoint found ("+currentTile.getX()+","+currentTile.getY()+")";
			sendDebugAndMessage(msg, false);
			this.graph.setCheckpointVertex(this.currentTile);
			break;
		case 15: 
			// "001111": speel een muziekje
			msg = "Executing " + convertIntToBinary(validatedCode)+": Playing music.";
			sendDebugAndMessage(msg, false);
			driver.playSound();
			break;
		case 19: 
			// "010011": wacht 5 seconden
			//TODO: dit is mogelijk geen correcte implementatie (wegens multi threading). Hogerop nodig? Vraag na en/of test
			msg = "Executing " + convertIntToBinary(validatedCode)+": Waiting 5 seconds.";
			sendDebugAndMessage(msg, false);
			double startTime = System.currentTimeMillis();
			while ((System.currentTimeMillis() - startTime) < 5000) {
				// wait
			}
			break;
		case 25: 
			// "011001": vanaf nu aan trage snelheid rijden
			msg = "Executing " + convertIntToBinary(validatedCode)+": Setting a slow speed (20%).";
			sendDebugAndMessage(msg, false);
			driver.setSpeed(lowSpeed);
			break;
		case 37: 
			// "100101": vanaf nu aan hoge snelheid rijden
			msg = "Executing " + convertIntToBinary(validatedCode)+": Setting a fast speed (100%).";
			sendDebugAndMessage(msg, false);
			driver.setSpeed(highSpeed);
			break;
		case 55:
			msg = "Executing" + convertIntToBinary(validatedCode)+": Finish found ("+currentTile.getX()+","+currentTile.getY()+")";
			sendDebugAndMessage(msg, false);
			this.graph.setFinishVertex(this.currentTile);
			break;
		default:
			//Valid barcode but not yet implemented.
			sendNotImplemented(validatedCode);
		}
	}
	
	/**
	 * Sends a message that the action for a valid barcode is not implemented.
	 * 
	 * @param code
	 */
	private void sendNotImplemented(int code) {
		String msg = "No action implemented for barcode " + convertIntToBinary(code) +".";
		driver.sendMessage(msg, "BARCODE");
		driver.sendDebug(msg);
	}
	
	/**
	 * Example: convertIntToBinary(19) returns "10011". Notice that all our
	 * barcodes consist of 6 bits and thus we need to add leading zeros after
	 * this conversion, where needed! This happens in the {@link addLeadingZeros()}
	 * method.
	 */
	private String convertIntToBinary(int number) {
		return addLeadingZeros(Integer.toBinaryString(number));
	}

	/**
	 * Adds leading zeros to a barcode that is less that 6 bits long. ("101" =>
	 * "000101")
	 */
	private String addLeadingZeros(String binaryCode) {
		int length = binaryCode.length();
		if (length < 6) {
			int nbOfLeadingZeros = 6 - length;
			while (nbOfLeadingZeros > 0) {
				binaryCode = "0" + binaryCode;
				nbOfLeadingZeros--;
			}
		}
		return binaryCode;
	}
	
	/**
	 * Obvious
	 */
	public int convertBinaryToInt(String binaryString){
		return Integer.parseInt(binaryString, 2);
	}
	public Graph getGraph() {
		return graph;
	}
	
	private final void loadSound(Driver driver) {
		final JFileChooser fc = new JFileChooser(new File("."));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		for (final FileFilter filter : fc.getChoosableFileFilters()) {
			fc.removeChoosableFileFilter(filter);
		}
		fc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(final File file) {
				return (file.isDirectory() || file.getName().endsWith(".wav"));
			}
			
			public String getDescription() {
				return "Sound files (.wav)";
			}
		});
		
		fc.setApproveButtonText("Load");
		fc.setDialogTitle("Load a sound file");
		
		final File file = fc.getSelectedFile();
		driver.playSound(file);
		
	}
	
	private void sendDebugAndMessage(String msg, boolean sendMessage){
		if(sendMessage){
			driver.sendMessage(msg,"BARCODE");
		}
		driver.sendDebug(msg);
	}
	
}


