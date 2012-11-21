package bluebot;

/**
 * 
 * @author Dieter, Michiel
 *
 */
public class BarcodeExecuter {
	private final Driver driver;

	public BarcodeExecuter(Driver driver) {
		this.driver = driver;
	}
	public void executeBarcode(String binaryCode) {
		int code = convertBinaryToInt(binaryCode);
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
		switch (code) {
		case 1: 
			sendNotImplemented(code);
			break;
		case 2: 
			sendNotImplemented(code);
			break;
		case 3: 
			sendNotImplemented(code);
			break;
		case 4: 
			sendNotImplemented(code);
			break;
		case 5: 
			// "000101": Draai een rondje naar links
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Turn 360° left.", "BARCODE");
			driver.turnLeft(360, false);
			break;
		case 6: 
			sendNotImplemented(code);
			break;
		case 7: 
			sendNotImplemented(code);
			break;
		case 9: 
			// "001001": Draai een rondje naar rechts
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Turn 360° right.", "BARCODE");
			driver.turnRight(360, false);
			break;
		case 10: 
			sendNotImplemented(code);
			break;
		case 11: 
			sendNotImplemented(code);
			break;
		case 13: 
			sendNotImplemented(code);
			break;
		case 14: 
			sendNotImplemented(code);
			break;
		case 15: 
			// TODO: "001111": speel een muziekje
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Playing music.", "BARCODE");
			break;
		case 17: 
			sendNotImplemented(code);
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
		case 21: 
			sendNotImplemented(code);
			break;
		case 22: 
			sendNotImplemented(code);
			break;
		case 23: 
			sendNotImplemented(code);
			break;
		case 25: 
			// "011001": vanaf nu aan trage snelheid rijden
			//TODO: if called in another algorithm this will probably not work since they often use their own speeds.
			//			Possible solution: work with a flag in driver ('changed externally')?
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Setting a slow speed (20%).", "BARCODE");
			driver.setSpeed(20);
		case 27: 
			sendNotImplemented(code);
			break;
		case 29: 
			sendNotImplemented(code);
			break;
		case 31: 
			sendNotImplemented(code);
			break;
		case 35: 
			sendNotImplemented(code);
			break;
		case 37: 
			// "100101": vanaf nu aan hoge snelheid rijden
			//TODO: if called in another algorithm this will probably not work since they often use their own speeds.
			//			Possible solution: work with a flag in driver ('changed externally')?
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Setting a fast speed (100%).", "BARCODE");
			driver.setSpeed(100);
		case 39: 
			sendNotImplemented(code);
			break;
		case 43: 
			sendNotImplemented(code);
			break;
		case 47: 
			sendNotImplemented(code);
			break;
		case 55:
			// TODO: "110111": Deze tegel is de finish!
			driver.sendMessage("Executing " + convertIntToBinary(code)+": Marking this tile as the finish.", "BARCODE");
			break;
		default:
			driver.sendError(convertIntToBinary(code) + " is an illegal barcode. No action will be undertaken.");
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
	
}


