package server.control.datamanagement;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import server.control.errormanagement.LogManager;
import server.dbinterface.DBInterface;
import server.serverinterface.emailinterface.EmailManager;
import com.sun.rowset.CachedRowSetImpl;

/**
 * @author Peter Abelseth
 * @version 3
 */
public class ReservationManager extends ScheduledTask 	{
	
	private static final long HR24 = 24*60*60*1000L;
	private static final int MAX_RUN_TIMES_PER_DAY = 24;
	
	private String sql_Select_Reservation_Ready_To_Email;
	private String sql_Delete_Expired_Reservations;
	
	
	/**
	 * Constructs the Manager with the given parameters
	 * @param timeToRunPerDay The number of times a day the reservation manager will be run. It will always run in intervals based off of 1 am
	 * @param periodBetweenEmails The time between sending another email to a user
	 */
	public ReservationManager(int timesToRunPerDay, long periodBetweenEmails){
		super(1);
		if(timesToRunPerDay <= 0)
			timesToRunPerDay = 1;
		else if(timesToRunPerDay > MAX_RUN_TIMES_PER_DAY)
			timesToRunPerDay = MAX_RUN_TIMES_PER_DAY;
		long interval = (HR24)/(long)timesToRunPerDay;
		setInterval(interval);
		
		setSQLSelectResevationReadyToEmailString(periodBetweenEmails);
		setSQLDeleteExpiredReservationsString();
		
		Calendar cal = Calendar.getInstance();	//get time of first time it should run
		cal.set(Calendar.HOUR_OF_DAY, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		while(cal.compareTo(Calendar.getInstance()) < 0)
			cal.add(Calendar.MILLISECOND, (int)interval);
		start(cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
		performTask();	//run right away, one time
	}
	
	/**
	 * Sends emails to users who have reservations ready and removes expired reservations
	 */
	protected void performTask(){
		try {
			removeExpiredReservations();
			sendReadyReservationEmail();	
			LogManager.getReference().logMessage(Calendar.getInstance().getTime() + " ReservationManager Running. Last Ran: " + new Date(getLastRan()));
		} catch (SQLException e) {	//sql command error or db connection error
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Manages sending emails to users who have a reservation that is ready to be picked up
	 * @throws SQLException If couldn't query the database for reservations that are ready
	 * @throws MessagingException If problem with email server
	 */
	private void sendReadyReservationEmail() throws SQLException, MessagingException{
		ArrayList<Reservation> readyReservationsToEmail = getReadyReservationsToEmail();
		
		for(Reservation reservation: readyReservationsToEmail){
			String emailSubject = "\"" + reservation.resourceTitle + "\" is Ready to be Picked Up";
			String emailAddress = reservation.email;
			String emailMessage = createReservationReadyEmail(reservation);
			try{
				EmailManager.getReference().sendEmail(emailAddress, emailSubject, emailMessage);
			}
			catch(MessagingException e){
				//bad email address, possibly add flag to the user
			}
			DBInterface.getReference().executeUpdate("UPDATE reservation SET last_email_date = UNIX_TIMESTAMP(NOW())*1000 WHERE reservation_ID = " + reservation.reservationID);
		}	
	}
	
	/**
	 * Removes expired reservations from the database
	 * @throws SQLException If couldn't send the query to the database
	 */
	private void removeExpiredReservations() throws SQLException{
		DBInterface.getReference().executeDelete(sql_Delete_Expired_Reservations);
	}
	
	/**
	 * Creates the body message to send to a user with a reservation that is ready
	 * @param reservation The Reservation that is ready to be picked up
	 * @return message The message to send to a user with a ready reservation
	 */
	private String createReservationReadyEmail(Reservation reservation){
		String heading = "Dear, " + reservation.firstName + " " + reservation.lastName + "\n\n";
		
		String bodyP1 = "Your reservation of \"" + reservation.resourceTitle + "\" is now available to be picked up.\n";
		
		Calendar expireDate = Calendar.getInstance();
		expireDate.setTimeInMillis(reservation.expireDate);
		
		String bodyP2 = "This reservation will expire on " +
				expireDate.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.LONG, Locale.CANADA) + " " +
				expireDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA) + " " +
				expireDate.get(Calendar.DAY_OF_MONTH) + ", " +
				expireDate.get(Calendar.YEAR) + ".\n";
		
		String bodyP3 = "Please come as soon as possible to pick up your reservation.\n";
		
		String footer = "\nThank you,\n" + "The UBC CICSR Library";
		
		return heading + bodyP1 + bodyP2 + bodyP3 + footer;
	}
	
	/**
	 * Queries the database for a list of reservations that are ready to be picked up
	 * @return reservationsReadyToEmail The list of reservations that are ready and need to be emailed
	 * @throws SQLException If the database couldn't be queried.
	 */
	private ArrayList<Reservation> getReadyReservationsToEmail() throws SQLException{
		CachedRowSetImpl result = DBInterface.getReference().executeSelect(sql_Select_Reservation_Ready_To_Email);
		ArrayList<Reservation> reservationsReadyToEmail = new ArrayList<Reservation>();
		while(result.next()){
			reservationsReadyToEmail.add(new Reservation(
					result.getInt("reservation_ID"),
					result.getLong("available_date"),
					result.getLong("expire_date"),
					result.getLong("last_email_date"),
					result.getString("first_name"),
					result.getString("last_name"),
					result.getString("email"),
					result.getString("title")));
		}
		return reservationsReadyToEmail;
	}
	
	/**
	 * Nested class used to store necessary information about Reservations that are ready to be picked up
	 */
	private class Reservation{
		public int reservationID;
		public long availableDate;
		public long expireDate;
		public long lastEmailDate;
		public String firstName;
		public String lastName;
		public String email;
		public String resourceTitle;
		public Reservation(int reservationID, long availableDate, long expireDate, long lastEmailDate, String firstName, String lastName, String email, String resourceTitle){
			this.reservationID = reservationID;
			this.availableDate = availableDate;
			this.expireDate = expireDate;
			this.lastEmailDate = lastEmailDate;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.resourceTitle = resourceTitle;
		}
	};
	
	private void setSQLSelectResevationReadyToEmailString(long periodBetweenEmails){
		sql_Select_Reservation_Ready_To_Email = "SELECT " +
			"reservation.reservation_ID,reservation.available_date, reservation.expire_date, reservation.last_email_date, user.first_name, user.last_name, user.email, resource.title " +
			"FROM " +
			"reservation, user, resource " +
			"WHERE " +
			"reservation.user_user_ID = user.user_ID AND " +
			"reservation.resource_resource_ID = resource.resource_ID AND " +
			"reservation.available_date IS NOT NULL AND " +													//resource is available
			"(reservation.last_email_date IS NULL OR " +													//hasn't been email yet OR
			"reservation.last_email_date < (UNIX_TIMESTAMP(NOW())*1000 - " + periodBetweenEmails + "))";	//email has been long enough ago to send another
	}


	private void setSQLDeleteExpiredReservationsString(){
		sql_Delete_Expired_Reservations = "DELETE FROM " +
			"reservation " +
			"WHERE " +
			"reservation.expire_date < UNIX_TIMESTAMP(NOW())*1000";
	}
}
