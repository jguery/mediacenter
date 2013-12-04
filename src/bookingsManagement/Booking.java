package bookingsManagement;

import exceptions.*;

import java.util.*;

import main.Constraints;
import mediasManagement.Media;
import subscribersManagement.Subscriber;


public class Booking
{

	/**
   * Sets wether the booking has been taken into account or not yet
   * @uml.property  name="accounted"
   */
  private boolean accounted;

	/**
   * Getter of the property <tt>accounted</tt>
   * @return  Returns the accounted.
   * @uml.property  name="accounted"
   */
  public boolean isAccounted()
  {
  	return accounted;
  }

	/**
   * Setter of the property <tt>accounted</tt>
   * @param accounted  The accounted to set.
   * @uml.property  name="accounted"
   */
  public void setAccounted(boolean accounted)
  {
  	this.accounted = accounted;
  }

	/** 
   * gives the date when the subscriber booked the media
   * @uml.property name="bookingDate"
   */
  private Calendar bookingDate;

	/** 
   * Getter of the property <tt>bookingDate</tt>
   * @return  Returns the bookingDate.
   * @uml.property  name="bookingDate"
   */
  public Calendar getBookingDate()
  {
  	return bookingDate;
  }

	/** 
   * Setter of the property <tt>bookingDate</tt>
   * @param bookingDate  The bookingDate to set.
   * @uml.property  name="bookingDate"
   */
  public void setBookingDate(Calendar bookingDate)
  {
  	this.bookingDate = bookingDate;
  }

	/**
   * gives the date when the booking becomes invalid
   * @uml.property  name="cancelDate"
   */
  private Calendar cancelDate;

	/**
   * Getter of the property <tt>cancelDate</tt>
   * @return  Returns the cancelDate.
   * @uml.property  name="cancelDate"
   */
  public Calendar getCancelDate()
  {
  	return cancelDate;
  }

	/**
   * Setter of the property <tt>cancelDate</tt>
   * @param cancelDate  The cancelDate to set.
   * @uml.property  name="cancelDate"
   */
  public void setCancelDate(Calendar cancelDate)
  {
  	this.cancelDate = cancelDate;
  }
		
  	/**
  	 * 
  	 * @param subscriber booker
  	 * @param media media to be booked
  	 * @throws BadParametersException
  	 * @throws TooManyLoansOrBookingsException 
  	 * @throws MediaAvailableException 
  	 * @throws NoRightsException 
  	 */
  	public Booking(Subscriber subscriber, Media media)	throws BadParametersException, 
  		TooManyLoansOrBookingsException, MediaAvailableException, NoRightsException 
  	{
  		if(subscriber==null || media==null)
  			throw new BadParametersException();
  		
  		this.booker = subscriber;
  		this.media = media;
  		this.bookingDate = new GregorianCalendar();
  		
  		//When creating the booking, the media is unavailable, so the cancel date
  		//hasn't been fixed yet
  		this.cancelDate = null;
  		
  		//To be booked, a media must be completly unavailable
  		if(media.isAvailableForLoan())
  			throw new MediaAvailableException();
  		
  		this.accounted = false;	//By default, the booking is not accounted (cause no exemplary is available)
  		
  		//Add the booking to the list of bookings of the subscriber, if possible
  		booker.book(this);
  	}

		/** 
     * @uml.property name="media"
     * @uml.associationEnd inverse="booking:mediasManagement.Media"
     */
    private Media media;

    /** 
     * Getter of the property <tt>media</tt>
     * @return  Returns the media.
     * @uml.property  name="media"
     */
    public Media getMedia()
    {
    	return media;
    }

		/** 
     * Setter of the property <tt>media</tt>
     * @param media  The media to set.
     * @uml.property  name="media"
     */
    public void setMedia(Media media)
    {
    	this.media = media;
    }

		/**
     * @uml.property  name="booker"
     * @uml.associationEnd  inverse="currentBookings:subscribersManagement.Subscriber"
     */
    private Subscriber booker;

