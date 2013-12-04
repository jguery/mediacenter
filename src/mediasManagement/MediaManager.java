package mediasManagement;

import org.hibernate.*;

import bookingsManagement.Booking;

import java.util.*;

import loansManagement.Loan;

public class MediaManager
{

	public Boolean addOrUpdateMedia(Session session, Media media) 
	{
		try {
			//Need to save the loans before saving the media
			Map<Integer, Loan> tabEx = media.getTabExemplaries();
			for(Iterator<Loan> i = tabEx.values().iterator() ; i.hasNext() ;)
				session.saveOrUpdate(i.next());
			
			//Persist the media
			session.saveOrUpdate(media);
			
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la sauvegarde ");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}

	
  @SuppressWarnings("unchecked")
  public Boolean delMedia(Session session, Media media) 
	{
		try {
			//Need to delete the loans before deleting the media
			Map<Integer, Loan> tabEx = media.getTabExemplaries();
			for(Iterator<Loan> i = tabEx.values().iterator() ; i.hasNext() ;)
				session.delete(i.next());
			
			//Also, all the bookings of the media need to be deleted
			List<Booking> bookings = (List<Booking>)session.createQuery("from Booking where media=:m")
					.setParameter("m", media).list();
			for(Booking b: bookings)
				session.delete(b);
			
			session.delete(media);
			
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la suppression ");				  
			  pe.printStackTrace(); 
			  return false;
		 }
		return true;
	}
	
	
	/**
	 * To get a media with the given key stored in the database
	 * @param session the session to connect to hibernate
	 * @param key the key of the media searched
	 * @return the media looked for
	 */
	public Media getByKey(Session session, String key)
	{
		return (Media)session.get(Media.class, key);
	}
	
	
	/**
	 * @param session the hibernate session
	 * @return a string listing the different subscribers present in the database
	 */
	@SuppressWarnings("unchecked")
  public String toString(Session session)
	{
		List<Media> medias = (List<Media>)session.createQuery("from Media").list();
		String s = "List of medias ("+medias.size()+") present in the library:\n";
		for(Media m : medias)
			s += "\t"+m.toString()+"\n";
		return s;
	}
	
	/**
	 * @param session the hibernate session
	 * @return a string in HTML listing the different subscribers present in the database
	 */
	@SuppressWarnings("unchecked")
  public String toHTML(Session session)
	{
		List<Media> medias = (List<Media>)session.createQuery("from Media").list();
		
		if(medias.size()<=0)
			return "No media present in the library.";
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<p>List of medias ("+medias.size()+") present in the library:</p>");
		sb.append(Media.toHTML(medias));
		return sb.toString();
	}
}
