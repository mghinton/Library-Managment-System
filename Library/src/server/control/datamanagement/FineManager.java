package server.control.datamanagement;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import com.sun.rowset.CachedRowSetImpl;

import server.control.errormanagement.LogManager;
import server.dbinterface.DBInterface;

/**
 * This is the Fine Manager that will increment the fines of overdue resources.
 * It will check for fines that should be incremented and set the fine to its appropriate amount 
 * It will calculate the fine amount that the fine should be and if it is currently less than this it will set it to the new calculated amount.
 * If the fine incurred will be greater than the maximum allowed it will set it to the maximum allowed fine.
 * 
 * @author Peter Abelseth
 * @version 2
 */
public class FineManager extends ScheduledTask {
		
	private final static long INCREMENT_FINE_24HR = 24*60*60*1000L;	//The default time between when a fine is incremented
	private final static long HR24 = 24*60*60*1000L;	//Run the fine manager once every 24 hours
	
	private ArrayList<ResourceType> resourceTypes = new ArrayList<ResourceType>();	//store the list of the resourceType fine amounts
	
	
	/**
	 * Constructs a Fine Manager and starts the process
	 * @param intervalToRun The interval between the fine manager checking for loans that have fines to be incremented in milliseconds
	 * @param timeToStart The time until the first time the FineManager will be run, in milliseconds
	 * @param fineIncrementPeriod The time between when another fine should be applied to the loan
	 */
	public FineManager(long timeOfDayToRun) {
		super(HR24);
		
		if(timeOfDayToRun > HR24)
			timeOfDayToRun = HR24;
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);
		cal.add(Calendar.MILLISECOND, (int)timeOfDayToRun);		//can type cast because garunteed TODTR is less than 24*60*60*1000 which is less than 2^32
		if(cal.compareTo(Calendar.getInstance()) < 0)
			cal.add(Calendar.DAY_OF_YEAR, 1);
		start(cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
		performTask();	//run right away, one time
	}
	
