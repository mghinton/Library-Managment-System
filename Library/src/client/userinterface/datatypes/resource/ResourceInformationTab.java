package client.userinterface.datatypes.resource;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import util.Utilities;
import client.control.data.ReserveManager;
import client.control.data.ResourceManager;
import client.control.data.ResourceTypeManager;
import client.control.data.entity.resource.Reserve;
import client.control.data.entity.resource.Resource;
import client.control.data.entity.resource.ResourceType;
import client.control.data.entity.user.Patron;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a tab to view the information of a particular resource.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceInformationTab extends JPanel {

	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();

	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();

	// The resource entity
	private Resource resource;
	
	// Indicates that a new Resource entity is being created
	// This is set to true if the resource parameter in the constructor is null
	private boolean newResource = false;
	
	// indicated whether this tab has been started
	private boolean started = false;
	
	// An array of resource types
	private ResourceType[] resourceTypes;
	
	// The input fields
	private JTextPane resourceIDTextPane;
	private JComboBox resourceTypeComboBox;
	private JTextField titleInputField;
	private JTextPane titleTextPane;
	private JTextField creatorInputField;
	private JTextPane creatorTextPane;
	private JTextField companyInputField;
	private JTextPane companyTextPane;
	private JTextField serialNumberInputField;
	private JTextPane serialNumberTextPane;
	private JTextField publicationDateInputField;
	private JTextPane publicationDateTextPane;
	private ErrorPanel errorPanel;
	private JCheckBox enabledCheckBox;

	/**
	 * Create the panel.
	 */
	public ResourceInformationTab(Resource resource) {

		this.resource = resource;
		
	}
	
	/**
	 * start
	 */
	public void start() {
		
		if (started)
			return;
		started = true;
		
		// Check if the resource parameter is null
		// If so, then a new Resource entity is being created
		if (this.resource == null) {
			newResource = true;
			this.resource = createDefaultResource();
		}
	
		// get necessary data from server
		if (getData())
			init();
		else
			displayError(true);
		
	}
	
	// Creates the GUI
	private void init() {
		
		setBackground(Constants.BACKPANEL);
		setVisible(true);
		setLayout(null);
		createIDTextPane();
		createTypeInput();
		createTitleInput();
		createCreatorInput();
		createCompanyInput();
		createSerialNumberInput();
		createPublicationDateInput();
		createEnabledCheckBox();
		createSaveButton();
		createReserveButton();

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
	
	// Creates the id textpane
	private void createIDTextPane() {
		
		// Creating the text pane that says "User ID:"
		JTextPane resourceIDTextPaneLabel = new JTextPane();
		resourceIDTextPaneLabel.setBackground(Constants.BACKPANEL);
		resourceIDTextPaneLabel.setText("Resource ID:");
		resourceIDTextPaneLabel.setBounds(40, 30, 100, 20);
		resourceIDTextPaneLabel.setEditable(false);
		resourceIDTextPaneLabel.setFocusable(false);
		add(resourceIDTextPaneLabel);
		
		// Creating the actual ID text
		resourceIDTextPane = new JTextPane();
		resourceIDTextPane.setBackground(Constants.BACKPANEL);
		if (!newResource)
			resourceIDTextPane.setText(String.valueOf(resource.getID()));
		resourceIDTextPane.setBounds(140, 30, 100, 20);
		resourceIDTextPane.setEditable(false);
		resourceIDTextPane.setFocusable(false);
		add(resourceIDTextPane);
		
	}
	
	// Creates the type input stuff
	private void createTypeInput() {
		
		// Creating the text pane that says "Resource Type:"
		JTextPane resourceTypeTextPane = new JTextPane();
		resourceTypeTextPane.setBackground(Constants.BACKPANEL);
		resourceTypeTextPane.setText("Resource Type:");
		resourceTypeTextPane.setBounds(40, 60, 100, 20);
		resourceTypeTextPane.setEditable(false);
		resourceTypeTextPane.setFocusable(false);
		add(resourceTypeTextPane);
		
		String[] resourceTypeNames = new String[resourceTypes.length];
		for (int i = 0; i < resourceTypes.length; i++)
			resourceTypeNames[i] = resourceTypes[i].getName();
		
		int currentResourceType = 0;
		for (int i = 0; i < resourceTypes.length; i++) {
			if (resourceTypes[i].getID() == resource.getResourceType()) {
				currentResourceType = i;
				break;
			}
		}

		//Create the combo box for resource types
		resourceTypeComboBox = new JComboBox(resourceTypeNames);
		resourceTypeComboBox.setBounds(140, 60, 180, 20);
		resourceTypeComboBox.setSelectedIndex(currentResourceType);
		add(resourceTypeComboBox);
		resourceTypeComboBox.setEnabled(sessionManager.getUser() instanceof StaffMember);
		resourceTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				titleTextPane.setText(resourceTypes[resourceTypeComboBox.getSelectedIndex()].getTitleLabel());
				creatorTextPane.setText(resourceTypes[resourceTypeComboBox.getSelectedIndex()].getCreatorLabel());
				companyTextPane.setText(resourceTypes[resourceTypeComboBox.getSelectedIndex()].getCompanyLabel());
				serialNumberTextPane.setText(resourceTypes[resourceTypeComboBox.getSelectedIndex()].getSerialNumberLabel());
			}
		});
		
	}
	
	// Creates the title input stuff
	private void createTitleInput() {
		
		titleTextPane = new JTextPane();
		titleTextPane.setBackground(Constants.BACKPANEL);
		titleTextPane.setText(resourceTypes[resourceTypeComboBox.getSelectedIndex()].getTitleLabel());
		titleTextPane.setBounds(40, 90, 100, 20);
		titleTextPane.setEditable(false);
		titleTextPane.setFocusable(false);
		add(titleTextPane);

		// Creating the input text field
		titleInputField = new JTextField();
		titleInputField.setBounds(140, 90, 180, 20);
		add(titleInputField);
		titleInputField.setColumns(8);
		titleInputField.setEditable(sessionManager.getUser() instanceof StaffMember);
		
		// Setting the textfield to the current value
		String title = resource.getTitle();
		titleInputField.setText(title);
		
	}
	
	// Creates the creator input stuff
	private void createCreatorInput() {
		
		creatorTextPane = new JTextPane();
		creatorTextPane.setBackground(Constants.BACKPANEL);
		creatorTextPane.setText(resourceTypes[resourceTypeComboBox.getSelectedIndex()].getCreatorLabel());
		creatorTextPane.setBounds(40, 120, 100, 20);
		creatorTextPane.setEditable(false);
		creatorTextPane.setFocusable(false);
		add(creatorTextPane);

		// Creating the input text field
		creatorInputField = new JTextField();
		creatorInputField.setBounds(140, 120, 180, 20);
		add(creatorInputField);
		creatorInputField.setColumns(8);
		creatorInputField.setEditable(sessionManager.getUser() instanceof StaffMember);

		// Setting the textfield to the current value
		String creator = resource.getCreator();
		creatorInputField.setText(creator);
		
	}
	
	// Creates the company input stuff
	private void createCompanyInput() {
		
		companyTextPane = new JTextPane();
		companyTextPane.setBackground(Constants.BACKPANEL);
		companyTextPane.setText(resourceTypes[resourceTypeComboBox.getSelectedIndex()].getCompanyLabel());
		companyTextPane.setBounds(40, 150, 100, 20);
		companyTextPane.setEditable(false);
		companyTextPane.setFocusable(false);
		add(companyTextPane);

		// Creating the input text field
		companyInputField = new JTextField();
		companyInputField.setBounds(140, 150, 180, 20);
		add(companyInputField);
		companyInputField.setColumns(8);
		companyInputField.setEditable(sessionManager.getUser() instanceof StaffMember);

		// Setting the textfield to the current value
		String company = resource.getCompany();
		companyInputField.setText(company);
		
	}
	
	// Creates the serial number input stuff
	private void createSerialNumberInput() {
		
		serialNumberTextPane = new JTextPane();
		serialNumberTextPane.setBackground(Constants.BACKPANEL);
		serialNumberTextPane.setText(resourceTypes[resourceTypeComboBox.getSelectedIndex()].getSerialNumberLabel());
		serialNumberTextPane.setBounds(40, 180, 100, 20);
		serialNumberTextPane.setEditable(false);
		serialNumberTextPane.setFocusable(false);
		add(serialNumberTextPane);

		// Creating the input text field
		serialNumberInputField = new JTextField();
		serialNumberInputField.setBounds(140, 180, 180, 20);
		add(serialNumberInputField);
		serialNumberInputField.setColumns(8);
		serialNumberInputField.setEditable(sessionManager.getUser() instanceof StaffMember);

		// Setting the textfield to the current value
		String serialNumber = resource.getSerialNumber();
		serialNumberInputField.setText(serialNumber);
		
	}
	
	// Creates the publication date input stuff
	private void createPublicationDateInput() {
		
		publicationDateTextPane = new JTextPane();
		publicationDateTextPane.setBackground(Constants.BACKPANEL);
		publicationDateTextPane.setText("Publication Date:");
		publicationDateTextPane.setBounds(40, 210, 100, 20);
		publicationDateTextPane.setEditable(false);
		publicationDateTextPane.setFocusable(false);
		add(publicationDateTextPane);

		// Creating the input text field
		publicationDateInputField = new JTextField();
		publicationDateInputField.setBounds(140, 210, 180, 20);
		add(publicationDateInputField);
		publicationDateInputField.setColumns(8);
		publicationDateInputField.setEditable(sessionManager.getUser() instanceof StaffMember);

		JTextPane publicationDateTextPane2 = new JTextPane();
		publicationDateTextPane2.setBackground(Constants.BACKPANEL);
		publicationDateTextPane2.setText("(" + Constants.DEFAULT_DATE_FORMAT.toUpperCase() + ")");
		publicationDateTextPane2.setBounds(330, 210, 100, 20);
		publicationDateTextPane2.setEditable(false);
		publicationDateTextPane2.setFocusable(false);
		add(publicationDateTextPane2);
		
		// Setting the textfield to the current value
		String date = Utilities.formatDate(resource.getPublicationDate(),
				Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
		publicationDateInputField.setText(date);
		
	}

	// creates the enabled check box
	private void createEnabledCheckBox() {
		
		JTextPane enabledTextPane = new JTextPane();
		enabledTextPane.setFocusable(false);
		enabledTextPane.setEditable(false);
		enabledTextPane.setText("Enabled:");
		enabledTextPane.setBackground(Constants.BACKPANEL);
		enabledTextPane.setBounds(40, 240, 100, 20);
		add(enabledTextPane);
		
		enabledCheckBox = new JCheckBox();
		enabledCheckBox.setSelected(resource.getEnabled());
		enabledCheckBox.setEnabled(sessionManager.getUser() instanceof StaffMember);
		enabledCheckBox.setBounds(140, 240, 200, 20);
		enabledCheckBox.setBackground(Constants.BACKPANEL);
		add(enabledCheckBox);
		
	}

	// Creates the Save button
	private void createSaveButton() {
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				save();
			}
		});
		button.setBounds(40, 270, 100, 30);
		button.setEnabled(sessionManager.getUser() instanceof StaffMember);
		add(button);
	}

	// Creates the Save button
	private void createReserveButton() {
		JButton button = new JButton("Reserve");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				reserve();
			}
		});
		button.setBounds(40, 310, 100, 30);
		button.setEnabled(!newResource && (sessionManager.getUser() instanceof Patron
				|| sessionManager.getUser() instanceof StaffMember));
		add(button);
	}
	
	// adds a new resource or saves the existing resource
	private void save() {
		
		// The index of this tab in the main window
		// When adding a new resource, this tab is replaced by the normal viewer
		// with all the tabs
		int tabIndex = mainWindow.getTabIndex();
		
		// The information fields
		ResourceType resourceType = resourceTypes[resourceTypeComboBox.getSelectedIndex()];
		String title = titleInputField.getText();
		String creator = creatorInputField.getText();
		String company = companyInputField.getText();
		String serialNumber = serialNumberInputField.getText();
		String publicationDate = publicationDateInputField.getText();
		boolean enabled = enabledCheckBox.isSelected();
		
		// Check if any of the fields are empty
		if ((title == null || title.length() <= 0)
				|| (creator == null || creator.length() <= 0)
				|| (company == null || company.length() <= 0)
				|| (serialNumber == null || serialNumber.length() <= 0)
				|| (publicationDate == null || publicationDate.length() <= 0)) {
			JOptionPane.showMessageDialog(mainWindow,
				    "None of the fields can be empty.",
				    "Save Error",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Date publication = resource.getPublicationDate();
		try {
			publication = Utilities.parseDate(publicationDate,
					Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainWindow,
				    "The date you entered is not valid.",
				    "Invalid Date",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// The new resource entity
		Resource newResourceEntity = new Resource(resource.getID(),
				resourceType.getID(), title, creator, company,
				serialNumber, publication, enabled);
		
		// if adding a new resource
		if (newResource) {
			newResourceEntity = ResourceManager.addResource(newResourceEntity);
			// check if an error occurred
			if (newResourceEntity == null)
				return;
			// if a new resource is created, replace the existing viewer with the
			// normal viewer for modifying a resource
			ResourceViewer newViewer = new ResourceViewer(resource);
			mainWindow.replaceTab(newViewer.toString(), newViewer, tabIndex);
		}
		
		// otherwise, modifying an existing resource
		if (ResourceManager.setResource(resource))
			resource = newResourceEntity;
		else
			return; // an error occurred
			
	}
	
	// Reserves the resource for the logged in user
	private void reserve() {
		User user = sessionManager.getUser();
		if (user == null)
			return;
		Reserve reserve = new Reserve(-1, user.getID(), resource.getID(), Utilities.removeTime(new Date()), null, null);
		ReserveManager.addReserve(reserve);
	}

	// Creates a default Resource entity
	private Resource createDefaultResource() {
		
		resource = new Resource();
		
		// The default id
		int id = -1;
		
		// The default type
		ResourceType searchKey = new ResourceType(-1, "Book", null, null, null, null, -1, -1, -1, -1, -1, true);
		ResourceType[] results = ResourceTypeManager.getResourceType(searchKey);
		ResourceType type;
		if (results != null && results.length > 0)
			type = results[0];
		else
			type = new ResourceType(-1, null, null, null, null, null, -1, -1, -1, -1, -1, true);

		// The default title
		String title = "";

		// The default creator
		String creator = "";

		// The default company
		String company = "";

		// The default serial number
		String serialNumber = "";
		
		// The default publication date
		Date publication = Utilities.removeTime(new Date());
		
		// By default, it is enabled
		boolean enabled = true;
		
		Resource newResourceEntity = new Resource(id, type.getID(), title, creator,
				company, serialNumber, publication, enabled);

		return newResourceEntity;

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
		return "Resource Information";
	}

}
