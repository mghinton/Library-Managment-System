package client.control.data.entity.user;

/**
 * This class represents a User of the Administrator type.
 * 
 * @author Jeremy Lerner
 * @version 3
 */
public class Administrator extends StaffMember{
	/**
	 * Constructs a new Administrator with no references, loans, reserves, and all other fields set to null
	 */
	public Administrator(){
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param Administrator to copy from
	 */
	public Administrator(Administrator original){
		super((StaffMember)original);
	}
	
	/**
	 * Constructs a new Administrator with all fields specified
	 * 
	 * @param value for ID
	 * @param value for First Name
	 * @param value for Last Name
	 * @param value for Password
	 * @param value for Email
	 * @param value for Enabled
	 */
	public Administrator(int newID, String newFirstName, String newLastName, String newPassword, String newEmail, boolean newEnabled){
		super(newID, newFirstName,newLastName,newPassword,newEmail,newEnabled);
	}
}