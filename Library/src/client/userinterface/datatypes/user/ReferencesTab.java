package client.userinterface.datatypes.user;

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
import client.control.data.ReferenceManager;
import client.control.data.entity.resource.Reference;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.datatypes.reference.ReferenceList;
import client.userinterface.mainwindow.MainWindow;

/**
 * Populates a tab with the list of references
 * @author Daniel
 * @version 2
 *
 */
public class ReferencesTab extends JPanel {

	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();;

	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();;

	// The user entity
	private User user;
	
	// The references list
	private ReferenceList list;
	
	// The reference array
	private Reference[] referenceArray;
	
	private ErrorPanel errorPanel;
	
	// indicated whether this tab has been started
	private boolean started = false;
	
	// Used to add new loans
	private JTextField resourceCopyBarcodeInputField;
	private JButton addReferenceButton;
	private JButton removeButton;
	private JPanel buttonPanel;

	/**
	 * Create the panel.
	 */
	public ReferencesTab(User user) {
		
		this.user = user;
		
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
		if (sessionManager.getUser() instanceof StaffMember)
			createButtonPanel();
		createReferenceList();
		
	}
	
	// Gets the required data from the server
	private boolean getData() {
		
		// get the resource types
		if (referenceArray == null) {
			Reference searchKey = new Reference(-1, -1, null, null, user.getID());
			referenceArray = ReferenceManager.getReference(searchKey);
			if (referenceArray == null)
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
	
	private void createReferenceList() {
		ArrayList<Object> references = new ArrayList<Object>(Arrays.asList(referenceArray));
		list = new ReferenceList();
		list.setItems(references);
		add(list);
	}
	
	// Creates the button panel
	private void createButtonPanel() {
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(1, 11, 0, 0));
		buttonPanel.setBackground(Constants.BACKPANEL);
		
		createAddReferenceInput();
		createRemoveReferenceInput();
	}

	// Creates the barcode textbox and add reference button
	private void createAddReferenceInput() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setHgap(10);
		
		// Creating the text pane that says "Barcode:"
		JTextPane resourceCopyBarcodeTextPane = new JTextPane();
		resourceCopyBarcodeTextPane.setBackground(Constants.BACKPANEL);
		resourceCopyBarcodeTextPane.setText("Barcode:");
		resourceCopyBarcodeTextPane.setEditable(false);
		resourceCopyBarcodeTextPane.setFocusable(false);
		panel.add(resourceCopyBarcodeTextPane);

		// Creating the input text field
		resourceCopyBarcodeInputField = new JTextField();
		resourceCopyBarcodeInputField.setColumns(8);
		panel.add(resourceCopyBarcodeInputField);
		
		// Creating the check out button
		addReferenceButton = new JButton("Add");
		addReferenceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				addReference();
			}
		});
		panel.add(addReferenceButton);
		buttonPanel.add(panel);
	}

	// Creates the remove button
	private void createRemoveReferenceInput() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				removeReference();
			}
		});
		panel.add(removeButton);
		buttonPanel.add(panel);
	}
	
	// adds a new reference for the resource copy who's barcode is given in the
	// barcode textbox
	private void addReference() {
		
		// get the resource copy for the barcode in the barcode textbox
		int resourceCopyID = getBarcode();
		
		// check if an error occurred
		if (resourceCopyID == -1)
			return;
		
		// The add reference request
		Reference reference = new Reference(-1, resourceCopyID,
				Utilities.removeTime(new Date()), null, user.getID());
		reference = ReferenceManager.addReference(reference);
		
		// check if an error occurred
		if (reference == null)
			return;
		
		// update the list
		ArrayList<Object> references = list.getItems();
		references.add(reference);
		list.setItems(references);
		
	}
	
	// removes the reference selected in the list
	private void removeReference() {
		
		// get the selected reference
		Reference selectedReference = (Reference) list.getSelected();
		if (selectedReference == null) {
			JOptionPane.showMessageDialog(mainWindow,
				    "Please select a reference from the list before clicking remove",
				    "No Reference Selected",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!ReferenceManager.removeReference(selectedReference.getID()))
			return;
		
		// update the list
		ArrayList<Object> references = list.getItems();
		references.remove(selectedReference);
		list.setItems(references);
		
	}
	
	// returns the barcode (actually a resource copy ID) associated with the text
	// in the barcode textbox. returns -1 if an error occurred
	private int getBarcode() {
		int barcode = -1;
		if (resourceCopyBarcodeInputField.getText() == null || resourceCopyBarcodeInputField.getText().length() <= 0)
			return -1;
		try {
			barcode = Integer.parseInt(resourceCopyBarcodeInputField.getText());
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(mainWindow,
				    "The barcode can only contain digits.",
				    "Invalid Barcode",
				    JOptionPane.ERROR_MESSAGE);
		}
		return barcode;
	}
	
	@Override
	public String toString() {
		return "References";
	}
	
}
