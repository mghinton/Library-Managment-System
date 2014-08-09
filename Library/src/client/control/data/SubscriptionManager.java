package client.control.data;

import java.sql.SQLException;
import java.util.Date;
import com.sun.rowset.CachedRowSetImpl;
import client.serverinterface.ServerInterface;
import client.control.data.entity.management.Subscription;
import client.control.session.ErrorManager;
import client.control.session.RequestException;

/**
 * Control Object for Subscriptions
 * 
 * @author Jeremy Lerner
 * @version 6
 *
 */
public class SubscriptionManager{
	
	/**
	 * Returns an array of Subscription objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such Subscriptions.
	 * 
	 * @param A Subscription object with some fields initialized
	 * @return An array of Subscription objects from the Server with fields matching those initialized in the parameter
	 */
	public static Subscription[] getSubscription(Subscription searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		
		CachedRowSetImpl rowSet = null;
		Subscription[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Returns a Subscription object with a given ID number
	 * 
	 * @param A Subscription ID number
	 * @return A Subscription object with the given ID number
	 */
	public static Subscription getSubscription(int idNumber){
		String sqlSelect = "SELECT subscription_ID, subscription_name, expire_date, contact_phone, contact_email " +
				"FROM subscription WHERE subscription_ID = " + idNumber;
		
		CachedRowSetImpl rowSet = null;
		Subscription[] result = null;
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
	 * Edits an existing Subscription entry in the database
	 * 
	 * @param A Subscription object with updated fields. The ID number field must be set in the Subscription.
	 * @return True if successful, false otherwise
	 */
	public static boolean setSubscription(Subscription newEntry){
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
	 * Inserts a new Subscription object in the database
	 * 
	 * @param The Subscription object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static Subscription addSubscription(Subscription newEntry){
		String sqlInsert = buildInsertStatement(newEntry);

		int id = 0;
		Subscription result = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert);
			result = getSubscription(id);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Removes a Subscription object from the database
	 * 
	 * @param The ID number of the Subscription to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeSubscription(int idNumber){
		String sqlDelete = "DELETE FROM subscription WHERE subscription_ID = " + idNumber;
		
		boolean success = false;
		try{
			success =ServerInterface.getReference().requestDelete(sqlDelete);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Builds the select statement to search for a Subscription with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(Subscription entity){
		String sqlSelect = "SELECT " +
				"subscription_ID, subscription_name, expire_date, contact_phone, contact_email " +
				"FROM subscription WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "subscription_ID = " + entity.getID() + " AND ";
		if(entity.getTitle() != null)
			sqlSelect += "subscription_name LIKE '%" + entity.getTitle().replace("'",  "\\'") + "%' AND ";
		if(entity.getExpirationDate() != null)
			sqlSelect += "expire_date = " + entity.getExpirationDate().getTime() + " AND ";
		if(entity.getContactPhone() > 0)
			sqlSelect += "contact_phone = " + entity.getContactPhone() + " AND ";
		if(entity.getContactEmail() != null)
			sqlSelect += "contact_email LIKE '%" + entity.getContactEmail().replace("'", "\\'") + "%' AND ";
		
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
	private static String buildUpdateStatement(Subscription entity){
		String sqlUpdate = "UPDATE subscription SET ";

		if(entity.getID() > 0)
			sqlUpdate += "subscription_ID = " + entity.getID() + ", ";
		if(entity.getTitle() != null)
			sqlUpdate += "subscription_name = '" + entity.getTitle().replace("'",  "\\'") + "', ";
		if(entity.getExpirationDate() != null)
			sqlUpdate += "expire_date = " + entity.getExpirationDate().getTime() + ", ";
		if(entity.getContactPhone() > 0)
			sqlUpdate += "contact_phone = " + entity.getContactPhone() + ", ";
		if(entity.getContactEmail() != null)
			sqlUpdate += "contact_email = '" + entity.getContactEmail().replace("'", "\\'") + "', ";
		
		if(sqlUpdate.endsWith(", "))//May not end with comma
			sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE subscription_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given Subscription
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(Subscription entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0){
			attributes += "subscription_ID, ";
			values += entity.getID() + ", ";
		}
		if(entity.getTitle() != null){
			attributes += "subscription_name, ";
			values += "'" + entity.getTitle().replace("'", "\\'") + "', ";
		}
		if(entity.getExpirationDate() != null){
			attributes += "expire_date, ";
			values += entity.getExpirationDate().getTime() + ", ";
		}
		if(entity.getContactPhone() > 0){
			attributes += "contact_phone, ";
			values += entity.getContactPhone() + ", ";
		}
		if(entity.getContactEmail() != null){
			attributes += "contact_email, ";
			values += "'" + entity.getContactEmail().replace("'", "\\'") + "', ";
		}
		if(attributes.endsWith(", ")){
			attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
			values = values.substring(0, values.length()-2);
		}

		return "INSERT subscription " + attributes + ") VALUES " + values + ")";
	}
	
	/**
	 * This builds the Subscription(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the Subscriptions acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static Subscription[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			Subscription[] result = new Subscription[nRows];
			rowSet.beforeFirst();
			for(int i=0; i < nRows && rowSet.next();i++){
				result[i] = new Subscription(
						rowSet.getInt("subscription_id"),
						rowSet.getString("subscription_name"),
						rowSet.getLong("expire_date") != 0 ? new Date(rowSet.getLong("expire_date")) : null,	//if check in date is NULL in database, set it to null in entity, if not create a new data object based on the long
						rowSet.getLong("contact_phone"),
						rowSet.getString("contact_email")
				);
					
			}
			return result;
		}
		catch(SQLException e){
			ErrorManager.handleError(e);
			return null;
		}
	}
	
	
	public static void main(String args[]){
		Subscription sub = new Subscription(0, null, null, -1L, null);
		System.out.println(buildSelectStatement(sub));
		
	}
/*	
	public static void main(String args[]){
		//Subscription r = new Subscription(0, "Library Management System Monthly", new Date(0), 6045555555L, "librislms@gmail.com");
		Subscription r = new Subscription(0,null,null,0,null);
		System.out.println(buildSelectStatement(r));
		ServerInterface.configureServerInterface("154.20.115.46",1500);
		Subscription result = addSubscription(r);
		Subscription result = getSubscription(r)[0];
		ServerInterface.getReference().closeConnection();
		
		System.out.println(result.getTitle());	
	}
	*/
}