package client.userinterface.datatypes.user;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

import util.Constants;

import client.control.data.LoanManager;
import client.control.data.entity.resource.Loan;
import client.control.data.entity.user.User;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.datatypes.loan.LoanList;

/**
 * Populates a tab with the history for a particular user
 * @author Daniel
 * @version 2
 *
 */
public class UserHistoryTab extends JPanel {

	private static final long serialVersionUID = 1L;

	// The user entity
	private User user;
	
	// The loan list
	private LoanList list;
	
	// The loan array
	private Loan[] loanArray;
	
	private ErrorPanel errorPanel;
	
	// indicated whether this tab has been started
	private boolean started = false;

	/**
	 * Create the panel.
	 */
	public UserHistoryTab(User user) {
		
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
	}
	
	// Gets the required data from the server
	private boolean getData() {
		
		// get the resource types
		if (loanArray == null) {
			Loan searchKey = new Loan(-1, -1, user.getID(), null, null, null, (float) -1.0, true);
			Loan[] loanArrayPaid = LoanManager.getLoan(searchKey);
			if (loanArrayPaid == null)
				return false;
			searchKey = new Loan(-1, -1, user.getID(), null, null, null, (float) -1.0, false);
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
	
	// Creates the loan list that shows the user's history
	private void createLoanList() {
		ArrayList<Object> loans = new ArrayList<Object>(Arrays.asList(loanArray));
		list = new LoanList();
		list.setItems(loans);
		add(list);
	}
	
	@Override
	public String toString() {
		return "History";
	}

}
