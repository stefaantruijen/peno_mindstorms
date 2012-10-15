package bluebot.ui;


import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import bluebot.core.ControllerAdapter;
import bluebot.core.ControllerListener;



/**
 * 
 * @author Ruben Feyen
 */
public class CommunicationList extends JList {
	private static final long serialVersionUID = 1L;
	
	
	public CommunicationList() {
		this(new CommunicationListModel());
	}
	private CommunicationList(final CommunicationListModel model) {
		super(model);
		setCellRenderer(new CommunicationListCellRenderer());
		setFocusable(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		model.addListDataListener(new AutoScroll());
	}
	
	
	
	public ControllerListener createControllerListener() {
		return new ControllerMonitor();
	}
	
	public JScrollPane createScrollPane() {
		return new JScrollPane(this,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	@Override
	public CommunicationListModel getModel() {
		return (CommunicationListModel)super.getModel();
	}
	
	public static void main(final String... args) {
		try {
			final CommunicationList list = new CommunicationList();
			final CommunicationListModel model = list.getModel();
			
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					final JFrame frame = new JFrame("SHOWCASE");
					frame.add(list.createScrollPane());
					frame.pack();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					frame.setVisible(true);
				}
			});
			
			for (int i = 15; i > 0; i--) {
				final String msg = Double.toString(Math.random());
				if ((i & 0x1) == 0) {
					model.addMessageIncoming(msg);
				} else {
					model.addMessageOutgoing(msg);
				}
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	private final class AutoScroll implements ListDataListener, Runnable {
		
		public void contentsChanged(final ListDataEvent event) {
			scroll();
		}
		
		public void intervalAdded(final ListDataEvent event) {
			scroll();
		}
		
		public void intervalRemoved(final ListDataEvent event) {
			scroll();
		}
		
		public void run() {
			ensureIndexIsVisible(getModel().getSize() - 1);
		}
		
		private final void scroll() {
			SwingUtilities.invokeLater(this);
		}
		
	}
	
	
	
	
	
	private final class ControllerMonitor extends ControllerAdapter {
		
		@Override
		public void onMessageIncoming(final String msg) {
			getModel().addMessageIncoming(msg);
		}
		
		@Override
		public void onMessageOutgoing(final String msg) {
			getModel().addMessageOutgoing(msg);
		}
		
	}
	
}