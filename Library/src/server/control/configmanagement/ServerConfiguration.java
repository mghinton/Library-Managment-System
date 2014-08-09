package server.control.configmanagement;

/**
 * This is an entity that just contains the parameters for the server
 * @author Peter Abelseth
 * @version 2
 */
public class ServerConfiguration {
	
	public int connectionManager_port;
	
	public String db_url;
	public String db_userName;
	public String db_password;
	
	public String emailMgr_host;
	public String emailMgr_port;
	public String emailMgr_account;
	public String emailMgr_password;
	
	public long overdueMgr_timeOfDayToRun;
	public long overdueMgr_periodBetweenEmails;
	public long overdueMgr_timeUntilStopEmails;
	public long overdueMgr_timeBeforeDueToEmail;
	
	public int reservationMgr_numberOfTimesToRunADay;
	public long reservationMgr_periodBetweenEmails;

	public long fineMgr_timeOfDayToRun;
	
}


