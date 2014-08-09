package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Holds common utility functions 
 * @author Daniel
 * @version 2
 *
 */
public class Utilities {

	/**
	 * Formats the given date as a String.
	 * @return the formatted date string
	 */
	public static String formatDate(Date date, String format, String timeZone) {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern(format);
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		return formatter.format(date);
	}

	/**
	 * Parses the date String.
	 * @throws ParseException if the date string is invalid
	 * @return the parsed date
	 */
	public static Date parseDate(String date, String format, String timeZone)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern(format);
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		return formatter.parse(date);
	}
	
	/**
	 * Converts the given date into a date at time 00:00.
	 * For example, it will convert "Septemper 1 at 13:24" to
	 * "September 1 at 00:00".
	 * @return the new date, or null if an error occurs
	 */
	public static Date removeTime(Date date) {
		
		// sanity check
		if (date == null)
			return null;
		
		// create new date
		Date newDate = null;
		try {
		newDate = Utilities.parseDate(Utilities.formatDate(new Date(),
				Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE),
				Constants.DEFAULT_DATE_FORMAT, Constants.DEFAULT_TIME_ZONE);
		} catch (Exception e) {
			// won't happen
			e.printStackTrace();
		}
		
		// return new date
		return newDate;
	}

}
