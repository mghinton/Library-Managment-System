package client.userinterface.login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import util.Constants;
import client.control.session.SessionManager;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates the login panel for logging in
 * @author Daniel
 * @version 2
 *
 */
public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField userIDInputField;
	private JPasswordField passwordInputField;
	private final Action okAction = new OKAction();
	private final Action cancelAction = new CancelAction();
	private MainWindow mainWindow = null;
	private SessionManager sessionManager = null;

	private final int WIDTH = 450;
	private final int HEIGHT = 230;

	LoginDialog dialogReference = null;

	/**
	 * Create the dialog.
	 */
	public LoginDialog() {
		
		mainWindow = MainWindow.getReference();
		sessionManager = SessionManager.getReference();
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		dialogReference = this;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		setResizable(false);
		setBounds((int) screenSize.getWidth() / 2 - WIDTH / 2,
				(int) screenSize.getHeight() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		getContentPane().setLayout(new BorderLayout());
		setTitle("Login");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JTextPane txtpnUserId = new JTextPane();
		txtpnUserId.setBackground(SystemColor.control);
		txtpnUserId.setText("User ID:");
		txtpnUserId.setBounds(90, 60, 70, 20);
		txtpnUserId.setEditable(false);
		txtpnUserId.setFocusable(false);
		contentPanel.add(txtpnUserId);

		userIDInputField = new JTextField();
		userIDInputField.setBounds(170, 60, 180, 20);
		contentPanel.add(userIDInputField);
		userIDInputField.setColumns(10);

		JTextPane txtpnPassword = new JTextPane();
		txtpnPassword.setBackground(SystemColor.control);
		txtpnPassword.setText("Password:");
		txtpnPassword.setBounds(90, 101, 70, 20);
		txtpnPassword.setEditable(false);
		txtpnPassword.setFocusable(false);
		contentPanel.add(txtpnPassword);

		passwordInputField = new JPasswordField();
		passwordInputField.setBounds(170, 101, 180, 20);
		contentPanel.add(passwordInputField);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(10, 11, 70, 30);
		lblNewLabel.setIcon(Constants.LOGO_SMALL);
		contentPanel.add(lblNewLabel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setAction(okAction);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setAction(cancelAction);
				buttonPane.add(cancelButton);
			}
		}
	}

	private class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CancelAction() {
			putValue(NAME, "Cancel");
		}

		public void actionPerformed(ActionEvent event) {
			dialogReference.dispose();
		}

	}

	private class OKAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public OKAction() {
			putValue(NAME, "OK");
		}

		public void actionPerformed(ActionEvent event) {

			String userIDText = userIDInputField.getText();
			if (userIDText == null) {
				JOptionPane.showMessageDialog(null, "Invalid User ID",
						"The UserID field cannot be empty",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			int userID = 0;
			try {
				userID = Integer.parseInt(userIDText);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid User ID",
						"The user ID can only contain digits",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String password = String.copyValueOf(passwordInputField
					.getPassword());
			if (password == null) {
				JOptionPane.showMessageDialog(null, "Invalid Password",
						"The password field cannot be empty",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			login(userID, password);
			dialogReference.dispose();
		}
	}

	private void login(int userID, String password) {
		
		if (sessionManager.login(userID, password))
			mainWindow.updateMode(false);

	}
}