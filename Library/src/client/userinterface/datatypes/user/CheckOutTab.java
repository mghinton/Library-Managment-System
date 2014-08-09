package client.userinterface.datatypes.user;

/**
 * Populates the information for the checkout tab
 * @author Daniel
 * @version 2
 *
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import util.Constants;
import util.Utilities;
import client.control.data.LoanManager;
import client.control.data.entity.resource.Loan;
import client.control.data.entity.user.User;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.datatypes.loan.LoanList;
import client.userinterface.mainwindow.MainWindow;

public class CheckOutTab extends JPanel {

	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();;

	// The user entity
	private User user;
	
	// The loan list
	private LoanList list;
	
	// The loan array
	private Loan[] loanArray;
	
	private ErrorPanel errorPanel;
	
	// indicated whether this tab has been started
	private boolean started = false;
	
	// Used to add new loans
	private JTextField resourceCopyBarcodeInputField;
	private JButton checkOutButton;
	private JButton renewButton;
	private JButton checkinButton;
	private JPanel buttonPanel;

	/**
	 * Create the panel.
	 */
	public CheckOutTab(User user) {
		
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
		createButtonPanel();
		createLoanList();
		
	}
	
	// Gets the required data from the server
	private boolean getData() {
		
		if (loanArray == null) {
			Loan searchKey = new Loan(-1, -1, user.getID(), null, null, null, -1, true);
			Loan[] loanArrayPaid = LoanManager.getLoan(searchKey);
			if (loanArrayPaid == null)
				return false;
			searchKey = new Loan(-1, -1, user.getID(), null, null, null, -1, false);
			Loan[] loanArrayNotPaid = LoanManager.getLoan(searchKey);
			if (loanArrayNotPaid == null)
				return false;
			loanArray = new Loan[loanArrayPaid.length + loanArrayNotPaid.length];
			int i;
			for (i = 0; i < loanArrayPaid.length; i++)
				loanArray[i] = loanArrayPaid[i];
			for (; i < loanArray.length; i++)
				loanArray[i] = loanArrayNotPaid[i - loanArrayPaid.length];
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
	
	private void createLoanList() {
		ArrayList<Object> activeLoans = new ArrayList<Object>();
		for (int i = 0; i < loanArray.length; i++)
			if (loanArray[i].getCheckInDate() == null)
				activeLoans.add(loanArray[i]);
		list = new LoanList();
		list.setItems(activeLoans);
		add(list);
	}
	
	// Creates the button panel
	private void createButtonPanel() {
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(1, 11, 0, 0));
		buttonPanel.setBackground(Constants.BACKPANEL);
		
		createCheckoutInput();
		createRenewInput();
		createCheckinInput();
	}

	// Creates the barcode textbox and check out button
	private void createCheckoutInput() {
		
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
		checkOutButton = new JButton("Check Out");
		checkOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				checkOut();
			}
		});
		panel.add(checkOutButton);
		buttonPanel.add(panel);
	}

	// Creates the renew button
	private void createRenewInput() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		
		// Creating the renew button
		renewButton = new JButton("Renew");
		renewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				renew();
			}
		});
		panel.add(renewButton);
		buttonPanel.add(panel);
	}

	// Creates the checkin button
	private void createCheckinInput() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		
		checkinButton = new JButton("Check In");
		checkinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				checkIn();
			}
		});
		panel.add(checkinButton);
		buttonPanel.add(panel);
	}
	
	// checks out the resource copy who's ID is given in the barcode textbox
	private void checkOut() {
		
		// get the resource copy ID from the barcode textbox
		int resourceCopyID = getBarcode();
		
		// check if an error occurred
		if (resourceCopyID == -1)
			return;
		
		// The add loan request
		Loan loan = new Loan(-1, resourceCopyID, user.getID(),
				Utilities.removeTime(new Date()), null, null, -1, false);
		loan = LoanManager.addLoan(loan);
		
		// check if an error occurred
		if (loan == null)
			return;
		
		// update the list
		ArrayList<Object> loans = list.getItems();
		loans.add(loan);
		list.setItems(loans);
		
	}
	
	// renews the resource copy selected in the list
	private void renew() {
		
		// get the selected loan
		Loan selectedLoan = (Loan) list.getSelected();
		if (selectedLoan == null) {
			JOptionPane.showMessageDialog(mainWindow,
				    "Please select a resource copy from the list before clicking renew",
				    "No Resource Copy Selected",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// send the renew request
		Loan newLoan = LoanManager.renewLoan(selectedLoan);
		
		// check if an error occurred
		if (newLoan == null)
			return;
		
		// update the loan list
		ArrayList<Object> loans = list.getItems();
		loans.remove(selectedLoan);
		loans.add(newLoan);
		list.setItems(loans);
		
	}
	
	// checks in the resource copy selected in the list
	private void checkIn() {
		
		// get the selected loan
		Loan selectedLoan = (Loan) list.getSelected();
		if (selectedLoan == null) {
			JOptionPane.showMessageDialog(mainWindow,
				    "Please select a resource copy from the list before clicking check in",
				    "No Resource Copy Selected",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (!LoanManager.removeLoan(selectedLoan.getID()))
			return;
			
		// update list
		ArrayList<Object> loans = list.getItems();
		loans.remove(selectedLoan);
		list.setItems(loans);
		
	}
	
	// returns the barcode (actually the resource copy ID) associated with the
	// text in the barcode textbox
	// returns -1 if an error occurred
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
		return "Check Out";
	}

}
