/*
 * Class representing a subscriber of a library
 * A subscriber knows his first and last name,
 * his born date and his number in the library
 * @author M.T. Segarra
 * @version 0.0.1
 */
package subscribersManagement;

import java.util.*;

import loansManagement.Loan;
import main.Constraints;
import mediasManagement.TypeMedia;
import exceptions.*;
import bookingsManagement.Booking;

public class Subscriber {

	/**
	 * @uml.property  name="firstName"
	 */
	private String firstName;

	/**
	 * Getter of the property <tt>firstName</tt>
	 * @return  Returns the firstName.
	 * @uml.property  name="firstName"
	 */
	public String getFirstName() {
  	return firstName;
  }

	/**
	 * Setter of the property <tt>firstName</tt>
	 * @param firstName  The firstName to set.
	 * @uml.property  name="firstName"
	 */
	public void setFirstName(String firstName) {
  	this.firstName = firstName;
  }

	/**
	 * @uml.property  name="lastName"
	 */
	private String lastName;

	/** 
	 * Getter of the property <tt>lastname</tt>
	 * @return  Returns the lastname.
	 * @uml.property  name="lastName"
	 */
	public String getLastName() {
  	return lastName;
  }

	/** 
	 * Setter of the property <tt>lastname</tt>
	 * @param lastname  The lastname to set.
	 * @uml.property  name="lastName"
	 */
	public void setLastName(String lastName) {
  	this.lastName = lastName;
  }

	/**
	 * @uml.property  name="bornDate"
	 */
	private Calendar bornDate;

	/**
	 * Getter of the property <tt>bornDate</tt>
	 * @return  Returns the bornDate.
	 * @uml.property  name="bornDate"
	 */
	public Calendar getBornDate() {
  	return bornDate;
  }

	/**
	 * Setter of the property <tt>bornDate</tt>
	 * @param bornDate  The bornDate to set.
	 * @uml.property  name="bornDate"
	 */
	public void setBornDate(Calendar bornDate) {
  	this.bornDate = bornDate;
  }

	/** 
   * @uml.property name="currentLoans"
   * @uml.associationEnd multiplicity="(0 -1)" ordering="true" inverse="lender:java.util.ArrayList"
   */
	private List<Loan> currentLoans;

	/** 
   * Getter of the property <tt>currentLoans</tt>
   * @return  Returns the currentLoans.
   * @uml.property  name="currentLoans"
   */
	public List<Loan> getCurrentLoans() {
  	return currentLoans;
  }

		
  	/** 
     * Constructor of a subscriber
     * Generates the number of the subscriber
     * @params firstName first name of the subscriber
     * @params lastName last name of the subscriber
     * @params bornDate born date of the subscriber
     * @param inscriptionDate date of his inscription
     * @throws BadParametersException
     */
  	public Subscriber(String firstName, String lastName, Calendar bornDate, Calendar inscriptionDate)	
  			throws BadParametersException {
  	
  			//Check the validity of the parameters
  			if ((firstName == null) || (lastName ==null) || (bornDate == null) || (inscriptionDate == null))
  				throw new BadParametersException();
  			
  			this.firstName = firstName;
  			this.lastName = lastName;
  			this.bornDate = bornDate;
  			this.inscriptionDate = inscriptionDate;
  			this.currentLoans = new ArrayList<Loan>();
  			this.currentBookings = new ArrayList<Booking>();
  			
  			//The subscriber has the right to lend or rigth from now on
  			this.getRightsDate = new GregorianCalendar();
  	}
  	
	/*
	 * Decides if the subscriber object (parameter) is the same
	 * subscriber as this one
	 * Same first name, last name, and born date
	 * @param subscriber object to be compared with this one
	 * @return true if parameter object and this one are equal
	 * @return false if parameter object is null or different from this one
	 */
	@Override
	public boolean equals(Object subscriber){
		if (subscriber == null)
			return false;
		Subscriber s = (Subscriber)subscriber;
		
		//Important: two subscribers are equal if their numbers are equal
		if (s.number == number)
			return true;
		
		//Or if their first name, last name and born date are equal
		if ((s.firstName== null) || (s.lastName == null)
				|| (s.bornDate == null))
			return false;
		
		if ((this.firstName== null) || (this.lastName == null) 
				|| (this.bornDate == null))
			return false;
		
		boolean res = (s.firstName.equals(firstName)) 
		&& (s.lastName.equals(lastName))
		&& (s.bornDate.equals(bornDate));
		
		return res;
	}

