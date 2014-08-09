package client.userinterface.datatypes.reserve;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import util.Constants;
import util.Utilities;
import client.control.data.entity.resource.Reserve;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list to populate with any number of reserves.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ReserveList extends ListPanel {
	
	private ArrayList<Object> reserves = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;

	public ReserveList() {
		super();
		table.setModel(new UserTableModel());
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return reserves;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		reserves = (ArrayList<Object>) items;
	}
	
	@Override
	 public Object getSelected() {
	  int selectedRow = table.getSelectedRow();
	  if (selectedRow == -1)
	   return null;
	  return reserves.get(selectedRow);
	 }


	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			//int col = table.columnAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				Reserve reserve = (Reserve) reserves.get(row);
				ReserveViewer viewer = new ReserveViewer(reserve);
				mainWindow.addTab(viewer.toString(), viewer);
			}
			//String value = (String) table.getValueAt(row, col);
		}
	}

	private class UserTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// The column names
		private String[] columns = { "ID", "Reservation Date", "Available Date"};

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			if (reserves == null)
				return 0;
			return reserves.size();
		}

		@Override
		public String getColumnName(int column) {
			if (column < 0 || column >= columns.length)
				return new String();
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (reserves == null)
				return null;
			if (rowIndex < 0 || rowIndex >= reserves.size())
				return null;
			if (columnIndex < 0 || columnIndex >= columns.length)
				return null;
			Reserve reserve = (Reserve) reserves.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return reserve.getID();
			case 1:
				if (reserve.getReservationDate() == null)
					return "";
				return Utilities.formatDate(reserve.getReservationDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			case 2:
				if (reserve.getAvailableDate() == null)
					return "";
				return Utilities.formatDate(reserve.getAvailableDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			}
			return reserve;
		}

	}

}
