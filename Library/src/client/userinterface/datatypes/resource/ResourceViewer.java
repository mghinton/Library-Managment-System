package client.userinterface.datatypes.resource;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Constants;

import client.control.data.entity.resource.Resource;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.User;
import client.control.session.SessionManager;

/**
 * Creates a viewer to view a particular resource's information.
 * 
 * @author Daniel Huettner
 * @version 5
 */
public class ResourceViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// A reference to the SessionManager class
	private SessionManager sessionManager = SessionManager.getReference();;
	
	// The resource entity
	private Resource resource;
	
	// The tabbed pane for the different tabs
	private JTabbedPane tabbedPane = null;
	
	// Indicates that a new resource entity is being created
	// This is set to true if the user parameter in the constructor is null
	private boolean newResource = false;
	
	// the tabs
	private ResourceInformationTab resourceInformationTab;
	private ResourceCopyTab resourceCopyTab;
	private ReservesTab reservesTab;
	
	/**
	 * Construct a new ResourceViewer.
	 * 
	 * @param resource
	 * 		The resource entity to view/modify, or null if a new resource is to be
	 * 		created.
	 */
	public ResourceViewer(Resource resource) {
		
		this.resource = resource;
		
		// Check if the resource parameter is null
		// If so, then a new resource entity is being created
		if (this.resource == null)
			newResource = true;
		
		// Create the GUI
		setBackground(Constants.BACKPANEL);
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		if (newResource == true)
			initNewResource();
		else
			initModResource();
	}
	
	// the viewer for creating a new resource
	private void initNewResource() {
		
		// add the ResourceInformationTab
		resourceInformationTab = new ResourceInformationTab(null);
		add(resourceInformationTab);
		resourceInformationTab.start();
		
	}
	
	// the viewer for modifying an existing resource
	private void initModResource() {
		
		// create the tabs
		resourceInformationTab = new ResourceInformationTab(resource);
		resourceCopyTab = new ResourceCopyTab(resource);
		reservesTab = new ReservesTab(resource);
		
		// the tabbed pane
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBackground(Constants.BACKPANEL);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				int index = tabbedPane.getSelectedIndex();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(resourceInformationTab.toString()))
					resourceInformationTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(resourceCopyTab.toString()))
					resourceCopyTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(reservesTab.toString()))
					reservesTab.start();
			}
		});
		add(tabbedPane);
		
		// adding tabs based on user mode
		User user = sessionManager.getUser();
		
		if (user instanceof StaffMember) {
			
			// add the ResourceInformationTab
			tabbedPane.add(resourceInformationTab.toString(), resourceInformationTab);
			
			// add the ResourceCopyTab
			tabbedPane.add(resourceCopyTab.toString(), resourceCopyTab);
			
			// add the ReservesTab
			tabbedPane.add(reservesTab.toString(), reservesTab);
			
		}
		else {
			
			// add the ResourceInformationTab
			tabbedPane.add(resourceInformationTab.toString(), resourceInformationTab);
			
		}
	}
	
	@Override
	public String toString() {
		if (newResource)
			return "New Resource";
		return resource.getTitle();
	}

}
