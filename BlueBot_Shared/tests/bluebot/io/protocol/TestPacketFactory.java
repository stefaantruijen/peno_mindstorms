package bluebot.io.protocol;


import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import bluebot.io.protocol.impl.MovePacket;
import bluebot.io.protocol.impl.StopPacket;



/**
 * 
 * @author Ruben Feyen
 */
public final class TestPacketFactory {
	
	private static final double DELTA = 0.001D;
	
	private static PacketFactory factory;
	
	
	
	private static final void assertIsMovePacket(final Packet packet) {
		assertEquals(Packet.OP_MOVE, packet.getOpcode());
		assertTrue(packet instanceof MovePacket);
	}
	
	private static final void assertIsStopPacket(final Packet packet) {
		assertEquals(Packet.OP_STOP, packet.getOpcode());
		assertTrue(packet instanceof StopPacket);
	}
	
	private static final float getRandomQuantity() {
		return (float)(Math.random() * 100);
	}
	
	@BeforeClass
	public static void init() {
		factory = PacketFactory.getPacketFactory();
	}
	
	private static final void testCreateMove(final int direction, final Generator generator) {
		float quantity;
		MovePacket move;
		Packet packet;
		
		packet = generator.generate(factory);
		assertIsMovePacket(packet);
		move = (MovePacket)packet;
		assertEquals(direction, move.getDirection());
		assertFalse(move.isQuantified());
		
		quantity = getRandomQuantity();
		packet = generator.generate(factory, quantity);
		assertIsMovePacket(packet);
		move = (MovePacket)packet;
		assertEquals(direction, move.getDirection());
		assertTrue(move.isQuantified());
		assertEquals(quantity, move.getQuantity(), DELTA);
	}
	
	@Test
	public void testCreateMoveBackward() {
		testCreateMove(MovePacket.MOVE_BACKWARD, new Generator() {
			public Packet generate(PacketFactory factory) {
				return factory.createMoveBackward();
			}
			
			public Packet generate(PacketFactory factory, float quantity) {
				return factory.createMoveBackward(quantity);
			}
		});
	}
	
	@Test
	public void testCreateMoveForward() {
		testCreateMove(MovePacket.MOVE_FORWARD, new Generator() {
			public Packet generate(PacketFactory factory) {
				return factory.createMoveForward();
			}
			
			public Packet generate(PacketFactory factory, float quantity) {
				return factory.createMoveForward(quantity);
			}
		});
	}
	
	@Test
	public void testCreateStop() {
		final Packet packet = factory.createStop();
		assertIsStopPacket(packet);
		assertTrue(packet == StopPacket.SINGLETON);
	}
	
	@Test
	public void testCreateTurnLeft() {
		testCreateMove(MovePacket.TURN_LEFT, new Generator() {
			public Packet generate(PacketFactory factory) {
				return factory.createTurnLeft();
			}
			
			public Packet generate(PacketFactory factory, float quantity) {
				return factory.createTurnLeft(quantity);
			}
		});
	}
	
	@Test
	public void testCreateTurnRight() {
		testCreateMove(MovePacket.TURN_RIGHT, new Generator() {
			public Packet generate(PacketFactory factory) {
				return factory.createTurnRight();
			}
			
			public Packet generate(PacketFactory factory, float quantity) {
				return factory.createTurnRight(quantity);
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	private static interface Generator {
		
		public Packet generate(PacketFactory factory);
		
		public Packet generate(PacketFactory factory, float quantity);
		
	}
	
}
