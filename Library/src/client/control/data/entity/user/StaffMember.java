package client.control.data.entity.user;

/**
 * This class represents a User of the StaffMember type, and is intended to be extended by the Faculty and Student classes.
 * 
 * @author Jeremy Lerner
 * @version 2
 */
public abstract class StaffMember extends User{
	/**
	 * Constructs a new StaffMember with no reserves, loans, and all other fields set to null
	 */
	public StaffMember(){
		super();
	}
	/**
	 * Copy constructor
	 * 
	 * @param StaffMember to copy from
	 */
	public StaffMember(StaffMember original){
		super((User)original);
	}
	
	/**
	 * Constructs a new StaffMember object and sets all the fields to specified values
	 * 
	 * @param value for ID
	 * @param value for First Name
	 * @param value for Last Name
	 * @param value for Password
	 * @param value for E-mail address
	 * @param True for enabled; false otherwise
	 */
	public StaffMember(int newID, String newFirstName, String newLastName, String newPassword, String newEmail, boolean newEnabled){
		super(newID, newFirstName, newLastName, newPassword, newEmail, newEnabled);
	}
}