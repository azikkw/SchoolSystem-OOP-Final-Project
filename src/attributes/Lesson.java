package attributes;

import java.util.Objects;
import java.io.Serializable;
import java.time.DayOfWeek;
import enums.TypeLesson;


/**
 */
public class Lesson implements Serializable, Cloneable, Comparable <Lesson>
{
	private static final long serialVersionUID = -3948456844746305207L;
	
	private Course course;

	private TypeLesson type;
	private DayOfWeek day;
	
	private int begin;
    private int duration;
    private int room;
    
    {
    	setBegin(-1);
    	setDay(null);
    }
    
    
   public Lesson() {}
   
	public Lesson(Course course, TypeLesson type, int duration) {
		this.course = course;
		this.type = type;
		this.duration = duration;
		this.room = 0;
	}
	
	
	public TypeLesson getType() {
		return type;
	}
	public void setType(TypeLesson type) {
		this.type = type;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getRoom() {
		return room;
	}
	public void setRoom(int room) {
		this.room = room;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	public DayOfWeek getDay() {
		return day;
	}
	public void setDay(DayOfWeek day) {
		this.day = day;
	}
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}


	public boolean equals(Object obj) {
		Lesson other = (Lesson) obj;
		return this.getCourse().getName().equals(other.getCourse().getName()) && this.getType()==other.getType();
	}
	public int hashCode() {
		return Objects.hash(begin, course, day, duration, room, type);
	}
	
	public Object clone() throws CloneNotSupportedException {
		Lesson other = (Lesson) super.clone();
		other.setCourse(course);
		return other;
	}

	public String getDescription() {
		return this.getCourse().getName() + ": " + this.getType() + ", " + this.getDay() + ", " + this.getBegin() + "-" + (this.getBegin()+this.getDuration());
	}
	public String toString() {
		return this.getCourse().getName() + ": " + this.getType() + ", " + this.getBegin() + "-" + (this.getBegin()+this.getDuration());
	}
	
	public int compareTo(Lesson l) {
		if(this.getBegin() > l.getBegin()) return 1;
		if(this.getBegin() < l.getBegin()) return -1;
		return 0;
	}
}

