package main;

/*
 * Class offering the main functions of the library
 * @author M.T. Segarra
 * @version 0.0.1
 */

import historyManagement.History;

import java.util.*;

import loansManagement.Loan;
import mediasManagement.*;
import subscribersManagement.*;
import utils.HibernateUtil;
import exceptions.*;

import bookingsManagement.*;

import java.util.concurrent.locks.*;

import org.hibernate.*;
import loansManagement.LoanManager;
import historyManagement.HistoryManager;


public class Library {

  		
		/** 
		 * Adds a new media to the library
		 * @param media the media to be added
		 * @throws BadParametersException
		 * @throws MediaExistsException
		 */
	public void addMedia(Media media)	throws BadParametersException, MediaExistsException {

		if (media == null)
			throw new BadParametersException();
    	  	
		//Look if the book is already in the library
		if (isAMedia(media))
			throw new MediaExistsException();
    
		//Add the media to the database
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		mediaManager.addOrUpdateMedia(session, media);
		session.getTransaction().commit();
	}

	/**
	 * This method add data about a new subscriber
	 * @param s the subscriber to be added
	 * @return subscriber number
	 * @throws BadParametersException
	 * @throws SubscriberExistsException
	 */
	public long addSubscriber(Subscriber s) throws BadParametersException,
			SubscriberExistsException 
	{
		if (s == null)
			throw new BadParametersException();

		// Do not subscribe more than once a subscriber
		if (isASubscriber(s))
			throw new SubscriberExistsException();

		//Add the subscriber to the database
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		subscriberManager.addSubscriber(session, s);
		session.getTransaction().commit();
		
		return s.getNumber();
	}

		
  	/** 
     * This method deletes all data of a media from the library
     * @param key the key of the media to delete
     * @throws BadParametersException
     * @throws MediaExistsException
     * @throws LentOrBookedMediaException
     */
  	public void deleteMedia(String key)	throws BadParametersException, 
  		MediaExistsException, LentOrBookedMediaException 
  	{
  			Media media = lookForMedia(key);	
  		
  			if (media == null)
  				throw new BadParametersException();

  			// If the book is lent do not delete
  			if (media.isLent())
  				throw new LentOrBookedMediaException("Media lent. Cannot delete.");
  			
  			session = HibernateUtil.getSessionFactory().getCurrentSession();
  			session.beginTransaction();
  			
  			//Will also delete the bookings
  			mediaManager.delMedia(session, media);
  			
  			session.getTransaction().commit();
  	}

