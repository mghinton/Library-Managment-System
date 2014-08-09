package client.userinterface.datatypes.user;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import client.control.data.UserManager;
import client.control.data.entity.user.Administrator;
import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.Librarian;
import client.control.data.entity.user.Student;
import client.control.data.entity.user.User;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a panel to hold the list of users
 * @author Daniel
 * @version 2
 *
 */
public class UserListPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();
	
	private JComboBox userTypeComboBox;
	private JCheckBox includeDisabledCheckBox;
	private JTextField userIDInputField;
	private JTextField firstNameInputField;
	private JTextField lastNameInputField;
	private JTextField emailAddressInputField;
	private JPanel searchPanel;
	private UserList list;
	
	// The search results
	private User[] searchResults;
	
	private ErrorPanel searchErrorPanel;
	
	public UserListPanel() {
		super();
		init();
	}
	
	// Create GUI
	private void init() {
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		setBackground(Constants.BACKPANEL);
		searchPanel = new JPanel();
		searchPanel.setBackground(Constants.BACKPANEL);
		add(searchPanel, BorderLayout.NORTH);
		createUserTypeInput();
		createUserIDInput();
		createFirstNameInput();
		createLastNameInput();
		createEmailAddressInput();
		createIncludeDisabledCheckBox();
		createSearchButton();
		createList();
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
		list = new UserList();
		add(list, BorderLayout.SOUTH);
	}

	private void createSearchButton() {
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (getSearchResults()) {
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

	private void createEmailAddressInput() {
		JTextPane emailAddressTextPane = new JTextPane();
		emailAddressTextPane.setFocusable(false);
		emailAddressTextPane.setEditable(false);
		emailAddressTextPane.setText("Email Address:");
		emailAddressTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(emailAddressTextPane);
		
		emailAddressInputField = new JTextField();
		searchPanel.add(emailAddressInputField);
		emailAddressInputField.setColumns(7);
	}

	private void createLastNameInput() {
		JTextPane lastNameTextPane = new JTextPane();
		lastNameTextPane.setFocusable(false);
		lastNameTextPane.setEditable(false);
		lastNameTextPane.setText("Last Name:");
		lastNameTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(lastNameTextPane);
		
		lastNameInputField = new JTextField();
		searchPanel.add(lastNameInputField);
		lastNameInputField.setColumns(7);
	}

	private void createFirstNameInput() {
		
		JTextPane firstNameTextPane = new JTextPane();
		firstNameTextPane.setFocusable(false);
		firstNameTextPane.setEditable(false);
		firstNameTextPane.setText("First Name:");
		firstNameTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(firstNameTextPane);
		
		firstNameInputField = new JTextField();
		searchPanel.add(firstNameInputField);
		firstNameInputField.setColumns(7);
	}

	private void createUserIDInput() {
		
		JTextPane userIDTextPane = new JTextPane();
		userIDTextPane.setFocusable(false);
		userIDTextPane.setEditable(false);
		userIDTextPane.setText("ID:");
		userIDTextPane.setBackground(Constants.BACKPANEL);
		searchPanel.add(userIDTextPane);
		
		userIDInputField = new JTextField();
		searchPanel.add(userIDInputField);
		userIDInputField.setColumns(9);
	}
	
	private void createUserTypeInput() {
		userTypeComboBox = new JComboBox();
		userTypeComboBox.setModel(new DefaultComboBoxModel(new String[]
				{"Student", "Faculty", "Librarian", "Administrator"}));
		userTypeComboBox.setSelectedIndex(0);
		userTypeComboBox.setMaximumRowCount(4);
		searchPanel.add(userTypeComboBox);
	}
	
	// performs the search
	private boolean getSearchResults() {
		
		if (searchResults == null)
			searchResults = new User[0];
		
		// The search parameters
		String userType = (String) userTypeComboBox.getSelectedItem();
		int userID = -1;
		if (userIDInputField.getText() != null && userIDInputField.getText().length() > 0) {
			try {
				userID = Integer.parseInt(userIDInputField.getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(mainWindow,
					    "The user ID can only contain digits.",
					    "Invalid User ID",
					    JOptionPane.ERROR_MESSAGE);
				return true;
			}
		}
		String firstName = firstNameInputField.getText();
		String lastName = lastNameInputField.getText();
		String emailAddress = emailAddressInputField.getText();
		
		// The User entity used for the search
		User searchQuery;
		if (userType.equalsIgnoreCase("administrator"))
			searchQuery = new Administrator(userID, firstName, lastName, null, emailAddress, true);
		else if (userType.equalsIgnoreCase("librarian"))
			searchQuery = new Librarian(userID, firstName, lastName, null, emailAddress, true);
		else if (userType.equalsIgnoreCase("faculty"))
			searchQuery = new Faculty(userID, firstName, lastName, null, emailAddress, true);
		else
			searchQuery = new Student(userID, firstName, lastName, null, emailAddress, true);
		
		User[] userArrayEnabled = UserManager.getUser(searchQuery);
		if (userArrayEnabled == null)
			return false;
		
		// optionally include disabled users
		if (includeDisabledCheckBox.isSelected()) {
			if (userType.equalsIgnoreCase("administrator"))
				searchQuery = new Administrator(-1, firstName, lastName, null, emailAddress, false);
			else if (userType.equalsIgnoreCase("librarian"))
				searchQuery = new Librarian(-1, firstName, lastName, null, emailAddress, false);
			else if (userType.equalsIgnoreCase("faculty"))
				searchQuery = new Faculty(-1, firstName, lastName, null, emailAddress, false);
			else
				searchQuery = new Student(-1, firstName, lastName, null, emailAddress, false);
			User[] userArrayDisabled = UserManager.getUser(searchQuery);
			if (userArrayDisabled == null)
				return false;
			
			searchResults = new User[userArrayEnabled.length + userArrayDisabled.length];
			int i;
			for (i = 0; i < userArrayEnabled.length; i++)
				searchResults[i] = userArrayEnabled[i];
			for (; i < searchResults.length; i++)
				searchResults[i] = userArrayDisabled[i - userArrayEnabled.length];
		}
		else
			searchResults = userArrayEnabled;
		
		return true;
		
	}
	
	@Override
	public String toString() {
		return "User Search";
	}

}
