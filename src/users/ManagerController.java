package users;

import java.io.BufferedReader; 
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Vector;
import enums.*;
import interfaces.*;
import attributes.*;

public class ManagerController implements StudentViewable, Viewable
{
	static Manager manager = null; static Dean dean = null;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void logIn(Manager m) throws IOException {
		manager = m;
		viewMenu();
	}

    public static void viewMenu() throws IOException {
    	while(true) {
	    	System.out.println("\nMenu:\n" +
	    						"1. My profile\n" +
	    						"2. View user information\n" +
	    						"3. Research\n" +
	    						"4. View researches\n" +
	    						"5. Add course\n" +
	    						"6. Assign course to a teacher\n" +
	    						"7. View courses\n" +
	    						"8. Student/Teacher courses\n" +
	    						"9. Student journal\n" +
	    						"10. Student/Teacher schedule\n" +
	    						"11. Student attestation\n" +
	    						"12. Student transcript\n" +
	    						"13. Send request\n" +
	    						"14. Received request\n" +
	    						"15. View Statistical reports\n" +
	    						"16. Post news\n" +
	    						"17. Send message\n" + 
	    						"18. Read my messages\n" + 
	    						"19. Log-out\n");
    	
			String choosen = br.readLine();
			
			switch(choosen) {
			case "1":
				UserController.viewProfile(manager);
				break;
			case "2":
				viewInfo();
				break;
			case "3":
				EmployeeController.doResearch(manager);
				break;
			case "4":
				EmployeeController.seeResearches(manager);
				break;
			case "5":
				addCourse();
				break;
			case "6":
				assignTeacher();
				break;
			case "7":
				System.out.println("\nAll courses list:"); int cnt = 1;
				for(Course c : DataBase.courses) {
					System.out.println(cnt + ". Course " + c); cnt += 1;
				}
				break;
			case "8":
				viewStudentCourses();
				break;
			case "9":
				viewJournal();
				break;
			case "10":
				viewSchedule();
				break;
			case "11":
				viewAttestation();
				break;
			case "12":
				break;
			case "13":
				sendRequest();
				break;
			case "14":
				seeRequest();
				break;
			case "15":
				break;
			case "16":
				System.out.println("\nChoose action:");
				System.out.println("1. Post news\n" + 
				      		"2. Edit news\n" + 
						    "3. See existing news\n");
				String action = br.readLine();
				System.out.println("\nChoose news column:");
				System.out.println("1. Official\n"
				      		+ "2. Upcoming events\n"
				    		+ "3. Lost and found\n");
				String type = br.readLine();
				if(type.equals("1")) type = "Official";
				if(type.equals("2")) type = "Upcoming events";
				if(type.equals("3")) type = "Lost and found";
				switch(action) {
				case "1": 
					System.out.println("Enter new event's date (year month day hour minute) separated by a space as in the example:\n \"2022 12 20 13 51\"");
					int[] dateInfo = new int[5]; 
					int i = 0;
			        for(String s: br.readLine().split(" ")) {
			        	dateInfo[i] = Integer.parseInt(s);
			        	i++;
			        }
					System.out.println("Enter new news content:");
					String content = br.readLine();
					DataBase.news.get(type).add(new News(new GregorianCalendar(dateInfo[0], dateInfo[1] -1, dateInfo[2],dateInfo[3],dateInfo[4]), content));
					Collections.sort(DataBase.news.get(type));
					break;
				case "2":
		    	    if(DataBase.news.get(type).isEmpty()) {
		    	    	System.out.println("There is no event to edit");
		    	    	break;
		    	    }
		    	    System.out.println("What do you want to edit?\n  1.Date\n  2.Content");
		    	    String editAction = br.readLine();
		    	    System.out.println(DataBase.news.get(type));
		    	    System.out.print("Enter id of the event that you want to edit: ");
		    	    int id = Integer.parseInt(br.readLine());
		    	    switch(editAction) {
		    	    case "1":
		    	    	System.out.println("Enter new date (year month day hour minute) separated by a space as in the example:\n \"2022 12 20 13 51\""); 
		    	    	int[] dateInfo1 = new int[5]; 
		    	    	i = 0;
				        for(String s: br.readLine().split(" ")) {
				        	 dateInfo1[i] = Integer.parseInt(s);
				        	i++;
				        }
				        for(News n: DataBase.news.get(type)) {
				        	if(n.getId() == id) {
				        		n.setDate(new GregorianCalendar(dateInfo1[0], dateInfo1[1], dateInfo1[2],dateInfo1[3],dateInfo1[4]));
				        		break;
				        	}
				        }
				        break;
		    	    case "2":
		    	    	System.out.print("\nEnter new content:");
						content = br.readLine();
						for(News n: DataBase.news.get(type)) {
					        if(n.getId() == id) {
					        	n.setContent(content);
					        	break;
					        }
					    }
		    	    	break;
		    	    default:
		    			System.out.println("\nWe don't have such an index. Please select again:");
		    	    }
		    	    Collections.sort(DataBase.news.get(type));
		    	    break;
				case "3":
					if(manager.seeNews(type).isEmpty()) System.out.println("There is no event\n");
					else System.out.println(manager.seeNews(type));
					break;
				default:
					System.out.println("\nWe don't have such an index. Please select again:");
				}
				break;
			case "17":
				System.out.println("\nChoose an employee position from the list:");
				System.out.println("TEACHER, MANAGER, DEAN, LIBRARIAN\n");
		    	String position= br.readLine().toUpperCase();
		    	try {
					enums.TypeUser.valueOf(position);
				}
				catch(IllegalArgumentException e){
						System.out.println("Error, no such variant! Enter again:  ");
						position = br.readLine().toUpperCase();
				}
				System.out.print("\nEnter name of the employee");
				String name = br.readLine();
				System.out.print("\nEnter surname of the employee");
				String surname = br.readLine();
				System.out.print("\nEnter id of employee");
				Vector <User> receivers = manager.findEmployeeList(position, name, surname);
				System.out.println(receivers);
				String id = br.readLine();
				Employee e = manager.findEmployee(id, receivers);
				if(e != null) {
					System.out.print("\nEnter your message: ");
					String message = br.readLine();
					manager.sendMessage(e, new Message(manager.getFirstName() + " " + manager.getLastName(), message, LocalDate.now()));
				}
				else {
					System.out.println("\nThere is no such employee !");
				}
				break;
			case "18":
				System.out.println(manager.getMyMessages());
				break;
			case "19":
				System.out.println();
				Admin.getAdmin().setLogFiles(new Action(LocalDate.now(), manager, "logged out"));
				Admin.serilaizeLogFiles();
				DataBase.serilaizeNews();
				DataBase.serilaizeUsers();
				return;
			default:
				System.out.println("\nWe don't have such an index. Please select again:");
			}
    	}
    }
    
