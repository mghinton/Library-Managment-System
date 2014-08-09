package client.userinterface.datatypes.subscriptions;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import util.Constants;
import util.Utilities;

import client.control.data.entity.management.Subscription;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list to view subscriptions
 * @author Matthew
 * @version 3
 *
 */
public class SubscriptionList extends ListPanel {
	
	private ArrayList<Object> subs = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;

	public SubscriptionList() {
		super();
		table.setModel(new UserTableModel());
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return subs;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		subs = items;
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}
	
	@Override
	 public Object getSelected() {
	  int selectedRow = table.getSelectedRow();
	  if (selectedRow == -1)
	   return null;
	  return subs.get(selectedRow);
	 }


	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			//int col = table.columnAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				Subscription sub = (Subscription)subs.get(row);
				SubscriptionViewer viewer = new SubscriptionViewer(sub);
				mainWindow.addTab(viewer.toString(), viewer);
			}
			//String value = (String) table.getValueAt(row, col);
		}
	}

	private class UserTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// The column names
		private String[] columns = { "ID", "Title", "Contact Phone", "Contact Email", "Expiration Date"};

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			if (subs == null)
				return 0;
			return subs.size();
		}

		@Override
		public String getColumnName(int column) {
			if (column < 0 || column >= columns.length)
				return new String();
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (subs == null)
				return null;
			if (rowIndex < 0 || rowIndex >= subs.size())
				return null;
			if (columnIndex < 0 || columnIndex >= columns.length)
				return null;
			Subscription subscription = (Subscription) subs.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return String.valueOf(subscription.getID());
			case 1:
				return subscription.getTitle();
			case 2:
				return String.valueOf(subscription.getContactPhone());
			case 3:
				return subscription.getContactEmail();
			case 4:
				return subscription.getExpirationDate() == null ? "" : Utilities.formatDate(subscription.getExpirationDate(),
						Constants.DEFAULT_DATE_FORMAT,
						Constants.DEFAULT_TIME_ZONE);
			}
			return subscription;
		}

	}

}
