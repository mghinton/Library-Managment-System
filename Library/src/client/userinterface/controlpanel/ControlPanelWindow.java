package client.userinterface.controlpanel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import util.Constants;

import client.userinterface.mainwindow.MainWindow;

/**
 * Creates a window for using control panel functions.
 * Admin Only
 * @author Daniel Huettner
 * @version 5
 */
public class ControlPanelWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private ControlPanel controlPanel;

	private final int WIDTH = 370;
	private final int HEIGHT = 150;

	/**
	 * Create the frame.
	 */
	public ControlPanelWindow() {
		super();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBackground(Constants.BACKGROUND);
		this.setUndecorated(true);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		setResizable(false);
		setBounds((int) screenSize.getWidth() / 2 - WIDTH / 2,
				(int) screenSize.getHeight() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		controlPanel = new ControlPanel();
		add(controlPanel);
		ActionListener[] listeners = controlPanel.saveButton.getActionListeners();
		for (int i = 0; listeners != null && i < listeners.length; i++)
			controlPanel.saveButton.removeActionListener(listeners[i]);
		controlPanel.saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				save();
			}
		});
		setTitle("Please Enter Server Information");
		setVisible(true);
	}
	
	private void save() {
		if (controlPanel.save()) {
			this.dispose();
			MainWindow mainWindow = MainWindow.getReference();
			mainWindow.updateMode(false);
		}
	}

}