    public static Manager getManager() throws IOException{
    	System.out.print("\n1. Enter password: ");
    	String password = br.readLine();
    	System.out.print("\n2. Enter first name: ");
    	String firstName = br.readLine();
    	System.out.print("\n3. Enter last name: ");
    	String lastName = br.readLine();
    	System.out.print("\n4. Enter age: ");
    	int age = Integer.parseInt(br.readLine());
    	System.out.println("\n5. Choose type of manager from list:\n" + Arrays.asList(enums.TypeManager.values()) + "\n");
    	String managerType = br.readLine().toUpperCase();
    	try {
			enums.TypeManager.valueOf(managerType);
		}
		catch(IllegalArgumentException e){
				System.out.println("Error, no such variant! Enter again:  ");
				managerType = br.readLine().toUpperCase();
		}
    	Manager m = new Manager(password, firstName, lastName, age, enums.TypeManager.valueOf(managerType));
    	System.out.print("6. Id of new manager is: " + m.getId() + "\n");
    	return m;
    }
    
    public static void viewInfo() throws IOException {
		System.out.println("\n1. Enter the ID of the student to view information about him\n2. Enter back for return");

    	while(true) {
    		System.out.print("\nEnter name or 'back': ");
    		String userId = br.readLine();
    		
    		boolean found = false;
		
    		if(userId.toLowerCase().equals("back")) return;
			for(User u: DataBase.users.get(TypeUser.STUDENT)) {
				if(u instanceof Student && u.getId().toLowerCase().equals(userId.toLowerCase())) {
					Student t = (Student)u;
					found = true;
					System.out.println("\n" + t.forProfile());
				}
			}
			
			if(!found) System.out.println("There is no such student in the system.");
    	}
    }
    
