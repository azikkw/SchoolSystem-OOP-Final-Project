package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;
import attributes.*;
import enums.TypeLesson;
import enums.TypeUser;
import interfaces.*;

public class StudentController implements ViewableAndDrawupable
{
	static Student student = null; static Manager manager = null; static Teacher teacher = null;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void logIn(Student s) throws IOException {
		student = s;
		viewMenu();
	}

    static void viewMenu() throws IOException {
    	while(true) {
	    	System.out.println("\nMenu:\n" +
				                "1. My profile\n" +
				                "2. View user information\n" +
				                "3. Research\n" +
				                "4. See researches\n" +
				                "5. Register to course\n" +
				                "6. Rate Teacher\n" +
				                "7. View Schedule\n" +
				                "8. Draw up schedule\n" +
				                "9. View journal\n" +
				                "10. View my courses\n" +
				                "11. View attestation\n" +
				                "12. View trancript\n" +
				                "13. Create organization\n" +
				                "14. Join organization\n" +
				                "15. Send request\n" +
				                "16. Get book\n" +
				                "17. See news\n" +
				                "18. Log-out\n");
    	
			String choosen = br.readLine();
			
			switch(choosen) {
			case "1":
				UserController.viewProfile(student);
				break;
			case "2":
				viewInfo();
				break;
			case "3":
				doResearch();
				DataBase.serilaizeResearches();
				break;
			case "4":
				seeResearches();
				break;
			case "5":
				registerToCourse();
				break;
			case "6":
				rateTeacher();
				break;
			case "7":
				viewSchedule(student);
				break;
			case "8":
				drawUpSchedule();
				break;
			case "9":
				viewJournal();
				break;
			case "10":
				viewStudentCourses();
				break;
			case "11":
				viewAttestation();
				break;
			case "12":
				break;
			case "13":
				System.out.println("1. Create simple organization\n" + 
				           "2. Update existing organization\n");
				String action = br.readLine();
				System.out.println("Enter name of the organization");
				switch(action) {
				case "1":
					String name = br.readLine();
					DataBase.organizations.add(new SimpleOrganization(name));
					break;
				case"2":
					System.out.println(DataBase.organizations);
					System.out.print("Enter organization name:");
					name = br.readLine();
					Optional <Organization> optional = DataBase.organizations.stream().filter(n-> n.getName().equals(name)).findFirst();
					if(optional.isPresent()) {
						System.out.println("Choose: 1.International Organization\n"+
													"2.Inter- University Organization");
						String type = br.readLine();
						Organization o = optional.get(); 
						switch(type) {
						case"1":
							o = new InternationalOrganization(o);
							break;
						case"2":
							o = new InterUniversityOrganization(o);
							break;
						}
					}
					break;
				}		
				break;
			case "14":
				if(DataBase.organizations.isEmpty()) break;
				System.out.println(DataBase.organizations);
				System.out.print("Enter organization name:");
				String name = br.readLine();
				Optional <Organization> optional = DataBase.organizations.stream().filter(n-> n.getName().equals(name)).findFirst();
				if(optional.isPresent()) {((SimpleOrganization) optional.get()).list.add(student);}
				break;
			case "15":
				sendRequest();
				break;
			case "16":
				if(Librarian.givenBooks.get(student) != null) {
					System.out.println("Books that you already got: " + Librarian.givenBooks.get(student).size()); int cnt = 1;
					for(Literature li: Librarian.givenBooks.get(student)) {
						System.out.println(cnt + ". Book: " + li.getName() + ", author: " + li.getAuthor() + ", pages: " + li.getPages()); cnt += 1;
						Period time = Period.between(LocalDate.now(), li.getDeadline().plusDays(3));
						System.out.println("   Deadline: number of months: " + time.getMonths() + 
										   "\n             number of days: " + time.getDays());
					}
				}
				else {
					Librarian.givenBooks.put(student, new Vector<>());
				}
				if(Librarian.availableBooks.isEmpty()) break;
				System.out.println("\nAvailable books: " + Librarian.availableBooks.size()); int cnt = 1;
				for(Literature li: Librarian.availableBooks) {
					System.out.println(cnt + ". Book: " + li.getName() + ", author: " + li.getAuthor() + ", pages: " + li.getPages()); 
					cnt += 1;
				}
				System.out.print("\nEnter book's name that you want to get: ");
				String title = br.readLine();
				System.out.print("\nEnter book's author that you want to get: ");
				String author= br.readLine();
				Literature book = Librarian.getBook(title, author);
				Librarian.availableBooks.remove(book);
				book.setDeadline(LocalDate.now());
				Librarian.givenBooks.get(student).add(book);
				
				break;
			case "17":
				System.out.println("Choose news column:");
				System.out.println("1. Official\n"
				      		+ "2. Upcoming events\n"
				    		+ "3. Lost and found\n");
				String type = br.readLine();
				if(type.equals("1")) type = "Official";
				if(type.equals("2")) type = "Upcoming events";
				if(type.equals("3")) type = "Lost and found";
				if(student.seeNews(type).isEmpty()) System.out.println("There is no event\n");
				else System.out.println(student.seeNews(type));
				break;
			case "18":
				System.out.println();
				Admin.getAdmin().setLogFiles(new Action(LocalDate.now(), student, "logged out"));
		    	Admin.serilaizeLogFiles();
				Librarian.serilaizeAvailableBooks();
				Librarian.serilaizeGivenBooks();
				DataBase.serilaizeOraganizations();
				DataBase.serilaizeUsers();
				DataBase.serilaizeResearches();
				return;
			default:
				System.out.println("\nWe don't have such an index. Please select again:");
			}
    	}
    }
    
