package mediasManagement;

import exceptions.*;

import historyManagement.History;

import java.util.*;

import subscribersManagement.Subscriber;
import loansManagement.Loan;


public abstract class Media
{

	/**
   * @uml.property  name="exempTotal"
   */
  private int exempTotal;

	/**
   * Getter of the property <tt>exempTotal</tt>
   * @return  Returns the exempTotal.
   * @uml.property  name="exempTotal"
   */
  public int getExempTotal()
  {
  	return exempTotal;
  }

	/**
   * Setter of the property <tt>exempTotal</tt>
   * @param exempTotal  The exempTotal to set.
   * @uml.property  name="exempTotal"
   */
  public void setExempTotal(int exempTotal)
  {
  	this.exempTotal = exempTotal;
  }

	/**
   * @uml.property  name="exempDispo"
   */
  private int exempDispo;

	/**
   * Getter of the property <tt>exempDispo</tt>
   * @return  Returns the exempDispo.
   * @uml.property  name="exempDispo"
   */
  public int getExempDispo()
  {
  	return exempDispo;
  }

	/**
   * Setter of the property <tt>exempDispo</tt>
   * @param exempDispo  The exempDispo to set.
   * @uml.property  name="exempDispo"
   */
  public void setExempDispo(int exempDispo)
  {
  	this.exempDispo = exempDispo;
  }
	
  /**
   * Two medias are equals if their keys are the same
   * @return true if equals, false otherwise
   */
  public boolean equals(Object m)
  {
  	if(m==null)
  		return false;
  	Media me = (Media)m;
  	
  	if(me.getKey().equals(this.key))
  		return true;
  	else return false;	
  }

				
  public abstract boolean isBook();
	public abstract boolean isDVD();


		
		
  	/** 
     * Return a lended exemplary of the media
     * @param exemplary exemplary of the media to be returned
     * @throws LentOrBookedMediaException
     * @throws BadParametersException
     */
  	public History returnExempMedia(int exemplary)	throws LentOrBookedMediaException, BadParametersException,
  		NotAnExemplaryException
  	{
  		//Get the loan associated to the exemplary
  		Loan loan = tabExemplaries.get(Integer.valueOf(exemplary));
  		
  		if(loan==null)
  			throw new NotAnExemplaryException();
  		
  		//Check wether this loan exists indeed
  		if(loan.isVoidLoan())
  			throw new LentOrBookedMediaException("The exemplary is not lent.");
  		
  		History history = loan.returnMedia();
  		this.exempDispo++;			//Increase the number of exemplaries available
  		
  		//Return the loan (contained in the history) that we just modified
  		return history;
  	}

	
  		/** 
       * Lend one exemplary of the media
       * @param l loan to add to the list of loans of the media
       * @throws LentOrBookedMediaException
       * @return the number of the exemplary lent
       */
    	public Loan lend(Subscriber lender, Media media, Calendar rDate) 
    	{
    	 	//Reduce the number of exemplaries available
    	 	exempDispo--;
    	 	
    	 	//Choose an available exemplary to be lent
    	 	Iterator<Integer> it = tabExemplaries.keySet().iterator();
    	 	Loan loan = null;
    	 	while(it.hasNext())
    	 	{
    	 		Integer ret = it.next();
    	 		loan = tabExemplaries.get(ret);
    	 		if(loan.isVoidLoan())		//Modify the loan associated to an available exemplary
    	 		{
    	 			loan.setVoidLoan(false);
    	 			loan.setExemplary(ret);
    	 			loan.setLender(lender);
    	 			loan.setMedia(media);
    	 			loan.setReturnDate(rDate);
    	 			break;
    	 		}
    	 	}

    	 	return loan;
    	}

