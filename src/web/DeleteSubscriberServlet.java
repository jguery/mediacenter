package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.BadParametersException;
import exceptions.SubscriberExistsException;
import exceptions.SubscriberWithLoansException;

import main.Library;

/**
 * Servlet implementation class DeleteSubscriberServlet
 */
@WebServlet("/deleteSubscriber.do")
public class DeleteSubscriberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the values from the form deleteSubs.jsp
		String number = request.getParameter("number");
		
		//We need to check if the given number of exemplaries is correct
		int nNumber;
		try {
		  if((nNumber=Integer.parseInt(number))<0)
		  {
		   	this.getServletContext().setAttribute("badExecution", "The number of the subscriber must be higher than 0.");
		   	request.getRequestDispatcher("deleteSubs.jsp").forward(request, response);
		     return ;
		  }
		}
		catch (NumberFormatException e) {	
		 	this.getServletContext().setAttribute("badExecution", "You must provide a number in the field \"number of the subscriber\".");
		 	request.getRequestDispatcher("deleteSubs.jsp").forward(request, response);
		  e.printStackTrace();
		  return ;
		}
		
		//Get the library from the context and try to delete the subscriber
		Library lib = (Library)this.getServletContext().getAttribute("library");
		
		try{ lib.deleteSubscriber(nNumber); }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("deleteSubs.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (SubscriberWithLoansException e)
    {
    	this.getServletContext().setAttribute("badExecution", "The subscriber currently has loans.");
			request.getRequestDispatcher("deleteSubs.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (SubscriberExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "No subscriber with the given number was found in the library.");
			request.getRequestDispatcher("deleteSubs.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
		
		//Everything went right
		this.getServletContext().setAttribute("badExecution",null);
				
		//We redirect the client to the success page
		request.getRequestDispatcher("success.jsp").forward(request, response);
			
	}

}
