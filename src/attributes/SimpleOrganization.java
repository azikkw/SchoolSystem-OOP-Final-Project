package attributes;

import java.io.Serializable;
import java.util.Vector;

import interfaces.Organization;
import users.Student;


/**
 */
public class SimpleOrganization implements Organization, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3417654893571714986L;
	/**
     */
	public String name;
	public Vector<Student> list = new Vector<>();

    /**
     */
    public SimpleOrganization() {
		
    	
    }
    public SimpleOrganization(String name) {
		this.name = name;
    }

	@Override
	public String selection() {
		// TODO Auto-generated method stub
		return "Must be a KBTU student.";
	}

	@Override
	public String getBonus() {
		// TODO Auto-generated method stub
		return "New acquaintances.";
	}

	
	public String toString() {
		return "Name: " + this.name;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}

