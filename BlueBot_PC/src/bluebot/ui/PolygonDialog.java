package bluebot.ui;


import static bluebot.ui.SwingUtils.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;



/**
 * 
 * @author Ruben Feyen
 */
public class PolygonDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private boolean confirmed;
	private JTextField inputCorners;
	private JTextField inputLength;
	
	
	public PolygonDialog() {
		initComponents();
		pack();
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);
	}
	
	
	
	public boolean confirm() {
		setVisible(true);
		return confirmed;
	}
	
	public int getCorners() {
		try {
			return Integer.parseInt(inputCorners.getText());
		} catch (final NumberFormatException e) {
			return -1;
		}
	}
	
	public float getLength() {
		try {
			return Float.parseFloat(inputLength.getText());
		} catch (final NumberFormatException e) {
			return -1F;
		}
	}
	
	private final void initComponents() {
		final JLabel lblCorners = new JLabel("Number of corners:");
		
		final JLabel lblLength = new JLabel("Length of side:");
		
		final Dimension sizeText = new Dimension(250, 25);
		
		inputCorners = new JTextField();
		inputCorners.setPreferredSize(sizeText);
		
		inputLength = new JTextField();
		inputLength.setPreferredSize(sizeText);
		
		final JButton btn = new JButton("Confirm");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				if (getCorners() < 0) {
					showWarning("Invalid input (corners)");
					return;
				}
				
				if (getLength() < 0) {
					showWarning("Invalid input (lenght)");
					return;
				}
				
				confirmed = true;
				dispose();
			}
		});
		
		add(groupVertical(
				groupHorizontal(
						groupVertical(lblCorners, lblLength),
						groupVertical(inputCorners, inputLength)),
				btn));
	}
	
}