	/**
	 * This method deletes data of a subscriber from the library
	 * @param subsNumber number of the subscriber
	 * @throws BadParametersException
	 * @throws SubscriberExistsException
	 */
	public void deleteSubscriber(long subsNumber) throws BadParametersException,
			SubscriberWithLoansException, SubscriberExistsException 
	{
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Subscriber s = subscriberManager.getByNumber(session, subsNumber);
		
		// If the number is not a subscriber number
		// raise exception
		if (s==null){
			session.getTransaction().commit();
			throw new SubscriberExistsException();
		}
		
		// If loans, remove the rights to lend or book and raise exception
		if (s.existingLoans())
		{
			//Rights available in 200 years, to assure that the subscriber
			//will never have the rights again
			Calendar date = new GregorianCalendar();
			date.add(Calendar.YEAR, 200);
			s.setGetRightsDate(date);
			
			subscriberManager.removeRights(session,s);
			session.getTransaction().commit();
			
			throw new SubscriberWithLoansException();
		}

		// Remove the subscriber
		s.sendMessage("You have been removed from the library. All your bookings have been canceled and your rights removed.");
		
		session.delete(s);	//Will automatically delete the bookings of the subscriber
		session.getTransaction().commit();
	}
		
  		
    	/** 
       * Method to lend a media for a subscriber
       * @param keyMedia the key of the media to be lent
       * @param subsNumber the number of the subscriber who whants to lend
       * @return return exemplary lent
       * @throws BadParametersException
       * @throws MediaExistsException
       * @throws LentOrBookedMediaException
       * @throws SubscriberExistsException
       * @throws SubscriberWithLoansException
       * @throws TooManyLoansOrBookingsException
    	 * @throws NoRightsException 
       */
    	public Integer lend(String keyMedia, long subsNumber)	throws BadParametersException, 
    			MediaExistsException, LentOrBookedMediaException, SubscriberExistsException, TooManyLoansOrBookingsException, NoRightsException 
    	{	
    			session = HibernateUtil.getSessionFactory().getCurrentSession();
    	  	session.beginTransaction();
    	  	
    	  	//Get the media from the database
    			Media media = mediaManager.getByKey(session,keyMedia);
    			
    			// Check if the media exists
    			if (media == null) { 
    				session.getTransaction().commit();
    				throw new MediaExistsException();
    			}
    	  	
    	  	
    	  	// Get the subscriber from the database, given his number
    	  	Subscriber lender = subscriberManager.getByNumber(session, subsNumber);
    	  	
    	  	// Check if the subscriber exists
    	  	if (lender==null) {
    	  		session.getTransaction().commit();
    	  		throw new SubscriberExistsException();
    	  	}
    	  	    	  	
    	  	
    	  	//Check the bookings of the lender, if he has booked the media
    	  	//and the booking has been taken into account, he will be able
    	  	//to borrow an exemplary of the media
    	  	this.lockBookings.lock();
  	  		try
  	  		{
  	  			Booking booking = bookingManager.getBookingFromBooker(session, lender, media);
  	  			
  	  			if(booking!=null)
  	  			{
  	  				//removes the booking from the list of bookings of the subscriber, and of the library
      				lender.getCurrentBookings().remove(booking);
      				bookingManager.delBooking(session, booking);
      	  				
      				//Make the loan possible
      	 			media.setBookedExemplaries(media.getBookedExemplaries()-1);
  	  			}
    	  	}
  				finally{
  					this.lockBookings.unlock();
  					
  					session.getTransaction().commit();
  	  		}
    	  			
    	  	// Make the loan if possible
    	  	Loan l = Loan.createLoan(lender, media);
    	  	
    	  	session = HibernateUtil.getSessionFactory().getCurrentSession();
    	  	session.beginTransaction();
    	  	loanManager.addOrUpdateLoan(session, l);
    	  	session.getTransaction().commit();
    	  	
    	  	return l.getExemplary();
    	}

	/**
	 * @return a string listing the current loans
	 */
	public String listLoans() 
	{
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = loanManager.toString(session);
		
		session.getTransaction().commit();
		
		return s;
	}
	
	/**
	 * @return a string listing the current loans in a HTML table
	 */
	public String listLoansHTML() 
	{
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = loanManager.toHTML(session);
		
		session.getTransaction().commit();
		
		return s;
	}

	/**
	 * @return all subscribers of the library
	 */
	public String listSubscribers() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = subscriberManager.toString(session);
		
		session.getTransaction().commit();
		
