package bluebot.io.protocol.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;

public class ReadBarcodePacket extends Packet {

	public ReadBarcodePacket(){
		
	}
	
	@Override
	public int getOpcode() {
		return OP_READBARCODE;
	}

	@Override
	protected void readPayload(DataInput input) throws IOException {
		//ignored
	}

	@Override
	protected void writePayload(DataOutput output) throws IOException {
		//ignored
	}

}
