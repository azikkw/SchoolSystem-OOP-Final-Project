package attributes;

import java.time.LocalDate;

import users.User;

public class Action {
    LocalDate date;
    User executor;
    String action;
    
    public Action() {
    	
    }
    
    public Action(LocalDate date, User user, String action) {
    	super();
    	this.date = date;
    	this.executor = user;
    	this.action = action;
    } 
    
	@Override
	public String toString() {
		return "Date = " + date + ", executor = " + executor + ", action = " + action + "\n";
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public User getExecutor() {
		return executor;
	}

	public void setExecutor(User user) {
		this.executor = user;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
