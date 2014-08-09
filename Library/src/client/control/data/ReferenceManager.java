package client.control.data;

import java.sql.SQLException;
import java.util.Date;

import com.sun.rowset.CachedRowSetImpl;

import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Reference;
import client.control.data.entity.resource.ResourceCopy;
import client.control.session.ErrorManager;
import client.control.session.RequestException;

/**
 * Control Object for References
 * 
 * @author Jeremy Lerner
 * @version 4
 *
 */
public class ReferenceManager{

	/**
	 * Returns an array of Reference objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such References.
	 * 
	 * @param A Reference object with some fields initialized
	 * @return An array of Reference objects from the Server with fields matching those initialized in the parameter
	 */
	public static Reference[] getReference(Reference searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		
		CachedRowSetImpl rowSet = null;
		Reference[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		
		return result;
	}
	
	/**
	 * Returns a Reference object with a given ID number
	 * 
	 * @param A Reference ID number
	 * @return A Reference object with the given ID number
	 */
	public static Reference getReference(int idNumber){
		String sqlSelect = "SELECT reference_ID, placed_date, expire_date, user_user_ID, resourceCopy_resourceCopy_ID " +
				"FROM reference WHERE reference_ID = " + idNumber;
		
		CachedRowSetImpl rowSet = null;
		Reference[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		
		if(result != null && result.length != 0)
			return result[0];
		return null;
	}
	
	/**
	 * Edits an existing Reference entry in the database
	 * 
	 * @param A Reference object with updated fields. The ID number field must be set in the Reference.
	 * @return True if successful, false otherwise
	 */
	public static boolean setReference(Reference newEntry){
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
	 * Inserts a new Reference object in the database
	 * 
	 * @param The Reference object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static Reference addReference(Reference newEntry){
		
		ResourceCopy resourceCopy = ResourceCopyManager.getResourceCopy(newEntry.getResourceCopy());
		if (resourceCopy.getOwnerID() == newEntry.getUserID())
			newEntry = new Reference(newEntry.getID(),newEntry.getResourceCopy(), newEntry.getStartDate(), null, newEntry.getUserID());
		
		try{
			if (!ResourceCopyManager.isAvailable(newEntry.getResourceCopy()))
				throw new ControlException("This resource is not currently available");
		}catch(ControlException e){
			ErrorManager.handleError(e);
			return null;
		}
				
		
		String sqlInsert = buildInsertStatement(newEntry);
		int id = 0;
		Reference result = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert);
			result = getReference(id);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Removes a Reference object from the database
	 * 
	 * @param The ID number of the Reference to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeReference(int idNumber){
		String sqlDisable = "DELETE FROM reference WHERE reference_ID = " + idNumber;
		
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestUpdate(sqlDisable);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Builds the select statement to search for a Reference with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(Reference entity){
		String sqlSelect = "SELECT " +
				"reference_ID, placed_date, expire_date, user_user_ID, resourceCopy_resourceCopy_ID " +
				"FROM reference WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "reference_ID = " + entity.getID() + " AND ";
		if(entity.getStartDate() != null)
			sqlSelect += "placed_date = " + entity.getStartDate().getTime() + " AND ";
		if(entity.getEndDate() != null)
			sqlSelect += "expire_date = " + entity.getEndDate().getTime() + " AND ";
		if(entity.getUserID() > 0)
			sqlSelect += "user_user_ID = " + entity.getUserID() + " AND ";
		if(entity.getResourceCopy() >= 0)
			sqlSelect += "resourceCopy_resourceCopy_ID = " + entity.getResourceCopy() + " AND ";		
		
		
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
	private static String buildUpdateStatement(Reference entity){
		String sqlUpdate = "UPDATE reference SET ";
		
		if(entity.getID() > 0)
			sqlUpdate += "reference_ID = " + entity.getID() + ", ";
		if(entity.getStartDate() != null)
			sqlUpdate += "placed_date = " + entity.getStartDate().getTime() + ", ";
		if(entity.getEndDate() != null)
			sqlUpdate += "expire_date = " + entity.getEndDate().getTime() + ", ";
		if(entity.getUserID() > 0)
			sqlUpdate += "user_user_ID = " + entity.getUserID() + ", ";
		if(entity.getResourceCopy() >= 0)
			sqlUpdate += "resourceCopy_resourceCopy_ID = " + entity.getResourceCopy() + ", ";		


		if(sqlUpdate.endsWith(", "))
			sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE reference_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given Reference
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(Reference entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "reference_ID, ";
			values += entity.getID() + ", ";
		}
		if(entity.getStartDate() != null){
			attributes += "placed_date, ";
			values += entity.getStartDate().getTime() + ", ";
		}
		if(entity.getEndDate() != null){
			attributes += "expire_date, ";
			values += entity.getEndDate().getTime() + ", ";
		}		
		if(entity.getUserID() > 0 ){
			attributes += "user_user_ID, ";
			values += entity.getUserID() + ", ";
		}
		if(entity.getResourceCopy() > 0 ){
			attributes += "resourceCopy_resourceCopy_ID, ";
			values += entity.getResourceCopy() + ", ";
		}
		
		if (attributes.endsWith(", ")){
			attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
			values = values.substring(0, values.length()-2);
		}
		return "INSERT reference " + attributes + ") VALUES " + values + ")";
	}
	
	/**
	 * This builds the Reference(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the References acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static Reference[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			Reference[] result = new Reference[nRows];
			rowSet.beforeFirst();
			for(int i=0; i < nRows && rowSet.next();i++){
				result[i] = new Reference(
						rowSet.getInt("reference_ID"),
						rowSet.getInt("resourceCopy_resourceCopy_ID"),
						new Date(rowSet.getLong("placed_date")),
						rowSet.getLong("expire_date") != 0 ? new Date(rowSet.getLong("expire_date")) : null,
						rowSet.getInt("user_user_ID")
				);

			}
			return result;
		}
		catch(SQLException e){
			ErrorManager.handleError(e);
			return null;
		}
	}
	
	/*
	public static void main(String args[]){
		Reference r = new Reference(0, 0, new Date(), new Date(0), 0);


		System.out.println(buildInsertStatement(r));
		ServerInterface.configureServerInterface("localhost",1600);
		addReference(r);
		Reference result = getReference(r)[0];
		ServerInterface.getReference().closeConnection();
		
		System.out.println(result.getStartDate().getTime());	
	}
	*/
}