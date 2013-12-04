/**
 * Test class for the Library class
 * @author M.T. Segarra
 * @version 0.0.1
 */
package tests;


import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

import subscribersManagement.Subscriber;
import utils.HibernateUtil;
import main.Library;
import mediasManagement.*;
import exceptions.*;


public class LibraryTest {

	private Library lib;
	private Book b;
	private Subscriber s;
	
	@Before
	public void beforeTests() throws BadParametersException, 
		TooManyLoansOrBookingsException, LentOrBookedMediaException, NoRightsException, 
		MediaAvailableException, MediaExistsException, NotAnExemplaryException, BookingAccountedException 
	{
		lib = new Library();
		s = new Subscriber("Yann", "Bellier", new GregorianCalendar(2000,11,13), new GregorianCalendar());
		
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("Corral");
		b = new Book("La noche", authors, new GregorianCalendar(), 2, "0201708515");
	}
	
	@After
	public void afterTests()
	{
		//lib.getCheckInvalidBookings().interrupt();
		
		HibernateUtil.getSessionFactory().close();
	}

	@Test(expected = SubscriberExistsException.class)
	public void testLookForSubsNotExist() throws BadParametersException, SubscriberExistsException
	{
		lib.addSubscriber(s);
		lib.lookForSubscriber("Yann", "Bellier", new GregorianCalendar(2001,12,13));
	}
	
	@Test
	public void testLookForSubsExist() throws BadParametersException, SubscriberExistsException
	{
		lib.addSubscriber(s);
		Subscriber r = lib.lookForSubscriber("Yann", "Bellier", new GregorianCalendar(2000,11,13));
		assertTrue(r.equals(s));
	}
	
	@Test(expected = MediaExistsException.class)
	public void testLookForMediaNotExist() throws BadParametersException, MediaExistsException
	{
		lib.addMedia(b);
		lib.lookForMedia("truc");
	}
	
	@Test
	public void testLookForMediaExist() throws BadParametersException, MediaExistsException
	{
		lib.addMedia(b);
		Media m = lib.lookForMedia("0201708515");
		assertTrue(m.equals(b));
	}
	
	
	@Test(expected = SubscriberExistsException.class)
	public void testAddExistingSubscriber() throws BadParametersException, 
	SubscriberExistsException {		
		lib.addSubscriber(s);
		s = new Subscriber("Yann", "Bellier", new GregorianCalendar(2000,11,13), new GregorianCalendar());
		lib.addSubscriber(s);
	}

	
	@Test
	public void testDelSubsWithoutLoans() throws BadParametersException, 
		SubscriberExistsException, SubscriberWithLoansException{
		long number = lib.addSubscriber(s);
		lib.deleteSubscriber(number);
	}
	
	@Test(expected = SubscriberExistsException.class)
	public void testDelSubsNotExist() throws SubscriberExistsException, SubscriberWithLoansException,
		BadParametersException
	{
		lib.deleteSubscriber(123);
	}
	
	@Test(expected = SubscriberWithLoansException.class)
	public void testDelSubsWithLoans() throws BadParametersException, 
		SubscriberExistsException, SubscriberWithLoansException, 
		MediaExistsException, LentOrBookedMediaException, TooManyLoansOrBookingsException, NoRightsException{
		
		long number = lib.addSubscriber(s);
		lib.addMedia(b);
		
		// Create a loan
		lib.lend("0201708515",number);
		
    lib.deleteSubscriber(number);
	}
	
	
	@Test
	public void testDelSubsWithBookings() throws BadParametersException, 
		SubscriberExistsException, SubscriberWithLoansException, 
		MediaExistsException, LentOrBookedMediaException, TooManyLoansOrBookingsException, 
		NoRightsException, MediaAvailableException, NotAnExemplaryException, BookingAccountedException
		{
		
		long number = lib.addSubscriber(s);
		lib.addMedia(b);
		
		// Create a loan
		Integer ex1 = lib.lend("0201708515",number);
		Integer ex2 =  lib.lend("0201708515",number);
		lib.book("0201708515",number);
		
		lib.returnMedia("0201708515",ex1);
		lib.returnMedia("0201708515",ex2);
		
    lib.deleteSubscriber(number);
	}
	
	
	@Test(expected = NoRightsException.class)
	public void testLendWithoutRights() throws BadParametersException, SubscriberExistsException, 
		MediaExistsException, LentOrBookedMediaException, TooManyLoansOrBookingsException, 
		NoRightsException, SubscriberWithLoansException, MediaAvailableException
	{
		long number = lib.addSubscriber(s);
		lib.addMedia(b);
		
		// Create a loan
		lib.lend("0201708515",number);

		try
    {
			lib.deleteSubscriber(number);
    }
    catch (SubscriberWithLoansException e)
    {
    	lib.lend("0201708515",number);
    }
	}
	
