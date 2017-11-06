//$Id$
package uplus;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/* for task timer
 * 
 */
public class config implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        
    	System.out.println("In config class ,,ServletContextListener");
    	TimerTask task = new TimerTask() {
    	      @Override
    	      public void run() {
    	        // task to run goes here
    	        System.out.println("This is for checking the updating the statuses of finished rides !!!");
    	        dbutil.updateStatusOfFinsihedRides();
    	      }
    	    };
    	    
    	    Timer timer = new Timer();
    	    long delay = 0;
    	    long intevalPeriod = 1 * 10000; 
    	    
    	    // schedules the task to be run in an interval 
    	    timer.scheduleAtFixedRate(task, delay,
    	                                intevalPeriod);
    	  
    	  } 
    
    public void contextDestroyed(ServletContextEvent event) {
        
    }
}