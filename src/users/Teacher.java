package users;

import java.io.IOException;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Vector;
import attributes.*;
import enums.*;
import interfaces.SendableAndResearchable;

/**
 */
public class Teacher extends Employee implements Cloneable, Serializable, SendableAndResearchable
{
	private static final long serialVersionUID = 4526927638042923352L;

	private double rate;
	private int rateCounter;
    
    private TypeTeacher type;
    private Faculty faculty;

    /** to distinct his lessons
	 */
    private HashMap <Lesson, Vector <Student> > lessons;
    
    private Schedule schedule;
    
    {
    	lessons = new HashMap <>();
    	schedule = new Schedule();
    }

    
    public Teacher(String password, String firstName, String lastName, int age, TypeTeacher type, Faculty faculty) {
    	super(password, firstName, lastName, age);
    	this.type = type;
    	this.faculty = faculty;
    	if(this.type.equals(TypeTeacher.PROFESSOR)) super.setResearchStatus(true);
    }


	// Getters/Setters
	public TypeTeacher getType() {
		return type;
	}
	public void setType(TypeTeacher type) {
		this.type = type;
	}
	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public HashMap <Lesson, Vector <Student> > getLessons() {
		return lessons;
	}
	public void setCourses(HashMap <Lesson, Vector <Student>> lessons) {
		this.lessons = lessons;
	}	
	public int getRateCounter() {
		return rateCounter;
	}
	public void setRateCounter(int rateCounter) {
		this.rateCounter = rateCounter;
	}
	public Schedule gettSchedule() {
		return this.schedule;
	}


	// Teacher methods
	public boolean putMark(Student s, Mark m, Lesson lesson) throws IOException {
	    Vector <Mark> marks = s.getJournal().getMarks().computeIfAbsent(lesson.getCourse(), k -> new Vector<>());
	    Double total = marks.stream().map(n->n.getMark()).mapToDouble(i -> i).sum();
	    if(total + m.getMark() > 30) {
	      return false;
	    }
	    marks.add(m);
	    DataBase.serilaizeUsers();
	    return true;
	 }
	
	public boolean drawUpSchedule(Lesson lesson, DayOfWeek day, int begin) throws IOException {
		lesson.setDay(day);
		if(!(begin > 7 && begin < 23)) return false;
		lesson.setBegin(begin);
		DataBase.serilaizeUsers();
		DataBase.serilaizeCourses();
		return true;
	}
	
	public void sendRequest(Request request) throws IOException {
		DataBase.requests.add(request); DataBase.serilaizeRequests();
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
	
	public int StudentsAmount() {
		for(Lesson l: lessons.keySet()) {
			 return lessons.get(l).size();
		}
		return 0;
	}

	public String forProfile() {
		 return super.forProfile() + "\n-\n" +
				"Teacher type: " + this.type + "\nFaculty: " + this.faculty + "\n-\n" +
				"Teacher rate: " + (this.rate / this.rateCounter) +  "\n-\n" +
				"Students amount: " + this.StudentsAmount();
	}
	public String toString() {
		return super.toString() + "\n-\n" +
			   "Teacher type: " + this.type + "\nFaculty: " + this.faculty + "\n-\n" +
			   "Teacher rate: " + (this.rate / this.rateCounter);
	}
}

