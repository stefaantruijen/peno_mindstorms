package bluebot.core;


import bluebot.ConfigListener;
import bluebot.graph.Tile;
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
	private SensorDispatcher sensors;
	
	
	public AbstractController() {
		this.config = new ConfigDispatcher();
		this.maze = new MazeDispatcher();
		this.sensors = new SensorDispatcher();
	}
	
	
	
	public void addListener(final ConfigListener listener) {
		config.addListener(listener);
	}
	
	public void addListener(final MazeListener listener) {
		maze.addListener(listener);
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
	
	protected void fireMotion(final float x, final float y,
			final float heading) {
		maze.fireMotion(x, y, heading);
	}
	
	protected void fireSensorLight(final int value) {
		sensors.fireLight(value);
	}
	
	protected void fireSensorUltraSonic(final int value) {
		sensors.fireUltraSonic(value);
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
		
		public void fireMotion(final float x, final float y,
				final float heading) {
			for (final MazeListener listener : getListeners()) {
				listener.onMotion(x, y, heading);
			}
		}
		
		public void fireTileUpdated(final Tile tile) {
			for (final MazeListener listener : getListeners()) {
				listener.onTileUpdate(tile);
			}
		}
		
	}
	
	
	
	
	
	private static final class SensorDispatcher extends AbstractEventDispatcher<SensorListener> {
		
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