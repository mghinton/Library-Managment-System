package client.control.configurationmanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import client.serverinterface.ServerInterface;

import util.Constants;

/**
 * ConfigurationManager Generates the configuration manager
 * 
 * @author Matthew Hinton
 * @version 1
 */
public class ConfigurationManager
{
	private static ConfigurationManager singleton = null;
	
	private ConfigurationManager(){	}
	
	/**
	 * Returns a reference to this singleton class
	 * @return a reference to this singleton class
	 */
	public static ConfigurationManager getReference() {
		if (singleton == null) {
			singleton = new ConfigurationManager();
		}
		return singleton;
	}
	
	public static Configuration getConfiguration()
	{
		String input;
		String name;
		String IP = null;
		int port = 0;
		try
		{
			File file = new File(Constants.CONFIG_FILE_PATH);
			if (!file.exists())
			    return null;
			BufferedReader in = new BufferedReader(new FileReader(file));
			StringTokenizer st;
			
			input = in.readLine();
			st = new StringTokenizer(input, "=");
			while(st.hasMoreTokens())
			{
				name = st.nextToken();
				IP = st.nextToken();
			}
			
			input = in.readLine();
			st = new StringTokenizer(input, "=");
			while(st.hasMoreTokens())
			{
				name = st.nextToken();
				port = Integer.parseInt(st.nextToken());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("config error");
		}
		
		return new Configuration(IP, port);
	}
	
	public static boolean setConfiguration(Configuration config)
	{
		File file = new File(Constants.CONFIG_FILE_PATH);
		if(!file.exists() || file.canWrite())
		{
			try
			{
				if (!file.exists())
					file.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(Constants.CONFIG_FILE_PATH));
				out.write("ipaddress=" + config.getIPAddress() + "\n");
				out.write("port=" + config.getPortNumber() + "\n");
				out.close();
				
				ServerInterface.configureServerInterface(config.getIPAddress(), config.getPortNumber());
			}
			catch(Exception e)
			{
				return false;
			}
			return true;
		}
		else
			return false;
	}
}