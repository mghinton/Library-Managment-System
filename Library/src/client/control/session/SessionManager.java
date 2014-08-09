package client.control.session;
import java.net.ConnectException;

import com.sun.rowset.CachedRowSetImpl;

import client.control.data.entity.user.Administrator;
import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.Librarian;
import client.control.data.entity.user.Student;
import client.control.data.entity.user.User;
import client.serverinterface.ServerInterface;
import global.LibrisGlobalInterface;

/**
 * This class manages a session. When a User logs in, this is the class that authenticates the User.
 * 
 * @author Jeremy Lerner
 * @version 8
 */
public class SessionManager{
	
	User user;
	static SessionManager INSTANCE = new SessionManager();
	
	/**
	 * Returns the (singleton) instance
	 * 
	 * @return The instance
	 */
	public static synchronized SessionManager getReference(){
		if (INSTANCE == null)
			return new SessionManager();
		return INSTANCE;
	}
	
	/**
	 * Constructs a new UserManager with no User logged in
	 */
	public SessionManager(){
		user = null;
	}
	
	/**
	 * Returns the User under whom the session is running
	 * 
	 * @return The User under whom the session is running
	 */
	public User getUser(){
		return user;
	}
	
	/**
	 * Logs a User into the system
	 * @param The ID number of the User to log in
	 * @param The User's password
	 * @return True if successful; false otherwise
	 */
	public boolean login(int userID, String password){
		try{
			CachedRowSetImpl rowSet = ServerInterface.getReference().requestLogin(userID, password);
			if(rowSet.next()){
				int userType = rowSet.getInt("type");
				if(userType == LibrisGlobalInterface.USER_TYPE_STUDENT)
					user = new Student(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_FACULTY)
					user = new Faculty(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_LIBRARIAN)
					user = new Librarian(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_ADMIN)
					user = new Administrator(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else
					user = null;
				return true;
			}
			else
				return false;
		}catch(RequestException e){
			ErrorManager.handleError(e);
			return false;
		} catch (ConnectException e) {
			ErrorManager.handleTimeOutError(e);
			return false;
		}catch(Exception e){
			ErrorManager.handleError(e);
			return false;
		}
	}
	
	/**
	 * Logs out of the current session
	 */
	public void logout(){
		try {
			ServerInterface.getReference().requestLogout();
		} catch (RequestException e) {
			ErrorManager.handleError(e);
		} catch (ConnectException e) {
			// Leave this empty
		}
		user = null;
	}
}