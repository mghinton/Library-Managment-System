package client.userinterface.datatypes.reports;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Constants;

/**
 * Creates a viewer to view information about a particular report.
 * 
 * @author Daniel Huettner
 * @version 4
 */
public class ReportViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// The tabbed pane for the different tabs
	private JTabbedPane tabbedPane = null;
	
	private FinesReportTab finesReportTab;
	private LoansReportTab loansReportTab;
	
	/**
	 * Constructs a new ReportViewer.
	 */
	public ReportViewer() {
		// Create the GUI
		setBackground(Constants.BACKPANEL);
		setVisible(true);
		setLayout(new BorderLayout(0, 0));
		init();
	}
	
	private void init() {
		
		// create the tabs
		finesReportTab = new FinesReportTab();
		loansReportTab = new LoansReportTab();
		
		// create the tabbed pane
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBackground(Constants.BACKPANEL);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				int index = tabbedPane.getSelectedIndex();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(finesReportTab.toString()))
					finesReportTab.start();
				if (tabbedPane.getTitleAt(index).equalsIgnoreCase(loansReportTab.toString()))
					loansReportTab.start();
			}
		});
		add(tabbedPane);
		
		// add the fines report tab
		tabbedPane.add(finesReportTab.toString(), finesReportTab);
			
		// add the loans report tab
		tabbedPane.add(loansReportTab.toString(), loansReportTab);
			
	}
	
	@Override
	public String toString() {
		return "Reports";
	}

}
