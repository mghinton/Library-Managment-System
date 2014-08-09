package client.userinterface.datatypes.user;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import client.control.data.entity.user.Administrator;
import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.Librarian;
import client.control.data.entity.user.User;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list to hold users
 * @author Daniel
 * @version 2
 *
 */
public class UserList extends ListPanel {
	
	private ArrayList<Object> users = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;

	public UserList() {
		super();
		table.setModel(new UserTableModel());
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return users;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		users = items;
	}

	@Override
	public Object getSelected() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1)
			return null;
		return users.get(selectedRow);
	}

	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				User user = (User) users.get(row);
				UserViewer viewer = new UserViewer(user);
				mainWindow.addTab(viewer.toString(), viewer);
			}
		}
	}

	private class UserTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// The column names
		private String[] columns = { "Type", "Library ID", "First Name", "Last Name", "Email Address" };

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			if (users == null)
				return 0;
			return users.size();
		}

		@Override
		public String getColumnName(int column) {
			if (column < 0 || column >= columns.length)
				return new String();
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (users == null)
				return null;
			if (rowIndex < 0 || rowIndex >= users.size())
				return null;
			if (columnIndex < 0 || columnIndex >= columns.length)
				return null;
			User user = (User) users.get(rowIndex);
			switch (columnIndex) {
			case 0:
				if (user instanceof Administrator)
					return "Administrator";
				else if (user instanceof Librarian)
					return "Librarian";
				else if (user instanceof Faculty)
					return "Faculty";
				else
					return "Student";
			case 1:
				return user.getID();
			case 2:
				return user.getFirstName();
			case 3:
				return user.getLastName();
			case 4:
				return user.getEmailAddress();
			}
			return user;
		}

	}

}
