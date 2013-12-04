package subscribersManagement;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import bookingsManagement.Booking;

public class SubscriberManager
{
	public Boolean addSubscriber(Session session, Subscriber subscriber) {
		try {
			session.save(subscriber);
			
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la sauvegarde ");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}

	
	public Boolean delSubscriber(Session session, Subscriber subscriber) {
		try {
			session.delete(subscriber);
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la suppression ");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}
	
	/**
	 * Return a subscriber who is stored in the database, and which number is number
	 * @param session the hibernate session
	 * @param number the number of the subscriber
	 * @return the subscriber with the given number, null if he doesn't exist
	 */
	public Subscriber getByNumber(Session session, long number)
	{
		return (Subscriber)session.get(Subscriber.class, number);
	}
	
	
	public Boolean removeRights(Session session, Subscriber subscriber) {
		try {
			//Delete the bookings of the subscirbers
			for(Booking b: subscriber.getCurrentBookings())
				session.delete(b);
			
			//Update the rights of the subscriber
			session.update(subscriber);
		}catch (HibernateException pe) {
			  System.err.println("Problem in removing the rights of subscriber");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}
	
	/**
	 * @param session the hibernate session
	 * @return a string listing the different subscribers present in the database
	 */
	@SuppressWarnings("unchecked")
  public String toString(Session session)
	{
		List<Subscriber> subs = (List<Subscriber>)session.createQuery("from Subscriber").list();
		String s = "List of subscribers ("+subs.size()+") present in the library:\n";
		for(Subscriber sub : subs)
			s += "\t"+sub.toString()+"\n";
		return s;
	}
	
	
	/**
	 * @param session the hibernate session
	 * @return a string in HTML listing the different subscribers present in the database
	 */
	@SuppressWarnings("unchecked")
  public String toHTML(Session session)
	{
		List<Subscriber> subs = (List<Subscriber>)session.createQuery("from Subscriber").list();
		
		if(subs.size()<=0)
			return "No subscribers present in the library.";
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<p>List of subscribers ("+subs.size()+") present in the library:</p>");
		sb.append(Subscriber.toHTML(subs));
		return sb.toString();
	}
}