    public static void addCourse() throws IOException {
    	System.out.println("\nAdd course to the system");
    	while(true) {
        	try {
            	String courseName; Integer credits; TypeCourse typeCourse; Faculty faculty; Vector <Lesson> lessons = new Vector <>();
            	System.out.print("Write course name: ");
            	courseName = br.readLine();
            	
            	
            	System.out.print("Write the number of credits: ");
            	credits = Integer.parseInt(br.readLine());
            	if(credits > 7) {
            		System.out.println("Warning!\nThe maximum credits for one course is 6. Try again.\n");
            		continue;
            	}
            	
            	System.out.print("Choose course type (1 - Required, 2 - Major, 3 - Minor, 4 - Free elective): ");
            	typeCourse = UserController.typeCourse.get(Integer.parseInt(br.readLine()));
            	
            	System.out.print("Write course faculty (1 - FIT, 2 - BS, 3 - ISE): ");
            	faculty = UserController.faculties.get(Integer.parseInt(br.readLine()));
            	Course course = new Course(courseName, credits, typeCourse, faculty);
            	
            	
            	System.out.print("Write lectures hours: ");
            	lessons.add(new Lesson(course, TypeLesson.LECTURE, Integer.parseInt(br.readLine())));
            	
            	System.out.print("Write laboratory hours: ");
            	lessons.add(new Lesson(course, TypeLesson.LABORATORY, Integer.parseInt(br.readLine())));
            	
            	System.out.print("Write practice hours: ");
            	lessons.add(new Lesson(course, TypeLesson.PRACTICE, Integer.parseInt(br.readLine())));
            	   	
            	
            	manager.addCourse(course, lessons);
            	System.out.println(courseName + " has been successfully added");
            	
            	
        	} catch(Exception e) {
        		System.out.print("Something went wrong.");
        	} finally{
            	System.out.print("Write \"Continue\" or \"Back\": ");
            	if(br.readLine().toLowerCase().equals("back")) break;
            	System.out.println();
        	}	
    	}
    }
    
    public static void assignTeacher() throws IOException{
    	while(true) {
        	try {
        		
            	System.out.print("List of courses: \n");
            	for(Course c : DataBase.courses) {
            		System.out.println(c);
            	}
            	System.out.print("\nChoose one of them: ");
            	String name = br.readLine().toLowerCase();
            	
        		Course course = DataBase.courses.stream().filter(c->c.getName().toLowerCase().equals(name)).findFirst().get();
        		System.out.print("Choose one of lesson type: ");
        		course.getLessons().stream().filter(c->c.getDuration()!=0).forEach(t -> System.out.print(t.getType() + " "));
        		System.out.println();
        		String find = br.readLine().toUpperCase();
        		Lesson l = (Lesson) course.getLessons().stream().filter(c-> c.getType().equals(TypeLesson.valueOf(find))).findFirst().get().clone();
        		
        		System.out.println("Choose teacher for " + find.toLowerCase() + ": ");
            	for(User u : DataBase.users.get(TypeUser.TEACHER)) {
            		Teacher t = (Teacher) u;
            		if(t.getFaculty() == course.getFaculty() && !(t.getLessons().containsKey(l)) ) System.out.println(t.getId() + ": " + t.getFirstName() + " " + t.getLastName());
            	}
            	
            	String id = br.readLine();
            	Teacher t = (Teacher) DataBase.users.get(TypeUser.TEACHER).stream().filter(u->u.getId().equals(id)).findFirst().get();
            	t.getLessons().put(l, new Vector <Student>());
            	System.out.println(manager.assignTeacher(t, l) ? t.getFirstName() + " " + t.getLastName() + " was successfully added to course as " + find.toLowerCase() + " teacher" : "Something went wrong"); 
        		
        	} catch(Exception e) {
        		System.out.print("Something went wrong. ");
        	} finally{
            	System.out.print("Write \"Continue\" or \"Back\": ");
            	if(br.readLine().toLowerCase().equals("back")) break;
            	System.out.println();
        	}
    	}
    	
    	
    }
    
