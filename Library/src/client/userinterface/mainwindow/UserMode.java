package client.userinterface.mainwindow;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import util.Constants;
import client.control.data.UserManager;
import client.control.data.entity.user.Administrator;
import client.control.data.entity.user.Librarian;
import client.control.data.entity.user.Patron;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;
import client.userinterface.controlpanel.ControlPanel;
import client.userinterface.datatypes.nopatron.NoPatronPanel;
import client.userinterface.datatypes.reports.ReportViewer;
import client.userinterface.datatypes.resource.ResourceListPanel;
import client.userinterface.datatypes.resource.ResourceViewer;
import client.userinterface.datatypes.resourcetype.ResourceTypeListPanel;
import client.userinterface.datatypes.subscriptions.SubscriptionListPanel;
import client.userinterface.datatypes.todo.ToDoListPanel;
import client.userinterface.datatypes.user.ImportUserPanel;
import client.userinterface.datatypes.user.UserListPanel;
import client.userinterface.datatypes.user.UserViewer;

/**
 * The main panel.  At any time, either this panel or the StartMode panel is
 * visible.
 * 
 * @author Daniel Huettner, Matthew Hinton
 * @version 0
 */
public class UserMode extends JPanel {

	private static final long serialVersionUID = 1L;

	// The button toolbar
	private JPanel buttonPanel = null;
	
	// The tabbed pane
	private JPanel tabbedPanePanel = null;
	private JTabbedPane tabbedPane = null;

	// The different buttons
	private JButton logoutButton = null;
	private JButton resourceSearchButton = null;
	private JButton myAccountButton = null;
	private JButton userSearchButton = null;
	private JButton subscriptionButton = null;
	private JButton todoButton = null;
	private JButton reportsButton = null;
	private JButton addResourceButton = null;
	private JButton addUserButton = null;
	private JButton importUsersButton = null;
	private JButton resourceTypeListButton = null;
	private JButton controlPanelButton = null;
	private JButton noPatronButton = null;
	
	// A reference to the session manager
	private SessionManager sessionManager = null;
	
	// A reference to the main window
	private MainWindow mainWindow = null;
	
	// For closing the currently selected tab
	private JButton closeTabButton = null;
	
	// The number of tabs open
	private int tabs = 0;
	
