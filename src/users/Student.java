package users;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import attributes.*;
import enums.*;
import interfaces.SendableAndResearchable;

/**
 */
public class Student extends User implements Serializable, SendableAndResearchable
{
	private static final long serialVersionUID = 5966106202813773446L;

	private int year;
	private double hIndex;
    private boolean researchStatus;
    private double GPA;
    
    private Degree degree;
    private Faculty faculty;
    
    private Vector <Course> courses;
    private Vector <Teacher> ratedTeachers;
    
    private Schedule schedule;
	
    
    /** transcript where marks are located 
	 */
    private Transcript transcript;
    
    
    private Journal journal;
    
    {
    	hIndex = 0;
		researchStatus = false;
    	courses = new Vector <Course>();
    	journal = new Journal();
    	transcript = new Transcript();
    	setRatedTeachers(new Vector<>());
    	schedule = new Schedule();
    	try {this.setId(this.idGenerator()); System.out.println("Id ");} 
    	catch(IOException e) {e.printStackTrace();}
    }
    

    public Student(String password, String firstName, String lastName, int age, int year, double GPA, Degree degree, Faculty faculty) {
		super(password, firstName, lastName, age);
		this.year = year;
		this.GPA = GPA;
		this.degree = degree;
		if(this.degree.equals(Degree.PHD)) researchStatus = true;
		this.faculty = faculty;
	}

    
    // Getters/Setters
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}	
	public double gethIndex() {
		return hIndex;
	}
	public void sethIndex(double d) {
		this.hIndex = d; 
	}
	public boolean isResearchStatus() {
		return researchStatus;
	}
	public void setResearchStatus(boolean researchStatus) {
		this.researchStatus = researchStatus;
	}
	public double getGPA() {
		return GPA;
	}
	public void setGPA(double GPA) {
		this.GPA = GPA;
	}
	public Degree getDegree() {
		return degree;
	}
	public void setDegree(Degree degree) {
		this.degree = degree;
	}
	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	public Vector <Course> getCourses() {
		return courses;
	}
	public Journal getJournal() {
		return journal;
	}
	public void setJournal(Journal journal) {
		this.journal = journal;
	}
    public Transcript getTranscript() {
        return transcript;
    }
	public void setTranscript(Transcript transcript) {
		this.transcript = transcript;
	}
	public Vector <Teacher> getRatedTeachers() {
		return ratedTeachers;
	}
	public void setRatedTeachers(Vector <Teacher> ratedTeachers) {
		this.ratedTeachers = ratedTeachers;
	}
	public Schedule gettSchedule() {
		return this.schedule;
	}
 

	// Student methods	
	public List <Lesson> getSchedule() {
		List <Lesson> lessons = new ArrayList <>();
		for(Teacher teacher: DataBase.users.get(TypeUser.TEACHER).stream().map(u-> (Teacher) u).filter(t-> (t.getLessons().values().stream().filter(v->v.contains(this)).findFirst().orElse(null) != null)).collect(Collectors.toList())) {
			for(Lesson lesson: teacher.getLessons().keySet()) {
				if(teacher.getLessons().get(lesson).contains(this) == true) {
					lessons.add(lesson);
				}
			}
		}
		return lessons;
	}

	public boolean registerToCourse(Course course) throws IOException {
	    if(this.getCourses().contains(course)) return false;
	    if(this.getTranscript().getSemesters().isEmpty() == true) {
	      this.getTranscript().getSemesters().add(new Semester(1));
	    }
	    if((this.getTranscript().getSemesters().lastElement().getCredits() + course.getCredits()) > 21) return false;
	    this.getCourses().add(course);
	    this.getTranscript().getSemesters().lastElement().addCredits(course.getCredits());
	    DataBase.serilaizeUsers();
	    return true;
	  }
	
	public boolean DrawUpSchedule(Teacher teacher, Lesson lesson) throws IOException {
		try {
			teacher.getLessons().computeIfAbsent(lesson, k -> new Vector<Student>()).add(this);
			DataBase.serilaizeUsers();
			return true;
		} 
		catch(Exception e) {return false;}
	}
	
	public void sendRequest(Request request) throws IOException {
		DataBase.requests.add(request); DataBase.serilaizeRequests(); DataBase.serilaizeUsers();
	}

	public void doResearch(Research research) throws IOException {
		DataBase.researches.add(research); DataBase.serilaizeResearches();
	}

    public Literature getBook() {
        return null;
    }
    
	public String idGenerator() throws IOException {
		DataBase.cnt = DataBase.cnt + 1;
		DataBase.serilaizeId();
		return String.valueOf(LocalDate.now().getYear() - this.year +1).substring(2) + DataBase.idStudDegree.get(this.getDegree()) + "0".repeat(5- Integer.toString(DataBase.cnt).length()) + (DataBase.cnt -1);
	}
	
	public Vector <News> seeNews(String type){
		return DataBase.news.get(type);
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
				"Year of study: " + this.year + "\nTotal GPA: " + this.GPA + "\n-\n" + 
				"Faculty: " + this.faculty + "\nDegree: " + this.degree + (this.researchStatus ? "\n-\nH-index: " + this.hIndex : "");
	}
	public String toString() {
		return super.toString() + "\n-\n" + 
			   "Year of study: " + this.year + "\nTotal GPA: " + this.GPA + "\n-\n" + 
			   "Faculty: " + this.faculty + "\nDegree: " + this.degree + (this.researchStatus ? "\n-\nH-index: " + this.hIndex : "");
	}
}

