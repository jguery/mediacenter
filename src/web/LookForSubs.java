package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.BadParametersException;
import exceptions.SubscriberExistsException;

import main.Library;
import subscribersManagement.Subscriber;

/**
 * Servlet implementation class LookForSubs
 */
@WebServlet("/lookForSubs.do")
public class LookForSubs extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the values from the form lookForSubs.jsp
		String lastname = request.getParameter("lastname");
				
		//Get the library from the context
		Library lib = (Library)this.getServletContext().getAttribute("library");
		
		List<Subscriber> li = null;
		try { li = lib.lookForSubscriberLastname(lastname); }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("lookForSubs.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (SubscriberExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "No subscriber with the given last name was found in the library.");
			request.getRequestDispatcher("lookForSubs.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
		
		//Go to the lookForSubs.jsp page and print the result
		this.getServletContext().setAttribute("success", Subscriber.toHTML(li));
		request.getRequestDispatcher("lookForSubs.jsp").forward(request, response);
	}

}
