package bluebot.io.protocol.impl;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bluebot.io.protocol.Packet;



/**
 * Implementation of the {@link Packet} class for movement packets
 * 
 * @author Ruben Feyen
 */
public class MovePacket extends Packet {
	
	public static final int MOVE_BACKWARD = 2;
	public static final int MOVE_FORWARD  = 8;
	public static final int TURN_LEFT     = 4;
	public static final int TURN_RIGHT    = 6;
	
	private int direction;
	private Float quantity;
	
	
	public MovePacket(final DataInput input) throws IOException {
		super(input);
	}
	public MovePacket(final int direction) {
		setDirection(direction);
	}
	public MovePacket(final int direction, final float quantity) {
		this(direction);
		setQuantity(quantity);
	}
	
	
	
	/**
	 * Returns the direction
	 * 
	 * @return an <code>int</code> value from { 2, 4, 6, 8 }
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * Returns the quantity or <code>-1.0</code> if not quantified
	 * 
	 * @return a <code>float</code> value
	 * 
	 * @see {@link #isQuantified()}
	 */
	public float getQuantity() {
		return (isQuantified() ? quantity.floatValue() : -1F);
	}
	
	public int getOpcode() {
		return OP_MOVE;
	}
	
	/**
	 * Determines whether or not this move packet contains a quantity
	 * 
	 * @return <code>TRUE</code> if a quantity is given,
	 * 			<code>FALSE</code> otherwise
	 * 
	 * @see {@link #getQuantity()}
	 */
	public boolean isQuantified() {
		return (quantity != null);
	}
	
	protected void readPayload(final DataInput input) throws IOException {
		setDirection(input.readUnsignedByte());
		if (input.readBoolean()) {
			setQuantity(input.readFloat());
		}
	}
	
	private final void setDirection(final int direction) {
		this.direction = direction;
	}
	
	private final void setQuantity(final float quantity) {
		this.quantity = Float.valueOf(quantity);
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		switch (getDirection()) {
			case MOVE_BACKWARD:
				sb.append("Move backward");
				break;
			case MOVE_FORWARD:
				sb.append("Move forward");
				break;
			case TURN_LEFT:
				sb.append("Turn left");
				break;
			case TURN_RIGHT:
				sb.append("Turn right");
				break;
			default:
				sb.append("Unknown move command");
				break;
		}
		if (isQuantified()) {
			switch (getDirection()) {
				case MOVE_BACKWARD:
				case MOVE_FORWARD:
					sb.append(' ').append(getQuantity()).append(" mm");
					break;
				case TURN_LEFT:
				case TURN_RIGHT:
					sb.append(' ').append(getQuantity()).append(" degrees");
					break;
			}
		}
		return sb.toString();
	}
	
	protected void writePayload(final DataOutput output) throws IOException {
		output.writeByte(getDirection());
		if (isQuantified()) {
			output.writeBoolean(true);
			output.writeFloat(getQuantity());
		} else {
			output.writeBoolean(false);
		}
	}
	
}