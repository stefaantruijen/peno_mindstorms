package bluebot.ui.rendering;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;



/**
 * Represents a texture
 * 
 * @author Ruben Feyen
 */
public class Texture {
	
	private int height;
	private int[] pixels;
	private int width;
	
	
	public Texture(final BufferedImage image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
	}
	
	
	
	public int getColor(final double x, final double y) {
		return pixels[(width * (int)(y * height)) + (int)(x * width)];
	}
	
	public static final Texture load(final String name) {
		try {
			return new Texture(ImageIO.read(Texture.class.getResource(name)));
		} catch (final IOException e) {
			return null;
		}
	}
	
}