package client.userinterface.datatypes.todo;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import util.Constants;
import util.Utilities;

import client.control.data.entity.management.ToDo;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list to view a list of ToDos
 * @author Matthew
 * @version 3
 *
 */
public class ToDoList extends ListPanel {
	
	private ArrayList<Object> todos = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;

	public ToDoList() {
		super();
		table.setModel(new UserTableModel());
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return todos;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		todos = (ArrayList<Object>) items;
	}
	
	@Override
	 public Object getSelected() {
	  int selectedRow = table.getSelectedRow();
	  if (selectedRow == -1)
	   return null;
	  return todos.get(selectedRow);
	 }


	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			//int col = table.columnAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				ToDo todo = (ToDo) todos.get(row);
				ToDoViewer viewer = new ToDoViewer(todo);
				mainWindow.addTab(viewer.toString(), viewer);
			}
			//String value = (String) table.getValueAt(row, col);
		}
	}

	private class UserTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// The column names
		private String[] columns = { "ID", "Title", "Start Date", "End Date", "Completed" };

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			if (todos == null)
				return 0;
			return todos.size();
		}

		@Override
		public String getColumnName(int column) {
			if (column < 0 || column >= columns.length)
				return new String();
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (todos == null)
				return null;
			if (rowIndex < 0 || rowIndex >= todos.size())
				return null;
			if (columnIndex < 0 || columnIndex >= columns.length)
				return null;
			ToDo todo = (ToDo) todos.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return String.valueOf(todo.getID());
			case 1:
				return todo.getTitle();
			case 2:
				return todo.getStartDate() == null? "" : Utilities.formatDate(todo.getStartDate(),
					Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			case 3:
				return todo.getEndDate() == null? "" : Utilities.formatDate(todo.getEndDate(),
					Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			case 4:
				return todo.getCompleted() ? "Yes" : "No" ;
			}
			return todo;
		}

	}

}
