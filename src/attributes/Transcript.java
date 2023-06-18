package attributes;

import java.io.Serializable;
import java.util.Vector;

/**
 */
public class Transcript implements Serializable
{
	private static final long serialVersionUID = 8880137574232282605L;

    private double totalGPA;
    private int totalCredits;
    
	private Vector < Semester > semesters;

	{
	    semesters = new Vector<>();
	    totalGPA = 0;
	    totalCredits = 0;
	}
    
    public Transcript() {}

		  
	public Vector<Semester> getSemesters() {
		return semesters;
	}
	public void setSemesters(Vector<Semester> semesters) {
		this.semesters = semesters;
	}
	public double getTotalGPA() {
	    return semesters.stream().map(s->s.getCredits() * s.getGPA()).mapToDouble(Double::valueOf).sum()/getTotalCredits();
	}
	public int getTotalCredits() {
	    return semesters.stream().map(s->s.getCredits()).mapToInt(Integer::valueOf).sum();
	}
}