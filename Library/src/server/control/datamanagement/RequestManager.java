package server.control.datamanagement;

import java.sql.SQLException;
import com.sun.rowset.CachedRowSetImpl;
import server.dbinterface.DBInterface;

/**
 * This class handles all client requests to the database
 * 
 * @author Sardor/Peter
 * @version 5
 */

public class RequestManager {
	
	private static RequestManager singleton;		//reference to the single RequestManager
	
	/**
	 * Empty constructor for singleton
	 */
	private RequestManager(){
	}
	
	/**
	 * Returns the singleton reference to the RequestManager
	 * 
	 * @return singleton The RequestManager
	 */
	public static synchronized RequestManager getReference(){
		if(singleton == null)
			return new RequestManager();
		return singleton;
	}
	
	/**
	 * Closes the RequestManager and allows it to be picked up by garbage collection
	 */
	public static synchronized void close(){
		singleton = null;
	}


	/**
	 * Processes a select statement along with optional checks statements before processing the select
	 * 
	 * @param sqlSelect The statement to return a result from
	 * @param sqlCheck The statements to check before executing the select statement
	 * @param userID The userID of the user performing the operation (0 if guest)
	 * @param userType The type of user performing the operation
	 * @return CachedRowSetImpl The result from the database
	 * @throws IllegalAccessException If the user has insufficient privileges to perform the sql select statement
	 */
	public synchronized CachedRowSetImpl processSQLSelect(String sqlSelect, String sqlCheck[], int userID, int userType) throws IllegalAccessException{
		if(!PermissionManager.checkAllowed(sqlSelect, userID, userType))
			throw new IllegalAccessException("Insuffecient permissions to perform that operation.");
		
		if(sqlCheck != null){	//if there are check statements
			try{
				if(!checkBeforeExecuting(sqlCheck)){
					throw new IllegalAccessException("Operation not allowed at this time.");
				}
			} catch(RuntimeException e){
				throw new IllegalArgumentException("System Error: " + e.getMessage());
			}
		}
		
		try {
			CachedRowSetImpl result = DBInterface.getReference().executeSelect(sqlSelect);
			return result;
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("System Error: " + e.getMessage());
		}
	}
	
	/**
	 * Processes an update statement along with optional checks statements before processing the update
	 * 
	 * @param sqlUpdate The statement to return a result from
	 * @param sqlCheck The statements to check before executing the update statement
	 * @param userID The userID of the user performing the operation (0 if guest)
	 * @param userType The type of user performing the operation
	 * @return boolean If there were entities update
	 * @throws IllegalAccessException If the user has insufficient privileges to perform the sql update statement
	 */
	public synchronized boolean processSQLUpdate(String sqlUpdate, String sqlCheck[], int userID, int userType) throws IllegalAccessException{
		if(!PermissionManager.checkAllowed(sqlUpdate, userID, userType))
			throw new IllegalAccessException("Insuffecient permissions to perform that operation.");
		
		if(sqlCheck != null){	//if there are check statements
			try{
				if(!checkBeforeExecuting(sqlCheck)){
					throw new IllegalAccessException("Operation not allowed at this time.");
				}
			} catch(RuntimeException e){
				throw new IllegalArgumentException("System Error: " + e.getMessage());
			}
		}
		
		try {
			return DBInterface.getReference().executeUpdate(sqlUpdate);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("System Error: " + e.getMessage());
		}
	}
	
	/**
	 * Processes an insert statement along with optional checks statements before processing the insert
	 * 
	 * @param sqlInsert The statement to execute
	 * @param sqlCheck The statements to check before executing the insert statement
	 * @param userID The userID of the user performing the operation (0 if guest)
	 * @param userType The type of user performing the operation
	 * @return int The ID of the entity inserted
	 * @throws IllegalAccessException If the user has insufficient privileges to perform the sql insert statement
	 */
	public synchronized int processSQLInsert(String sqlInsert, String sqlCheck[], int userID, int userType) throws IllegalAccessException{
		if(!PermissionManager.checkAllowed(sqlInsert, userID, userType))
			throw new IllegalAccessException("Insuffecient permissions to perform that operation.");
		
		if(sqlCheck != null){	//if there are check statements
			try{
				if(!checkBeforeExecuting(sqlCheck)){
					throw new IllegalAccessException("Operation not allowed at this time.");
				}
			} catch(RuntimeException e){
				throw new IllegalArgumentException("System Error: " + e.getMessage());
			}
		}
		
		try {
			int id = DBInterface.getReference().executeInsert(sqlInsert);
			if(id < 0)
				throw new IllegalAccessException("Invalid insertion.");
			else 
				return id;
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("System Error: " + e.getMessage());
		}
	}
	
