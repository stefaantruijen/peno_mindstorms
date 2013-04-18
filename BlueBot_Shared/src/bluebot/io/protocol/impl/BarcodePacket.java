package bluebot.io.protocol.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;

public class BarcodePacket extends Packet {
	
	private int barcode;
	
	/**
	 * Creates a new barcodePacket with the given input as barcode.
	 * @param input		The barcode
	 * @throws IOException
	 */
	public BarcodePacket(final DataInput input) throws IOException {
		super(input);
	}
	
	/**
	 * Creates a new barcodePacket with the given input as barcode.
	 * @param input		The barcode
	 */
	public BarcodePacket(int barcode) {
		setBarcode(barcode);
	}

	@Override
	public int getOpcode() {
		return OP_BARCODE;
	}
	
	private void setBarcode(int barcode){
		this.barcode = barcode;
	}

	@Override
	protected void readPayload(DataInput input) throws IOException {
		setBarcode(input.readInt());
		
	}

	@Override
	protected void writePayload(DataOutput output) throws IOException {
		output.writeInt(barcode);
	}
	
	public int getBarcode(){
		return barcode;
	}

}