	@Test
	public void testListSubscriber() throws BadParametersException, SubscriberExistsException
	{
		lib.addSubscriber(s);
		lib.addSubscriber(new Subscriber("Machin", "bidule", new GregorianCalendar(), new GregorianCalendar()));
		
		System.out.println(lib.listSubscribers());
	}
	
	@Test(expected = BadParametersException.class)
	public void testAddNullMedia() throws BadParametersException, 
		MediaExistsException{
		lib.addMedia(null);
	}
	
	@Test
	public void testAddMedia() throws BadParametersException, MediaExistsException{
		lib.addMedia(b);
		System.out.println(lib.listMedias());
	}
	
	@Test(expected = MediaExistsException.class)
	public void testAddExistingMedia() throws BadParametersException, 
		MediaExistsException
	{
		lib.addMedia(b);
		
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("Corral");
		b = new Book("La noche", authors, new GregorianCalendar(), 2, "0201708515");
		lib.addMedia(b);
	}
	
	@Test
	public void testDelMedia() throws BadParametersException, 
		MediaExistsException, LentOrBookedMediaException
	{
		lib.addMedia(b);
	
		lib.deleteMedia(b.getKey());
		
		//Should list 0 media
		System.out.println(lib.listMedias());
	}
	
	@Test(expected = LentOrBookedMediaException.class)
	public void testDelLentMedia() throws TooManyLoansOrBookingsException, LentOrBookedMediaException, 
	BadParametersException, MediaExistsException, NoRightsException, SubscriberExistsException
	{
		lib.addMedia(b);
		long numb = lib.addSubscriber(s);
		
		lib.lend(b.getKey(), numb);
		
		lib.deleteMedia(b.getKey());
	}
	
	@Test(expected = MediaExistsException.class)
	public void testDelMediaNotExist() throws MediaExistsException, 
		LentOrBookedMediaException, BadParametersException
	{
		lib.deleteMedia(b.getKey());
	}
	
	@Test
	public void testAddExemplary() throws BadParametersException, MediaExistsException 
	{
		lib.addMedia(b);
		lib.addExemplary("0201708515");
		System.out.println("List of medias: "+lib.listMedias());
		System.out.println("\n");
	}
	
	@Test(expected = MediaExistsException.class)
	public void testAddExemplaryExc() throws BadParametersException, MediaExistsException
	{
		lib.addExemplary("0201708515");
	}
	

	@Test(expected = LentOrBookedMediaException.class)
	public void testDelExemplary() throws BadParametersException, 
		MediaExistsException, SubscriberExistsException, LentOrBookedMediaException, 
		NoRightsException, NotEnoughExemplariesException, TooManyLoansOrBookingsException
	{
		lib.addMedia(b);
		long number = lib.addSubscriber(s);
		lib.lend("0201708515",number);
		
		System.out.println(lib.listMedias());
		System.out.println(lib.listLoans());
		
		lib.delExemplary("0201708515");
		
		System.out.println(lib.listMedias());
		System.out.println(lib.listLoans()+"\n");
		
		lib.lend("0201708515",number);
	}
	
	//A normal lending
	@Test
	public void testLend() throws  BadParametersException, 
		MediaExistsException, SubscriberExistsException, TooManyLoansOrBookingsException, 
		LentOrBookedMediaException, NoRightsException
	{
		
		lib.addMedia(b);
		long number = lib.addSubscriber(s);
		
		lib.lend("0201708515",number);

		System.out.println(lib.listLoans());
	}
	
	
	//Someone tries to lend more than allowed to him
	@Test(expected = TooManyLoansOrBookingsException.class)
	public void testLendTooMany() throws  BadParametersException, 
		MediaExistsException, SubscriberExistsException, TooManyLoansOrBookingsException, 
		LentOrBookedMediaException, NoRightsException
	{
  	//Set Constraints.maxBOOKLENTCHILD = 1
  		
  	lib.addMedia(b);
  	long number = lib.addSubscriber(s);
  	
  	lib.lend("0201708515",number);
  
  	System.out.println(lib.listLoans());
  	
  	lib.lend("0201708515",number);
	}
	
	//Lend more exemplaries than available
	@Test(expected = LentOrBookedMediaException.class)
	public void testLendNotEnoughExemplaries() throws  BadParametersException, 
		MediaExistsException, SubscriberExistsException, TooManyLoansOrBookingsException, 
		LentOrBookedMediaException, NoRightsException
	{
		lib.addMedia(b);
		long number = lib.addSubscriber(s);
		Subscriber ns = new Subscriber("jean","paul",new GregorianCalendar(), new GregorianCalendar());
		Subscriber ns2 = new Subscriber("jean","michel",new GregorianCalendar(), new GregorianCalendar());
		long number2 = lib.addSubscriber(ns);
		long number3 = lib.addSubscriber(ns2);
				
		lib.lend("0201708515",number);		
		lib.lend("0201708515", number2);
		lib.lend("0201708515", number3);
	}
	
	
	//Someone not registered in the library tries to lend a book
	@Test(expected=SubscriberExistsException.class)
	public void testNoSubscriberLend() throws  BadParametersException, 
		MediaExistsException, SubscriberExistsException, TooManyLoansOrBookingsException, 
		LentOrBookedMediaException, NoRightsException
	{
		
		lib.addMedia(b);
		
		lib.lend("0201708515",1);
	}
	

