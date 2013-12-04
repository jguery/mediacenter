package bookingsManagement;

import java.util.List;

import mediasManagement.Media;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import subscribersManagement.Subscriber;

public class BookingManager
{
	public Boolean addBooking(Session session, Booking booking) {
		try {
			session.save(booking);
			
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la sauvegarde ");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}

	
	public Boolean delBooking(Session session, Booking booking) {
		try {
			session.delete(booking);
		}catch (HibernateException pe) {
			  System.err.println("Problème dans la suppression ");				  
			  pe.printStackTrace(); 
			  return false;
		  }
		return true;
	}
	
	public List<Booking> getBookings(Session session)
	{
		return (List<Booking>)session.createQuery("from Booking").list();
	}
	
	
	/**
	 * @param session connection to hibernate
	 * @param media the media booked
	 * @return the first bookings of the media "media" not taken into account
	 */
	@SuppressWarnings("unchecked")
  public Booking getWaitingBooking(Session session, Media media)
	{
		String s = "from Booking b where b.media=:m and b.accounted=false order by id asc";
		Query q = session.createQuery(s).setParameter("m", media);
		List<Booking> bookings = (List<Booking>)q.list();
		
		if(bookings.size()==0)
			return null;
		else return bookings.get(0);
	}
	

	/**
	 * 
	 * @param session
	 * @param booker
	 * @param media
	 * @return
	 */
	@SuppressWarnings("unchecked")
  public Booking getBookingFromBooker(Session session, Subscriber booker, Media media)
	{
		String s = "from Booking b where b.media=:m and b.booker=:booker and b.accounted=true order by id asc";
		Query q = session.createQuery(s).setParameter("m", media).setParameter("booker", booker);
		List<Booking> bookings = (List<Booking>)q.list();
		
		if(bookings.size()==0)
			return null;
		else return bookings.get(0);
	}
	
	
	/**
	 * @param session the hibernate session
	 * @return a string listing the different bookings present in the database
	 */
	@SuppressWarnings("unchecked")
  public String toString(Session session)
	{
		List<Booking> bookings = (List<Booking>)session.createQuery("from Booking").list();
		String s = "List of bookings ("+bookings.size()+") present in the library:\n";
		int i=1;
		for(Booking l : bookings) {
			s += i+":\t"+l.toString()+"\n";
			i++;
		}
		return s;
	}
	
	/**
	 * @param session the hibernate session
	 * @return a string in HTML listing the different bookings present in the database
	 */
	@SuppressWarnings("unchecked")
  public String toHTML(Session session)
	{
		List<Booking> bookings = (List<Booking>)session.createQuery("from Booking").list();
		
		if(bookings.size()<=0)
			return "No bookings present in the library.";

		StringBuilder sb = new StringBuilder();
		
		sb.append("<p>List of bookings ("+bookings.size()+") present in the library:</p>");
		sb.append("<table border=\"1\" >");
		sb.append(Booking.getBookingHeader());
		int i=1;
		for(Booking l : bookings) {
			sb.append("<tr><td>"+i+"</td>"+l.toHTML()+"</tr>");
			i++;
		}
		sb.append("</table>");
		return sb.toString();
	}
}
