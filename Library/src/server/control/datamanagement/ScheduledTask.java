package server.control.datamanagement;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This is an abstract class that performs a scheduled task at a given starting time and repeats at the given interval time
 * @author Peter Abelseth
 * @version 1
 */
public abstract class ScheduledTask {

	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);	//create a thread pool
	private ScheduledFuture<?> handler = null;   //the task scheduler
	private Runnable taskPerformed;	//this is the task that will be run periodically
	
	private long lastRan;	//the time the task was last ran
	private long interval;	//interval between run times, in milliseconds
	
	/**
	 * Constructs a new scheduled task. Does not start until start(initialDelay) is called
	 * @param interval The time, in milliseconds, between when the next task executes
	 */
	public ScheduledTask(long interval){
		if(interval < 0)
			throw new IllegalArgumentException("The interval between running a scheduled task must be > 0");
		this.interval = interval;
		createTask();
	}
		
	/**
	 * Starts the task after the initial delay
	 * @param initialDelay The time, in milliseconds, until the first execution of the task is performed
	 */
	public void start(long initialDelay){
		if(initialDelay < 0)
			initialDelay = 0;
		if(handler == null)
			handler = scheduler.scheduleAtFixedRate(taskPerformed,initialDelay,interval,TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Stops all future executions of the task
	 */
	public void stop(){
		if(handler != null)
			handler.cancel(true);
	}
	
	/**
	 * Returns the time the task was last ran
	 * @return lastRan The time the task was last ran
	 */
	public long getLastRan(){
		return lastRan;
	}
	
	/**
	 * Returns the interval between tasks being performed
	 * @return interval the interval in milliseconds
	 */
	public long getInterval(){
		return interval;
	}
	
	/**
	 * Sets the interval between tasks being performed and stops the scheduledtask. Start must be called again to restart
	 * @param interval The interval, in milliseconds, between tasks being performed
	 */
	public void setInterval(long interval){
		stop();
		this.interval = interval;
	}
	
	/**
	 * The task that is performed on the schedule. This task must be overloaded by subclasses.
	 */
	protected abstract void performTask();
	
	/**
	 * Creates the scheduled task, calls performTask and logs the last run date
	 */
	private void createTask() {
		taskPerformed = new Runnable() { 
			public void run(){
				performTask();
				lastRan = Calendar.getInstance().getTimeInMillis();
			}
		};
	}
}
