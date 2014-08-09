package client.userinterface.datatypes.reference;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import util.Constants;
import util.Utilities;
import client.control.data.entity.resource.Reference;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list to hold any number of references.
 * 
 * @author Daniel Huettner
 * @version 4
 */
public class ReferenceList extends ListPanel {
	
	private ArrayList<Object> references = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;

	public ReferenceList() {
		super();
		table.setModel(new UserTableModel());
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return references;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		references = (ArrayList<Object>) items;
	}
	
	@Override
	 public Object getSelected() {
	  int selectedRow = table.getSelectedRow();
	  if (selectedRow == -1)
	   return null;
	  return references.get(selectedRow);
	 }


	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			//int col = table.columnAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				Reference reference = (Reference) references.get(row);
				ReferenceViewer viewer = new ReferenceViewer(reference);
				mainWindow.addTab(viewer.toString(), viewer);
			}
			//String value = (String) table.getValueAt(row, col);
		}
	}

	private class UserTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// The column names
		private String[] columns = { "ID", "Start Date", "End Date"};

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			if (references == null)
				return 0;
			return references.size();
		}

		@Override
		public String getColumnName(int column) {
			if (column < 0 || column >= columns.length)
				return new String();
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (references == null)
				return null;
			if (rowIndex < 0 || rowIndex >= references.size())
				return null;
			if (columnIndex < 0 || columnIndex >= columns.length)
				return null;
			Reference reference = (Reference) references.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return reference.getID();
			case 1:
				if (reference.getStartDate() == null)
					return "";
				return Utilities.formatDate(reference.getStartDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			case 2:
				if (reference.getEndDate() == null)
					return "";
				return Utilities.formatDate(reference.getEndDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			}
			return reference;
		}

	}

}