	/** 
	 * @uml.property name="number"
	 */
	private long number;

	/** 
	 * Getter of the property <tt>number</tt>
	 * @return  Returns the number.
	 * @uml.property  name="number"
	 */
	public long getNumber() {
  	return number;
  }
	
	public void setNumber(long number){
		this.number = number;
	}

		
	/**
	 * @param subsNumber the number of the subscriber to create
	 */
	public Subscriber(long subsNumber){
		this.number = subsNumber;
		this.currentLoans = new ArrayList<Loan>();
	}
	
	/**
	 * @return true if the subscriber has loans
	 */
	public boolean existingLoans(){
		return currentLoans.size() > 0; 
	}

	/**
	 * Add a new loan to the subscriber if quota ok
	 * @param loan the loan to add to the subscriber
	 * @throws TooManyLoansOrBookingsException 
	 * @throws BadParametersException 
	 * @throws NoRightsException 
	 */
	public void lend(Loan loan) throws TooManyLoansOrBookingsException, 
	 BadParametersException, NoRightsException 
	{
		currentLoans.add(loan);
	}

	
	/**
	 * test whether the subscriber can lend or book a media of type "type"
	 * @param type the type of media on which the test will be performed
	 * @return true if the subscriber can lend books
	 */
	public boolean canLendOrBook(TypeMedia type)
	{
		boolean ret=false;
		
		//Count the number of current loans and bookings of the type "type"
		int count=0;
		for(int i=0; i<currentLoans.size(); i++)
		{
			Loan tmp = currentLoans.get(i);
			if(tmp.getMedia().getType()==type)
				count++;
		}
		for(int i=0; i<currentBookings.size();i++)
		{
			Booking tmp = currentBookings.get(i);
			if(tmp.getMedia().getType()==type)
				count++;
		}
		
		boolean child = isAChild();
		switch(type)
		{
			//If type is as Book
			case BOOK:
				if(child)	//Subscriber is a child
				{
					if(count >= Constraints.maxBOOKLENTCHILD)
						ret = false;
					else ret = true;
				}
				else {	//Subscriber is an adult
					long year = yearSinceIns(); 
					if(year==1)
					{
						if(count>=Constraints.maxBOOKLENT1ST)
							ret = false;
						else ret = true;
					}
					else if(year==2)
					{
						if(count>=Constraints.maxBOOKLENT2ND)
							ret = false;
						else ret = true;
					}
					else if(year>=3)
					{
						if(count>=Constraints.maxBOOKLENTTHEN)
							ret = false;
						else ret = true;
					}
					else ret = false;
				}
				
				break;
			
			case DVD:
				if(child)	//Subscriber is a child
					ret = false;
				else {	//Subscriber is an adult
					long year = yearSinceIns(); 
					if(year==1)
					{
						if(count>=Constraints.maxDVDLENT1ST)
							ret = false;
						else ret = true;
					}
					else if(year==2)
					{
						if(count>=Constraints.maxDVDLENT2ND)
							ret = false;
						else ret = true;
					}
					else if(year>=3)
					{
						if(count>=Constraints.maxDVDLENTTHEN)
							ret = false;
						else ret = true;
					}
					else ret = false;
				}
				
				break;
				
		}
		
		return ret;
	}
	
		
		
