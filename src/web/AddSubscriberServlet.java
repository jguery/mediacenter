package web;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.BadParametersException;
import exceptions.SubscriberExistsException;

import subscribersManagement.Subscriber;

import main.Library;

/**
 * Servlet implementation class AddSubscriberServlet
 */
@WebServlet("/addSubs.do")
public class AddSubscriberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the values from the form addSubscriber.jsp
		String firstname = request.getParameter("firstname"),
					 lastname = request.getParameter("lastname"),
					 borndate = request.getParameter("borndate");
		
		Library lib = (Library)this.getServletContext().getAttribute("library");
		
		//Check if the borndate given matches the regular expression mm/dd/yyyy
		Pattern p = Pattern.compile("(\\d\\d)/(\\d\\d)/(\\d\\d\\d\\d)");
		Matcher m = p.matcher(borndate);
		if(!m.matches())
		{
			this.getServletContext().setAttribute("badExecution", "The date you provided was not in the good format.");
			request.getRequestDispatcher("addSubscriber.jsp").forward(request, response);
			return ;
		}
		
		
		Subscriber s = null;
		//Try to create a subscriber, using the values of the form addSubscriber.jsp
		try
    {
	    s = new Subscriber(firstname, lastname, new GregorianCalendar(Integer.parseInt(m.group(3)),
	    		Integer.parseInt(m.group(1))-1,Integer.parseInt(m.group(2))), new GregorianCalendar());
    }
    catch (Exception e)	//If an error occured, it is treated here.
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
    	request.getRequestDispatcher("addSubscriber.jsp").forward(request, response);
	    e.printStackTrace();
	    return ;
    }
		
		
		//Try to add the subscriber to the library, and treat errors if any
		try{ lib.addSubscriber(s);  }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
    	request.getRequestDispatcher("addSubscriber.jsp").forward(request, response);
	    e.printStackTrace();
	    return ;
    }
    catch (SubscriberExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "The person you want to add to the library already exists.");
    	request.getRequestDispatcher("addSubscriber.jsp").forward(request, response);
	    e.printStackTrace();
	    return ;
    }
		
		//Everything went right
		this.getServletContext().setAttribute("badExecution",null);
		
		//We redirect the client to the success page
		request.getRequestDispatcher("success.jsp").forward(request, response);
		
	}

}
