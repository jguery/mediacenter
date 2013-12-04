package mediasManagement;

import java.util.*;

import exceptions.BadParametersException;



public class Book extends Media
{

	@Override
	public boolean isBook()
	{
		return true;
	}

	@Override
	public boolean isDVD()
	{
		return false;
	}

	/**
   * @uml.property  name="authors"
   */
  private List<String> authors;

	/**
   * Getter of the property <tt>authors</tt>
   * @return  Returns the authors.
   * @uml.property  name="authors"
   */
  public List<String> getAuthors()
  {
  	return authors;
  }

	/**
   * Setter of the property <tt>authors</tt>
   * @param authors  The authors to set.
   * @uml.property  name="authors"
   */
  public void setAuthors(List<String> authors)
  {
  	this.authors = authors;
  }

	/**
   * @uml.property  name="editionDate"
   */
  private Calendar editionDate;

	/**
   * Getter of the property <tt>editionDate</tt>
   * @return  Returns the editionDate.
   * @uml.property  name="editionDate"
   */
  public Calendar getEditionDate()
  {
	  return editionDate;
  }

	/**
   * Setter of the property <tt>editionDate</tt>
   * @param editionDate  The editionDate to set.
   * @uml.property  name="editionDate"
   */
  public void setEditionDate(Calendar editionDate)
  {
	  this.editionDate = editionDate;
  }

		
  /**
   * Creates a new book
   * @param title
   * @param authors
   * @param editionDate
   * @param exemplaries
   * @param isbn
   * @throws BadParametersException
   */
	public Book(String title, ArrayList<String> authors, Calendar editionDate, int exemplaries, String isbn)
			throws BadParametersException 
	{
		//Calls the constructor of Media
		super(title,isbn,exemplaries);
		
		this.type = TypeMedia.BOOK;
		this.authors=authors;
		this.editionDate=editionDate;
	}

		
	@Override
	public String toString()
	{
		String date = String.valueOf( this.editionDate.get(Calendar.MONTH)+1 ) +"/" +
				String.valueOf( this.editionDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
				String.valueOf( this.editionDate.get(Calendar.YEAR) );	
		return super.toString() + ", Authors: " + this.authors + ", edition date (english): " + date;	
	}
		
  	/**
  	 */
  	public Book(){
  	}

		@Override
    public String toHtml()
    {
			String date = String.valueOf( this.editionDate.get(Calendar.MONTH)+1 ) +"/" +
					String.valueOf( this.editionDate.get(Calendar.DAY_OF_MONTH) ) + "/" +
					String.valueOf( this.editionDate.get(Calendar.YEAR) );	
			return "<tr><td>"+this.type.label()+"</td><td>"+this.getTitle()+"</td><td>"+this.authors.toString()+"</td>" +
				"<td>"+date+"</td><td>"+getKey()+"</td><td>"+this.getExempTotal()+"</td><td>"+this.getExempDispo()+"</td></tr>";
    }

}
