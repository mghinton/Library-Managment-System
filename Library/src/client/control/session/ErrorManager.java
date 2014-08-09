package client.control.session;
import java.net.ConnectException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import client.control.data.entity.user.User;
import client.serverinterface.ServerInterface;
import client.userinterface.mainwindow.MainWindow;

/**
 * This class manages client-side errors
 * 
 * @author Jeremy Lerner
 * @version 0
 */
public class ErrorManager{

	// A reference to the SessionManager class
	private static SessionManager sessionManager = SessionManager.getReference();

	// A reference to the MainWindow frame
	// This is a horrible violation of our layered architecture, but it gets the job done.
	private static MainWindow mainWindow = MainWindow.getReference();;
	
	/**
	 * Displays an error message
	 * 
	 * @param An Exception containing the error message
	 */
	public static void handleError(Exception exception){
		JOptionPane.showMessageDialog(null, exception.getMessage());
	}
	
	/**
	 * Displays a timeout error and asks the user if they want to reconnect to the server
	 * @param e The exception to display error.
	 */
	public static void handleTimeOutError(ConnectException e){
		int choice = JOptionPane.showConfirmDialog(null, e.getMessage() + " Reconnect?", "Disconnected from the Server", JOptionPane.YES_NO_OPTION);
		
		User user = SessionManager.getReference().getUser();
		
		if(choice == JOptionPane.YES_OPTION){
			try {
				ServerInterface.getReference().closeConnection();
			} catch (ConnectException e1) {	}
			try {
				ServerInterface.getReference().connect();
			} catch (ConnectException e1) {	}
			if(user != null){
				boolean loginSuccess = false;
				while(!loginSuccess){
					JPasswordField pwd = new JPasswordField(32);  
				    int pwdChoice = JOptionPane.showConfirmDialog(null, pwd,"Please Re-Enter Your Password",JOptionPane.OK_CANCEL_OPTION);
				    if(pwdChoice == JOptionPane.OK_OPTION)
				    	loginSuccess = SessionManager.getReference().login(user.getID(), new String(pwd.getPassword()));
				    else{
				    	sessionManager.logout();
				    	mainWindow.updateMode(false);
				    	break;
				    }
				}
			}
		}
		
		else{
	    	sessionManager.logout();
	    	mainWindow.updateMode(false);
		}
	}
	
}