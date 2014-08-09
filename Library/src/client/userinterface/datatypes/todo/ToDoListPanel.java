package client.userinterface.datatypes.todo;

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
import client.control.data.ToDoManager;
import client.control.data.entity.management.ToDo;
import client.userinterface.datatypes.ErrorPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a panel to hold the list of ToDos
 * @author Matthew
 * @version 3
 *
 */
public class ToDoListPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();;
	
	// The todo array
	private ToDo[] todoArray;
	
	private ErrorPanel errorPanel;
	
	private JPanel buttonPanel;
	private ToDoList list = null;
	
	public ToDoListPanel() {
		super();
		
		// get necessary data from server
		if (getData())
			init();
		else
			displayError(true);
	}
	
	// Create the GUI
	private void init() {
		setVisible(true);
		setBackground(Constants.BACKPANEL);
		setLayout(new BorderLayout(0, 0));
		buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.NORTH);
		createButtonPanel();
		createList();
	}
	
	// Gets the required data from the server
	private boolean getData() {
		
		if (todoArray == null) {
			ToDo searchKey = new ToDo(-1, null, null, null, null, true);
			ToDo[] todoArrayCompleted = ToDoManager.getToDo(searchKey);
			if (todoArrayCompleted == null)
				return false;
			searchKey = new ToDo(-1, null, null, null, null, false);
			ToDo[] todoArrayNotCompleted = ToDoManager.getToDo(searchKey);
			if (todoArrayNotCompleted == null)
				return false;
			todoArray = new ToDo[todoArrayCompleted.length + todoArrayNotCompleted.length];
			int i;
			for (i = 0; i < todoArrayCompleted.length; i++)
				todoArray[i] = todoArrayCompleted[i];
			for (; i < todoArrayNotCompleted.length; i++)
				todoArray[i] = todoArrayNotCompleted[i - todoArrayCompleted.length];
		}
		
		return true;
		
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

	private void createList() {
		list = new ToDoList();
		list.setItems(new ArrayList<Object>(Arrays.asList(todoArray)));
		add(list, BorderLayout.SOUTH);
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
		
		JButton addButton = new JButton("Add");
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
		
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ToDo selectedTodo = (ToDo) list.getSelected();
				if (selectedTodo == null) {
					JOptionPane.showMessageDialog(mainWindow,
						    "Please select a todo from the list before clicking remove",
						    "No Todo Selected",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				removeItem(selectedTodo);
			}
		});
		panel.add(removeButton);
		buttonPanel.add(panel);
	}
	
	private void addItem()
	{
		ToDoViewer viewer = new ToDoViewer(null);
		MainWindow main = MainWindow.getReference();
		main.addTab("new todo", viewer);
	}
	
	private void removeItem(ToDo todo)
	{
		ArrayList<Object> todos = list.getItems();
		if (!ToDoManager.removeToDo(todo.getID())) {
			JOptionPane.showMessageDialog(mainWindow,
				    "Error",
				    "Unable to remove todo at this time",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		todos.remove(todo);
		list.setItems(todos);
	}
	
	@Override
	public String toString() {
		return "Todo List";
	}

}
