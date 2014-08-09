package server.control.errormanagement;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Will hopefully be used for logging
 * 
 * @author Sardor
 * @version 3
 *
 */


public class LogManager {	
	private static LogManager singleton;
    private final static String errorLogFileName = "log/server.%g.log";
    private boolean consoleLoggingActive = false;

    
    /**
     * Private constructor for singleton
     */
	private LogManager(){
		
	}
	
	public static synchronized LogManager getReference( ) {
		if(singleton == null){
			singleton = new LogManager();
	         /*synchronized(Logger.class){
	                if(singleton == null){
	                    singleton = getFileLogger(errorLogFileName);
	                }
	            }*/
	         }
	     return singleton;
	}
	
	
	/**
	 * Logs a message that isn't an error. Like a client connecting
	 * 
	 * @param message The message to log
	 */
	public void logMessage(String message){
		
		System.out.println(message);	//for now just print message
	}
	
	/**
	 * Logs an error message. Does not fix error, that is ErrorManager
	 * 
	 * @param message The error to log
	 */
	public void logError(String message){
		
		System.out.println("Error: " + message);	//for now just print message
	}
	
	
	private static Logger getFileLogger(String fileName) {

        Logger logger = Logger.getLogger("server.control.LogManager");
 
        try {
            boolean append = true;
            int limit = 5024; // 5 KB Per log file, modify as required.

            String pattern = fileName;
            int numLogFiles = 5;

            FileHandler handler = new FileHandler(pattern, limit, numLogFiles, append);
            Formatter formatter = getCustomFormatter();
 
            if (formatter != null)
                handler.setFormatter(formatter);
 
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return logger;
    }

	
	public void setConsoleLogging(boolean enable){
		this.consoleLoggingActive = enable;
	}
	
	public boolean isConsoleLoggingActive(){
		return this.consoleLoggingActive;
	}
	
	
	private static Formatter getCustomFormatter() {
        return new Formatter() {
            public String format(LogRecord record) {
                String recordStr = "[Date] " + new Date() + " [Log Level] "
                        + record.getLevel() + ": [Class] "
                        + record.getSourceClassName() + " [Method] "
                        + record.getSourceMethodName() + " [Message] "
                        + record.getMessage() + "\n";
                return recordStr;
            }
        };
    }
	

}
