package client.userinterface.datatypes.resourcecopy;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import client.control.data.entity.resource.ResourceCopy;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list to populate with any number of resources.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceCopyList extends ListPanel {
	
	private ArrayList<Object> resourceCopies = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;
	
	public ResourceCopyList() {
		super();
		table.setModel(new ResourceTableModel());
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return resourceCopies;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		resourceCopies = items;
	}

	@Override
	public Object getSelected() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1)
			return null;
		return resourceCopies.get(selectedRow);
	}

	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				ResourceCopy resourceCopy = (ResourceCopy) resourceCopies.get(row);
				ResourceCopyViewer viewer = new ResourceCopyViewer(resourceCopy);
				mainWindow.addTab(viewer.toString(), viewer);
			}
		}
	}

	private class ResourceTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		
		// The column names
		private String[] columns = { "Barcode (ID)", "Enabled" };

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			if (resourceCopies == null)
				return 0;
			return resourceCopies.size();
		}

		@Override
		public String getColumnName(int column) {
			if (column < 0 || column >= columns.length)
				return new String();
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (resourceCopies == null)
				return null;
			if (rowIndex < 0 || rowIndex >= resourceCopies.size())
				return null;
			if (columnIndex < 0 || columnIndex >= columns.length)
				return null;
			ResourceCopy resourceCopy = (ResourceCopy) resourceCopies.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return String.valueOf(resourceCopy.getID());
			case 1:
				return resourceCopy.getEnabled() ? "Yes" : "No";
			}
			return resourceCopy;
		}

	}
	
}
