package server.dbinterface;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Scanner;
import com.sun.rowset.CachedRowSetImpl;

/**
 * This class tests the DBInterface class
 * @author Peter Abelseth
 * @version 1
 */
public class DBInterfaceTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBInterface dbInterface = DBInterface.configureDBInterface(
				"jdbc:mysql://localhost:3306/librisDB",
				"root",
				"root");
		Scanner  input =new Scanner(System.in);
		int quit = 0;
		
		String sqlCommand;
		
		String statement = "insert user (first_name,last_name,password,email,type) VALUES ('" + (new BigInteger(130, (new SecureRandom()))).toString() + "','Abelseth',MD5('test'),'test',5)";
		
		while(true){
			dbInterface.executeInsert(statement);
			
			System.out.println(Calendar.getInstance().getTime());
		}
		
		
	}
	
	
	private static void printRowSet(CachedRowSetImpl rowSet) throws SQLException{
		ResultSetMetaData rsmd = rowSet.getMetaData();
		int columns = rsmd.getColumnCount();
		while(rowSet.next()){
			for(int i=1;i<=columns;i++){
				System.out.print(rsmd.getColumnName(i) + ": ");
				System.out.print(rowSet.getString(i) + "\t");
			}
			System.out.println();
		}
		
	}

}
