package client.userinterface.mainwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.Constants;
import client.control.session.SessionManager;
import client.userinterface.login.LoginDialog;

/**
 * The start panel.  At any time, either this panel or the UserMode panel is
 * visible.
 * 
 * @author Daniel Huettner, Matthew Hinton
 * @version 0
 */
public class StartMode extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow = null;
	private SessionManager sessionManager = null;

	/**
	 * Create the panel.
	 * @param mainWindow
	 * 		A reference to the MainWindow class
	 */
	public StartMode(MainWindow mainWindow) {
		super();
		
		this.mainWindow = mainWindow;
		sessionManager = SessionManager.getReference();
		
		setVisible(true);
		setBackground(new Color(204, 51, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		setOpaque(true);
		
		JPanel logoPanel = new JPanel();
		add(logoPanel, BorderLayout.NORTH);
		logoPanel.setLayout(new BorderLayout(0, 0));
		logoPanel.setBackground(Constants.BACKPANEL);
		logoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JLabel logoLabel = createLogoLabel();
		logoPanel.add(logoLabel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(204, 51, 0));
		add(buttonPanel, BorderLayout.CENTER);
		buttonPanel.setLayout(null);
		
		JButton loginButton = createLoginButton();
		buttonPanel.add(loginButton);
		
		JButton guestButton = createGuestButton();
		buttonPanel.add(guestButton);
		
		JButton exitButton = createExitButton();
		buttonPanel.add(exitButton);
		
		JLabel loginLabel = createLoginLabel();
		buttonPanel.add(loginLabel);
		
		JLabel guestLabel = createGuestLabel();
		buttonPanel.add(guestLabel);
		
	}
	
	private JButton createLoginButton() {
		JButton button = new JButton(Constants.LOGIN_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoginDialog loginDialog = new LoginDialog();
				loginDialog.setVisible(true);
			}
		});
		button.setBounds(310, 174, 128, 128);
		return button;
	}
	
	private JButton createGuestButton() {
		JButton button = new JButton(Constants.GUEST_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainWindow.updateMode(true);
			}
		});
		button.setBounds(510, 174, 128, 128);
		return button;
	}
	
	private JButton createExitButton() {
		JButton button = new JButton("Exit");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sessionManager.logout();
				System.exit(0);
			}
		});
		button.setBounds(797, 430, 89, 23);
		return button;
	}
	
	private JLabel createLoginLabel() {
		JLabel label = new JLabel("Login");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setBounds(359, 313, 49, 23);
		return label;
	}
	
	private JLabel createGuestLabel() {
		JLabel label = new JLabel("Guest");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setBounds(555, 313, 49, 23);
		return label;
	}
	
	private JLabel createLogoLabel() {
		JLabel label = new JLabel("");
		label.setIcon(Constants.LOGO_LARGE);
		return label;
	}
	
}
