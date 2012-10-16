package bluebot.ui;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
		
		addComponentListener(new AutoScroll());
//		model.addListDataListener(new AutoScroll());
	}
	
	
	
	public ControllerListener createControllerListener() {
		return new ControllerMonitor();
	}
	
	public JScrollPane createScrollPane() {
		return new JScrollPane(this,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	@Override
	public CommunicationListModel getModel() {
		return (CommunicationListModel)super.getModel();
	}
	
	
	
	
	
	
	
	
	
	
	private final class AutoScroll extends ComponentAdapter
			implements ListDataListener, Runnable {
		
		private boolean initial = true;
		
		
		
		@Override
		public void componentResized(final ComponentEvent event) {
			if (initial) {
				initial = false;
				return;
			}
			scroll();
//			scrollRectToVisible(getCellRect((getRowCount() - 1), 0, true));
		}
		
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
			if (SwingUtilities.isEventDispatchThread()) {
				run();
			} else {
				SwingUtilities.invokeLater(this);
			}
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