	/**
	 * Create the panel.
	 * @param mainWindow
	 * 		A reference to the MainWindow class
	 */
	public UserMode(MainWindow mainWindow) {

		sessionManager = SessionManager.getReference();
		this.mainWindow = mainWindow;

		setVisible(true);
		setLayout(null);
		setOpaque(true);
		this.setBackground(Constants.BACKGROUND);
		buttonPanel = new JPanel();
		buttonPanel.setBounds(10, 10, Constants.SCREEN_WIDTH - 26, 75);
		buttonPanel.setLayout(null);
		buttonPanel.setVisible(true);
		buttonPanel.setBackground(Constants.BACKPANEL);
		logoutButton = createLogoutButton();
		resourceSearchButton = createResourceSearchButton();
		myAccountButton = createMyAccountButton();
		userSearchButton = createUserSearchButton();
		subscriptionButton = createSubscriptionButton();
		todoButton = createTodoButton();
		reportsButton = createReportsButton();
		addResourceButton = createAddResourceButton();
		addUserButton = createAddUserButton();
		controlPanelButton = createControlPanelButton();
		noPatronButton = createNoPatronButton();
		importUsersButton = createImportUsersButton();
		resourceTypeListButton = createResourceTypeListButton();
		buttonPanel.add(logoutButton);
		buttonPanel.add(resourceSearchButton);
		buttonPanel.add(myAccountButton);
		buttonPanel.add(userSearchButton);
		buttonPanel.add(subscriptionButton);
		buttonPanel.add(todoButton);
		buttonPanel.add(reportsButton);
		buttonPanel.add(addResourceButton);
		buttonPanel.add(addUserButton);
		buttonPanel.add(controlPanelButton);
		buttonPanel.add(noPatronButton);
		buttonPanel.add(importUsersButton);
		buttonPanel.add(resourceTypeListButton);
		add(buttonPanel);

		tabbedPanePanel = new JPanel();
		tabbedPanePanel.setBounds(10, 107, Constants.SCREEN_WIDTH - 26, 507);
		tabbedPanePanel.setLayout(null);
		tabbedPanePanel.setBackground(Constants.BACKPANEL);
		closeTabButton = createCloseTabButton();
		tabbedPanePanel.add(closeTabButton);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Constants.BACKPANEL);
		tabbedPane.setBounds(10, 10,  tabbedPanePanel.getWidth() - 20, tabbedPanePanel.getHeight() - 20);
		tabbedPanePanel.add(tabbedPane);
		add(tabbedPanePanel);
	}

	/**
	 * Checks the type of user that is logged in and updates the interface
	 * as necessary
	 */
	public void updateMode() {
		User user = sessionManager.getUser();
		
		if (user instanceof Patron) {
			logoutButton.setVisible(true);
			resourceSearchButton.setVisible(true);
			myAccountButton.setVisible(true);
			userSearchButton.setVisible(false);
			subscriptionButton.setVisible(false);
			todoButton.setVisible(false);
			reportsButton.setVisible(false);
			addResourceButton.setVisible(false);
			addUserButton.setVisible(false);
			controlPanelButton.setVisible(false);
			noPatronButton.setVisible(false);
			importUsersButton.setVisible(false);
			resourceTypeListButton.setVisible(false);
		}
		
		else if (user instanceof Librarian) {
			logoutButton.setVisible(true);
			resourceSearchButton.setVisible(true);
			myAccountButton.setVisible(true);
			userSearchButton.setVisible(true);
			subscriptionButton.setVisible(true);
			todoButton.setVisible(true);
			reportsButton.setVisible(true);
			addResourceButton.setVisible(true);
			addUserButton.setVisible(true);
			controlPanelButton.setVisible(true);
			noPatronButton.setVisible(true);
			importUsersButton.setVisible(false);
			resourceTypeListButton.setVisible(false);
		}
		
		else if (user instanceof Administrator) {
			logoutButton.setVisible(true);
			resourceSearchButton.setVisible(true);
			myAccountButton.setVisible(true);
			userSearchButton.setVisible(true);
			subscriptionButton.setVisible(true);
			todoButton.setVisible(true);
			reportsButton.setVisible(true);
			addResourceButton.setVisible(true);
			addUserButton.setVisible(true);
			controlPanelButton.setVisible(true);
			noPatronButton.setVisible(true);
			importUsersButton.setVisible(true);
			resourceTypeListButton.setVisible(true);
		}
		
		else {
			logoutButton.setVisible(true);
			resourceSearchButton.setVisible(true);
			myAccountButton.setVisible(false);
			userSearchButton.setVisible(false);
			subscriptionButton.setVisible(false);
			todoButton.setVisible(false);
			reportsButton.setVisible(false);
			addResourceButton.setVisible(false);
			addUserButton.setVisible(false);
			controlPanelButton.setVisible(false);
			noPatronButton.setVisible(false);
			importUsersButton.setVisible(false);
			resourceTypeListButton.setVisible(false);
		}
		
	}

	/**
	 * Adds a tab
	 * @param name the title
	 * @param component the item within the tab
	 */
	public void addTab(String name, JComponent component) {
		if (tabs >= Constants.MAXTABS)
			return;
		tabbedPane.addTab(name, component);
		tabs++;
		tabbedPane.setSelectedComponent(component);
		closeTabButton.setEnabled(true);
		closeTabButton.setVisible(true);
	}

	/**
	 * Removes the specified tab
	 * @param index the index of the tab to remove
	 */
	public void removeTab(int i) {
		if (tabs > Constants.MINTABS) {
			tabbedPane.remove(i);
			tabs--;
		}
		if (tabs == Constants.MINTABS) {
			closeTabButton.setEnabled(false);
			closeTabButton.setVisible(false);
		}
	}

	/**
	 * Replaces the current tab
	 * @param name the new title
	 * @param component the new item within the tab
	 */
	public void replaceTab(String name, JComponent component) {
		if (tabs == 0) {
			addTab(name, component);
			return;
		}
		replaceTab(name, component, tabbedPane.getSelectedIndex());
	}

	/**
	 * Replaces the specified tab
	 * @param name the new title
	 * @param component the new item within the tab
	 * @param index the index of the tab to replace
	 */
	public void replaceTab(String name, JComponent component, int index) {
		if (tabs == 0) {
			addTab(name, component);
			return;
		}
		if (index < 0 || index > tabs)
			return;
		tabbedPane.removeTabAt(index);
		tabbedPane.insertTab(name, null, component, name, index);
		tabbedPane.setTitleAt(index, name);
	}
	
	/**
	 * Returns the index of the current tab
	 * @return the index of the currently visible tab, or -1 if there are no tabs
	 */
	public int getTabIndex() {
		return tabbedPane.getSelectedIndex();
	}

	private JButton createLogoutButton() {
		JButton button = new JButton(Constants.LOGOUT_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sessionManager.logout();
				mainWindow.updateMode(false);
			}
		});
		button.setBounds(0, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Logout");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}
	
	private JButton createResourceSearchButton() {
		JButton button = new JButton(Constants.SEARCH_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResourceListPanel panel = new ResourceListPanel();
				mainWindow.addTab(panel.toString(), panel);
			}
		});
		button.setBounds(76, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Search");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}
	
	private JButton createMyAccountButton() {
		JButton button = new JButton(Constants.MY_ACCOUNT_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User me = UserManager.getUser(sessionManager.getUser().getID());
				if (me == null)
					return;
				UserViewer viewer = new UserViewer(me);
				mainWindow.addTab(viewer.toString(), viewer);
			}
		});
		button.setBounds(152, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("My Account");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}
	
	private JButton createUserSearchButton() {
		JButton button = new JButton(Constants.USER_SEARCH_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserListPanel panel = new UserListPanel();
				mainWindow.addTab(panel.toString(), panel);
			}
		});
		button.setBounds(228, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Search Users");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}

	private JButton createSubscriptionButton() {
		// Subscription Icon
		JButton button = new JButton(Constants.SUBSCRIPTION_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SubscriptionListPanel panel = new SubscriptionListPanel();
				mainWindow.addTab(panel.toString(), panel);
			}
		});
		button.setBounds(304, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Subscriptions");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}

	private JButton createReportsButton() {
		// Report Icon
		JButton button = new JButton(Constants.REPORT_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReportViewer viewer = new ReportViewer();
				addTab(viewer.toString(), viewer);
			}
		});
		button.setBounds(380, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Reports");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}

	private JButton createTodoButton() {
		// ToDo Icon
		JButton button = new JButton(Constants.TODO_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ToDoListPanel panel = new ToDoListPanel();
				mainWindow.addTab(panel.toString(), panel);				
			}
		});
		button.setBounds(456, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("ToDos");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}

	private JButton createAddResourceButton() {
		// Adding Resource Icon
		JButton button = new JButton(Constants.ADD_RESOURCE_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResourceViewer panel = new ResourceViewer(null);
				mainWindow.addTab(panel.toString(), panel);
			}
		});
		button.setBounds(532, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Add Resource");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}

	private JButton createAddUserButton() {
		// Adding User Icon
		JButton button = new JButton(Constants.ADD_USER_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserViewer panel = new UserViewer(null);
				mainWindow.addTab(panel.toString(), panel);
			}
		});
		button.setBounds(608, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Add User");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}
	
	private JButton createControlPanelButton() {
		JButton button = new JButton(Constants.CONTROL_PANEL_LIST_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ControlPanel panel = new ControlPanel();
				mainWindow.addTab(panel.toString(), panel);
			}
		});
		button.setBounds(684, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Control Panel");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}
	
	private JButton createNoPatronButton() {
		JButton button = new JButton(Constants.NO_PATRON_LIST_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NoPatronPanel panel = new NoPatronPanel();
				mainWindow.addTab(panel.toString(), panel);
			}
		});
		button.setBounds(760, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("No Patron Panel");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}

	private JButton createImportUsersButton() {
		// Adding User Icon
		JButton button = new JButton(Constants.IMPORT_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImportUserPanel panel = new ImportUserPanel();
				mainWindow.addTab("Import Users", panel);
			}
		});
		button.setBounds(836, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Import Users");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}
	
	private JButton createResourceTypeListButton() {
		JButton button = new JButton(Constants.RESOURCE_TYPE_LIST_ICON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResourceTypeListPanel panel = new ResourceTypeListPanel();
				mainWindow.addTab(panel.toString(), panel);
			}
		});
		button.setBounds(912, 0, 78, 76);
		button.setOpaque(false);
		button.setToolTipText("Resource Types");
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		return button;
	}
	

	private JButton createCloseTabButton() {
		JButton button = new JButton("X");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeTab(tabbedPane.getSelectedIndex());
			}
		});
		button.setBounds(928, 8, 52, 23);
		button.setEnabled(false);
		button.setVisible(false);
		return button;
	}

	public void clear() {
		tabbedPane.removeAll();
		tabs = 0;
		closeTabButton.setEnabled(false);
		closeTabButton.setVisible(false);
	}
}
