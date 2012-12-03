package bluebot.ui;


import java.awt.Window;

import javax.swing.JDialog;



/**
 * 
 * @author Ruben Feyen
 */
public abstract class AbstractDialog<T> extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private T result;
	
	
	public AbstractDialog(final Window owner, final String title) {
		super(owner, title);
		initComponents();
		pack();
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);
	}
	
	
	
	public T display() {
		setVisible(true);
		return result;
	}
	
	protected abstract void initComponents();
	
	protected void setResult(final T result) {
		this.result = result;
	}
	
}