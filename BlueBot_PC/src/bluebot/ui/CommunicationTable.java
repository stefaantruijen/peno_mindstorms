package bluebot.ui;


import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;



/**
 * 
 * @author Ruben Feyen
 */
public class CommunicationTable extends JTable {
	private static final long serialVersionUID = 1L;
	
	
	public CommunicationTable(final CommunicationTableModel model) {
		super(model);
		setDefaultRenderer(Icon.class, new IconTableCellRenderer());
//		setIntercellSpacing(new Dimension(25, 1));
		setRowHeight(32);
		setShowGrid(false);
		setTableHeader(null);
		
		final TableColumnModel columns = getColumnModel();
		columns.getColumn(0).setMaxWidth(32);
		columns.getColumn(1).setMaxWidth(32);
	}
	
	
	
	public static void main(final String... args) {
		try {
			final CommunicationTableModel model = new CommunicationTableModel();
			final CommunicationTable table = new CommunicationTable(model);
			
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					
					final JScrollPane scroller = new JScrollPane(table,
							JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					
					final JFrame frame = new JFrame("SHOWCASE");
					frame.add(scroller);
					frame.pack();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setLocationRelativeTo(null);
					frame.setResizable(false);
					frame.setVisible(true);
				}
			});
			
			for (int i = 15; i > 0; i--) {
				final String msg = Double.toString(Math.random());
				if (Math.random() < 0.5D) {
					model.addMessageIncoming(msg);
				} else {
					model.addMessageOutgoing(msg);
				}
				table.scrollToBottom();
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void scrollToBottom() {
		scrollRectToVisible(getCellRect((getRowCount() - 1), 0, true));
	}
	
}