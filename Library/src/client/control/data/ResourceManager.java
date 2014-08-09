package client.control.data;

import java.sql.SQLException;
import java.util.Date;
import com.sun.rowset.CachedRowSetImpl;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Resource;
import client.control.session.ErrorManager;
import client.control.session.RequestException;

/**
 * Control Object for Resources
 * 
 * @author Jeremy Lerner
 * @version 5
 *
 */
public class ResourceManager{
	
	/**
	 * Returns an array of Resource objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such Resources.
	 * 
	 * @param A Resource object with some fields initialized
	 * @return An array of Resource objects from the Server with fields matching those initialized in the parameter
	 */
	public static Resource[] getResource(Resource searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		CachedRowSetImpl rowSet = null;
		Resource[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Returns a Resource object with a given ID number
	 * 
	 * @param A Resource ID number
	 * @return A Resource object with the given ID number
	 */
	public static Resource getResource(int idNumber){
		String sqlSelect = "SELECT resource_ID, serial, title, creator, publication_date, description, company, enabled, resourceType_type_ID " +
				"FROM resource WHERE resource_ID = " + idNumber;
		CachedRowSetImpl rowSet = null;
		Resource[] result = null;
		
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
	 * Edits an existing Resource entry in the database
	 * 
	 * @param A Resource object with updated fields. The ID number field must be set in the Resource.
	 * @return True if successful, false otherwise
	 */
	public static boolean setResource(Resource newEntry){
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
	 * Inserts a new Resource object in the database
	 * 
	 * @param The Resource object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static Resource addResource(Resource newEntry){
		String sqlInsert = buildInsertStatement(newEntry);
		
		int id = 0;
		Resource result = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert);
			result = getResource(id);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Removes a Resource object from the database
	 * 
	 * @param The ID number of the Resource to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeResource(int idNumber){
		String sqlDisable = "UPDATE resource SET enabled = FALSE WHERE resource_ID = " + idNumber;		//this will disable this Resource and all associated resources!!!
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestUpdate(sqlDisable);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Builds the select statement to search for a Resource with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(Resource entity){
		String sqlSelect = "SELECT " +
				"resource_ID, serial, title, creator, publication_date, company, resourceType_type_ID, enabled " +
				"FROM resource WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "resource_ID = " + entity.getID() + " AND ";
		if (entity.getSerialNumber() != null)
			sqlSelect += "serial LIKE '%" + entity.getSerialNumber().replace("'",  "\\'") + "%' AND ";
		if(entity.getTitle() != null)
			sqlSelect += "title LIKE '%" + entity.getTitle().replace("'", "\\'") + "%' AND ";
		if(entity.getCreator() != null)
			sqlSelect += "creator LIKE '%" + entity.getCreator().replace("'", "\\'") + "%' AND ";
		if(entity.getPublicationDate() != null)
			sqlSelect += "publication_date = " + entity.getPublicationDate().getTime() + " AND ";
		if(entity.getCompany() != null)
			sqlSelect += "company LIKE '%" + entity.getCompany().replace("'", "\\'") + "%' AND ";
		if (entity.getResourceType() > 0)
			sqlSelect += "resourceType_type_ID = " + entity.getResourceType() + " AND ";
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
	private static String buildUpdateStatement(Resource entity){
		String sqlUpdate = "UPDATE resource SET ";

		if (entity.getSerialNumber() != null)
			sqlUpdate += "serial = '" + entity.getSerialNumber().replace("'",  "\\'") + "', ";
		if(entity.getTitle() != null)
			sqlUpdate += "title = '" + entity.getTitle().replace("'", "\\'") + "', ";
		if(entity.getCreator() != null)
			sqlUpdate += "creator = '" + entity.getCreator().replace("'", "\\'") + "', ";
		if(entity.getPublicationDate() != null)
			sqlUpdate += "publication_date = " + entity.getPublicationDate().getTime() + ", ";
		if(entity.getCompany() != null)
			sqlUpdate += "company = '" + entity.getCompany().replace("'", "\\'") + "', ";
		if (entity.getResourceType() > 0)
			sqlUpdate += "resourceType_type_ID = " + entity.getResourceType() + ", ";

		sqlUpdate += "enabled = " + entity.getEnabled() + ", ";

		sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE resource_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given Resource
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(Resource entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "resource_ID, ";
			values += entity.getID() + ", ";
		}
		if(entity.getSerialNumber() != null){
			attributes += "serial, ";
			values += "'" + entity.getSerialNumber().replace("'", "\\'") + "', ";
		}
		if(entity.getTitle() != null){
			attributes += "title, ";
			values += "'" + entity.getTitle().replace("'", "\\'") + "', ";
		}
		if(entity.getCreator() != null){
			attributes += "creator, ";
			values += "'" +entity.getCreator().replace("'", "\\'") + "', ";
		}
		if(entity.getCompany() != null){
			attributes += "company, ";
			values += "'" + entity.getCompany().replace("'", "\\'") + "', ";
		}
		if(entity.getPublicationDate() != null){
			attributes += "publication_date, ";
			values += entity.getPublicationDate().getTime() + ", ";
		}
		if(entity.getResourceType() > 0){
			attributes += "resourceType_type_ID, ";
			values += entity.getResourceType() + ", ";
		}
		
		attributes += "enabled, ";
		values += entity.getEnabled() + ", ";
		
		attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
		values = values.substring(0, values.length()-2);

		return "INSERT resource " + attributes + ") VALUES " + values + ")";
	}
	
	/**
	 * This builds the Resource(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the Resources acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static Resource[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			Resource[] result = new Resource[nRows];
			rowSet.beforeFirst();
			for(int i=0; i < nRows && rowSet.next();i++){
				result[i] = new Resource(
						rowSet.getInt("resource_ID"), 
						rowSet.getInt("resourceType_type_ID"), 
						rowSet.getString("title"),
						rowSet.getString("creator"), 
						rowSet.getString("company"), 
						rowSet.getString("serial"), 
						new Date(rowSet.getLong("publication_date")), 
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
	
	/*
	public static void main(String args[]){
		Resource r = new Resource(1, 2, "Jeremy's control class adventure", "Francis Ford Coppola", "Langara Pictures", "1337", new Date(0), true);

		System.out.println(buildInsertStatement(r));
		ServerInterface.configureServerInterface("154.20.115.46",1500);
		addResource(r);
		Resource result = getResource(r)[0];
		//ServerInterface.getReference().closeConnection();
		
		System.out.println(result.getTitle());	
	}
	*/

}