	/**
	 * Processes a select statement along with optional checks statements before processing the select
	 * 
	 * @param sqlDelete The statement to delete rows from the database
	 * @param sqlCheck The statements to check before executing the delete statement
	 * @param userID The userID of the user performing the operation (0 if guest)
	 * @param userType The type of user performing the operation
	 * @return boolean If the delete was successful
	 * @throws IllegalAccessException If the user has insufficient privileges to perform the sql delete statement
	 */
	public synchronized boolean processSQLDelete(String sqlDelete, String sqlCheck[], int userID, int userType) throws IllegalAccessException{
		if(!PermissionManager.checkAllowed(sqlDelete, userID, userType))
			throw new IllegalAccessException("Insuffecient permissions to perform that operation.");
		
		if(sqlCheck != null){	//if there are check statements
			try{
				if(!checkBeforeExecuting(sqlCheck)){
					throw new IllegalAccessException("Operation not allowed at this time.");
				}
			} catch(RuntimeException e){
				throw new IllegalArgumentException("System Error: " + e.getMessage());
			}
		}
		
		try {
			return DBInterface.getReference().executeDelete(sqlDelete);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("System Error: " + e.getMessage());
		}
	}
	
	/**
	 * Handles a client's logging request. Needed separate method to bypass the PermissionManager
	 * 
	 * @param request The request with the log in sql statement
	 * @return request The fulfilled log in request
	 * @throws IllegalArgumentException If system error with database
	 */
	public synchronized CachedRowSetImpl processLogin(int userID, String password) throws IllegalArgumentException{
		String sqlLogin = "SELECT user_ID, first_name, last_name, email, phone, type, enabled " +
				"FROM user WHERE user_ID = " + userID + " " +
				"AND password = MD5('" + password.replace("'", "\\'") + "') " +
				"AND enabled = TRUE";
		
		CachedRowSetImpl result = null;
		try{
			result = DBInterface.getReference().executeSelect(sqlLogin);
		} catch(RuntimeException e){
			throw new IllegalArgumentException("System Error: " + e.getMessage());
		}
		return result;
	}
	
	/**
	 * Changes the password of a user
	 * 
	 * @param userID The userID of the user to change password for
	 * @param currentPassword The current password of the user
	 * @param newPassword The new password of the user
	 * @return boolean True if changed successfully, false otherwise
	 * @throws IllegalArgumentException If system error
	 */
	public synchronized boolean processPasswordChange(int userID, String currentPassword, String newPassword) throws IllegalArgumentException{
		String sqlChangePassword = "UPDATE user SET password = MD5('" + newPassword.replace("'", "\'") + "') " +
				"WHERE user_ID = " + userID + " AND " +
				"password = MD5('" + currentPassword.replace("'", "\'") + "') AND " +
				"enabled = true";
		
		boolean success = false;
		try{
			success = DBInterface.getReference().executeUpdate(sqlChangePassword);
		} catch(RuntimeException e){
			throw new IllegalArgumentException("System Error: " + e.getMessage());
		}
		return success;
	}
	
	/**
	 * Runs the sql check statements and ensure they all return empty sets
	 * 
	 * @param sqlCheck The sql select statements to execute
	 * @return boolean True if all check statements return an empty set, false if any select statement returns a row
	 * @throws RuntimeException If a check statement was invalid
	 */
	private boolean checkBeforeExecuting(String sqlCheck[]) throws RuntimeException{
		try{
			for(String check: sqlCheck){
				CachedRowSetImpl result = DBInterface.getReference().executeSelect(check);
				if(result.next())
					return false;
			}
		} catch(SQLException e){
			throw new RuntimeException("System Error: Invalid SQL Check Statement");
		}
		return true;
	}	
}


