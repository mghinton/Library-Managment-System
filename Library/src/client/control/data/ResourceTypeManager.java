package client.control.data;

import java.sql.SQLException;
import com.sun.rowset.CachedRowSetImpl;

import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.ResourceType;
import client.control.session.ErrorManager;
import client.control.session.RequestException;

/**
 * Control Object for ResourceTypes
 * 
 * @author Peter Abelseth
 * @version 11
 *
 */
public class ResourceTypeManager{
	
	/**
	 * Returns an array of ResourceType objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such ResourceTypes.
	 * 
	 * @param A ResourceType object with some fields initialized
	 * @return An array of ResourceType objects from the Server with fields matching those initialized in the parameter
	 */
	public static ResourceType[] getResourceType(ResourceType searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		CachedRowSetImpl rowSet = null;
		ResourceType[] result = null;
		try {
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch (RequestException e) {
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Returns a ResourceType object with a given ID number
	 * 
	 * @param A ResourceType ID number
	 * @return A ResourceType object with the given ID number
	 */
	public static ResourceType getResourceType(int idNumber){
		String sqlSelect = "SELECT type_ID, type_name, title_heading, creator_heading, serial_heading, company_heading, fine_amount, fine_max, student_loan, faculty_loan, staff_loan, enabled " +
				"FROM resourceType WHERE type_ID = " + idNumber;
		
		CachedRowSetImpl rowSet = null;
		ResourceType result[] = null;
		try {
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch (RequestException e) {
			ErrorManager.handleError(e);
		}
		if(result != null && result.length != 0)
			return result[0];
		return null;
	}
	
	/**
	 * Edits an existing ResourceType entry in the database
	 * 
	 * @param A ResourceType object with updated fields. The ID number field must be set in the ResourceType.
	 * @return True if successful, false otherwise
	 */
	public static boolean setResourceType(ResourceType newEntry){
		String sqlUpdate = buildUpdateStatement(newEntry);
		boolean success = false;
		try {
			success = ServerInterface.getReference().requestUpdate(sqlUpdate);
		} catch (RequestException e) {
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Inserts a new ResourceType object in the database
	 * 
	 * @param The ResourceType object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static ResourceType addResourceType(ResourceType newEntry){
		String sqlInsert = buildInsertStatement(newEntry);
		int id = 0;
		ResourceType newEntity = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert);
			newEntity =  getResourceType(id);
		} catch (RequestException e){
			ErrorManager.handleError(e);
		}
		return newEntity;
	}
	
	/**
	 * Removes a ResourceType object from the database
	 * 
	 * @param The ID number of the ResourceType to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeResourceType(int idNumber){
		String sqlDisable = "UPDATE resourceType SET enabled = FALSE WHERE type_ID = " + idNumber;		//this will disable this resourceType and all associated resources!!!
		
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestUpdate(sqlDisable);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Builds the select statement to search for a ResourceType with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(ResourceType entity){
		String sqlSelect = "SELECT " +
				"type_ID, type_name, title_heading, creator_heading, serial_heading, company_heading, fine_amount, fine_max, student_loan, faculty_loan, staff_loan, enabled " +
				"FROM resourceType WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "type_ID = " + entity.getID() + " AND ";
		if(entity.getName() != null)
			sqlSelect += "type_name LIKE '%" +entity.getName().replace("'", "\\'") + "%' AND ";
		if(entity.getTitleLabel() != null)
			sqlSelect += "title_heading LIKE '%" + entity.getTitleLabel().replace("'", "\\'") + "%' AND ";
		if(entity.getCreatorLabel() != null)
			sqlSelect += "creator_heading LIKE '%" + entity.getName().replace("'", "\\'") + "%' AND ";
		if(entity.getSerialNumberLabel() != null)
			sqlSelect += "serial_heading LIKE '%" + entity.getSerialNumberLabel().replace("'", "\\'") + "%' AND ";
		if(entity.getCompanyLabel() != null)
			sqlSelect += "company_heading LIKE '%" + entity.getCompanyLabel().replace("'", "\\'") + "%' AND ";
		if(entity.getFineIncrementAmount() >= 0)
			sqlSelect += "fine_amount = " + entity.getFineIncrementAmount() + " AND ";
		if(entity.getMaxFineAmount() >= 0)
			sqlSelect += "fine_max = " + entity.getMaxFineAmount() + " AND ";
		if(entity.getStudentPeriod() >= 0)
			sqlSelect += "student_loan = " + entity.getStudentPeriod() + " AND ";
		if(entity.getFacultyPeriod() >= 0)
			sqlSelect += "faculty_loan = " + entity.getFacultyPeriod() + " AND ";
		if(entity.getStaffPeriod() >= 0)
			sqlSelect += "staff_loan = " + entity.getStaffPeriod() + " AND ";
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
	private static String buildUpdateStatement(ResourceType entity){
		String sqlUpdate = "UPDATE resourceType SET ";
		if(entity.getName() != null)
			sqlUpdate += "type_name = '" +entity.getName().replace("'", "\\'") + "', ";
		if(entity.getTitleLabel() != null)
			sqlUpdate += "title_heading = '" + entity.getTitleLabel().replace("'","\\'") + "', ";
		if(entity.getCreatorLabel() != null)
			sqlUpdate += "creator_heading = '" + entity.getName().replace("'", "\\'") + "', ";
		if(entity.getSerialNumberLabel() != null)
			sqlUpdate += "serial_heading = '" + entity.getSerialNumberLabel().replace("'", "\\'") + "', ";
		if(entity.getCompanyLabel() != null)
			sqlUpdate += "company_heading = '" + entity.getCompanyLabel().replace("'", "\\'") + "', ";
		if(entity.getFineIncrementAmount() >= 0)
			sqlUpdate += "fine_amount = " + entity.getFineIncrementAmount() + ", ";
		if(entity.getMaxFineAmount() >= 0)
			sqlUpdate += "fine_max = " + entity.getMaxFineAmount() + ", ";
		if(entity.getStudentPeriod() >= 0)
			sqlUpdate += "student_loan = " + entity.getStudentPeriod() + ", ";
		if(entity.getFacultyPeriod() >= 0)
			sqlUpdate += "faculty_loan = " + entity.getFacultyPeriod() + ", ";
		if(entity.getStaffPeriod() >= 0)
			sqlUpdate += "staff_loan = " + entity.getStaffPeriod() + ", ";
		
		sqlUpdate += "enabled = " + entity.getEnabled() + ", ";

		sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE type_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given ResourceType
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(ResourceType entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "type_ID, ";
			values += entity.getID() + ", ";
		}
		if(entity.getName() != null){
			attributes += "type_name, ";
			values += "'" + entity.getName().replace("'", "\\'") + "', ";
		}
		if(entity.getTitleLabel() != null){
			attributes += "title_heading, ";
			values += "'" + entity.getTitleLabel().replace("'", "\\'") + "', ";
		}
		if(entity.getCreatorLabel() != null){
			attributes += "creator_heading, ";
			values += "'" +entity.getName().replace("'", "\\'") + "', ";
		}
		if(entity.getSerialNumberLabel() != null){
			attributes += "serial_heading, ";
			values += "'" + entity.getSerialNumberLabel().replace("'", "\\'") + "', ";
		}
		if(entity.getCompanyLabel() != null){
			attributes += "company_heading, ";
			values += "'" + entity.getCompanyLabel().replace("'", "\\'") + "', ";
		}
		if(entity.getFineIncrementAmount() != 0){
			attributes += "fine_amount, ";
			values += entity.getFineIncrementAmount() + ", ";
		}
		if(entity.getMaxFineAmount() != 0){
			attributes += "fine_max, ";
			values += entity.getMaxFineAmount() + ", ";
		}
		if(entity.getStudentPeriod() >= 0){
			attributes += "student_loan, ";
			values += entity.getStudentPeriod() + ", ";
		}
		if(entity.getFacultyPeriod() >= 0){
			attributes += "faculty_loan, ";
			values += entity.getFacultyPeriod() + ", ";
		}
		if(entity.getStaffPeriod() >= 0){
			attributes += "staff_loan, ";
			values += entity.getStaffPeriod() + ", ";
		}
		attributes += "enabled, ";
		values += entity.getEnabled() + ", ";
		
		attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
		values = values.substring(0, values.length()-2);

		return "INSERT resourceType " + attributes + ") VALUES " + values + ")";
	}
	
	/**
	 * This builds the ResourceType(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the ResourceTypes acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static ResourceType[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			ResourceType[] result = new ResourceType[nRows];
			rowSet.beforeFirst();
			for(int i=0; i < nRows && rowSet.next();i++){
				result[i] = new ResourceType(
						rowSet.getInt("type_ID"),
						rowSet.getString("type_name"),
						rowSet.getString("title_heading"),
						rowSet.getString("creator_heading"),
						rowSet.getString("company_heading"),
						rowSet.getString("serial_heading"),
						rowSet.getInt("staff_loan"),
						rowSet.getInt("faculty_loan"),
						rowSet.getInt("student_loan"),
						rowSet.getFloat("fine_max"),
						rowSet.getFloat("fine_amount"),
						rowSet.getBoolean("enabled"));	
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
		ResourceType r = new ResourceType(2,"DvD","Title","Director","Studio","MPAA Number",0,0,0,0,0,true);
		System.out.println(buildInsertStatement(r));
		ServerInterface.configureServerInterface("154.20.115.46",1500);
		ResourceType result = addResourceType(r);
		//ServerInterface.getReference().closeConnection();		
		System.out.println(result.getTitleLabel());	
	}
	*/

}