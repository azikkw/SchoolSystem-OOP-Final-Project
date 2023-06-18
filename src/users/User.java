package users;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;


public abstract class User implements Cloneable, Comparable <User>, Serializable
{
	private static final long serialVersionUID = -3210816682560011695L;
	/** users have unique id from generator id 
	 */
	private String id;
    private String password;
    
    private String firstName;
    private String lastName;

    private int age;

    private boolean status;
    
    {
    	status = true;
    }
    
    public User() {}
   
    public User(String password, String firstName, String lastName, int age) {
    	this.password = password;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.age = age;
    	
    }
    

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null) return false;
		if(this.getClass() != o.getClass()) return false;
		
		User u = (User)o;
		
		return this.id.equals(u.id);
	}
	public int hashCode() {
		return Objects.hash(id);
	}

	public int compareTo(User u) {
	     return id.compareTo(u.getId());
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public String forProfile() {
		 return "User ID: "  + this.id + "\n-\n" + 
				"First name: " + this.firstName + "\nLast name: " + this.lastName + "\nAge: " + this.age;
	}
	public String toString() {
		return "User ID: "  + this.id + "\n-\n" + 
			   "First name: " + this.firstName + "\nLast name: " + this.lastName;
	}
	
	public abstract String idGenerator() throws IOException;
}