package client.userinterface.datatypes.user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import client.control.data.UserManager;
import client.control.data.entity.user.Administrator;
import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.Librarian;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.Student;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;
import client.userinterface.mainwindow.MainWindow;

/**
 * Populates a tab with a user's information
 * @author Daniel
 * @version 2
 *
 */
public class UserInformationTab extends JPanel {

	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();

	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();

	// The user entity
	private User user;

	// Indicates that a new User entity is being created
	// This is set to true if the user parameter in the constructor is null
	private boolean newUser = false;
	
	// indicated whether this tab has been started
	private boolean started = false;

	// The input fields
	private JTextPane userIDTextPane;
	private JComboBox userTypeComboBox;
	private JCheckBox enabledCheckBox;
	private JTextField firstNameInputField;
	private JTextField lastNameInputField;
	private JTextField emailAddressInputField;
	private JPasswordField passwordInputField;

	/**
	 * Create the panel.
	 */
	public UserInformationTab(User user) {

		this.user = user;
		
	}
	
	/**
	 * start
	 */
	public void start() {
		
		if (started)
			return;
		started = true;
		
		// Check if the user parameter is null
		// If so, then a new User entity is being created
		if (this.user == null) {
			newUser = true;
			this.user = createDefaultUser();
		}

		// Create the GUI
		setBackground(Constants.BACKPANEL);
		setVisible(true);
		setLayout(null);
		createIDTextPane();
		createTypeInput();
		createFirstNameInput();
		createLastNameInput();
		createEmailAddressInput();
		createPasswordInput();
		createEnabledCheckBox();
		createSaveButton();

	}
	
	// Creates the id textpane
	private void createIDTextPane() {
		
		// Creating the text pane that says "User ID:"
		JTextPane userIDTextPaneLabel = new JTextPane();
		userIDTextPaneLabel.setBackground(Constants.BACKPANEL);
		userIDTextPaneLabel.setText("User ID:");
		userIDTextPaneLabel.setBounds(40, 30, 100, 20);
		userIDTextPaneLabel.setEditable(false);
		userIDTextPaneLabel.setFocusable(false);
		add(userIDTextPaneLabel);
		
		// Creating the actual ID text
		userIDTextPane = new JTextPane();
		userIDTextPane.setBackground(Constants.BACKPANEL);
		if (!newUser)
			userIDTextPane.setText(String.valueOf(user.getID()));
		userIDTextPane.setBounds(140, 30, 100, 20);
		userIDTextPane.setEditable(false);
		userIDTextPane.setFocusable(false);
		add(userIDTextPane);
		
	}
	
	// Creates the type input stuff
	private void createTypeInput() {
		
		// Creating the text pane that says "User Type:"
		JTextPane userTypeTextPane = new JTextPane();
		userTypeTextPane.setBackground(Constants.BACKPANEL);
		userTypeTextPane.setText("User Type:");
		userTypeTextPane.setBounds(40, 60, 100, 20);
		userTypeTextPane.setEditable(false);
		userTypeTextPane.setFocusable(false);
		add(userTypeTextPane);

		//Create the combo box for user types
		String[] userTypes = { "Student", "Faculty", "Librarian", "Administrator" };
		userTypeComboBox = new JComboBox(userTypes);
		userTypeComboBox.setBounds(140, 60, 180, 20);
		add(userTypeComboBox);
		userTypeComboBox.setEnabled(sessionManager.getUser() instanceof Administrator);
		
		if (user instanceof Student)
			userTypeComboBox.setSelectedIndex(0);
		if (user instanceof Faculty)
			userTypeComboBox.setSelectedIndex(1);
		if (user instanceof Librarian)
			userTypeComboBox.setSelectedIndex(2);
		if (user instanceof Administrator)
			userTypeComboBox.setSelectedIndex(3);
		
	}
	
	// Creates the first name input stuff
	private void createFirstNameInput() {
		
		// Creating the text pane that says "First Name:"
		JTextPane firstNameTextPane = new JTextPane();
		firstNameTextPane.setBackground(Constants.BACKPANEL);
		firstNameTextPane.setText("First Name:");
		firstNameTextPane.setBounds(40, 90, 100, 20);
		firstNameTextPane.setEditable(false);
		firstNameTextPane.setFocusable(false);
		add(firstNameTextPane);

		// Creating the input text field
		firstNameInputField = new JTextField();
		firstNameInputField.setBounds(140, 90, 180, 20);
		add(firstNameInputField);
		firstNameInputField.setColumns(8);
		
		// Setting the first name to the current value
		String firstName = user.getFirstName();
		firstNameInputField.setText(firstName);
		
	}
	
	// Creates the last name input stuff
	private void createLastNameInput() {
		
		// Creating the text pane that says "Last Name:"
		JTextPane lastNameTextPane = new JTextPane();
		lastNameTextPane.setBackground(Constants.BACKPANEL);
		lastNameTextPane.setText("Last Name:");
		lastNameTextPane.setBounds(40, 120, 100, 20);
		lastNameTextPane.setEditable(false);
		lastNameTextPane.setFocusable(false);
		add(lastNameTextPane);

		// Creating the input text field
		lastNameInputField = new JTextField();
		lastNameInputField.setBounds(140, 120, 180, 20);
		add(lastNameInputField);
		lastNameInputField.setColumns(8);
		
		// Setting the last name to the current value
		String lastName = user.getLastName();
		lastNameInputField.setText(lastName);
		
	}
	
