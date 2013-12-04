package mediasManagement;

import java.util.*;

import exceptions.BadParametersException;



public class DVD extends Media
{

	@Override
	public boolean isBook()
	{
		return false;
	}

	@Override
	public boolean isDVD()
	{
		return true;
	}

	/**
   * @uml.property  name="directors"
   */
  private List<String> directors;

	/**
   * Getter of the property <tt>directors</tt>
   * @return  Returns the directors.
   * @uml.property  name="directors"
   */
  public List<String> getDirectors()
  {
  	return directors;
  }

	/**
   * Setter of the property <tt>directors</tt>
   * @param directors  The directors to set.
   * @uml.property  name="directors"
   */
  public void setDirectors(List<String> directors)
  {
  	this.directors = directors;
  }

	/**
   * @uml.property  name="outDate"
   */
  private Calendar outDate;

	/**
   * Getter of the property <tt>outDate</tt>
   * @return  Returns the outDate.
   * @uml.property  name="outDate"
   */
  public Calendar getOutDate()
  {
	  return outDate;
  }

	/**
   * Setter of the property <tt>outDate</tt>
   * @param outDate  The outDate to set.
   * @uml.property  name="outDate"
   */
  public void setOutDate(Calendar outDate)
  {
	  this.outDate = outDate;
  }

		
  /**
   * Creates a new DVD
   * @param title
   * @param directors
   * @param outDate
   * @param exemplaries
   * @param exp n° of exploitation
   * @throws BadParametersException
   */
	public DVD(String title, ArrayList<String> directors, Calendar outDate, int exemplaries, String exp)
			throws BadParametersException 
	{
		//Calls the constructor of media
		super(title,exp,exemplaries);
		
		this.type = TypeMedia.DVD;
		this.directors=directors;
		this.outDate=outDate;
	}

			
	@Override
	public String toString()
	{
		String date = String.valueOf( this.outDate.get(Calendar.MONTH)+1 ) +"/" +
				String.valueOf( this.outDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
				String.valueOf( this.outDate.get(Calendar.YEAR) );	
		return super.toString() + ", Directors: " + this.directors + ", out date (english): " + date;	
	}

	@Override
  public String toHtml()
  {
		String date = String.valueOf( this.outDate.get(Calendar.MONTH)+1 ) +"/" +
				String.valueOf( this.outDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
				String.valueOf( this.outDate.get(Calendar.YEAR) );	
		return "<tr><td>"+this.type.label()+"</td><td>"+this.getTitle()+"</td><td>"+this.directors.toString()+"</td>" +
		"<td>"+date+"</td><td>"+getKey()+"</td><td>"+this.getExempTotal()+"</td><td>"+this.getExempDispo()+"</td></tr>";
  }
		
  	/**
  	 */
  	public DVD(){
  	}

}