  	/** 
     * Remove the loan in parameter from the list of current loans of the subscriber
     * and check whether the exemplary has been returned on time
     * @param loan the loan to be finished
     * @throws BadParametersException 
     * @throws LentOrBookedMediaException if loan not found in the list
     */
  	public void returnMedia(Loan loan)	throws BadParametersException, LentOrBookedMediaException 
  	{
  			if(loan == null)
  				throw new BadParametersException();
  			
  			if(!currentLoans.remove(loan))
  				throw new LentOrBookedMediaException("The lender does not contain the loan");
  			
  			//Check the date of the return
  			if(loan.getReturnDate().before(GregorianCalendar.getInstance()))
  			{
  				int dayLapse = loan.getReturnDate().get(Calendar.DAY_OF_YEAR) - 
  												GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR);
  				
  				//Set the punishment time (in weeks)
  				int punish = (dayLapse/7)+1;
  				
  				//Delay the obtention of the rights to book or lend
  				Calendar pDate = new GregorianCalendar();
  				pDate.add(Calendar.WEEK_OF_YEAR, punish);
  				this.getRightsDate = pDate;
  			}
  	}

	/**
   * @uml.property  name="inscriptionDate"
   */
  private Calendar inscriptionDate;

	/**
   * Getter of the property <tt>inscriptionDate</tt>
   * @return  Returns the inscriptionDate.
   * @uml.property  name="inscriptionDate"
   */
  public Calendar getInscriptionDate()
  {
  	return inscriptionDate;
  }

	/**
   * Setter of the property <tt>inscriptionDate</tt>
   * @param inscriptionDate  The inscriptionDate to set.
   * @uml.property  name="inscriptionDate"
   */
  public void setInscriptionDate(Calendar inscriptionDate)
  {
  	this.inscriptionDate = inscriptionDate;
  }

	/**
  	 * @return true if the subscriber is a minor
  	 */
  	public boolean isAChild(){
  		GregorianCalendar test = (GregorianCalendar)bornDate.clone();
  		test.set(Calendar.YEAR, test.get(Calendar.YEAR)+18);
  		
  		//If the borndate+18 of the subscriber is after today, then he's still a child
  		if(test.after(Calendar.getInstance()))
  			return true;
  		else return false;	
  	}

  	/**
  	 * 
  	 * @return the number of years since the inscription
  	 */
  	public int yearSinceIns()
  	{
  		int y=-1;
  		
  		GregorianCalendar test = (GregorianCalendar)inscriptionDate.clone();
  		for(int i=1; i<=3; i++)
  		{
  			test.set(Calendar.YEAR, test.get(Calendar.YEAR)+i);
  			if(test.after(Calendar.getInstance()))
  			{
  				y = i;
  				break;
  			}
  		}
  		if(y==-1) 
  			y=3;
  		
  		return y;
  	}
  	
  	@Override
  	public String toString()
  	{
  		String dateBirth = String.valueOf( this.bornDate.get(Calendar.MONTH)+1 ) +"/" +
					String.valueOf( this.bornDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
					String.valueOf( this.bornDate.get(Calendar.YEAR) ),
				insdate = String.valueOf( this.inscriptionDate.get(Calendar.MONTH)+1 ) +"/" +
					String.valueOf( this.inscriptionDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
					String.valueOf( this.inscriptionDate.get(Calendar.YEAR) );
  		
  		return "Firstname: "+firstName+", lastname: "+lastName+", date of birth (english): "+dateBirth+
  						", inscription date: "+insdate+", number: "+number;
  	}
  	
  	/**
     * @return a string giving the first line of the HTML table of the subscribers
     */
    public static String getSubscriberHeader()
    {
    	return "<tr><td><strong>Number</strong></td><td><strong>First name</strong>" +
    			"</td><td><strong>Last name</strong></td>" +
    			"<td><strong>Date of birth</strong></td><td><strong>Inscription date</strong>" +
    			"</td><td><strong>Has rights</strong></td></tr>";
    }
    
    
    /**
     * Print a list of subscribers in the form of a HTML table
     * @param li list of subscribers
     * @return a HTML table
     */
    public static String toHTML(List<Subscriber> li)
    {
    	StringBuilder sb = new StringBuilder();
  		
  		sb.append("<table border=\"1\" >");
  		sb.append(Subscriber.getSubscriberHeader());
  		for(Subscriber sub : li)
  			sb.append(sub.toHtml());
  		sb.append("</table>");
  		return sb.toString();
    }
    
    
    /**
     * @return a string giving a line in a HTML table format describing a subscriber
     */
    public String toHtml()
    {
    	String dateBirth = String.valueOf( this.bornDate.get(Calendar.MONTH)+1 ) +"/" +
					String.valueOf( this.bornDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
					String.valueOf( this.bornDate.get(Calendar.YEAR) ),
				insdate = String.valueOf( this.inscriptionDate.get(Calendar.MONTH)+1 ) +"/" +
					String.valueOf( this.inscriptionDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
					String.valueOf( this.inscriptionDate.get(Calendar.YEAR) );
    	
			return "<tr><td>"+this.number+"</td><td>"+this.firstName+"</td><td>"+this.lastName+"</td>" +
			"<td>"+dateBirth+"</td><td>"+insdate+"</td><td>"+(this.getRightsDate.before(Calendar.getInstance())?"Yes":"No")+"</td></tr>";
    }

		/** 
     * @uml.property name="currentBookings"
     * @uml.associationEnd multiplicity="(0 -1)" ordering="true" inverse="booker:bookingsManagement.Booking"
     */
    private List<Booking> currentBookings;

		/** 
     * Getter of the property <tt>currentBookings</tt>
     * @return  Returns the bookings.
     * @uml.property  name="currentBookings"
     */
    public List<Booking> getCurrentBookings()
    {
    	return currentBookings;
    }

		/**
		 * Try to add a booking to the list of bookings of the subscriber
		 * @param booking the booking to add to the list of bookings of the subscriber
		 * @throws BadParametersException
		 * @throws TooManyLoansOrBookingsException
		 * @throws NoRightsException 
		 */
    public void book(Booking booking)	throws BadParametersException, TooManyLoansOrBookingsException, 
    NoRightsException 
    {
    	if(booking==null)
    		throw new BadParametersException();
    	
    	if(!hasRights())
    		throw new NoRightsException();
    	
    	if(!canLendOrBook(booking.getMedia().getType()))
  			throw new TooManyLoansOrBookingsException();
  		
  		currentBookings.add(booking);
    }

    /**
     * print a message in the output, concerning the subscriber
     * @param msg the message to print
     */
    public void sendMessage(String msg)
    {
    	System.out.println("The subscriber ["+toString()+"] has a message:\n\t\""+msg+"\"\n");
    }
    
    
    /**
     * Check wether the subscriber currently has the rights to book or lend
     * @return true if he has the rights, false otherwise
     */
		public boolean hasRights()
    {
			//If the date when the subscriber receives his rights is after today
			//then he has no right to lend or book yet
    	if(getRightsDate.after(GregorianCalendar.getInstance()))
    		return false;
    	else return true;	
    }

		
		/**
     * Date when the subscriber received, or receives, his rights to lend or book
     * @uml.property  name="getRightsDate"
     */
    private Calendar getRightsDate;

		/** 
     * Getter of the property <tt>getRightDate</tt>
     * @return  Returns the getRightDate.
     * @uml.property  name="getRightsDate"
     */
    public Calendar getGetRightsDate()
    {
    	return getRightsDate;
    }

		/** 
     * Setter of the property <tt>getRightDate</tt>
     * @param getRightDate  The getRightDate to set.
     * @uml.property  name="getRightsDate"
     */
    public void setGetRightsDate(Calendar getRightsDate)
    {
    	this.getRightsDate = getRightsDate;
    }

		/**
     * @uml.property  name="booker"
     * @uml.associationEnd  inverse="currentBookings:mediasManagement.Media"
     */
    private Booking booker;

		/**
     * Getter of the property <tt>booker</tt>
     * @return  Returns the booker.
     * @uml.property  name="booker"
     */
    public Booking getBooker()
    {
    	return booker;
    }

		/**
     * Setter of the property <tt>booker</tt>
     * @param booker  The booker to set.
     * @uml.property  name="booker"
     */
    public void setBooker(Booking booker)
    {
    	this.booker = booker;
    }

		public Subscriber(){
    }

		/** 
     * Setter of the property <tt>currentBookings</tt>
     * @param currentBookings  The bookings to set.
     * @uml.property  name="currentBookings"
     */
    public void setCurrentBookings(List<Booking> currentBookings)
    {
    	this.currentBookings = currentBookings;
    }

		/** 
     * Setter of the property <tt>currentLoans</tt>
     * @param currentLoans  The currentLoans to set.
     * @uml.property  name="currentLoans"
     */
    public void setCurrentLoans(List<Loan> currentLoans)
    {
	    this.currentLoans = currentLoans;
    }
  	
}	