    public static Student getStudent() throws IOException{
    	System.out.print("\n1. Enter password: ");
    	String password = br.readLine();
    	System.out.print("\n2. Enter first name: ");
    	String firstName = br.readLine();
    	System.out.print("\n3. Enter last name: ");
    	String lastName = br.readLine();
    	System.out.print("\n4. Enter age: ");
    	int age = Integer.parseInt(br.readLine());
    	System.out.print("\n5. Enter year of study: ");
    	int year = Integer.parseInt(br.readLine());
    	System.out.print("\n6. Enter GPA:  ");
    	double GPA = Double.parseDouble(br.readLine());
    	System.out.print("\n6. Choose degree from list:\n" + Arrays.asList(enums.Degree.values()) + "\n\nEnter degree: ");
    	String degree = br.readLine().toUpperCase();
    	try {
			enums.Degree.valueOf(degree);
		}
		catch(IllegalArgumentException e){
				System.out.println("Error, no such variant! Enter again:  ");
				degree = br.readLine().toUpperCase();
		}
    	System.out.print("\n7. Choose faculty from list:\n" + Arrays.asList(enums.Faculty.values()) + "\n\nEnter faculty: ");
    	String facultyName = br.readLine().toUpperCase();
    	try {
			enums.Faculty.valueOf(facultyName);
		}
		catch(IllegalArgumentException e){
				System.out.println("Error, no such variant! Enter again:  ");
				facultyName = br.readLine().toUpperCase();
		}
    	Student s = new Student(password, firstName, lastName, age, year, GPA, enums.Degree.valueOf(degree), enums.Faculty.valueOf(facultyName));
    	s.setId(s.idGenerator());
    	System.out.println("8. Id of new student is: " + s.getId() + "\n");
    	return s;
    }
    
    public static void viewInfo() throws IOException {
		System.out.println("\n1. Enter the name of the teacher to view information about him\n2. Enter back for return");

    	while(true) {
    		System.out.print("\nEnter name or 'back': ");
    		String teacherName = br.readLine();
    		
    		boolean found = false;
		
    		if(teacherName.toLowerCase().equals("back")) return;
			for(User u: DataBase.users.get(TypeUser.TEACHER)) {
				if(u instanceof Teacher && u.getFirstName().toLowerCase().equals(teacherName.toLowerCase())) {
					Teacher t = (Teacher)u;
					found = true;
					System.out.println("\n" + t); //+ "\n-\nCourses taught: " + t.getCourses().keySet());
				}
			}
			
			if(!found) System.out.println("There is no such teacher in the system.");
    	}
    }

