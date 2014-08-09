package client.control.data.entity.management;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a subscription to a magazine
 * @author Jeremy Lerner
 * @version 5
 *
 */
public class Subscription extends Entity{
	
	String title;
	Date expirationDate;
	long contactPhone;
	String contactEmail;
	
	/**
	 * Constructs a new Subscription object will all fields set to null
	 */
	public Subscription(){
		super();
		title = null;
		expirationDate = null;
		contactPhone = 0;
		contactEmail = null;
	}
	/**
	 * Copy Constructor
	 * 
	 * @param The Subscription to copy
	 */
	public Subscription(Subscription original){
		super(original);
		title = original.getTitle();
		expirationDate = original.getExpirationDate();
		contactPhone = original.getContactPhone();
		contactEmail = original.getContactEmail();
	}
	/**
	 * Constructs a new Subscription with all fields specified
	 * 
	 * @param value for ID
	 * @param value for Title
	 * @param value for expiration date
	 * @param value for contact phone
	 * @param value for contact E-mail address
	 */
	public Subscription(int newID, String newTitle, Date newExpirationDate, long newContactPhone, String newContactEmail){
		super(newID);
		title = newTitle;
		expirationDate = newExpirationDate;
		contactPhone = newContactPhone;
		contactEmail = newContactEmail;
	}
	
	/**
	 * Returns the magazine's title
	 * @return The title
	 */
	public String getTitle(){
		return title;
	}
	/**
	 * Returns the magazine's publisher's contact E-mail address
	 * @return The magazine's publisher's contact E-mail address
	 */
	public String getContactEmail(){
		return contactEmail;
	}
	/**
	 * Returns the Subscription's expiration date
	 * @return The expiration date
	 */
	public Date getExpirationDate(){
		return expirationDate;
	}
	/**
	 * Returns the magazine publisher's contact phone number
	 * 
	 * @return The magazine publisher's contact phone number
	 */
	public long getContactPhone(){
		return contactPhone;
	}
	
	public boolean checkValid(){
		return (Entity.checkEmail(contactEmail) && Entity.checkPhone(contactPhone));
	}
}