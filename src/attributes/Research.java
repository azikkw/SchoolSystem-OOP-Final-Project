package attributes;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import users.User;

/**
 */
public class Research implements Serializable
{
	private static final long serialVersionUID = -3429241527029990779L;

	private String name;
    private String description;
    
	private Vector <User> researchers;
    
    {
    	researchers = new Vector <User>();
    }
    
    
    public Research() {}
    
	public Research(String name, String description) {
		this.name = name;
		this.description = description;
	}

	
	// Getters/Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Vector <User> getResearchers() {
		return researchers;
	}
	
	public List <String> sysResearchers() {
		List <String> res = this.researchers.stream().map(r -> r.getFirstName()).collect(Collectors.toList()); return res;
	}
	public String toString() {
		return this.name + ", authors: " + this.sysResearchers();
	}
}

