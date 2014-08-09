package util;

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * Holds the constant values for GUI
 * @author Matthew
 * @version 3
 *
 */
public class Constants {
	
	//Screen Sizes
	public static final int SCREEN_HEIGHT = 650;
	public static final int SCREEN_WIDTH = 1016;
	public static final int MAXTABS = 5;
	public static final int MINTABS = 0;

	//GUI Icons
	//Need to change paths to Resource directory
	public static ImageIcon GUEST_ICON = new ImageIcon("Resources/guestMode.gif");
	public static ImageIcon LOGO_LARGE = new ImageIcon("Resources/logo.gif");
	public static ImageIcon LOGO_SMALL = new ImageIcon("Resources/logo_small.gif");
	public static ImageIcon LOGOUT_ICON = new ImageIcon("Resources/logout.gif");
	public static ImageIcon LOGIN_ICON = new ImageIcon("Resources/login.gif");
	public static ImageIcon ADD_RESOURCE_ICON = new ImageIcon("Resources/add_resource.gif");
	public static ImageIcon ADD_USER_ICON = new ImageIcon("Resources/add_user.gif");
	public static ImageIcon SUBSCRIPTION_ICON = new ImageIcon("Resources/subscription.gif");
	public static ImageIcon REPORT_ICON = new ImageIcon("Resources/report.gif");
	public static ImageIcon TODO_ICON = new ImageIcon("Resources/todo.gif");
	public static ImageIcon SEARCH_ICON = new ImageIcon("Resources/search.gif");
	public static ImageIcon IMPORT_ICON = new ImageIcon("Resources/importUsers.gif");
	public static ImageIcon MY_ACCOUNT_ICON = new ImageIcon("Resources/myAccount.gif");
	public static ImageIcon USER_SEARCH_ICON = new ImageIcon("Resources/userSearch.gif");
	public static ImageIcon RESOURCE_TYPE_LIST_ICON = new ImageIcon("Resources/resourceType.gif");
	public static ImageIcon CONTROL_PANEL_LIST_ICON = new ImageIcon("Resources/control_panel.gif");
	public static ImageIcon NO_PATRON_LIST_ICON = new ImageIcon("Resources/no_patron.gif");
	
	public static Color BACKGROUND = new Color(204, 51, 0);
	public static Color BACKPANEL = new Color(210, 105, 30);
	
	// used for parsing and generating dates.
	public static final String DEFAULT_DATE_FORMAT =
			"dd-MM-yyyy";
	public static final String DEFAULT_TIME_ZONE = "PST";
	
	public static final String CONFIG_FILE_PATH = "Resources/config.txt";
	
	public static ImageIcon ICON_64 = new ImageIcon("Resources/icon64.gif");
	public static ImageIcon ICON_32 = new ImageIcon("Resources/icon32.gif");
	public static ImageIcon ICON_16 = new ImageIcon("Resources/icon16.gif");
	
}
