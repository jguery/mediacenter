package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.BadParametersException;
import exceptions.BookingAccountedException;
import exceptions.LentOrBookedMediaException;
import exceptions.MediaExistsException;
import exceptions.NotAnExemplaryException;

import main.Library;

/**
 * Servlet implementation class ReturnExemplaryServlet
 */
@WebServlet("/return.do")
public class ReturnExemplaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the values from the form returnExemplary.jsp
		String key = request.getParameter("key"),
					 number = request.getParameter("number");
		
		//We need to check if the given number of exemplary is correct
		int nNumber;
		try {
		   if((nNumber=Integer.parseInt(number))<0)
		   {
		   	this.getServletContext().setAttribute("badExecution", "The number of the exemplary must be higher than 0.");
		   	request.getRequestDispatcher("returnExemplary.jsp").forward(request, response);
		     return ;
		   }
		}
		catch (NumberFormatException e) {	
		 	this.getServletContext().setAttribute("badExecution", "You must provide a number in the field \"Number of the exemplary to return\".");
		 	request.getRequestDispatcher("returnExemplary.jsp").forward(request, response);
		  e.printStackTrace();
		  return ;
		}
		
		//Get the library from the context and try to return the exemplary of the given media
		Library lib = (Library)this.getServletContext().getAttribute("library");
		try{ lib.returnMedia(key, nNumber); }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("returnExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (MediaExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "No media with the given key was found in the library.");
			request.getRequestDispatcher("returnExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (LentOrBookedMediaException e)
    {
    	this.getServletContext().setAttribute("badExecution", "The given exemplary is not lent.");
			request.getRequestDispatcher("returnExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (NotAnExemplaryException e)
    {
    	this.getServletContext().setAttribute("badExecution", "The number of exemplary you provided does not refer to an exemplary of the media.");
			request.getRequestDispatcher("returnExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (BookingAccountedException e)
    {
    	this.getServletContext().setAttribute("badExecution", "Not possible.");
			request.getRequestDispatcher("returnExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
		
		//Everything went right
		this.getServletContext().setAttribute("badExecution",null);
		
		//We redirect the client to the success page
		request.getRequestDispatcher("success.jsp").forward(request, response);
	}
}
