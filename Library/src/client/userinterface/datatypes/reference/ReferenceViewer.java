package client.userinterface.datatypes.reference;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import util.Constants;
import util.Utilities;
import client.control.data.ReferenceManager;
import client.control.data.ResourceCopyManager;
import client.control.data.UserManager;
import client.control.data.entity.resource.Reference;
import client.control.data.entity.resource.ResourceCopy;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;
import client.userinterface.datatypes.resourcecopy.ResourceCopyViewer;
import client.userinterface.datatypes.user.UserViewer;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a viewer for viewing individual reference's information.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ReferenceViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();

	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();
	
	// The reference entity
	private Reference reference = null;
	
	// Button panel
	private JPanel buttonPanel;
	
	
	/**
	 * Construct a new ReferenceViewer.
	 * 
	 * @param todo
	 * 		The reference entity to view/modify
	 */
	public ReferenceViewer(Reference reference) {
		
		this.reference = reference;
		
		// Create the GUI
		setVisible(true);
		setLayout(null);
		setBackground(Constants.BACKPANEL);
		createIDText();
		createStartDateText();
		createEndDateText();
		createResourceCopyLink();
		createUserLink();
		createButtonPanel();
		
	}
	
	private void createIDText() {
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("Reference ID:");
		textPane.setBounds(10, 30, 100, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		add(textPane);
		
		JTextPane idText = new JTextPane();
		idText.setBackground(Constants.BACKPANEL);
		idText.setText(String.valueOf(reference.getID()));
		idText.setBounds(110, 30, 100, 20);
		idText.setEditable(false);
		idText.setBackground(Constants.BACKPANEL);
		add(idText);
	}
	
	private void createStartDateText() {
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("Start Date:");
		textPane.setBounds(10, 60, 100, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		add(textPane);
		
		JTextPane startDateText = new JTextPane();
		startDateText.setBackground(Constants.BACKPANEL);
		startDateText.setText(reference.getStartDate() == null ? ""
				: Utilities.formatDate(reference.getStartDate(),
						Constants.DEFAULT_DATE_FORMAT,
						Constants.DEFAULT_TIME_ZONE));
		startDateText.setBounds(110, 60, 100, 20);
		startDateText.setEditable(false);
		startDateText.setBackground(Constants.BACKPANEL);
		add(startDateText);
	}
	
	private void createEndDateText() {
		JTextPane textPane = new JTextPane();
		textPane.setBackground(Constants.BACKPANEL);
		textPane.setText("End Date:");
		textPane.setBounds(10, 90, 100, 20);
		textPane.setEditable(false);
		textPane.setFocusable(false);
		add(textPane);
		
		JTextPane endDateText = new JTextPane();
		endDateText.setBackground(Constants.BACKPANEL);
		endDateText.setText(reference.getEndDate() == null ? ""
				: Utilities.formatDate(reference.getEndDate(),
						Constants.DEFAULT_DATE_FORMAT,
						Constants.DEFAULT_TIME_ZONE));
		endDateText.setBounds(110, 90, 100, 20);
		endDateText.setEditable(false);
		endDateText.setBackground(Constants.BACKPANEL);
		add(endDateText);
	}
	
	private void createResourceCopyLink() {
		JTextPane resourceCopyLink = new JTextPane();
		resourceCopyLink.setForeground(new Color(0, 0, 153));
		resourceCopyLink.setBackground(Constants.BACKPANEL);
		resourceCopyLink.setText("Click here to open the associated resource copy");
		resourceCopyLink.setBounds(10, 120, 300, 20);
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
		userLink.setBounds(10, 150, 300, 20);
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
		buttonPanel.setBounds(10, 180, 270, 30);
		buttonPanel.setBackground(Constants.BACKPANEL);
		buttonPanel.setLayout(null);
		
		// Creating the check in button
		JButton checkInButton = new JButton("Remove Reference");
		checkInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				removeReference();
			}
		});
		checkInButton.setBounds(10, 0, 200, 30);
		checkInButton.setVisible(sessionManager.getUser() instanceof StaffMember);
		buttonPanel.add(checkInButton);
		add(buttonPanel);
	}
	
	// opens the user
	private void openUser() {
		User user = UserManager.getUser(reference.getUserID());
		UserViewer viewer = new UserViewer(user);
		mainWindow.addTab(viewer.toString(), viewer);
	}
	
	// opens the resource copy
	private void openResourceCopy() {
		ResourceCopy resourceCopy = ResourceCopyManager.getResourceCopy(reference.getResourceCopy());
		ResourceCopyViewer viewer = new ResourceCopyViewer(resourceCopy);
		mainWindow.addTab(viewer.toString(), viewer);
	}
	
	// removes the resource copy
	private void removeReference() {
		
		if (!ReferenceManager.removeReference(reference.getID()))
			return;
		
		reference = null;
		
		mainWindow.removeTab(mainWindow.getTabIndex());
		
	}
	
	@Override
	public String toString() {
		return "Reference: " + reference.getID();
	}
	
}
