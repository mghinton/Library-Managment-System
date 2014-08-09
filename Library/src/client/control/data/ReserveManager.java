package client.control.data;

import java.sql.SQLException;
import java.util.Date;

import com.sun.rowset.CachedRowSetImpl;

import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Reserve;
import client.control.session.ErrorManager;
import client.control.session.RequestException;

/**
 * Control Object for Reserves
 * 
 * @author Jeremy Lerner, Peter Abelseth
 * @version 7
 *
 */
public class ReserveManager{
	/**
	 * Returns an array of Reserve objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such Reserves.
	 * 
	 * @param A Reserve object with some fields initialized
	 * @return An array of Reserve objects from the Server with fields matching those initialized in the parameter
	 */
	public static Reserve[] getReserve(Reserve searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		
		CachedRowSetImpl rowSet = null;
		Reserve[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		
		return result;
	}
	
	/**
	 * Returns a Reserve object with a given ID number
	 * 
	 * @param A Reserve ID number
	 * @return A Reserve object with the given ID number
	 */
	public static Reserve getReserve(int idNumber){
		String sqlSelect = "SELECT reservation_ID, reserved_date, available_date, expire_date, user_user_ID, resource_resource_ID " +
				"FROM reservation WHERE reservation_ID = " + idNumber;
		
		CachedRowSetImpl rowSet = null;
		Reserve[] result = null;
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
	 * Edits an existing Reserve entry in the database
	 * 
	 * @param A Reserve object with updated fields. The ID number field must be set in the Reserve.
	 * @return True if successful, false otherwise
	 */
	public static boolean setReserve(Reserve newEntry){
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
	 * Inserts a new Reserve object in the database
	 * 
	 * @param The Reserve object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static Reserve addReserve(Reserve newEntry){
		String sqlInsert = buildInsertStatement(newEntry);
		String[] sqlCheck = buildInsertCheckStatements(newEntry);
		
		int id = 0;
		Reserve result = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert,sqlCheck);
			result = getReserve(id);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Removes a Reserve object from the database
	 * 
	 * @param The ID number of the Reserve to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeReserve(int idNumber){
		Reserve reserve = getReserve(idNumber);
		Reserve reserveKey = new Reserve(-1,-1, reserve.getResource(), null, null, null);
		Reserve[] reserves = ReserveManager.getReserve(reserveKey);
		if (reserves.length != 0){
			int oldestIndex = 0;
			for(int i=1;i<reserves.length;i++)
				if (reserves[i].getAvailableDate().getTime() < reserves[oldestIndex].getAvailableDate().getTime())
					oldestIndex = i;
			if (reserves[oldestIndex].getAvailableDate() == null){
				String query = "UPDATE reservation SET available_date = UNIX_TIMESTAMP(NOW())*1000, expire_date = (UNIX_TIMESTAMP(NOW()) + 2*7*24*60*60)*1000) WHERE reservation_ID = "+reserves[oldestIndex].getID();
				try{
					ServerInterface.getReference().requestUpdate(query);
				}catch(RequestException e){
					ErrorManager.handleError(e);
				}
			}
		}
		
		String sqlDelete = "DELETE FROM reservation WHERE reservation_ID = " + idNumber;
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestDelete(sqlDelete);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Builds the select statement to search for a Reserve with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(Reserve entity){
		String sqlSelect = "SELECT " +
				"reservation_ID, available_date, reserved_date, expire_date, resource_resource_ID, user_user_ID " +
				"FROM reservation WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "reservation_ID = " + entity.getID() + " AND ";
		if(entity.getAvailableDate() != null)
			sqlSelect += "available_date = " + entity.getAvailableDate().getTime() + " AND ";
		if(entity.getReservationDate() != null)
			sqlSelect += "reserved_date = " + entity.getReservationDate().getTime() + " AND ";
		if(entity.getEndDate() != null)
			sqlSelect += "expire_date = " + entity.getEndDate().getTime() + " AND ";
		if(entity.getResource() > 0)
			sqlSelect += "resource_resource_ID = " + entity.getResource() + " AND ";
		if(entity.getUserID() > 0)
			sqlSelect += "user_user_ID = " + entity.getUserID() + " AND ";
		
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
	private static String buildUpdateStatement(Reserve entity){
		String sqlUpdate = "UPDATE reservation SET ";
		
		if(entity.getAvailableDate() != null)
			sqlUpdate += "available_date = " + entity.getAvailableDate().getTime() + ", ";
		if(entity.getEndDate() != null)
			sqlUpdate += "expire_date = " + entity.getEndDate().getTime() + ", ";
		if(entity.getReservationDate() != null)
			sqlUpdate += "reserved_date = " + entity.getReservationDate().getTime() + ", ";
		if(entity.getResource() > 0)
			sqlUpdate += "resource_resource_ID = " + entity.getResource() + ", ";
		if(entity.getUserID() > 0)
			sqlUpdate += "user_user_ID = " + entity.getUserID() + ", ";

		if(sqlUpdate.endsWith(", "))
			sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE reservation_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given Reserve
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(Reserve entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "reservation_ID, ";
			values += entity.getID() + ", ";
		}
		if(entity.getAvailableDate() != null){
			attributes += "available_date, ";
			values += entity.getAvailableDate().getTime() + ", ";
		}
		if(entity.getEndDate() != null){
			attributes += "expire_date, ";
			values += entity.getEndDate().getTime() + ", ";
		}
		if(entity.getReservationDate() != null){
			attributes += "reserved_date, ";
			values += entity.getReservationDate().getTime() + ", ";
		}

		if(entity.getResource() > 0){
			attributes += "resource_resource_ID, ";
			values += entity.getResource() + ", ";
		}
		if(entity.getUserID() > 0 ){
			attributes += "user_user_ID, ";
			values += entity.getUserID() + ", ";
		}
		
		if (attributes.endsWith(", "))
			attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
		if (values.endsWith(", "))
			values = values.substring(0, values.length()-2);
		
		return "INSERT reservation " + attributes + ") VALUES " + values + ")";
	}
	
	private static String[] buildInsertCheckStatements(Reserve entity){
		String sqlCheckNotDuplicateReservation = 
				"SELECT reservation.reservation_ID " +
				"FROM reservation " +
				"WHERE user_user_ID = " + entity.getUserID();
		
		String sqlCheckNotAlreadyLoaning = 
				"SELECT loan.loan_ID " +
				"FROM loan, resourceCopy " +
				"WHERE " +
				"loan.user_user_ID = " + entity.getUserID() + " AND " +
				"loan.resourceCopy_resourceCopy_ID = resourceCopy.resourceCopy_ID AND " +
				"resourceCopy.resource_resource_ID = " + entity.getResource();
		
		//need to fix.
		String sqlCheckNotAllReference =
				"SELECT sum " +
				"FROM( " +
					"SELECT SUM(count)'sum' " +
					"FROM ( " +
						"SELECT COUNT(resourceCopy.resourceCopy_ID)'count' " +
						"FROM resourceCopy " +
						"WHERE " +
						"resourceCopy.enabled = TRUE AND " +
						"resourceCopy.resource_resource_ID = " + entity.getResource() + " " + 
						"UNION ALL " +
						"SELECT -COUNT(reference.resourceCopy_resourceCopy_ID)'count' " +
						"FROM reference,resourceCopy " + 
						"WHERE " +
						"reference.resourceCopy_resourceCopy_ID = resourceCopy.resourceCopy_ID AND " +
						"resourceCopy.resource_resource_ID = " + entity.getResource() + " AND " +
						"resourceCopy.enabled = TRUE " +
					") AS resourceCopyCount " +
				") AS count " +
				"WHERE sum <= 0";
				
		String[] sqlCheck = {sqlCheckNotDuplicateReservation, sqlCheckNotAlreadyLoaning, sqlCheckNotAllReference};
		return sqlCheck;
	}
	
	/**
	 * This builds the Reserve(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the Reserves acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static Reserve[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			Reserve	 [] result = new Reserve[nRows];
			rowSet.beforeFirst();
			for(int i=0; i < nRows && rowSet.next();i++){
				result[i] = new Reserve(
						rowSet.getInt("reservation_ID"),
						rowSet.getInt("user_user_ID"),
						rowSet.getInt("resource_resource_ID"),
						new Date(rowSet.getLong("reserved_date")),
						new Date(rowSet.getLong("available_date")),
						new Date(rowSet.getLong("expire_date"))
				);
						
			}
			return result;
		}
		catch(SQLException e){
			ErrorManager.handleError(new InstantiationException("System Error: Error building the Reserve entity."));
			return null;
		}
	}
	
	public static void main(String args[]){
		Reserve r = new Reserve(0, 100000000, 100, new Date(0), new Date(0), new Date(0));
		
		System.out.println(buildInsertStatement(r));
		ServerInterface.configureServerInterface("154.20.115.46",1500);
		Reserve result = addReserve(r);
		
		ServerInterface.getReference().closeConnection();
		
		System.out.println(result.getUserID());	
	}
	

}