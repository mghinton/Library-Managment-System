package client.control.data.entity.resource;
import client.control.data.entity.Entity;

/**
 * Represents a resource type with a name and labels for the title, creator, company, and serial number. For example, a Resource Type May have the title "Book", and the title, creator, company, and serial number labeled as "Title", "Author", "Publisher", and "ISBN Number" respectively.
 * 
 * @author Jeremy Lerner
 * @version 7
 *
 */
public class ResourceType extends Entity{
	String typeName;
	String titleLabel;
	String creatorLabel;
	String companyLabel;
	String serialNumberLabel;
	int staffPeriod;
	int facultyPeriod;
	int studentPeriod;
	float maxFineAmount;
	float fineIncrAmount;
	boolean enabled;
	
	/**
	 * Constructs a new ResourceType with all fields set to null
	 */
	public ResourceType(){
		super();
		typeName = null;
		titleLabel = null;
		creatorLabel = null;
		companyLabel = null;
		serialNumberLabel = null;
		staffPeriod = 0;
		facultyPeriod = 0;
		studentPeriod = 0;
		maxFineAmount = 0;
		fineIncrAmount = 0;
		enabled = false;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param ResourceType to copy
	 */
	public ResourceType(ResourceType original){
		super(original);
		typeName = original.getName();
		titleLabel = original.getTitleLabel();
		creatorLabel = original.getCreatorLabel();
		companyLabel = original.getCompanyLabel();
		serialNumberLabel = original.getSerialNumberLabel();
		staffPeriod = original.getStaffPeriod();
		facultyPeriod = original.getFacultyPeriod();
		studentPeriod = original.getStudentPeriod();
		maxFineAmount = original.getMaxFineAmount();
		fineIncrAmount = original.getFineIncrementAmount();
		enabled = original.getEnabled();
	}
	
	/**
	 * Constructs a new ResourceType with all fields specified
	 * 
	 * @param value for ID
	 * @param value for Type Name
	 * @param value for Title Label
	 * @param value for Creator Label
	 * @param value for Company Label
	 * @param value for Serial Number Label
	 * @param value for Staff Period
	 * @param value for Faculty Period
	 * @param value for Student Period
	 * @param value for Maximum Fine Amount
	 * @param value for Fine Increment Amount
	 * @param value for Enabled
	 */
	public ResourceType(int newID, String newTypeName, String newTitleLabel, String newCreatorLabel, String newCompanyLabel, String newSerialNumberLabel, int newStaffPeriod, int newFacultyPeriod, int newStudentPeriod, float newMaxFineAmount, float newFineIncrAmount, boolean newEnabled){
		super(newID);
		typeName = newTypeName;
		titleLabel = newTitleLabel;
		creatorLabel = newCreatorLabel;
		companyLabel = newCompanyLabel;
		serialNumberLabel = newSerialNumberLabel;
		staffPeriod = newStaffPeriod;
		facultyPeriod = newFacultyPeriod;
		studentPeriod = newStudentPeriod;
		maxFineAmount = newMaxFineAmount;
		fineIncrAmount = newFineIncrAmount;
		enabled = newEnabled;
	}
	
	/**
	 * Returns the name
	 * 
	 * @return The name
	 */
	public String getName(){
		return typeName;
	}
	/**
	 * Returns the title label
	 * 
	 * @return The title label
	 */
	public String getTitleLabel(){
		return titleLabel;
	}
	/**
	 * Returns the creator label
	 * 
	 * @return The creator label
	 */
	public String getCreatorLabel(){
		return creatorLabel;
	}	
	/**
	 * Returns the company label
	 * 
	 * @return The creator label
	 */
	public String getCompanyLabel(){
		return companyLabel;
	}

	/**
	 * Returns the serial number label
	 * 
	 * @return The serial number label
	 */
	public String getSerialNumberLabel(){
		return serialNumberLabel;
	}
	/**
	 * Returns the length of a loan (in days) of a resource of this type to a Staff member
	 * 
	 * @return The length of a loan (in days) of a resource of this type to a Staff member
	 */
	public int getStaffPeriod(){
		return staffPeriod;
	}
	/**
	 * Returns the length of a loan (in days) of a resource of this type to a faculty member
	 * 
	 * @return The length of a loan (in days) of a resource of this type to a faculty member
	 */
	public int getFacultyPeriod(){
		return facultyPeriod;
	}
	/**
	 * Returns the length of a loan (in days) of a resource of this type to a student
	 * 
	 * @return The length of a loan (in days) of a resource of this type to a student
	 */
	public int getStudentPeriod(){
		return studentPeriod;
	}
	/**
	 * Returns the maximum amount that can be fined for a loan of this resource type
	 * 
	 * @return The maximum amount that can be fined for a loan of this resource type
	 */
	public float getMaxFineAmount(){
		return maxFineAmount;
	}
	/**
	 * Returns the amount by which a fine for a late loan of a resource with this resource type is incremented by each day
	 * 
	 * @return The amount by which a fine for a late loan of a resource with this resource type is incremented by each day
	 */
	public float getFineIncrementAmount(){
		return fineIncrAmount;
	}
	/**
	 * Returns true if the Resource Type is enabled, false otherwise
	 * 
	 * @return True if the Resource Type is enabled, false otherwise
	 */
	public boolean getEnabled(){
		return enabled;
	}
	
	public boolean checkValid(){
		return true;
	}
}