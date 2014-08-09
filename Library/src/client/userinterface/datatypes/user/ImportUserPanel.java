package client.userinterface.datatypes.user;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;

import util.Constants;
import client.control.data.UserManager;
import client.control.data.entity.user.Administrator;
import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.Librarian;
import client.control.data.entity.user.Student;
import client.control.data.entity.user.User;
import client.userinterface.datatypes.ListPanel;
import client.userinterface.mainwindow.MainWindow;

/**
 * WorkSpacePanel Generates the workspace for all interactions with the system
 * 
 * @author Matthew Hinton
 * @version 4
 */
public class ImportUserPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	MainWindow mainWindow = MainWindow.getReference();
	
	private JPanel panel = new JPanel();
	private final JFileChooser fc = new JFileChooser();
	File file = null;
	private ArrayList<Object> newUsers = null;
	private ArrayList<String> displayUsers = null;
	
	private UserListMod list = null;

	public ImportUserPanel() {

		setVisible(true);
		setBounds(10, 11, 944, 485);
		setBackground(Constants.BACKPANEL);
		setLayout(null);

		JLabel lblImportFile = new JLabel("Import File");
		lblImportFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblImportFile.setBounds(62, 47, 105, 20);
		add(lblImportFile);

		list = new UserListMod();
		list.setBounds(220, 88, 550, 332);
		add(list);
		
		JButton btnImport = new JButton("File");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(new FileChooserDemo());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();

					process(file);
					list.setItems(newUsers);
					validate();
					repaint();
				}

			}
		});
		btnImport.setBounds(54, 73, 89, 23);
		add(btnImport);

		JButton btnImport_1 = new JButton("Import");
		btnImport_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (newUsers != null) {
					boolean sucessful = true;
					User dummy = new Student();
					int i = 0;
					while (i < newUsers.size()) {
						dummy = UserManager.addUser((Student)newUsers.get(i));
						if(dummy == null)
						{
							sucessful = false;
						}
						i++;
					}

					if(!sucessful)
					{
						JOptionPane.showMessageDialog(mainWindow,
							    "An Error has occured\nwhen attempting to import.",
							    "Import Unsuceesful",
							    JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(mainWindow,
							    "Users have been imported.",
							    "Import Sucessful",
							    JOptionPane.INFORMATION_MESSAGE);						
					}
					newUsers.clear();
					newUsers = null;
					displayUsers.clear();
					displayUsers = null;
					list.setItems(new ArrayList<Object>());
					validate();
					repaint();
				}
			}
		});
		btnImport_1.setBounds(54, 108, 89, 23);
		add(btnImport_1);

		JLabel lblFile = new JLabel("New Users");
		lblFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFile.setBounds(244, 47, 105, 20);
		add(lblFile);

		list.setItems(new ArrayList<Object>());
	}

	/**
	 * The process function takes a File to import, and returns an array list
	 * with each item seperated
	 * 
	 * @return JPanel
	 */
	public JPanel get() {
		return panel;
	}

	/**
	 * The process function takes a File to import, and returns an array list
	 * with each item seperated
	 * 
	 * @param File
	 *            file
	 * @return ArrayList<String>
	 */
	private void process(File file) {
		newUsers = new ArrayList<Object>();
		displayUsers = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (int i = 0; i < file.length(); i++) {
				String line = br.readLine();
				StringTokenizer st = new StringTokenizer(line, ", ");
				while (st.hasMoreTokens()) {
					int ID = Integer.parseInt(st.nextToken());
					String first = st.nextToken();
					String last = st.nextToken();
					String pass = st.nextToken();
					String email = st.nextToken();
					User newUser = new Student(ID, first, last, pass, email, true);
					newUsers.add(newUser);
					displayUsers.add(Integer.toString(ID));
					displayUsers.add(first);
					displayUsers.add(last);
					displayUsers.add(email);
				}
			}

			br.close();

		} catch (Exception e) {

		}

		return;
	}
	
	
	@Override
	public String toString() {
		return "Import Users";
	}
	public class UserListMod extends ListPanel {
		
		private ArrayList<Object> users = null;

		private static final long serialVersionUID = 1L;

		private MainWindow mainWindow = null;

		public UserListMod() {
			super();
			table.setModel(new UserTableModel());
			table.addMouseListener(new TableMouseAdapter());
			mainWindow = MainWindow.getReference();
			table.setEnabled(false);
		}
		
		@Override
		public ArrayList<Object> getItems() {
			return users;
		}
		
		@Override
		public void setItems(ArrayList<Object> items) {
			((AbstractTableModel) table.getModel()).fireTableDataChanged();
			users = items;
		}

		@Override
		public Object getSelected() {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1)
				return null;
			return users.get(selectedRow);
		}

		private class TableMouseAdapter extends MouseAdapter {

			public void mouseClicked(MouseEvent event) {
				Point clickPoint = event.getPoint();
				int row = table.rowAtPoint(clickPoint);
				if (event.getClickCount() == 2) {
					User user = (User) users.get(row);
					UserViewer viewer = new UserViewer(user);
					mainWindow.addTab(viewer.toString(), viewer);
				}
			}
		}

		private class UserTableModel extends AbstractTableModel {

			private static final long serialVersionUID = 1L;

			// The column names
			private String[] columns = { "Type", "Library ID", "First Name", "Last Name", "Email Address" };

			@Override
			public int getColumnCount() {
				return columns.length;
			}

			@Override
			public int getRowCount() {
				if (users == null)
					return 0;
				return users.size();
			}

			@Override
			public String getColumnName(int column) {
				if (column < 0 || column >= columns.length)
					return new String();
				return columns[column];
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (users == null)
					return null;
				if (rowIndex < 0 || rowIndex >= users.size())
					return null;
				if (columnIndex < 0 || columnIndex >= columns.length)
					return null;
				User user = (User) users.get(rowIndex);
				switch (columnIndex) {
				case 0:
					if (user instanceof Administrator)
						return "Administrator";
					else if (user instanceof Librarian)
						return "Librarian";
					else if (user instanceof Faculty)
						return "Faculty";
					else
						return "Student";
				case 1:
					return user.getID();
				case 2:
					return user.getFirstName();
				case 3:
					return user.getLastName();
				case 4:
					return user.getEmailAddress();
				}
				return user;
			}

		}

	}
	/*
	 * FileChooserDemo.java uses these files:
	 *   images/Open16.gif
	 *   images/Save16.gif
	 */
	public class FileChooserDemo extends JPanel
	                             implements ActionListener {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		static private final String newline = "\n";
	    JButton openButton, saveButton;
	    JTextArea log;
	    JFileChooser fc;

	    public FileChooserDemo() {
	        super(new BorderLayout());

	        //Create the log first, because the action listeners
	        //need to refer to it.
	        log = new JTextArea(5,20);
	        log.setMargin(new Insets(5,5,5,5));
	        log.setEditable(false);
	        JScrollPane logScrollPane = new JScrollPane(log);

	        //Create a file chooser
	        fc = new JFileChooser();

	        //Uncomment one of the following lines to try a different
	        //file selection mode.  The first allows just directories
	        //to be selected (and, at least in the Java look and feel,
	        //shown).  The second allows both files and directories
	        //to be selected.  If you leave these lines commented out,
	        //then the default mode (FILES_ONLY) will be used.
	        //
	        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

	        //Create the open button.  We use the image from the JLF
	        //Graphics Repository (but we extracted it from the jar).
	        openButton = new JButton("Open a File...",
	                                 createImageIcon("images/Open16.gif"));
	        openButton.addActionListener(this);

	        //Create the save button.  We use the image from the JLF
	        //Graphics Repository (but we extracted it from the jar).
	        saveButton = new JButton("Save a File...",
	                                 createImageIcon("images/Save16.gif"));
	        saveButton.addActionListener(this);

	        //For layout purposes, put the buttons in a separate panel
	        JPanel buttonPanel = new JPanel(); //use FlowLayout
	        buttonPanel.add(openButton);
	        buttonPanel.add(saveButton);

	        //Add the buttons and the log to this panel.
	        add(buttonPanel, BorderLayout.PAGE_START);
	        add(logScrollPane, BorderLayout.CENTER);
	    }

	    public void actionPerformed(ActionEvent e) {

	        //Handle open button action.
	        if (e.getSource() == openButton) {
	            int returnVal = fc.showOpenDialog(FileChooserDemo.this);

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would open the file.
	                log.append("Opening: " + file.getName() + "." + newline);
	            } else {
	                log.append("Open command cancelled by user." + newline);
	            }
	            log.setCaretPosition(log.getDocument().getLength());

	        //Handle save button action.
	        } else if (e.getSource() == saveButton) {
	            int returnVal = fc.showSaveDialog(FileChooserDemo.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would save the file.
	                log.append("Saving: " + file.getName() + "." + newline);
	            } else {
	                log.append("Save command cancelled by user." + newline);
	            }
	            log.setCaretPosition(log.getDocument().getLength());
	        }
	    }

	    /** Returns an ImageIcon, or null if the path was invalid. */
	    protected  ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = FileChooserDemo.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            //System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }
	    
	}

}