package bluebot.util;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;



/**
 * Utility class for loading external resources
 * 
 * @author Ruben Feyen
 */
public final class Resources {
	
	private Resources() {
		// disabled
	}
	
	
	
	public static final Icon loadIcon(final Class<?> owner,
			final String name) {
		final BufferedImage image = loadImage(owner, name);
		return ((image == null) ? null : new ImageIcon(image));
	}
	
	public static final BufferedImage loadImage(final Class<?> owner,
			final String name) {
		try {
			return ImageIO.read(owner.getResource(name));
		} catch (final IOException e) {
			return null;
		}
	}
	
}