package client.control.data;

import java.sql.SQLException;
import com.sun.rowset.CachedRowSetImpl;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Loan;
import client.control.data.entity.resource.Reference;
import client.control.data.entity.resource.Reserve;
import client.control.data.entity.resource.ResourceCopy;
import client.control.session.ErrorManager;
import client.control.session.RequestException;

/**
 * Control Object for ResourceCopys
 * 
 * @author Jeremy Lerner
 * @version 6
 *
 */
public class ResourceCopyManager{
	
	/**
	 * Returns an array of ResourceCopy objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such ResourceCopys.
	 * 
	 * @param A ResourceCopy object with some fields initialized
	 * @return An array of ResourceCopy objects from the Server with fields matching those initialized in the parameter
	 */
	public static ResourceCopy[] getResourceCopy(ResourceCopy searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		CachedRowSetImpl rowSet = null;
		ResourceCopy[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		
		return result;
	}
	
	/**
	 * Returns a ResourceCopy object with a given ID number
	 * 
	 * @param A ResourceCopy ID number
	 * @return A ResourceCopy object with the given ID number
	 */
	public static ResourceCopy getResourceCopy(int idNumber){
		String sqlSelect = "SELECT resourceCopy_ID, copy_ID, resource_resource_ID, user_user_ID, enabled " +
				"FROM resourceCopy WHERE resourceCopy_ID = " + idNumber;
		
		CachedRowSetImpl rowSet = null;
		ResourceCopy[] result = null;
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
	 * Edits an existing ResourceCopy entry in the database
	 * 
	 * @param A ResourceCopy object with updated fields. The ID number field must be set in the ResourceCopy.
	 * @return True if successful, false otherwise
	 */
	public static boolean setResourceCopy(ResourceCopy newEntry){
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
	 * Inserts a new ResourceCopy object in the database
	 * 
	 * @param The ResourceCopy object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static ResourceCopy addResourceCopy(ResourceCopy newEntry){
		String sqlInsert = buildInsertStatement(newEntry);

		int id = 0;
		ResourceCopy result = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert);
			result = getResourceCopy(id);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Removes a ResourceCopy object from the database
	 * 
	 * @param The ID number of the ResourceCopy to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeResourceCopy(int idNumber){
		String sqlDisable = "UPDATE resourceCopy SET enabled = FALSE WHERE resourceCopy_ID = " + idNumber;		//this will disable this ResourceCopy and all associated ResourceCopys!!!

		boolean success = false;
		try{
			success = ServerInterface.getReference().requestUpdate(sqlDisable);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Builds the select statement to search for a ResourceCopy with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(ResourceCopy entity){
		String sqlSelect = "SELECT " +
				"resourceCopy_ID, copy_ID, resource_resource_ID, user_user_ID, enabled " +
				"FROM ResourceCopy WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "resourceCopy_ID = " + entity.getID() + " AND ";
		if(entity.getCopyID() > 0)
			sqlSelect += "copy_ID = " + entity.getCopyID() + " AND ";
		if (entity.getResource() > 0)
			sqlSelect += "resource_resource_ID = " + entity.getResource() + " AND ";
		if (entity.getOwnerID() > 0)
			sqlSelect += "user_user_ID = " + entity.getOwnerID() + " AND ";
		sqlSelect += "enabled = " + entity.getEnabled() + " AND ";

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
	private static String buildUpdateStatement(ResourceCopy entity){
		String sqlUpdate = "UPDATE resourceCopy SET ";

		if(entity.getCopyID() > 0)
			sqlUpdate += "copy_ID = " + entity.getCopyID() + " AND ";
		if(entity.getResource() > 0)
			sqlUpdate += "resource_resource_ID = " + entity.getResource() + " AND ";
		if(entity.getOwnerID() > 0)
			sqlUpdate += "user_user_ID = " + entity.getOwnerID() + " AND ";
		sqlUpdate += "enabled = " + entity.getEnabled() + ", ";

		sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE resourceCopy_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given ResourceCopy
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(ResourceCopy entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "resourceCopy_ID, ";
			values += entity.getID() + ", ";
		}
		if(entity.getCopyID() > 0 ){
			attributes += "copy_ID, ";
			values += entity.getCopyID() + ", ";
		}
		if(entity.getResource() > 0 ){
			attributes += "resource_resource_ID, ";
			values += entity.getResource() + ", ";
		}
		if(entity.getOwnerID() > 0 ){
			attributes += "user_user_ID, ";
			values += entity.getOwnerID() + ", ";
		}
		
		attributes += "enabled, ";
		values += entity.getEnabled() + ", ";
		
		attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
		values = values.substring(0, values.length()-2);

		return "INSERT resourceCopy " + attributes + ") VALUES " + values + ")";
	}
	
	/**
	 * This builds the ResourceCopy(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the ResourceCopys acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static ResourceCopy[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			ResourceCopy[] result = new ResourceCopy[nRows];
			rowSet.beforeFirst();
			for(int i=0; i < nRows && rowSet.next();i++){
				result[i] = new ResourceCopy(
						rowSet.getInt("resourceCopy_ID"),
						rowSet.getInt("resource_resource_ID"),
						rowSet.getInt("copy_ID"),
						rowSet.getInt("user_user_ID"),
						rowSet.getBoolean("enabled")
				);

			}
			return result;
		}
		catch(SQLException e){
			ErrorManager.handleError(e);
			return null;
		}
	}
	
	/**
	 * Returns the number of enabled copies of a given resource that are not on loan, reference, or reserve
	 * 
	 * @param The resource to count copies of
	 * @return The number of enabled copies of the given resource that are not on loan, reference, or reserve
	 */
	private static int availableCopies(int resourceID){
		ResourceCopy resourceCopyKey = new ResourceCopy(-1,resourceID, -1, -1, true);
		ResourceCopy[] resourceCopies = getResourceCopy(resourceCopyKey);
		int numberOfCopies = resourceCopies.length;
		
		Reserve reserveKey = new Reserve(-1,-1, resourceID, null, null, null);
		int numberOfReserves = ReserveManager.getReserve(reserveKey).length;
		
		int numberOfLoans = 0;
		boolean isOnReference = false;
		
		for(int i=0;i<numberOfCopies;i++){
			Loan loanKey = new Loan(-1,resourceCopies[i].getID(), -1, null, null, null, -1, false);
			Loan[] loans = LoanManager.getLoan(loanKey);
			if ((loans.length != 0) && (loans[0].getCheckInDate() == null))
				numberOfLoans++;
			if (!isOnReference){
				Reference referenceKey = new Reference(-1,resourceCopies[i].getID(), null, null, -1);
				isOnReference = (ReferenceManager.getReference(referenceKey).length > 0);
			}
		}
		
		if (isOnReference)
			return (numberOfCopies - numberOfLoans - numberOfReserves - 1);
		
		return (numberOfCopies - numberOfLoans - numberOfReserves);
	}
	
	/**
	 * Returns true if a given resource copy is available to be loaned/put on reference
	 * 
	 * @param The ID of the resource copy to check
	 * @return True if the given resource copy is available to be loaned/put on reference; false otherwise
	 */
	public static boolean isAvailable(int resourceCopyID){
		Loan loanKey = new Loan(-1,resourceCopyID, -1, null, null, null, -1, false);
		if (LoanManager.getLoan(loanKey).length > 0)
			return false;
		Reference referenceKey = new Reference(-1,resourceCopyID, null, null, -1);
		if (ReferenceManager.getReference(referenceKey).length > 0)
			return false;
		
		ResourceCopy resourceCopy = getResourceCopy(resourceCopyID);
		return (availableCopies(resourceCopy.getResource()) > 0);
	}
	
	/*
	public static void main(String args[]){
		ResourceCopy r = new ResourceCopy(100, 2, 0, 0, true);


		System.out.println(buildInsertStatement(r));
		ServerInterface.configureServerInterface("154.20.115.46",1500);
		addResourceCopy(r);
		ResourceCopy result = getResourceCopy(r)[0];
		//ServerInterface.getReference().closeConnection();
		
		System.out.println(result.getCopyID());	
	}
	*/

}