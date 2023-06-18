package users;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import attributes.Course;
import attributes.DataBase;
import attributes.Lesson;
import attributes.Request;
import attributes.Research;
import enums.*;

/**
 */
public class Manager extends Employee implements Serializable
{
	private static final long serialVersionUID = 2447457106357453401L;

	
	private TypeManager type;

	
	public Manager( String password, String firstName, String lastName, int age, TypeManager type) {
		super( password, firstName, lastName, age);
		this.type = type;
	}


	// Getter/Setter
	public TypeManager getType() {
		return type;
	}
	public void setType(TypeManager type) {
		this.type = type;
	}
	
	
    /** adding course in the system
	 */
	public boolean addCourse(Course course, Vector <Lesson> lessons) throws IOException {
		try {
	    	DataBase.courses.add(new Course(course, lessons));
	    	DataBase.serilaizeCourses();
	    	return true;
		} 
		catch(Exception e) {}
		return false;
	}
	
	public boolean assignTeacher(Teacher t, Lesson l) throws IOException {
		try {
			Vector <Student> students = new Vector <Student>();
			t.getLessons().put(l, students);
	    	DataBase.serilaizeUsers(); DataBase.serilaizeCourses();
	    	return true;
		} 
		catch(Exception e) { }
		return false;
	}
	
	public void sendRequest(Request request) throws IOException {
		DataBase.requests.add(request); DataBase.serilaizeRequests();
	}
	
    public boolean seeRequest(User u, String requestType, String requestMess) {
    	//if(requestType.toLowerCase().equals("rem")) sendRequest(Admin.getAdmin(), new Request(u, requestType, requestMess));
    	return true;
    }

	public void doResearch(Research research) throws IOException {
		super.doResearch(research);
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
		return super.forProfile() + "\n-\n" +
			   "Manager type: " + this.type;
	}
	public String toString() {
		return super.toString() + "\n-\n" +
			   "Manager type: " + this.type;
	}
}