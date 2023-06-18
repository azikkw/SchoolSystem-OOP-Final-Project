package attributes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.Map.Entry;
import users.*;
import enums.*;
import interfaces.Organization;

/**
 */
public class DataBase
{
    public static HashMap <TypeUser, HashSet <User> > users = new HashMap <>();
    public static HashSet <Course> courses = new HashSet <>();
    public static HashSet <Research> researches = new HashSet <>();
    public static Vector <Request> requests = new Vector <>();
	public static HashMap <String , Vector <News> > news = new HashMap <>();
	public final static Map <Degree, String> idStudDegree;
	public static Vector <Organization> organizations = new Vector<>();
	
	public static int cnt = 1;
	
	static {
		idStudDegree = new HashMap<>();
		idStudDegree.put(Degree.BACHELOR, "BD");
		idStudDegree.put(Degree.MASTER, "MD");
		idStudDegree.put(Degree.PHD, "PD");
		
		HashSet <User> students = new HashSet <User>();
		HashSet <User> teachers = new HashSet <User>();
		HashSet <User> admin = new HashSet <User>();
		HashSet <User> deans = new HashSet <User>();
		HashSet <User> managers = new HashSet <User>();
		HashSet <User> librarian = new HashSet <User>();
		admin.add(Admin.getAdmin());
		DataBase.users.put(TypeUser.TEACHER, teachers);
		DataBase.users.put(TypeUser.STUDENT, students);
		DataBase.users.put(TypeUser.DEAN, deans);
		DataBase.users.put(TypeUser.ADMIN, admin);
		DataBase.users.put(TypeUser.MANAGER, managers);
		DataBase.users.put(TypeUser.LIBRARIAN, librarian);
		
		DataBase.news.put("Official", new Vector<News>() );
		DataBase.news.put("Upcoming events", new Vector<News>() );
		DataBase.news.put("Lost and found", new Vector<News>() );


	}

	public static User isRegistered(String id, String password) {
		for(Entry <TypeUser, HashSet <User> > e: DataBase.users.entrySet()) {
			Optional <User> optional = e.getValue().stream().filter(n -> n.getId().equals(id) && n.getPassword().equals(password)).findFirst();
			if(optional.isPresent()) {User u = optional.get(); return u;}
		}
		return null;
	}
	
	public static void SerializeAll() throws IOException {
		serilaizeUsers();
		serilaizeCourses();
		serilaizeNews();
		serilaizeId();
		serilaizeResearches();
		serilaizeRequests();
		Admin.serilaizeLogFiles();
		Librarian.serilaizeAvailableBooks();
		Librarian.serilaizeGivenBooks();
		serilaizeOraganizations();
	}
	
	public static void DeserializeAll() throws ClassNotFoundException, IOException {
		DataBase.deserializeUsers();
		DataBase.deserializeCourses();
		DataBase.deserializeNews();
		DataBase.deserializeId();
		DataBase.deserializeResearches();
		DataBase.deserializeRequests();
		Admin.deserializeLogFiles();
		Librarian.deserializeAvailableBooks();
		Librarian.deserializeGivenBooks();
		deserializeOraganizations();
	}
	
