package users;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Vector;
import java.util.Map.Entry;
import attributes.Action;
import attributes.DataBase;
import enums.TypeUser;
import interfaces.Requestable;

public final class Admin extends User implements Cloneable, Serializable, Requestable
{
	private static final long serialVersionUID = 7786797688270823464L;
    private static final Admin admin = new Admin();
    
	private static Vector <Action> logFiles;
    
    {
    	logFiles = new Vector <>();
    	try {this.setId(this.idGenerator());} 
    	catch(IOException e) {e.printStackTrace();}
    }
    
    private Admin() {}
    
    public Admin(String password, String firstName, String lastName, int age) {
    	super(password, firstName, lastName, age);
    }

    
    // Getters/Setters
    public static Admin getAdmin() {
        return admin;
    }
	public Vector <Action> seeLogFiles() {
		return logFiles;
	}
	public void setLogFiles(Action action) {
		Admin.logFiles.add(action);
	}
	public String idGenerator() throws IOException {
		return "admin";
	}
    
	
    // Administrator methods
    public void addUser(TypeUser type,User user) {
		(DataBase.users.get(type)).add(user); 
	}
	
	public static User search(String id) {
		for(Entry <TypeUser, HashSet <User> > e: DataBase.users.entrySet()) {
			Optional <User> optional = e.getValue().stream().filter(n -> n.getId().equals(id)).findFirst();
			if(optional.isPresent()) {User u = optional.get(); return u;}
		}
		return null;
	}

	public void updatePassword(String id, String newPassword) {
		search(id).setPassword(newPassword); 
	}

	public void removeUser(String id) {
		User u = search(id);
		String userType = u.getClass().getName().substring(6).toUpperCase();
		DataBase.users.get(enums.TypeUser.valueOf(userType)).remove(u);
	}

	public void blockUser(String id) {
			search(id).setStatus(false);
	}
    
	public boolean seeRequest(User user, String requestType, String requestMess) throws IOException {
		boolean found = false;
		for(TypeUser tu: DataBase.users.keySet()) {
			for(User u: DataBase.users.get(tu)) {
				if(u.getId().equals(requestMess) || u.getId().equals(user.getId())) {
					found = true;
					if(requestType.toLowerCase().equals("cp")) u.setPassword(requestMess);
					if(requestType.toLowerCase().equals("bu")) u.setStatus(false);
					if(requestType.toLowerCase().equals("unl")) u.setStatus(true);
					if(requestType.toLowerCase().equals("rem")) removeUser(u.getId());
				}
			}
		} DataBase.serilaizeUsers(); return found;
	}
	
	
	public static void serilaizeLogFiles() throws IOException {
	    try {
	      FileOutputStream fos = new FileOutputStream("logFiles.out");
	      ObjectOutputStream log = new ObjectOutputStream(fos);
	      log.writeObject(logFiles); log.flush(); log.close(); fos.close();
	    } 
	    catch(Exception e) {e.getStackTrace();}
	}
	public static Vector<Action> deserializeLogFiles() throws IOException, ClassNotFoundException {
	    try {
	      FileInputStream fis = new FileInputStream("logFiles.out");
	      ObjectInputStream log = new ObjectInputStream(fis);
	      logFiles = (Vector<Action>) log.readObject();
	      log.close(); fis.close(); 
	    } 
	    catch(Exception e) {e.getStackTrace();}
	    
	    return logFiles;
	}
    
    
    // Standard methods
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	public int hashCode() {
		return super.hashCode();
	}

	public int compareTo(User u) {
		return super.compareTo(u);
	}

	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
    public String forProfile(){
    	return super.forProfile();
    }
	public String toString() {
		return super.toString();

	}
}

