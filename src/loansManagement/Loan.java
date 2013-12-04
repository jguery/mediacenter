/*
 * Class representing a loan of a media by a subscriber
 * @author M.T. Segarra
 * @version 0.0.1
 */
package loansManagement;


import exceptions.BadParametersException;
import exceptions.LentOrBookedMediaException;
import exceptions.NoRightsException;
import exceptions.TooManyLoansOrBookingsException;

import historyManagement.History;

import java.util.*;

import main.Constraints;
import mediasManagement.Media;
import subscribersManagement.Subscriber;

public class Loan {

	/**
	 * @uml.property  name="returnDate"
	 */
	private Calendar returnDate;

	/**
	 * Getter of the property <tt>returnDate</tt>
	 * @return  Returns the returnDate.
	 * @uml.property  name="returnDate"
	 */
	public Calendar getReturnDate() {
  	return returnDate;
  }

	/**
	 * Setter of the property <tt>returnDate</tt>
	 * @param returnDate  The returnDate to set.
	 * @uml.property  name="returnDate"
	 */
	public void setReturnDate(Calendar returnDate) {
  	this.returnDate = returnDate;
  }

	
	/** 
	 * @uml.property name="lender"
	 * @uml.associationEnd inverse="currentLoans:subscribersManagement.Subscriber"
	 */
	private Subscriber lender;


	/**
   * Getter of the property <tt>lender</tt>
   * @return  Returns the lender.
   * @uml.property  name="lender"
   */
	public Subscriber getLender() {
  	return lender;
  }

	/**
   * Setter of the property <tt>lender</tt>
   * @param lender  The lender to set.
   * @uml.property  name="lender"
   */
	public void setLender(Subscriber lender) {
  	this.lender = lender;
  }
	
	/**
	 * Two loans are equals is they concern the same media
	 * the same lender and return date
	 * @return true if the current loan is the same as the parameter
	 */
	@Override
	public boolean equals(Object loan){
		if (loan == null)
			return false;
		Loan l = (Loan)loan;
		
		//first of all, if the two loans are void, they are equal
		if(l.isVoidLoan() && voidLoan)
			return true;
		if((l.isVoidLoan() && !voidLoan) || (!l.isVoidLoan() && voidLoan))
			return false;
		
		//System.out.println("the loan " + loan);
		boolean res = this.media.equals(l.media) 
			&& this.lender.equals(l.lender) 
			&& (this.returnDate.equals(l.returnDate));
		return res;	
	}

		
		
  	/** 
     * Called by the lent media
     * @throws BadParametersException 
     * @throws LentOrBookedMediaException
     */
  	public History returnMedia()	throws BadParametersException, LentOrBookedMediaException 
  	{
  			// Ask the lender to return the media
				lender.returnMedia(this);
  		
				// Save the loan in the history
				History history = new History(this, new GregorianCalendar());
				
  			//Clear the loan
  			this.voidLoan = true;
  			this.lender = null;
  			this.media = null;
  			this.returnDate = null;
  			
  			history.setLoan(this);
  			
  			return history;
    }


	/** 
   * @uml.property name="media"
   * @uml.associationEnd inverse="loans:mediasManagement.Media"
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
   * @uml.property  name="exemplary"
   */
  private Integer exemplary;

	/**
   * Getter of the property <tt>exemplary</tt>
   * @return  Returns the exemplary.
   * @uml.property  name="exemplary"
   */
  public Integer getExemplary()
  {
  	return exemplary;
  }

	/**
   * Setter of the property <tt>exemplary</tt>
   * @param exemplary  The exemplary to set.
   * @uml.property  name="exemplary"
   */
  public void setExemplary(Integer exemplary)
  {
  	this.exemplary = exemplary;
  }

		
  /**
   * Constructs a "default" loan which is void.
   * Loans are to be compared to a loan instantiated with this constructor
   * to know that it is a void loan.
   */
	public Loan(boolean b)
	{
		this.voidLoan = true;
	}


	/** 
   * true if the loan is a void one
   * @uml.property name="voidLoan"
   */
  private boolean voidLoan;

	/** 
   * Getter of the property <tt>voidLoan</tt>
   * @return  Returns the voidLoan.
   * @uml.property  name="voidLoan"
   */
  public boolean isVoidLoan()
  {
  	return voidLoan;
  }

		
	public String toString(){
		if(voidLoan)
			return "Loan is void";
		else {
			String date = String.valueOf( this.returnDate.get(Calendar.MONTH)+1 ) +"/" +
										String.valueOf( this.returnDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
										String.valueOf( this.returnDate.get(Calendar.YEAR) );
			return "Lender: ["+lender.toString()+"], \n\tMedia: ["+media.toString()+"], \n\tn° of exemplary:"
										+exemplary.toString() + ", Return date (english): " + date;	
		}
	}

	
	/**
   * @return a string giving the first line of the HTML table of the loans
   */
  public static String getLoanHeader()
  {
  	return "<tr><td><strong>Number</strong></td><td><strong>Lender's number</strong></td><td><strong>Media's key</strong></td>" +
  			"<td><strong>N° of exemplary</strong></td><td><strong>Return date</strong></td></tr>";
  }
  
  
  public String toHTML(){
		if(voidLoan)
			return "Loan is void";
		else {
			String date = String.valueOf( this.returnDate.get(Calendar.MONTH)+1 ) +"/" +
										String.valueOf( this.returnDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
										String.valueOf( this.returnDate.get(Calendar.YEAR) );
			return "<td>"+this.lender.getNumber()+"</td><td>"+this.media.getKey()+"</td><td>"+this.exemplary.toString()+"</td>" +
				"<td>"+date+"</td>";
		}
	}
		
	/**
	 * Void constructor for hibernate initiation
	 */
  public Loan()
  {
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
   * Setter of the property <tt>voidLoan</tt>
   * @param voidLoan  The voidLoan to set.
   * @uml.property  name="voidLoan"
   */
  public void setVoidLoan(boolean voidLoan)
  {
  	this.voidLoan = voidLoan;
  }

		
  		
  /**
   * Create a loan between a lender and a media. Update the void loan existing
   * @param lender the lender of the media
   * @param media the media to be lent
   * @return the loan created during the process
   * @throws BadParametersException
   * @throws LentOrBookedMediaException
   * @throws TooManyLoansOrBookingsException
   * @throws NoRightsException
   */
	public static Loan createLoan(Subscriber lender, Media media) throws BadParametersException, 
		LentOrBookedMediaException, TooManyLoansOrBookingsException, NoRightsException
	{
		if ((media == null) || (lender == null))
			throw new BadParametersException();
		
		if(!media.isAvailableForLoan())
			throw new LentOrBookedMediaException("All exemplaries lent or booked.");

		if(!lender.hasRights())
			throw new NoRightsException();
	
		if(!lender.canLendOrBook(media.getType()))
			throw new TooManyLoansOrBookingsException();
		
		Calendar rDate = new GregorianCalendar();
		rDate.add(Calendar.MONTH, Constraints.loanDELAY);
		
		// Modify the loan of an available exemplary
		Loan loan = media.lend(lender, media, rDate);
		
		if(loan == null)
			throw new BadParametersException();
		
		// Update the subscriber if he can lend
		lender.lend(loan);
		
		return loan;
	}

}
