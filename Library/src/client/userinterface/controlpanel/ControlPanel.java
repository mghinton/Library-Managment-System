package client.userinterface.controlpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import client.control.configurationmanagement.Configuration;
import client.control.configurationmanagement.ConfigurationManager;

/**
 * Creates the panel that holds the control panel operations
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ControlPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	// The configuration entity
	private Configuration configuration;
	
	private JTextField ipAddressInputField;
	private JTextField portNumberInputField;
	JButton saveButton;
	
	public ControlPanel() {
		init();
	}

	// Create the GUI
	private void init() {
		setBackground(Constants.BACKPANEL);
		configuration = ConfigurationManager.getConfiguration();
		setLayout(null);
		createIPAddressInput();
		createPortNumberInput();
		createSaveButton();
	}
	
	// Creates the ip address input
	private void createIPAddressInput() {
		
		// Creating the text pane that says "IP Address:"
		JTextPane ipAddressTextPane = new JTextPane();
		ipAddressTextPane.setBackground(Constants.BACKPANEL);
		ipAddressTextPane.setText("IP Address:");
		ipAddressTextPane.setBounds(40, 30, 100, 20);
		ipAddressTextPane.setEditable(false);
		ipAddressTextPane.setFocusable(false);
		add(ipAddressTextPane);

		// Creating the input text field
		ipAddressInputField = new JTextField();
		ipAddressInputField.setBounds(140, 30, 180, 20);
		add(ipAddressInputField);
		ipAddressInputField.setColumns(8);
		
		// Setting the first name to the current value
		if (configuration != null) {
			String ipAddress = String.valueOf(configuration.getIPAddress());
			ipAddressInputField.setText(ipAddress);
		}
		
	}
	
	// Creates the port number input
	private void createPortNumberInput() {
		
		// Creating the text pane that says "Port Number:"
		JTextPane portNumberTextPane = new JTextPane();
		portNumberTextPane.setBackground(Constants.BACKPANEL);
		portNumberTextPane.setText("Port Number:");
		portNumberTextPane.setBounds(40, 60, 100, 20);
		portNumberTextPane.setEditable(false);
		portNumberTextPane.setFocusable(false);
		add(portNumberTextPane);

		// Creating the input text field
		portNumberInputField = new JTextField();
		portNumberInputField.setBounds(140, 60, 180, 20);
		add(portNumberInputField);
		portNumberInputField.setColumns(8);
		
		// Setting the first name to the current value
		if (configuration != null) {
			String portNumber = String.valueOf(configuration.getPortNumber());
			portNumberInputField.setText(portNumber);
		}		
	}

	// Creates the Save button
	private void createSaveButton() {
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				save();
			}
		});
		saveButton.setBounds(40, 90, 78, 30);
		add(saveButton);
	}
	
	boolean save() {
		
		// The information fields
		String ipAddress = ipAddressInputField.getText();
		String portNumber = portNumberInputField.getText();
		
		// Check if any of the fields are empty
		if ((ipAddress == null || ipAddress.length() <= 0)
				|| (portNumber == null || portNumber.length() <= 0)) {
			JOptionPane.showMessageDialog(null,
				    "None of the fields can be empty.",
				    "Save Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		int port = Integer.parseInt(portNumber);
		
		Configuration newConfiguration = new Configuration(ipAddress, port);
		
		ConfigurationManager.setConfiguration(newConfiguration);
		
		configuration = newConfiguration;
		return true;
	}
	
	@Override
	public String toString() {
		return "Control Panel";
	}

}
