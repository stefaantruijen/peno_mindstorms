package bluebot.ui;


import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;



/**
 * 
 * @author Ruben Feyen
 */
public class CommunicationListModel extends AbstractListModel {
	private static final long serialVersionUID = 1L;
	
	public static final int MAX_ENTRIES = 1000;
	
	private ArrayList<Entry> entries;
	
	
	public CommunicationListModel() {
		entries = new ArrayList<Entry>();
	}
	
	
	
	private final void addMessage(final Entry entry) {
		final int row;
		final boolean trimmed;
		synchronized (entries) {
			if (entries.size() < MAX_ENTRIES) {
				row = entries.size();
				trimmed = false;
			} else {
				row = (MAX_ENTRIES - 1);
				entries = new ArrayList<Entry>(entries.subList((entries.size() - row), row));
				trimmed = true;
			}
			entries.add(entry);
		}
		if (trimmed) {
			fireContentsChanged(this, 0, row);
		} else {
			fireIntervalAdded(this, row, row);
		}
	}
	
	public void addMessageIncoming(final String msg) {
		addMessage(new Entry.IncomingEntry(msg));
	}
	
	public void addMessageOutgoing(final String msg) {
		addMessage(new Entry.OutgoingEntry(msg));
	}
	
	public Entry getElementAt(final int index) {
		return entries.get(index);
	}
	
	private static final Icon getIcon(final String name) {
		return new ImageIcon(getIconUrl(name));
	}
	
	private static final URL getIconUrl(final String name) {
		return CommunicationTableModel.class.getResource(name + ".png");
	}
	
	public int getSize() {
		return entries.size();
	}
	
	
	
	
	
	
	
	
	
	
	public static abstract class Entry {
		
		protected static final Icon ICON_NXJ;
		protected static final Icon ICON_PC;
		static {
			ICON_NXJ = CommunicationListModel.getIcon("nxj");
			ICON_PC = CommunicationListModel.getIcon("pc");
		}
		
		private String msg;
		
		
		public Entry(final String msg) {
			this.msg = msg;
		}
		
		
		
		public String getMessage() {
			return msg;
		}
		
		public abstract Icon getIcon();
		
		
		
		
		
		
		
		
		
		
		private static final class IncomingEntry extends Entry {
			
			public IncomingEntry(final String msg) {
				super(msg);
			}
			
			
			
			public Icon getIcon() {
				return ICON_NXJ;
			}
			
		}
		
		
		
		
		
		private static final class OutgoingEntry extends Entry {
			
			public OutgoingEntry(final String msg) {
				super(msg);
			}
			
			
			
			public Icon getIcon() {
				return ICON_PC;
			}
			
		}
		
	}
	
}