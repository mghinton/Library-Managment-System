package client.userinterface.datatypes.resourcecopy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import util.Utilities;
import client.control.data.LoanManager;
import client.control.data.ReferenceManager;
import client.control.data.ResourceCopyManager;
import client.control.data.ResourceManager;
import client.control.data.entity.resource.Loan;
import client.control.data.entity.resource.Reference;
import client.control.data.entity.resource.Resource;
import client.control.data.entity.resource.ResourceCopy;
import client.control.data.entity.user.StaffMember;
import client.control.session.SessionManager;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.datatypes.resource.ResourceViewer;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a tab to view the information of a particular resource copy.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceCopyInformationTab extends JPanel {

	private static final long serialVersionUID = 1L;
	
	// Represents the different states the resource copy can be in
	private static final int AVAILABLE_STATE = 0;
	private static final int DISABLED_STATE = 1;
	private static final int LOAN_STATE = 2;
	private static final int REFERENCE_STATE = 3;
	
	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();

	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();

	// The resourceCopy entity
	private ResourceCopy resourceCopy;

	private JTextPane resourceLink;
	private JPanel statePanel;
	
	// indicated whether this tab has been started
	private boolean started = false;
	
	// Panels that are shown depending on the state of the resource copy
	private JPanel availableStatePanel;
	private JPanel disabledStatePanel;
	private JPanel loanStatePanel;
	private JPanel referenceStatePanel;
	
	// the associated loan or reference, if any
	private Loan loan;
	private Reference reference;
	
	// other components
	private JTextField userIDInputField;
	private JTextField ownerIDInputField;
	
	private ErrorPanel errorPanel;
	
	private JTextPane resourceCopyIDTextPane;

	/**
	 * Create the panel.
	 */
	public ResourceCopyInformationTab(ResourceCopy resourceCopy) {

		this.resourceCopy = resourceCopy;
		
	}
	
	/**
	 * start
	 */
	public void start() {
		
		if (started)
			return;
		started = true;
		
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
		setLayout(null);
		createIDTextPane();
		createResourceLink();
		createOwnerInput();
		createStatePanels();
	}
	
	// Gets the required data from the server
	private boolean getData() {
		// check if this resource copy is on loan
		Loan searchLoan = new Loan(-1, resourceCopy.getID(), -1, null, null, null, -1, true);
		Loan[] loansPaid = LoanManager.getLoan(searchLoan);
		if (loansPaid == null)
			return false;
		int i;
		for (i = 0; i < loansPaid.length; i++) {
			if (loansPaid[i].getCheckInDate() == null) {
				loan = loansPaid[i];
				return true;
			}
		}
		searchLoan = new Loan(-1, resourceCopy.getID(), -1, null, null, null, -1, false);
		Loan[] loansNotPaid = LoanManager.getLoan(searchLoan);
		if (loansNotPaid == null)
			return false;
		for (i = 0; i < loansNotPaid.length; i++) {
			if (loansNotPaid[i].getCheckInDate() == null) {
				loan = loansNotPaid[i];
				return true;
			}
		}
		
		// check if this resource copy is on reference
		Reference searchReference = new Reference(-1, resourceCopy.getID(), null, null, -1);
		Reference[] references = ReferenceManager.getReference(searchReference);
		if (references == null)
			return false;
		if (references.length == 1) {
			reference = references[0];
			return true;
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
	
	// Creates the id textpane
	private void createIDTextPane() {
		
		// Creating the text pane that says "Barcode (ID):"
		JTextPane resourceCopyIDTextPaneLabel = new JTextPane();
		resourceCopyIDTextPaneLabel.setBackground(Constants.BACKPANEL);
		resourceCopyIDTextPaneLabel.setText("Barcode (ID):");
		resourceCopyIDTextPaneLabel.setBounds(40, 30, 100, 20);
		resourceCopyIDTextPaneLabel.setEditable(false);
		resourceCopyIDTextPaneLabel.setFocusable(false);
		add(resourceCopyIDTextPaneLabel);
		
		// Creating the actual ID text
		resourceCopyIDTextPane = new JTextPane();
		resourceCopyIDTextPane.setBackground(Constants.BACKPANEL);
		resourceCopyIDTextPane.setText(resourceCopy.getID() == 1 ? ""
				: String.valueOf(resourceCopy.getID()));
		resourceCopyIDTextPane.setBounds(140, 30, 100, 20);
		resourceCopyIDTextPane.setEditable(false);
		resourceCopyIDTextPane.setFocusable(false);
		add(resourceCopyIDTextPane);
		
	}

	// Creates the resource link
	private void createResourceLink() {
		
		resourceLink = new JTextPane();
		resourceLink.setBackground(Constants.BACKPANEL);
		resourceLink.setForeground(new Color(0, 0, 153));
		resourceLink.setText("Open Associated Resource");
		resourceLink.setBounds(40, 60, 250, 20);
		resourceLink.setEditable(false);
		resourceLink.setEnabled(true);
		resourceLink.setFocusable(true);
		resourceLink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				openResource();
			}
		});
		Font font = resourceLink.getFont();
		Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		font = font.deriveFont(map);
		resourceLink.setFont(font);
		add(resourceLink);
		
	}

	// Creates the owner input
	private void createOwnerInput() {
		
		JPanel panel = new JPanel();
		panel.setBounds(40, 90, 360, 30);
		panel.setBorder(null);
		panel.setVisible(sessionManager.getUser() instanceof StaffMember);
		panel.setBackground(Constants.BACKPANEL);
		panel.setLayout(null);
		
		// Creating the text pane that says "User ID:"
		JTextPane userIDTextPane = new JTextPane();
		userIDTextPane.setBounds(0, 4, 46, 22);
		userIDTextPane.setBackground(Constants.BACKPANEL);
		userIDTextPane.setText("User ID:");
		userIDTextPane.setEditable(false);
		userIDTextPane.setFocusable(false);
		panel.add(userIDTextPane);

		// Creating the input text field
		ownerIDInputField = new JTextField();
		ownerIDInputField.setBounds(55, 4, 70, 22);
		ownerIDInputField.setColumns(9);
		ownerIDInputField.setText(String.valueOf(resourceCopy.getOwnerID()));
		ownerIDInputField.setEditable(sessionManager.getUser() instanceof StaffMember);
		panel.add(ownerIDInputField);
		
		// Creating the check out button
		JButton setOwnerButton = new JButton("Set Owner");
		setOwnerButton.setBounds(140, 4, 100, 22);
		setOwnerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				changeOwnerID();
			}
		});
		setOwnerButton.setVisible(sessionManager.getUser() instanceof StaffMember);
		panel.add(setOwnerButton);
		add(panel);
	}

	// Creates the state panels that represent the resource copy's availability
	private void createStatePanels() {
		
		statePanel = new JPanel();
		statePanel.setLayout(null);
		statePanel.setBounds(40, 130, 500, 300);
		statePanel.setBackground(Constants.BACKPANEL);
		add(statePanel);
		
		createAvailableStatePanel();
		createDisabledStatePanel();
		createLoanStatePanel();
		createReferenceStatePanel();
		
		// check if this resource copy is disabled
		if (!resourceCopy.getEnabled()) {
			switchState(DISABLED_STATE);
			return;
		}
		
		// check if this resource copy is on loan
		if (loan != null) {
			switchState(LOAN_STATE);
			return;
		}
		
		// check if this resource copy is on reference
		if (reference != null) {
			switchState(REFERENCE_STATE);
			return;
		}
		
		// otherwise, the resourceCopy is available
		switchState(AVAILABLE_STATE);
		
	}
	
	// Creating the panel that is shown when the resource copy is available
	private void createAvailableStatePanel() {
		
		availableStatePanel = new JPanel();
		availableStatePanel.setLayout(null);
		availableStatePanel.setBounds(0, 0, 500, 200);
		availableStatePanel.setBackground(Constants.BACKPANEL);
		
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("This copy is available.");
		textPane.setBounds(0, 0, 300, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		availableStatePanel.add(textPane);
		
		JPanel panel = new JPanel();
		panel.setSize(500, 170);
		panel.setLocation(0, 30);
		panel.setBorder(null);
		panel.setBackground(Constants.BACKPANEL);
		panel.setVisible(sessionManager.getUser() instanceof StaffMember);
		panel.setLayout(null);
		
		// Creating the text pane that says "User ID:"
		JTextPane userIDTextPane = new JTextPane();
		userIDTextPane.setBounds(0, 6, 46, 20);
		userIDTextPane.setBackground(Constants.BACKPANEL);
		userIDTextPane.setText("User ID:");
		userIDTextPane.setEditable(false);
		userIDTextPane.setFocusable(false);
		panel.add(userIDTextPane);

		// Creating the input text field
		userIDInputField = new JTextField();
		userIDInputField.setBounds(55, 6, 70, 20);
		userIDInputField.setColumns(8);
		panel.add(userIDInputField);
		
		// Creating the check out button
		JButton checkOutButton = new JButton("Check Out");
		checkOutButton.setBounds(140, 5, 150, 23);
		checkOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				checkOut();
			}
		});
		panel.add(checkOutButton);
		
		// Creating the put on reference button
		JButton referenceButton = new JButton("Put on Reference");
		referenceButton.setBounds(310, 5, 150, 23);
		referenceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				addReference();
			}
		});
		panel.add(referenceButton);
		
		// Creating the put on reference button
		JButton disableButton = new JButton("Disable");
		disableButton.setBounds(0, 40, 150, 23);
		disableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setCopyEnabled(false);
			}
		});
		panel.add(disableButton);
		availableStatePanel.add(panel);
		
	}
	
	// Creating the panel that is shown when the resource copy is disabled
	private void createDisabledStatePanel() {
		
		disabledStatePanel = new JPanel();
		disabledStatePanel.setLayout(null);
		disabledStatePanel.setBounds(0, 0, 400, 200);
		disabledStatePanel.setBackground(Constants.BACKPANEL);
		
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("This resource copy is disabled.");
		textPane.setBounds(0, 0, 300, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		disabledStatePanel.add(textPane);
		
		// Creating the put on reference button
		JButton disableButton = new JButton("Enable");
		disableButton.setBounds(0, 40, 150, 23);
		disableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setCopyEnabled(true);
			}
		});
		disableButton.setVisible(sessionManager.getUser() instanceof StaffMember);
		disabledStatePanel.add(disableButton);
		
	}
	
	// Creating the panel that is shown when the resource copy is on loan
	private void createLoanStatePanel() {
		
		loanStatePanel = new JPanel();
		loanStatePanel.setLayout(null);
		loanStatePanel.setBounds(0, 0, 400, 200);
		loanStatePanel.setBackground(Constants.BACKPANEL);
		
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("This resource copy is on loan.");
		textPane.setBounds(0, 0, 300, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		loanStatePanel.add(textPane);
		
		// Creating the renew button
		JButton renewButton = new JButton("Renew");
		renewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				renewLoan();
			}
		});
		renewButton.setBounds(0, 30, 150, 30);
		renewButton.setVisible(sessionManager.getUser() instanceof StaffMember);

		loanStatePanel.add(renewButton);
		
		// Creating the check in button
		JButton checkInButton = new JButton("Check In");
		checkInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				checkIn();
			}
		});
		checkInButton.setBounds(160, 30, 150, 30);
		checkInButton.setVisible(sessionManager.getUser() instanceof StaffMember);
		loanStatePanel.add(checkInButton);
		
	}
	
	// Creating the panel that is shown when the resource copy is on reference
	private void createReferenceStatePanel() {
		
		referenceStatePanel = new JPanel();
		referenceStatePanel.setLayout(null);
		referenceStatePanel.setBounds(0, 0, 400, 200);
		referenceStatePanel.setBackground(Constants.BACKPANEL);
		
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("This resource copy is on reference.");
		textPane.setBounds(0, 0, 300, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		referenceStatePanel.add(textPane);
		
		// Creating the check in button
		JButton removeButton = new JButton("Remove Reference");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				removeReference();
			}
		});
		removeButton.setBounds(0, 30, 150, 30);
		removeButton.setVisible(sessionManager.getUser() instanceof StaffMember);
		referenceStatePanel.add(removeButton);
		
	}

	// opens the resource for this resource copy
	private void openResource() {
		
		// send the request
		Resource resource = ResourceManager.getResource(resourceCopy.getResource());
		
		// check if an error occurred
		if (resource == null)
			return;
		
		ResourceViewer viewer = new ResourceViewer(resource);
		mainWindow.addTab(viewer.toString(), viewer);
		
	}
	
	// changes the owner ID
	private void changeOwnerID() {
		// get the new owner ID
		int ownerID = getOwnerID();
		
		// check if an error occurred
		if (ownerID == -1)
			return;
		
		ResourceCopy newResourceCopy = new ResourceCopy(resourceCopy.getID(),
				resourceCopy.getID(), resourceCopy.getResource(), ownerID,
				resourceCopy.getEnabled());
		
		if (!ResourceCopyManager.setResourceCopy(newResourceCopy))
			return;
		
		resourceCopy = newResourceCopy;
	}
	
	// enables/disables the resource
	private void setCopyEnabled(boolean enabled) {
		if (enabled == resourceCopy.getEnabled())
			return;
		
		ResourceCopy newCopy = new ResourceCopy(resourceCopy.getID(),
				resourceCopy.getResource(), resourceCopy.getCopyID(),
				resourceCopy.getOwnerID(), enabled);
		
		if (!ResourceCopyManager.setResourceCopy(newCopy))
			return;
		
		resourceCopy = newCopy;
		
		if (enabled)
			switchState(AVAILABLE_STATE);
		else
			switchState(DISABLED_STATE);
	}	
	
	// checks out the resource copy for the user who's ID is given in the user ID textbox
	private void checkOut() {
		
		// get the user ID from the user ID textbox
		int userID = getUserID();
		
		// check if an error occurred
		if (userID == -1)
			return;
		
		// The add loan request
		Loan newLoan = new Loan(-1, resourceCopy.getID(), userID,
				Utilities.removeTime(new Date()), null, null, 0, false);
		newLoan = LoanManager.addLoan(newLoan);
		
		// check if an error occurred
		if (newLoan == null)
			return;
		
		loan = newLoan;
		
		// switch state
		switchState(LOAN_STATE);
		
	}
	
	// renews the loan
	private void renewLoan() {
		
		// send the renew request
		Loan newLoan = LoanManager.renewLoan(loan);
		
		// check if an error occurred
		if (newLoan == null)
			return;
		
		loan = newLoan;
		
	}
	
	// checks in the resource copy
	private void checkIn() {
		
		// check if an error occurred
		if (!LoanManager.removeLoan(loan.getID()))
			return;
		
		loan = null;
			
		// switch state
		switchState(AVAILABLE_STATE);
	}
	
	// puts the resource copy on reference for the user who's ID is given in the user ID textbox
	private void addReference() {
		
		// get the user ID from the user ID textbox
		int userID = getUserID();
		
		// check if an error occurred
		if (userID == -1)
			return;
		
		// The add loan request
		Reference newReference = new Reference(-1, resourceCopy.getID(), Utilities.removeTime(new Date()), null, userID);
		newReference = ReferenceManager.addReference(newReference);
		
		// check if an error occurred
		if (newReference == null)
			return;
		
		reference = newReference;
		
		// switch state
		switchState(REFERENCE_STATE);
		
	}
	
	// removes the reference for the resource copy
	private void removeReference() {
		
		if (!ReferenceManager.removeReference(reference.getID()))
			return;
		
		reference = null;
			
		// switch state
		switchState(AVAILABLE_STATE);
	}
	
	// changes the appearance of the viewer to reflect the given state of the
	// resource copy
	private void switchState(int newState) {
		statePanel.removeAll();
		switch (newState) {
		case AVAILABLE_STATE:
			statePanel.add(availableStatePanel);
			break;
		case DISABLED_STATE:
			statePanel.add(disabledStatePanel);
			break;
		case LOAN_STATE:
			statePanel.add(loanStatePanel);
			break;
		case REFERENCE_STATE:
			statePanel.add(referenceStatePanel);
			break;
		}
		validate();
		repaint();
	}
	
	// returns the user id in the user ID textbox
	// returns -1 if an error occurred
	private int getUserID() {
		int userID = -1;
		if (userIDInputField.getText() == null || userIDInputField.getText().length() <= 0)
			return -1;
		try {
			userID = Integer.parseInt(userIDInputField.getText());
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(mainWindow,
				    "The user ID can only contain digits.",
				    "Invalid User ID",
				    JOptionPane.ERROR_MESSAGE);
		}
		return userID;
	}
	
	// returns the user id in the owner ID textbox
	// returns -1 if an error occurred
	private int getOwnerID() {
		int ownerID = -1;
		if (ownerIDInputField.getText() == null || ownerIDInputField.getText().length() <= 0)
			return 0;
		try {
			ownerID = Integer.parseInt(ownerIDInputField.getText());
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(mainWindow,
				    "The user ID can only contain digits.",
				    "Invalid User ID",
				    JOptionPane.ERROR_MESSAGE);
		}
		return ownerID;
	}
	
	@Override
	public String toString() {
		return "Information";
	}

}
