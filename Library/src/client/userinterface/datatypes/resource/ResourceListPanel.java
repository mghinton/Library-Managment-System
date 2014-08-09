package client.userinterface.datatypes.resource;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import client.control.data.ResourceManager;
import client.control.data.ResourceTypeManager;
import client.control.data.entity.resource.Resource;
import client.control.data.entity.resource.ResourceType;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a panel to hold a list of resources.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceListPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();
	
	// An array of resource types
	private ResourceType[] resourceTypes;
	
	// The search results
	private Resource[] searchResults;
	
	private JComboBox resourceTypeComboBox;
	private JCheckBox includeDisabledCheckBox;
	private JTextField resourceIDInputField;
	private JTextField titleInputField;
	private JTextField creatorInputField;
	private JTextField companyInputField;
	private JPanel searchPanel;
	private ResourceList list;
	private ErrorPanel errorPanel;
	private ErrorPanel searchErrorPanel;
	private JTextPane titleTextPane;
	private JTextPane creatorTextPane;
	private JTextPane companyTextPane;
	
	public ResourceListPanel() {
		super();
		
		// get necessary data from server
		if (getData())
			init(); // create GUI
		else
			displayError(true); // display error
	}
	
	// Creates the GUI
	private void init() {
		
		setBackground(Constants.BACKPANEL);
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		searchPanel = new JPanel();
		searchPanel.setBackground(Constants.BACKPANEL);
		add(searchPanel, BorderLayout.NORTH);
		createResourceTypeInput();
		createResourceIDInput();
		createTitleInput();
		createCreatorInput();
		createCompanyInput();
		createIncludeDisabledCheckBox();
		createSearchButton();
		createList();
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
	
	// Displays an error explaining that the search results could not
	// be retrieved
	private void displaySearchError(boolean visible) {
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		if (searchErrorPanel == null) {
			searchErrorPanel = new ErrorPanel();
			searchErrorPanel.addActionListenerToButton(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (getSearchResults()) {
						int selectedIndex = resourceTypeComboBox.getSelectedIndex();
						if (resourceTypes == null || selectedIndex >= resourceTypes.length)
							return;
						ResourceType resourceType = resourceTypes[selectedIndex];
						list.setType(resourceType);
						ArrayList<Object> resources = new ArrayList<Object>(Arrays.asList(searchResults));
						list.setItems(resources);
						displaySearchError(false);
					}
				}
			});
		}
		if (visible){
			if (this.isAncestorOf(list))
				remove(list);
			if (!this.isAncestorOf(searchErrorPanel))
				add(searchErrorPanel, BorderLayout.SOUTH);
		}
		else {
			if (this.isAncestorOf(searchErrorPanel))
				remove(searchErrorPanel);
			if (!this.isAncestorOf(list))
				add(list, BorderLayout.SOUTH);
		}
		
	}

	private void createList() {
		list = new ResourceList(resourceTypes[resourceTypeComboBox.getSelectedIndex()]);
		add(list, BorderLayout.SOUTH);
	}

	private void createSearchButton() {
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (getSearchResults()) {
					int selectedIndex = resourceTypeComboBox.getSelectedIndex();
					if (resourceTypes == null || selectedIndex >= resourceTypes.length)
						return;
					ResourceType resourceType = resourceTypes[selectedIndex];
					list.setType(resourceType);
					ArrayList<Object> resources = new ArrayList<Object>(Arrays.asList(searchResults));
					list.setItems(resources);
					displaySearchError(false);
				}
				else {
					displaySearchError(true);
				}
			}
		});
		searchPanel.add(searchButton);
	}

	private void createIncludeDisabledCheckBox() {
		JTextPane includeDisabledTextPane = new JTextPane();
		includeDisabledTextPane.setFocusable(false);
		includeDisabledTextPane.setEditable(false);
		includeDisabledTextPane.setText("Include Disabled:");
		includeDisabledTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(includeDisabledTextPane);
		
		includeDisabledCheckBox = new JCheckBox();
		includeDisabledCheckBox.setSelected(false);
		includeDisabledCheckBox.setBackground(Constants.BACKPANEL);
		searchPanel.add(includeDisabledCheckBox);
	}

	private void createCompanyInput() {
		companyTextPane = new JTextPane();
		companyTextPane.setFocusable(false);
		companyTextPane.setEditable(false);
		if (resourceTypes.length > 0) {
			int selectedIndex = resourceTypeComboBox.getSelectedIndex();
			ResourceType resourceType = resourceTypes[selectedIndex];
			companyTextPane.setText(resourceType.getCompanyLabel() + ":");
		}
		companyTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(companyTextPane);
		
		companyInputField = new JTextField();
		searchPanel.add(companyInputField);
		companyInputField.setColumns(10);
	}

	private void createCreatorInput() {
		creatorTextPane = new JTextPane();
		creatorTextPane.setFocusable(false);
		creatorTextPane.setEditable(false);
		if (resourceTypes.length > 0) {
			int selectedIndex = resourceTypeComboBox.getSelectedIndex();
			ResourceType resourceType = resourceTypes[selectedIndex];
			creatorTextPane.setText(resourceType.getCreatorLabel() + ":");
		}
		creatorTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(creatorTextPane);
		
		creatorInputField = new JTextField();
		searchPanel.add(creatorInputField);
		creatorInputField.setColumns(10);
	}

	private void createTitleInput() {		
		titleTextPane = new JTextPane();
		titleTextPane.setFocusable(false);
		titleTextPane.setEditable(false);
		if (resourceTypes.length > 0) {
			int selectedIndex = resourceTypeComboBox.getSelectedIndex();
			ResourceType resourceType = resourceTypes[selectedIndex];
			titleTextPane.setText(resourceType.getTitleLabel() + ":");
		}
		titleTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(titleTextPane);
		
		titleInputField = new JTextField();
		searchPanel.add(titleInputField);
		titleInputField.setColumns(10);
	}

	private void createResourceIDInput() {
		
		JTextPane resourceIDTextPane = new JTextPane();
		resourceIDTextPane.setFocusable(false);
		resourceIDTextPane.setEditable(false);
		resourceIDTextPane.setText("ID:");
		resourceIDTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(resourceIDTextPane);
		
		resourceIDInputField = new JTextField();
		searchPanel.add(resourceIDInputField);
		resourceIDInputField.setColumns(9);
	}
	
	private void createResourceTypeInput() {
		
		String[] resourceTypeNames = new String[resourceTypes.length];
		for (int i = 0; i < resourceTypes.length; i++)
			resourceTypeNames[i] = resourceTypes[i].getName();
		
		//Create the combo box for resource types
		resourceTypeComboBox = new JComboBox(resourceTypeNames);
		if (resourceTypes.length > 0)
			resourceTypeComboBox.setSelectedIndex(0);
		searchPanel.add(resourceTypeComboBox);
		
		resourceTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int selectedIndex = resourceTypeComboBox.getSelectedIndex();
				if (resourceTypes == null || selectedIndex >= resourceTypes.length)
					return;
				ResourceType resourceType = resourceTypes[selectedIndex];
				titleTextPane.setText(resourceType.getTitleLabel());
				creatorTextPane.setText(resourceType.getCreatorLabel() + ":");
				companyTextPane.setText(resourceType.getCompanyLabel() + ":");
			}
		});
		
	}
	
	// performs the search
	private boolean getSearchResults() {
		if (searchResults == null)
			searchResults = new Resource[0];
		// The search parameters
		int selectedIndex = resourceTypeComboBox.getSelectedIndex();
		if (resourceTypes == null || selectedIndex >= resourceTypes.length)
			return false;
		ResourceType resourceType = resourceTypes[selectedIndex];
		int resourceID = -1;
		if (resourceIDInputField.getText() != null && resourceIDInputField.getText().length() > 0) {
			try {
				resourceID = Integer.parseInt(resourceIDInputField.getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(mainWindow,
					    "The resource ID can only contain digits.",
					    "Invalid Resource ID",
					    JOptionPane.ERROR_MESSAGE);
				return true;
			}
		}
		String title = titleInputField.getText();
		String creator = creatorInputField.getText();
		String company = companyInputField.getText();
		
		Resource searchQuery = new Resource(resourceID, resourceType.getID(), title, creator, company, null, null, true);
		Resource[] resourceArrayEnabled = ResourceManager.getResource(searchQuery);
		if (resourceArrayEnabled == null)
			return false;
		if (includeDisabledCheckBox.isSelected()) {
			searchQuery = new Resource(resourceID, resourceType.getID(), title, creator, company, null, null, false);
			Resource[] resourceArrayDisabled = ResourceManager.getResource(searchQuery);
			if (resourceArrayDisabled == null)
				return false;
			searchResults = new Resource[resourceArrayEnabled.length + resourceArrayDisabled.length];
			int i;
			for (i = 0; i < resourceArrayEnabled.length; i++)
				searchResults[i] = resourceArrayEnabled[i];
			for (; i < searchResults.length; i++)
				searchResults[i] = resourceArrayDisabled[i - resourceArrayEnabled.length];
		}
		else
			searchResults = resourceArrayEnabled;
		return true;
	}
	
	// gets the required data from the server
	private boolean getData() {
		
		// get the resource types
		if (resourceTypes == null) {
			ResourceType searchKey = new ResourceType(-1, null, null, null, null, null, -1, -1, -1, -1, -1, true);
			resourceTypes = ResourceTypeManager.getResourceType(searchKey);
		}
		
		return !(resourceTypes == null);
		
	}
	
	@Override
	public String toString() {
		return "Resource Search";
	}
	

}
