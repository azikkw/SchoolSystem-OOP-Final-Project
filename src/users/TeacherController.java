package users;

import java.io.BufferedReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Vector;
import java.util.stream.Collectors;
import attributes.*;
import enums.*;
import interfaces.*;
import java.lang.Double;

public class TeacherController implements ViewableAndDrawupable, CanManageCourse
{
	static Teacher teacher = null; static Dean dean = null;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void logIn(Teacher t) throws IOException {
		teacher = t;
		viewMenu();
	}

    static void viewMenu() throws IOException {
    	while(true) {
    		System.out.println("\nMenu:\n" +
	    						"1. My profile\n" +
	    						"2. View user information\n" +
	    						"3. Research\n" +
	    						"4. View researches\n" +
	    						"5. View my courses\n" +
	    						"6. View schedule\n" +
	    						"7. Draw up schedule\n" +
	    						"8. Put mark\n" +
	    						"9. Student journal\n" +
	    						"10. Student attestation\n" +
	    						"11. Manage course\n" +
	    						"12. Send request\n" +
	    						"13. View Statistical reports\n" +
	    						"14. See news\n" +
	    						"15. Get book\n" +
	    						"16. Send message\n" + 
	    						"17. Read my messages\n" + 
	    						"18. Log-out\n");
    	
			String choosen = br.readLine();
			
			switch(choosen) {
			case "1":
				UserController.viewProfile(teacher);
				break;
			case "2":
				viewInfo();
				break;
			case "3":
				EmployeeController.doResearch(teacher);
				break;
			case "4":
				EmployeeController.seeResearches(teacher);
				break;
			case "5":
				viewCourses();
				break;
			case "6":
				viewSchedule(teacher);
				break;
			case "7":
				drawUpSchedule();
				break;
			case "8":
				putMark();
				break;
			case "9":
				viewJournal();
				break;
			case "10":
				viewAttestation();
				break;
			case "11":
				manageCourse();
				break;
			case "12":
				sendRequest();
				break;
			case "13":
				break;
			case "14":
				System.out.println("Choose news column:");
				System.out.println("1. Official\n"
				      		+ "2. Upcoming events\n"
				    		+ "3. Lost and found\n");
				String type = br.readLine();
				if(type.equals("1")) type = "Official";
				if(type.equals("2")) type = "Upcoming events";
				if(type.equals("3")) type = "Lost and found";
				if(teacher.seeNews(type).isEmpty()) System.out.println("There is no event\n");
				else System.out.println(teacher.seeNews(type));
				break;
			case "15":
				if(Librarian.givenBooks.get(teacher) != null) {
					System.out.println("Books that you already got:\n" + Librarian.givenBooks.get(teacher));
					for(Literature l: Librarian.givenBooks.get(teacher)) {
						System.out.println(l.getName() + ":");
						Period time = Period.between(LocalDate.now(), l.getDeadline().plusDays(3));
						System.out.println("    number of months: " + time.getMonths() + 
										   "    number of days: " + time.getDays());
					}
				}
				else {
					Librarian.givenBooks.put(teacher, new Vector<>());
				}
				if(Librarian.availableBooks.isEmpty()) break;
				System.out.println("Available books:\n" + Librarian.availableBooks);
				System.out.print("\nEnter book's name that you want to get: ");
				String title = br.readLine();
				System.out.print("\nEnter book's author that you want to get: ");
				String author= br.readLine();
				Literature book = Librarian.getBook(title, author);
				Librarian.availableBooks.remove(book);
				book.setDeadline(LocalDate.now());
				Librarian.givenBooks.get(teacher).add(book);
				
				break;
			case "16":
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
				Vector<User> receivers = teacher.findEmployeeList(position, name, surname);
				System.out.println(receivers);
				String id = br.readLine();
				Employee e = teacher.findEmployee(id, receivers);
				if(e != null) {
					System.out.println("Enter your message");
					String message = br.readLine();
					teacher.sendMessage(e, new Message(teacher.getFirstName() + " " + teacher.getLastName(), message, LocalDate.now()));
				}
				else {
					System.out.println("There is no such employee!");
				}
				break;
			case "17":
				System.out.println(teacher.getMyMessages());
				break;
			case "18":
				System.out.println();
				Admin.getAdmin().setLogFiles(new Action(LocalDate.now(), teacher, "logged out"));
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
    
	public static Teacher getTeacher() throws IOException{
    	System.out.print("\n1. Enter password: ");
    	String password = br.readLine();
    	System.out.print("\n2. Enter first name: ");
    	String firstName = br.readLine();
    	System.out.print("\n3. Enter last name: ");
    	String lastName = br.readLine();
    	System.out.print("\n4. Enter age: ");
    	int age = Integer.parseInt(br.readLine());
    	System.out.print("\n5. Choose type of teacher from list:\n" + Arrays.asList(enums.TypeTeacher.values()) + "\n\nEnter type: ");
    	String teacherType= br.readLine().toUpperCase();
    	try {
			enums.TypeTeacher.valueOf(teacherType);
		}
		catch(IllegalArgumentException e){
				System.out.println("Error, no such variant! Enter again:  ");
				teacherType = br.readLine().toUpperCase();
		}
    	System.out.print("\n6. Choose faculty from list:\n" + Arrays.asList(enums.Faculty.values()) + "\n\nEnter faculty: ");
    	String facultyName = br.readLine().toUpperCase();
    	try {
			enums.Faculty.valueOf(facultyName);
		}
		catch(IllegalArgumentException e){
				System.out.println("Error, no such variant! Enter again:  ");
				facultyName = br.readLine().toUpperCase();
		}
    	Teacher t = new Teacher(password, firstName, lastName, age, enums.TypeTeacher.valueOf(teacherType), enums.Faculty.valueOf(facultyName));
    	System.out.print("\n7. Id of new teacher is:  " + t.getId() + "\n");
    	return t;
    }
    
    public static void viewInfo() throws IOException {
		System.out.println("\n1. Enter the ID of the student to view information about him\n2. Enter back for return");

    	while(true) {
    		System.out.print("\nEnter ID or 'back': ");
    		String userId = br.readLine();
    		
    		boolean found = false;
		
			if(userId.toLowerCase().equals("back")) return;
			for(Lesson l: teacher.getLessons().keySet()) {
				for(Student s: teacher.getLessons().get(l)) {
					if(s.getId().toLowerCase().equals(userId.toLowerCase())) {
						found = true;
						System.out.println(s + "\n-\n" + "Course: " + l.getCourse().getName() + ", " + l.getType());
					}
				}
			}
			
			if(!found) System.out.println("You don't have such a student in your course.");;
    	}
    }

    public static void viewAttestation() throws IOException {
    	System.out.println("\nList of your students: "); int cnt = 0;
		for(Lesson l: teacher.getLessons().keySet()) {
			for(Student s: teacher.getLessons().get(l)) {
				System.out.println(cnt + ". " + s.getId() + ": " + s.getFirstName() + " " + s.getLastName() + ", course: " + l.getCourse().getName()); 
				cnt += 1;
			}
		}
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
	
	public static void viewCourses() {
		System.out.println("\nYou current courses:"); int cnt = 1;
		for(Lesson l : teacher.getLessons().keySet()){
			System.out.println(cnt + ". " + l.getDescription()); cnt += 1;
		}
	}
	
	public static void manageCourse() throws IOException {
	      while(true) {
	      try {
	          viewCourses();
	          System.out.print("Choose course: ");
	          String courseName = br.readLine();
	          Course course = DataBase.courses.stream().filter(c->c.getName().toLowerCase().equals(courseName.toLowerCase())).findFirst().get();
	          
	          System.out.println("Choose one of actions:\n1 - Close first attestation\n2 - Close second attestation\n3 - Put final mark");
	          String chosen = br.readLine();
	        HashSet <Student> students = new HashSet <>();
	        for(Lesson lesson : teacher.getLessons().keySet()) {
	          for(Student student : teacher.getLessons().get(lesson)) {
	            if(lesson.getCourse().getName().equals(course.getName())) students.add(student);
	          }
	        }
	          switch(chosen) {
	          case "1":
	            System.out.println("Do you want to close 1st attestation? (1 - YES, 2 - NO)");
	            if(Integer.parseInt(br.readLine()) == 1) {
	                students.stream().forEach(s->  s.getTranscript().getSemesters().add(new Semester(1)));
	                students.stream().forEach(s->s.getTranscript().getSemesters().lastElement().getAttestations().computeIfAbsent(course, k -> new Attestation(course, s.getJournal().getTotal(course))));
	                students.stream().forEach(s-> s.getJournal().clear());
	                DataBase.serilaizeUsers();
	            }
	            break;
	          case "2":
	            System.out.println("Do you want to close 2st attestation? (1 - YES, 2 - NO)");
	            if(Integer.parseInt(br.readLine()) == 1) {
	              students.stream().forEach(s->s.getTranscript().getSemesters().lastElement().getAttestations().get(course).setSecondAttestation(s.getJournal().getTotal(course)));
	              DataBase.serilaizeUsers();
	            }
	            break;
	          case "3":
	            students.stream().filter(s-> (s.getTranscript().getSemesters().lastElement().getAttestations().get(course).getFirstAttestation() + s.getTranscript().getSemesters().lastElement().getAttestations().get(course).getSecondAttestation()) > 30).forEach(s->{
	            try {
	              System.out.print(s.getId() + " " + s.getLastName() + " " + s.getFirstName() + ": ");
	              s.getTranscript().getSemesters().lastElement().getAttestations().get(course).setFinalExam(Double.parseDouble(br.readLine()));
	            } catch (NumberFormatException | IOException e) {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	            }
	          });
	            students.stream().filter(s-> (s.getTranscript().getSemesters().lastElement().getAttestations().get(course).getFirstAttestation() + s.getTranscript().getSemesters().lastElement().getAttestations().get(course).getSecondAttestation() < 30)).forEach(s->s.getCourses().remove(course));
	            students.stream().filter(s-> (s.getTranscript().getSemesters().lastElement().getAttestations().get(course).getFirstAttestation() + s.getTranscript().getSemesters().lastElement().getAttestations().get(course).getSecondAttestation() < 30)).forEach(n-> System.out.println(n.getFirstName() + n.getLastName() + " gets retake"));
	            DataBase.serilaizeUsers();
	            break;
	          }
	          } 
	      catch(Exception e) {
	            System.out.print("Something went wrong. ");
	          } 
	      finally {
	              System.out.print("Write \"Continue\" or \"Back\": ");
	              if(br.readLine().toLowerCase().equals("back")) break;
	          }
	    }
	}
    
    public static void putMark() throws IOException {
    	while(true) {
			try {
				viewCourses();
				System.out.print("Choose course: ");
				String courseName = br.readLine().toLowerCase();
				System.out.print("Choose your class: ");
				teacher.getLessons().keySet().stream().filter(c-> c.getCourse().getName().toLowerCase().equals(courseName)).forEach(c-> System.out.print(c.getType() + " "));
				System.out.println();
				String className = br.readLine().toLowerCase();
				Lesson lesson = (Lesson) teacher.getLessons().keySet().stream().filter(l->l.getType().toString().toLowerCase().equals(className) && l.getCourse().getName().toLowerCase().equals(courseName)).findFirst().get();
				System.out.println("List of students: ");
				teacher.getLessons().get(lesson).stream().forEach(s-> System.out.println(s.getId() + ": " + s.getFirstName() + " " + s.getLastName()));
				System.out.print("Choose student by id: ");
				String id = br.readLine();
				Student s = teacher.getLessons().get(lesson).stream().filter(st->st.getId().equals(id)).findFirst().get();
				System.out.print("Write the mark: ");
				double mark = Double.parseDouble(br.readLine());
				System.out.print("\nWrite the description: ");
				String description = br.readLine();
				Mark m = new Mark(teacher, mark, new GregorianCalendar(), description);
				System.out.println(teacher.putMark(s, m, lesson) ? "The mark is in the system" : "You can't put mark more than 30 in journal");
        	} 
			catch(Exception e) {
        		System.out.print("Something went wrong. ");
        	} 
			finally {
            	System.out.print("Write \"Continue\" or \"Back\": ");
            	if(br.readLine().toLowerCase().equals("back")) break;
        	}
		}
    }

    public void viewStatisticalReport() {
    }

	public static void viewJournal() throws IOException {
		System.out.println("\nList of your students: "); int cnt = 0;
		for(Lesson l: teacher.getLessons().keySet()) {
			for(Student s: teacher.getLessons().get(l)) {
				System.out.println(cnt + ". " + s.getId() + ": " + s.getFirstName() + " " + s.getLastName() + ", course: " + l.getCourse().getName()); 
				cnt += 1;
			}
		}
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

	public static void viewSchedule(Teacher t) throws IOException {
		//teacher.getLessons().keySet().stream().filter(l->l.getBegin()!=-1 && l.getDay()!=null).forEach(l-> System.out.println(l));
		Schedule sc1 = new Schedule(DayOfWeek.MONDAY);
		Schedule sc2 = new Schedule(DayOfWeek.TUESDAY);
		Schedule sc3 = new Schedule(DayOfWeek.WEDNESDAY);
		Schedule sc4 = new Schedule(DayOfWeek.THURSDAY);
		Schedule sc5 = new Schedule(DayOfWeek.FRIDAY);
		Schedule sc6 = new Schedule(DayOfWeek.SATURDAY);
		
		for(Lesson lessonsTeacher: t.getLessons().keySet()) {
			if(lessonsTeacher.getDay() == sc1.getWeekDay()) {
				sc1.getLessons().add((lessonsTeacher.getCourse().getName() + ": " + t.getLastName() + " " + t.getFirstName().charAt(0) + ". " + (lessonsTeacher.getType() == TypeLesson.LABORATORY ? lessonsTeacher.getType().toString().substring(0, 3) : lessonsTeacher.getType().toString().substring(0, 4))  + ", " + lessonsTeacher.getBegin() + "-" + (lessonsTeacher.getBegin() + lessonsTeacher.getDuration())));
			}
			else if(lessonsTeacher.getDay() == sc2.getWeekDay()) {
				sc2.getLessons().add((lessonsTeacher.getCourse().getName() + ": " + t.getLastName() + " " + t.getFirstName().charAt(0) + ". " + (lessonsTeacher.getType() == TypeLesson.LABORATORY ? lessonsTeacher.getType().toString().substring(0, 3) : lessonsTeacher.getType().toString().substring(0, 4)) + ", " + lessonsTeacher.getBegin() + "-" + (lessonsTeacher.getBegin() + lessonsTeacher.getDuration())));
			}
			else if(lessonsTeacher.getDay() == sc3.getWeekDay()) {
				sc3.getLessons().add((lessonsTeacher.getCourse().getName() + ": " + t.getLastName() + " " + t.getFirstName().charAt(0) + ". " + (lessonsTeacher.getType() == TypeLesson.LABORATORY ? lessonsTeacher.getType().toString().substring(0, 3) : lessonsTeacher.getType().toString().substring(0, 4)) + ", " + lessonsTeacher.getBegin() + "-" + (lessonsTeacher.getBegin() + lessonsTeacher.getDuration())));
			}
			else if(lessonsTeacher.getDay() == sc4.getWeekDay()) {
				sc4.getLessons().add((lessonsTeacher.getCourse().getName() + ": " + t.getLastName() + " " + t.getFirstName().charAt(0) + ". " + (lessonsTeacher.getType() == TypeLesson.LABORATORY ? lessonsTeacher.getType().toString().substring(0, 3) : lessonsTeacher.getType().toString().substring(0, 4)) + ", " + lessonsTeacher.getBegin() + "-" + (lessonsTeacher.getBegin() + lessonsTeacher.getDuration())));
			}
			else if(lessonsTeacher.getDay() == sc5.getWeekDay()) {
				sc5.getLessons().add((lessonsTeacher.getCourse().getName() + ": " + t.getLastName() + " " + t.getFirstName().charAt(0) + ". " + (lessonsTeacher.getType() == TypeLesson.LABORATORY ? lessonsTeacher.getType().toString().substring(0, 3) : lessonsTeacher.getType().toString().substring(0, 4)) + ", " + lessonsTeacher.getBegin() + "-" + (lessonsTeacher.getBegin() + lessonsTeacher.getDuration())));
			}
			else if(lessonsTeacher.getDay() == sc6.getWeekDay()) {
				sc6.getLessons().add((lessonsTeacher.getCourse().getName() + ": " + t.getLastName() + " " + t.getFirstName().charAt(0) + ". " + (lessonsTeacher.getType() == TypeLesson.LABORATORY ? lessonsTeacher.getType().toString().substring(0, 3) : lessonsTeacher.getType().toString().substring(0, 4)) + ", " + lessonsTeacher.getBegin() + "-" + (lessonsTeacher.getBegin() + lessonsTeacher.getDuration())));
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
				sched[i][j] = " ".repeat(30);
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
			System.out.println("-".repeat(187));
			for(int j = 0; j < 6; j++) {
				System.out.print("|");
				System.out.print(sched[i][j]);
			}
			System.out.println("|");
		}
		
		System.out.println("-".repeat(187));

		System.out.print("\nEnter 'back' to return: ");
		
		String returnBack = br.readLine();
		if(returnBack.toLowerCase().equals("back")) return;
	}

	public static void drawUpSchedule() throws IOException {
		while(true) {
			try {
				ArrayList <Lesson> lessons = (ArrayList<Lesson>) teacher.getLessons().keySet().stream().filter(l->l.getDay()==null && l.getBegin()==-1).collect(Collectors.toList());
				for(Lesson lesson : lessons) {
					System.out.print("\nChoose day for " + lesson.getCourse().getName() + " " + lesson.getType().toString().toLowerCase() + ": ");
					Arrays.asList(DayOfWeek.values()).stream().forEach(d-> System.out.print(d + " "));
					System.out.println();
					DayOfWeek day = DayOfWeek.valueOf(br.readLine().toUpperCase());
					System.out.println("Choose begin time for " + lesson.getCourse().getName() + " (duration " + lesson.getDuration() + " hours)");
					teacher.drawUpSchedule(lesson, day, Integer.parseInt(br.readLine()));
					System.out.println("You have successfully drawn up your schedule for " + lesson.getCourse().getName() + " " + lesson.getType().toString().toLowerCase());
				}
        	} 
			catch(Exception e) {
        		System.out.print("Something went wrong. ");
        	} 
			finally {
            	System.out.print("Write \"Continue\" or \"Back\": ");
            	if(br.readLine().toLowerCase().equals("back")) break;
        	}
		}
	}
	
	public static void sendRequest() throws IOException {
		System.out.println("\nWho do you want to send the request to:\nTo return back, enter - back\n\n" +
						  "1. For dean, enter - d\n2. For manager, enter - m\n3. For admin, enter - a");
		
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
				
				teacher.sendRequest(new Request(teacher, dean, "rem", requestMess));
				System.out.println("\nSuccessfully sent !");
			}
			if(user.toLowerCase().equals("m")) {
				// Soon
			}
			if(user.toLowerCase().equals("a")) {
				System.out.print("\nYou can send a request to the admin only to change the password.\nEnter a new password or 'back': ");
				String requestMess = br.readLine();
				if(requestMess.toLowerCase().equals("back")) return;
				teacher.sendRequest(new Request(teacher, Admin.getAdmin(), "cp", requestMess));
				System.out.println("\nSuccessfully sent !");
			}
		}
	}
}