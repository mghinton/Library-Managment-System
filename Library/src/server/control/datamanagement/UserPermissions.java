package server.control.datamanagement;

import global.LibrisGlobalInterface;

/**
 * @author Peter Abelseth
 * @version 1
 * 
 * Notes: This is hardcoded, should be coded so that the index of the array matches number of the user type automatically
 */
public interface UserPermissions extends LibrisGlobalInterface {

	public static int USER_BASIC[] = {
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b00000011",2),
		Integer.parseInt("0b00000011",2),
		Integer.parseInt("0b11110011",2),
		Integer.parseInt("0b11110011",2),
		Integer.parseInt("0b11111111",2)						//admin
	};
	
	public static final int USER_EXTENDED[] = {
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b00000001",2),
		Integer.parseInt("0b00000001",2),
		Integer.parseInt("0b11010001",2),
		Integer.parseInt("0b11010001",2),
		Integer.parseInt("0b11111111",2)
	};
	
	public static final int RESOURCE[] = {
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b11110000",2),
		Integer.parseInt("0b11110000",2),
		Integer.parseInt("0b11111111",2)
	};

	public static final int RESOURCECOPY[] = {
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b11110000",2),
		Integer.parseInt("0b11110000",2),
		Integer.parseInt("0b11111111",2)
	};

	public static final int SUBSCRIPTION[] = {
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b11111111",2),
		Integer.parseInt("0b11111111",2),
		Integer.parseInt("0b11111111",2)
	};

	public static final int TODO[] = {
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b11111111",2),
		Integer.parseInt("0b11111111",2),
		Integer.parseInt("0b11111111",2)
	};

	public static final int LOAN[] = {
		Integer.parseInt("0b00000000",2),
		Integer.parseInt("0b00000001",2),
		Integer.parseInt("0b00000001",2),
		Integer.parseInt("0b11111111",2),
		Integer.parseInt("0b11111111",2),
		Integer.parseInt("0b11111111",2)
	};

	public static final int REFERENCE[] = {
		Integer.parseInt("0b00000001",2),
		Integer.parseInt("0b00000001",2),
		Integer.parseInt("0b00000001",2),
		Integer.parseInt("0b11111111",2),
		Integer.parseInt("0b11111111",2),
		Integer.parseInt("0b11111111",2)
	};

	public static final int RESERVATION[] = {
		Integer.parseInt("0b00001101",2),
		Integer.parseInt("0b00001101",2),
		Integer.parseInt("0b11011101",2),
		Integer.parseInt("0b11011101",2),
		Integer.parseInt("0b11011101",2)
	};
	
	public static final int CREATOR[] = {
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b00010000",2),
		Integer.parseInt("0b11110000",2),
		Integer.parseInt("0b11110000",2),
		Integer.parseInt("0b11111111",2)
	};
}
