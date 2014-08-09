package client.control.configurationmanagement;


/**
 * Configuration Generates the configuration object
 * 
 * @author Matthew Hinton
 * @version 1
 */
public class Configuration
{
	private String IP = null;
	private int portNumber = 0;
	
	public Configuration(String IPAddress, int PortNumber)
	{
		IP = IPAddress;
		portNumber = PortNumber;
	}
	
	/**
	 * Returns the Configuration IP address
	 * 
	 * @param void
	 * @return int: IP address
	 */
	public String getIPAddress()
	{
		return IP;
	}
	
	/**
	 * Returns the Configuration Port number
	 * 
	 * @param void
	 * @return int: Port Number
	 */
	public int getPortNumber()
	{
		return portNumber;
	}
}