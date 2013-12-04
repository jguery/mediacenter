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
import exceptions.NotEnoughExemplariesException;

import main.Library;

/**
 * Servlet implementation class DeleteExemplaryServlet
 */
@WebServlet("/deleteExemplary.do")
public class DeleteExemplaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		//Get the values from the form deleteExemplary.jsp
		String key = request.getParameter("key");
				
		//Get the library from the context
		Library lib = (Library)this.getServletContext().getAttribute("library");
		
		try{ lib.delExemplary(key); }
    catch (MediaExistsException e)
    {
    	this.getServletContext().setAttribute("badExecution", "No media with the given key was found in the library.");
			request.getRequestDispatcher("deleteExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (BadParametersException e)
    {
    	this.getServletContext().setAttribute("badExecution", "You have not submitted all the information correctly, or information was missing.");
			request.getRequestDispatcher("deleteExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (LentOrBookedMediaException e)
    {
    	this.getServletContext().setAttribute("badExecution", "All exemplaries of the media currently lent.");
			request.getRequestDispatcher("deleteExemplary.jsp").forward(request, response);
			e.printStackTrace();
			return;
    }
    catch (NotEnoughExemplariesException e)
    {
    	this.getServletContext().setAttribute("badExecution", "There are not enough exemplaries left. Delete the media instead.");
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