	/**
	 * Creates a new media
	 * @param title title of the media
	 * @param key key common to all exemplaries of the media
	 * @param exemplaries number of exemplaries of the media
	 */
	protected Media(String title, String key, int exemplaries) throws BadParametersException {
		
		if(key==null || title==null || exemplaries<0)
			throw new BadParametersException();
		
		this.key=key;
		this.title=title;
		this.exempTotal=exemplaries;
		this.exempDispo=exemplaries;
		this.bookedExemplaries=0;
		
		//Instantiate the hash table of availability of the exemplaries
		this.tabExemplaries = new Hashtable<Integer, Loan>();
		for(int i=1; i<=exemplaries; i++)
		{
			tabExemplaries.put(Integer.valueOf(i), new Loan(true));
		}
		this.currentMaxExemplary = exemplaries;
  }

	/** 
   * @uml.property name="key"
   */
	private String key;

	/** 
   * Getter of the property <tt>cle</tt>
   * @return  Returns the cle.
   * @uml.property  name="key"
   */
	public String getKey()
	{
  	return key;
  }

	/**
	* @uml.property  name="title"
	*/
	private String title;

	/** 
	* Getter of the property <tt>titre</tt>
	* @return  Returns the titre.
	* @uml.property  name="title"
	*/
	public String getTitle()
	{
  	return title;
  }

	/** 
	* Setter of the property <tt>titre</tt>
	* @param titre  The titre to set.
	* @uml.property  name="title"
	*/
	public void setTitle(String title)
	{
  	this.title = title;
  }

	/**
	 * Table describing all the exemplaries and pointing to their loans, if lent
	 * or to a void loan otherwise
   * @uml.property  name="tabExemplaries"
   */
  private Map<Integer, Loan> tabExemplaries;

	/** 
   * Getter of the property <tt>tabExemplaries</tt>
   * @return  Returns the tabExemplaries.
   * @uml.property  name="tabExemplaries"
   */
  public Map<Integer, Loan> getTabExemplaries()
  {
  	return tabExemplaries;
  }

	/** 
   * Setter of the property <tt>tabExemplaries</tt>
   * @param tabExemplaries  The tabExemplaries to set.
   * @uml.property  name="tabExemplaries"
   */
  public void setTabExemplaries(Map<Integer, Loan> tabExemplaries)
  {
  	this.tabExemplaries = tabExemplaries;
  }

  /**
   * Add toAdd exemplaries of the media
   * @param toAdd number of exemplaries to add
   * @throws BadParametersException
   */
	public void addExemplary(int toAdd)	throws BadParametersException 
	{
		if(toAdd<0)
			throw new BadParametersException();
		
		//Add the given amount of exemplaries
		for(int i=currentMaxExemplary+1; i<=currentMaxExemplary+toAdd; i++){
			tabExemplaries.put(Integer.valueOf(i), new Loan(true));
		}
		currentMaxExemplary = currentMaxExemplary+toAdd;
		exempDispo += toAdd;
		exempTotal += toAdd;
	}

	/**
	 * Delete one of the available exemplaries 
	 * @throws LentOrBookedMediaException
	 * @throws NotEnoughExemplariesException 
	 */
	public Integer delExemplary() throws LentOrBookedMediaException, NotEnoughExemplariesException
	{
		if(exempTotal<=1)
			throw new NotEnoughExemplariesException();
		if(exempDispo<=0)
			throw new LentOrBookedMediaException("All exemplaries currently lent.");
		if(exempDispo-bookedExemplaries<=0)
			throw new LentOrBookedMediaException("All exemplaries currently booked.");
		
		//Delete an available exemplary (and their must be one!)
		Iterator<Integer> it = tabExemplaries.keySet().iterator();
	 	Integer ret = null;
	 	while(it.hasNext())
	 	{
	 		ret = it.next();
	 		if(tabExemplaries.get(ret).isVoidLoan()) {
	 			tabExemplaries.remove(ret);
	 			break;
	 		}
	 	}
	 	
	 	if(ret==null)
	 		throw new LentOrBookedMediaException("All exemplaries currently lent. Default in the variables.");
	 	
	 	//Signal that an exemplary has been deleted
	 	exempDispo--;
	 	exempTotal--;
	 	
	 	return ret;
	}
			
