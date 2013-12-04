package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.BadParametersException;
import exceptions.MediaExistsException;

import main.Library;
import mediasManagement.Book;
import mediasManagement.DVD;
import mediasManagement.Media;

/**
 * Servlet implementation class AddMediaServlet
 */
@WebServlet("/addMedia.do")
public class AddMediaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the parameters from the form of addMedia.jsp
		String makers = request.getParameter("makers"),
    				title = request.getParameter("title"),
            type = request.getParameter("type"),
            date = request.getParameter("date"),
            key = request.getParameter("key"),
            exemp = request.getParameter("exemp");
		
		//Several verifications have to be made
		//We have to check if the type matches the format mm/dd/yyyy,
		//We also need to check if the given number of exemplaries is correct
		int nExemp;
		try {
	    if((nExemp=Integer.parseInt(exemp))<=0)
	    {
	    	this.getServletContext().setAttribute("badExecution", "The number of exemplaries of the media must be strictly higher than 0.");
	    	request.getRequestDispatcher("addMedia.jsp").forward(request, response);
	      return ;
	    }
    }
    catch (NumberFormatException e) {	
    	this.getServletContext().setAttribute("badExecution", "You must provide a number in the field \"number of exemplaries\".");
    	request.getRequestDispatcher("addMedia.jsp").forward(request, response);
	    e.printStackTrace();
	    return ;
    }
		
		Pattern p = Pattern.compile("(\\d\\d)/(\\d\\d)/(\\d\\d\\d\\d)");
		Matcher m = p.matcher(date);
		if(!m.matches())
		{
			this.getServletContext().setAttribute("badExecution", "The date you provided was not in the good format.");
			request.getRequestDispatcher("addMedia.jsp").forward(request, response);
			return ;
		}
			
		
		//We know have to create the list of makers
		ArrayList<String> alMakers = new ArrayList<String>();
		for(String s: makers.split(","))
			alMakers.add(s);
				
		Media media = null;
		//Try to create a media, using the values of the form addMedia.jsp
		try
		{
		  if(type.equals("DVD"))
		  	media = new DVD(title, alMakers, new GregorianCalendar(Integer.parseInt(m.group(3)),
			    		Integer.parseInt(m.group(1))-1,Integer.parseInt(m.group(2))), nExemp, key);
		  else if(type.equals("Book"))
		  	media = new Book(title, alMakers, new GregorianCalendar(Integer.parseInt(m.group(3)),
		    		Integer.parseInt(m.group(1))-1,Integer.parseInt(m.group(2))), nExemp, key);
		}
		catch (Exception e)	//If an error occured, it is treated here.
		{
			this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("addMedia.jsp").forward(request, response);
			e.printStackTrace();
			return ;
		}
		
		//Get the library from the context and try to add the media to it
		Library lib = (Library)this.getServletContext().getAttribute("library");
		try{ lib.addMedia(media); }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("addMedia.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (MediaExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "The media you want to add to the library already exists.");
    	request.getRequestDispatcher("addMedia.jsp").forward(request, response);
	    e.printStackTrace();
	    return ;
    }
		
		//Everything went right
		this.getServletContext().setAttribute("badExecution",null);
			
		//We redirect the client to the success page
		request.getRequestDispatcher("success.jsp").forward(request, response);
		
	}

}
