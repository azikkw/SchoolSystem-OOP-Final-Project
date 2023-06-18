package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Vector;

import attributes.Action;
import attributes.DataBase;
import attributes.Literature;
import attributes.Message;
import attributes.Request;
import enums.TypeUser;

public class LibrarianController
{
	static Librarian librarian = null; static Dean dean = null;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void logIn(Librarian l) throws IOException {
		librarian = l;
		viewMenu();
	}

    static void viewMenu() throws IOException {
    	while(true) {
	    	System.out.println("\nMenu:\n" +
	    						"1. My profile\n" +
	    						"2. View user information\n" +
	    						"3. Research\n" +
	    						"4. View researches\n" +
	    						"5. Given books\n" +
	    						"6. Aviable books\n" +
	    						"7. Update available books\n" +
	    						"8. Send request\n" +
	    						"9. See news\n" + 
	    						"10. Send message\n" + 
	    						"11. Read my messages\n" + 
	    						"12. Log-out\n");
	    	
			String choosen = br.readLine();
			
			switch(choosen) {
			case "1":
				UserController.viewProfile(librarian);
				break;
			case "2":
				viewInfo();
				break;
			case "3":
				EmployeeController.doResearch(librarian);
				break;
			case "4":
				EmployeeController.seeResearches(librarian);
				break;
			case "5":
				System.out.println(librarian.getGivenBooksList());
				break;
			case "6":
				System.out.println(librarian.getAvailableBooks());
				break;
			case "7":
				System.out.print("\nEnter book's name: ");
				String title = br.readLine();
				System.out.print("\nEnter book's author: ");
				String  author= br.readLine();
				System.out.print("\nEnter number of pager: ");
				int pages= Integer.parseInt(br.readLine());
				librarian.setAvailableBooks(new Literature(title, author, pages));
				break;
			case "8":
				sendRequest();
				break;
			case "9":
				System.out.println("\nChoose news column:");
				System.out.println("1. Official\n"
				      		+ "2. Upcoming events\n"
				    		+ "3. Lost and found\n");
				String type = br.readLine();
				if(type.equals("1")) type = "Official";
				if(type.equals("2")) type = "Upcoming events";
				if(type.equals("3")) type = "Lost and found";
				if(librarian.seeNews(type).isEmpty()) System.out.println("There is no event\n");
				else System.out.println(librarian.seeNews(type));
				break;
			case "10":
				System.out.println("\nChoose an employee position from the list:");
				System.out.println("TEACHER, MANAGER, DEAN, LIBRARIAN");
		    	String position= br.readLine().toUpperCase();
		    	try {
					enums.TypeUser.valueOf(position);
				}
				catch(IllegalArgumentException e){
						System.out.println("Error, no such variant! Enter again:  ");
						position = br.readLine().toUpperCase();
				}
				System.out.println("Enter name of the employee");
				String name = br.readLine();
				System.out.println("Enter surname of the employee");
				String surname = br.readLine();
				System.out.println("Enter id of employee");
				Vector<User> receivers = librarian.findEmployeeList(position, name, surname);
				System.out.println(receivers);
				String id = br.readLine();
				Employee e = librarian.findEmployee(id, receivers);
				if(e != null) {
					System.out.println("Enter your message");
					String message = br.readLine();
					librarian.sendMessage(e, new Message(librarian.getFirstName() + " " + librarian.getLastName(), message, LocalDate.now()));
				}
				else {
					System.out.println("There is no such employee!");
				}
				break;
			case "11":
				System.out.println(librarian.getMyMessages());
			case "12":
				System.out.println();
				Admin.getAdmin().setLogFiles(new Action(LocalDate.now(), librarian, "logged out"));
				Admin.serilaizeLogFiles();
				DataBase.serilaizeUsers();
				Librarian.serilaizeAvailableBooks();
				Librarian.serilaizeGivenBooks();
				return;
			default:
				System.out.println("\nWe don't have such an index. Please select again:");
			}
    	}
    }
    
    public static Librarian getLibrarian() throws IOException{
    	System.out.println("\nEnter password:   ");
    	String password = br.readLine();
    	System.out.println("\nEnter first name:   ");
    	String firstName = br.readLine();
    	System.out.println("\nEnter last name:   ");
    	String lastName = br.readLine();
    	System.out.println("\nEnter age:   ");
    	int age = Integer.parseInt(br.readLine());
    	Librarian l = new Librarian(password, firstName, lastName, age);
    	System.out.print("Id of new librarian is:  " + l.getId() + "\n");
    	return l;
    }
    
    public static void viewInfo() throws IOException {
		System.out.println("\n1. Enter the ID of the student to view information about him\n2. Enter back for return");

    	while(true) {
    		System.out.print("\nEnter ID or 'back': ");
    		String userId = br.readLine();
    		
    		boolean found = false;
		
			if(userId.toLowerCase().equals("back")) return;
			for(User u: Librarian.givenBooks.keySet()) {
				if(u.getId().toLowerCase().equals(userId.toLowerCase())) {
					found = true;
					System.out.println(u + "\n-\n" + "Given bools: ");
				}
			}
			
			if(!found) System.out.println("There is no such user in the list.");
    	}
    }
    
    public void addBook() {
    }   
	
	public static void sendRequest() throws IOException {
		System.out.println("\nWho do you want to send the request to:\nTo return back, enter - back\n\n" +
				  		   "1. For dean, enter - d\n2. For admin, enter - a");
		
		while(true) {
			System.out.print("\nEnter user or 'back': ");
			String user = br.readLine();
			
			if(user.toLowerCase().equals("back")) return;
			if(user.toLowerCase().equals("d")) {
				System.out.println("\nYou can only send a request to the dean for dismissal.\n\nChoose dean from list:");
				
				int cnt = 1;
				for(User u: DataBase.users.get(TypeUser.DEAN)) {
					if(u instanceof Dean) {
						Dean d = (Dean)u;
						System.out.println(cnt + ". " + d.getFirstName() + ", ID: " + d.getId() + ". Faculty: " + d.getFaculty()); 
						cnt += 1;
					}
				}
				
				System.out.print("\nEnter ID of dean or 'back': ");
				String deanId = br.readLine();
				
				if(deanId.toLowerCase().equals("back")) break;
				for(User u: DataBase.users.get(TypeUser.DEAN)) {
					if(u instanceof Dean && deanId.toLowerCase().equals(u.getId())) dean = (Dean)u;
				}
				
				System.out.print("\nWrite the reason for your dismissal: ");
				
				String requestMess = br.readLine();
				if(requestMess.toLowerCase().equals("back")) break;
				
				librarian.sendRequest(new Request(librarian, dean, "rem", requestMess));
				System.out.println("\nSuccessfully sent !");
			}
			if(user.toLowerCase().equals("a")) {
				System.out.print("\nYou can send a request to the admin only to change the password.\nEnter a new password or 'back': ");
				String requestMess = br.readLine();
				if(requestMess.toLowerCase().equals("back")) break;
				librarian.sendRequest(new Request(librarian, dean, "cp", requestMess));
				System.out.println("\nSuccessfully sent !");
			}
		}
	}
}