		return s;
	}
	
	
	/**
	 * @return all subscribers of the library, in the form of a HTML table
	 */
	public String listSubscribersHTML()
	{
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = subscriberManager.toHTML(session);
		
		session.getTransaction().commit();
		
		return s;
	}

		
  		
	/** 
	 * This method returns the exemplary of the Media which key is passed in parameter
	 * @param key the key of the media to return
	 * @param exemplary the exemplary of the media which is to be returned
	 * @throws MediaExistsException
	 * @throws LentOrBookedMediaException
	 * @throws BookingAccountedException 
	 */
	public void returnMedia(String key, int exemplary)	throws BadParametersException, 
		MediaExistsException, LentOrBookedMediaException, NotAnExemplaryException, 
		BookingAccountedException 
	{
		if (key == "" || exemplary<1)
			throw new BadParametersException();
          		
		//Returns an exception if the media doesn't exist
		Media media = lookForMedia(key);

		//Begin the hibernate transaction
		session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();	  			
    
		// Ask the media to make return
		History history = media.returnExempMedia(exemplary);
		
		//Delete the loan of the exemplary returned in the database
		//and update the media in the database
		loanManager.addOrUpdateLoan(session, history.getLoan());
		mediaManager.addOrUpdateMedia(session, media);
		
		//Add the history to the database
		historyManager.addHistory(session, history);
		
		//Check if the media has been booked by a subscriber
		//and if the booking has not already been taken into account
		this.lockBookings.lock();
		try
		{
			//Get the first booking not accounted, concerning the given media
			Booking booking = bookingManager.getWaitingBooking(session, media);
			
			//If there is such a booking...
			if(booking != null)
			{
				//Take account of the booking
				booking.setAccounted();
  			
  			//One more exemplary of the media won't be lent unless booked
  			media.setBookedExemplaries(media.getBookedExemplaries()+1);
  			
  			//In the database, modify the booking and the media
  			session.saveOrUpdate(booking);
  			session.saveOrUpdate(media);
  			
  			//Notice the booker that he can lend the media
  			booking.getBooker().sendMessage("You can come to the library to get your booked media: ["+media.toString()+"]");
			}
  	}
		finally{
			lockBookings.unlock();
			session.getTransaction().commit();	//Commit the hibernate transaction
		}
	}

		
  	/** 
     * A utility method to know if a media is already in the library
     * @param media media checked
     * @return false if the media not known by the library
     * @return true if the media is known by the library
     */
  	private boolean isAMedia(Media media)	throws BadParametersException 
  	{			
  			try{
  				// If the object is in the database, it is a known media
  				// and it will raise a MediaExistsException exception
  				lookForMedia(media.getKey());
  				return true;
  			}
  			catch(MediaExistsException e) {
  				return false;
  			}
  			catch(Exception e){
  				throw new BadParametersException();
  			}
  	}

	/**
	 * A utility method to know if a subscriber is known of the library
	 * @param number of the subscriber 
	 * @return false if the subscriber of the parameter is not known of the library
	 * @return true if the subscriber of the parameter is known of the library
	 */
	private boolean isASubscriber(Subscriber subscriber) throws BadParametersException 
	{
		try{
			// If the object is in the database, it is a known subscriber
			// and it will raise a SubscriberExistsException exception
			lookForSubscriber(subscriber.getFirstName(), subscriber.getLastName(), subscriber.getBornDate());
			return true;
		}
		catch(SubscriberExistsException e) {
			return false;
		}
		catch(Exception e){
			throw new BadParametersException();
		}
	}
		
	@SuppressWarnings("unchecked")
  public Subscriber lookForSubscriber(String firstname, String lastname, Calendar bornDate)
	 throws BadParametersException, SubscriberExistsException
	{
		if(firstname.isEmpty() || lastname.isEmpty() || bornDate==null)
			throw new BadParametersException();
		
		//Connection to hibernate
		session = HibernateUtil.getSessionFactory().getCurrentSession();
  	session.beginTransaction();
  	
  	//Create the HQL query to get the subscriber from the database
  	Query query = session.createQuery("from Subscriber subs where subs.firstName='"+
  			firstname+"' and subs.lastName='"+lastname+"' and subs.bornDate=:borndate")
  			.setParameter("borndate", bornDate);
  	
  	List<Subscriber> li = (List<Subscriber>)query.list();
  	
  	//If no one found, raise exception
  	if(li.size()<=0)
  		throw new SubscriberExistsException();
  	
  	Subscriber s = li.get(0);
  
  	session.getTransaction().commit();
  	
  	return s;
  }

	
	/**
	 * Gets a media in the library, based on his key
	 * @param key key of the media we are looking for
	 * @return the media which is searched
	 * @throws MediaExistsException
	 * @throws BadParametersException
	 */
  public Media lookForMedia(String key) throws MediaExistsException, BadParametersException
 	{		
 		if(key.isEmpty())
 			throw new BadParametersException();
 		
 		//Connection to hibernate
 		session = HibernateUtil.getSessionFactory().getCurrentSession();
 	 	session.beginTransaction();
 	  	
 	 	//Retrieve the good media from the database
 	  Media m = mediaManager.getByKey(session, key);
 	  
 	  //If no one found, raise exception
 	  if(m == null)
 	  	throw new MediaExistsException();
 	  
 	  session.getTransaction().commit();
 	  
 	  return m;
 	}

 	/**
 	 * Add an exemplary to a media in the library
 	 * @param key key of the media to which we add an exemplary
 	 * @throws BadParametersException
 	 * @throws MediaExistsException
 	 */
	public void addExemplary(String key) throws BadParametersException, MediaExistsException
	{
		Media m = this.lookForMedia(key);
		m.addExemplary(1);
		
		session=HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		mediaManager.addOrUpdateMedia(session, m);
		session.getTransaction().commit();
	}
	
	/**
	 * Delete an exemplary of a media present in the library
	 * @param keykey of the media
	 * @throws MediaExistsException
	 * @throws BadParametersException
	 * @throws LentOrBookedMediaException
	 * @throws NotEnoughExemplariesException 
	 */
	public void delExemplary(String key) throws MediaExistsException, 
		BadParametersException, LentOrBookedMediaException, NotEnoughExemplariesException 
	{
		Media m = this.lookForMedia(key);
		m.delExemplary();
		
		//Hibernate seems to update automatically tabExemplaries
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.saveOrUpdate(m);
		session.getTransaction().commit();
	}
	
	/**
	 * Prints a list of all the medias present in the library
	 */
	public String listMedias()
	{
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = mediaManager.toString(session);
		
		session.getTransaction().commit();
		
		return s;
	}

	
	/**
	 * Prints a list of all the medias present in the library
	 * in the form of a HTML table
	 */
	public String listMediasHTML()
	{
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = mediaManager.toHTML(session);
		
		session.getTransaction().commit();
		
		return s;
	}
	
  /**
   * Make a subscriber book a media
   * @param keyMedia the key of the media to be booked
   * @param subsNumber the number of the subscriber who whants to book a media
   * @return the date when the booking is cancelled because the subscriber does not come and lend the media
   * @throws SubscriberExistsException 
   * @throws BadParametersException 
   * @throws MediaExistsException 
   * @throws MediaAvailableException 
   * @throws TooManyLoansOrBookingsException 
   * @throws NoRightsException 
   */
	public Calendar book(String keyMedia, long subsNumber) throws BadParametersException, 
		SubscriberExistsException, MediaExistsException, TooManyLoansOrBookingsException, 
		MediaAvailableException, NoRightsException
	{
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		//Get the media from the database
		Media media = mediaManager.getByKey(session,keyMedia);
		
		// Check if the media exists
		if (media == null)
			throw new MediaExistsException();

		//Get the subscriber from the database
		Subscriber booker = subscriberManager.getByNumber(session, subsNumber);
		
		// Check if the subscriber exists
		if (booker == null)
			throw new SubscriberExistsException();

		
		//Make the booking if possible
		Booking booking = new Booking(booker,media);
		
		this.lockBookings.lock();
		try
		{
  		bookingManager.addBooking(session, booking);
		}
		finally {
			lockBookings.unlock();
			session.getTransaction().commit();
		}
		
		return booking.getCancelDate();
	}

	
	public String listBookings()
 	{
 		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = bookingManager.toString(session);
		
		session.getTransaction().commit();
		
		return s;
  }
	
	public String listBookingsHTML()
 	{
 		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = bookingManager.toHTML(session);
		
		session.getTransaction().commit();
		
		return s;
  }

	/**
   * @uml.property  name="lockBookings" readOnly="true"
   */
  private Lock lockBookings;



	/**
   * Getter of the property <tt>lockBookings</tt>
   * @return  Returns the lockBookings.
   * @uml.property  name="lockBookings"
   */
  public Lock getLockBookings()
  {
  	return lockBookings;
  }

	/**
   * @uml.property  name="checkInvalidBookings"
   * @uml.associationEnd  multiplicity="(1 1)" inverse="library:bookingsManagement.CheckInvalidBookings"
   */
  private CheckInvalidBookings checkInvalidBookings;


	/**
   * Getter of the property <tt>checkInvalidBookings</tt>
   * @return  Returns the checkInvalidBookings.
   * @uml.property  name="checkInvalidBookings"
   */
  public CheckInvalidBookings getCheckInvalidBookings()
  {
  	return checkInvalidBookings;
  }

		
	public Library()
	{
		//Create the managers of the subscribers, medias,
		//loans and bookings, which add, delete, etc... in the database
		subscriberManager = new SubscriberManager();
		mediaManager = new MediaManager();
		loanManager = new LoanManager();
		bookingManager = new BookingManager();
		historyManager = new HistoryManager(); 
		
		this.lockBookings = new ReentrantLock();
		
		//Initialize hibernate
		session = HibernateUtil.sessionFactory.openSession();
		
		//Create data in the dababase to run tests
  	//createDataTest();	
		
		//this.checkInvalidBookings = new CheckInvalidBookings(this.bookings,this.lockBookings);
		
		//Launch the thread which checks if all the bookings are still effective
		//DOES NOT WORK YET
		//this.checkInvalidBookings.start();
	}
	
	
	/**
   * Launch the library
   * @param args
   */
  public static void main(String[] args)
  {
  	try {
		  Library lib = new Library();
		  ArrayList<String> dir = new ArrayList<String>();
		  dir.add("John");
		  
		  System.out.println(lib.lookForMediaTitle(" "));
		  
		  //lib.addMedia(new DVD("Juju à la plage encore",dir,new GregorianCalendar(2000,10,10), 3, "123456789"));
		  //lib.lend("123456789", 2);
		  //lib.addSubscriber(new Subscriber("Audrey", "Oudard", new GregorianCalendar(1992,6,14), new GregorianCalendar()));
		  //lib.returnMedia("123456789", 1);
		  //lib.returnMedia("025fd2a5f4a", 2);
		  //lib.book("123456789", 4);
		  //lib.addExemplary("41117895");
		  //lib.delExemplary("123456789");
		  //lib.deleteMedia("025fd2a5f4a");
		  //lib.deleteSubscriber(1);
		  //System.out.println(lib.listSubscribers());
		  //Media m = lib.lookForMedia("1123456789");
		  //System.out.println(lib.listMedias());
		  //lib.listMedias();
		  //lib.lend("1123456789", 9);
		  //System.out.println(lib.listLoans());
		  
	  }
  	catch(Exception e) 
  	{
  		System.out.println("Problem in the execution of the main thread");
  		e.printStackTrace();
  	}
	  finally {
		  // End of the program
		  HibernateUtil.getSessionFactory().close();
	  }
  	
  }

	/**
   * @uml.property  name="session"
   */
  private Session session;



	/**
   * Getter of the property <tt>session</tt>
   * @return  Returns the session.
   * @uml.property  name="session"
   */
  public Session getSession()
  {
  	return session;
  }

	/**
   * Setter of the property <tt>session</tt>
   * @param session  The session to set.
   * @uml.property  name="session"
   */
  public void setSession(Session session)
  {
  	this.session = session;
  }

	/**
   * @uml.property  name="subscriberManager"
   * @uml.associationEnd  inverse="library:subscribersManagement.SubscriberManager"
   */
  private SubscriberManager subscriberManager;



	/**
   * Getter of the property <tt>subscriberManager</tt>
   * @return  Returns the subscriberManager.
   * @uml.property  name="subscriberManager"
   */
  public SubscriberManager getSubscriberManager()
  {
  	return subscriberManager;
  }

	/**
   * Setter of the property <tt>subscriberManager</tt>
   * @param subscriberManager  The subscriberManager to set.
   * @uml.property  name="subscriberManager"
   */
  public void setSubscriberManager(SubscriberManager subscriberManager)
  {
  	this.subscriberManager = subscriberManager;
  }

