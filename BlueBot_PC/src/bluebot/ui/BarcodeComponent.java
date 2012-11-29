package bluebot.ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;



/**
 * 
 * @author Ruben Feyen
 */
public class BarcodeComponent extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private final BarcodeIcon icon = new BarcodeIcon();
	
	
	public BarcodeComponent() {
		super("");
		setIcon(icon);
	}
	
	
	
	public void setBarcode(final int barcode) {
		icon.setBarcode(barcode);
	}
	
	
	
	
	
	
	
	
	
	
	private static final class BarcodeIcon implements Icon {
		
		private static final int STROKE_WIDTH = 5;
		
		private int barcode;
		
		
		
		public int getIconHeight() {
			return 24;
		}
		
		public int getIconWidth() {
			return (8 * STROKE_WIDTH);
		}
		
		public void paintIcon(final Component c,
				final Graphics gfx, final int x, final int y) {
			final int h = getIconHeight();
			if (barcode <= 0) {
				gfx.clearRect(0, 0, getIconWidth(), h);
				return;
			}
			
			gfx.setColor(Color.BLACK);
			gfx.fillRect(0, 0, getIconWidth(), getIconHeight());
			
			for (int i = 5; i >= 0; i--) {
				if ((barcode & (1 << i)) == 0) {
					// Black
					gfx.setColor(Color.WHITE);
				} else {
					// White
					gfx.setColor(Color.WHITE);
					gfx.fillRect(((6 - i) * STROKE_WIDTH), 0, STROKE_WIDTH, h);
				}
			}
		}
		
		public void setBarcode(final int barcode) {
			this.barcode = (barcode & 0xFF);
		}
		
	}
	
}