package client.userinterface.datatypes.resource;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import util.Constants;
import client.control.data.ResourceCopyManager;
import client.control.data.entity.resource.Resource;
import client.control.data.entity.resource.ResourceCopy;
import client.control.data.entity.user.StaffMember;
import client.control.session.SessionManager;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.datatypes.resourcecopy.ResourceCopyList;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a tab to vew copies of a particular resource.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceCopyTab extends JPanel {

	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();;

	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();;

	// The resource entity
	private Resource resource;
	
	// The resource copy list
	private ResourceCopyList list;
	
	// The resource copy array
	private ResourceCopy[] resourceCopyArray;
	
	// indicated whether this tab has been started
	private boolean started = false;
	
	// Used to add new loans
	private JButton addButton;
	private JButton disableButton;
	private JPanel buttonPanel;
	private JTextField userIDInputField;
	private ErrorPanel errorPanel;

	/**
	 * Create the panel.
	 */
	public ResourceCopyTab(Resource resource) {
		
		this.resource = resource;
		
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
		setLayout(new BorderLayout(0, 0));
		createButtonPanel();
		createResourceCopyList();
		
	}
	
	// Gets the required data from the server
	private boolean getData() {
		
		// get the resource types
		if (resourceCopyArray == null) {
			ResourceCopy searchKey = new ResourceCopy(-1, resource.getID(), -1, -1, true);
			ResourceCopy[] resourceCopiesEnabled = ResourceCopyManager.getResourceCopy(searchKey);
			if (resourceCopiesEnabled == null)
				return false;
			searchKey = new ResourceCopy(-1, resource.getID(), -1, -1, false);
			ResourceCopy[] resourceCopiesDisabled = ResourceCopyManager.getResourceCopy(searchKey);
			if (resourceCopiesDisabled == null)
				return false;
			resourceCopyArray = new ResourceCopy[resourceCopiesEnabled.length + resourceCopiesDisabled.length];
			int i;
			for (i = 0; i < resourceCopiesEnabled.length; i++)
				resourceCopyArray[i] = resourceCopiesEnabled[i];
			for (; i < resourceCopyArray.length; i++)
				resourceCopyArray[i] = resourceCopiesDisabled[i-resourceCopiesEnabled.length];
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
	
	private void createResourceCopyList() {
		// the resource copies
		ArrayList<Object> resourceCopies = new ArrayList<Object>(Arrays.asList(resourceCopyArray));
		
		// create the list
		list = new ResourceCopyList();
		list.setItems(resourceCopies);
		add(list);
	}
	
	// Creates the button panel
	private void createButtonPanel() {
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(1, 11, 0, 0));
		buttonPanel.setBackground(Constants.BACKPANEL);
		
		createAddInput();
		createDisableInput();
	}

	// Creates the add button
	private void createAddInput() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setHgap(10);
		panel.setVisible(sessionManager.getUser() instanceof StaffMember);
		
		// Creating the text pane that says "Owner ID:"
		JTextPane userIDTextPane = new JTextPane();
		userIDTextPane.setBackground(Constants.BACKPANEL);
		userIDTextPane.setText("Owner ID:");
		userIDTextPane.setEditable(false);
		userIDTextPane.setFocusable(false);
		panel.add(userIDTextPane);

		// Creating the input text field
		userIDInputField = new JTextField();
		userIDInputField.setColumns(8);
		panel.add(userIDInputField);
		
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				addResourceCopy();
			}
		});
		panel.add(addButton);
		buttonPanel.add(panel);
	}

	// Creates the disable button
	private void createDisableInput() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		
		// Creating the remove button
		disableButton = new JButton("Disable");
		disableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				disableResourceCopy();
			}
		});
		panel.add(disableButton);
		buttonPanel.add(panel);
	}
	
	// creates a new resource copy
	private void addResourceCopy() {
		
		// get the owner ID
		int ownerID = getUserID();
		
		// check if an error occurred
		if (ownerID == -1)
			return;
		
		// the new resource copy
		ResourceCopy resourceCopy = new ResourceCopy(-1, resource.getID(), -1, ownerID, true);
		resourceCopy = ResourceCopyManager.addResourceCopy(resourceCopy);
		
		// check if an error occurred
		if (resourceCopy == null)
			return;
			
		// update the list
		ArrayList<Object> resourceCopies = list.getItems();
		resourceCopies.add(resourceCopy);
		list.setItems(resourceCopies);
		
	}
	
	// disabled the resource copy selected in the list
	private void disableResourceCopy() {
		
		// get the selected resource copy
		ResourceCopy selectedCopy = (ResourceCopy) list.getSelected();
		if (selectedCopy == null) {
			JOptionPane.showMessageDialog(mainWindow,
				    "Please select a resource copy from the list before clicking disable",
				    "No Resource Copy Selected",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ResourceCopy newResourceCopy = new ResourceCopy(selectedCopy.getID(),
				selectedCopy.getID(), selectedCopy.getResource(),
				selectedCopy.getOwnerID(), false);
		
		if (!ResourceCopyManager.setResourceCopy(newResourceCopy))
			return;
			
		// update the list
		ArrayList<Object> resourceCopies = list.getItems();
		resourceCopies.remove(selectedCopy);
		resourceCopies.add(newResourceCopy);
		list.setItems(resourceCopies);
		
	}
	
	// returns the user id in the user ID textbox
	// returns -1 if an error occurred
	private int getUserID() {
		int userID = -1;
		if (userIDInputField.getText() == null || userIDInputField.getText().length() <= 0)
			return 0;
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
	
	@Override
	public String toString() {
		return "Resource Copies";
	}
	
}
