package client.control.data.entity.user;


/**
 * This class represents a User of the Student type.
 * 
 * @author Jeremy Lerner
 * @version 3
 */
public class Student extends Patron{
	
	/**
	 * Constructs a new Student with no references, loans, reserves, and all other fields set to null
	 */
	public Student(){
		super();
	}
	/**
	 * Copy constructor
	 * 
	 * @param Student to copy from
	 */
	public Student(Student original){
		super((Patron)original);
	}
	/**
	 * Constructs a new Student with all fields specified
	 * 
	 * @param value for ID
	 * @param value for First Name
	 * @param value for Last Name
	 * @param value for Password
	 * @param value for Email
	 * @param value for Enabled
	 */
	public Student(int newID, String newFirstName, String newLastName, String newPassword, String newEmail, boolean newEnabled){
		super(newID, newFirstName,newLastName,newPassword,newEmail,newEnabled);
	}
}