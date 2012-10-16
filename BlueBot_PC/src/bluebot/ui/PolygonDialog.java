package bluebot.ui;


import static bluebot.ui.SwingUtils.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
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
		super((Window)null, "Polygon");
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
		final Dimension sizeLabel = new Dimension(190, 25);
		
		final JLabel lblCorners = new JLabel("Number of corners:");
		lblCorners.setPreferredSize(sizeLabel);
		
		final JLabel lblLength = new JLabel("Length of side:");
		lblLength.setPreferredSize(sizeLabel);
		lblLength.setToolTipText("Length is measured in millimeter");
		
		final Dimension sizeText = new Dimension(75, 25);
		
		inputCorners = new JTextField();
		inputCorners.setPreferredSize(sizeText);
		
		inputLength = new JTextField();
		inputLength.setPreferredSize(sizeText);
		
		final JButton btn = new JButton("Confirm");
		btn.setPreferredSize(new Dimension(275, 50));
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
		
		setLayout(new GridBagLayout());
		
		final GridBagConstraints gbc = SwingUtils.createGBC();
		gbc.insets.set(5, 5, 5, 5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(lblCorners, gbc);
		
		gbc.gridx++;
		add(inputCorners, gbc);
		
		gbc.gridx--;
		gbc.gridy++;
		add(lblLength, gbc);
		
		gbc.gridx++;
		add(inputLength, gbc);
		
		gbc.gridx--;
		gbc.gridy++;
		gbc.gridwidth = 2;
		add(btn, gbc);
	}
	
}