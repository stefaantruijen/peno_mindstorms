package bluebot;

import bluebot.graph.Graph;
import bluebot.graph.Tile;
import bluebot.maze.BarcodeValidator;

/**
 * 
 * @author Dieter, Michiel,Dario
 *
 */
public class BarcodeExecuter {
	private final Driver driver;
	private final Graph graph;
	private Tile currentTile;

	public BarcodeExecuter(Driver driver,Graph g) {
		this.driver = driver;
		this.graph = g;
		
	}
	public void executeBarcode(String binaryCode,Tile currentTile) {
		int code = convertBinaryToInt(binaryCode);
		this.currentTile = currentTile;
		executeBarcode(code);
	}

	/*
	 * I know this is f-ugly code but what do...
	 * I find we need to make the distinction between:
	 * barcodes with no action and illegal barcodes.
	 * Think about where to check for a valid barcode in combination with ReadBarcodeAction
	 * 
	 * Proposals:
	 * -Enum?
	 * -In default case a method checking it with if's (e.g. if(1>=code && 4<=code))
	 * -...?
	 * 
	 * */
	public void executeBarcode(int code) {
		int validatedCode = BarcodeValidator.validate(code);
		if(validatedCode == -1){
			driver.sendError(convertIntToBinary(code) + " is an illegal barcode. No action will be undertaken.");
			return;
		}
		//validatedCode is a valid code so try executing.
		switch (code) {
		case 5: 
			// "000101": Draai een rondje naar links
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Turning 360° left.", "BARCODE");
			driver.turnLeft(360, false);
			break;
		case 9: 
			// "001001": Draai een rondje naar rechts
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Turning 360° right.", "BARCODE");
			driver.turnRight(360, false);
			break;
		case 15: 
			// TODO: "001111": speel een muziekje
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Playing music.", "BARCODE");
			break;
		case 19: 
			// "010011": wacht 5 seconden
			//TODO: dit is geen correcte implementatie (wegens multi threading). Hogerop nodig
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Waiting 5 seconds.", "BARCODE");
			double startTime = System.currentTimeMillis();
			while ((System.currentTimeMillis() - startTime) < 5000) {
				// wait
			}
			break;
		case 25: 
			// "011001": vanaf nu aan trage snelheid rijden
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Setting a slow speed (20%).", "BARCODE");
			driver.setSpeed(20);
			break;
		case 37: 
			// "100101": vanaf nu aan hoge snelheid rijden
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Setting a fast speed (100%).", "BARCODE");
			driver.setSpeed(100);
		case 55:
			this.graph.setFinishVertex(this.currentTile);
			break;
		default:
			//Valid barcode but not yet implemented.
			sendNotImplemented(code);
		}
	}
	
	/**
	 * Sends a message that the action for a valid barcode is not implemented.
	 * 
	 * @param code
	 */
	private void sendNotImplemented(int code) {
		driver.sendMessage("No action implemented for barcode " + convertIntToBinary(code) +".", "BARCODE");
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
	
}


