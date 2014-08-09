package client.userinterface.datatypes.resource;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import util.Constants;
import util.Utilities;
import client.control.data.entity.resource.Resource;
import client.control.data.entity.resource.ResourceType;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list to hold any number of resources.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceList extends ListPanel {
	
	private ArrayList<Object> resources = null;

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow = null;
	
	private ResourceType type = null;
	
	private ResourceTableModel resourceTableModel;

	public ResourceList(ResourceType type) {
		super();
		this.type = type;
		resourceTableModel = new ResourceTableModel();
		table.setModel(resourceTableModel);
		table.addMouseListener(new TableMouseAdapter());
		mainWindow = MainWindow.getReference();
	}
	
	@Override
	public ArrayList<Object> getItems() {
		return resources;
	}
	
	@Override
	public void setItems(ArrayList<Object> items) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		resources = items;
	}
	
	public ResourceType getType() {
		return type;
	}
	
	public void setType(ResourceType type) {
		this.type = type;
		resourceTableModel.fireTableStructureChanged();
	}

	@Override
	public Object getSelected() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1)
			return null;
		return resources.get(selectedRow);
	}

	private class TableMouseAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			Point clickPoint = event.getPoint();
			int row = table.rowAtPoint(clickPoint);
			if (event.getClickCount() == 2) {
				Resource resource = (Resource) resources.get(row);
				ResourceViewer viewer = new ResourceViewer(resource);
				mainWindow.addTab(viewer.toString(), viewer);
			}
		}
	}

	private class ResourceTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			return 6;
		}

		@Override
		public int getRowCount() {
			if (resources == null)
				return 0;
			return resources.size();
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return "Resource ID";
			case 1:
				return type.getTitleLabel();
			case 2:
				return type.getCreatorLabel();
			case 3:
				return type.getCompanyLabel();
			case 4:
				return type.getSerialNumberLabel();
			case 5:
				return "Release Date";
			}
			return null;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (resources == null)
				return null;
			if (rowIndex < 0 || rowIndex >= resources.size())
				return null;
			if (columnIndex < 0 || columnIndex >= getColumnCount())
				return null;
			Resource resource = (Resource) resources.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return resource.getID();
			case 1:
				return resource.getTitle();
			case 2:
				return resource.getCreator();
			case 3:
				return resource.getCompany();
			case 4:
				return resource.getSerialNumber();
			case 5:
				return resource.getPublicationDate() == null ? ""
						: Utilities.formatDate(resource.getPublicationDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
			}
			return resource;
		}

	}

}
