package client.control.data.entity.user;

/**
 * This class represents a User of the Patron type, and is intended to be extended by the Faculty and Student classes.
 * 
 * @author Jeremy Lerner
 * @version 2
 */
public abstract class Patron extends User{
	/**
	 * Constructs a new Patron with no reserves, loans, and all other fields set to null
	 */
	public Patron(){
		super();
	}
	/**
	 * Copy constructor
	 * 
	 * @param Patron to copy from
	 */
	public Patron(Patron original){
		super((User)original);
	}
	
	/**
	 * Constructs a new Patron object and sets all the fields to specified values
	 * 
	 * @param value for ID
	 * @param value for First Name
	 * @param value for Last Name
	 * @param value for Password
	 * @param value for E-mail address
	 * @param True for enabled; false otherwise
	 */
	public Patron(int newID, String newFirstName, String newLastName, String newPassword, String newEmail, boolean newEnabled){
		super(newID, newFirstName, newLastName, newPassword, newEmail, newEnabled);
	}
}