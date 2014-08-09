package client.userinterface.datatypes.resourcetype;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import client.control.data.entity.management.Subscription;
import client.control.data.entity.resource.ResourceType;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list that holds any number of resource types.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceTypeList extends ListPanel {
	
	private ArrayList<Object> resourceTypes = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;

	public ResourceTypeList() {
		super();
		table.setModel(new UserTableModel());
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return resourceTypes;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		resourceTypes = (ArrayList<Object>) items;
	}
	
	@Override
	 public Object getSelected() {
	  int selectedRow = table.getSelectedRow();
	  if (selectedRow == -1)
	   return null;
	  return resourceTypes.get(selectedRow);
	 }


	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			//int col = table.columnAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				ResourceType resourceType = (ResourceType) resourceTypes.get(row);
				ResourceTypeViewer viewer = new ResourceTypeViewer(resourceType);
				mainWindow.addTab(viewer.toString(), viewer);
			}
			//String value = (String) table.getValueAt(row, col);
		}
	}

	private class UserTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// The column names
		private String[] columns = { "Name", "Title Label", "Creator Label",
				"Company Label", "Serial Number Label", "Staff Loan Period",
				"Faculty Loan Period", "Student Loan Period", "Enabled"};

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			if (resourceTypes == null)
				return 0;
			return resourceTypes.size();
		}

		@Override
		public String getColumnName(int column) {
			if (column < 0 || column >= columns.length)
				return new String();
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (resourceTypes == null)
				return null;
			if (rowIndex < 0 || rowIndex >= resourceTypes.size())
				return null;
			if (columnIndex < 0 || columnIndex >= columns.length)
				return null;
			ResourceType resourceType = ((ResourceType) resourceTypes.get(rowIndex));
			switch (columnIndex) {
			case 0:
				return resourceType.getName();
			case 1:
				return resourceType.getTitleLabel();
			case 2:
				return resourceType.getCreatorLabel();
			case 3:
				return resourceType.getCompanyLabel();
			case 4:
				return resourceType.getSerialNumberLabel();
			case 5:
				return resourceType.getStaffPeriod();
			case 6:
				return resourceType.getFacultyPeriod();
			case 7:
				return resourceType.getStudentPeriod();
			case 8:
				return resourceType.getEnabled() ? "Yes" : "No";
			}
			return ((Subscription)resourceTypes.get(rowIndex));
		}

	}

}
