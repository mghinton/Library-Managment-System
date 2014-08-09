package client.control.data.entity.user;

/**
 * This class represents a User of the Faculty type.
 * 
 * @author Jeremy Lerner
 * @version 3
 */
public class Faculty extends Patron{
	
	/**
	 * Constructs a new Faculty with no references, loans, reserves, and all other fields set to null
	 */
	public Faculty(){
		super();
	}
	/**
	 * Copy constructor
	 * 
	 * @param Faculty to copy from
	 */
	public Faculty(Faculty original){
		super((Patron)original);
	}
	/**
	 * Constructs a new Faculty with all fields specified
	 * 
	 * @param value for ID
	 * @param value for First Name
	 * @param value for Last Name
	 * @param value for Password
	 * @param value for Email
	 * @param value for Enabled
	 */
	public Faculty(int newID, String newFirstName, String newLastName, String newPassword, String newEmail, boolean newEnabled){
		super(newID, newFirstName,newLastName,newPassword,newEmail,newEnabled);
	}
}