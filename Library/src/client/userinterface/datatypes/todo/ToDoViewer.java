package client.userinterface.datatypes.todo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.Constants;
import util.Utilities;
import client.control.data.ToDoManager;
import client.control.data.entity.management.ToDo;
import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a viewer to view a single ToDos information
 * @author Matthew
 * @version 3
 *
 */
public class ToDoViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the MainWindow frame
	private MainWindow mainWindow = MainWindow.getReference();
	
	// The ToDo entity
	private ToDo todo = null;
	
	// Allows the user to edit sections
	private JTextPane todoIDTextPane;
	private JTextField startDateInputField = null;
	private JTextField titleInputField = null;
	private JTextField endDateInputField = null;
	private JTextArea descriptionInputField = null;
	private JCheckBox completedCheckBox = null;
	
	// Indicates that a new ToDo entity is being created
	// This is set to true if the todo parameter in the constructor is null
	private boolean newToDo = false;
	
	/**
	 * Construct a new ToDoViewer.
	 * 
	 * @param todo
	 * 		The todo entity to view/modify, or null if a new ToDo is to be
	 * 		created.
	 */
	public ToDoViewer(ToDo todo) {
		
		this.todo = todo;
		
		// Check if the todo parameter is null
		// If so, then a new ToDo entity is being created
		if (this.todo == null) {
			newToDo = true;
			this.todo = createDefaultToDo();
		}
		
		// Create the GUI
		setVisible(true);
		setLayout(null);
		setBackground(Constants.BACKPANEL);
		createIDTextPane();
		createTitleInput();
		createStartDateInput();
		createEndDateInput();
		createDescriptionInput();
		createCompletedInput();
		createSaveButton();
		
	}
	
	// Creates the id textpane
	private void createIDTextPane() {
		
		// Creating the text pane that says "User ID:"
		JTextPane todoIDTextPaneLabel = new JTextPane();
		todoIDTextPaneLabel.setBackground(Constants.BACKPANEL);
		todoIDTextPaneLabel.setText("Todo ID:");
		todoIDTextPaneLabel.setBounds(40, 30, 100, 20);
		todoIDTextPaneLabel.setEditable(false);
		todoIDTextPaneLabel.setFocusable(false);
		add(todoIDTextPaneLabel);
		
		// Creating the actual ID text
		todoIDTextPane = new JTextPane();
		todoIDTextPane.setBackground(Constants.BACKPANEL);
		if (!newToDo)
			todoIDTextPane.setText(String.valueOf(todo.getID()));
		todoIDTextPane.setBounds(140, 30, 100, 20);
		todoIDTextPane.setEditable(false);
		todoIDTextPane.setFocusable(false);
		add(todoIDTextPane);
		
	}
	
	private void createTitleInput() {
		JTextPane titleTextPane = new JTextPane();
		titleTextPane.setBackground(Constants.BACKPANEL);
		titleTextPane.setText("Title:");
		titleTextPane.setBounds(40, 60, 70, 20);
		titleTextPane.setEditable(false);
		titleTextPane.setBackground(Constants.BACKPANEL);
		add(titleTextPane);
		
		titleInputField = new JTextField();
		titleInputField.setText(todo.getTitle());
		titleInputField.setBounds(140, 60, 180, 20);
		add(titleInputField);
		titleInputField.setColumns(8);
		
	}
	
	// Creates the start date input stuff
	private void createStartDateInput() {
		
		// Creating the text pane that says "Start Date:"
		JTextPane startDateTextPane1 = new JTextPane();
		startDateTextPane1.setBackground(Constants.BACKPANEL);
		startDateTextPane1.setText("Start Date:");
		startDateTextPane1.setBounds(40, 90, 70, 20);
		startDateTextPane1.setEditable(false);
		startDateTextPane1.setBackground(Constants.BACKPANEL);
		add(startDateTextPane1);

		// Creating the input text field
		startDateInputField = new JTextField();
		startDateInputField.setBounds(140, 90, 180, 20);
		add(startDateInputField);
		startDateInputField.setColumns(8);
		
		// Creating the text pane that says "(MM-DD-YYYY)"
		JTextPane startDateTextPane2 = new JTextPane();
		startDateTextPane2.setBackground(Constants.BACKPANEL);
		startDateTextPane2.setText("(" + Constants.DEFAULT_DATE_FORMAT.toUpperCase() + ")");
		startDateTextPane2.setBounds(330, 90, 91, 20);
		startDateTextPane2.setEditable(false);
		add(startDateTextPane2);
		
		// Setting the start date to the current value
		if (todo.getStartDate() == null)
			return;
		String startDate = Utilities.formatDate(
				todo.getStartDate(),
				Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE);
		startDateInputField.setText(startDate);
		
	}

	private void createDescriptionInput()
	{
		JTextPane txtpnDescription = new JTextPane();
		txtpnDescription.setText("Description:");
		txtpnDescription.setEditable(false);
		txtpnDescription.setBackground(Constants.BACKPANEL);
		txtpnDescription.setBounds(40, 120, 70, 20);
		add(txtpnDescription);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(140, 120, 356, 124);
		add(scrollPane);
		
		descriptionInputField = new JTextArea();
		scrollPane.setViewportView(descriptionInputField);
		descriptionInputField.setText(todo.getDescription());
		descriptionInputField.setLineWrap(true);
	}
	
	private void createEndDateInput() {
		
		// Creating the text pane that says "Start Date:"
		JTextPane endDateTextPanel = new JTextPane();
		endDateTextPanel.setBackground(Constants.BACKPANEL);
		endDateTextPanel.setText("End Date:");
		endDateTextPanel.setBounds(40, 254, 70, 20);
		endDateTextPanel.setEditable(false);
		add(endDateTextPanel);

		// Creating the input text field
		endDateInputField = new JTextField();
		endDateInputField.setBounds(140, 254, 180, 20);
		endDateInputField.setColumns(8);
		add(endDateInputField);
		
		// Creating the text pane that says "(MM-DD-YYYY)"
		JTextPane startDateTextPane2 = new JTextPane();
		startDateTextPane2.setBackground(Constants.BACKPANEL);
		startDateTextPane2.setText("(" + Constants.DEFAULT_DATE_FORMAT.toUpperCase() + ")");
		startDateTextPane2.setBounds(330, 254, 91, 20);
		startDateTextPane2.setEditable(false);
		add(startDateTextPane2);
		
		// Setting the start date to the current value
		if (todo.getEndDate() == null)
			return;
		String endDate = Utilities.formatDate(
				todo.getEndDate(),
				Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE);
		endDateInputField.setText(endDate);
		
	}

	// creates the completed checkbox
	private void createCompletedInput()
	{
		completedCheckBox = new JCheckBox("Completed", todo.getCompleted());
		completedCheckBox.setBounds(140, 284, 97, 23);
		completedCheckBox.setBackground(Constants.BACKPANEL);
		add(completedCheckBox);
	}
	
	// Creates the Save button
	private void createSaveButton() {
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				save();
			}
		});
		button.setBounds(40, 314, 78, 76);
		add(button);
	}
	
	// creates a new ToDo or modifies the existing ToDo
	private void save() {
		try {
			
			// The information fields
			String title = (String) titleInputField.getText();
			String startDateStr = startDateInputField.getText();
			String endDateStr = endDateInputField.getText();
			String description = descriptionInputField.getText();
			boolean completed = completedCheckBox.isSelected();
			
			// Check if any of the fields are empty
			if ((title == null || title.length() <= 0)
					|| (startDateStr == null || startDateStr.length() <= 0)
					|| (endDateStr == null || endDateStr.length() <= 0)
					|| (description == null || description.length() <= 0)) {
				JOptionPane.showMessageDialog(mainWindow,
					    "None of the fields can be empty.",
					    "Save Error",
					    JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// create the new todo
			Date startDate = Utilities.parseDate(
					startDateStr,
					Constants.DEFAULT_DATE_FORMAT,
					Constants.DEFAULT_TIME_ZONE);	
			startDateInputField.setText(Utilities.formatDate(
					todo.getStartDate(),
					Constants.DEFAULT_DATE_FORMAT,
					Constants.DEFAULT_TIME_ZONE));
			Date endDate = Utilities.parseDate(
					endDateStr,
					Constants.DEFAULT_DATE_FORMAT,
					Constants.DEFAULT_TIME_ZONE);	
			endDateInputField.setText(Utilities.formatDate(
					todo.getEndDate(),
					Constants.DEFAULT_DATE_FORMAT,
					Constants.DEFAULT_TIME_ZONE));
			
			if (newToDo) {
				ToDo newToDoEntity = new ToDo(-1, startDate, endDate, title, description, completed);
				newToDoEntity = ToDoManager.addToDo(newToDoEntity);
				// check if an error occurred
				if (newToDoEntity == null)
					return;
				todo = newToDoEntity;
			}
			else {
				ToDo newToDoEntity = new ToDo(todo.getID(), startDate, endDate, title, description, completed);
				if (!ToDoManager.setToDo(todo))
					return;
				todo = newToDoEntity;
			}
			
			// update fields
			todoIDTextPane.setText(String.valueOf(todo.getID()));
			titleInputField.setText(todo.getTitle());
			if (todo.getStartDate() != null)
				startDateInputField.setText(Utilities.formatDate(todo.getStartDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE));
			if (todo.getEndDate() != null)
				endDateInputField.setText(Utilities.formatDate(todo.getEndDate(),
						Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE));
			descriptionInputField.setText(todo.getDescription());
			completedCheckBox.setSelected(todo.getCompleted());

		} catch (ParseException parseException) {
			JOptionPane.showMessageDialog(mainWindow,
				    "The date you entered is invalid.\n"
				    + "Make sure the date is formatted properly",
				    "Invalid Date.",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	// Creates a default ToDo entity
	private ToDo createDefaultToDo() {
		
		// The default start date is today at 00:00 hours
		// The start date is formatted into a string then parsed so that the
		// time is changed to 00:00 hours.
		Date start = null;
		Date end = null;
		String startDate = Utilities.formatDate(new Date(),
				Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE);
		String endDate = Utilities.formatDate(new Date(),
				Constants.DEFAULT_DATE_FORMAT,
				Constants.DEFAULT_TIME_ZONE);
		try {
			start = (Utilities.parseDate(startDate,
					Constants.DEFAULT_DATE_FORMAT,
					Constants.DEFAULT_TIME_ZONE));
			end = (Utilities.parseDate(endDate,
					Constants.DEFAULT_DATE_FORMAT,
					Constants.DEFAULT_TIME_ZONE));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return todo = new ToDo(-1, start, end, "new todo", "", false);
		
	}
	
	@Override
	public String toString() {
		if (newToDo)
			return "New ToDo";
		return todo.getTitle();
	}

}
