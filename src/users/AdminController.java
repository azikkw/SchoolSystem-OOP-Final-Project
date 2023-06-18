package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Map.Entry;

import attributes.Action;
import attributes.DataBase;
import attributes.Request;
import enums.TypeUser;

public class AdminController 
{
	public static Admin admin;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static boolean isIdExits(String id) {
		for(Entry <TypeUser, HashSet <User> > e: DataBase.users.entrySet()) {
			Optional <User> optional = e.getValue().stream().filter(n -> n.getId().equals(id)).findFirst();
			if(optional.isPresent()) return true;
		}
		return false;
	}
	
	public static void logIn(Admin a) throws IOException {
		admin = a;
		viewMenu();
	}
		
	public static void viewMenu() throws IOException {
		while(true) {
			System.out.println("\nMenu:\n" +
				                "1. My profile\n" +
				                "2. View user information\n" +
				                "3. Add user\n" +
				                "4. Remove user\n" +
				                "5. Update user\n" +
				                "6. Block user\n" +
				                "7. Received requests\n" +
				                "8. See log files\n" + 
				                "9. Log out\n");
		      
		    String choosen = br.readLine();
		    
		    switch(choosen) {
			case "1":
				UserController.viewProfile(admin);
				break;
			case "2":
				viewInfo();
				break;
			case "3":
			    System.out.print("Choose the type of user: \n");
			    System.out.println("1. Student\n"
			      				+ "2. Teacher\n"
			      				+ "3. Dean\n"
			      				+ "4. Librarian\n"
			      				+ "5. Manager\n");
			      
			    String type = br.readLine();
			    User u = null;
			    
			    switch(type) {
			      	case "1":
			      		u = StudentController.getStudent();
			      		break;
			      	case "2":
			      		u = TeacherController.getTeacher();
			      		break;
			      	case "3":
			      		u = DeanController.getDean();
			      		break;
			      	case "4":
			      		u = LibrarianController.getLibrarian();
			      		break;
			      	case "5":
			      		u = ManagerController.getManager();
			      		break;
			      	default:
						System.out.println("\nWe don't have such an index. Please select again:");
			    }
			    if(u != null) {
			    	String userType = u.getClass().getName().substring(6).toUpperCase();
			    	admin.addUser(enums.TypeUser.valueOf(userType), u);
			    	DataBase.serilaizeUsers();
			    }
			    break;
		    case "4":
		    	System.out.print("\nEnter id: ");
		    	String id = br.readLine();
		    	while(!isIdExits(id)) {
		    		System.out.println("\nUser with this id does not exist! Enter id again or enter \"back\":  ");
		    		id = br.readLine();
		    		if(id.equals("back")) break;
		    	}
		    	if(isIdExits(id)) admin.removeUser(id);
		    	DataBase.serilaizeUsers();
		    	break;
		    case "5":
		    	System.out.print("\nEnter id: ");
		    	id = br.readLine();
		    	while(!isIdExits(id)) {
		    		System.out.print("\nUser with this id does not exist! Enter id again or enter \"back\":   ");
		    		id = br.readLine();
		    		if(id.equals("back")) break;
		    	}
		    	if(isIdExits(id)) {
			    	System.out.print("\nEnter new password: ");
			    	String newPassword = br.readLine();
			    	admin.updatePassword(id, newPassword);
		    	}
		    	DataBase.serilaizeUsers();
		        break;
		    case "6":
		    	System.out.print("\nEnter id: ");
		    	id = br.readLine();
		    	while(!isIdExits(id)) {
		    		System.out.print("\nUser with this id does not exist! Enter id again or enter \"back\":   ");
		    		id = br.readLine();
		    		if(id.equals("back")) break;
		    	}
		    	if(isIdExits(id)) admin.blockUser(id);
		    	DataBase.serilaizeUsers();
		    	break;
		    case "7":
		    	seeRequest();
		    	break;
		    case "8":
			    System.out.println("\nUsers actions: "); int cnt = 1;
			    for(Action a: admin.seeLogFiles()) {
			    	System.out.println(cnt + ". '" + a.getAction() + "' user " + a.getExecutor().getFirstName() + " with ID: " + a.getExecutor().getId());
			    	cnt += 1;
			    }
		    	break;
		    case "9":
		    	System.out.println();
		    	Admin.getAdmin().setLogFiles(new Action(LocalDate.now(), admin, "logged out"));
		    	DataBase.serilaizeUsers();
		    	return;
			default:
				System.out.println("\nWe don't have such an index. Please select again:");
			}
		}
	}
    
