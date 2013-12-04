package loansManagement;

import java.util.List;

import org.hibernate.*;

public class LoanManager
{
	public Boolean addOrUpdateLoan(Session session, Loan loan) {
		try {
			session.saveOrUpdate(loan);
			
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la sauvegarde ");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}

	
	public Boolean delLoan(Session session, Loan loan) {
		try {
			session.delete(loan);
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la suppression ");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}
	
	
	/**
	 * @param session the hibernate session
	 * @return a string listing the different loans not void present in the database
	 */
	@SuppressWarnings("unchecked")
  public String toString(Session session)
	{
		List<Loan> loans = (List<Loan>)session.createQuery("from Loan where voidLoan=false").list();
		String s = "List of loans ("+loans.size()+") present in the library:\n";
		int i=1;
		for(Loan l : loans) {
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
		List<Loan> loans = (List<Loan>)session.createQuery("from Loan where voidLoan=false").list();
		
		if(loans.size()<=0)
			return "No media is currently lent in the library.";
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<p>List of loans ("+loans.size()+") present in the library:</p>");
		sb.append("<table border=\"1\" >");
		sb.append(Loan.getLoanHeader());
		int i=1;
		for(Loan l : loans) {
			sb.append("<tr><td>"+i+"</td>"+l.toHTML()+"</tr>");
			i++;
		}
		sb.append("</table>");
		return sb.toString();
	}
}