    public static void viewJournal() {
        for(Course c : student.getJournal().getMarks().keySet()) {
          System.out.println(c.getName());
          for(Mark m : student.getJournal().getMarks().get(c)) {
            System.out.println(m);
          }
          System.out.println();
        }
     }

	public static void viewSchedule(Student s) throws IOException {
		Schedule sc1 = new Schedule(DayOfWeek.MONDAY);
		Schedule sc2 = new Schedule(DayOfWeek.TUESDAY);
		Schedule sc3 = new Schedule(DayOfWeek.WEDNESDAY);
		Schedule sc4 = new Schedule(DayOfWeek.THURSDAY);
		Schedule sc5 = new Schedule(DayOfWeek.FRIDAY);
		Schedule sc6 = new Schedule(DayOfWeek.SATURDAY);
		
		for(Lesson lesson: s.getSchedule()) {
			for(Teacher teacher: DataBase.users.get(TypeUser.TEACHER).stream().map(u-> (Teacher)u).filter(r->(r.getLessons().values().stream().filter(v->v.contains(s)).findFirst().orElse(null) != null)).collect(Collectors.toList())) {
				for(Lesson lessonsTeacher: teacher.getLessons().keySet()) {
					if(lesson.equals(lessonsTeacher) && teacher.getLessons().get(lessonsTeacher).contains(s) == true) {
						if(lesson.getDay() == sc1.getWeekDay()) {
							sc1.getLessons().add((lesson.getCourse().getName() + ": " + teacher.getLastName() + " " + teacher.getFirstName().charAt(0) + ". " + (lesson.getType() == TypeLesson.LABORATORY ? lesson.getType().toString().substring(0, 3) : lesson.getType().toString().substring(0, 4))  + ", " + lesson.getBegin() + "-" + (lesson.getBegin() + lesson.getDuration())));
						}
						else if(lesson.getDay() == sc2.getWeekDay()) {
							sc2.getLessons().add((lesson.getCourse().getName() + ": " + teacher.getLastName() + " " + teacher.getFirstName().charAt(0) + ". " + (lesson.getType() == TypeLesson.LABORATORY ? lesson.getType().toString().substring(0, 3) : lesson.getType().toString().substring(0, 4)) + ", " + lesson.getBegin() + "-" + (lesson.getBegin() + lesson.getDuration())));
						}
						else if(lesson.getDay() == sc3.getWeekDay()) {
							sc3.getLessons().add((lesson.getCourse().getName() + ": " + teacher.getLastName() + " " + teacher.getFirstName().charAt(0) + ". " + (lesson.getType() == TypeLesson.LABORATORY ? lesson.getType().toString().substring(0, 3) : lesson.getType().toString().substring(0, 4)) + ", " + lesson.getBegin() + "-" + (lesson.getBegin() + lesson.getDuration())));
						}
						else if(lesson.getDay() == sc4.getWeekDay()) {
							sc4.getLessons().add((lesson.getCourse().getName() + ": " + teacher.getLastName() + " " + teacher.getFirstName().charAt(0) + ". " + (lesson.getType() == TypeLesson.LABORATORY ? lesson.getType().toString().substring(0, 3) : lesson.getType().toString().substring(0, 4)) + ", " + lesson.getBegin() + "-" + (lesson.getBegin() + lesson.getDuration())));
						}
						else if(lesson.getDay() == sc5.getWeekDay()) {
							sc5.getLessons().add((lesson.getCourse().getName() + ": " + teacher.getLastName() + " " + teacher.getFirstName().charAt(0) + ". " + (lesson.getType() == TypeLesson.LABORATORY ? lesson.getType().toString().substring(0, 3) : lesson.getType().toString().substring(0, 4)) + ", " + lesson.getBegin() + "-" + (lesson.getBegin() + lesson.getDuration())));
						}
						else if(lesson.getDay() == sc6.getWeekDay()) {
							sc6.getLessons().add((lesson.getCourse().getName() + ": " + teacher.getLastName() + " " + teacher.getFirstName().charAt(0) + ". " + (lesson.getType() == TypeLesson.LABORATORY ? lesson.getType().toString().substring(0, 3) : lesson.getType().toString().substring(0, 4)) + ", " + lesson.getBegin() + "-" + (lesson.getBegin() + lesson.getDuration())));
						}
					}
				}
			}
		}
		
		Collections.sort(sc1.getLessons());
		Collections.sort(sc2.getLessons());
		Collections.sort(sc3.getLessons());
		Collections.sort(sc4.getLessons());
		Collections.sort(sc5.getLessons());
		Collections.sort(sc6.getLessons());

		String[][] sched = new String[8][6];
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 6; j++) {
				sched[i][j] = " ".repeat(35);
			}
		}
		
		sched[0][0] = " ".repeat((sched[0][0].length() - sc1.sysWeekDay().length()) / 2) + sc1.sysWeekDay() + " ".repeat((sched[0][0].length() - sc1.sysWeekDay().length()) - (sched[0][0].length() - sc1.sysWeekDay().length()) / 2);
		sched[0][1] = " ".repeat((sched[0][0].length() - sc2.sysWeekDay().length()) / 2) + sc2.sysWeekDay() + " ".repeat((sched[0][0].length() - sc2.sysWeekDay().length()) - (sched[0][0].length() - sc2.sysWeekDay().length()) / 2);
		sched[0][2] = " ".repeat((sched[0][0].length() - sc3.sysWeekDay().length()) / 2) + sc3.sysWeekDay() + " ".repeat((sched[0][0].length() - sc3.sysWeekDay().length()) - (sched[0][0].length() - sc3.sysWeekDay().length()) / 2);
		sched[0][3] = " ".repeat((sched[0][0].length() - sc4.sysWeekDay().length()) / 2) + sc4.sysWeekDay() + " ".repeat((sched[0][0].length() - sc4.sysWeekDay().length()) - (sched[0][0].length() - sc4.sysWeekDay().length()) / 2);
		sched[0][4] = " ".repeat((sched[0][0].length() - sc5.sysWeekDay().length()) / 2) + sc5.sysWeekDay() + " ".repeat((sched[0][0].length() - sc5.sysWeekDay().length()) - (sched[0][0].length() - sc5.sysWeekDay().length()) / 2);
		sched[0][5] = " ".repeat((sched[0][0].length() - sc6.sysWeekDay().length()) / 2) + sc6.sysWeekDay() + " ".repeat((sched[0][0].length() - sc6.sysWeekDay().length()) - (sched[0][0].length() - sc6.sysWeekDay().length()) / 2);

		for(int i = 0; i < sc1.getLessons().size(); i++) {
			sched[i + 1][0] = " ".repeat((sched[i + 1][0].length() - (sc1.getLessons().get(i)).length()) / 2) + (sc1.getLessons().get(i)) + 
					" ".repeat((sched[i + 1][0].length() - (sc1.getLessons().get(i)).length()) - (sched[i + 1][0].length() - (sc1.getLessons().get(i)).length()) / 2);
		}
		for(int i = 0; i < sc2.getLessons().size(); i++) {
			sched[i + 1][1] = " ".repeat((sched[i + 1][1].length() - (sc2.getLessons().get(i)).length()) / 2) + (sc2.getLessons().get(i)) + 
					" ".repeat((sched[i + 1][1].length() - (sc2.getLessons().get(i)).length()) - (sched[i + 1][1].length() - (sc2.getLessons().get(i)).length()) / 2);
		}
		for(int i = 0; i < sc3.getLessons().size(); i++) {
			sched[i + 1][2] = " ".repeat((sched[i + 1][2].length() - (sc3.getLessons().get(i)).length()) / 2) + (sc3.getLessons().get(i)) + 
					" ".repeat((sched[i + 1][2].length() - (sc3.getLessons().get(i)).length()) - (sched[i + 1][2].length() - (sc3.getLessons().get(i)).length()) / 2);
		}
		for(int i = 0; i < sc4.getLessons().size(); i++) {
			sched[i + 1][3] = " ".repeat((sched[i + 1][3].length() - (sc4.getLessons().get(i)).length()) / 2) + (sc4.getLessons().get(i)) + 
					" ".repeat((sched[i + 1][3].length() - (sc4.getLessons().get(i)).length()) - (sched[i + 1][3].length() - (sc4.getLessons().get(i)).length()) / 2);
		}
		for(int i = 0; i < sc5.getLessons().size(); i++) {
			sched[i + 1][4] = " ".repeat((sched[i + 1][4].length() - (sc5.getLessons().get(i)).length()) / 2) + (sc5.getLessons().get(i)) + 
					" ".repeat((sched[i + 1][4].length() - (sc5.getLessons().get(i)).length()) - (sched[i + 1][4].length() - (sc5.getLessons().get(i)).length()) / 2);
		}
		for(int i = 0; i < sc6.getLessons().size(); i++) {
			sched[i + 1][5] = " ".repeat((sched[i + 1][5].length() - (sc6.getLessons().get(i)).length()) / 2) + (sc6.getLessons().get(i)) + 
					" ".repeat((sched[i + 1][5].length() - (sc6.getLessons().get(i)).length()) - (sched[i + 1][5].length() - (sc6.getLessons().get(i)).length()) / 2);
		}
		
		System.out.println();
		
		for(int i = 0; i < 8; i++) {
			System.out.println("-".repeat(217));
			for(int j = 0; j < 6; j++) {
				System.out.print("|");
				System.out.print(sched[i][j]);
			}
			System.out.println("|");
		}
		
		System.out.println("-".repeat(217));
		
		System.out.print("\nEnter 'back' to return: ");
		
		String returnBack = br.readLine();
		if(returnBack.toLowerCase().equals("back")) return;
	}

	public static void drawUpSchedule() throws IOException {
		while(true) {						
			try {
				for(Course course : student.getCourses()) {
					for(Lesson lesson : course.getLessons()) {	
						Vector <Teacher> teachers = (Vector<Teacher>) DataBase.users.get(TypeUser.TEACHER).stream().map(s-> (Teacher) s).collect(Collectors.toCollection(Vector::new));
						List <Teacher> listTeachers = teachers.stream().filter(t-> t.getLessons().keySet().stream().filter(l->l.equals(lesson)).findAny().orElse(null) != null).collect(Collectors.toList());
						if(listTeachers.stream().filter(t->t.getLessons().entrySet().stream().filter(e-> e.getKey().equals(lesson) && e.getValue().contains(student) == true).findAny().orElse(null) != null).findFirst().orElse(null) != null) {
							continue;
						}
						if(listTeachers.size()!=0) {
							System.out.println("\nChoose teacher for " + course.getName() + " " + lesson.getType() + ":"); 
							listTeachers.stream().forEach(t->System.out.println(t.getId() + ": " + t.getFirstName() + " " + t.getLastName() + " " + t.getLessons().keySet().stream().filter(l->l.getType() == lesson.getType()).findFirst().get().getDescription()));
							String teacherID = br.readLine();
							Teacher teacher = (Teacher) DataBase.users.get(TypeUser.TEACHER).stream().filter(t->t.getId().equals(teacherID)).findFirst().get();
							Lesson les = teacher.getLessons().keySet().stream().filter(l->l.getType() == lesson.getType()).findFirst().get();
							System.out.println(student.DrawUpSchedule(teacher, les) ? "You have successfully chosen your " + course.getName() + " " + lesson.getType().toString().toLowerCase() + " " + "time" : "Something went wrong. ");
						}
					}
				}
				
        	} 
			catch(Exception e) {
        		System.out.print("Something went wrong. ");
        	} 
			finally {
        		System.out.println("You completed your schedule");
            	System.out.print("Write \"Continue\" or \"Back\": ");
            	if(br.readLine().toLowerCase().equals("back")) break;
        	}
		}
	}
	
	public static void registerToCourse() throws IOException {
	      while(true) {    
	        try
	        { 
	            System.out.println("List of courses: ");
	            DataBase.courses.stream().forEach(c-> System.out.println(c));
	            
	            System.out.print("Choose one of them: ");
	            String name = br.readLine().toLowerCase();
	            Course course = DataBase.courses.stream().filter(c->c.getName().toLowerCase().equals(name)).findFirst().get();            
	            System.out.println(student.registerToCourse(course) ? "You have successfully registered to " + course.getName() : "You cannot register to this course. ");
	        }
	        catch (Exception ex)
	        {
	           System.out.println("Something went wrong. ");
	        }
	          finally{
	            System.out.print("Write \"Continue\" or \"Back\": ");
	            if(br.readLine().toLowerCase().equals("back")) break;
	            System.out.println();
	          }
	          
	      }
	    }

    public void createOrganization() {
    }
    
    public static void viewAttestation() {
        for(Course c : student.getTranscript().getSemesters().lastElement().getAttestations().keySet()) {
          System.out.println(student.getTranscript().getSemesters().lastElement().getAttestations().get(c));
        }
      }

    public void joinOrganization() {
    }
    
    public void addDrop() {
    }

    public static void rateTeacher() throws NumberFormatException, IOException {
    	int cnt = 0;
    	for(User u: DataBase.users.get(TypeUser.TEACHER)) {
    		Teacher t = (Teacher)u; 
    		for(Lesson l: t.getLessons().keySet()) {
    			for(Student s: t.getLessons().get(l)) {
    				if(s.getId().equals(student.getId()) && !(student.getRatedTeachers().contains(t))) {
    					cnt += 1;
    				}
    			}
    		}
    	}
    	if(cnt == 0) System.out.println("\nYou rated all of your teachers.");
    	while(cnt > 0) {
    		for(User u: DataBase.users.get(TypeUser.TEACHER)) {
        		Teacher t = (Teacher)u;
        		for(Lesson l: t.getLessons().keySet()) {
        			for(Student s: t.getLessons().get(l)) {
        				if(s.getId().equals(student.getId()) && !(student.getRatedTeachers().contains(t))) {
        					System.out.println("\nRate '" + t.getFirstName() + " " + t.getLastName() + "' in that range:\n\nMin -> 1 2 3 4 5 <- Max");
        					System.out.print("\n1. The teacher is generally well-organized and prepared for class: ");
        					int rate1 = Integer.parseInt(br.readLine());
        					System.out.print("2. Teacher manage all grades on time and provide students with feedback: ");
        					int rate2 = Integer.parseInt(br.readLine());
        					System.out.print("3. The teacher uses a variety of activities (discussion, group work) during class time especially bonus party: ");
        					int rate3 = Integer.parseInt(br.readLine());
        					int totalRate = (rate1 + rate2 + rate3) / 3;
        					if(totalRate >= 1 && totalRate <= 5) {
        						t.setRate(t.getRate() + totalRate); t.setRateCounter(t.getRateCounter() + 1);
        						student.getRatedTeachers().add(t); DataBase.serilaizeUsers(); cnt -= 1;
        						System.out.println("\n'" + t.getFirstName() + " " + t.getLastName() + "' was successfully evaluated !");
        					}
        				}
        			}
        		}
        	}
    	}
    }

    public static void viewStudentCourses() {
    	student.getCourses().stream().forEach(c-> System.out.println(c));
    }

    public void viewStudentTranscript() {
    }
	
	public static void sendRequest() throws IOException {		
		System.out.println("\nWho do you want to send the request to:\nTo return back, enter - back\n\n" + 
						  "1. For manager, enter - m\n2. For admin, enter - a");
		
		while(true) {
			System.out.print("\nEnter user or 'back': ");
			String user = br.readLine();
			
			if(user.toLowerCase().equals("back")) return;
			if(user.toLowerCase().equals("m")) {
				System.out.println("\nChoose manager from list:");
				
				int cnt = 1;
				for(User u: DataBase.users.get(TypeUser.MANAGER)) {
					System.out.println(cnt + ". " + u.getFirstName() + ", ID: " + u.getId()); cnt += 1;
				}
				
				System.out.print("\nEnter ID of manager or 'back': ");
				String managerId = br.readLine();
				
				if(managerId.toLowerCase().equals("back")) return;
				for(User u: DataBase.users.get(TypeUser.MANAGER)) {
					if(u instanceof Manager && managerId.toLowerCase().equals(u.getId())) manager = (Manager)u;
				}
				
				System.out.println("\nChoose request:\n1. To add/drop, enter - ad\n2. To change schedule, enter - cs\n3. To change teacher, enter - ct\n");
				
				System.out.print("Enter request or 'back': ");
				String requestType = br.readLine();
				
				if(requestType.toLowerCase().equals("back")) return;
				if(requestType.toLowerCase().equals("ad")) {
					System.out.print("\n1.Enter 'add' if you want to add some course\n2. Enter 'drop' if you want to drop one of your courses\n\nEnter 'add' or 'drop': ");
					String requestTypeIn = br.readLine();
					if(requestTypeIn.toLowerCase().equals("add")) {
						System.out.println("\nList of all courses: ");
						// Sysout all of the courses
						System.out.print("\nEnter the name of chosen course to add: ");
						String courseName = br.readLine();
						student.sendRequest(new Request(student, manager, requestTypeIn, courseName));
						System.out.println("\nSuccessfully sent !");
					}
					if(requestTypeIn.toLowerCase().equals("drop")) {
						System.out.println("\nList of your courses: ");
						// Sysout student courses
						System.out.print("\nEnter the name of chosen course to drop: ");
						String courseName = br.readLine();
						student.sendRequest(new Request(student, manager, requestTypeIn, courseName));
						System.out.println("\nSuccessfully sent !");
					}
				}
				if(requestType.toLowerCase().equals("cs")) {
					// Sysout student schedule
					String requestMess = br.readLine();
					student.sendRequest(new Request(student, manager, requestType, requestMess));
					System.out.println("\nSuccessfully sent !");
				}
				if(requestType.toLowerCase().equals("ct")) {
					// Sysout student teachers
					System.out.print("\nEnter the 'name of course/new teacher ID' in such format: ");
					String requestMess = br.readLine();
					student.sendRequest(new Request(student, manager, requestType, requestMess));
					System.out.println("\nSuccessfully sent !");
				}
			}
			if(user.toLowerCase().equals("a")) {
				System.out.print("\nYou can send a request to the admin only to change the password.\nEnter a new password or 'back': ");
				String requestMess = br.readLine();
				if(requestMess.toLowerCase().equals("back")) break;
				student.sendRequest(new Request(student, Admin.getAdmin(), "cp", requestMess));
				System.out.println("\nSuccessfully sent !");
			}
		} DataBase.serilaizeUsers();
	}

	public static void doResearch() throws IOException {
		if(!student.isResearchStatus()) {
			System.out.print("\nTo return back, enter - back.\n\nHello, you are not a researcher at the moment.\nIf you want to become one, enter 'yes'.\n\nEnter 'yes' or 'back': ");
			String answer = br.readLine();
			if(answer.toLowerCase().equals("back")) return;
			if(answer.toLowerCase().equals("yes")) {
				student.setResearchStatus(true); System.out.println("\nCongratulations, now you have become a researcher !"); 
				DataBase.serilaizeUsers();
			}
		}
		else {
			Research research = null;
			System.out.println("\nHello, " + student.getFirstName() + ". Your h-index: " + student.gethIndex() + "\nWhat kind of scientific research do you want to surprise the world with today?");
			while(true) {
				System.out.print("\n1. To view your researches, enter - vr\n2. To publish a new research, enter - pr\n3. To return back, enter - back\n\nEnter: ");
				String chosen = br.readLine();
				switch(chosen) {
				case "vr":
					System.out.println("\nYour researches:"); int cnt = 1;
					for(Research r: DataBase.researches) {
						for(User u: r.getResearchers()) {
							if(student.getId().equals(u.getId())) {
								System.out.println(cnt + ". " + r);
								cnt += 1;
							}
						}
					}
					break;
				case "pr":
					System.out.print("\n1. Enter the name of your research: ");
					String researchName = br.readLine();
					
					System.out.print("\n2. Add a description of your research: ");
					String researchDesc = br.readLine();
					
					System.out.print("\n3. Team members:\nIf you did this research yourself, enter '0'.\nIf there were several of you, enter the number of participants. \n\nEnter number: ");
					String researchPart = br.readLine();
			
					research = new Research(researchName, researchDesc);
					
					if(researchPart.equals("0")) {
						research.getResearchers().add(student);
						student.doResearch(research);
					}
					else {
						research.getResearchers().add(student);
						student.doResearch(research);
						
						int partNumber = Integer.parseInt(researchPart);
						
						while(partNumber != 0) {
							System.out.print("\nEnter 'ID' of your team member: ");
							String memberId = br.readLine(); boolean found = false;
							for(TypeUser tu: DataBase.users.keySet()) {
								for(User u: DataBase.users.get(tu)) {
									if(u instanceof Student && memberId.equals(u.getId()) && ((Student)u).isResearchStatus()) {
										Student s = (Student)u; found = true; 
										research.getResearchers().add(s);
										s.doResearch(research);
									}
									if(u instanceof Employee && memberId.equals(u.getId()) && ((Employee)u).isResearchStatus()) {
										Employee e = (Employee)u; found = true;
										research.getResearchers().add(e);
										e.doResearch(research);
									}
								}
							}
							if(found) partNumber -= 1;
							else System.out.println("\nThis user is not researcher !");
						}

						DataBase.serilaizeResearches();
					}
					System.out.println("\nThe research was successfully published !");
					break;
				case "back":
					DataBase.serilaizeResearches();
					return;
				default:
					System.out.println("Invalid index, select again: ");
					break;
				}
			}
		}
	}

	public static void seeResearches() throws IOException {
		System.out.println("\nAll researches:"); int cnt = 1;
		
		for(Research r: DataBase.researches) {
			System.out.println(cnt + ". " + r); cnt += 1;
		}
		
		while(true) {
			System.out.print("\n1. Enter the name of the research to view it\n2. Enter 'back' to return\n\nEnter name or 'back': ");
			
			String chosen = br.readLine();
			if(chosen.toLowerCase().equals("back")) return;
			
			for(Research r: DataBase.researches) {
				if(chosen.toLowerCase().equals(r.getName().toLowerCase())) {
					String desc = ""; int size = 80;
					
					if(r.getDescription().length() > 80) {
						for(int i = 0; i < r.getDescription().length(); i++) {
							desc += r.getDescription().charAt(i);
							if(desc.length() > size && r.getDescription().charAt(i) == ' ') {
								desc += "\n"; size += 80;
							}
						}
					} else desc = r.getDescription();
					
					System.out.print("\nResearch name: " + r.getName() + "\n-\nResearch description:\n" + desc + "\n-\nAuthors: ");
					
					for(User u: r.getResearchers()) {
						System.out.print(u.getFirstName() + " " + u.getLastName() + (r.getResearchers().lastElement() != u ? ", " : "."));
						for(TypeUser tu: DataBase.users.keySet()) {
							for(User us: DataBase.users.get(tu)) {
								if(us instanceof Student && us.getId().equals(u.getId()) && !us.getId().equals(student.getId())) ((Student)us).sethIndex(((Student)us).gethIndex() + 0.1);
								else if(us instanceof Employee && us.getId().equals(u.getId())) ((Employee)us).sethIndex(((Employee)us).gethIndex() + 0.1);
								DataBase.serilaizeUsers();
							}
						}
					} System.out.println();
				}
			}
		}
	}
}