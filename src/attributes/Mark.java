package attributes;

import java.util.GregorianCalendar;

import users.Teacher;

import java.io.Serializable;
import java.util.Calendar;

/**
 */
public class Mark implements Serializable
{
  /** fields give full information about mark
	 */
	private Teacher teacher;
    private double mark;
    private String description;
    private GregorianCalendar date;
    String month[] = { "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec" };

    public Mark() {
      
    }
    
    public Mark(Teacher t, double mark, GregorianCalendar date, String description) {
      this.setTeacher(t);
    this.setMark(mark);
    this.setDate(date);
    this.setDescription(description);
  }

  /**
     */

    public double getMark() {
    return mark;
  }

  public void setMark(double mark) {
    this.mark = mark;
  }

  public GregorianCalendar getDate() {
    return date;
  }

  public void setDate(GregorianCalendar date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Mark =" + mark + ", description = " + description + ", date = " + month[date.get(Calendar.MONTH)] + " " + date.get(Calendar.DATE) + ", " +  date.get(Calendar.YEAR) + ", by " + teacher.getLastName();
  }
  
  public void setTeacher(Teacher t) {
    this.teacher = t;
  }
}

