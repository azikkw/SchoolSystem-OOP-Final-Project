package users;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import attributes.*;
import interfaces.Researchable;

/**
 */
public abstract class Employee extends User implements Cloneable, Serializable, Researchable
{
	private static final long serialVersionUID = -6930497298631431512L;

	private LocalDate hireDate;

	private double hIndex;
    private boolean researchStatus;

    private Vector <Message> myMessages = new Vector <Message>();
	private Vector <Literature> books;
    
    {
    	hireDate = LocalDate.now();
    	hIndex = 0;
    	researchStatus = false;
    	books = new Vector<>();
    	try {this.setId(this.idGenerator());} 
    	catch(IOException e) {e.printStackTrace();}
    }
    
    public Employee(String password, String firstName, String lastName, int age) { 
		super(password, firstName, lastName, age);
	}
    
	public LocalDate getHireDate() {
		return hireDate;
	}
	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}
	public boolean isResearchStatus() {
		return researchStatus;
	}
	public void setResearchStatus(boolean researchStatus) {
		this.researchStatus = researchStatus;
	}
	public double gethIndex() {
		return hIndex;
	}
	public void sethIndex(double hIndex) {
		this.hIndex = hIndex;
	}
	public Vector <Literature> getBooks() {
		return books;
	}
	public void setBooks(Vector<Literature> books) {
		this.books = books;
	}
	public String idGenerator() throws IOException {
		DataBase.cnt = DataBase.cnt +1;
		DataBase.serilaizeId();
		return String.valueOf(hireDate.getYear()).substring(2) + "EM" +  "0".repeat(5- Integer.toString(DataBase.cnt).length()) + (DataBase.cnt -1);
	}
	public Vector<News> seeNews(String type){
		return DataBase.news.get(type);
	}
	public Vector<Message> getMyMessages() {
		return myMessages;
	}
	
	
	public void doResearch(Research research) throws IOException {
		DataBase.researches.add(research); DataBase.serilaizeResearches();
	}
	
	public void sendMessage(Employee e, Message m) {
		e.getMyMessages().add(m);
	}
	
	public Vector<User> findEmployeeList(String type, String name, String surname) {
		Vector<User> list = new Vector<>();
		for(User u: DataBase.users.get(enums.TypeUser.valueOf(type))) {
			if(u.getFirstName().equals(name) && u.getLastName().equals(surname)) {
				list.add(u);
			}
		}
		return list;
	}
	
	public Employee findEmployee(String id, Vector<User> receivers) {
		Optional<User> optional = receivers.stream().filter(n -> n.getId().equals(id)).findFirst();
		if(optional.isPresent()) {
			Employee u = (Employee)optional.get();
			return u;
		}
		return null;
	}
    
	
	// Standard methods
    public boolean equals(Object o) {
    	if(!super.equals(o)) return false;
    	
    	Employee e = (Employee)o;
    	
    	return this.hireDate.equals(e.hireDate) && this.myMessages.equals(e.myMessages);
    }
	public int hashCode() {
		return Objects.hash(super.getId());
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public String forProfile() {
		return super.forProfile() + "\n-\n" +
			   "Hire date: " + this.hireDate + (this.researchStatus ? "\n-\nH-index: " + this.hIndex : "");
	}
	public String toString() {
		return super.toString() + "\n-\n" +
			   "Hire date: " + this.hireDate + (this.researchStatus ? "\n-\nH-index: " + this.hIndex : "");
	}
}