    public void acceptAddDrop() {
    }

    public void assignCourseToTeacher() {
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
				
				manager.sendRequest(new Request(manager, dean, "rem", requestMess));
				System.out.println("\nSuccessfully sent !");
			}
			if(user.toLowerCase().equals("a")) {
				System.out.print("\nYou can send a request to the admin only to change the password.\nEnter a new password or 'back': ");
				String requestMess = br.readLine();
				if(requestMess.toLowerCase().equals("back")) break;
				manager.sendRequest(new Request(manager, Admin.getAdmin(), "cp", requestMess));
				System.out.println("\nSuccessfully sent !");
			}
		}
	}

	public static void seeRequest() throws IOException {
		boolean found = false; int requestsNumb = 0;
		for(int i = 0; i < DataBase.requests.size(); i++) {
			System.out.println(DataBase.requests.get(i).getTo().getId());
			if(manager.getId().equals(DataBase.requests.get(i).getTo().getId())) {
				found = true; requestsNumb += 1;
			}
		} 
		if(!found) System.out.println("\nAt the moment, you have not received a single request !");
    	else {
	    	System.out.println("\nTo return back, enter - back.\nNumber of requests received: " + requestsNumb);
	    	while(requestsNumb != 0) {
		    	for(int i = 0; i < DataBase.requests.size(); i++) {
	        		if(DataBase.requests.get(i).getTo().getId().equals(manager.getId())) {
			    		Request req = DataBase.requests.get(i);
			    		String answer = "";
	    		
			    		switch(req.getRequestType().toLowerCase()) {
			    		case "add":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: Add/Drop, add course\n2. Course name: " + req.getRequestMess());
			    			System.out.print("\nEnter 'add' to add course: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); DataBase.serilaizeUsers(); return;}
			    			if(answer.toLowerCase().equals("add")) {
		    					System.out.println("\nRequest successfully processed !");
		    					DataBase.requests.remove(i);
		    				} requestsNumb -= 1;
			    			break;
			    		case "drop":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: Add/Drop, drop course\n2. Course name: " + req.getRequestMess());
			    			System.out.print("\nEnter 'drop' to drop course: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); DataBase.serilaizeUsers(); return;}
			    			if(answer.toLowerCase().equals("drop")) {
			    				
		    					System.out.println("\nRequest successfully processed !");
		    				} requestsNumb -= 1;
			    			break;
			    		case "cs":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: change schedule\n2. User ID: " + req.getRequestMess());
			    			System.out.print("\nEnter 'change' to change schedule: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); DataBase.serilaizeUsers(); return;}
			    			if(answer.toLowerCase().equals("change")) {
		    					System.out.println("\nRequest successfully processed !");
		    				} requestsNumb -= 1;
			    			break;
			    		case "ct":
				    		System.out.println("\nFrom user with ID: " + req.getFrom().getId() + "\n\nRequest details:\n1. Type: change teacher\n2. Course name/New teacher ID: " + req.getRequestMess());
				    		System.out.print("\nEnter 'change' to change teacher: ");
			    			answer = br.readLine();
			    			if(answer.toLowerCase().equals("back")) {DataBase.serilaizeRequests(); DataBase.serilaizeUsers(); return;}
			    			if(answer.toLowerCase().equals("change")) {
		    					System.out.println("\nRequest successfully processed !");
		    				} requestsNumb -= 1;
			    			break;
			    		default:
							System.out.println("\nThe sent request does not match the type !");
							DataBase.requests.remove(i); requestsNumb -= 1;
							break;
			    		}
		        		
			    		if(requestsNumb == 0) System.out.println("\nYou have answered all the sent requests.");
			        	DataBase.serilaizeRequests();
	        		}
		    	}
	    	}
    	}
    }
}