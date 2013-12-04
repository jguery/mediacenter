package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.BadParametersException;
import exceptions.MediaExistsException;

import main.Library;

/**
 * Servlet implementation class AddExemplaryServlet
 */
@WebServlet("/addExemplary.do")
public class AddExemplaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the values from the form addExemplary.jsp
		String key = request.getParameter("key");
			
		//Get the library from the context
		Library lib = (Library)this.getServletContext().getAttribute("library");
		
		try { lib.addExemplary(key); }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("addExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (MediaExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "No media with the given key was found in the library.");
			request.getRequestDispatcher("addExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
		
		//Everything went right
		this.getServletContext().setAttribute("badExecution",null);
					
		//We redirect the client to the success page
		request.getRequestDispatcher("success.jsp").forward(request, response);
	}

}
