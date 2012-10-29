package bluebot.ui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import bluebot.sensors.SensorListener;



/**
 * 
 * @author Ruben Feyen
 */
public class SensorsComponent extends JPanel
		implements SensorListener {
	private static final long serialVersionUID = 1L;
	
	private static final Font DEFAULT_FONT =
			new Font(Font.MONOSPACED, Font.BOLD, 24);
	private static final Dimension DEFAULT_SIZE =
			new Dimension(50, 1);
	
	private GraphComponent graphLight, graphUltraSonic;
	private JLabel labelLight, labelUltraSonic;
	
	
	public SensorsComponent() {
		initComponents();
	}
	
	
	
	private static final GraphComponent createGraph() {
		final GraphComponent graph = new GraphComponent();
		graph.setPreferredSize(DEFAULT_SIZE);
		return graph;
	}
	
	private static final JLabel createLabel() {
		final JLabel label = new JLabel("???");
		label.setFont(DEFAULT_FONT);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setPreferredSize(DEFAULT_SIZE);
		label.setOpaque(true);
		return label;
	}
	
	private final void initComponents() {
		final GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 1D, 0D };
		layout.rowWeights = new double[] { 1D, 1D };
		setLayout(layout);
		
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets.set(2, 2, 2, 2);
		
		graphUltraSonic = createGraph();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(graphUltraSonic, gbc);
		
		labelUltraSonic = createLabel();
		
		gbc.gridx++;
		add(labelUltraSonic, gbc);
		
		graphLight = createGraph();
		
		gbc.gridx = 0;
		gbc.gridy++;
		add(graphLight, gbc);
		
		labelLight = createLabel();
		
		gbc.gridx++;
		add(labelLight, gbc);
	}
	
	public void onSensorValueLight(final int value) {
		System.out.println("light value = " + value);
		graphLight.addData(value);
		
		final int gray = (255 * value / 100);
//		final float s = (1F - (Math.abs(value - 50) / 50F));
//		final float b = (value / 100F);
		
		labelLight.setBackground(new Color(gray, gray, gray));
//		labelLight.setBackground(Color.getHSBColor(0.15F, s, b));
		labelLight.setForeground((value < 50) ? Color.WHITE : Color.BLACK);
		labelLight.setText(Integer.toString(value));
	}
	
	public void onSensorValueUltraSonic(final int value) {
		graphUltraSonic.addData(value);
		
		final float hue;
		if (value >= 20) {
			hue = 0.33F;
		} else if (value > 0) {
			hue = (0.33F * value / 20F);
		} else {
			hue = 0F;
		}
		
		labelUltraSonic.setBackground(Color.getHSBColor(hue, 1F, 1F));
		labelUltraSonic.setText(Integer.toString(value));
	}
	
}