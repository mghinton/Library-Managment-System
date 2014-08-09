package client.userinterface.datatypes.user;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Constants;

import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.Patron;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;

/**
 * Populates the information for the reserves tab
 * @author Daniel
 * @version 2
 *
 */
public class UserViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();;
	
	// The user entity
	private User user;
	
	// The tabbed pane for the different tabs
	private JTabbedPane tabbedPane = null;
	
	// Indicates that a new User entity is being created
	// This is set to true if the user parameter in the constructor is null
	private boolean newUser = false;
	
	private UserInformationTab userInformationTab;
	private FinesTab finesTab;
	private UserHistoryTab userHistoryTab;
	private ReservesTab reservesTab;
	private ReferencesTab referencesTab;
	private CheckOutTab checkOutTab;
	
	/**
	 * Construct a new UserViewer.
	 * 
	 * @param user
	 * 		The user entity to view/modify, or null if a new User is to be
	 * 		created.
	 */
	public UserViewer(User user) {
		
		this.user = user;
		
		// Check if the user parameter is null
		// If so, then a new User entity is being created
		if (this.user == null)
			newUser = true;
		
		// Create the GUI
		setBackground(Constants.BACKPANEL);
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		if (newUser == true)
			initNewUser();
		else
			initModUser();
	}
	
	// creates the user viewer for creating a new user
	private void initNewUser() {
		
		// add the user information tab
		userInformationTab = new UserInformationTab(null);
		add(userInformationTab);
		userInformationTab.start();
		
	}
	
	// creates the user viewer for modifying an existing user
	private void initModUser() {
		
		// create the tabs
		userInformationTab = new UserInformationTab(user);
		finesTab = new FinesTab(user);
		userHistoryTab = new UserHistoryTab(user);
		reservesTab = new ReservesTab(user);
		referencesTab = new ReferencesTab(user);
		checkOutTab = new CheckOutTab(user);
		
		// create the tabbed pane
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBackground(Constants.BACKPANEL);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				int index = tabbedPane.getSelectedIndex();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(userInformationTab.toString()))
					userInformationTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(finesTab.toString()))
					finesTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(userHistoryTab.toString()))
					userHistoryTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(reservesTab.toString()))
					reservesTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(referencesTab.toString()))
					referencesTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(checkOutTab.toString()))
					checkOutTab.start();
			}
		});
		add(tabbedPane);
		
		// Add tabs based on user mode
		User loggedInUser = sessionManager.getUser();
		
		// Staff mode
		if (loggedInUser instanceof StaffMember) {
			
			// add the check out tab
			tabbedPane.add(checkOutTab.toString(), checkOutTab);
			
			// add the user information tab
			tabbedPane.add(userInformationTab.toString(), userInformationTab);
			
			// add the fines tab
			tabbedPane.add(finesTab.toString(), finesTab);
			
			// add the history tab
			tabbedPane.add(userHistoryTab.toString(), userHistoryTab);
			
			// add the reserves tab
			tabbedPane.add(reservesTab.toString(), reservesTab);
			
			// if the user to be viewed is a faculty or staff member, then add the references tab
			if (user instanceof Faculty || user instanceof StaffMember)
				tabbedPane.add(referencesTab.toString(), referencesTab);
			
		}
		
		// patron mode
		if (loggedInUser instanceof Patron) {
			
			// add the user information tab
			tabbedPane.add(userInformationTab.toString(), userInformationTab);
			
			// add the fines tab
			tabbedPane.add(finesTab.toString(), finesTab);
			
			// add the history tab
			tabbedPane.add(userHistoryTab.toString(), userHistoryTab);
			
			// add the reserves tab
			tabbedPane.add(reservesTab.toString(), reservesTab);
			
			// if the user is a faculty member, then add the references tab
			if (user instanceof Faculty)
				tabbedPane.add(referencesTab.toString(), referencesTab);
			
		}
		
	}
	
	@Override
	public String toString() {
		if (newUser)
			return "New User";
		return user.getFirstName() + " " + user.getLastName();
	}

}
