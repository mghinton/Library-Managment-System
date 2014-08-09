package client.serverinterface;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import communication.RequestPacket;

/**
 * This is the Network Connection manager, which represents connection between client and server
 * This is protected class and can only be accessed by Server Interface class
 * It will connect to server, and create Input and Output streams for messaging 
 * 
 * @author Sardor Isakov
 * @version 5
 */
class NetworkConnection {
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;
	
	// the server, the port and the address
	private String serverIP;
	private int port;
	
	boolean reconnect = false;
	
	/** 
	 * Constructor of the Network Consnection manager class
	 * 
	 * @param serverIP Server's IP address
	 * @param port Server's port number
	 */
	protected NetworkConnection(String serverIP, int port) {
		this.serverIP = serverIP;
		this.port = port;
	}
	/**
	 * This method creates the client socket. It will initialize the socket, ObjectInputStream, and ObjectOutputStream
	 * 
	 * @return boolean Returns true if connected to server
	 */
	protected boolean configure() {
		try {
			socket = new Socket(serverIP, port);
			socket.setKeepAlive(true);
		} 
		catch(UnknownHostException e) {
			// TODO
			System.out.println(e.getMessage());
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		
		log(msg);
		try {
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	protected RequestPacket sendRequest(RequestPacket request){
		try {
			sOutput.writeObject(request);
			sOutput.flush();
		} catch (SocketException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Object obj = null;
		try {
			obj = sInput.readObject();
		} catch(EOFException e){
			// Do nothing, this means we reach the end of the object! And is ok (I think...)
			//System.out.println(e.getMessage());
			//e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(obj instanceof RequestPacket)
			return (RequestPacket)obj;
		return null;
	}
	
	
	/**
	 * @deprecated
	 * @param request
	 */
	protected void sendMessage(RequestPacket request) {
			try {
				sOutput.writeObject(request);
				sOutput.flush();
			}
			catch(IOException e) {
				log("Exception writing to server: " + e);
			}
		}
	
	/**
	 * @deprecated
	 * @return
	 */
	protected RequestPacket getResponse() {
		try {
			//while(sInput.available() <= 0);	//wait for something to be available
			Object obj = sInput.readObject();
			RequestPacket response = null;
			if(obj instanceof RequestPacket){
				response = (RequestPacket) obj;
			}
			// if console mode print the message and add back the prompt
			return response;
		}
		catch(EOFException e) {
			//log("Server has close the connection: " + e);
			System.out.println("EOF Exception");
			//return getResponse();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException e) {
		}
		return null;
	}
	
	
	private void log(String msg) {
		System.out.println(msg);
	}

}
