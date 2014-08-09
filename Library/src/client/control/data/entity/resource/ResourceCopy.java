package client.control.data.entity.resource;
import client.control.data.entity.Entity;

/**
 * Represents a copy of a resource
 * 
 * @author Jeremy Lerner
 * @version 7
 */
public class ResourceCopy extends Entity{
	int resource;
	int copyID;
	int ownerID;
	boolean enabled;
	
	/**
	 * Constructs a new disabled ResourceCopy with all other fields set to 0
	 */
	public ResourceCopy(){
		super();
		resource = 0;
		copyID = 0;
		ownerID = 0;
		enabled = false;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param The ResourceCopy to copy
	 */
	public ResourceCopy(ResourceCopy original){
		super(original);
		resource = original.getResource();
		copyID = original.getCopyID();
		ownerID = original.getOwnerID();
		enabled = original.getEnabled();
	}
	
	/**
	 * Constructs a new ResourceCopy with all fields specified
	 * 
	 * @param value of ID
	 * @param value of Resource
	 * @param value of Copy ID
	 * @param value of Owner
	 * @param value of Enabled
	 */
	public ResourceCopy(int newID, int newResource, int newCopyID, int newOwner, boolean newEnabled){
		super(newID);
		resource = newResource;
		copyID = newCopyID;
		ownerID = newOwner;
		enabled = newEnabled;
	}
	
	/**
	 * Returns the ID of the resource that the calling ResourceCopy is a copy of
	 * 
	 * @return The Resource that the calling ResourceCopy is a copy of
	 */
	public int getResource(){
		return resource;
	}
	/**
	 * Returns the Copy ID, which is unique among Resource Copies of one Resource
	 * 
	 * @return The Copy ID, which is unique among Resource Copies of one Resource
	 */
	public int getCopyID(){
		return copyID;
	}
	/**
	 * Returns the ResourceCopy's owner's user ID
	 * 
	 * @return The ResourceCopy's owner's user ID
	 */
	public int getOwnerID(){
		return ownerID;
	}
	/**
	 * Returns true if the ResourceCopy is enabled, false otherwise
	 * 
	 * @return True if the ResourceCopy is enabled, false otherwise
	 */
	public boolean getEnabled(){
		return enabled;
	}
	
	public boolean checkValid(){
		return true;
	}
}