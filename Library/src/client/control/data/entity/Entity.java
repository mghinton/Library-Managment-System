package client.control.data.entity;

/**
 * Represents an entity object. This class contains one field, "idNumber", and a getter method.
 * 
 * @author Jeremy
 * @version 2
 */
public abstract class Entity{
	int idNumber;
	
	/**
	 * Constructs a new Entity with an ID number of 0
	 */
	public Entity(){
		idNumber = 0;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param The Entity to copy from
	 */
	public Entity(Entity original){
		idNumber = original.getID();
	}
	
	public Entity(int newID){
		idNumber = newID;
	}
	
	/**
	 * Returns the Entity's ID number
	 * 
	 * @return The Entity's ID number
	 */
	public int getID(){
		return idNumber;
	}
	
	/**
	 * Returns true if all fields are correctly formatted; false otherwise
	 * 
	 * @return True if all fields are correctly formatted; false otherwise
	 */
	public abstract boolean checkValid();
	
	/**
	 * Checks whether an E-mail address string is correctly formatted
	 * 
	 * @param A String Representing the E-mail address
	 * @return True if the String is a correctly formatted E-mail address; false otherwise
	 */
	public static boolean checkEmail(String email){
		if (email.startsWith("@") || email.endsWith("@"))
			return false;
		boolean found = false;
		for(int i=0;i<email.length();i++)
			if (email.charAt(i) == '@'){
				if (found == true)
					return false;
				found = true;
			}
		return found;
	}
	
	/**
	 * Checks whether a phone number is correctly formatted
	 * 
	 * @param Long representing the phone number
	 * @return True if the phone number is correctly formatted; false otherwise
	 */
	public static boolean checkPhone(long phone){
		return !(phone < 0 || phone > 9999999999L);
	}
}