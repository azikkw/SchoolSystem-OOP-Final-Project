package users;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;
import java.util.Vector;
import interfaces.*;
import attributes.*;

/**
 */
public class Librarian extends Employee implements Serializable, SendableAndResearchable
{
	private static final long serialVersionUID = -7203763362444277232L;

	public static HashMap <User, Vector <Literature> > givenBooks = new HashMap<>();
	public static Vector <Literature > availableBooks = new Vector<>();

	
	public Librarian(String password, String firstName, String lastName, int age) {
		super(password, firstName, lastName, age);
	}
    
    
    // Librarian methods
    public  HashMap <User, Vector <Literature> > getGivenBooksList() {
        return givenBooks;
    }
    
    public void setgivenBooksList(User u, Literature b) {
		Librarian.givenBooks.get(u).add(b);
	}

	public void sendRequest(Request request) throws IOException {
		DataBase.requests.add(request); DataBase.serilaizeRequests();
	}

	public void doResearch(Research research) throws IOException {
		super.doResearch(research);
	}
	
	public Vector <Literature > getAvailableBooks() {
		return availableBooks;
	}
	
	public static Literature getBook(String title, String author) {
		Optional <Literature>optional = Librarian.availableBooks.stream().filter(n -> n.getAuthor().equals(author) && n.getName().equals(title)).findFirst();
		if(optional.isPresent()) {Literature book = optional.get(); return book;}
		return null;
	}

	public void setAvailableBooks( Literature literature) {
		Librarian.availableBooks.add(literature);
	}

	
	// Standard methods
	public boolean equals(Object o) {
		return super.equals(o);
	}
	public int hashCode() {
		return super.hashCode();
	}

	public int compareTo(User u) {
	     return super.compareTo(u);
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public String forProfile() {
		return super.forProfile();
	}
	public String toString() {
		return super.toString();
	}
	
	
	public static void serilaizeAvailableBooks() throws IOException {
		try {
			FileOutputStream fos2 = new FileOutputStream("availableBooks.out");
			ObjectOutputStream book2 = new ObjectOutputStream(fos2);
			book2.writeObject(availableBooks);
			book2.flush();
			book2.close();
			fos2.close();
		} catch(Exception e) {
			e.getStackTrace();
		}
    }
	public static void serilaizeGivenBooks() throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream("givenBooks.out");
			ObjectOutputStream book = new ObjectOutputStream(fos);
			book.writeObject(givenBooks);
			book.flush();
			book.close();
			fos.close();
		} catch(Exception e) {
			e.getStackTrace();
		}
    }
        
	public static HashMap <User, Vector <Literature> > deserializeGivenBooks() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("givenBooks.out");
			ObjectInputStream book = new ObjectInputStream(fis);
			givenBooks  = (HashMap <User, Vector <Literature> >) book.readObject();
			book.close();
			fis.close();
			
		} catch(Exception e) {
			e.getStackTrace();
		}
		return givenBooks;
	}
	public static Vector <Literature > deserializeAvailableBooks() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("availableBooks.out");
			ObjectInputStream book2 = new ObjectInputStream(fis);
			availableBooks  = (Vector <Literature >) book2.readObject();
			book2.close();
			fis.close();
			
		} catch(Exception e) {
			e.getStackTrace();
		}
		return availableBooks;
	}
}

