package client.userinterface.datatypes;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import util.Constants;

/**
 * Creates the error panel for handling all errors.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ErrorPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JButton button;
	
	/**
	 * Create the panel.
	 */
	public ErrorPanel() {
		super();
		
		// Create GUI
		setBackground(Constants.BACKPANEL);
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		createTitle();
		createButton();
	}
	
	/**
	 * Adds an action listener to the button
	 * @param listener the action listener to add
	 */
	public void addActionListenerToButton(ActionListener listener) {
		button.addActionListener(listener);
	}
	
	/**
	 * Removes an action listener from the button
	 * @param listener the action listener to remove
	 */
	public void removeActionListenerFromButton(ActionListener listener) {
		button.removeActionListener(listener);
	}
	
	private void createTitle() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Constants.BACKPANEL);
		
		JTextPane title = new JTextPane();
		title.setFocusable(false);
		title.setEditable(false);
		title.setText("Content Cannot Be Displayed");
		title.setBackground(Constants.BACKPANEL);
		panel.add(title);
		add(panel, BorderLayout.NORTH);
	}
	
	private void createButton() {
		
		JPanel panel = new JPanel();
		panel.setBackground(Constants.BACKPANEL);
		
		button = new JButton();
		button.setFocusable(true);
		button.setText("Retry");
		panel.add(button);
		add(panel, BorderLayout.CENTER);
	}

}
