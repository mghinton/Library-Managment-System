 package client.userinterface.mainwindow;

import java.awt.Dimension;
import java.awt.Image;
//import java.util.List;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import util.Constants;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;

/**
 * The main window.
 * 
 * @author Daniel Huettner, Matthew Hinton
 * @version 0
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// singleton reference to this class
	private static MainWindow singleton = null;
	
	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();;
	
	// prevents more than one instance of this class being instantiated
	private static ReentrantLock lock = new ReentrantLock();
	
	// the two different panels that can be displayed
	private StartMode startMode = null;
	private UserMode userMode = null;
	
	/**
	 * Create the frame.
	 */
	private MainWindow() {
		super();
		this.setResizable(false);
		this.setTitle("Libris Library Management System");
		this.setIconImage(Constants.ICON_64.getImage());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(
				(int)(screenSize.getWidth()-Constants.SCREEN_WIDTH)/2, 0,
				Constants.SCREEN_WIDTH,
				Constants.SCREEN_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		startMode = new StartMode(this);
		userMode = new UserMode(this);
		
		ToolTipManager.sharedInstance().setDismissDelay(100000);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);
	}
	
	/**
	 * Returns a reference to this singleton class
	 * @return a reference to this singleton class
	 */
	public static MainWindow getReference() {
		if (singleton == null) {
			lock.lock();
			if (singleton == null)
				singleton = new MainWindow();
			lock.unlock();
		}
		return singleton;
	}
	
	/**
	 * Checks the type of user that is logged in and updates the interface
	 * as necessary
	 */
	public void updateMode(boolean guest) {
		User user = sessionManager.getUser();
		
		// StartMode
		if (!guest && user == null) {
			this.add(startMode);
			this.remove(userMode);
		}
		// UserMode
		else {
			userMode.clear();
			userMode.updateMode();
			this.add(userMode);
			this.remove(startMode);
		}
		validate();
		repaint();
	}
	
	/**
	 * Adds a tab
	 * @param name the title
	 * @param component the item within the tab
	 */
	public void addTab(String name, JComponent component) {
		userMode.addTab(name, component);
	}
	
	/**
	 * Removes the specified tab
	 * @param index the index of the tab to remove
	 */
	public void removeTab(int index) {
		userMode.removeTab(index);
	}
	
	/**
	 * Replaces the current tab
	 * @param name the new title
	 * @param component the new item within the tab
	 */
	public void replaceTab(String name, JComponent component) {
		userMode.replaceTab(name, component);
	}
	
	/**
	 * Replaces the specified tab
	 * @param name the new title
	 * @param component the new item within the tab
	 * @param index the index of the tab to replace
	 */
	public void replaceTab(String name, JComponent component, int index) {
		userMode.replaceTab(name, component, index);
	}
	
	/**
	 * Returns the index of the current tab
	 * @return the index of the currently visible tab, or -1 if there are no tabs
	 */
	public int getTabIndex() {
		return userMode.getTabIndex();
	}
}
