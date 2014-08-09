package client.control.data;

import global.LibrisGlobalInterface;

import java.sql.SQLException;
import com.sun.rowset.CachedRowSetImpl;
import client.serverinterface.ServerInterface;
import client.control.data.entity.user.Administrator;
import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.Librarian;
import client.control.data.entity.user.Student;
import client.control.data.entity.user.User;
import client.control.session.ErrorManager;
import client.control.session.RequestException;

/**
 * Control Object for Users
 * 
 * @author Jeremy Lerner, Peter Abelseth
 * @version 12
 *
 */
public class UserManager{
	
	/**
	 * Returns an array of User objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such Users.
	 * 
	 * @param searchKey A User object with some fields initialized
	 * @return An array of User objects from the Server with fields matching those initialized in the parameter
	 */
	public static User[] getUser(User searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		
		CachedRowSetImpl rowSet = null;
		User[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Returns a User object with a given ID number
	 * 
	 * @param A User ID number
	 * @return A User object with the given ID number
	 */
	public static User getUser(int idNumber){
		String sqlSelect = "SELECT user_ID, first_name, last_name, email, phone, type, enabled " +
				"FROM user WHERE user_ID = " + idNumber;
		CachedRowSetImpl rowSet = null;
		User result[] = null;
		try{
			rowSet =  ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		
		if(result != null && result.length != 0)
			return result[0];
		return null;
	}
	
	/**
	 * Edits an existing User entry in the database
	 * 
	 * @param A User object with updated fields. The ID number field must be set in the User.
	 * @return True if successful, false otherwise
	 */
	public static boolean setUser(User newEntry){
		String sqlUpdate = buildUpdateStatement(newEntry);
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestUpdate(sqlUpdate);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Inserts a new User object in the database
	 * 
	 * @param The User object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static User addUser(User newEntry){
		String sqlInsert = buildInsertStatement(newEntry);
		int id = 0;
		User newEntity = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert);
			newEntity = getUser(id);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return newEntity;
	}
	
	/**
	 * Removes a User object from the database
	 * 
	 * @param The ID number of the User to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeUser(int idNumber){
		String sqlDisable = "UPDATE user SET enabled = FALSE WHERE user_ID = " + idNumber;
		System.out.println(sqlDisable);
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestUpdate(sqlDisable);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Builds the select statement to search for a User with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(User entity){
		String sqlSelect = "SELECT " +
				"user_ID, first_name, last_name, email, phone, type, enabled " +
				"FROM user WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "user_ID = " + entity.getID() + " AND ";
		if(entity.getFirstName() != null && !entity.getFirstName().isEmpty())
			sqlSelect += "first_name LIKE '%" +entity.getFirstName().replace("'", "\\'") + "%' AND ";
		if(entity.getLastName() != null && !entity.getLastName().isEmpty())
			sqlSelect += "last_name LIKE '%" + entity.getLastName().replace("'",  "\\'") + "%' AND ";
		if(entity.getEmailAddress() != null && !entity.getEmailAddress().isEmpty())
			sqlSelect += "email LIKE '%" + entity.getEmailAddress().replace("'","\\'") + "%' AND ";
		sqlSelect += "enabled = " + entity.getEnabled() + " AND ";
		
		if(entity instanceof Student)
			sqlSelect += "type = " + LibrisGlobalInterface.USER_TYPE_STUDENT + " AND ";
		else if(entity instanceof Faculty)
			sqlSelect += "type = " + LibrisGlobalInterface.USER_TYPE_FACULTY + " AND ";
		else if(entity instanceof Librarian)
			sqlSelect += "type = " + LibrisGlobalInterface.USER_TYPE_LIBRARIAN + " AND ";
		else if(entity instanceof Administrator)
			sqlSelect += "type = " + LibrisGlobalInterface.USER_TYPE_ADMIN + " AND ";
		
		if (sqlSelect.endsWith(" AND "))//May not end with AND
			sqlSelect = sqlSelect.substring(0, sqlSelect.length()-5) + " ";	//get rid of extra AND statement
		if(sqlSelect.endsWith(" WHERE "))
			sqlSelect = sqlSelect.substring(0,sqlSelect.length()-" WHERE ".length());
		
		return sqlSelect;
	}
	
	/**
	 * Builds an update statement based on the values of entity that aren't null or 0
	 * @param entity The entity to update with the desired values
	 * @return sqlUpdate The statement that will update the given entity in the database
	 */
	private static String buildUpdateStatement(User entity){
		String sqlUpdate = "UPDATE user SET ";
		
		if(entity.getFirstName() != null && !entity.getFirstName().isEmpty())
			sqlUpdate += "first_name = '" +entity.getFirstName().replace("'", "\\'") + "', ";
		if(entity.getLastName() != null && !entity.getLastName().isEmpty())
			sqlUpdate += "last_name = '" + entity.getLastName().replace("'","\\'") + "', ";
		if(entity.getEmailAddress() != null && !entity.getEmailAddress().isEmpty())
			sqlUpdate += "email = '" + entity.getEmailAddress().replace("'", "\\'") + "', ";
		
		if(entity instanceof Student)
			sqlUpdate += "type = " +  LibrisGlobalInterface.USER_TYPE_STUDENT + ", ";
		else if(entity instanceof Faculty)
			sqlUpdate += "type = " +  LibrisGlobalInterface.USER_TYPE_FACULTY + ", ";
		else if(entity instanceof Librarian)
			sqlUpdate += "type = " +  LibrisGlobalInterface.USER_TYPE_LIBRARIAN + ", ";
		else if(entity instanceof Administrator)
			sqlUpdate += "type = " +  LibrisGlobalInterface.USER_TYPE_ADMIN + ", ";
		
		
		sqlUpdate += "enabled = " + entity.getEnabled() + ", ";
		
		if(sqlUpdate.endsWith(", "))
			sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE user_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given User
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(User entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "user_ID, ";
			values += entity.getID() + ", ";
		}
		if( entity.getFirstName() != null && !entity.getFirstName().isEmpty()){
			attributes += "first_name, ";
			values += "'" + entity.getFirstName().replace("'", "\\'") + "', ";
		}
		if(entity.getLastName() != null && !entity.getLastName().isEmpty()){
			attributes += "last_name, ";
			values += "'" + entity.getLastName().replace("'",  "\\'") + "', ";
		}
		if(entity.getEmailAddress() != null && !entity.getEmailAddress().isEmpty()){
			attributes += "email, ";
			values += "'" + entity.getEmailAddress().replace("'","\\'") + "', ";
		}
		attributes += "password, ";
		if(entity.getPassword() != null && !entity.getPassword().isEmpty()){
			values += "MD5('" + entity.getPassword().replace("'", "\\'") + "'), ";		//I really don't like putting the has function here :S
		}
		else{
			values += "MD5('" + createPassword(entity) + "'), ";
		}
		
		
		if(entity instanceof Student){
			values += LibrisGlobalInterface.USER_TYPE_STUDENT + ", ";
			attributes += "type, ";
		}
		else if(entity instanceof Faculty){
			values += LibrisGlobalInterface.USER_TYPE_FACULTY + ", ";
			attributes += "type, ";
		}
		else if(entity instanceof Librarian){
			values += LibrisGlobalInterface.USER_TYPE_LIBRARIAN + ", ";
			attributes += "type, ";
		}
		else if(entity instanceof Administrator){
			values += LibrisGlobalInterface.USER_TYPE_ADMIN + ", ";
			attributes += "type, ";
		}
		//possibly throw exception for invalid user type
		
		attributes += "enabled, ";
		values += entity.getEnabled() + ", ";
		
		if (attributes.endsWith(", ")){ //May not end with a comma
			attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
			values = values.substring(0, values.length()-2);
		}
		
		return "INSERT user " + attributes + ") VALUES " + values + ")";
	}
	


	/**
	 * This builds the User(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the Users acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static User[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		if(rowSet == null)
			return null;
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			
			User[] result = new User[nRows];
			rowSet.beforeFirst();
			for(int i=0; rowSet.next() && i < nRows ;i++){
				int userType = rowSet.getInt("type");
				if(userType == LibrisGlobalInterface.USER_TYPE_STUDENT)
					result[i] = new Student(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_FACULTY)
					result[i] = new Faculty(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_LIBRARIAN)
					result[i] = new Librarian(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else if(userType == LibrisGlobalInterface.USER_TYPE_ADMIN)
					result[i] = new Administrator(
							rowSet.getInt("user_ID"),
							rowSet.getString("first_name"),
							rowSet.getString("last_name"),
							null,
							rowSet.getString("email"),
							rowSet.getBoolean("enabled")
							);
				else
					result[i] = null;
			}
			return result;
		}
		catch(SQLException e){
			ErrorManager.handleError(e);
			return null;
		}
	}
	
	private static String createPassword(User entity) {
		if(entity.getFirstName() != null && entity.getLastName() != null)
			return entity.getFirstName().replace("'","").charAt(0) + entity.getLastName().replace("'", "\'");
		if(entity.getID() > 0)
			return Integer.toString(entity.getID());
		return "libris";
	}
	
	public static void main(String args[]){
		Student r = new Student(100000000,"Jeremy","Lerner","test","librislms@gmail.com",true);
				
		//ServerInterface.configureServerInterface("154.20.115.46",1500);
		
		//User result = addUser(r);
		System.out.println(buildUpdateStatement(r));
		//User result[] = getUser(r);
		//System.out.println(result[0].getFirstName());
		//boolean success = removeUser(100000000);
		//System.out.println(success);
		
		//ServerInterface.getReference().closeConnection();
	}
	
}