	/**
	 * The scheduled task that will automatically be called by the TaskScheduler
	 */
	protected void performTask(){
		//System.out.println(Calendar.getReference().getTime());
		try {
			updateFines();
			LogManager.getReference().logMessage(Calendar.getInstance().getTime() + " FineManager Running. Last Ran: " + new Date(getLastRan()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets all overdue loans and increments their fines accordingly
	 * @throws SQLException If couldn't connect to database or invalid query was sent
	 */
	private void updateFines() throws SQLException{
		ArrayList<Loan> overdueLoans = getOverdueLoans();
		
		for (Loan loan: overdueLoans){	//for each overdue loan
			ResourceType rType = getResourceType(loan.typeID);
			int numFinePeriods;
			int numFinesApplied;
			try{
				numFinePeriods = (int)((Calendar.getInstance().getTimeInMillis() - loan.dueDate) / INCREMENT_FINE_24HR + 1);
			} catch(ArithmeticException e){
				numFinePeriods = Integer.MAX_VALUE;
			}
			try{
				numFinesApplied = loan.fine.divide(rType.fineAmount).intValue();
			} catch(ArithmeticException e){
				numFinesApplied = Integer.MAX_VALUE;
			}
			if(numFinePeriods > numFinesApplied){	//if fine should be added
				int numFinesToApply = numFinePeriods - numFinesApplied;
				updateFine(loan,rType,numFinesToApply);
			}
		}
	}
	
	/**
	 * Updates the given loan by adding the fineAmount up to a maximum fine
	 * @param loan The loan to apply the fine to
	 * @param rType The resourceType for the fine
	 * @param numFinesToApply The number of fine periods that need to be account for
	 * @throws SQLException If the update to the database was unsuccessful
	 */
	private void updateFine(Loan loan, ResourceType rType,int numFinesToApply) throws SQLException{
		BigDecimal fineToAdd = rType.fineAmount.multiply(new BigDecimal(numFinesToApply));
		if(loan.fine.compareTo(rType.maxFine) < 0){	//if the loan hasn't reached the maximum fine
			if(loan.fine.compareTo(rType.maxFine.subtract(fineToAdd)) > 0)	//if the fine will go past the maximum
				loan.fine = rType.maxFine;	//set the loan's fine to the maximum allowed
			else
				loan.fine = loan.fine.add(fineToAdd);  //add the fine to the loan
			DBInterface.getReference().executeUpdate("UPDATE loan SET fine = " + loan.fine.doubleValue() + " WHERE loan_ID = " + loan.loanID);	//update the loan in the database with the new fine amount
		}
	}
	
	/**
	 * Gets the resourceType with the given typeID
	 * @param typeID The ResourceType to get fine amount for
	 * @return rType The ResourceType with the given typeID
	 * @throws SQLException If couldn't find the ResourceType
	 */
	private ResourceType getResourceType(int typeID) throws SQLException{
		for(ResourceType rType: resourceTypes){	//find the corresponding resource type
			if(rType.typeID == typeID)
				return rType;
		}
		resourceTypes = getResourceTypes();	//if the resource type couldn't be found, query all resourceTypes and search again
		for(ResourceType rType: resourceTypes){
			if(rType.typeID == typeID)
				return rType;
		}
		throw new SQLException("Resource Type could not be found");  //if resourceType still couldn't be found, then we're screwed! Database is messed up
	}
		
	/**
	 * Queries the database for all overdue loans
	 * @return overdueLoans All loans that are overdue and haven't had their fines paid for
	 * @throws SQLException If couldn't get result from the database
	 */
	private ArrayList<Loan> getOverdueLoans() throws SQLException{
		CachedRowSetImpl result = DBInterface.getReference().executeSelect(SQL_GET_OVERDUE_LOANS); //execute query to get overdue loans
		ArrayList<Loan> overdueLoans = new ArrayList<Loan>();
		while(result.next()){	//insert each overdue loan into the array list
				overdueLoans.add(new Loan(
					result.getInt(1),
					result.getLong(2),
					result.getBigDecimal(3),
					result.getInt(4)));
		}
		return overdueLoans;	//return the arraylist
	}
	
	
	/**
	 * Queries the database for the fine amounts for each type of resource
	 * @return resourceTypes All the types of resources and their fine amounts
	 * @throws SQLException If couldn't get result from the database
	 */
	private ArrayList<ResourceType> getResourceTypes() throws SQLException{		
		CachedRowSetImpl result = DBInterface.getReference().executeSelect(SQL_GET_RESOURCE_TYPES);	//execute query to get all resourceTypes
		
		ArrayList<ResourceType> resourceTypes = new ArrayList<ResourceType>();
		while(result.next()){	//put each resourceType into an arrayList
			resourceTypes.add(new ResourceType(
					result.getInt(1),
					result.getBigDecimal(2),
					result.getBigDecimal(3)));
		}
		return resourceTypes; //return the arraylist
	}
	
	
	/**
	 * Nested class to store the overdue loans and all necessary attributes
	 */
	private class Loan{
		public int loanID;
		public long dueDate;
		public BigDecimal fine;
		public int typeID;
		public Loan(int loanID, long dueDate, BigDecimal fine, int typeID){
			this.loanID = loanID;
			this.dueDate = dueDate;
			this.fine = fine;
			this.typeID = typeID;
		}
	};
	
	/**
	 * Nested class to store all resourceTypes and their fine amounts
	 */
	private class ResourceType{
		public int typeID;
		public BigDecimal fineAmount;
		public BigDecimal maxFine;
		public ResourceType(int typeID, BigDecimal fineAmount, BigDecimal maxFine){
			this.typeID = typeID;
			this.fineAmount = fineAmount;
			this.maxFine = maxFine;
		}
	};
	
	
	//SQL Statement to get all the resource types
	private static final String SQL_GET_RESOURCE_TYPES = "SELECT " +
			"type_ID, fine_amount, fine_max " +
			"FROM resourceType " +
			"WHERE enabled = TRUE " +
			"ORDER BY type_ID";
	
	//SQL Statement to get all overdue loans
	private static final String SQL_GET_OVERDUE_LOANS = "SELECT " +
			"loan.loan_ID, loan.due_date, loan.fine, resource.resourceType_type_ID " +
			"FROM " +
			"loan, user, resourceCopy, resource " +
			"WHERE " +
			"user.enabled = TRUE AND " +
			"user.user_ID = loan.user_user_ID AND " +
			"loan.resourceCopy_resourceCopy_ID = resourceCopy.resourceCopy_ID AND " +
			"resourceCopy.resource_resource_ID = resource.resource_ID AND " +
			"loan.check_in_date IS NULL AND " +
			"loan.fine_paid = FALSE AND " +
			"loan.due_date IS NOT NULL AND " +
			"loan.due_date < (UNIX_TIMESTAMP(NOW())*1000) ";	
	
	
// Used for testing the FineManager
/*
	public static void main(String args[]){
		DBInterface.createDBInterface(
				"jdbc:mysql://localhost:3306/",
				"librisDB",
				"root",
				"root");
		FineManager fineMgr = new FineManager(1000,1000,5000);
	}
*/
}
