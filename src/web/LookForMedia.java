package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.Library;
import mediasManagement.Media;
import subscribersManagement.Subscriber;
import exceptions.BadParametersException;
import exceptions.MediaExistsException;
import exceptions.SubscriberExistsException;

/**
 * Servlet implementation class LookForMedia
 */
@WebServlet("/lookForMedia.do")
public class LookForMedia extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the values from the form lookForMedia.jsp
		String title = request.getParameter("title");
						
		//Get the library from the context
		Library lib = (Library)this.getServletContext().getAttribute("library");
				
		List<Media> li = null;
		try{ li = lib.lookForMediaTitle(title); }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("lookForMedia.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (MediaExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "No media containing the given title found.");
			request.getRequestDispatcher("lookForMedia.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
				
		//Go to the lookForMedia.jsp page and print the result
		this.getServletContext().setAttribute("success", Media.toHTML(li));
		request.getRequestDispatcher("lookForMedia.jsp").forward(request, response);
	}

}
