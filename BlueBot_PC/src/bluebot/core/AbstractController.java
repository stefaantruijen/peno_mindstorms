package bluebot.core;


import bluebot.ConfigListener;
import bluebot.MotionListener;
import bluebot.graph.Tile;
import bluebot.io.Message;
import bluebot.io.MessageListener;
import bluebot.maze.MazeListener;
import bluebot.sensors.SensorListener;
import bluebot.util.AbstractEventDispatcher;



/**
 * Skeletal implementation of the {@link Controller} interface
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractController
		extends AbstractEventDispatcher<ControllerListener>
		implements Controller {
	
	private ConfigDispatcher config;
	private MazeDispatcher maze;
	private MessageDispatcher messages;
	private MotionDispatcher motion;
	private SensorDispatcher sensors;
	
	
	public AbstractController() {
		this.config = new ConfigDispatcher();
		this.maze = new MazeDispatcher();
		this.messages = new MessageDispatcher();
		this.motion = new MotionDispatcher();
		this.sensors = new SensorDispatcher();
	}
	
	
	
	public void addListener(final ConfigListener listener) {
		config.addListener(listener);
	}
	
	public void addListener(final MazeListener listener) {
		maze.addListener(listener);
	}
	
	public void addListener(final MessageListener listener) {
		messages.addListener(listener);
	}
	
	public void addListener(final MotionListener listener) {
		motion.addListener(listener);
	}
	
	public void addListener(final SensorListener listener) {
		sensors.addListener(listener);
	}
	
	public void dispose() {
		removeListeners();
	}
	
	protected void fireError(final String msg) {
		for (final ControllerListener listener : getListeners()) {
			listener.onError(msg);
		}
	}
	
	protected void fireMessage(final String msg, final String title) {
		for (final ControllerListener listener : getListeners()) {
			listener.onMessage(msg, title);
		}
	}
	
	protected void fireMessageIncoming(final Message msg) {
		messages.fireMessageIncoming(msg);
	}
	
	protected void fireMessageOutgoing(final Message msg) {
		messages.fireMessageOutgoing(msg);
	}
	
	protected void fireMotion(final float x, final float y,
			final float body, final float head) {
		motion.fireMotion(x, y, body, head);
	}
	
	protected void fireSensorInfrared(final int value) {
		sensors.fireInfrared(value);
	}
	
	protected void fireSensorLight(final int value) {
		sensors.fireLight(value);
	}
	
	protected void fireSensorUltraSonic(final int value) {
		sensors.fireUltraSonic(value);
	}
	
	protected void fireSpeed(final int value){
		sensors.fireSpeed(value);
	}
	
	protected void fireSpeedChanged(final int percentage) {
		config.fireSpeedChanged(percentage);
	}
	
	protected void fireTileUpdated(final Tile tile) {
		maze.fireTileUpdated(tile);
	}
	
	public void removeListener(final ConfigListener listener) {
		config.removeListener(listener);
	}
	
	public void removeListener(final MazeListener listener) {
		maze.removeListener(listener);
	}
	
	public void removeListener(final MessageListener listener) {
		messages.removeListener(listener);
	}
	
	public void removeListener(final MotionListener listener) {
		motion.removeListener(listener);
	}
	
	public void removeListener(final SensorListener listener) {
		sensors.removeListener(listener);
	}
	
	@Override
	public void removeListeners() {
		super.removeListeners();
		config.removeListeners();
		maze.removeListeners();
		sensors.removeListeners();
	}
	
	
	
	
	
	
	
	
	
	
	private static final class ConfigDispatcher extends AbstractEventDispatcher<ConfigListener> {
		
		public void fireSpeedChanged(final int percentage) {
			for (final ConfigListener listener : getListeners()) {
				listener.onSpeedChanged(percentage);
			}
		}
		
	}
	
	
	
	
	
	private static final class MazeDispatcher extends AbstractEventDispatcher<MazeListener> {
		
		public void fireTileUpdated(final Tile tile) {
			for (final MazeListener listener : getListeners()) {
				listener.onTileUpdate(tile);
			}
		}
		
	}
	
	
	
	
	
	private static final class MessageDispatcher
			extends AbstractEventDispatcher<MessageListener> {
		
		public void fireMessageIncoming(final Message msg) {
			for (final MessageListener listener : getListeners()) {
				listener.onMessageIncoming(msg);
			}
		}
		
		public void fireMessageOutgoing(final Message msg) {
			for (final MessageListener listener : getListeners()) {
				listener.onMessageOutgoing(msg);
			}
		}
		
	}
	
	
	
	
	
	private static final class MotionDispatcher
			extends AbstractEventDispatcher<MotionListener> {
		
		public void fireMotion(final float x, final float y,
				final float body, final float head) {
			for (final MotionListener listener : getListeners()) {
				listener.onMotion(x, y, body, head);
			}
		}
		
	}
	
	
	
	
	
	private static final class SensorDispatcher extends AbstractEventDispatcher<SensorListener> {
		
		public void fireInfrared(final int value) {
			for (final SensorListener listener : getListeners()) {
				listener.onSensorValueInfrared(value);
			}
		}
		
		public void fireSpeed(int value) {
			for (final SensorListener listener : getListeners()) {
				listener.onSensorSpeed(value);
			}
		}

		public void fireLight(final int value) {
			for (final SensorListener listener : getListeners()) {
				listener.onSensorValueLight(value);
			}
		}
		
		public void fireUltraSonic(final int value) {
			for (final SensorListener listener : getListeners()) {
				listener.onSensorValueUltraSonic(value);
			}
		}
		
	}
	
}