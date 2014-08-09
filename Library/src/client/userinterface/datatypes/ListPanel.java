package client.userinterface.datatypes;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import util.Constants;

/**
 * Abstract class for generated lists.
 * 
 * @author Matthew Hinton
 */
public abstract class ListPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	protected JTable table = null;
	protected JScrollPane scrollPane = null;
	
	public ListPanel() {
		setBorder(null);
		
		setBackground(Constants.BACKPANEL);
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportBorder(null);
		add(scrollPane);
		
		table = new JTable();
		table.setBorder(null);
		scrollPane.setViewportView(table);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	}
	
	public abstract ArrayList<Object> getItems();
	
	public abstract void setItems(ArrayList<Object> items);
	
	public abstract Object getSelected();

	/**
	 * Returns the panel to its original state.
	 */
	public void reset() {
		setItems(null);
	}
	
}// end class