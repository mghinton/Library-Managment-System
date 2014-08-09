package client.userinterface.datatypes.subscriptions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import util.Utilities;
import client.control.data.SubscriptionManager;
import client.control.data.entity.management.Subscription;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a viewer to view single subscription information
 * @author Matthew
 * @version 3
 *
 */
public class SubscriptionViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();;
	
	// The Subscription entity
	private Subscription subscription = null;
	
	//Labels
	private JLabel lblPhoneNumber = new JLabel("Phone Number:");
	private JLabel lblContactEmail = new JLabel("Contact Email:");	
	private JLabel expirationTextPane1 = new JLabel("Expiration Date:");
	private JLabel titleTextPane = new JLabel("Title:");
	private JLabel idTextPane = new JLabel("ID:");
	private JTextPane expirationDateFormatPane = new JTextPane();
	private JLabel idNumberTextPane = new JLabel();
	private JLabel Formatlabel = new JLabel("5554742424");
	
	// Allows the user to edit sections
	private JTextField expirationDateInputField = null;
	private JTextField titleInputField = null;
	
	// Indicates that a new Subscription entity is being created
	// This is set to true if the Subscription parameter in the constructor is null
	private boolean newSub = false;
	
	private JTextField phoneNumberField = null;
	private JTextField contactEmailField = null;
	
	
	/**
	 * Construct a new SubscriptionViewer.
	 * 
	 * @param todo
	 * 		The Subscription entity to view/modify, or null if a new ToDo is to be
	 * 		created.
	 */
	public SubscriptionViewer(Subscription sub) {
		
		this.subscription = sub;
		
		// Check if the Subscription parameter is null
		// If so, then a new Subscription entity is being created
		if (this.subscription == null) {
			newSub = true;
			this.subscription = createDefaultSubscription();
		}
		
		// Create the GUI
		setVisible(true);
		setLayout(null);
		setBackground(Constants.BACKPANEL);
		createIDTextPane();
		createTitleInput();
		createExpirationDateInput();
		createPhoneInput();
		createEmailInput();
		createSaveButton();
		
	}
	
	private void createIDTextPane() {
		
		idTextPane.setBackground(Constants.BACKPANEL);
		idTextPane.setBounds(40, 30, 44, 20);
		idTextPane.setBackground(Constants.BACKPANEL);
		add(idTextPane);
		
		if (!newSub)
			idNumberTextPane.setText(String.valueOf(subscription.getID()));
		idNumberTextPane.setBounds(140, 30, 180, 20);
		add(idNumberTextPane);
		
	}
	
	private void createTitleInput() {
		
		titleTextPane.setBackground(Constants.BACKPANEL);
		titleTextPane.setBounds(40, 60, 44, 20);
		titleTextPane.setBackground(Constants.BACKPANEL);
		add(titleTextPane);
		
		titleInputField = new JTextField();
		titleInputField.setText(subscription.getTitle());
		titleInputField.setBounds(140, 60, 180, 20);
		add(titleInputField);
		titleInputField.setColumns(8);
		
	}
	
	// Creates the Expiration date input stuff
	private void createExpirationDateInput() {
		
		// Creating the text pane that says "Start Date:"
		expirationTextPane1.setBackground(Constants.BACKPANEL);
		expirationTextPane1.setBounds(40, 90, 110, 20);
		expirationTextPane1.setBackground(Constants.BACKPANEL);
		add(expirationTextPane1);

		// Creating the input text field
		expirationDateInputField = new JTextField();
		expirationDateInputField.setBounds(140, 90, 180, 20);
		add(expirationDateInputField);
		expirationDateInputField.setColumns(8);
		
		// Creating the text pane that says "(MM-DD-YYYY)"
		expirationDateFormatPane.setBackground(Constants.BACKPANEL);
		expirationDateFormatPane.setText("(" + Constants.DEFAULT_DATE_FORMAT.toUpperCase() + ")");
		expirationDateFormatPane.setBounds(330, 90, 91, 20);
		expirationDateFormatPane.setEditable(false);
		add(expirationDateFormatPane);
		
		// Setting the start date to the current value
		if (subscription.getExpirationDate() == null)
			return;
		String startDate = Utilities.formatDate(
				subscription.getExpirationDate(),
				Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE);
		expirationDateInputField.setText(startDate);
		
	}

	private void createPhoneInput()
	{
		lblPhoneNumber.setBounds(40, 120, 110, 20);
		add(lblPhoneNumber);
		
		phoneNumberField = new JTextField();
		phoneNumberField.setBounds(140, 120, 180, 20);
		phoneNumberField.setText(Long.toString(subscription.getContactPhone()));
		add(phoneNumberField);
		phoneNumberField.setColumns(10);
		
		Formatlabel.setBounds(330, 120, 72, 14);
		add(Formatlabel);
	}
	
	private void createEmailInput()
	{
		lblContactEmail.setBounds(40, 150, 110, 20);
		add(lblContactEmail);
		
		contactEmailField = new JTextField();
		contactEmailField.setBounds(140, 150, 180, 20);
		contactEmailField.setText(subscription.getContactEmail());
		add(contactEmailField);
		contactEmailField.setColumns(10);
	}
	// Creates the Save button
	private void createSaveButton() {
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					
					// The information fields
					String title = titleInputField.getText();
					String expirationDateStr = expirationDateInputField.getText();
					String email = contactEmailField.getText();
					long phone = Long.parseLong(phoneNumberField.getText());
					
					// Check if any of the fields are empty
					if ((title == null || title.length() <= 0)
							|| (expirationDateStr == null || expirationDateStr.length() <= 0) 
							|| (phoneNumberField.getText().length() < Formatlabel.getText().length())
							|| (email == null || email.length() <= 0)){
						JOptionPane.showMessageDialog(mainWindow,
							    "None of the fields can be empty.",
							    "Save Error",
							    JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					Date expiryDate = Utilities.parseDate(
							expirationDateStr,
							Constants.DEFAULT_DATE_FORMAT,
							Constants.DEFAULT_TIME_ZONE);		
					
					if (newSub) {
						Subscription newSubscriptionEntity = new Subscription(-1, title, expiryDate, phone, email);
						newSubscriptionEntity = SubscriptionManager.addSubscription(newSubscriptionEntity);
						// check if an error occurred
						if (newSubscriptionEntity == null)
							return;
						subscription = newSubscriptionEntity;
					}
					else {
						Subscription newSubscriptionEntity = new Subscription(subscription.getID(), title, expiryDate, phone, email);
						if (!SubscriptionManager.setSubscription(newSubscriptionEntity))
							return;
						subscription = newSubscriptionEntity;
					}
					
					// update fields
					idNumberTextPane.setText(String.valueOf(subscription.getID()));
					if (subscription.getExpirationDate() != null)
						expirationDateInputField.setText(Utilities.formatDate(
								subscription.getExpirationDate(),
								Constants.DEFAULT_DATE_FORMAT,
								Constants.DEFAULT_TIME_ZONE));
					titleInputField.setText(subscription.getTitle());
					expirationDateInputField.setText(Utilities.formatDate(
							subscription.getExpirationDate(),
							Constants.DEFAULT_DATE_FORMAT,
							Constants.DEFAULT_TIME_ZONE));
					contactEmailField.setText(subscription.getContactEmail());
					phoneNumberField.setText(String.valueOf(subscription.getContactPhone()));
					
				} catch (ParseException parseException) {
					JOptionPane.showMessageDialog(mainWindow,
						    "The date you entered is invalid.\n"
						    + "Make sure the date is formatted properly",
						    "Invalid Date.",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			}});
		button.setBounds(140, 180, 78, 76);
		add(button);
	}
	
	// Creates a default Subscription entity
	private Subscription createDefaultSubscription() {
		
		// The default expiry date is today at 00:00 hours
		Date expire = Utilities.removeTime(new Date());
		
		// Creating the new Subscription
		Subscription sub = new Subscription(-1, "new subscription", expire, 0, "");
		
		return sub;
		
	}
	
	@Override
	public String toString() {
		if (newSub)
			return "New Subscription";
		return subscription.getTitle();
	}
}
