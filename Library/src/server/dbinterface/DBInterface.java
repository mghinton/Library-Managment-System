package server.dbinterface;

import java.sql.*;

import server.control.errormanagement.ErrorManager;

import com.sun.rowset.CachedRowSetImpl;


/**
 * This class is the interface to the database and utilises JDBC
 * 
 * @author Peter Abelseth
 * @version 4
 *
 */
public class DBInterface {
	
	private static DBInterface singleton = null;	//the single object
	
	final private static String driver = "com.mysql.jdbc.Driver";	//the driver of the JDBC connection
	
	private String url;	//the URL of the database
	private String userName;	//database username
	private String password;	//database password
	private Connection connection = null;	//connection to the database
	

	/**
	 * Empty constructor of the DBInterface. Using Singleton design Pattern
	 */
	private DBInterface() {
		
	}
	
	/**
	 * Initialises the JDBC connection and 
	 * @param url	The url of the database
	 * @param dbName	The name of the database to use
	 * @param userName	The username for the database to use
	 * @param password	The password of the username for the database
	 * @return singleton	The singleton object of the DBInterface
	 */
	public static synchronized DBInterface configureDBInterface(String url, String userName, String password){
		if(singleton != null){
			try {
				singleton.connection.close();
			} catch (SQLException e) {
				//do nothing, not being used anymore anyways
			}
		}

		singleton = new DBInterface();
		singleton.url = url;
		singleton.userName = userName;
		singleton.password = password;
		singleton.createConnection();
		
		return singleton;	//return the singleton object
	}
	
	/**
	 * Gets the singleton object of the datasbase
	 * @return	singleton The singleton object of the DBInterface, returns null if DBInterface hasn't been created yet
	 */
	public static synchronized DBInterface getReference()throws IllegalArgumentException{
		if(singleton == null)
			throw new IllegalArgumentException();	//change to better exception
		return singleton;
	}
	
	
	/**
	 * Closes the JDBC connection to the database
	 */
	public static synchronized void close(){
		if(singleton != null && singleton.connection != null){
			try {
				singleton.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		singleton = null;
	}
	
	/**
	 * Executes the given query and returns the result as a CachedRowSet
	 * @param sqlStatement	The query to perform on the database
	 * @return rowSet	The result of the query on the database
	 * @throws RuntimeException If the syntax of the sqlStatement is bad
	 */
	public CachedRowSetImpl executeSelect(String sqlStatement) throws RuntimeException{
		CachedRowSetImpl rowSet = null;
		Statement statement = null;
		try {
			statement = connection.createStatement();
		}
		catch(SQLException e){
			ErrorManager.getReference().fatalSubystemError(e, this);
			throw new RuntimeException("Database failure, please try again");
		}
		try{
			ResultSet resultSet = statement.executeQuery(sqlStatement);
			rowSet = new CachedRowSetImpl();
			rowSet.populate(resultSet);
		} catch(SQLException e){
			throw new IllegalArgumentException("Invalid Query Statement");
		}
		return rowSet;
	}
	
	/**
	 * Executes the given statement as an insert on the database
	 * @param sqlStatement The statement to execute
	 * @return id The id of the inserted record, returns -1 if failed insertion
	 * @throws RuntimeException If the syntax of the sqlStatement is bad
	 */
	public int executeInsert(String sqlStatement)throws RuntimeException{
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch(SQLException e){
			ErrorManager.getReference().fatalSubystemError(e, this);
			throw new RuntimeException("Database failure, please try again");
		}
		try{
			statement.executeUpdate(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = statement.getGeneratedKeys();
			if(rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Invalid Insert Statement");
		}
		return -1;
	}
	
	/**
	 * Executes the given sql statement as an insertion/deletion/update on the database
	 * @param sqlStatement	The statement to update the database
	 * @return success Returns if the statement was successful or not.
	 * @throws RuntimeException If the sqlStatment was bad
	 */
	public boolean executeUpdate(String sqlStatement){
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch(SQLException e){
			ErrorManager.getReference().fatalSubystemError(e, this);
			throw new RuntimeException("Database failure, please try again");	
		}
		int count = 0;
		try{
			count = statement.executeUpdate(sqlStatement);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Invalid Update Statement");
		}
		if(count > 0)
			return true;	//return true if query executed successfully.
		return false;
	}
	
	/**
	 * Executes the given command as a delete on the database
	 * @param sqlStatement The delete statement to execute
	 * @return success Returns if the statement was successful or not
	 * @throws RuntimeException If the sqlStatement was bad
	 */
	public boolean executeDelete(String sqlStatement)throws RuntimeException{
		Statement statement = null;
		try {
			statement = connection.createStatement();
		}catch(SQLException e){
			ErrorManager.getReference().fatalSubystemError(e, this);
			throw new RuntimeException("Database failure, please try again");
		}
		int count = 0;
		try{
			count = statement.executeUpdate(sqlStatement);
		} catch (SQLException e) {
			throw new IllegalArgumentException("Invalid Delete Statement");
		}
		if(count > 0)
			return true;
		return false;
	}
	
	/**
	 * Establishes the connection to the database using the object's attributes
	 */
	private void createConnection(){
		try{
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url,userName,password);   //create the connection to the database
		
		} catch(SQLException e){
			ErrorManager.getReference().fatalSubystemError(e, this);
		} catch(Exception e){
			ErrorManager.getReference().fatalSystemError(e, this);
		}
	}
	
/*//testing
	public static void main(String args[]){
		DBInterface.configureDBInterface(
				"jdbc:mysql://localhost:3306/librisDB",
				"root",
				"root");
		DBInterface.getReference().executeUpdate("update resourceType set type_name = 'booo' where type_ID = 1");

	}*/

}




