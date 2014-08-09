package server.control.errormanagement;

import java.util.Calendar;

import server.control.configmanagement.Server;
import server.control.datamanagement.FineManager;
import server.control.datamanagement.OverdueManager;
import server.control.datamanagement.RequestManager;
import server.control.datamanagement.ReservationManager;
import server.dbinterface.DBInterface;
import server.serverinterface.clientinterface.ClientConnectionManager;
import server.serverinterface.emailinterface.EmailManager;

/**
 * @author Peter Abelseth
 * @version 2
 */
public class ErrorManager {
	
	private Server server;	//the server to send subsystem responses to

	private static ErrorManager singleton = null;
	
/*	private static final Calendar TIME_ALLOWED_BETWEEN_FATAL_ERROR = Calendar.getInstance();
	
	private Calendar lastFatalError_System = null;
	private Calendar lastFatalError_ErrorManager = null;
	private Calendar lastFatalError_DBInterface = null;
 	private Calendar lastFatalError_RequestManager = null;
 	private Calendar lastFatalError_ClientConnectionManager = null;
	private Calendar lastFatalError_EmailManager = null;
	private Calendar lastFatalError_FineManager = null;
	private Calendar lastFatalError_OverdueManager = null;
	private Calendar lastFatalError_ReservationManager = null;
	private Calendar lastFatalError_BacukupManager = null;
*/		
	/**
	 * 
	 */
	private ErrorManager(Server server) {
		this.server = server;
	}
	
	public static synchronized ErrorManager configureErrorManager(Server server){
		singleton = new ErrorManager(server);
		return singleton;
	}
	
	public static synchronized ErrorManager getReference(){
		if(singleton == null){
			//throw exception to say not initialized
		}
		return singleton;
	}
	
	/**
	 * Closes the ErrorManager, allows it to be picked up by garbage collector
	 */
	public static synchronized void close(){
		singleton = null;
	}
	
	public void fatalSystemError(Exception e, Object subsystem){
		System.out.println("Fatal Error. Shutting down Server:" + e.getMessage());
		e.printStackTrace();
		server.shutDownServer();
	}
	
	/**
	 * This will restart all necessary subsystems to get the given subsystem back up and running.
	 * 
	 * @param e
	 * @param subsystem
	 */
	public void fatalSubystemError(Exception e, Object subsystem){
		if(subsystem instanceof DBInterface){
			//if(lastFatalError_DBInterface == null || Calendar.getInstance().compareTo(lastFatalError_DBInterface) < 0){
			//	lastFatalError_DBInterface = Calendar.getInstance();
			server.restartDBInterface();
			
		}
		else if(subsystem instanceof RequestManager){
			server.restartRequestManager();
		}
		else if(subsystem instanceof ClientConnectionManager){
			
		}
		else if(subsystem instanceof EmailManager){
			server.restartEmailManager();
		}
		else if(subsystem instanceof FineManager){
			server.restartFineManager();
		}
		else if(subsystem instanceof OverdueManager){
			server.restartOverdueManager();
		}
		else if(subsystem instanceof ReservationManager){
			server.restartReservationManager();
		}
		/*else if(subsystem instanceof BackupManager){
			
		}*/
		else{	//who the **** is calling me!
			
		}
	}
	
}



