package client.userinterface.datatypes.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import util.Constants;
import client.control.data.LoanManager;
import client.control.data.entity.resource.Loan;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.datatypes.loan.LoanList;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a tab to hold a user's fines
 * @author Daniel
 * @version 2
 *
 */
public class FinesTab extends JPanel {

	private static final long serialVersionUID = 1L;
	
	// A reference to the MainWindow class
	private MainWindow mainWindow = MainWindow.getReference();;
	
	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();;

	// The user entity
	private User user;
	
	// The loan list
	private LoanList list;
	
	// The loan array
	private Loan[] loanArray;
	
	private ErrorPanel errorPanel;
	
	// indicated whether this tab has been started
	private boolean started = false;
	
	private JButton removeFineButton;
	private JPanel buttonPanel;
	private JTextPane totalTextPane;

	/**
	 * Create the panel.
	 */
	public FinesTab(User user) {
		
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
		createLoanList();
		if (sessionManager.getUser() instanceof StaffMember)
			createButtonPanel();
		
	}
	
	// Gets the required data from the server
	private boolean getData() {
		
		// get the resource types
		if (loanArray == null) {
			Loan searchKey = new Loan(-1, -1, user.getID(), null, null, null, -1, false);
			loanArray = LoanManager.getLoan(searchKey);
			if (loanArray == null)
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
	
	// Creates the button panel
	private void createButtonPanel() {
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(1, 11, 0, 0));
		buttonPanel.setBackground(Constants.BACKPANEL);
		
		createRemoveFineInput();
		createTotalInput();
	}

	// Creates the remove button
	private void createRemoveFineInput() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		
		removeFineButton = new JButton("Remove Fine");
		removeFineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				removeFine();
			}
		});
		panel.add(removeFineButton);
		buttonPanel.add(panel);
	}
	
	// Displays the total fine
	private void createTotalInput() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		flowLayout.setHgap(10);
		
		// Creating the text pane that says the total amount
		totalTextPane = new JTextPane();
		totalTextPane.setBackground(Constants.BACKPANEL);
		totalTextPane.setForeground(Color.RED);
		totalTextPane.setText("Total: $" + new DecimalFormat("0.00").format(getTotalAmount()));
		totalTextPane.setEditable(false);
		totalTextPane.setFocusable(false);
		panel.add(totalTextPane);

	}
	
	private float getTotalAmount() {
		ArrayList<Object> loans = list.getItems();
		float total = 0;
		for (int i = 0; i < loans.size(); i++)
			total += ((Loan) loans.get(i)).getFineAmount();
		return total;
	}
	
	// removes the fine associated with the selected loan
	private void removeFine() {
		
		// get the selected loan
		Loan selectedLoan = (Loan) list.getSelected();
		if (selectedLoan == null) {
			JOptionPane.showMessageDialog(mainWindow,
				    "Please select a loan from the list before clicking remove fine",
				    "No Loan Selected",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Loan newLoan = new Loan(selectedLoan.getID(),
				selectedLoan.getResourceCopy(), selectedLoan.getUserID(),
				selectedLoan.getCheckOutDate(), selectedLoan.getDueDate(),
				selectedLoan.getCheckInDate(), 0, true);
		
		if (LoanManager.setLoan(newLoan))
			return;
		
		// update list
		ArrayList<Object> loans = list.getItems();
		loans.remove(selectedLoan);
		loans.add(newLoan);
		list.setItems(loans);
		totalTextPane.setText("Total: $" + new DecimalFormat("0.00").format(getTotalAmount()));
		
	}

	// Creates the loan list that shows the user's fines
	private void createLoanList() {
		ArrayList<Object> loans = new ArrayList<Object>();
		loans.addAll(Arrays.asList(loanArray));
		for (int i = 0; i < loans.size(); i++)
			if (((Loan) loans.get(i)).getFineAmount() <= 0)
				loans.remove(i);
		list = new LoanList();
		list.setItems(loans);
		add(list);
	}
	
	@Override
	public String toString() {
		return "Fines";
	}
	
}
