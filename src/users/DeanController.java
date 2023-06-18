package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Vector;

import attributes.Action;
import attributes.Course;
import attributes.DataBase;
import attributes.Lesson;
import attributes.Mark;
import attributes.Message;
import attributes.Request;
import enums.TypeUser;
import interfaces.*;

public class DeanController implements StudentViewable, Viewable
{
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static Dean dean;
	
	public static void logIn(Dean d) throws IOException {
		dean = d;
		viewMenu();
	}

	static void viewMenu() throws IOException {
    	while(true) {
	    	System.out.println("\nMenu:\n" +
	    						"1. My profile\n" +
	    						"2. View user information\n" +
	    						"3. Teachers rate\n" +
	    						"4. Research\n" +
	    						"5. View researches\n" +
	    						"6. Student/Teacher courses\n" +
	    						"7. Student journal\n" +
	    						"8. Student/Teacher schedule\n" +
	    						"9. Student attestation\n" +
	    						"10. Student transcript\n" +
	    						"11. Send request\n" +
	    						"12. Received requests\n" +
	    						"13. See news\n" + 
	    						"14. Send message\n" + 
	    						"15. Read my messages\n" + 
	    						"16. Log-out\n");
	    	
			String choosen = br.readLine();
			
			switch(choosen) {
			case "1":
				UserController.viewProfile(dean);
				break;
			case "2":
				viewInfo();
				break;
			case "3":
				viewTeacherRate();
				break;
			case "4":
				EmployeeController.doResearch(dean);
				break;
			case "5":
				EmployeeController.seeResearches(dean);
				break;
			case "6":
				viewStudentCourses();
				break;
			case "7":
				viewJournal();
				break;
			case "8":
				viewSchedule();
				break;
			case "9":
				viewAttestation();
				break;
			case "10":
				break;
			case "11":
				sendRequest();
				break;
			case "12":
				seeRequest();
				break;
			case "13":
				System.out.println("Choose news column:");
				System.out.println("1. Official\n"
				      		+ "2. Upcoming events\n"
				    		+ "3. Lost and found\n");
				String type = br.readLine();
				if(type.equals("1")) type = "Official";
				if(type.equals("2")) type = "Upcoming events";
				if(type.equals("3")) type = "Lost and found";
				if(dean.seeNews(type).isEmpty()) System.out.println("There is no event\n");
				else System.out.println(dean.seeNews(type));
				break;
			case "14":
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
				Vector<User> receivers = dean.findEmployeeList(position, name, surname);
				System.out.println(receivers);
				String id = br.readLine();
				Employee e = dean.findEmployee(id, receivers);
				if(e != null) {
					System.out.println("Enter your message");
					String message = br.readLine();
					dean.sendMessage(e, new Message(dean.getFirstName() + " " + dean.getLastName(), message, LocalDate.now()));
				}
				else {
					System.out.println("There is no such employee!");
				}
				break;
			case "15":
				System.out.println(dean.getMyMessages());
				break;
			case "16":
				System.out.println();
				Admin.getAdmin().setLogFiles(new Action(LocalDate.now(), dean, "logged out"));
				Admin.serilaizeLogFiles();
				DataBase.serilaizeUsers();
				return;
			default:
				System.out.println("\nWe don't have such an index. Please select again:");
			}
    	}
    }
	
