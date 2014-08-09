package client.userinterface.datatypes.resourcecopy;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Constants;

import client.control.data.entity.resource.ResourceCopy;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;

/**
 * Creates a viewer to view information on a particular resource copy.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceCopyViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();;
	
	// The resource copy entity
	private ResourceCopy resourceCopy;
	
	// The tabbed pane for the different tabs
	private JTabbedPane tabbedPane = null;
	
	private ResourceCopyInformationTab resourceCopyInformationTab;
	private ResourceCopyHistoryTab resourceCopyHistoryTab;
	
	/**
	 * Construct a new ResourceCopyViewer.
	 * 
	 * @param resourceCopy
	 * 		The resourceCopy entity to view
	 */
	public ResourceCopyViewer(ResourceCopy resourceCopy) {
		
		this.resourceCopy = resourceCopy;
		
		// Create the GUI
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		init();
	}
	
	private void init() {
		
		// create the tabs
		setBackground(Constants.BACKPANEL);
		resourceCopyInformationTab = new ResourceCopyInformationTab(resourceCopy);
		resourceCopyHistoryTab = new ResourceCopyHistoryTab(resourceCopy);
		
		// the tabbed pane
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBackground(Constants.BACKPANEL);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				int index = tabbedPane.getSelectedIndex();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(resourceCopyInformationTab.toString()))
					resourceCopyInformationTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(resourceCopyHistoryTab.toString()))
					resourceCopyHistoryTab.start();
			}
		});
		add(tabbedPane);
		
		// add tabs based on user mode
		User user = sessionManager.getUser();
		
		if (user instanceof StaffMember) {
			
			// add the ResourceCopyInformationTab
			tabbedPane.add(resourceCopyInformationTab.toString(),
					resourceCopyInformationTab);
			
			// add the ResourceCopyHistoryTab
			tabbedPane.add(resourceCopyHistoryTab.toString(), resourceCopyHistoryTab);
			
		}
		
		else {
			
			// add the ResourceCopyInformationTab
			tabbedPane.add(resourceCopyInformationTab.toString(),
					resourceCopyInformationTab);
			
		}
	}
	
	@Override
	public String toString() {
		return "Copy: " + resourceCopy.getID();
	}

}
