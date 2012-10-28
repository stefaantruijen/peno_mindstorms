package bluebot.ui;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;



/**
 * 
 * @author Ruben Feyen
 */
public class CommunicationTable extends JTable {
	private static final long serialVersionUID = 1L;
	
	
	public CommunicationTable() {
		this(new CommunicationTableModel());
	}
	private CommunicationTable(final CommunicationTableModel model) {
		super(model);
		setComponentPopupMenu(createContextMenu());
		setDefaultRenderer(Icon.class, new IconTableCellRenderer());
		setDefaultRenderer(String.class, new MessageCellRenderer());
		setShowGrid(false);
		setTableHeader(null);
		
		final TableColumnModel columns = getColumnModel();
		columns.getColumn(0).setMaxWidth(32);
		columns.getColumn(1).setMaxWidth(32);
		
		addComponentListener(new AutoScroll());
	}
	
	
	
	private final JPopupMenu createContextMenu() {
		final JPopupMenu menu = new JPopupMenu();
		
		final JMenuItem itemClear = new JMenuItem("Clear");
		itemClear.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				getModel().clear();
			}
		});
		menu.add(itemClear);
		
		return menu;
	}
	
	public JScrollPane createScrollPane() {
		return new JScrollPane(this,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	@Override
	public CommunicationTableModel getModel() {
		return (CommunicationTableModel)super.getModel();
	}
	
	public void scrollToBottom() {
		scrollRectToVisible(getCellRect((getRowCount() - 1), 0, true));
	}
	
	
	
	
	
	
	
	
	
	
	private final class AutoScroll extends ComponentAdapter
			implements Runnable, TableModelListener {
		
		private boolean initial = true;
		
		
		
		@Override
		public void componentResized(final ComponentEvent event) {
			if (initial) {
				initial = false;
				return;
			}
			scrollRectToVisible(getCellRect((getRowCount() - 1), 0, true));
		}
		
		public void run() {
			scrollRectToVisible(getCellRect((getRowCount() - 1), 0, true));
		}
		
		private final void scroll() {
			if (SwingUtilities.isEventDispatchThread()) {
				run();
			} else {
				SwingUtilities.invokeLater(this);
			}
		}
		
		public void tableChanged(final TableModelEvent event) {
			scroll();
		}
		
	}
	
	
	
	
	
	private static final class MessageCellRenderer extends JTextArea
			implements TableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		private ArrayList<ArrayList<Integer>> rowColHeight = new ArrayList<ArrayList<Integer>>();
		
		
		public MessageCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
		}
		
		
		
		/**
		 * 
		 * @param table
		 * @param row
		 * @param col
		 * 
		 * @author <a href="http://blog.botunge.dk/post/2009/10/09/JTable-multiline-cell-renderer.aspx">Botunge</a>
		 */
		private final void adjustRowHeight(final JTable table, final int row,
				final int col) {
			// The trick to get this to work properly is to set the width of the
			// column to the
			// textarea. The reason for this is that getPreferredSize(), without
			// a width tries
			// to place all the text in one line. By setting the size with the
			// with of the column,
			// getPreferredSize() returnes the proper height which the row
			// should have in
			// order to make room for the text.
			int cWidth = table.getColumnModel().getColumn(col)
					.getWidth();
			setSize(new Dimension(cWidth, 1000));
			int prefH = getPreferredSize().height;
			while (rowColHeight.size() <= row) {
				rowColHeight.add(new ArrayList<Integer>(col));
			}
			ArrayList<Integer> colHeights = rowColHeight.get(row);
			while (colHeights.size() <= col) {
				colHeights.add(0);
			}
			colHeights.set(col, prefH);
			int maxH = prefH;
			for (Integer colHeight : colHeights) {
				if (colHeight > maxH) {
					maxH = colHeight;
				}
			}
			
			if (maxH < 32) {
				maxH = 32;
			}
			if (table.getRowHeight(row) != maxH) {
				table.setRowHeight(row, maxH);
			}
		}
		
		public Component getTableCellRendererComponent(final JTable table,
				final Object value,
				final boolean selected,
				final boolean focus,
				final int row,
				final int col) {
			setFont(table.getFont());
			setText((String)value);
			adjustRowHeight(table, row, col);
			return this;
		}
		
	}
	
}