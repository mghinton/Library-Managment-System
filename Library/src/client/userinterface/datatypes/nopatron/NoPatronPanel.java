package client.userinterface.datatypes.nopatron;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import util.Utilities;
import client.control.data.LoanManager;
import client.control.data.entity.resource.Loan;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates the panel for checking in books when no patron is present
 * 
 * @author Daniel Huettner
 * @version 3
 */
public class NoPatronPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();

	private JTextField barcodeInputField;
	
	/**
	 * Construct a new NoPatronPanel.
	 */
	public NoPatronPanel() {
		// Create the GUI
		setVisible(true);
		setLayout(null);
		init();
	}
	
	private void init() {
		setBackground(Constants.BACKPANEL);
		createBarcodeInput();
		createCheckInButton();
		createRenewButton();
	}
	
	private void createBarcodeInput() {
		
		JTextPane barcodeTextPane = new JTextPane();
		barcodeTextPane.setBackground(Constants.BACKPANEL);
		barcodeTextPane.setText("Barcode:");
		barcodeTextPane.setBounds(40, 30, 100, 20);
		barcodeTextPane.setEditable(false);
		barcodeTextPane.setFocusable(false);
		add(barcodeTextPane);

		// Creating the input text field
		barcodeInputField = new JTextField();
		barcodeInputField.setBounds(140, 30, 180, 20);
		add(barcodeInputField);
		barcodeInputField.setColumns(9);
		
	}
	
	private void createCheckInButton() {
		JButton button = new JButton("Check In");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int resourceCopyID = getBarcode();
				if (resourceCopyID == -1)
					return;
				Loan searchKey = new Loan(-1, resourceCopyID, -1, null, null, null, -1, true);
				Loan[] loansPaid = LoanManager.getLoan(searchKey);
				if (loansPaid == null)
					return;
				Loan loan = null;
				for (int i = 0; i < loansPaid.length; i++)
					if (loansPaid[i].getCheckInDate() == null)
						loan = loansPaid[i];
				if (loan == null) {
					searchKey = new Loan(-1, resourceCopyID, -1, null, null, null, -1, false);
					Loan[] loansNotPaid = LoanManager.getLoan(searchKey);
					if (loansNotPaid == null)
						return;
					for (int i = 0; i < loansNotPaid.length; i++)
						if (loansNotPaid[i].getCheckInDate() == null)
							loan = loansNotPaid[i];
				}
				if (loan == null) {
					JOptionPane.showMessageDialog(mainWindow,
						    "The resource copy was not checked out",
						    "Not Checked Out",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				loan = new Loan(loan.getID(),
						loan.getResourceCopy(), loan.getUserID(),
						loan.getCheckOutDate(), loan.getDueDate(),
						Utilities.removeTime(new Date()),
						loan.getFineAmount(), loan.getFinePaid());
				LoanManager.setLoan(loan);
			}
		});
		button.setBounds(40, 60, 100, 30);
		add(button);
	}
	
	private void createRenewButton() {
		JButton button = new JButton("Renew");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int resourceCopyID = getBarcode();
				if (resourceCopyID == -1)
					return;
				Loan searchKey = new Loan(-1, resourceCopyID, -1, null, null, null, -1, true);
				Loan[] loansPaid = LoanManager.getLoan(searchKey);
				if (loansPaid == null)
					return;
				Loan loan = null;
				for (int i = 0; i < loansPaid.length; i++)
					if (loansPaid[i].getCheckInDate() == null)
						loan = loansPaid[i];
				if (loan == null) {
					searchKey = new Loan(-1, resourceCopyID, -1, null, null, null, -1, false);
					Loan[] loansNotPaid = LoanManager.getLoan(searchKey);
					if (loansNotPaid == null)
						return;
					for (int i = 0; i < loansNotPaid.length; i++)
						if (loansNotPaid[i].getCheckInDate() == null)
							loan = loansNotPaid[i];
				}
				if (loan == null) {
					JOptionPane.showMessageDialog(mainWindow,
						    "The resource copy was not checked out",
						    "Not Checked Out",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				LoanManager.renewLoan(loan);
			}
		});
		button.setBounds(150, 60, 100, 30);
		add(button);
	}
	
	// returns the barcode (resource copy ID) in the barcode textbox
	// returns -1 if an error occurred
	private int getBarcode() {
		int barcode = -1;
		if (barcodeInputField.getText() == null || barcodeInputField.getText().length() <= 0)
			return -1;
		try {
			barcode = Integer.parseInt(barcodeInputField.getText());
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
		return "No Patron";
	}

}
