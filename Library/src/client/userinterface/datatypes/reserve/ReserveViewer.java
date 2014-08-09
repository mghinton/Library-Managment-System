package client.userinterface.datatypes.reserve;

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
import client.control.data.ReserveManager;
import client.control.data.ResourceManager;
import client.control.data.UserManager;
import client.control.data.entity.resource.Reserve;
import client.control.data.entity.resource.Resource;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;
import client.userinterface.datatypes.resource.ResourceViewer;
import client.userinterface.datatypes.user.UserViewer;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a viewer for viewing a particular reserve's information.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ReserveViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reserve to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();

	// A reserve to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();
	
	// The reserve entity
	private Reserve reserve = null;
	
	// Button panel
	private JPanel buttonPanel;
	
	
	/**
	 * Construct a new ReserveViewer.
	 * 
	 * @param todo
	 * 		The reserve entity to view/modify
	 */
	public ReserveViewer(Reserve reserve) {
		
		this.reserve = reserve;
		
		// Create the GUI
		setVisible(true);
		setLayout(null);
		setBackground(Constants.BACKPANEL);
		createIDText();
		createReservationDateText();
		createAvailableDateText();
		createEndDateText();
		createResourceLink();
		createUserLink();
		createButtonPanel();
		
	}
	
	private void createIDText() {
		JTextPane idText = new JTextPane();
		idText.setBackground(Constants.BACKPANEL);
		idText.setText("Reserve ID: " + String.valueOf(reserve.getID()));
		idText.setBounds(10, 30, 100, 20);
		idText.setEditable(false);
		idText.setBackground(Constants.BACKPANEL);
		add(idText);
	}
	
	private void createReservationDateText() {
		JTextPane reservationDateText = new JTextPane();
		reservationDateText.setBackground(Constants.BACKPANEL);
		reservationDateText.setText("Reservation Date: " + Utilities.formatDate(
				reserve.getReservationDate(), Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE));
		reservationDateText.setBounds(10, 60, 100, 20);
		reservationDateText.setEditable(false);
		reservationDateText.setBackground(Constants.BACKPANEL);
		add(reservationDateText);
	}
	
	private void createAvailableDateText() {
		JTextPane availableDateText = new JTextPane();
		availableDateText.setBackground(Constants.BACKPANEL);
		availableDateText.setText("Available Date: " + Utilities.formatDate(
				reserve.getAvailableDate(), Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE));
		availableDateText.setBounds(10, 90, 100, 20);
		availableDateText.setEditable(false);
		availableDateText.setBackground(Constants.BACKPANEL);
		add(availableDateText);
	}
	
	private void createEndDateText() {
		JTextPane endDateText = new JTextPane();
		endDateText.setBackground(Constants.BACKPANEL);
		endDateText.setText("End Date: " + Utilities.formatDate(
				reserve.getEndDate(), Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE));
		endDateText.setBounds(10, 120, 100, 20);
		endDateText.setEditable(false);
		endDateText.setBackground(Constants.BACKPANEL);
		add(endDateText);
	}
	
	private void createResourceLink() {
		JTextPane resourceLink = new JTextPane();
		resourceLink.setForeground(new Color(0, 0, 153));
		resourceLink.setBackground(Constants.BACKPANEL);
		resourceLink.setText("Click here to open the associated resource");
		resourceLink.setBounds(10, 150, 200, 20);
		resourceLink.setEditable(false);
		resourceLink.setBackground(Constants.BACKPANEL);
		resourceLink.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				openResource();
			}
		});
		Font font = resourceLink.getFont();
		Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		font = font.deriveFont(map);
		resourceLink.setFont(font);
		add(resourceLink);
	}
	
	// Creates the Expiration date input stuff
	private void createUserLink() {
		JTextPane userLink = new JTextPane();
		userLink.setForeground(new Color(0, 0, 153));
		userLink.setBackground(Constants.BACKPANEL);
		userLink.setText("Click here to open the associated user");
		userLink.setBounds(10, 180, 200, 20);
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
		buttonPanel.setBounds(10, 210, 130, 30);
		buttonPanel.setBackground(Constants.BACKPANEL);

		JButton removeButton = new JButton("Remove Reserve");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				removeReserve();
			}
		});
		removeButton.setBounds(0, 0, 130, 30);
		removeButton.setVisible(sessionManager.getUser() instanceof StaffMember);
		buttonPanel.add(removeButton);
		add(buttonPanel);
	}
	
	// opens the user
	private void openUser() {
		User user = UserManager.getUser(reserve.getUserID());
		UserViewer viewer = new UserViewer(user);
		mainWindow.addTab(viewer.toString(), viewer);
	}
	
	// opens the resource
	private void openResource() {
		Resource resource = ResourceManager.getResource(reserve.getResource());
		ResourceViewer viewer = new ResourceViewer(resource);
		mainWindow.addTab(viewer.toString(), viewer);
	}
	
	// removes the resource copy
	private void removeReserve() {
		
		if (!ReserveManager.removeReserve(reserve.getID()))
			return;
		
		reserve = null;
		
	}
	
	@Override
	public String toString() {
		return "Reserve: " + reserve.getID();
	}
	
}