	// Creates the email address input stuff
	private void createEmailAddressInput() {
		
		// Creating the text pane that says "Email Address:"
		JTextPane emailAddressTextPane = new JTextPane();
		emailAddressTextPane.setBackground(Constants.BACKPANEL);
		emailAddressTextPane.setText("Email Address:");
		emailAddressTextPane.setBounds(40, 150, 100, 20);
		emailAddressTextPane.setEditable(false);
		emailAddressTextPane.setFocusable(false);
		add(emailAddressTextPane);

		// Creating the input text field
		emailAddressInputField = new JTextField();
		emailAddressInputField.setBounds(140, 150, 180, 20);
		add(emailAddressInputField);
		emailAddressInputField.setColumns(8);
		
		// Setting the email address to the current value
		String emailAddress = user.getEmailAddress();
		emailAddressInputField.setText(emailAddress);
		
	}
	
	// Creates the password input stuff
	private void createPasswordInput() {
		
		// Creating the text pane that says "Password:"
		JTextPane passwordTextPane = new JTextPane();
		passwordTextPane.setBackground(Constants.BACKPANEL);
		passwordTextPane.setText("Password:");
		passwordTextPane.setBounds(40, 180, 100, 20);
		passwordTextPane.setEditable(false);
		passwordTextPane.setFocusable(false);
		add(passwordTextPane);

		// Creating the input text field
		passwordInputField = new JPasswordField();
		passwordInputField.setBounds(140, 180, 180, 20);
		add(passwordInputField);
		passwordInputField.setColumns(8);
		
		// Setting the email address to the current value
		String password = user.getPassword();
		passwordInputField.setText(password);
		
	}

	// creates the enabled check box
	private void createEnabledCheckBox() {
		
		JTextPane enabledTextPane = new JTextPane();
		enabledTextPane.setFocusable(false);
		enabledTextPane.setEditable(false);
		enabledTextPane.setText("Enabled:");
		enabledTextPane.setBackground(Constants.BACKPANEL);
		enabledTextPane.setBounds(40, 210, 100, 20);
		add(enabledTextPane);
		
		enabledCheckBox = new JCheckBox();
		enabledCheckBox.setSelected(user.getEnabled());
		enabledCheckBox.setEnabled(sessionManager.getUser() instanceof StaffMember);
		enabledCheckBox.setBackground(Constants.BACKPANEL);
		enabledCheckBox.setBounds(140, 210, 200, 20);
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
		button.setBounds(40, 240, 78, 30);
		add(button);
	}
	
	private void save() {
		
		// The index of this tab in the main window
		// When adding a new user, this tab is replaced by the normal viewer
		// with all the tabs
		int tabIndex = mainWindow.getTabIndex();
		
		// The information fields
		String userType = (String) userTypeComboBox.getSelectedItem();
		String firstName = firstNameInputField.getText();
		String lastName = lastNameInputField.getText();
		String emailAddress = emailAddressInputField.getText();
		String password = String.copyValueOf(passwordInputField.getPassword());
		boolean enabled = enabledCheckBox.isSelected();
		
		// Check if any of the fields are empty
		if ((userType == null || userType.length() <= 0)
				|| (firstName == null || firstName.length() <= 0)
				|| (lastName == null || lastName.length() <= 0)
				|| (emailAddress == null || emailAddress.length() <= 0)) {
			JOptionPane.showMessageDialog(mainWindow,
				    "None of the fields (except the password field) can be empty.",
				    "Save Error",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (password == null || password.length() == 0)
			password = null;
		
		User newUserEntity;
		if (userType.equalsIgnoreCase("Administrator"))
			newUserEntity = new Administrator(user.getID(), firstName, lastName, password, emailAddress, enabled);
		else if (userType.equalsIgnoreCase("Librarian"))
			newUserEntity = new Librarian(user.getID(), firstName, lastName, password, emailAddress, enabled);
		else if (userType.equalsIgnoreCase("Faculty"))
			newUserEntity = new Faculty(user.getID(), firstName, lastName, password, emailAddress, enabled);
		else
			newUserEntity = new Student(user.getID(), firstName, lastName, password, emailAddress, enabled);
		
		// Add the new user or modify the existing user
		if (newUser == true)
			newUserEntity = UserManager.addUser(newUserEntity);
		else
			if (!UserManager.setUser(newUserEntity))
				return;
		
		user = newUserEntity;
		
		// If adding a new user, replace the current tab in the main window
		// with the normal user viewer
		if (newUser) {
			UserViewer newViewer = new UserViewer(user);
			mainWindow.replaceTab(newViewer.toString(), newViewer, tabIndex);
		}
	}

	// Creates a default User entity
	private User createDefaultUser() {
		
		// Default ID
		int id = -1;
		
		// The default first name
		String firstName = "";

		// The default last name
		String lastName = "";

		// The default email address
		String emailAddress = "";

		// The default password
		String password = "";

		// The user is enabled by default
		boolean enabled = true;
		
		// Default is a new student
		User user = new Student(id, firstName, lastName, password,
				emailAddress, enabled);

		return user;

	}
	
	
	@Override
	public String toString() {
		return "User Information";
	}


}