	/**
	 * Check if one of the exemplaries of the media is currently lent
	 */
	public boolean isLent()
	{
		if(exempTotal==exempDispo)
			return false;
		else return true;	
	}

	/** 
   * Gives the number of the exemplary which has the maximum number
   * @uml.property name="currentMaxExemplary"
   */
  private int currentMaxExemplary;
	/** 
   * @uml.property name="type"
   */
  protected TypeMedia type;

	/** 
   * Getter of the property <tt>type</tt>
   * @return  Returns the type.
   * @uml.property  name="type"
   */
  public TypeMedia getType()
  {
  	return type;
  }

	/**
   * Getter of the property <tt>currentMaxExemplary</tt>
   * @return  Returns the currentMaxExemplary.
   * @uml.property  name="currentMaxExemplary"
   */
  public int getCurrentMaxExemplary()
  {
  	return currentMaxExemplary;
  }

	/**
   * Setter of the property <tt>currentMaxExemplary</tt>
   * @param currentMaxExemplary  The currentMaxExemplary to set.
   * @uml.property  name="currentMaxExemplary"
   */
  public void setCurrentMaxExemplary(int currentMaxExemplary)
  {
  	this.currentMaxExemplary = currentMaxExemplary;
  }
  
  @Override
  public String toString()
  {
  	return "Type: "+type.label()+", Title: \""+title+"\", key: "+ key +", n° of exemplaries: "+exempTotal;
  }

  public abstract String toHtml();
		
	/**
	 * Check if at least one exemplary of the media is available for a loan
	 */
	public boolean isAvailableForLoan()
	{
		if(this.exempDispo-this.bookedExemplaries>0)
			return true;
		else return false;
	}

	/**
   * Gives the amount of booked exemplaries, among those available
   * @uml.property  name="bookedExemplaries"
   */
  private int bookedExemplaries;

	/**
   * Getter of the property <tt>bookedExemplaries</tt>
   * @return  Returns the bookedExemplaries.
   * @uml.property  name="bookedExemplaries"
   */
  public int getBookedExemplaries()
  {
  	return bookedExemplaries;
  }

	/**
   * Setter of the property <tt>bookedExemplaries</tt>
   * @param bookedExemplaries  The bookedExemplaries to set.
   * @uml.property  name="bookedExemplaries"
   */
  public void setBookedExemplaries(int bookedExemplaries)
  {
  	this.bookedExemplaries = bookedExemplaries;
  }
  
  
  	/**
  	 */
  	protected Media(){
  	}

		/**
     * Setter of the property <tt>key</tt>
     * @param key  The key to set.
     * @uml.property  name="key"
     */
    public void setKey(String key)
    {
    	this.key = key;
    }

		/**
     * Setter of the property <tt>type</tt>
     * @param type  The type to set.
     * @uml.property  name="type"
     */
    public void setType(TypeMedia type)
    {
	    this.type = type;
    }
    
    /**
     * @return a string giving the first line of the HTML table of the medias
     */
    public static String getMediaHeader()
    {
    	return "<tr><td><strong>Type</strong></td><td><strong>Title</strong></td><td><strong>Authors/Directors</strong></td>" +
    			"<td><strong>Edition date/out date</strong></td><td><strong>Key</strong></td>" +
    			"<td><strong>N° of exemplaries</strong></td><td><strong>Exemplaries available</strong></td></tr>";
    }
    
    /**
     * Print a list of medias in the form of a HTML table
     * @param li list of medias
     * @return a HTML table
     */
    public static String toHTML(List<Media> li)
    {
    	StringBuilder sb = new StringBuilder();
  		
  		sb.append("<table border=\"1\" >");
  		sb.append(Media.getMediaHeader());
  		for(Media m : li)
  			sb.append(m.toHtml());
  		sb.append("</table>");
  		return sb.toString();
    }
	
}