/**
 * Test the hibernate connection
 * @throws BadParametersException
 * @throws TooManyLoansOrBookingsException
 * @throws LentOrBookedMediaException
 * @throws NoRightsException
 * @throws MediaAvailableException
 */
  public void createDataTest() throws BadParametersException, TooManyLoansOrBookingsException, 
  	LentOrBookedMediaException, NoRightsException, MediaAvailableException
  {
  	ArrayList<String> authors = new ArrayList<String>();
  	authors.add("un mec");
  	authors.add("un autre encore");
  	Media m = new Book("Un jour, une histoire",authors,new GregorianCalendar(), 2,"025fd2a5f4a");
  	Subscriber s = new Subscriber("julien","guery",new GregorianCalendar(), new GregorianCalendar());
 		
 		try {
 			long l = this.addSubscriber(s);
 			this.addMedia(m);
 			this.lend("025fd2a5f4a", l);
 			this.lend("025fd2a5f4a", l);
 			this.book("025fd2a5f4a", l);
 		}
 		catch (Exception pe){
		  System.err.println("problème dans la creation des datas");				  
		  pe.printStackTrace();    
	  }
  }
  
  /**
   * @uml.property  name="bookingManager"
   * @uml.associationEnd  inverse="library:bookingsManagement.BookingManager"
   */
  private BookingManager bookingManager;
  
  
  
  /**
   * Getter of the property <tt>bookingManager</tt>
   * @return  Returns the bookingManager.
   * @uml.property  name="bookingManager"
   */
  public BookingManager getBookingManager()
  {
  	return bookingManager;
  }
  
  /**
   * Setter of the property <tt>bookingManager</tt>
   * @param bookingManager  The bookingManager to set.
   * @uml.property  name="bookingManager"
   */
  public void setBookingManager(BookingManager bookingManager)
  {
  	this.bookingManager = bookingManager;
  }
  
  /**
   * @uml.property  name="loanManager"
   * @uml.associationEnd  inverse="library:loansManagement.LoanManager"
   */
  private LoanManager loanManager;
  
  
  
  /**
   * Getter of the property <tt>loanManager</tt>
   * @return  Returns the loanManager.
   * @uml.property  name="loanManager"
   */
  public LoanManager getLoanManager()
  {
  	return loanManager;
  }
  
  /**
   * Setter of the property <tt>loanManager</tt>
   * @param loanManager  The loanManager to set.
   * @uml.property  name="loanManager"
   */
  public void setLoanManager(LoanManager loanManager)
  {
  	this.loanManager = loanManager;
  }
  
  /**
   * @uml.property  name="mediaManager"
   * @uml.associationEnd  inverse="library:mediasManagement.MediaManager"
   */
  private MediaManager mediaManager;
  
  
  
  /**
   * Getter of the property <tt>mediaManager</tt>
   * @return  Returns the mediaManager.
   * @uml.property  name="mediaManager"
   */
  public MediaManager getMediaManager()
  {
  	return mediaManager;
  }

  /**
   * Setter of the property <tt>mediaManager</tt>
   * @param mediaManager  The mediaManager to set.
   * @uml.property  name="mediaManager"
   */
  public void setMediaManager(MediaManager mediaManager)
  {
  	this.mediaManager = mediaManager;
  }

	/**
   * @uml.property  name="historyManager"
   * @uml.associationEnd  inverse="library:historyManagement.HistoryManager"
   */
  private HistoryManager historyManager;



	/**
   * Getter of the property <tt>historyManager</tt>
   * @return  Returns the historyManager.
   * @uml.property  name="historyManager"
   */
  public HistoryManager getHistoryManager()
  {
	  return historyManager;
  }

	/**
   * Setter of the property <tt>historyManager</tt>
   * @param historyManager  The historyManager to set.
   * @uml.property  name="historyManager"
   */
  public void setHistoryManager(HistoryManager historyManager)
  {
	  this.historyManager = historyManager;
  }

		
  /**
   * @return a string containing the history of the loans of the library
   */
  public String printLoanHistory()
  {
  	session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = historyManager.toString(session);
		
		session.getTransaction().commit();
		
		return s;
  }
  
  /**
   * @return a string containing the history of the loans of the library in the form of a HTML table
   */
  public String printLoanHistoryHTML()
  {
  	session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		String s = historyManager.toHTML(session);
		
		session.getTransaction().commit();
		
		return s;
  }

	
  /**
   * Look for medias which have a certain set of caracters in their titles
   * @param title title of the media looked for
   * @return a list of medias containing the given title in their titles
   * @throws BadParametersException
   * @throws MediaExistsException
   */
  @SuppressWarnings("unchecked")
  public List<Media> lookForMediaTitle(String title) throws BadParametersException, MediaExistsException
  {
  	if(title.isEmpty())
 			throw new BadParametersException();
 		
  	title = title.toLowerCase();
  	
 		//Connection to hibernate
 		session = HibernateUtil.getSessionFactory().getCurrentSession();
 	 	session.beginTransaction();
 	  	
 	 	//Create the HQL query to get the media from the database
  	Query query = session.createQuery("from Media m where lower(m.title) like '%"+title+"%'");
  	
  	List<Media> li = (List<Media>)query.list();
  	
  	//If no one found, raise exception
  	if(li.size()<=0)
  		throw new MediaExistsException();
 	 	
 	  session.getTransaction().commit();
 	  
 	  return li;
  }
  
  
  /**
   * Look for subscribers whose last names contain the string lastname
   * @param lastname the string to look for in the subscribers' names
   * @return A list of the subscribers whose last names contain the given parameter
   * @throws BadParametersException
   * @throws SubscriberExistsException
   */
  @SuppressWarnings("unchecked")
  public List<Subscriber> lookForSubscriberLastname(String lastname) 
  		throws BadParametersException, SubscriberExistsException
  {
  	if(lastname.isEmpty())
  		throw new BadParametersException();
  	
  	lastname = lastname.toLowerCase();
  	
  	//Connection to hibernate 
  	session = HibernateUtil.getSessionFactory().getCurrentSession();
  	session.beginTransaction();
  	
  	//Create the HQL query to get the subscriber from the database
  	Query query = session.createQuery("from Subscriber s where lower(s.lastName) like '%"+lastname+"%'");
  	
  	List<Subscriber> li = (List<Subscriber>)query.list();
  	
  	//If no one found, raise exception
  	if(li.size()<=0)
  		throw new SubscriberExistsException();
  	
  	session.getTransaction().commit();
  	
  	return li;
  }


}
