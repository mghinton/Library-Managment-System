package client.userinterface.datatypes.resource;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import util.Constants;
import util.Utilities;
import client.control.data.ReserveManager;
import client.control.data.entity.resource.Reserve;
import client.control.data.entity.resource.Resource;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.datatypes.reserve.ReserveList;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a tab to view reserves on a particular resources.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ReservesTab extends JPanel {

	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();;

	// The resource entity
	private Resource resource;
	
	// The reserve list
	private ReserveList list;
	
	// The reserve array
	private Reserve[] reserveArray;
	
	// indicated whether this tab has been started
	private boolean started = false;
	
	private JTextField userIDInputField;
	private JButton addButton;
	private JButton removeButton;
	private JPanel buttonPanel;
	private ErrorPanel errorPanel;

	/**
	 * Create the panel.
	 */
	public ReservesTab(Resource resource) {
		
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
		createReserveList();
		
	}
	
	// Gets the required data from the server
	private boolean getData() {
		
		// get the resource types
		if (reserveArray == null) {
			Reserve searchKey = new Reserve(0, resource.getID(), 0, null, null, null);
			reserveArray = ReserveManager.getReserve(searchKey);
			if (reserveArray == null)
				return false;
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
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setHgap(10);
		
		// Creating the text pane that says "User ID:"
		JTextPane userIDTextPane = new JTextPane();
		userIDTextPane.setBackground(Constants.BACKPANEL);
		userIDTextPane.setText("User ID:");
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
				addReserve();
			}
		});
		panel.add(addButton);
		buttonPanel.add(panel);
	}

	// Creates the remove button
	private void createRemoveButton() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				removeReserve();
			}
		});
		panel.add(removeButton);
		buttonPanel.add(panel);
	}
	
	// adds a reserve for the user who's ID is given in the User ID textbox
	private void addReserve() {
		
		// get the resource ID
		int userID = getUserID();

		// check if an error occurred
		if (userID == -1)
			return;
		
		// The add reserve request
		Reserve reserve = new Reserve(-1, resource.getID(), -1, Utilities.removeTime(new Date()), null, null);
		reserve = ReserveManager.addReserve(reserve);
		
		// check if an error occurred
		if (reserve == null)
			return;
		
		// update the list
		ArrayList<Object> reserves = list.getItems();
		reserves.add(reserve);
		list.setItems(reserves);
		
	}
	
	// removes the reserve which is selected in the list
	private void removeReserve() {
		
		// get the selected reserve
		Reserve selectedReserve = (Reserve) list.getSelected();
		if (selectedReserve == null) {
			JOptionPane.showMessageDialog(mainWindow,
				    "No Reserve Selected",
				    "Please select a reserve from the list before clicking remove",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!ReserveManager.removeReserve(selectedReserve.getID()))
			return;
		
		// update the list
		ArrayList<Object> reserves = list.getItems();
		reserves.remove(selectedReserve);
		list.setItems(reserves);
		
	}
	
	// returns the user id in the User ID textbox
	// returns -1 if an error occurred
	private int getUserID() {
		int userID = -1;
		if (userIDInputField.getText() == null || userIDInputField.getText().length() <= 0)
			return -1;
		try {
			userID = Integer.parseInt(userIDInputField.getText());
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(mainWindow,
				    "The resource ID can only contain digits.",
				    "Invalid Resource ID",
				    JOptionPane.ERROR_MESSAGE);
		}
		return userID;
	}
	
	// Creates the reserve list
	private void createReserveList() {
		ArrayList<Object> reserves = new ArrayList<Object>(Arrays.asList(reserveArray));
		list = new ReserveList();
		list.setItems(reserves);
		add(list);
	}
	
	@Override
	public String toString() {
		return "Reserves";
	}
	
}
