package client.control.data.entity.management;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a TO-DO Note with a description, start date and end date
 * 
 * @author Jeremy Lerner
 * @version 4
 */
public class ToDo extends Entity{
	Date startDate;
	Date endDate;
	String title;
	String description;
	boolean completed;

	/**
	 * Constructs a new non-completed ToDo with all other fields set to null
	 */
	public ToDo() {
		super();
		startDate = null;
		endDate = null;
		title = null;
		description = null;
		completed = false;
	}
	/**
	 * Copy constructor
	 * 
	 * @param ToDo object to copy from
	 */
	public ToDo(ToDo original){
		super(original);
		startDate = original.getStartDate();
		endDate = original.getEndDate();
		title = original.getTitle();
		description = original.getDescription();
		completed = original.getCompleted();
	}
	/**
	 * Constructs a new ToDo object with all fields specified by parameters
	 * 
	 * @param value for ID
	 * @param value for Start Date
	 * @param value for End Date
	 * @param value for Title
	 * @param value for Description
	 * @param value for Completed
	 */
	public ToDo(int newID, Date newStartDate, Date newEndDate, String newTitle, String newDescription, boolean newCompleted){
		super(newID);
		startDate = newStartDate;
		endDate = newEndDate;
		title = newTitle;
		description = newDescription;
		completed = newCompleted;
	}
	
	/**
	 * Returns the ToDo's description
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Returns the ToDo's title
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Returns the ToDo's start date
	 * 
	 * @return The start date
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * Returns the ToDo's end date
	 * 
	 * @return The end date
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * Returns true of the ToDo is completed, false otherwise
	 * 
	 * @return True if the ToDo is completed, false otherwise
	 */
	public boolean getCompleted(){
		return completed;
	}
	
	public boolean checkValid(){
		return endDate.after(startDate);
	}
}