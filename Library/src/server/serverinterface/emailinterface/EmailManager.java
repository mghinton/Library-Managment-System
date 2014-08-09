/**
 * 
 */
package server.serverinterface.emailinterface;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import server.control.errormanagement.ErrorManager;


/**
 * Sends out emails
 * @author Peter Abelseth
 * @version 4
 *
 */
public class EmailManager {
	
	private static EmailManager singleton = null;
	
	private Properties props = null;
	
	//Parameters for email server
	private String emailHost;
	private String emailFrom;
	private String emailPassword;
	private String emailPort;
	
	private EmailManager(){
	}
	
	public static EmailManager configureEmailManager(String emailHost, String emailFrom, String emailPassword, String emailPort){
		singleton = new EmailManager();
		
		singleton.emailHost = emailHost;
		singleton.emailFrom = emailFrom;
		singleton.emailPassword = emailPassword;
		singleton.emailPort = emailPort;
		singleton.configureEmailServer();
		
		return singleton;
	}
	
	public static synchronized EmailManager getReference(){
		return singleton;
	}
	
	/**
	 * Closes EmailManager, allows it to be picked up by garbage collector
	 */
	public static synchronized void close(){
		ErrorManager.getReference().fatalSubystemError(new InstantiationException("ErrorManager not initialized!"), new EmailManager());
		singleton = null;
	}
	
	
	
	public void sendEmail(String emailAddress, String emailSubject, String emailMessage)throws MessagingException{
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailFrom,emailPassword);
					}
				});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(emailFrom));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailAddress));
		message.setSubject(emailSubject);
		message.setText(emailMessage);
		Transport.send(message);
	}
	
	private void configureEmailServer(){
		props = System.getProperties();
		props.put("mail.smtp.host", emailHost);
		props.put("mail.smtp.socketFactory.port", emailPort);
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.port", emailPort);
	}
}
