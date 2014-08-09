package client.control.data.entity.user;

import client.control.data.entity.Entity;

/**
 * This class represents a user, and is intended to be extended by the StaffMember and Patron classes.
 * 
 * @author Jeremy Lerner
 * @version 6
 *
 */
public class User extends Entity{
	
	String firstName;
	String lastName;
	String password;
	String emailAddress;
	boolean enabled;
	
	/**
	 * Constructs a new User with all fields set to null
	 */
	public User(){
		super();
		firstName = null;
		lastName = null;
		password = null;
		emailAddress = null;
		enabled = false;
	}
	
	/**
	 * Copy Constructor
	 * 
	 * @param The User object to copy
	 */
	public User(User original){
		super(original);
		firstName = original.getFirstName();
		lastName = original.getLastName();
		password = original.getPassword();
		emailAddress = original.getEmailAddress();
		enabled = original.getEnabled();
	}
	
	/**
	 * Constructs a new user object and sets all the fields to specified values
	 * 
	 * @param value for new ID
	 * @param value for First Name
	 * @param value for Last Name
	 * @param value for Password
	 * @param value for E-mail address
	 * @param True for enabled; false otherwise
	 */
	public User(int newID, String newFirstName, String newLastName, String newPassword, String newEmail, boolean newEnabled){
		super(newID);
		firstName = newFirstName;
		lastName = newLastName;
		password = newPassword;
		emailAddress = newEmail;
		enabled = newEnabled;
	}
	
	/**
	 * Returns the user's first name
	 * 
	 * @return The first name
	 */
	public String getFirstName(){
		return firstName;
	}
	/**
	 * Returns the user's last name
	 * 
	 * @return The last name
	 */
	public String getLastName(){
		return lastName;
	}

	/**
	 * Returns the user's E-mail address
	 * 
	 * @return The E-mail address
	 */
	public String getEmailAddress(){
		return emailAddress;
	}
	/**
	 * Returns the user's password
	 * 
	 * @return The password
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * Returns true if the User is enabled, false otherwise
	 * 
	 * @return True if the User is enabled, false otherwise
	 */
	public boolean getEnabled(){
		return enabled;
	}

	public boolean checkValid(){
		return Entity.checkEmail(getEmailAddress());
	}
}