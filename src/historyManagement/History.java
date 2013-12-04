package historyManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import loansManagement.Loan;
import main.Constraints;
import mediasManagement.*;


public class History
{

	/**
   * @uml.property  name="subsFirstName"
   */
  private String subsFirstName;

	/** 
   * Getter of the property <tt>subsName</tt>
   * @return  Returns the subsName.
   * @uml.property  name="subsFirstName"
   */
  public String getSubsFirstName()
  {
  	return subsFirstName;
  }

	/** 
   * Setter of the property <tt>subsName</tt>
   * @param subsName  The subsName to set.
   * @uml.property  name="subsFirstName"
   */
  public void setSubsFirstName(String subsFirstName)
  {
  	this.subsFirstName = subsFirstName;
  }

	/**
   * @uml.property  name="subsLastName"
   */
  private String subsLastName;

	/**
   * Getter of the property <tt>subsLastName</tt>
   * @return  Returns the subsLastName.
   * @uml.property  name="subsLastName"
   */
  public String getSubsLastName()
  {
  	return subsLastName;
  }

	/**
   * Setter of the property <tt>subsLastName</tt>
   * @param subsLastName  The subsLastName to set.
   * @uml.property  name="subsLastName"
   */
  public void setSubsLastName(String subsLastName)
  {
  	this.subsLastName = subsLastName;
  }

	/**
   * @uml.property  name="subsBornDate"
   */
  private Calendar subsBornDate;

	/**
   * Getter of the property <tt>subsBornDate</tt>
   * @return  Returns the subsBornDate.
   * @uml.property  name="subsBornDate"
   */
  public Calendar getSubsBornDate()
  {
  	return subsBornDate;
  }

	/**
   * Setter of the property <tt>subsBornDate</tt>
   * @param subsBornDate  The subsBornDate to set.
   * @uml.property  name="subsBornDate"
   */
  public void setSubsBornDate(Calendar subsBornDate)
  {
  	this.subsBornDate = subsBornDate;
  }

	/**
   * @uml.property  name="subsInscriptionDate"
   */
  private Calendar subsInscriptionDate;

	/**
   * Getter of the property <tt>subsInscriptionDate</tt>
   * @return  Returns the subsInscriptionDate.
   * @uml.property  name="subsInscriptionDate"
   */
  public Calendar getSubsInscriptionDate()
  {
  	return subsInscriptionDate;
  }

	/**
   * Setter of the property <tt>subsInscriptionDate</tt>
   * @param subsInscriptionDate  The subsInscriptionDate to set.
   * @uml.property  name="subsInscriptionDate"
   */
  public void setSubsInscriptionDate(Calendar subsInscriptionDate)
  {
  	this.subsInscriptionDate = subsInscriptionDate;
  }

	/**
   * @uml.property  name="subsNumber"
   */
  private long subsNumber;

	/**
   * Getter of the property <tt>subsNumber</tt>
   * @return  Returns the subsNumber.
   * @uml.property  name="subsNumber"
   */
  public long getSubsNumber()
  {
  	return subsNumber;
  }

	/**
   * Setter of the property <tt>subsNumber</tt>
   * @param subsNumber  The subsNumber to set.
   * @uml.property  name="subsNumber"
   */
  public void setSubsNumber(long subsNumber)
  {
  	this.subsNumber = subsNumber;
  }

	/**
   * @uml.property  name="mediaTitle"
   */
  private String mediaTitle;

	/**
   * Getter of the property <tt>mediaTitle</tt>
   * @return  Returns the mediaTitle.
   * @uml.property  name="mediaTitle"
   */
  public String getMediaTitle()
  {
  	return mediaTitle;
  }

	/**
   * Setter of the property <tt>mediaTitle</tt>
   * @param mediaTitle  The mediaTitle to set.
   * @uml.property  name="mediaTitle"
   */
  public void setMediaTitle(String mediaTitle)
  {
  	this.mediaTitle = mediaTitle;
  }

	/**
   * @uml.property  name="mediaMakers"
   */
  private List<String> mediaMakers;

	/**
   * Getter of the property <tt>mediaMakers</tt>
   * @return  Returns the mediaMakers.
   * @uml.property  name="mediaMakers"
   */
  public List<String> getMediaMakers()
  {
  	return mediaMakers;
  }

