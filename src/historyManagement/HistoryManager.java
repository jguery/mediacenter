package historyManagement;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class HistoryManager
{
	
	public Boolean addHistory(Session session, History history) {
		try {
			session.save(history);
			
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la sauvegarde ");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}
	
	
	/**
	 * Present the history of the loans of the library
	 * @param session session to hibernate
	 * @return string containing the history of the loans of the library
	 */
	@SuppressWarnings("unchecked")
  public String toString(Session session)
	{
		List<History> history = (List<History>)session.createQuery("from History").list();
		String s = "History of the loans ("+history.size()+"):\n";
		int i=1;
		for(History l : history) {
			s += i+":\t"+l.toString()+"\n";
			i++;
		}
		return s;
	}
	
	/**
	 * @param session the hibernate session
	 * @return a string in HTML listing the different loans present in the database
	 */
	@SuppressWarnings("unchecked")
  public String toHTML(Session session)
	{
		List<History> history = (List<History>)session.createQuery("from History").list();
		
		if(history.size()<=0)
			return "No loan in the history.";
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<p>History of all the loans ("+history.size()+"):</p>");
		sb.append("<table border=\"1\" >");
		sb.append(History.getHistoryHeader());
		int i=1;
		for(History h : history) {
			sb.append("<tr><td>"+i+"</td>"+h.toHTML()+"</tr>");
			i++;
		}
		sb.append("</table>");
		return sb.toString();
	}

}
