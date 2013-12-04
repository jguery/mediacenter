package bookingsManagement;

import java.util.*;

import exceptions.*;
import java.util.concurrent.locks.Lock;


public class CheckInvalidBookings extends Thread
{

	/**
	 * Called after start() is called, and the thread dies when this method ends
	 */
	@Override
  public void run()
  {
  	System.out.println("Go!!");
		while(true)	//the method never ends
  	{
  		
  		//Check the cancel date of every booking
			this.lock.lock();
			try
			{
    		Iterator<Booking> it = bookingsList.iterator();
    		while(it.hasNext())
    		{
  
      		Booking tmp = it.next();
      		
      		Calendar cDate = tmp.getCancelDate();
      		if(cDate==null)	//If cancel date has not been set (booking not taken into account)
      			continue;
      		
      		//if the cancel date has been reached...
      		if( tmp.getCancelDate().after(GregorianCalendar.getInstance()) )
      		{
        		//Free one exemplary of the media, so that it can be lent
      			tmp.getMedia().setBookedExemplaries(tmp.getMedia().getBookedExemplaries()-1);
        		
        		//Remove the booking from the list of bookings of the library
        		bookingsList.remove(tmp);
        		
        		//Remove the booking from the list of bookings of the booker
        		tmp.getBooker().getCurrentBookings().remove(tmp);
      		}
    		}
  			
  		}
			finally {
				lock.unlock();
			}
  		
  	}
  }

  /**
   * Creates a thread that checks if the cancel dates of the bookings 
   * taken into account are before this day
   * @param bookings reference towards the list of bookings of the library
   * @throws BadParametersException
   */
  public CheckInvalidBookings(List<Booking> bookings, Lock lock) 
  		throws BadParametersException
  {
  	super();					//Calls the constructeur of Thread
  	setDaemon(true);	//Sets the thread as a daemon
  	setPriority(Thread.MIN_PRIORITY);	//With minimum priority
  	
  	if(bookings==null)
  		throw new BadParametersException();
  	
  	this.bookingsList = bookings;
  	this.lock = lock;
  }


	/**
   * @uml.property  name="bookingsList"
   */
  private List<Booking> bookingsList;


	/**
   * Getter of the property <tt>bookingsList</tt>
   * @return  Returns the bookingsList.
   * @uml.property  name="bookingsList"
   */
  public List<Booking> getBookingsList()
  {
  	return bookingsList;
  }


	/**
   * Setter of the property <tt>bookingsList</tt>
   * @param bookingsList  The bookingsList to set.
   * @uml.property  name="bookingsList"
   */
  public void setBookingsList(List<Booking> bookingsList)
  {
  	this.bookingsList = bookingsList;
  }


	/**
   * @uml.property  name="lock"
   */
  private Lock lock;


	/**
   * Getter of the property <tt>lock</tt>
   * @return  Returns the lock.
   * @uml.property  name="lock"
   */
  public Lock getLock()
  {
	  return lock;
  }

	/**
   * Setter of the property <tt>lock</tt>
   * @param lock  The lock to set.
   * @uml.property  name="lock"
   */
  public void setLock(Lock lock)
  {
	  this.lock = lock;
  }

}
