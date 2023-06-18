package attributes;

import enums.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;
import enums.Faculty;

/**
 */
public class Course implements Serializable
{
	private static final long serialVersionUID = -1747230880342195123L;

    private final String name;
    private int credits;

    private TypeCourse type;
    private Faculty faculty;

    private Vector <Lesson> lessons;

    {
    	lessons = new Vector <>();
    }
    
    
    public Course(String name, int credits, TypeCourse type, Faculty faculty) {
		super();
		this.name = name;
		this.credits = credits;
		this.type = type;
		this.faculty = faculty;
	}
    
    public Course(Course course, Vector <Lesson> lessons) {
		super();
		this.name = course.getName();
		this.credits = course.getCredits();
		this.type = course.getType();
		this.faculty = course.getFaculty();
		this.lessons = lessons; 
	}

    
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public TypeCourse getType() {
		return type;
	}
	public void setType(TypeCourse type) {
		this.type = type;
	}
	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	public Vector <Lesson> getLessons() {
		return lessons;
	}
	public String getName() {
		return name;
	}

	
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null) return false;
		if(getClass() != o.getClass()) return false;
		
		Course other = (Course)o;
		
		return credits == other.credits && faculty == other.faculty && Objects.equals(lessons, other.lessons) && Objects.equals(name, other.name) && type == other.type;
	}
	public int hashCode() {
		return Objects.hash(credits, faculty, lessons, name, type);
	}
	
	public String toString() {
		return name + ": Credits number: " + credits + ", Type: " + type + ", Faculty: " + faculty;
	}
}

