package client.serverinterface;

import client.control.session.RequestException;
import communication.RequestPacket;
import com.sun.rowset.CachedRowSetImpl;
/**
 * ServerInterface application - file ServerInterface.java 
 * 
 * ServerInterface class
 * 
 * @author Sardor Isakov/Peter
 * @version 5
 */


public class ServerInterface {
	private static ServerInterface singleton;
	private NetworkConnection connection;
	
	private ServerInterface(String serverIP, int port){ 
		connection = new NetworkConnection(serverIP,port);
		connection.configure();
	}
	
	// Static 'instance' method 
	public static synchronized ServerInterface getReference( ) {
		if(singleton == null){
			throw new IllegalArgumentException("Server Interface needs to be initialized first!");
		}
		return singleton;
	}
	
	/**
	 * Set up the connection before using it
	 * 
	 * @param serverIP The ip where the server is
	 * @param port THe port the server is listening on
	 * @return boolean True if connected successfully, false otherwise
	 */
	public static synchronized ServerInterface configureServerInterface(String serverIP,int port) {
		singleton = new ServerInterface(serverIP,port);
		return singleton;
	}
	
	/**
	 * Closes the connection to the server gracefully
	 * 
	 */
	public void closeConnection() {
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_DISCONNECT);
		request = connection.sendRequest(request);
	}
	
	/**
	 * Sends a select statement to the server, waiting for a response
	 * 
	 * @param sqlSelect The select statement to execute on the database
	 * @return result The row set that the database retrieved if the operation was successful. Returns null if unsuccessful
	 * @throws RequestException If the operation was unsuccessful
	 */
	public CachedRowSetImpl requestSelect(String sqlSelect) throws RequestException {
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_SQL);
		request.setSQLType(RequestPacket.SELECT);
		request.setSQLStatment(sqlSelect);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess())
			throw new RequestException(request.getErrorMsg());
		return request.getRowSet();
	}
	
	/**
	 * Sends a select statement to the server with checks, waiting for a response
	 * 
	 * @param sqlSelect The select statement to execute on the database
	 * @param sqlCheck The statements to check the database with before executing the select statement
	 * @return result The row set that the database retrieved if the operation was successful. Returns null if unsuccessful
	 * @throws RequestException If the operation was unsuccessful
	 */
	public CachedRowSetImpl requestSelect(String sqlSelect, String sqlCheck[]) throws RequestException {
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_SQL);
		request.setSQLType(RequestPacket.SELECT);
		request.setSQLStatment(sqlSelect);
		request.setSQLCheck(sqlCheck);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess()){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getRowSet();
		
		
	}
	
	/**
	 * Sends an insert statement to the server, waiting for a response
	 * 
	 * @param sqlInsert The insert statement to execute on the database
	 * @return result The ID of the inserted entity. Returns 0 if unsuccessful.
	 * @throws RequestException If the operation was unsuccessful
	 */
	public int requestInsert(String sqlInsert) throws RequestException {
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_SQL);
		request.setSQLType(RequestPacket.INSERT);
		request.setSQLStatment(sqlInsert);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess()){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getInsertResult();
	}
	
	/**
	 * Sends an insert statement to the server with checks, waiting for a response
	 * 
	 * @param sqlInsert The insert statement to execute on the database
	 * @param sqlCheck The check statements to execute before inserting the entity
	 * @return result The ID of the inserted entity. Returns 0 if unsuccessful.
	 * @throws RequestException If the operation was unsuccessful
	 */
	public int requestInsert(String sqlInsert, String sqlCheck[]) throws RequestException {
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_SQL);
		request.setSQLType(RequestPacket.INSERT);
		request.setSQLStatment(sqlInsert);
		request.setSQLCheck(sqlCheck);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess()){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getInsertResult();
	}
	
	
	/**
	 * Sends an update statement to the server, waiting for a response
	 * 
	 * @param sqlUpdate The update statement to execute on the database
	 * @return result True if the entity was updated, false if not.
	 * @throws RequestException If the operation was unsuccessful
	 */
	public boolean requestUpdate(String sqlUpdate) throws RequestException{
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_SQL);
		request.setSQLType(RequestPacket.UPDATE);
		request.setSQLStatment(sqlUpdate);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess() && request.getErrorMsg() != null){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getSuccess();
	}
	
	/**
	 * Sends an update statement to the server, waiting for a response
	 * 
	 * @param sqlUpdate The update statement to execute on the database
	 * @param sqlCheck The check statements to execute before the update
	 * @return result True if the entity was updated, false otherwise.
	 * @throws RequestException If the operation was unsuccessful
	 */
	public boolean requestUpdate(String sqlUpdate, String sqlCheck[]) throws RequestException{
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_SQL);
		request.setSQLType(RequestPacket.UPDATE);
		request.setSQLStatment(sqlUpdate);
		request.setSQLCheck(sqlCheck);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess() && request.getErrorMsg() != null){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getSuccess();		
	}
	
	/**
	 * Sends an delete statement to the server, waiting for a response
	 * 
	 * @param sqlDelete The update statement to execute on the database
	 * @return result True if the entity was deleted, false otherwise.
	 * @throws RequestException If the operation was unsuccessful
	 */
	public boolean requestDelete(String sqlDelete) throws RequestException{
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_SQL);
		request.setSQLType(RequestPacket.DELETE);
		request.setSQLStatment(sqlDelete);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess() && request.getErrorMsg() != null){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getSuccess();
	}
	
	/**
	 * Sends an delete statement to the server, waiting for a response
	 * 
	 * @param sqlDelete The update statement to execute on the database
	 * @param sqlCheck The check statements to execute before deleteing
	 * @return result True if the entity was deleted, false otherwise.
	 * @throws RequestException If the operation was unsuccessful
	 */
	public boolean requestDelete(String sqlDelete, String sqlCheck[]) throws RequestException{
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_SQL);
		request.setSQLType(RequestPacket.DELETE);
		request.setSQLStatment(sqlDelete);
		request.setSQLCheck(sqlCheck);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess() && request.getErrorMsg() != null){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getSuccess();		
	}

	/**
	 * Attempts to log this client in with a userID and password
	 * 
	 * @param userID The user ID of the user trying to log in
	 * @param userPassword The password of the user trying to log in
	 * @return result Returns the user's entity if they logged in successfully, null otherwise
	 * @throws RequestException If the operation was unsuccessful
	 */
	public CachedRowSetImpl requestLogin(int userID, String userPassword) throws RequestException{
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_LOGIN);
		request.setUserID(userID);
		request.setPassword(userPassword);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess()){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getRowSet();
	}
	
	/**
	 * Attempts to log this client out of the current user's account
	 * 
	 * @return success Returns true if successfully logged out, false otherwise
	 * @throws RequestException If the operation was unsuccessful
	 */
	public boolean requestLogout() throws RequestException{
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_LOGOUT);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicating with the Server.");
		if(!request.getSuccess()){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getSuccess();
	}
	
	/**
	 * Attempts to change the password of the user to the newPassword
	 * 
	 * @param userID The userID of the user to change password for
	 * @param userPassword The current password of the user
	 * @param newPassword The new password for the user
	 * @return success True if successful, false otherwise
	 * @throws RequestException If the request was unsuccessful and there was an error.
	 */
	public boolean requestChangePassword(int userID, String userPassword, String newPassword) throws RequestException{
		RequestPacket request = new RequestPacket();
		request.setPacketType(RequestPacket.REQUEST_CHANGEPASSWORD);
		request.setUserID(userID);
		request.setPassword(userPassword);
		request.setNewPassword(newPassword);
		request = connection.sendRequest(request);
		if(request == null)
			throw new RequestException("System Error: Error communicatin with the Server.");
		if(!request.getSuccess()){
			throw new RequestException(request.getErrorMsg());
		}
		return request.getSuccess();
	}
}