	//A subscriber tries to lend a media not present in the library
	@Test(expected=MediaExistsException.class)
	public void testNoBookLend() throws  BadParametersException, 
		MediaExistsException, SubscriberExistsException, TooManyLoansOrBookingsException, LentOrBookedMediaException, NoRightsException{
		
		long number = lib.addSubscriber(s);
		
		lib.lend("0201708515",number);
	}
	
	
	//Make a loan and a normal return and then try to return an exemplary not lent
	@Test//(expected=LentOrBookedMediaException.class)
	public void testReturn() throws TooManyLoansOrBookingsException, LentOrBookedMediaException, 
		BadParametersException, MediaExistsException, SubscriberExistsException,
		NotAnExemplaryException, BookingAccountedException, NoRightsException
	{
		
		lib.addMedia(b);
		long number = lib.addSubscriber(s);
		
		Integer ex1 = lib.lend("0201708515",number);
		System.out.println(lib.listLoans());
		
		lib.returnMedia(b.getKey(), ex1);
		
		System.out.println(lib.listLoans());
		
		//lib.returnMedia(b.getKey(), ex1);
	}
	
	
	
	//A normal booking, followed by an attempt to book once more
	@Test(expected = TooManyLoansOrBookingsException.class)
	public void testBook() throws BadParametersException, MediaExistsException, 
		SubscriberExistsException, LentOrBookedMediaException, TooManyLoansOrBookingsException, 
		NoRightsException, MediaAvailableException
	{
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("Corral");
		Book b2 = new Book("La noche", authors, new GregorianCalendar(), 1, "12345");
		
		Subscriber ns = new Subscriber("jean","paul",new GregorianCalendar(), new GregorianCalendar());
		Subscriber ns2 = new Subscriber("Marc","michel",new GregorianCalendar(), new GregorianCalendar());
		long number1 = lib.addSubscriber(ns);
		long number2 = lib.addSubscriber(ns2);
		
		lib.addMedia(b2);
		
		lib.lend("12345", number1);
		
		lib.book("12345",number2);
		
		System.out.println(lib.listBookings());
		
		
		// Set Constraints.maxBOOKLENTCHILD = 1
		lib.book("12345", number2);
	}
	
	
	//A subscriber booked a media after someone. This media is available, he tries to lend it
	//But he is not prioritary
	@Test(expected = LentOrBookedMediaException.class)
	public void testBookNotPrioritary() throws BadParametersException, MediaExistsException, 
		SubscriberExistsException, LentOrBookedMediaException, TooManyLoansOrBookingsException, 
		NoRightsException, MediaAvailableException, NotAnExemplaryException, BookingAccountedException
	{
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("Corral");
		Book b2 = new Book("La noche", authors, new GregorianCalendar(), 1, "12345");
		
		Subscriber ns = new Subscriber("jean","paul",new GregorianCalendar(), new GregorianCalendar());
		Subscriber ns2 = new Subscriber("jean","michel",new GregorianCalendar(), new GregorianCalendar());
		long number1 = lib.addSubscriber(ns);
		long number2 = lib.addSubscriber(ns2);
		
		lib.addMedia(b2);
		
		Integer ex1 = lib.lend("12345", number1);
		
		lib.book("12345",number2);
		
		System.out.println(lib.listBookings());
		
		lib.book("12345", number1);
		lib.returnMedia("12345", ex1);
		lib.lend("12345", number1);
	}
	
	
	//A subscriber booked a media before someone. This media is available, he tries to lend it
	//Since he is prioritary, he can
	@Test
	public void testBookPrioritary() throws BadParametersException, MediaExistsException, 
		SubscriberExistsException, LentOrBookedMediaException, TooManyLoansOrBookingsException, 
		NoRightsException, MediaAvailableException, NotAnExemplaryException, BookingAccountedException
	{
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("Corral");
		Book b2 = new Book("La noche", authors, new GregorianCalendar(), 1, "12345");
		
		Subscriber ns = new Subscriber("jean","paul",new GregorianCalendar(), new GregorianCalendar());
		Subscriber ns2 = new Subscriber("jean","michel",new GregorianCalendar(), new GregorianCalendar());
		long number1 = lib.addSubscriber(ns);
		long number2 = lib.addSubscriber(ns2);
		
		lib.addMedia(b2);
		
		Integer ex1 = lib.lend("12345", number1);
		
		lib.book("12345",number2);
		
		System.out.println(lib.listBookings());
		
		lib.book("12345", number1);
		lib.returnMedia("12345", ex1);
		lib.lend("12345", number2);
		
		System.out.println(lib.listBookings());
		System.out.println(lib.listLoans());
	}
}