	/**
   * Setter of the property <tt>mediaMakers</tt>
   * @param mediaMakers  The mediaMakers to set.
   * @uml.property  name="mediaMakers"
   */
  public void setMediaMakers(List<String> mediaMakers)
  {
  	this.mediaMakers = mediaMakers;
  }

	/**
   * @uml.property  name="mediaDate"
   */
  private Calendar mediaDate;

	/**
   * Getter of the property <tt>mediaDate</tt>
   * @return  Returns the mediaDate.
   * @uml.property  name="mediaDate"
   */
  public Calendar getMediaDate()
  {
  	return mediaDate;
  }

	/**
   * Setter of the property <tt>mediaDate</tt>
   * @param mediaDate  The mediaDate to set.
   * @uml.property  name="mediaDate"
   */
  public void setMediaDate(Calendar mediaDate)
  {
  	this.mediaDate = mediaDate;
  }

	/**
   * @uml.property  name="mediaKey"
   */
  private String mediaKey;

	/**
   * Getter of the property <tt>mediaKey</tt>
   * @return  Returns the mediaKey.
   * @uml.property  name="mediaKey"
   */
  public String getMediaKey()
  {
  	return mediaKey;
  }

	/**
   * Setter of the property <tt>mediaKey</tt>
   * @param mediaKey  The mediaKey to set.
   * @uml.property  name="mediaKey"
   */
  public void setMediaKey(String mediaKey)
  {
  	this.mediaKey = mediaKey;
  }

	/**
   * @uml.property  name="loanDate"
   */
  private Calendar loanDate;

	/**
   * Getter of the property <tt>loanDate</tt>
   * @return  Returns the loanDate.
   * @uml.property  name="loanDate"
   */
  public Calendar getLoanDate()
  {
  	return loanDate;
  }

	/**
   * Setter of the property <tt>loanDate</tt>
   * @param loanDate  The loanDate to set.
   * @uml.property  name="loanDate"
   */
  public void setLoanDate(Calendar loanDate)
  {
  	this.loanDate = loanDate;
  }

	/**
   * @uml.property  name="loanReturnDate"
   */
  private Calendar loanReturnDate;

	/**
   * Getter of the property <tt>loanReturnDate</tt>
   * @return  Returns the loanReturnDate.
   * @uml.property  name="loanReturnDate"
   */
  public Calendar getLoanReturnDate()
  {
  	return loanReturnDate;
  }

