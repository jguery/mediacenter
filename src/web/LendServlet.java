package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.BadParametersException;
import exceptions.LentOrBookedMediaException;
import exceptions.MediaExistsException;
import exceptions.NoRightsException;
import exceptions.SubscriberExistsException;
import exceptions.TooManyLoansOrBookingsException;

import main.Library;

/**
 * Servlet implementation class LendServlet
 */
@WebServlet("/lend.do")
public class LendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the values from the form lend.jsp
		String key = request.getParameter("key"),
					 number = request.getParameter("number");
		
		//We need to check if the given number of exemplaries is correct
		int nNumber;
		try {
		   if((nNumber=Integer.parseInt(number))<0)
		   {
		   	this.getServletContext().setAttribute("badExecution", "The number of the subscriber must be higher than 0.");
		   	request.getRequestDispatcher("lend.jsp").forward(request, response);
		     return ;
		   }
	   }
	   catch (NumberFormatException e) {	
	   	this.getServletContext().setAttribute("badExecution", "You must provide a number in the field \"number of the subscriber\".");
	   	request.getRequestDispatcher("lend.jsp").forward(request, response);
		   e.printStackTrace();
		   return ;
	   }
		
		//Get the library from the context and try to lend the media
		Library lib = (Library)this.getServletContext().getAttribute("library");
		Integer ex = null;
		try{ ex=lib.lend(key, nNumber); }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("lend.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (MediaExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "No media with the given key was found in the library.");
			request.getRequestDispatcher("lend.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (LentOrBookedMediaException e)
    {
    	this.getServletContext().setAttribute("badExecution", "All the exemplaries of this media have been lent, or are currently booked.");
			request.getRequestDispatcher("lend.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (SubscriberExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "No subscriber with the given number was found in the library.");
			request.getRequestDispatcher("lend.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (TooManyLoansOrBookingsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "The subscriber has already lent or booked too many medias.");
			request.getRequestDispatcher("lend.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (NoRightsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "The subscriber does not have the right to lend.");
			request.getRequestDispatcher("lend.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
		
		//Everything went right
		this.getServletContext().setAttribute("badExecution",null);
		
		//We make a success-message be printed
		this.getServletContext().setAttribute("success", "The exemplary lent has the number: "+ex.toString()+".");
				
		//We redirect the client to the success page
		request.getRequestDispatcher("success.jsp").forward(request, response);
	}
}
