package server.control.configmanagement;

import server.control.backupmanagement.BackupManager;
import server.control.datamanagement.FineManager;
import server.control.datamanagement.OverdueManager;
import server.control.datamanagement.RequestManager;
import server.control.datamanagement.ReservationManager;
import server.control.errormanagement.ErrorManager;
import server.dbinterface.DBInterface;
import server.serverinterface.clientinterface.ClientConnectionManager;
import server.serverinterface.emailinterface.EmailManager;

/**
 * This is the main server class. It will handle all subsystems of the server system.
 * @author Peter Abelseth
 * @version 4
 */
public class Server {

	private static Server singleton = null;
	
	private ServerConfiguration config;	//the configuration of the server

 	private ClientConnectionManager clientConnectionMgr;
	private FineManager fineMgr;
	private OverdueManager overdueMgr;
	private ReservationManager reservationMgr;
	
	
	private Server(){
	}
	
	/**
	 * Returns the singleton reference to the server
	 * 
	 * @return Server The server
	 */
	public static synchronized Server getReference(){
		if(singleton == null)
			singleton = new Server();
		return singleton;
	}
	
	/**
	 * Starts the server with the given configuration, will do nothing if the server has already been started
	 * 
	 * @param config The configuration of the server
	 */
	public static synchronized void startServer(ServerConfiguration config){
		if(singleton == null)
			Server.getReference();
		if(singleton.config == null){
			singleton.config = config;
			singleton.startErrorManager();
			singleton.startDBInterface();
			singleton.startRequestManager();
			singleton.startClientConnectionManager();
			singleton.startEmailManager();
			singleton.startFineManager();
			singleton.startOverdueManager();
			singleton.startReservationManager();
			singleton.startBackupManager();
		}
	}
	
	/**
	 * Resets the server with the new ServerConfiguration
	 * 
	 * @param config The new ServerConfiguration
	 */
	public static synchronized void resetServer(ServerConfiguration config){
		singleton.config = config;
		//please note: order is important!
		singleton.startErrorManager();
		singleton.restartDBInterface();
		singleton.restartRequestManager();
		singleton.restartClientConnectionManager();
		singleton.restartEmailManager();
		singleton.restartFineManager();
		singleton.restartOverdueManager();
		singleton.restartReservationManager();
		singleton.restartBackupManager();
	}
	
	/**
	 * Shuts down all components of the server, and lets garbage collector eat the server
	 * 
	 */
	public static synchronized void shutDownServer(){
		singleton.config = null;
		
		BackupManager.close();
		if (singleton.fineMgr != null){
			singleton.fineMgr.stop();
			singleton.fineMgr = null;
		}
		if (singleton.overdueMgr != null){
			singleton.overdueMgr.stop();
			singleton.overdueMgr = null;
		}
		if (singleton.reservationMgr != null){
			singleton.reservationMgr.stop();
			singleton.reservationMgr = null;
		}
		EmailManager.close();
		if(singleton.clientConnectionMgr != null){
			singleton.clientConnectionMgr.close();
			singleton.clientConnectionMgr = null;
		}
		RequestManager.close();
		DBInterface.close();
		ErrorManager.close();
		
		singleton = null;
	}
	
	/**
	 * Restart the DBInterface using the existing configuration
	 */
	public void restartDBInterface(){
		DBInterface.close();
		startDBInterface();
	}
	
	/**
	 * Restart the Request using the existing configuration
	 */
	public void restartRequestManager(){
		RequestManager.close();
		startRequestManager();
	}
		
	/**
	 * Restart the ClientConnectionManager using the existing configuration
	 */
	public void restartClientConnectionManager(){
		if(clientConnectionMgr != null)
			clientConnectionMgr.close();
		clientConnectionMgr = null;
		startClientConnectionManager();
	}
	
	/**
	 * Restart the EmailManager using the existing configuration
	 */
	public void restartEmailManager(){
		EmailManager.close();
		startEmailManager();
	}
	
	/**
	 * Restart the FineManager using the existing configuration
	 */
	public void restartFineManager(){
		if(fineMgr != null)
			fineMgr.stop();
		fineMgr = null;
		startFineManager();
	}
	
	/**
	 * Restart the OverdueManger using the existing configuration
	 */
	public void restartOverdueManager(){
		if(overdueMgr != null)
			overdueMgr.stop();
		overdueMgr = null;
		startOverdueManager();
	}
	
	/**
	 * Restart the ReservationManager using the existing configuration
	 */
	public void restartReservationManager(){
		if(reservationMgr != null)
			reservationMgr.stop();
		reservationMgr = null;
		startReservationManager();
	}
	
	/**
	 * Restart the BackupManager using the existing configuration
	 */
	public void restartBackupManager(){
		BackupManager.close();
		startBackupManager();
	}
	
	
	
	
//Private Methods
	private void startErrorManager(){
		ErrorManager.configureErrorManager(this);
	}
	
	
	private void startDBInterface(){
		DBInterface.configureDBInterface(
			config.db_url,
			config.db_userName,
			config.db_password);
	}
	

	private void startRequestManager(){
		RequestManager.getReference();
	}
	
	
	private void startClientConnectionManager(){
		this.clientConnectionMgr = new ClientConnectionManager(config.connectionManager_port);
		this.clientConnectionMgr.start();
	}
	
	
	private void startEmailManager(){
		EmailManager.configureEmailManager(
			config.emailMgr_host,
			config.emailMgr_account,
			config.emailMgr_password, 
			config.emailMgr_port);
		
	}
	
	private void startFineManager(){
		this.fineMgr = new FineManager(
				config.fineMgr_timeOfDayToRun);
	}
		
	private void startOverdueManager(){
		this.overdueMgr = new OverdueManager(
				config.overdueMgr_timeOfDayToRun,
				config.overdueMgr_periodBetweenEmails,
				config.overdueMgr_timeUntilStopEmails,
				config.overdueMgr_timeBeforeDueToEmail);
	}
	
	private void startReservationManager(){
		this.reservationMgr = new ReservationManager(
				config.reservationMgr_numberOfTimesToRunADay,
				config.reservationMgr_periodBetweenEmails);
	}
	
	private void startBackupManager(){
		BackupManager.getReference();
	}

	
	
	// Going to use for testing
	public static void main(String args[]){
		ServerConfiguration config = new ServerConfiguration();
		config.connectionManager_port = 1500;
		config.db_password = "root";
		config.db_url = "jdbc:mysql://localhost:3306/librisDB";
		config.db_userName = "root";
		config.emailMgr_account = "librislms@gmail.com";
		config.emailMgr_host = "smtp.gmail.com";
		config.emailMgr_password = "Daniel'sAngels";
		config.emailMgr_port = "465";
		config.fineMgr_timeOfDayToRun = 60*60*1000L;
		config.overdueMgr_periodBetweenEmails = 2*24*60*60*1000L;
		config.overdueMgr_timeBeforeDueToEmail = 2*24*60*60*1000L;
		config.overdueMgr_timeUntilStopEmails = 4*7*24*60*60*1000L;
		config.overdueMgr_timeOfDayToRun = 90*60*1000L;
		config.reservationMgr_periodBetweenEmails = 10*60*1000L;
		config.reservationMgr_numberOfTimesToRunADay = 4;
		
		Server.startServer(config);
	}
}



