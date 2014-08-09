package client.userinterface.datatypes.resourcetype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import client.control.data.ResourceTypeManager;
import client.control.data.entity.resource.ResourceType;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a viewer to view the information about a particular resource type.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceTypeViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();;
	
	// The ResourceType entity
	private ResourceType resourceType = null;
	
	// Allows the user to edit sections
	private JTextPane resourceTypeIDTextPane;
	private JTextField nameInputField = null;
	private JTextField titleInputField = null;
	private JTextField creatorInputField = null;
	private JTextField companyInputField = null;
	private JTextField serialNumberInputField = null;
	private JTextField staffPeriodInputField = null;
	private JTextField facultyPeriodInputField = null;
	private JTextField studentPeriodInputField = null;
	private JTextField maxFineInputField = null;
	private JTextField fineIncrementInputField = null;
	private JCheckBox enabledCheckBox = null;
	
	// Indicates that a new ResourceType entity is being created
	// This is set to true if the ResourceType parameter in the constructor is null
	private boolean newResourceType = false;
	
	
	/**
	 * Construct a new ResourceTypeViewer.
	 * 
	 * @param todo
	 * 		The ResourceType entity to view/modify, or null if a new ToDo is to be
	 * 		created.
	 */
	public ResourceTypeViewer(ResourceType resourceType) {
		
		this.resourceType = resourceType;
		
		// Check if the ResourceType parameter is null
		// If so, then a new ResourceType entity is being created
		if (this.resourceType == null) {
			newResourceType = true;
			this.resourceType = createDefaultResourceType();
		}
		
		// Create the GUI
		setVisible(true);
		setLayout(null);
		setBackground(Constants.BACKPANEL);
		createIDTextPane();
		createNameInput();
		createTitleInput();
		createCreatorInput();
		createCompanyInput();
		createSerialNumberInput();
		createStaffPeriodInput();
		createFacultyPeriodInput();
		createStudentPeriodInput();
		createMaxFineInput();
		createFineIncrementInput();
		createEnabledInput();
		createSaveButton();
		
	}
	
	// Creates the id textpane
	private void createIDTextPane() {
		
		// Creating the text pane that says "Resource Type ID:"
		JTextPane resourceTypeIDTextPaneLabel = new JTextPane();
		resourceTypeIDTextPaneLabel.setBackground(Constants.BACKPANEL);
		resourceTypeIDTextPaneLabel.setText("Type ID:");
		resourceTypeIDTextPaneLabel.setBounds(40, 30, 100, 20);
		resourceTypeIDTextPaneLabel.setEditable(false);
		resourceTypeIDTextPaneLabel.setFocusable(false);
		add(resourceTypeIDTextPaneLabel);
		
		// Creating the actual ID text
		resourceTypeIDTextPane = new JTextPane();
		resourceTypeIDTextPane.setBackground(Constants.BACKPANEL);
		if (!newResourceType)
			resourceTypeIDTextPane.setText(String.valueOf(resourceType.getID()));
		resourceTypeIDTextPane.setBounds(180, 30, 100, 20);
		resourceTypeIDTextPane.setEditable(false);
		resourceTypeIDTextPane.setFocusable(false);
		add(resourceTypeIDTextPane);
		
	}
	
	private void createNameInput() {
		JTextPane nameTextPane = new JTextPane();
		nameTextPane.setBackground(Constants.BACKPANEL);
		nameTextPane.setText("Name:");
		nameTextPane.setBounds(40, 60, 110, 20);
		nameTextPane.setEditable(false);
		nameTextPane.setBackground(Constants.BACKPANEL);
		add(nameTextPane);
		
		nameInputField = new JTextField();
		nameInputField.setText(resourceType.getName());
		nameInputField.setBounds(180, 60, 180, 20);
		add(nameInputField);
		nameInputField.setColumns(8);
		
	}
	
	private void createTitleInput() {
		JTextPane titleTextPane = new JTextPane();
		titleTextPane.setBackground(Constants.BACKPANEL);
		titleTextPane.setText("Title Label:");
		titleTextPane.setBounds(40, 90, 110, 20);
		titleTextPane.setEditable(false);
		titleTextPane.setBackground(Constants.BACKPANEL);
		add(titleTextPane);
		
		titleInputField = new JTextField();
		titleInputField.setText(resourceType.getTitleLabel());
		titleInputField.setBounds(180, 90, 180, 20);
		add(titleInputField);
		titleInputField.setColumns(8);
		
	}
	
	private void createCreatorInput() {
		JTextPane creatorTextPane = new JTextPane();
		creatorTextPane.setBackground(Constants.BACKPANEL);
		creatorTextPane.setText("Creator Label:");
		creatorTextPane.setBounds(40, 120, 110, 20);
		creatorTextPane.setEditable(false);
		creatorTextPane.setBackground(Constants.BACKPANEL);
		add(creatorTextPane);
		
		creatorInputField = new JTextField();
		creatorInputField.setText(resourceType.getCreatorLabel());
		creatorInputField.setBounds(180, 120, 180, 20);
		add(creatorInputField);
		creatorInputField.setColumns(8);
		
	}
	
	private void createCompanyInput() {
		JTextPane companyTextPane = new JTextPane();
		companyTextPane.setBackground(Constants.BACKPANEL);
		companyTextPane.setText("Company Label:");
		companyTextPane.setBounds(40, 150, 110, 20);
		companyTextPane.setEditable(false);
		companyTextPane.setBackground(Constants.BACKPANEL);
		add(companyTextPane);
		
		companyInputField = new JTextField();
		companyInputField.setText(resourceType.getCompanyLabel());
		companyInputField.setBounds(180, 150, 180, 20);
		add(companyInputField);
		companyInputField.setColumns(8);
		
	}
	
	private void createSerialNumberInput() {
		JTextPane serialNumberTextPane = new JTextPane();
		serialNumberTextPane.setBackground(Constants.BACKPANEL);
		serialNumberTextPane.setText("Serial Number Label:");
		serialNumberTextPane.setBounds(40, 180, 110, 20);
		serialNumberTextPane.setEditable(false);
		serialNumberTextPane.setBackground(Constants.BACKPANEL);
		add(serialNumberTextPane);
		
		serialNumberInputField = new JTextField();
		serialNumberInputField.setText(resourceType.getSerialNumberLabel());
		serialNumberInputField.setBounds(180, 180, 180, 20);
		add(serialNumberInputField);
		serialNumberInputField.setColumns(8);
		
	}
	
	private void createStaffPeriodInput() {
		JTextPane staffPeriodTextPane = new JTextPane();
		staffPeriodTextPane.setBackground(Constants.BACKPANEL);
		staffPeriodTextPane.setText("Staff Period:");
		staffPeriodTextPane.setBounds(40, 210, 110, 20);
		staffPeriodTextPane.setEditable(false);
		staffPeriodTextPane.setBackground(Constants.BACKPANEL);
		add(staffPeriodTextPane);
		
		staffPeriodInputField = new JTextField();
		staffPeriodInputField.setText(String.valueOf(resourceType.getStaffPeriod()));
		staffPeriodInputField.setBounds(180, 210, 180, 20);
		add(staffPeriodInputField);
		staffPeriodInputField.setColumns(8);
		
	}
	
	private void createFacultyPeriodInput() {
		JTextPane facultyPeriodTextPane = new JTextPane();
		facultyPeriodTextPane.setBackground(Constants.BACKPANEL);
		facultyPeriodTextPane.setText("Faculty Period:");
		facultyPeriodTextPane.setBounds(40, 240, 110, 20);
		facultyPeriodTextPane.setEditable(false);
		facultyPeriodTextPane.setBackground(Constants.BACKPANEL);
		add(facultyPeriodTextPane);
		
		facultyPeriodInputField = new JTextField();
		facultyPeriodInputField.setText(String.valueOf(resourceType.getFacultyPeriod()));
		facultyPeriodInputField.setBounds(180, 240, 180, 20);
		add(facultyPeriodInputField);
		facultyPeriodInputField.setColumns(8);
		
	}
	
	private void createStudentPeriodInput() {
		JTextPane studentPeriodTextPane = new JTextPane();
		studentPeriodTextPane.setBackground(Constants.BACKPANEL);
		studentPeriodTextPane.setText("Student Period:");
		studentPeriodTextPane.setBounds(40, 270, 110, 20);
		studentPeriodTextPane.setEditable(false);
		studentPeriodTextPane.setBackground(Constants.BACKPANEL);
		add(studentPeriodTextPane);
		
		studentPeriodInputField = new JTextField();
		studentPeriodInputField.setText(String.valueOf(resourceType.getStudentPeriod()));
		studentPeriodInputField.setBounds(180, 270, 180, 20);
		add(studentPeriodInputField);
		studentPeriodInputField.setColumns(8);
		
	}
	
	private void createMaxFineInput() {
		JTextPane maxFineTextPane = new JTextPane();
		maxFineTextPane.setBackground(Constants.BACKPANEL);
		maxFineTextPane.setText("Maximum Fine:");
		maxFineTextPane.setBounds(40, 300, 110, 20);
		maxFineTextPane.setEditable(false);
		maxFineTextPane.setBackground(Constants.BACKPANEL);
		add(maxFineTextPane);
		
		maxFineInputField = new JTextField();
		maxFineInputField.setText(String.valueOf(resourceType.getMaxFineAmount()));
		maxFineInputField.setBounds(180, 300, 180, 20);
		add(maxFineInputField);
		maxFineInputField.setColumns(8);
		
	}
	
	private void createFineIncrementInput() {
		JTextPane fineIncrementTextPane = new JTextPane();
		fineIncrementTextPane.setBackground(Constants.BACKPANEL);
		fineIncrementTextPane.setText("Fine Increment:");
		fineIncrementTextPane.setBounds(40, 330, 110, 20);
		fineIncrementTextPane.setEditable(false);
		fineIncrementTextPane.setBackground(Constants.BACKPANEL);
		add(fineIncrementTextPane);
		
		fineIncrementInputField = new JTextField();
		fineIncrementInputField.setText(String.valueOf(resourceType.getFineIncrementAmount()));
		fineIncrementInputField.setBounds(180, 330, 180, 20);
		add(fineIncrementInputField);
		fineIncrementInputField.setColumns(8);
		
	}
	
	private void createEnabledInput() {
		JTextPane enabledTextPane = new JTextPane();
		enabledTextPane.setBackground(Constants.BACKPANEL);
		enabledTextPane.setText("Enabled:");
		enabledTextPane.setBounds(40, 360, 110, 20);
		enabledTextPane.setEditable(false);
		enabledTextPane.setBackground(Constants.BACKPANEL);
		add(enabledTextPane);
		
		enabledCheckBox = new JCheckBox();
		enabledCheckBox.setSelected(resourceType.getEnabled());
		enabledCheckBox.setBounds(180, 360, 20, 20);
		enabledCheckBox.setBackground(Constants.BACKPANEL);
		add(enabledCheckBox);
		
	}

	// Creates the Save button
	private void createSaveButton() {
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
					
				try {
					// The information fields
					String name = (String) nameInputField.getText();
					String title = (String) titleInputField.getText();
					String creator = (String) creatorInputField.getText();
					String company = (String) companyInputField.getText();
					String serialNumber = (String) serialNumberInputField.getText();
					
					int staffPeriod = Integer.parseInt(staffPeriodInputField.getText());
					int facultyPeriod = Integer.parseInt(facultyPeriodInputField.getText());
					int studentPeriod = Integer.parseInt(studentPeriodInputField.getText());
					float maxFine = Float.parseFloat(maxFineInputField.getText());
					float fineIncrement = Float.parseFloat(fineIncrementInputField.getText());
					boolean enabled = enabledCheckBox.isSelected();
					
					// Check if any of the fields are empty
					if ((name == null || name.length() <= 0)
							&& (title == null || title.length() <= 0 )
							&& (creator == null || creator.length() <= 0)
							&& (company == null || company.length() <= 0)
							&& (serialNumber == null || serialNumber.length() <= 0)) {
						JOptionPane.showMessageDialog(mainWindow,
							    "None of the fields can be empty.",
							    "Save Error",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if (staffPeriod <= 0 || facultyPeriod <= 0 || studentPeriod <= 0) {
						JOptionPane.showMessageDialog(mainWindow,
							    "Each of the loan periods must be greater than 0.",
							    "Save Error",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
						
					// send the request
					
					if (newResourceType) {
						ResourceType newResourceTypeEntity = new ResourceType(
								-1, name, title, creator, company,
								serialNumber, staffPeriod, facultyPeriod,
								studentPeriod, maxFine, fineIncrement, enabled);
						newResourceTypeEntity = ResourceTypeManager.addResourceType(newResourceTypeEntity);
						if (newResourceTypeEntity == null)
							return;
						resourceType = newResourceTypeEntity;
					}
					else {
						ResourceType newResourceTypeEntity = new ResourceType(
								resourceType.getID(), name, title, creator, company,
								serialNumber, staffPeriod, facultyPeriod,
								studentPeriod, maxFine, fineIncrement, enabled);
						if (ResourceTypeManager.setResourceType(newResourceTypeEntity))
							return;
						resourceType = newResourceTypeEntity;
					}
						
					// update fields
					resourceTypeIDTextPane.setText(String.valueOf(resourceType.getID()));
					nameInputField.setText(resourceType.getName());
					titleInputField.setText(resourceType.getTitleLabel());
					creatorInputField.setText(resourceType.getCreatorLabel());
					companyInputField.setText(resourceType.getCompanyLabel());
					serialNumberInputField.setText(resourceType.getSerialNumberLabel());
					staffPeriodInputField.setText(String.valueOf(resourceType.getStaffPeriod()));
					facultyPeriodInputField.setText(String.valueOf(resourceType.getFacultyPeriod()));
					studentPeriodInputField.setText(String.valueOf(resourceType.getStudentPeriod()));
					maxFineInputField.setText(String.valueOf(resourceType.getMaxFineAmount()));
					fineIncrementInputField.setText(String.valueOf(resourceType.getFineIncrementAmount()));
					enabledCheckBox.setSelected(resourceType.getEnabled());
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainWindow,
	                        "Only digits allowed for student, faculty, and staff loan periods.",
	                        "Error",
	                        JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button.setBounds(40, 390, 100, 30);
		add(button);
	}
	
	// Creates a default ResourceType entity
	private ResourceType createDefaultResourceType() {
		
		// The default name
		String name = "New Resource Type";
		
		// The default title label
		String title = "";
		
		// The default creator label
		String creator = "";
		
		// The default company label
		String company = "";
		
		// The default serial number label
		String serialNumber = "";
		
		// The default staff loan period
		int staffPeriod = 0;
		
		// The default faculty loan period
		int facultyPeriod = 0;
		
		// The default student loan period
		int studentPeriod = 0;
		
		// The default maximum fine
		float maxFine = 0;
		
		// The default fine increment
		float fineIncrement = 0;
		
		// Enabled by default
		boolean enabled = true;
		
		ResourceType newResourceType = new ResourceType(0, name, title,
				creator, company, serialNumber, staffPeriod, facultyPeriod,
				studentPeriod, maxFine, fineIncrement, enabled);
		
		return newResourceType;
		
	}
	
	@Override
	public String toString() {
		if (newResourceType)
			return "New Resource Type";
		return resourceType.getName();
	}
	
}
