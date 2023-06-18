package users;

import java.io.IOException;
import java.io.Serializable;
import attributes.DataBase;
import attributes.Request;
import attributes.Research;
import enums.*;
import interfaces.*;

/**
 */
public class Dean extends Employee implements Serializable, SendableAndResearchable
{
	private static final long serialVersionUID = -1072725458817589484L;

	private Faculty faculty;
    
	public Dean(String password, String firstName, String lastName, int age, Faculty faculty) {
		super(password, firstName, lastName, age);
		this.faculty = faculty;
	}

    
    // Getters/Setters
	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	
	// Dean methods
	public void sendRequest(Request request) throws IOException {
		DataBase.requests.add(request); DataBase.serilaizeRequests();
	}
	
    public boolean seeRequest(User u, String requestType, String requestMess) throws IOException {
    	if(requestType.toLowerCase().equals("rem")) sendRequest(new Request(this, Admin.getAdmin(), requestType, requestMess));
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
			   "Faculty: " + this.faculty;
	}
	public String toString() {
		return super.toString() + "\n-\n" +
			   "Faculty: " + this.faculty;
	}
}

