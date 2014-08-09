package client.userinterface.datatypes.subscriptions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import util.Constants;
import client.control.data.SubscriptionManager;
import client.control.data.entity.management.Subscription;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a panel to hold the subscription list
 * @author Matthew
 * @version 3
 *
 */
public class SubscriptionListPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();
	
	private ErrorPanel errorPanel;
	private Subscription[] subscriptionArray;
	
	private JButton addButton;
	private JButton removeButton;
	private JPanel buttonPanel;
	private SubscriptionList list = null;
	
	public SubscriptionListPanel() {
		super();

		// get necessary data from server
		if (getData())
			init();
		else
			displayError(true);
	}
	
	private void init()
	{
		// Create GUI
		setVisible(true);
		setBackground(Constants.BACKPANEL);
		setLayout(new BorderLayout(0, 0));
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		createButtonPanel();
		createList();
	}

	private void createList() {
		list = new SubscriptionList();
		add(list, BorderLayout.SOUTH);
		ArrayList<Object> items = new ArrayList<Object>(Arrays.asList(subscriptionArray));
		list.setItems(items);
	}
	
	// Creates the button panel
	private void createButtonPanel() {
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new GridLayout(1, 11, 0, 0));
		buttonPanel.setBackground(Constants.BACKPANEL);
		
		createAddButton();
		createRemoveButton();
	}

	// Creates the add button
	private void createAddButton() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				addItem();
			}
		});
		panel.add(addButton);
		buttonPanel.add(panel);
	}

	// Creates the remove button
	private void createRemoveButton() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Constants.BACKPANEL);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Subscription selectedSubscription = (Subscription) list.getSelected();
				if (selectedSubscription == null) {
					JOptionPane.showMessageDialog(mainWindow,
						    "Please select a subscription from the list before clicking remove",
						    "No Subscription Selected",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(SubscriptionManager.removeSubscription(selectedSubscription.getID()))
				{
					JOptionPane.showMessageDialog(mainWindow,
						    "Subscription Removed",
						    "Remove Sucessful",
						    JOptionPane.INFORMATION_MESSAGE);
					removeItem(selectedSubscription);
					updateUI();
				}
				else
				{
					JOptionPane.showMessageDialog(mainWindow,
						    "An error has occured when removing Subscription",
						    "Remove Unsucessful",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.add(removeButton);
		buttonPanel.add(panel);
	}
	
	public void addItem()
	{
		SubscriptionViewer viewer = new SubscriptionViewer(null);
		MainWindow main = MainWindow.getReference();
		main.addTab("new subscription", viewer);
	}
	
	public void removeItem(Subscription sub)
	{
		ArrayList<Object> subscriptions = list.getItems();
		subscriptions.remove(sub);
		//selectedTodo = ToDoManager.removeToDo(selectedTodo.getID());
		if (sub == null) {
			JOptionPane.showMessageDialog(mainWindow,
				    "Error",
				    "Unable to remove subscription at this time",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		list.setItems(subscriptions);
	}
	
	// Displays an error explaining that the necessary data could not
		// be retrieved
		private void displayError(boolean visible) {
			setVisible(true);
			setLayout(new BorderLayout(0, 0));
			if (errorPanel == null) {
				errorPanel = new ErrorPanel();
				errorPanel.addActionListenerToButton(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						if (getData()) {
							displayError(false);
							init();
						}
					}
				});
			}
			if (visible){
				if (!this.isAncestorOf(errorPanel))
					add(errorPanel);
			}
			else {
				if (this.isAncestorOf(errorPanel))
					remove(errorPanel);
			}
			
		}
		
		// Gets the required data from the server
		private boolean getData() {
			
			if (subscriptionArray == null) {
				Subscription searchKey = new Subscription(-1, null, null, -1, null);
				subscriptionArray = SubscriptionManager.getSubscription(searchKey);
				if (subscriptionArray == null)
					return false;
			}
			return true;
			
		}
		
		@Override
		public String toString() {
			return "Subscription List";
		}

}
