package client.control.data.entity.user;

/**
 * This class represents a User of the Librarian type.
 * 
 * @author Jeremy Lerner
 * @version 2
 */
public class Librarian extends StaffMember{
	/**
	 * Constructs a new Librarian with no references, loans, reserves, and all other fields set to null
	 */
	public Librarian(){
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param Librarian to copy from
	 */
	public Librarian(Librarian original){
		super((StaffMember)original);
	}
	
	/**
	 * Constructs a new Librarian with all fields specified
	 * 
	 * @param value for ID
	 * @param value for First Name
	 * @param value for Last Name
	 * @param value for Password
	 * @param value for Email
	 * @param value for Enabled
	 */
	public Librarian(int newID, String newFirstName, String newLastName, String newPassword, String newEmail, boolean newEnabled){
		super(newID, newFirstName,newLastName,newPassword,newEmail,newEnabled);
	}
}