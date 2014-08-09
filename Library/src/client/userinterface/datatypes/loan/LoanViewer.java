package client.userinterface.datatypes.loan;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import util.Constants;
import util.Utilities;
import client.control.data.LoanManager;
import client.control.data.ResourceCopyManager;
import client.control.data.UserManager;
import client.control.data.entity.resource.Loan;
import client.control.data.entity.resource.ResourceCopy;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;
import client.userinterface.datatypes.resourcecopy.ResourceCopyViewer;
import client.userinterface.datatypes.user.UserViewer;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a viewer for viewing information of individual loans.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class LoanViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();

	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();
	
	// The loan entity
	private Loan loan = null;
	
	// Button panel
	private JPanel buttonPanel;
	
	
	/**
	 * Construct a new LoanViewer.
	 * 
	 * @param todo
	 * 		The loan entity to view/modify
	 */
	public LoanViewer(Loan loan) {
		
		this.loan = loan;
		
		// Create the GUI
		setVisible(true);
		setLayout(null);
		setBackground(Constants.BACKPANEL);
		createIDText();
		createCheckOutDateText();
		createCheckInDateText();
		createDueDateText();
		createFineText();
		createResourceCopyLink();
		createUserLink();
		createButtonPanel();
		
	}
	
	private void createIDText() {
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("Loan ID:");
		textPane.setBounds(10, 120, 130, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		add(textPane);
		
		JTextPane idText = new JTextPane();
		idText.setBackground(Constants.BACKPANEL);
		idText.setText(String.valueOf(loan.getID()));
		idText.setBounds(10, 30, 100, 20);
		idText.setEditable(false);
		idText.setBackground(Constants.BACKPANEL);
		add(idText);
	}
	
	private void createCheckOutDateText() {
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("Check Out Date:");
		textPane.setBounds(10, 60, 130, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		add(textPane);
		
		JTextPane checkOutDateText = new JTextPane();
		checkOutDateText.setBackground(Constants.BACKPANEL);
		checkOutDateText.setText(loan.getCheckOutDate() == null ? "" : 
				Utilities.formatDate(loan.getCheckOutDate(),
				Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE));
		checkOutDateText.setBounds(140, 60, 100, 20);
		checkOutDateText.setEditable(false);
		checkOutDateText.setBackground(Constants.BACKPANEL);
		add(checkOutDateText);
	}
	
	private void createCheckInDateText() {
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("Check In Date:");
		textPane.setBounds(10, 90, 130, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		add(textPane);
		
		JTextPane checkInDateText = new JTextPane();
		checkInDateText.setBackground(Constants.BACKPANEL);
		checkInDateText.setText(loan.getCheckInDate() == null ? "" : 
				Utilities.formatDate(loan.getCheckInDate(),
				Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE));
		checkInDateText.setBounds(140, 90, 100, 20);
		checkInDateText.setEditable(false);
		checkInDateText.setBackground(Constants.BACKPANEL);
		add(checkInDateText);
	}
	
	private void createDueDateText() {
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("Due Date:");
		textPane.setBounds(10, 120, 130, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		add(textPane);
		
		JTextPane dueDateText = new JTextPane();
		dueDateText.setBackground(Constants.BACKPANEL);
		dueDateText.setText(loan.getDueDate() == null ? "" : 
				Utilities.formatDate(loan.getDueDate(),
				Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE));
		dueDateText.setBounds(140, 120, 100, 20);
		dueDateText.setEditable(false);
		dueDateText.setBackground(Constants.BACKPANEL);
		add(dueDateText);
	}
	
	private void createFineText() {
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("Fine Amount:");
		textPane.setBounds(10, 150, 130, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		add(textPane);
		
		JTextPane fineAmountText = new JTextPane();
		fineAmountText.setBackground(Constants.BACKPANEL);
		fineAmountText.setText(new DecimalFormat("0.00").format(loan.getFineAmount()));
		fineAmountText.setBounds(140, 150, 100, 20);
		fineAmountText.setEditable(false);
		fineAmountText.setBackground(Constants.BACKPANEL);
		add(fineAmountText);
	}
	
	private void createResourceCopyLink() {
		JTextPane resourceCopyLink = new JTextPane();
		resourceCopyLink.setForeground(new Color(0, 0, 153));
		resourceCopyLink.setBackground(Constants.BACKPANEL);
		resourceCopyLink.setText("Click here to open the associated resource copy");
		resourceCopyLink.setBounds(10, 180, 300, 20);
		resourceCopyLink.setEditable(false);
		resourceCopyLink.setBackground(Constants.BACKPANEL);
		resourceCopyLink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				openResourceCopy();
			}
		});
		Font font = resourceCopyLink.getFont();
		Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		font = font.deriveFont(map);
		resourceCopyLink.setFont(font);
		add(resourceCopyLink);
	}
	
	// Creates the Expiration date input stuff
	private void createUserLink() {
		JTextPane userLink = new JTextPane();
		userLink.setForeground(new Color(0, 0, 153));
		userLink.setBackground(Constants.BACKPANEL);
		userLink.setText("Click here to open the associated user");
		userLink.setBounds(10, 210, 300, 20);
		userLink.setEditable(false);
		userLink.setBackground(Constants.BACKPANEL);
		userLink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				openUser();
			}
		});
		Font font = userLink.getFont();
		Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		font = font.deriveFont(map);
		userLink.setFont(font);
		add(userLink);
	}
	
	// Creates the buttons
	private void createButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setBounds(10, 240, 250, 30);
		buttonPanel.setBackground(Constants.BACKPANEL);
		
		// Creating the renew button
		JButton renewButton = new JButton("Renew");
		renewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				renewLoan();
			}
		});
		renewButton.setBounds(0, 0, 120, 30);
		renewButton.setVisible(sessionManager.getUser() instanceof StaffMember
				&& loan.getCheckInDate() == null);
		buttonPanel.setLayout(null);

		buttonPanel.add(renewButton);
		
		// Creating the check in button
		JButton checkInButton = new JButton("Check In");
		checkInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				checkIn();
			}
		});
		checkInButton.setBounds(130, 0, 120, 30);
		checkInButton.setVisible(sessionManager.getUser() instanceof StaffMember);
		buttonPanel.add(checkInButton);
		add(buttonPanel);
	}
	
	// opens the user
	private void openUser() {
		User user = UserManager.getUser(loan.getUserID());
		UserViewer viewer = new UserViewer(user);
		mainWindow.addTab(viewer.toString(), viewer);
	}
	
	// opens the resource copy
	private void openResourceCopy() {
		ResourceCopy resourceCopy = ResourceCopyManager.getResourceCopy(loan.getResourceCopy());
		ResourceCopyViewer viewer = new ResourceCopyViewer(resourceCopy);
		mainWindow.addTab(viewer.toString(), viewer);
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
		
		buttonPanel.setVisible(false);
		
	}
	
	@Override
	public String toString() {
		return "Loan: " + loan.getID();
	}
	
}
