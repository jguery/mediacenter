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
import javax.servlet.http.HttpSession;

import exceptions.BadParametersException;
import exceptions.SubscriberExistsException;

import subscribersManagement.Subscriber;

import main.Library;

/**
 * Servlet implementation class ConnexionServlet
 */
@WebServlet("/connexion.do")
public class ConnexionServlet extends HttpServlet {
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
		
		
		//If the firstname is admin, and so is the lastname, 
		//this is the librarian logging on
		if(!firstname.equals("admin") || !lastname.equals("admin"))
		{
			
			Library lib = (Library)this.getServletContext().getAttribute("library");
			
			//Check if the borndate given matches the regular expression mm/dd/yyyy
			Pattern p = Pattern.compile("(\\d\\d)/(\\d\\d)/(\\d\\d\\d\\d)");
			Matcher m = p.matcher(borndate);
			if(!m.matches()){
				this.getServletContext().setAttribute("badExecution", "The date you provided was not in the good format.");
				request.getRequestDispatcher("connexion.jsp").forward(request, response);
				return ;
			}
			
			
			//If it is not, we try to retrieve the subscriber trying to log on
  		Subscriber s = null;
  		
  		try	{
  	    s = lib.lookForSubscriber(firstname, lastname, new GregorianCalendar(Integer.parseInt(m.group(3)),
  	      		Integer.parseInt(m.group(1))-1,Integer.parseInt(m.group(2))));
      }
      catch (NumberFormatException e) {	//Problem with the date
      	this.getServletContext().setAttribute("badExecution", "The date you provided was not in the good format.");
  			request.getRequestDispatcher("connexion.jsp").forward(request, response);
  			e.printStackTrace();
  			return ;
      }
      catch (BadParametersException e) {	//Problem with the names (may be empty)
      	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
      	request.getRequestDispatcher("connexion.jsp").forward(request, response);
  	    e.printStackTrace();
  	    return ;
      }
      catch (SubscriberExistsException e) {	//The subscriber does not exist
      	this.getServletContext().setAttribute("badExecution", "The subscriber does not exist in the library. Beware: the system is case-sensitive.");
      	request.getRequestDispatcher("connexion.jsp").forward(request, response);
  	    e.printStackTrace();
  	    return ;
      }
  		
  		//We save the subscriber who logged on in a SESSION
  		HttpSession session = request.getSession();
  		session.setAttribute("connected", "subscriber");
  		session.setAttribute("connectedSubs", s);
  		
  		//Redirect to the index page
  		request.getRequestDispatcher("index.jsp").forward(request, response);
  		
  		return;
		}
		
		//At that point, we know a librarian logged on
		//We save their information in a SESSION
		HttpSession session = request.getSession();
		session.setAttribute("connected", "admin");
		
		//Redirect to the index page
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	
}
