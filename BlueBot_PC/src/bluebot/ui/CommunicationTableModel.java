package bluebot.ui;


import java.net.URL;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;



/**
 * 
 * @author Ruben Feyen
 */
public class CommunicationTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Entry> entries;
	
	
	public CommunicationTableModel() {
		entries = new ArrayList<Entry>();
	}
	
	
	
	private final void addMessage(final Entry entry) {
		final int row;
		synchronized (entries) {
			row = entries.size();
			entries.add(entry);
		}
		fireTableRowsInserted(row, row);
	}
	
	public void addMessageIncoming(final String msg) {
		addMessage(new Entry.IncomingEntry(msg));
	}
	
	public void addMessageOutgoing(final String msg) {
		addMessage(new Entry.OutgoingEntry(msg));
	}
	
	@Override
	public Class<?> getColumnClass(final int col) {
		switch (col) {
			case 0:
			case 1:
				return Icon.class;
			case 2:
				return String.class;
			default:
				throw new IllegalArgumentException("Invalid col index:  " + col);
		}
	}
	
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public String getColumnName(final int col) {
		switch (col) {
			case 0:
				return "Sender";
			case 1:
				return "Receiver";
			case 2:
				return "Message";
			default:
				throw new IllegalArgumentException("Invalid col index:  " + col);
		}
	}
	
	private static final Icon getIcon(final String name) {
		return new ImageIcon(getIconUrl(name));
	}
	
	private static final URL getIconUrl(final String name) {
		return CommunicationTableModel.class.getResource(name + ".png");
	}
	
	public int getRowCount() {
		return entries.size();
	}
	
	public Object getValueAt(final int row, final int col) {
		final Entry entry = entries.get(row);
		switch (col) {
			case 0:
				return entry.getSenderIcon();
			case 1:
				return entry.getReceiverIcon();
			case 2:
				return entry.getMessage();
			default:
				throw new RuntimeException();
		}
	}
	
	
	
	
	
	
	
	
	
	
	private static abstract class Entry {
		
		protected static final Icon ICON_NXJ;
		protected static final Icon ICON_PC;
		static {
			ICON_NXJ = getIcon("nxj");
			ICON_PC = getIcon("pc");
		}
		
		private String msg;
		
		
		public Entry(final String msg) {
			this.msg = msg;
		}
		
		
		
		public String getMessage() {
			return msg;
		}
		
		public abstract Icon getReceiverIcon();
		
		public abstract Icon getSenderIcon();
		
		
		
		
		
		
		
		
		
		
		public static final class IncomingEntry extends Entry {
			
			public IncomingEntry(final String msg) {
				super(msg);
			}
			
			
			
			public Icon getReceiverIcon() {
				return ICON_PC;
			}
			
			public Icon getSenderIcon() {
				return ICON_NXJ;
			}
			
		}
		
		
		
		
		
		public static final class OutgoingEntry extends Entry {
			
			public OutgoingEntry(final String msg) {
				super(msg);
			}
			
			
			
			public Icon getReceiverIcon() {
				return ICON_NXJ;
			}
			
			public Icon getSenderIcon() {
				return ICON_PC;
			}
			
		}
		
	}
	
}