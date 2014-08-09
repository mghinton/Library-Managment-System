package client.userinterface.datatypes.resourcetype;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import util.Constants;
import client.control.data.ResourceTypeManager;
import client.control.data.ToDoManager;
import client.control.data.entity.resource.ResourceType;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a list to hold any number of resource types.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceTypeListPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();;
	
	// The resourceType array
	private ResourceType[] resourceTypeArray;
	
	private ErrorPanel errorPanel;
	
	private JButton addButton;
	private JButton removeButton;
	private JPanel buttonPanel;
	private ResourceTypeList list = null;
	
	public ResourceTypeListPanel() {
		super();
		
		// get necessary data from server
		if (getData())
			init();
		else
			displayError(true);
	}
	
	// Create the GUI
	private void init() {
		setBackground(Constants.BACKPANEL);
		setVisible(true);
		setBackground(Constants.BACKPANEL);
		setLayout(new BorderLayout(0, 0));
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		createButtonPanel();
		createList();
	}
	
	// Gets the required data from the server
	private boolean getData() {
		
		if (resourceTypeArray == null) {
			ResourceType searchKey = new ResourceType(-1, null, null, null, null, null, -1, -1, -1, -1, -1, true);
			ResourceType[] resourceTypeArrayEnabled = ResourceTypeManager.getResourceType(searchKey);
			if (resourceTypeArrayEnabled == null)
				return false;
			searchKey = new ResourceType(-1, null, null, null, null, null, -1, -1, -1, -1, -1, false);
			ResourceType[] resourceTypeArrayDisabled = ResourceTypeManager.getResourceType(searchKey);
			if (resourceTypeArrayDisabled == null)
				return false;
			resourceTypeArray = new ResourceType[resourceTypeArrayEnabled.length + resourceTypeArrayDisabled.length];
			int i;
			for (i = 0; i < resourceTypeArrayEnabled.length; i++)
				resourceTypeArray[i] = resourceTypeArrayEnabled[i];
			for (; i < resourceTypeArray.length; i++)
				resourceTypeArray[i] = resourceTypeArrayDisabled[i - resourceTypeArrayEnabled.length];
		}
		
		return true;
		
	}
	
	// Displays an error explaining that the necessary data could not
	// be retrieved
	private void displayError(boolean visible) {
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		if (errorPanel == null) {
			errorPanel = new ErrorPanel();
			errorPanel.addActionListenerToButton(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (getData()) {
						displayError(false);
						init();
					}
				}
			});
		}
		if (visible){
			if (!this.isAncestorOf(errorPanel))
				add(errorPanel);
		}
		else {
			if (this.isAncestorOf(errorPanel))
				remove(errorPanel);
		}
		
	}

	private void createList() {
		list = new ResourceTypeList();
		list.setItems(new ArrayList<Object>(Arrays.asList(resourceTypeArray)));
		add(list, BorderLayout.SOUTH);
	}
	
	// Creates the button panel
	private void createButtonPanel() {
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(1, 11, 0, 0));
		buttonPanel.setBackground(Constants.BACKPANEL);
		
		createAddButton();
		createRemoveButton();
	}

	// Creates the add button
	private void createAddButton() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Constants.BACKPANEL);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				addItem();
			}
		});
		panel.add(addButton);
		buttonPanel.add(panel);
	}

	// Creates the remove button
	private void createRemoveButton() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Constants.BACKPANEL);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ResourceType selectedResourceType = (ResourceType) list.getSelected();
				if (selectedResourceType == null) {
					JOptionPane.showMessageDialog(mainWindow,
						    "Please select a resourceType from the list before clicking remove",
						    "No ResourceType Selected",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				removeItem(selectedResourceType);
			}
		});
		panel.add(removeButton);
		buttonPanel.add(panel);
	}
	
	public void addItem()
	{
		ResourceTypeViewer viewer = new ResourceTypeViewer(null);
		MainWindow main = MainWindow.getReference();
		main.addTab(viewer.toString(), viewer);
	}
	
	public void removeItem(ResourceType resourceType)
	{
		ArrayList<Object> resourceTypes = list.getItems();
		if (!ToDoManager.removeToDo(resourceType.getID())) {
			JOptionPane.showMessageDialog(mainWindow,
				    "Error",
				    "Unable to remove resourceType at this time",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		resourceTypes.remove(resourceType);
		list.setItems(resourceTypes);
	}
	
	@Override
	public String toString() {
		return "Resource Type List";
	}
}