	/**
   * Setter of the property <tt>loanReturnDate</tt>
   * @param loanReturnDate  The loanReturnDate to set.
   * @uml.property  name="loanReturnDate"
   */
  public void setLoanReturnDate(Calendar loanReturnDate)
  {
  	this.loanReturnDate = loanReturnDate;
  }

		
  	/**
  	 */
  	public History(){
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
     * @uml.property  name="loan"
     */
    private Loan loan;

		/**
     * Getter of the property <tt>loan</tt>
     * @return  Returns the loan.
     * @uml.property  name="loan"
     */
    public Loan getLoan()
    {
    	return loan;
    }

		/**
     * Setter of the property <tt>loan</tt>
     * @param loan  The loan to set.
     * @uml.property  name="loan"
     */
    public void setLoan(Loan loan)
    {
    	this.loan = loan;
    }


    /**
     * Save a loan
     * @param loan the loan to put into the history
     * @param returnDate date of true return of the lent exemplary 
     */
    public History(Loan loan, Calendar returnDate)
    {
    	this.exemplary = loan.getExemplary();
    	
    	Calendar bDate = loan.getReturnDate();
    	bDate.add(Calendar.MONTH, -Constraints.loanDELAY);
    	this.loanDate = bDate;
    	
    	this.loanReturnDate = returnDate;
    	
    	//About the media
    	this.mediaTitle = loan.getMedia().getTitle();
    	this.mediaKey = loan.getMedia().getKey();
    	
    	switch(loan.getMedia().getType())
    	{
    		case DVD:
    			DVD dvd = (DVD)loan.getMedia();
    			this.mediaDate = dvd.getOutDate();
    			this.mediaMakers = new ArrayList<String>();
    			for(String s : dvd.getDirectors())
    				this.mediaMakers.add(s);
    			break;
    			
    		case BOOK:
    			Book book = (Book)loan.getMedia();
    			this.mediaDate = book.getEditionDate();
    			
    			this.mediaMakers = new ArrayList<String>();
    			for(String s : book.getAuthors())
    				this.mediaMakers.add(s);
    					
    			break;
    	}
    	
    	
    	//About the subscriber
    	this.subsBornDate = loan.getLender().getBornDate();
    	this.subsFirstName = loan.getLender().getFirstName();
    	this.subsLastName = loan.getLender().getLastName();
    	this.subsInscriptionDate = loan.getLender().getInscriptionDate();
    	this.subsNumber = loan.getLender().getNumber();
    }

		/**
     * @uml.property  name="id"
     */
    private long id;

		/**
     * Getter of the property <tt>id</tt>
     * @return  Returns the id.
     * @uml.property  name="id"
     */
    public long getId()
    {
	    return id;
    }

		/**
     * Setter of the property <tt>id</tt>
     * @param id  The id to set.
     * @uml.property  name="id"
     */
    public void setId(long id)
    {
	    this.id = id;
    }
    
    
    public String toString(){
			String loanReturndate = String.valueOf( this.loanReturnDate.get(Calendar.MONTH)+1 ) +"/" +
					String.valueOf( this.loanReturnDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
					String.valueOf( this.loanReturnDate.get(Calendar.YEAR) ),
						loanDate = String.valueOf( this.loanDate.get(Calendar.MONTH)+1 ) +"/" +
								String.valueOf( this.loanDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
								String.valueOf( this.loanDate.get(Calendar.YEAR) ),
						mediaDate = String.valueOf( this.mediaDate.get(Calendar.MONTH)+1 ) +"/" +
								String.valueOf( this.mediaDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
								String.valueOf( this.mediaDate.get(Calendar.YEAR) ),
						subsBornDate = String.valueOf( this.subsBornDate.get(Calendar.MONTH)+1 ) +"/" +
								String.valueOf( this.subsBornDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
								String.valueOf( this.subsBornDate.get(Calendar.YEAR) ),
						subsInscriptionDate = String.valueOf( this.subsInscriptionDate.get(Calendar.MONTH)+1 ) +"/" +
								String.valueOf( this.subsInscriptionDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
								String.valueOf( this.subsInscriptionDate.get(Calendar.YEAR) );
						 
			
			
			return "Lender: ["+"Firstname: "+subsFirstName+", lastname: "+subsLastName+", date of birth (english): "+subsBornDate+
					", inscription date: "+subsInscriptionDate+", number: "+subsNumber+"], " +
					"\n\tMedia: ["+"Title: \""+mediaTitle+"\", key: "+ mediaKey +", authors/directors: "+mediaMakers+", Edition date/out date: "+mediaDate+"], " +
							"\n\tn° of exemplary:"
										+exemplary.toString() + ", date of loan: "+loanDate+", Return date (english): " + loanReturndate;	

	}
    
  
  public static String getHistoryHeader()
  {
  	return "<tr><td><strong>Number</strong></td><td><strong>Lender's first name</strong></td>" +
  			"<td><strong>Lender's last name</strong></td><td><strong>Media's key</strong></td>" +
  			"<td><strong>Media's title</strong></td><td><strong>N° of exemplary</strong>" +
  			"</td><td><strong>Lending day</strong></td><td><strong>Return date</strong></td></tr>";
  }
  
  public String toHTML()
  {
  	String loanReturndate = String.valueOf( this.loanReturnDate.get(Calendar.MONTH)+1 ) +"/" +
				String.valueOf( this.loanReturnDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
				String.valueOf( this.loanReturnDate.get(Calendar.YEAR) ),
					loanDate = String.valueOf( this.loanDate.get(Calendar.MONTH)+1 ) +"/" +
							String.valueOf( this.loanDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
							String.valueOf( this.loanDate.get(Calendar.YEAR) );
  	
  	return "<td>"+this.subsFirstName+"</td><td>"+this.subsLastName+"</td><td>"+this.mediaKey+"</td>" +
			"<td>"+this.mediaTitle+"</td><td>"+this.exemplary+"</td><td>"+loanDate+"</td><td>"+loanReturndate+"</td>";
  }
}
