package client.control.data.entity.resource;
import java.util.Date;

import client.control.data.entity.Entity;

/**
 * Represents a resource held by the library
 * 
 * @author Jeremy Lerner
 * @version 4
 */
public class Resource extends Entity{
	int type;
	String title;
	String creator;
	String company;
	String serialNumber;
	Date publicationDate;
	boolean enabled;
	
	/**
	 * Constructs a new disabled Resource with a type and resource ID of 0, and all other fields set to null
	 */
	public Resource(){
		super();
		type = 0;
		title = null;
		creator = null;
		company = null;
		serialNumber = null;
		publicationDate = null;
		enabled = false;
	}
	/**
	 * Copy constructor
	 * 
	 * @param Resource to copy from
	 */
	public Resource(Resource original){
		super(original);
		type = original.getResourceType();
		title = original.getTitle();
		creator = original.getCreator();
		company = original.getCompany();
		serialNumber = original.getSerialNumber();
		publicationDate = original.getPublicationDate();
		enabled = original.getEnabled();
	}
	
	/**
	 * Constructs a new Resource with all fields specified
	 * 
	 * @param value for ID
	 * @param value for Type
	 * @param value for Creator
	 * @param value for Company
	 * @param value for Serial Number
	 * @param value for Publication Date
	 * @param value for Enabled
	 */
	public Resource(int newID, int newType, String newTitle, String newCreator, String newCompany, String newSerialNumber, Date newPublicationDate, boolean newEnabled){
		super(newID);
		type = newType;
		title = newTitle;
		creator = newCreator;
		company = newCompany;
		serialNumber = newSerialNumber;
		publicationDate = newPublicationDate;
		enabled = newEnabled;
	}

	/**
	 * Returns the Resource Type ID
	 * 
	 * @return The Resource Type ID
	 */
	public int getResourceType(){
		return type;
	}
	/**
	 * Returns the title
	 * 
	 * @return The title
	 */
	public String getTitle(){
		return title;
	}
	/**
	 * Returns the creator
	 * 
	 * @return The creator
	 */
	public String getCreator(){
		return creator;
	}
	/**
	 * Returns the company
	 * 
	 * @return The company
	 */
	public String getCompany(){
		return company;
	}
	/**
	 * Returns the serial number
	 * 
	 * @return The serial number
	 */
	public String getSerialNumber(){
		return serialNumber;
	}
	/**
	 * Returns the publication date
	 * 
	 * @return The publication date
	 */
	public Date getPublicationDate(){
		return publicationDate;
	}
	/**
	 * Returns true if the Resource is enabled, false otherwise
	 * 
	 * @return True if the resource is enabled, false otherwise
	 */
	public boolean getEnabled(){
		return enabled;
	}
	
	public boolean checkValid(){
		return true;
	}
}