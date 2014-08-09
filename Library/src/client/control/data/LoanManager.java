package client.control.data;

import java.sql.SQLException;
import java.util.Date;
import com.sun.rowset.CachedRowSetImpl;
import client.serverinterface.ServerInterface;
import client.control.data.entity.resource.Loan;
import client.control.data.entity.resource.Reference;
import client.control.data.entity.resource.Reserve;
import client.control.data.entity.resource.Resource;
import client.control.data.entity.resource.ResourceCopy;
import client.control.data.entity.resource.ResourceType;
import client.control.data.entity.user.Faculty;
import client.control.data.entity.user.StaffMember;
import client.control.data.entity.user.Student;
import client.control.data.entity.user.User;
import client.control.session.*;

/**
 * Control Object for Loans
 * 
 * @author Jeremy Lerner, Sardor Isakov
 * @version 6
 *
 */
public class LoanManager{
	
	/**
	 * Returns an array of Loan objects with fields matching the initialized fields of a parameter
	 * 
	 * @param A Loan object with some fields initialized
	 * @return An array of Loan objects from the Server with fields matching those initialized in the parameter
	 * @throws RequestException 
	 */
	public static Loan[] getLoan(Loan searchKey) {
		String sqlSelect = buildSelectStatement(searchKey);
		CachedRowSetImpl rowSet = null;
		Loan[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}

	/**
	 * Returns a Loan object with a given ID number
	 * 
	 * @param A Loan ID number
	 * @return A Loan object with the given ID number
	 */
	public static Loan getLoan(int idNumber){
		String sqlSelect = "SELECT loan_ID, check_out_date, check_in_date, due_date, fine, fine_paid, user_user_ID, resourceCopy_resourceCopy_ID " +
				"FROM loan WHERE loan_ID = " + idNumber;
		CachedRowSetImpl rowSet = null;
		Loan result[] = null;
		
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
	 * Edits a Loan entry in the database
	 * 
	 * @param A Loan object with updated fields. The ID number field must be set in the Loan.
	 * @return True if successful, false otherwise
	 */
	public static boolean setLoan(Loan newEntry){
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
	 * Inserts a new Loan object in the database
	 * 
	 * @param The Loan object to add to the database. As the Server generates the ID number, the ID number field in this parameter is ignored.
	 * @return The new entry's ID number
	 */
	public static Loan addLoan(Loan newEntry){
		/*try{
			Loan loanKey = new Loan(-1,-1,newEntry.getUserID(),null,null,null,-1,false);
			Loan[] loans = getLoan(loanKey);
			for(int i=0;i<loans.length;i++)
				if (loans[i].getFineAmount() > 0)
					throw new ControlException("User has outstanding fines");
			
			loanKey = new Loan(-1,newEntry.getResourceCopy(), -1, null, null, null, -1, false);
			if (getLoan(loanKey).length > 0)
				throw new ControlException("Resource Copy is on loan already");
			
			Reference referenceKey = new Reference(-1,newEntry.getResourceCopy(), null, null, -1);
			if (ReferenceManager.getReference(referenceKey).length > 0)
				throw new ControlException("Resource Copy is on reference");
			
			ResourceCopy resourceCopy = ResourceCopyManager.getResourceCopy(newEntry.getResourceCopy());
			Reserve reserveKey = new Reserve(-1,-1, resourceCopy.getResource(), null, null, null);
			
			ResourceCopy resourceCopyKey = new ResourceCopy(-1,resourceCopy.getResource(), -1, -1, true);
			Reserve[] reserves = ReserveManager.getReserve(reserveKey);
			int numberOfCopies = ResourceCopyManager.getResourceCopy(resourceCopyKey).length;
			if (numberOfCopies == 0)
				throw new ControlException("There are no copies of this resource");//This probably won't ever happen
			
			if (numberOfCopies <= reserves.length){
				boolean hasReserve = false;
				boolean hasAvailableReserve = false;
				for(int i=0;i<reserves.length;i++)
					if(reserves[i].getUserID() == newEntry.getUserID()){
						hasReserve = true;
						if (reserves[i].getAvailableDate() != null)
							hasAvailableReserve = true;
						break;
					}
				if (!hasReserve)
					throw new ControlException("There are "+reserves.length+" reserves for this resource. The loan could not be completed.");
				if (!hasAvailableReserve)
					throw new ControlException("The user's reserve is not yet available. Try again later.");				
			}
		}catch(ControlException e){
			ErrorManager.handleError(e);
			return null;
		}
		
		ResourceCopy resourceCopy = ResourceCopyManager.getResourceCopy(newEntry.getResourceCopy());
		Reserve reserveKey = new Reserve(-1,newEntry.getUserID(), resourceCopy.getResource(), null, null, null);

		Reserve[] reserves = ReserveManager.getReserve(reserveKey);
		if (reserves.length != 0)
			ReserveManager.removeReserve(reserves[0].getID());
		*/
		
		ResourceCopy resourceCopy = ResourceCopyManager.getResourceCopy(newEntry.getResourceCopy());
		if(resourceCopy == null){
			ErrorManager.handleError(new ControlException("The given resource copy does not exist"));
			return null;
		}
		
		Reserve[] reserve = ReserveManager.getReserve(new Reserve(-1,newEntry.getUserID(),resourceCopy.getResource(),null,null,null));
		if(reserve != null && reserve.length > 0){
			return redeemReserve(newEntry,reserve);
		}
		
		try{
			String sqlInsert = buildInsertStatement(newEntry);
			String[] sqlCheck = buildInsertCheckStatements(newEntry);
			int id = 0;
			Loan newEntity = null;
			
			try{
				id = ServerInterface.getReference().requestInsert(sqlInsert,sqlCheck);
				newEntity = getLoan(id);
			} catch (RequestException e){
				ErrorManager.handleError(e);
			}
			return newEntity;
		} catch(ControlException e){
			ErrorManager.handleError(e);
			return null;
		}
	}
	


	/**
	 * Checks in a loan and attempts to loan it again to the same person
	 * 
	 * @param The Loan to renew
	 * @return The renewed Loan, or null if the Loan cannot be renewed
	 */
	public static Loan renewLoan(Loan loan) {
		boolean success = false;;
		Loan updatedLoan = null;
		try {
			String sqlUpdate = "UPDATE loan " +
					"SET " +
					"loan.due_date = (UNIX_TIMESTAMP(NOW()) + " + getLoanDays(loan.getResourceCopy(), loan.getUserID()) + "*24*60*60)*1000 " +
					"WHERE loan.loan_ID = " + loan.getID();
			String[] sqlCheck = buildInsertCheckStatements(loan);
			try{
				success = ServerInterface.getReference().requestUpdate(sqlUpdate,sqlCheck);
				updatedLoan = getLoan(loan.getID());
			} catch(RequestException e){
				ErrorManager.handleError(e);
				return loan;
			}
		} catch (ControlException e) {
			ErrorManager.handleError(e);
			return loan;
		}
		if(success)
			return updatedLoan;
		return loan;
		
		/*removeLoan(loan.getID());
		
		loan = new Loan(-1,loan.getResourceCopy(), loan.getUserID(), null, null, null, -1, false);
		
		return addLoan(loan);*/
	}
	
	/**
	 * Gets all outstanding loans from database
	 * @return Loan[]	An array of Loan objects from the Server with fields matching those initialized in the parameter
	 */
	public static Loan[] getOutstanding() {
		String sqlSelect = "SELECT loan_ID, check_out_date, check_in_date, due_date, last_email_date, fine, fine_paid, user_user_ID, resourceCopy_resourceCopy_ID FROM loan WHERE fine > 0 AND fine_paid = FALSE";
		CachedRowSetImpl rowSet = null;
		Loan[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		
		return result;
	}
	
	/**
	 * Gets all active loans (checked out loans) from database
	 * @return Loan[]	An array of Loan objects from the Server with fields matching those initialized in the parameter
	 */
	public static Loan[] getActiveLoans() {
		String sqlSelect = "SELECT loan_ID, check_out_date, check_in_date, due_date, last_email_date, fine, fine_paid, user_user_ID, resourceCopy_resourceCopy_ID FROM loan WHERE check_in_date IS NULL";
		CachedRowSetImpl rowSet = null;
		Loan[] result = null;
		try{
			rowSet = ServerInterface.getReference().requestSelect(sqlSelect);
			result = buildEntityFromRowSet(rowSet);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		return result;
	}
	
	/**
	 * Removes a Loan object from the database
	 * 
	 * @param The ID number of the Loan to remove from the database
	 * @return True if successful, false otherwise
	 */
	public static boolean removeLoan(int idNumber){
		/*
		Loan loan = getLoan(idNumber);
		ResourceCopy resourceCopy = ResourceCopyManager.getResourceCopy(loan.getResourceCopy());
		
		Reserve reserveKey = new Reserve(-1,-1, resourceCopy.getResource(), null, null, null);
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
			
		*/
		String sqlDelete = "DELETE FROM loan WHERE loan_ID = " + idNumber;
		String[] sqlCheck = buildDeleteCheckStatements(idNumber);
		
		boolean success = false;
		try{
			success = ServerInterface.getReference().requestDelete(sqlDelete,sqlCheck);
		} catch(RequestException e){
			ErrorManager.handleError(e);
		}
		
		return success;
	}
	
	/**
	 * Redeems the given reservation, adding the given loan.
	 * 
	 * @param newEntry
	 * @param reserve
	 * @return
	 */
	private static Loan redeemReserve(Loan newEntry, Reserve reserve[]) {
		if(reserve == null || reserve.length <= 0 || reserve[0].getAvailableDate() == null){
			ErrorManager.handleError(new ControlException("The reserve is not ready yet."));
			return null;
		}
		int id = 0;
		Loan loan = null;
		try {
			String sqlInsert = buildInsertStatement(newEntry);
			String[] sqlCheck = buildRedeemReserveCheckStatements(newEntry);

			try{
				id = ServerInterface.getReference().requestInsert(sqlInsert,sqlCheck);
				loan = getLoan(id);
				ReserveManager.removeReserve(reserve[0].getID());
			} catch(RequestException e){
				ErrorManager.handleError(e);
			}
		} catch (ControlException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return loan;
	}
	
	/**
	 * Builds the select statement to search for a Loan with similar attributes to the given entity
	 * 
	 * @param entity The entity to base the select statement off of
	 * @return sqlSelect The statement that will return all similar entities in the database to the given entity
	 */
	private static String buildSelectStatement(Loan entity){
		String sqlSelect = "SELECT " +
				"loan_id, check_out_date, check_in_date, due_date, last_email_date, fine, fine_paid, user_user_id, resourceCopy_resourceCopy_ID " +
				"FROM loan WHERE ";

		if(entity.getID() > 0)
			sqlSelect += "loan_id = " + entity.getID() + " AND ";
		if(entity.getCheckOutDate() != null)
			sqlSelect += "check_out_date = " + entity.getCheckOutDate().getTime() + " AND ";
		if(entity.getCheckInDate() != null)
			sqlSelect += "check_in_date = " + entity.getCheckInDate().getTime() + " AND ";
		if(entity.getDueDate() != null)
			sqlSelect += "due_date = " + entity.getDueDate().getTime() + " AND ";
		if(entity.getFineAmount() >= 0)
			sqlSelect += "fine = " + entity.getFineAmount() + " AND ";
		if(entity.getUserID() > 0)
			sqlSelect += "user_user_ID = " + entity.getUserID() + "  AND ";
		if(entity.getResourceCopy() > 0)
			sqlSelect += "resourceCopy_resourceCopy_ID = " + entity.getResourceCopy() + "  AND ";
		
		sqlSelect += "fine_paid = " + entity.getFinePaid() + " AND ";

		if(sqlSelect.endsWith(" AND "))
			sqlSelect = sqlSelect.substring(0, sqlSelect.length()-5) + " ";	//get rid of extra AND statement
		return sqlSelect;
	}
	
	/**
	 * Builds an update statement based on the values of entity that aren't null or 0
	 * @param entity The entity to update with the desired values
	 * @return sqlUpdate The statement that will update the given entity in the database
	 */
	private static String buildUpdateStatement(Loan entity) {
		String sqlUpdate = "UPDATE loan SET ";
		
		if(entity.getCheckOutDate() != null)	
			sqlUpdate += "check_out_date = " + entity.getCheckOutDate().getTime() + ", ";
		if(entity.getCheckInDate() != null)
			sqlUpdate += "check_in_date = " + entity.getCheckInDate().getTime() + ", ";
		if(entity.getDueDate() != null)
			sqlUpdate += "due_date = " + entity.getDueDate().getTime() + ", ";
		if(entity.getFineAmount()  >= 0)
			sqlUpdate += "fine = " + entity.getFineAmount() + ", ";
		
		sqlUpdate += "fine_paid = " + entity.getFinePaid() + ", ";
		
		if(entity.getUserID() > 0)
			sqlUpdate += "user_user_ID = " + entity.getUserID() + ", ";
		if(entity.getResourceCopy() > 0)
			sqlUpdate += "resourceCopy_resourceCopy_ID = " + entity.getResourceCopy() + ", ";

		if(sqlUpdate.endsWith(", "))
			sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length()-2) + " ";	//get rid of extra comma
		
		return sqlUpdate + "WHERE loan_ID = " + entity.getID();
	}

	/**
	 * Builds an insert statement of the given Loan
	 * 
	 * @param entity The entity to build the insert statement based off of
	 * @return sqlInsert The insert statement that can insert the given entity into the database
	 * @throws ControlException If there is an invalid insert
	 */
	private static String buildInsertStatement(Loan entity) throws ControlException{
		String attributes = "(", values = "(";
		
		if(entity.getID() > 0 ){
			attributes += "loan_ID, ";
			values += entity.getID() + ", ";
		}
		if(entity.getCheckOutDate() != null){
			attributes += "check_out_date, ";
			values += entity.getCheckOutDate().getTime() + ", ";
		}
		if(entity.getCheckInDate() != null){
			attributes += "check_in_date, ";
			values += entity.getCheckInDate().getTime() + ", ";
		}
		if(entity.getDueDate() != null){
			attributes += "due_date, ";
			values += entity.getDueDate().getTime() + ", ";
		}
		else{
			attributes += "due_date, ";
			values +=  "(UNIX_TIMESTAMP(NOW()) + " + getLoanDays(entity.getResourceCopy(),entity.getUserID())+ "*24*60*60)*1000 " + ", ";
		}
		
		if(entity.getFineAmount() >= 0){
			attributes += "fine, ";
			values += entity.getFineAmount() + ", ";
		}
		if(entity.getFinePaid()){
			attributes += "fine_paid, ";
			values += entity.getFinePaid() + ", ";
		}
		if(entity.getUserID() > 0){
			attributes += "user_user_ID, ";
			values += entity.getUserID() + ", ";
		}
		if(entity.getResourceCopy() > 0){
			attributes += "resourceCopy_resourceCopy_ID, ";
			values += entity.getResourceCopy() + ", ";
		}
		
		attributes = attributes.substring(0, attributes.length()-2);	//get rid of extra comma and space
		values = values.substring(0, values.length()-2);

		return "INSERT loan " + attributes + ") VALUES " + values + ")";
	}
	
	private static String[] buildInsertCheckStatements(Loan entity){
		String checkIsOnReference = "SELECT resourceCopy_resourceCopy_ID FROM reference WHERE " +
				"resourceCopy_resourceCopy_ID = " + entity.getResourceCopy();
		
		String checkFines = "SELECT loan_ID FROM loan WHERE " +
				"user_user_ID = " + entity.getUserID() + " AND " +
				"fine_paid = FALSE AND " +
				"fine > 0";
		
		String checkAlreadyOnLoan = "SELECT loan_ID FROM loan WHERE " +
				"check_in_date = NULL AND " +
				"resourceCopy_resourceCopy_ID = " + entity.getResourceCopy();
		
		String checkReservation = 
				"SELECT reservation.reservation_ID " +
				"FROM reservation,resourceCopy " +
				"WHERE " +
				"reservation.resource_resource_ID = resourceCopy.resource_resource_ID AND " +
				"resourceCopy.resourceCopy_ID = " + entity.getResourceCopy();
		
		return new String[] {checkIsOnReference,checkFines,checkAlreadyOnLoan,checkReservation};
	}
	
	private static String[] buildRedeemReserveCheckStatements(Loan entity){
		String checkIsOnReference = "SELECT resourceCopy_resourceCopy_ID FROM reference WHERE " +
				"resourceCopy_resourceCopy_ID = " + entity.getResourceCopy();
		
		String checkFines = "SELECT loan_ID FROM loan WHERE " +
				"user_user_ID = " + entity.getUserID() + " AND " +
				"fine_paid = FALSE AND " +
				"fine > 0";
		
		String checkAlreadyOnLoan = "SELECT loan_ID FROM loan WHERE " +
				"check_in_date = NULL AND " +
				"resourceCopy_resourceCopy_ID = " + entity.getResourceCopy();
		
		return new String[] {checkIsOnReference,checkFines,checkAlreadyOnLoan};
	}
	
	/**
	 * Returns the check statements to user when deleting a loan
	 * 
	 * @param idNumber The id of the loan to check
	 * @return An array of strings to execute before deleting the loan
	 */
	private static String[] buildDeleteCheckStatements(int idNumber){
		String checkHasFine = "SELECT loan_ID FROM loan " +
				"WHERE loan_ID = " + idNumber + " AND " +
				"fine > 0";
		return new String[] {checkHasFine};
	}
	
	/**
	 * Returns the number of days the given user can loan the given resourceCopy
	 * 
	 * @param resourceCopyID
	 * @param userID
	 * @return
	 * @throws ControlException
	 */
	private static int getLoanDays(int resourceCopyID,int userID) throws ControlException{
		ResourceCopy resourceCopy = ResourceCopyManager.getResourceCopy(resourceCopyID);
		if(resourceCopy == null)
			throw new ControlException("That resource doesn't exist.");
		Resource resource = ResourceManager.getResource(resourceCopy.getResource());
		if(resource == null)
			throw new ControlException("That resource deosn't exist.");
		ResourceType type = ResourceTypeManager.getResourceType(resource.getResourceType());
		if(type == null)
			throw new ControlException("That resource deosn't exist.");
		User user = UserManager.getUser(userID);
		if(user == null)
			throw new ControlException("That user doesn't exist.");
	
		int daysToLoan = 0;
		if(user instanceof Student)
			daysToLoan = type.getStudentPeriod();
		if(user instanceof Faculty)
			daysToLoan = type.getFacultyPeriod();
		if(user instanceof StaffMember)
			daysToLoan = type.getStaffPeriod();
		
		if(daysToLoan == 0)
			throw new ControlException("This resource is not allowed to be checked out by this user.");
		return daysToLoan;
	}
	
	/**
	 * This builds the Loan(s) from the rowSet given by the database
	 * 
	 * @param rowSet The result from the database (must include all columns of a resouceType)
	 * @return result An array of the Loans acquired from the database. If only expecting one, check length first and then take first element of array.
	 */
	private static Loan[] buildEntityFromRowSet(CachedRowSetImpl rowSet) {
		int nRows = 0;
		try{
			while(rowSet.next())
				nRows++;
			Loan[] result = new Loan[nRows];
			rowSet.beforeFirst();
			for(int i=0; i < nRows && rowSet.next();i++){
				result[i] = new Loan(
						rowSet.getInt("loan_ID"),
						rowSet.getInt("resourceCopy_resourceCopy_ID"),
						rowSet.getInt("user_user_ID"),
						new Date(rowSet.getLong("check_out_date")),
						new Date(rowSet.getLong("due_date")),
						rowSet.getLong("check_in_date") != 0 ? new Date(rowSet.getLong("check_in_date")) : null,	//if check in date is NULL in database, set it to null in entity, if not create a new data object based on the long
						rowSet.getFloat("fine"),
						rowSet.getBoolean("fine_paid"));
			}
			return result;
		}
		catch(SQLException e){
			ErrorManager.handleError(new InstantiationException("System Error: Error building the Loan entity."));
			return null;
		}
	}
	
	/*
	public static void main(String args[]){
		Loan t1 = new Loan(1000,1000,100,new Date(),new Date(),new Date(), (float) .5,false);
		System.out.println(buildSelectStatement(t1));
		
	}*/
		
	/*
	public static void main(String args[]){
		ServerInterface.configureServerInterface("localhost",1500);
		Date sqlDate = new Date(new Date().getTime());
		
		
		
		//=============TESTS========================
		//=============TEST addLoan()===============
		System.out.println("\naddLoan() test:");
		Loan t1 = new Loan(103,sqlDate,null,sqlDate,0.00f, false, 100000005,111);
		Loan r1 = addLoan(t1);
		System.out.println("Retruned loan_id: " + r1.getID());
		
		
		//=============TEST getLoan() in array======
		System.out.println("\n\ngetLoan() test:");
		Loan t2 = new Loan(0,100000005,0,null,null,null, 0, false);
		Loan[] r2 = getLoan(t2);
		for(int i=0; i < r2.length;i++) 
			System.out.println("Loan ID: " + r2[i].getID() + "     User ID: "+ r2[i].getUserID() + "     Check-out date: " + r2[i].getCheckOutDate() + "     Check-in date: " + r2[i].getCheckInDate() );
		
		
		
		//=============TEST setLoan()==============
		System.out.println("\n\nsetLoan() test:");
		Loan t3 = new Loan(103,0,0,null,sqlDate,null,1.25f, false);
		System.out.println(setLoan(t3));
		
		//=============TEST removeLoan()==============
		//System.out.println(removeLoan(156));
		
		
		System.out.println("\n\ngetOutstanding() test:");
		Loan[] r4 = getOutstanding();
		for(int i=0; i < r4.length;i++) 
			System.out.println("Loan ID: " + r4[i].getID() + "     User ID: "+ r4[i].getUserID() + "     Fine amount: $" + r4[i].getFineAmount() + "     Check-in date: " + r4[i].getCheckInDate() );
		
		System.out.println("\n\ngetActiveLoans() test:");
		Loan[] r5 = getActiveLoans();
		for(int i=0; i < r5.length;i++) 
			System.out.println("Loan ID: " + r5[i].getID() + "     User ID: "+ r5[i].getUserID() + "     Check-out date: " + r5[i].getCheckOutDate() + "     Check-in date: " + r5[i].getCheckInDate() );
		
		
		//ServerInterface.closeConnection();
	}
    */
}