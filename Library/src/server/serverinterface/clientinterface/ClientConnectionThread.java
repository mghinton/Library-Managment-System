package server.serverinterface.clientinterface;
import global.LibrisGlobalInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Calendar;

import com.sun.rowset.CachedRowSetImpl;

import server.control.datamanagement.RequestManager;
import communication.*;
/** Representation for Client Thread
 */
/**
 * Client Thread side application - file ClientConnectionThread.java 
 * 
 * Thread started by Server.java
 * 
 * @author Sardor Isakov/Peter
 * @version 5.0
 */
public class ClientConnectionThread  extends Thread implements LibrisGlobalInterface{
	
	private ClientConnectionManager clientConnectionMgr;
	private Socket socket;					// the socket where to listen/talk
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	private boolean keepGoing;
	
	private int userID;		//user ID of current user logged in (0 if it is a guest)
	private int userType;	//userType of the current user logged in, according to LibrisGlobalInterface
	private boolean disconnect;	//flag to check if the client is disconnecting
	
	/** Constructs a new ClientConnectionThread to handle of this client's requests
	 * 
	 * @param socket socket received from server
	 * @param ClientConnectionManager The manager handling new clients.
	 */
	public ClientConnectionThread(Socket socket,ClientConnectionManager clientConnectionMgr){
		this.clientConnectionMgr = clientConnectionMgr;
		this.userID = 0;
		this.userType = 0;
		
		this.socket = socket;
		try {
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sInput  = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e) {
			System.out.println("Constructing CLientThread error");
			this.close();
		}
	}
	
	/**
	 * This will run and accept requests from the client and return responses
	 */
	public void run() {
		keepGoing = true;		
		while(keepGoing) {
			RequestPacket request = null;
			try {
				Object obj = sInput.readObject();
				if(obj instanceof RequestPacket){
					request = (RequestPacket) obj;
					request = handleRequest(request);
					writeMsg(request);
				}
				if(disconnect)
					this.close();
			}
			catch (IOException e) {
				this.close();
			} catch (ClassNotFoundException e) {
				this.close();
			}
		}
		System.out.println("" + Calendar.getInstance().getTime() + " " + socket.getInetAddress() + " disconnected");
	}
	

	/**
	 * Closes this ClientConnectionThread
	 */
	public void close() {
		keepGoing = false;
		clientConnectionMgr.removeClient(this);
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(IOException e) {}
		try {
			if(sInput != null) sInput.close();
		}
		catch(IOException e) {};
		try {
			if(socket != null) socket.close();
		}
		catch (IOException e) {};
	}
	
	/**
	 * This class determines the type of request and delegates accordingly
	 * 
	 * @param request The request to process
	 * @return RequestPacket The request result
	 */
	private RequestPacket handleRequest(RequestPacket request){
		if(request.getPacketType() == RequestPacket.REQUEST_SQL)
			return handleSQLRequest(request);
		if(request.getPacketType() == RequestPacket.REQUEST_LOGIN)
			return loginClient(request);
		if(request.getPacketType() == RequestPacket.REQUEST_LOGOUT)
			return logoutClient(request);
		if(request.getPacketType() == RequestPacket.REQUEST_CHANGEPASSWORD)
			return changePassword(request);
		if(request.getPacketType() == RequestPacket.REQUEST_DISCONNECT){
			this.disconnect = true;
			request.setSuccess(true);
			return request;
		}
		request.setSuccess(false);
		request.setErrorMsg("System Error: Unkown Request Type");
		return request;
	}
	
	private RequestPacket handleSQLRequest(RequestPacket request){
		try{
			if(request.getSQLType() == RequestPacket.SELECT){
				CachedRowSetImpl result = RequestManager.getReference().processSQLSelect(request.getSQLStatment(), request.getSQLCheck(), this.userID, this.userType);
				request.setRowSet(result);
				request.setSuccess(true);
				return request;
			}
			if(request.getSQLType() == RequestPacket.UPDATE){
				boolean success = RequestManager.getReference().processSQLUpdate(request.getSQLStatment(), request.getSQLCheck(), this.userID, this.userType);
				request.setSuccess(success);
				return request;
			}
			if(request.getSQLType() == RequestPacket.INSERT){
				int entityID = RequestManager.getReference().processSQLInsert(request.getSQLStatment(), request.getSQLCheck(), this.userID, this.userType);
				request.setInsertResult(entityID);
				request.setSuccess(true);
				return request;
			}
			if(request.getSQLType() == RequestPacket.DELETE){
				boolean success = RequestManager.getReference().processSQLDelete(request.getSQLStatment(), request.getSQLCheck(), this.userID, this.userType);
				request.setSuccess(success);
				return request;
			}
		} catch (IllegalAccessException e) {
			request.setSuccess(false);
			request.setErrorMsg(e.getMessage());
		} catch (IllegalArgumentException e){
			request.setSuccess(false);
			request.setErrorMsg(e.getMessage());				
		}
		request.setSuccess(false);
		request.setErrorMsg("System Error: Unkown SQL request type.");
		
		return request;
	}
	
	private RequestPacket loginClient(RequestPacket request){
		if(userID > 0 || userType > 0){	//make sure this client isn't already logged in
			request.setSuccess(false);
			request.setErrorMsg("Must log out first before logging in");
			return request;
		}
		try{
			CachedRowSetImpl result = RequestManager.getReference().processLogin(request.getUserID(), request.getPassword());
			if(result.next()){
				int userID = result.getInt("user_ID");
				int userType = result.getInt("type");
				this.userID = userID;
				this.userType = userType;
				request.setSuccess(true);
				result.beforeFirst();
				request.setRowSet(result);
			}
			else{
				request.setSuccess(false);
				request.setErrorMsg("Invalid user ID or password");
			}
		}catch(IllegalArgumentException e){
			request.setSuccess(false);
			request.setErrorMsg(e.getMessage());
		} catch (SQLException e) {
			request.setSuccess(false);
			request.setErrorMsg("System Error: Error logging in.");
		}
		return request;
	}
	
	private RequestPacket logoutClient(RequestPacket request){
		if(this.userID <= 0 || this.userType <= 0){
			request.setSuccess(false);
			request.setErrorMsg("Must be logged in to log out!");
		}
		else{
			this.userID = 0;
			this.userType = 0;
			request.setSuccess(true);
		}
		
		return request;
	}
	
	private RequestPacket changePassword(RequestPacket request){
		if(this.userID <= 0 || this.userType <= 0){
			request.setSuccess(false);
			request.setErrorMsg("Must be logged in to change your password!");
			return request;
		}
		
		try{
			boolean success = RequestManager.getReference().processPasswordChange(request.getUserID(), request.getPassword(), request.getNewPassword());
			request.setSuccess(success);
			if(!success)
				request.setErrorMsg("Invalid user ID or password.");
		} catch(IllegalArgumentException e){
			request.setSuccess(false);
			request.setErrorMsg(e.getMessage());
		}
		return request;
	}
	
	/** Writes message to output stream
	 * 
	 * @param response
	 * @return boolean
	 */
	private boolean writeMsg(RequestPacket response) {
		// if Client is still connected send the message to it
		if(!socket.isConnected()) {
			close();
			return false;
		}
		// write the message to the stream
		try {
			sOutput.writeObject(response);
			sOutput.flush();
		}
		// if an error occurs, do not abort just inform the user
		catch(IOException e) {
			return false;
		}
		return true;
	}
}
