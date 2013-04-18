package bluebot.ui;


import javax.swing.JFrame;



/**
 * 
 * @author Ruben Feyen
 */
public class AbstractFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	
	public AbstractFrame(final String title) {
		super(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	
	
	@Override
	public void pack() {
		super.pack();
		setLocationRelativeTo(null);
	}
	
}
