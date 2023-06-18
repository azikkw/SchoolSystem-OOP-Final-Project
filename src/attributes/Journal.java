package attributes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

/**
 */
public class Journal implements Serializable
{
	private static final long serialVersionUID = -7133292178588222345L;

	private HashMap <Course, Vector <Mark> > marks;
    
    {
      marks = new HashMap<Course, Vector<Mark> >();
    }
    
    
    public Journal() {}
    
    
    public HashMap <Course, Vector <Mark> > getMarks() {
    	return marks;
    }
    public void setJournal(HashMap <Course, Vector <Mark> > marks) {
    	this.marks = marks;
    }
  
  
  
    public double getTotal(Course course) {
      double total = 0;
      for(Course c : marks.keySet()) {
        if(c.getName().equals(course.getName()) == false) continue;
        for(Mark m : marks.get(c)) {
          total+=m.getMark();
        }
      }
      System.out.println(total);
      return total;
    }
    
    
    public void clear() {
      marks = new HashMap<Course, Vector<Mark> >();
    }
}

