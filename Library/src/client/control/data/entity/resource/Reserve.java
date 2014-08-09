package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a reserve on a Resource
 * 
 * @author Jeremy Lerner
 * @version 6
 *
 */
public class Reserve extends Entity{
	int resource;
	int userID;
	Date reservationDate;
	Date availableDate;
	Date endDate;
	
	/**
	 * Constructs a new Reserve with a resource ID of 0 and null reservation date
	 */
	public Reserve(){
		super();
		userID = 0;
		resource = 0;
		reservationDate = null;
		availableDate = null;
		endDate = null;
	}
	/**
	 * Copy constructor
	 * 
	 * @param Reserve object to copy
	 */
	public Reserve(Reserve original){
		super(original);
		userID = original.getUserID();
		resource = original.getResource();
		reservationDate = original.getReservationDate();
		availableDate = original.getAvailableDate();
		endDate = original.getEndDate();
	}
	/**
	 * Constructs a new Reserve with specified fields
	 * 
	 * @param value for ID
	 * @param value for User ID
	 * @param value for Resource
	 * @param value for Reservation Date
	 * @param value for Available Date
	 * @param value for End Date
	 */
	public Reserve(int newID, int newUserID, int newResource, Date newReservationDate, Date newAvailableDate, Date newEndDate){
		super(newID);
		userID = newUserID;
		resource = newResource;
		reservationDate = newReservationDate;
		availableDate = newAvailableDate;
		endDate = newEndDate;
	}
	
	/**
	 * Returns the ID number of the resource held on Reserve
	 * 
	 * @return The ID number of the resource held on reserve
	 */
	public int getResource(){
		return resource;
	}
	/**
	 * Returns the ID of the User in whose name the Reserve is
	 * 
	 * @return The ID of the User in whose name the Reserve is
	 */
	public int getUserID(){
		return userID;
	}
	/**
	 * Returns the Reserve's reservation date
	 * 
	 * @return The Reserve's reservation date
	 */
	public Date getReservationDate(){
		return reservationDate;
	}
	/**
	 * Returns the Reserve's available date
	 * 
	 * @return The Reserve's available date
	 */
	public Date getAvailableDate(){
		return availableDate;
	}
	/**
	 * Returns the Reserve's end date
	 * 
	 * @return The Reserve's end date
	 */
	public Date getEndDate(){
		return endDate;
	}
	
	public boolean checkValid(){
		return (availableDate.after(reservationDate));
	}
}