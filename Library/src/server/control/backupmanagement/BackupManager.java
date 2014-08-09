package server.control.backupmanagement;

/**
 * Manages backing up the database
 * 
 * @author Peter Abelseth
 * @version 0
 *
 */
public class BackupManager {
	
	private static BackupManager singleton = null;
	
	
	private BackupManager(){
	}
	
	/**
	 * Gets reference for singleton backup manager
	 * 
	 * @return BakupManager The one and only backupManager
	 */
	public static synchronized BackupManager getReference(){
		if(singleton == null)
			singleton = new BackupManager();
		return singleton;
	}
	
	/**
	 * Closes the backup manager. Just allows garbage collector to eat it up
	 */
	public static synchronized void close(){
		singleton = null;
	}
	
	public boolean backup(String destination){
		
		return false;
	}
	
	public boolean restore(String location){
		
		return false;
	}
}
