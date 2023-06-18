package attributes;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class News  implements Serializable, Comparable<News>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7221925647723337030L;
	private GregorianCalendar date;
	private String content;
	private final int id;
	
	{
		id = DataBase.cnt++;
	}
	public News() {
		
	}
	public News(GregorianCalendar date, String content) {
		super();
		this.date = date;
		this.content = content;
	}
	public GregorianCalendar getDate() {
		return date;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Id:  "+id + "\n   Date:  " + date.get(Calendar.YEAR) + " " + date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + "-" + date.get(Calendar.DAY_OF_MONTH) +" "
											+ date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE)+ "\n   Content:  " + content + "\n";
	}
	@Override
	public int hashCode() {
		return Objects.hash(date);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		News other = (News) obj;
		return date.equals(other.date);
	}
	public int compareTo(News news) {
		return date.compareTo(news.date);
	}
	public int getId() {
		return id;
	}
	
	
	
}
