package client.control.data;

import java.sql.SQLException;
import java.util.Date;
import com.sun.rowset.CachedRowSetImpl;

import client.serverinterface.ServerInterface;
import client.control.data.entity.management.ToDo;
import client.control.session.ErrorManager;
import client.control.session.RequestException;

/**
 * Control Object for ToDos
 * 
 * @author Jeremy Lerner, Peter Abelseth
 * @version 11
 *
 */
public class ToDoManager{
	
	/**
	 * Returns an array of ToDo objects with fields matching the initialized fields of a parameter. Returns an empty list should there be no such ToDos.
	 * 
	 * @param A ToDo object with some fields initialized
	 * @return An array of ToDo objects from the Server with fields matching those initialized in the parameter
	 */
	public static ToDo[] getToDo(ToDo searchKey){
		String sqlSelect = buildSelectStatement(searchKey);
		CachedRowSetImpl rowSet = null;
		ToDo[] result = null;
		try {
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch (RequestException e) {
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Returns a ToDo object with a given ID number
	 * 
	 * @param A ToDo ID number
	 * @return A ToDo object with the given ID number
	 */
	public static ToDo getToDo(int idNumber){
		String sqlSelect = "SELECT todo_ID, name, description, due_date, start_date, completed " +
				"FROM toDo WHERE todo_ID = " + idNumber;
		
		CachedRowSetImpl rowSet =  null;
		ToDo result[] = null;
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
	 * Edits an existing ToDo entry in the database
	 * 
	 * @param A ToDo object with updated fields. The ID number field must be set in the ToDo.
	 * @return True if successful, false otherwise
	 */
	public static boolean setToDo(ToDo newEntry){
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
	 * Inserts a new ToDo object in the database
	 * 
	 * @param The ToDo object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry
	 */
	public static ToDo addToDo(ToDo newEntry){
		String sqlInsert = buildInsertStatement(newEntry);
		int id = 0;
		ToDo newEntity = null;
		try{
			id = ServerInterface.getReference().requestInsert(sqlInsert);
			newEntity = getToDo(id);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return newEntity;
	}
	
	/**
	 * Removes a ToDo object from the database
	 * 
	 * @param The ID number of the ToDo to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeToDo(int idNumber){
		String sqlDelete = "DELETE FROM toDo WHERE todo_ID = " + idNumber;
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestDelete(sqlDelete);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return success;
	}
	
	/**
	 * Builds the select statement to search for a ToDo with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(ToDo entity){
		String sqlSelect = "SELECT " +
				"todo_ID, name, description, due_date, start_date, completed " +
				"FROM toDo WHERE ";
		
		if(entity.getID() > 0)
			sqlSelect += "todo_ID = " + entity.getID() + " AND ";
		if(entity.getTitle() != null)
			sqlSelect += "name LIKE '%" +entity.getTitle().replace("'", "\\'") + "%' AND ";
		if(entity.getDescription() != null)
			sqlSelect += "description LIKE '%" + entity.getDescription().replace("'",  "\\'") + "%' AND ";
		if(entity.getEndDate() != null)
			sqlSelect += "due_date = " + entity.getEndDate().getTime() + " AND ";
		if(entity.getStartDate() != null)
			sqlSelect += "start_date = " + entity.getStartDate().getTime() + " AND ";
		sqlSelect += "completed = " + entity.getCompleted() + " AND ";
		
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
	private static String buildUpdateStatement(ToDo entity){
		String sqlUpdate = "UPDATE toDo SET ";
		
		if(entity.getTitle() != null)
			sqlUpdate += "name = '" +entity.getTitle().replace("'", "\\'") + "', ";
		if(entity.getDescription() != null)
			sqlUpdate += "description = '" + entity.getDescription().replace("'","\\'") + "', ";
		if(entity.getEndDate() != null)
			sqlUpdate += "due_date = " + entity.getEndDate().getTime() + ", ";
		if(entity.getStartDate() != null)
			sqlUpdate += "start_date = " + entity.getStartDate().getTime() + ", ";

		if(sqlUpdate.endsWith(", "))
			sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE todo_ID = " + entity.getID();
	}
	
	/**
	 * Builds an insert statement of the given ToDo
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 */
	private static String buildInsertStatement(ToDo entity){
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "todo_ID, ";
			values += entity.getID() + ", ";
		}
		if(entity.getTitle() != null){
			attributes += "name, ";
			values += "'" + entity.getTitle().replace("'", "\\'") + "', ";
		}
		if(entity.getDescription() != null){
			attributes += "description, ";
			values += "'" + entity.getDescription().replace("'",  "\\'")+"', ";
		}
		if(entity.getEndDate() != null){
			attributes += "due_date, ";
			values += entity.getEndDate().getTime() + ", ";
		}
		if(entity.getStartDate() != null){
			attributes += "start_date, ";
			values += entity.getStartDate().getTime() + ", ";
		}
		attributes += "completed, ";
		values += entity.getCompleted() + ", ";
		if (attributes.endsWith(", ")){ //May not end with a comma
			attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
			values = values.substring(0, values.length()-2);
		}
		
		return "INSERT toDo " + attributes + ") VALUES " + values + ")";
	}
	
	/**
	 * This builds the ToDo(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the ToDos acquired from the database. If only expecting one, check length first and then take first element of array.
	 * @throws SQLException If couldn't get correct results from rowSet
	 */
	private static ToDo[] buildEntityFromRowSet(CachedRowSetImpl rowSet){
		if(rowSet == null)
			return null;
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			
			ToDo[] result = new ToDo[nRows];
			rowSet.beforeFirst();
			for(int i=0; rowSet.next() && i < nRows ;i++){
				result[i] = new ToDo(
						rowSet.getInt("todo_ID"),
						new Date(rowSet.getLong("start_date")),
						new Date(rowSet.getLong("due_date")),
						rowSet.getString("name"),
						rowSet.getString("description"),
						rowSet.getBoolean("completed")
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
		ToDo r = new ToDo(0, new Date(0), new Date(0), "Finish LMS", "Finish implementing the Libris LMS", false);
		System.out.println(buildInsertStatement(r));
		//154.20.115.46
		ServerInterface.configureServerInterface("154.20.115.46",1500);
		ToDo result = null;
		//for(int i=0;i<100;i++)
			result = addToDo(r);
		//ToDo result[] = getToDo(r);
		//System.out.println(removeToDo(24));
		
		//ServerInterface.getReference().closeConnection();
		
		System.out.println(result.getDescription());	
	}
	*/
}