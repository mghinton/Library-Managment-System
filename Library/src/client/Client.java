package client;

/**
 * @version 2
 */
import java.net.ConnectException;

import client.control.configurationmanagement.Configuration;
import client.control.configurationmanagement.ConfigurationManager;
import client.control.session.ErrorManager;
import client.serverinterface.ServerInterface;
import client.userinterface.controlpanel.ControlPanelWindow;
import client.userinterface.mainwindow.MainWindow;

public class Client {
	
	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
		Configuration config = ConfigurationManager.getConfiguration();
		if (config == null) {
			new ControlPanelWindow();
		}
		else {
			try {
				ServerInterface.configureServerInterface(config.getIPAddress(), config.getPortNumber());
			} catch (ConnectException e) {
				ErrorManager.handleError(e);
			}
			MainWindow mainWindow = MainWindow.getReference();
			mainWindow.updateMode(false);
		}
	}

}