    public static void serilaizeUsers() throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream("users.out");
			ObjectOutputStream user = new ObjectOutputStream(fos);
			user.writeObject(users);
			user.flush();
			user.close();
			fos.close();
		} 
		catch(Exception e) {e.getStackTrace();}
    }
	public static HashMap <TypeUser, HashSet <User> > deserializeUsers() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("users.out");
			ObjectInputStream user = new ObjectInputStream(fis);
			users = (HashMap <TypeUser, HashSet <User> >)user.readObject();
			user.close();
			fis.close();
		} 
		catch(Exception e) {e.getStackTrace();}
		
		return users;
	}

    public static void serilaizeResearches() throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream("researches.out");
			ObjectOutputStream research = new ObjectOutputStream(fos);
			research.writeObject(researches); research.flush(); research.close();
			fos.close();
		} 
		catch(Exception e) {e.getStackTrace();}
    }
	public static HashSet <Research> deserializeResearches() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("researches.out");
			ObjectInputStream research = new ObjectInputStream(fis);
			researches = (HashSet <Research>) research.readObject();
			research.close(); fis.close();
		} 
		catch(Exception e) {e.getStackTrace();}
		
		return researches;
	}

    public static void serilaizeRequests() throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream("requests.out");
			ObjectOutputStream request = new ObjectOutputStream(fos);
			request.writeObject(requests); request.flush(); request.close();
			fos.close();
		} 
		catch(Exception e) {e.getStackTrace();}
    }
	public static Vector <Request> deserializeRequests() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("requests.out");
			ObjectInputStream request = new ObjectInputStream(fis);
			requests = (Vector <Request>)request.readObject();
			request.close(); fis.close();
		} 
		catch(Exception e) {e.getStackTrace();}
		
		return requests;
	}
 	
    public static void serilaizeCourses() throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream("courses.out");
			ObjectOutputStream course = new ObjectOutputStream(fos);
			course.writeObject(courses);
			course.flush();
			course.close();
			fos.close();
		} catch(Exception e) {
			e.getStackTrace();
		}
    }
	public static HashSet <Course> deserializeCourses() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("courses.out");
			ObjectInputStream course = new ObjectInputStream(fis);
			courses = (HashSet <Course>) course.readObject();
			course.close();
			fis.close();
			
		} catch(Exception e) {
			e.getStackTrace();
		}
		return courses;
	}
	
	public static void serilaizeNews() throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream("news.out");
			ObjectOutputStream newsList = new ObjectOutputStream(fos);
			newsList.writeObject(news);
			newsList.flush();
			newsList.close();
			fos.close();
		} catch(Exception e) {
			e.getStackTrace();
		}
    }
	public static HashMap <String , Vector<News> > deserializeNews() throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("news.out");
			ObjectInputStream newsList = new ObjectInputStream(fis);
			news = (HashMap <String , Vector<News> >) newsList.readObject();
			newsList.close();
			fis.close();
			
		} catch(Exception e) {
			e.getStackTrace();
		}
		return news;
	}
	
    public static void serilaizeId() throws IOException {
	    try {
	      FileOutputStream fos = new FileOutputStream("idGenerator.out");
	      ObjectOutputStream count = new ObjectOutputStream(fos);
	      count.writeObject(cnt);
	      count.flush();
	      count.close();
	      fos.close();
	    } catch(Exception e) {
	      e.getStackTrace();
	    }
    }
	public static int deserializeId() throws IOException, ClassNotFoundException {
	    try {
	      FileInputStream fis = new FileInputStream("idGenerator.out");
	      ObjectInputStream count = new ObjectInputStream(fis);
	      cnt = (int) count.readObject();
	      count.close();
	      fis.close();
	      
	    } catch(Exception e) {
	      e.getStackTrace();
	    }
	    return cnt;
	}
	
	public static void serilaizeOraganizations() throws IOException {
		  try {
		   FileOutputStream fos = new FileOutputStream("organization.out");
		   ObjectOutputStream newsList = new ObjectOutputStream(fos);
		   newsList.writeObject(organizations);
		   newsList.flush();
		   newsList.close();
		   fos.close();
		  } catch(Exception e) {
		   e.getStackTrace();
		  }
	}
	public static Vector<Organization> deserializeOraganizations() throws IOException, ClassNotFoundException {
		try {
		   FileInputStream fis = new FileInputStream("organization.out");
		   ObjectInputStream newsList = new ObjectInputStream(fis);
		   organizations = (Vector<Organization>) newsList.readObject();
		   newsList.close();
		   fis.close();
		 } catch(Exception e) {
		   e.getStackTrace();
		 }
		
		 return organizations;
	}
}