package attributes;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Vector;

public class Schedule implements Serializable
{
	private static final long serialVersionUID = 6409133660976214577L;

	private DayOfWeek weekDay;
	
	private Vector <String> lessons;
	
	{
		lessons = new Vector <String>();
	}

	public Schedule() {}
	
	public Schedule(DayOfWeek weekDay) {
		this.weekDay = weekDay;
	}
	
	
	// Getters/Setters
	public DayOfWeek getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(DayOfWeek weekDay) {
		this.weekDay = weekDay;
	}
	public Vector <String> getLessons() {
		return lessons;
	}

	public String sysWeekDay() {
		return " " +  weekDay + "";
	}
}