	public static Dean getDean() throws IOException{
    	System.out.print("\n1. Enter password: ");
    	String password = br.readLine();
    	System.out.print("\n2. Enter first name: ");
    	String firstName = br.readLine();
    	System.out.print("\n3. Enter last name: ");
    	String lastName = br.readLine();
    	System.out.print("\n4. Enter age: ");
    	int age = Integer.parseInt(br.readLine());
    	System.out.print("\n5. Choose faculty from list:\n" + Arrays.asList(enums.Faculty.values()) + "\n\nEnter faculty: ");
    	String facultyName = br.readLine().toUpperCase();
    	try {
    			enums.Faculty.valueOf(facultyName);
    	}
    	catch(IllegalArgumentException e){
    			System.out.println("Error, no such variant! Enter again:  ");
    			facultyName = br.readLine().toUpperCase();
    	}
    	Dean d = new Dean(password, firstName, lastName, age, enums.Faculty.valueOf(facultyName));
    	System.out.print("\n6. Id of new dean is: " + d.getId() + "\n");
    	return d;
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
						System.out.println("\nUser type: " + tu + "\n" + u.forProfile());
					}
				}
			}
			
			if(!found) System.out.println("There is no such user in the system.");
    	}
    }
    
	public static void viewAttestation() throws IOException {
		while(true) {
    		System.out.print("\nEnter 'ID' of student to view his/her attestation: ");
	    	try {
	    		String studentID = br.readLine();
	    		Student student = (Student)DataBase.users.get(TypeUser.STUDENT).stream().filter(s->s.getId().equals(studentID)).findFirst().get();
	    		for(Course c : student.getTranscript().getSemesters().lastElement().getAttestations().keySet()) {
	    	          System.out.println(student.getTranscript().getSemesters().lastElement().getAttestations().get(c));
	    	    }
	    	} catch(Exception e) {
        		System.out.print("Something went wrong.");
        	} finally{
            	System.out.print("Write \"Continue\" or \"Back\": ");
            	if(br.readLine().toLowerCase().equals("back")) break;
            	System.out.println();
        	}
    	}
	}

    public static void viewJournal() throws IOException {
    	while(true) {
    		System.out.print("\nEnter 'ID' of student to view his/her journal: ");
	    	try {
	    		String studentID = br.readLine(); 
	    		Student student = (Student)DataBase.users.get(TypeUser.STUDENT).stream().filter(s->s.getId().equals(studentID)).findFirst().get();
	    		for(Course c : student.getJournal().getMarks().keySet()) {
	    	         System.out.println("Course " + c.getName() + ":"); int cnt1 = 1;
	    	         for(Mark m : student.getJournal().getMarks().get(c)) {
	    	        	  System.out.println(cnt1 + ". " + m); cnt1 += 1;
	    	         }
	    	         System.out.println();
	    		}
	    	} catch(Exception e) {
        		System.out.print("\nSomething went wrong.");
        	} finally{
            	System.out.print("\nWrite \"Continue\" or \"Back\": ");
            	if(br.readLine().toLowerCase().equals("back")) break;
            	System.out.println();
        	}
    	}
    }

	public static void viewSchedule() throws IOException {
		while(true) {
			System.out.print("\n1. Enter 'ID' of student or teacher to view their schedule\n2. Enter 'back' to return\n\nEnter 'ID' or 'back': ");
			String userIdST = br.readLine();
			if(userIdST.toLowerCase().equals("back")) return;
			for(TypeUser tu: DataBase.users.keySet()) {
				for(User u: DataBase.users.get(tu)) {
					if(u instanceof Student && u.getId().equals(userIdST)) StudentController.viewSchedule((Student)u);
					if(u instanceof Teacher && u.getId().equals(userIdST)) TeacherController.viewSchedule((Teacher)u);
				}
			}
		}
	}

    public static void viewStudentCourses() throws IOException {
    	while(true) {
	    	System.out.print("\n1. Enter 'ID' of the student to view his courses\n2. Enter 'back' to return back\n\nEnter 'ID' or 'back': ");
	    	String stId = br.readLine(); int cnt = 1;
	    	if(stId.toLowerCase().equals("back")) return;
	    	for(TypeUser tu: DataBase.users.keySet()) {
				for(User u: DataBase.users.get(tu)) {
					if(u instanceof Student && u.getId().equals(stId)) {
						System.out.println("\nStudent courses: ");
						for(Course c: ((Student)u).getCourses()) {
							System.out.println(cnt + ". " + c); cnt += 1;
						}
					}
					if(u instanceof Teacher && u.getId().equals(stId)) {
						System.out.println("\nTeacher courses: ");
						for(Lesson l : ((Teacher)u).getLessons().keySet()){
							System.out.println(cnt + ". " + l.getDescription()); cnt += 1;
						}
					}
				}
			}
    	}
    }

	public void viewStudentTranscript() {
	}

    public static void viewTeacherRate() {
    	System.out.println("\nTeachers rating:"); int cnt = 1;
    	for(User u: DataBase.users.get(TypeUser.TEACHER)) {
    		System.out.println(cnt + ". " + ((Teacher)u).getFirstName() + " " + ((Teacher)u).getLastName() + ", rating: " + ((Teacher)u).getRate()); 
    		cnt += 1;
    	}
    }
	
	public static void sendRequest() throws IOException {
		System.out.println("\nYou can only send requests to the Admin.\nTo return back, enter - back\n");
		
		System.out.println("Choose request:\n1. To block user, enter - bu\n2. To unlock user, enter - unl\n3. To change password, enter - cp");
		
		while(true) {
			System.out.print("\nEnter request or 'back': ");
			String requestType = br.readLine();
			
			if(requestType.toLowerCase().equals("back")) return;
			if(requestType.toLowerCase().equals("bu")) {
				System.out.print("\nEnter ID of the user or 'back': ");
				String requestMess = br.readLine();
				if(requestMess.toLowerCase().equals("back")) return;
				dean.sendRequest(new Request(dean, Admin.getAdmin(), requestType, requestMess));
				System.out.println("\nSuccessfully sent !");
			}
			if(requestType.toLowerCase().equals("unl")) {
				System.out.print("\nEnter ID of the user or 'back': ");
				String requestMess = br.readLine();
				if(requestMess.toLowerCase().equals("back")) return;
				dean.sendRequest(new Request(dean, Admin.getAdmin(), requestType, requestMess));
				System.out.println("\nSuccessfully sent !");
			}
			if(requestType.toLowerCase().equals("cp")) {
				System.out.print("\nEnter a new password or 'back': ");
				String requestMess = br.readLine();
				if(requestMess.toLowerCase().equals("back")) break;
				dean.sendRequest(new Request(dean, Admin.getAdmin(), requestType, requestMess));
				System.out.println("\nSuccessfully sent !");
			}
		}
	}

	public static void seeRequest() throws IOException {
    	boolean found = false; int requestsNumb = 0;
    	for(int i = 0; i < DataBase.requests.size(); i++) {
    		if(DataBase.requests.get(i).getTo().getId().equals(dean.getId())) {
    			found = true; requestsNumb += 1;
    		}
    	} 
    	if(!found) System.out.println("\nAt the moment, you have not received a single request !");
    	else {
	    	System.out.println("\nTo return back, enter - back.\nNumber of requests received: " + requestsNumb);
    		while(requestsNumb != 0) {
		    	for(int i = 0; i < DataBase.requests.size(); i++) {
	        		if(DataBase.requests.get(i).getTo().getId().equals(dean.getId())) {
			    		Request req = DataBase.requests.get(i);
			    		String answer = "";
	    		
			    		switch(req.getRequestType().toLowerCase()) {
			    		case "rem":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: remove user\n2. Reason of dismissal: " + req.getRequestMess());
			    			System.out.print("\nDo you approve of the dismissal or not ?\nEnter 'yes' if you agree or 'no' if you disagree: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); DataBase.serilaizeUsers(); return;}
			    			if(answer.toLowerCase().equals("no")) System.out.println("\nRefused. Request successfully processed !");
			    	    	if(answer.toLowerCase().equals("yes")) {
			    	    		if(dean.seeRequest(dean, "rem", req.getFrom().getId())) System.out.println("\nApproved. Request successfully processed !");
			    			} DataBase.requests.remove(i); requestsNumb -= 1;
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