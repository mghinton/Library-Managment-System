package server.control.datamanagement;

/**
 * This is a static class that determines if a User is allowed to perform the given SQL statement on the database using the permissions matrix
 * @author Peter Abelseth
 * @version 2
 */
public final class PermissionManager implements UserPermissions {

	public static boolean checkAllowed(String sqlStatement, int userID, int userType){
		boolean isStub = true;
		if(isStub){
			return true;
		}
			
		sqlStatement = sqlStatement.toLowerCase().trim();
		
		if(sqlStatement.startsWith("select"))
			return isAllowedSelect(sqlStatement,userID,userType);
		
		if(sqlStatement.startsWith("insert"))
			return isAllowedInsert(sqlStatement,userID,userType);
		
		if(sqlStatement.startsWith("update"))
			return isAllowedUpdate(sqlStatement,userID,userType);
		
		if(sqlStatement.startsWith(("delete")))
			return isAllowedDelete(sqlStatement,userID,userType);
		
		return false;
	}

	
	private static boolean isAllowedSelect(String sqlStatement, int userID, int userType){
		
		return true;
	}
	

	private static boolean isAllowedInsert(String sqlStatement,int userID, int userType){
		
		return true;
	}
	
	private static boolean isAllowedUpdate(String sqlStatement, int userID, int userType){
		
		return true;
	}
	
	private static boolean isAllowedDelete(String sqlStatement, int userID, int userType){
			
		return true;
	}
	
	
	
}