    public static void viewInfo() throws IOException {
		System.out.println("\n1. Enter the ID of the user to view information about him\n2. Enter back for return");

    	while(true) {
    		System.out.print("\nEnter ID or 'back': ");
    		String userId = br.readLine();
    		
    		boolean found = false;
		
			if(userId.toLowerCase().equals("back")) return;
			for(TypeUser tu: DataBase.users.keySet()) {
				for(User u: DataBase.users.get(tu)) {
					if(u.getId().toLowerCase().equals(userId.toLowerCase())) {
						found = true;
						System.out.println("\nUser type: " + tu + "\n-\nUser password: " + u.getPassword() + "\n" + u.forProfile());
					}
				}
			}
			
			if(!found) System.out.println("There is no such user in the system.");
    	}
    }
    
    public static void seeRequest() throws IOException {
    	boolean found = false; int requestsNumb = 0;
    	for(int i = 0; i < DataBase.requests.size(); i++) {
    		if(DataBase.requests.get(i).getTo().getId().equals(Admin.getAdmin().getId())) {
    			found = true; requestsNumb += 1;
    		}
    	} 
    	if(!found) System.out.println("\nAt the moment, you have not received a single request !");
    	else {
	    	System.out.println("\nTo return back, enter - back.\nNumber of requests received: " + requestsNumb);
    		while(requestsNumb != 0) {
		    	for(int i = 0; i < DataBase.requests.size(); i++) {
	        		if(DataBase.requests.get(i).getTo().getId().equals(Admin.getAdmin().getId())) {
			    		Request req = DataBase.requests.get(i);
			    		String answer = "";
			    		
			    		switch(req.getRequestType().toLowerCase()) {
			    		case "cp":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: password change\n2. New password: " + req.getRequestMess());
			    			System.out.print("\nEnter 'change' to change password: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); return;}
			    			if(answer.toLowerCase().equals("change")) {
			    				if(Admin.getAdmin().seeRequest(req.getFrom(), req.getRequestType(), req.getRequestMess())) {
			    					System.out.println("\nRequest successfully processed !");
			    					DataBase.requests.remove(i);
			    				} 
			    			}  requestsNumb -= 1;
			    			break;
			    		case "bu":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: block user\n2. User ID: " + req.getRequestMess());
			    			System.out.print("\nEnter 'block' to block user: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); return;}
			    			if(answer.toLowerCase().equals("block")) {
			    				if(Admin.getAdmin().seeRequest(req.getFrom(), req.getRequestType(), req.getRequestMess())) {
			    					System.out.println("\nRequest successfully processed !");
			    				} else System.out.println("\nThere is no such user in the system.");
			    				DataBase.requests.remove(i);
			    			} requestsNumb -= 1;
			    			break;
			    		case "unl":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: unlock user\n2. User ID: " + req.getRequestMess());
			    			System.out.print("\nEnter 'unlock' to unlock user: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); return;}
			    			if(answer.toLowerCase().equals("unlock")) {
			    				if(Admin.getAdmin().seeRequest(req.getFrom(), req.getRequestType(), req.getRequestMess())) {
			    					System.out.println("\nRequest successfully processed !");
			    				} else System.out.println("\nThere is no such user in the system.");
			    				DataBase.requests.remove(i);
			    			} requestsNumb -= 1;
			    			break;
			    		case "rem":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: remove user\n2. User ID: " + req.getRequestMess());
				    		System.out.print("\nEnter 'remove' to remove user: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); return;}
			    			if(answer.toLowerCase().equals("remove")) {
			    				if(Admin.getAdmin().seeRequest(req.getFrom(), req.getRequestType(), req.getRequestMess())) {
			    					System.out.println("\nRequest successfully processed !");
			    				} else System.out.println("\nThere is no such user in the system.");
			    				DataBase.requests.remove(i);
			    			} requestsNumb -= 1;
			    			break;
			    		default:
							System.out.println("\nThe sent request does not match the type !");
							DataBase.requests.remove(i); requestsNumb -= 1;
							break;
			    		}
			    	}
	        		
		    		if(requestsNumb == 0) System.out.println("\nYou have answered all the sent requests.");
		        	DataBase.serilaizeRequests();
	    		}
    		}
		}
    }
	}