		/**
     * Getter of the property <tt>booker</tt>
     * @return  Returns the booker.
     * @uml.property  name="booker"
     */
    public Subscriber getBooker()
    {
    	return booker;
    }

		/**
     * Setter of the property <tt>booker</tt>
     * @param booker  The booker to set.
     * @uml.property  name="booker"
     */
    public void setBooker(Subscriber booker)
    {
    	this.booker = booker;
    }

    
    /**
     * Tests if o is equal to this. Equality requires: same media, same booker, same bookingDate
     * @param o object to test
     */
    @Override
    public boolean equals(Object o)
    {
    	if (o == null)
  			return false;
  		Booking b = (Booking)o;

  		
  		if ((b.media== null) || (b.booker == null)
  				|| (b.bookingDate == null))
  			return false;
  		
  		if ((this.media== null) || (this.booker == null) 
  				|| (this.bookingDate == null))
  			return false;
  		
  		boolean res = (b.media.equals(this.media)) 
    		&& (b.bookingDate.equals(this.bookingDate))
    		&& (b.booker.equals(this.booker));
  		
  		return res;
    }

			
  @Override
 	public String toString()
  {
  	
  	String date;
  	if(accounted)
  		date = String.valueOf( this.cancelDate.get(Calendar.MONTH)+1 ) +"/" +
				String.valueOf( this.cancelDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
				String.valueOf( this.cancelDate.get(Calendar.YEAR) );
  	else date="null";
  	
  	return "Booker: ["+booker.toString()+"], \n\tMedia: ["+media.toString()+
  				"], \n\taccounted: "+accounted+", cancel date (english): " + date;	
 	}
  
  public String toHTML()
  {
  	String date;
  	if(accounted)
  		date = String.valueOf( this.cancelDate.get(Calendar.MONTH)+1 ) +"/" +
				String.valueOf( this.cancelDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
				String.valueOf( this.cancelDate.get(Calendar.YEAR) );
  	else date="null";
  	
  	return "<td>"+this.booker.getNumber()+"</td><td>"+this.media.getKey()+"</td><td>"+((this.accounted)?"Yes":"No")+"</td>" +
				"<td>"+date+"</td>";
 	}
  
  /**
   * @return a string giving the first line of the HTML table of the bookings
   */
  public static String getBookingHeader()
  {
  	return "<tr><td><strong>Number</strong></td><td><strong>Booker's number</strong></td><td><strong>Media's key</strong></td>" +
  			"<td><strong>Accounted</strong></td><td><strong>Cancel date</strong></td></tr>";
  }
  

 	public void setAccounted() throws BookingAccountedException
 	{
 		if(accounted==true)
 			throw new BookingAccountedException("Booking already accounted.");
 		
 		accounted=true;
 		
 		//Set the cancel date
 		Calendar cDate = new GregorianCalendar();
		cDate.add(Calendar.DAY_OF_YEAR, Constraints.bookingDELAY);
		cancelDate=cDate;
 	}

	/**
   * @uml.property  name="currentBookings"
   * @uml.associationEnd  inverse="booker:bookingsManagement.Booking"
   */
  private Subscriber currentBookings;

	/**
   * Getter of the property <tt>currentBookings</tt>
   * @return  Returns the currentBookings.
   * @uml.property  name="currentBookings"
   */
  public Subscriber getCurrentBookings()
  {
  	return currentBookings;
  }

	/**
   * Setter of the property <tt>currentBookings</tt>
   * @param currentBookings  The currentBookings to set.
   * @uml.property  name="currentBookings"
   */
  public void setCurrentBookings(Subscriber currentBookings)
  {
  	this.currentBookings = currentBookings;
  }

	/**
   * @uml.property  name="id"
   */
  private Long id;

	/**
   * Getter of the property <tt>id</tt>
   * @return  Returns the id.
   * @uml.property  name="id"
   */
  public Long getId()
  {
  	return id;
  }

	/**
   * Setter of the property <tt>id</tt>
   * @param id  The id to set.
   * @uml.property  name="id"
   */
  public void setId(Long id)
  {
  	this.id = id;
  }

		
  	/**
  	 */
  	public Booking(){
  	}

}
