package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;

import attributes.Action;
import attributes.DataBase;
import attributes.Literature;
import enums.Faculty;
import enums.TypeCourse;
import enums.TypeLesson;

public class UserController
{
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void logIn() throws IOException {
		System.out.println("Hello user. Welcome to WSP!");
		while(true) {
			System.out.println("For exit enter - exit.\n");
			
			System.out.print("Enter your ID: ");
			String id = br.readLine();
			if(id.toLowerCase().equals("exit")) {System.out.println("\nBye-bye. Have a nice day!");DataBase.serilaizeUsers();break;}
			
			System.out.print("Enter your password: ");
			String password = br.readLine();
			if(password.toLowerCase().equals("exit")) {System.out.println("\nBye-bye. Have a nice day!");DataBase.serilaizeUsers();break;}
			
			User u = DataBase.isRegistered(id, password);
			
			if(u == null) System.out.println("\nError. Incorrect ID or password. Try again:\n");
			else {
				if(u instanceof Student) {
					Student s = (Student)u;
					if(Librarian.givenBooks.get(s) !=null) {
					for(Literature l : Librarian.givenBooks.get(s)) {
						if(Period.between(LocalDate.now(), l.getDeadline().plusDays(3)).isNegative()) {
							s.setStatus(false);
							DataBase.serilaizeUsers();
						}
					}
					}
				}
				if(u instanceof Teacher) {
					Teacher t = (Teacher)u;
					if(Librarian.givenBooks.get(t) !=null) {
					for(Literature l : Librarian.givenBooks.get(t)) { 
						if(Period.between(LocalDate.now(), l.getDeadline().plusDays(3)).isNegative()) {
							t.setStatus(false);
							DataBase.serilaizeUsers();
						}
					}
					}
				}
				
				if(!u.isStatus()) {System.out.println("\nYour account is blocked, to find out the reason, contact the dean's office."); break;}
				System.out.println("\nHello " + u.getFirstName() + ". What's your focus for today ?");
				Admin.getAdmin().setLogFiles(new Action(LocalDate.now(), u, "logged in")); Admin.serilaizeLogFiles();
				
				if(u instanceof Student) {
					Student s = (Student)u;
					StudentController.logIn(s);
				}
				if(u instanceof Teacher) {
					Teacher t = (Teacher)u;
					TeacherController.logIn(t);
				}
				if(u instanceof Manager) {
					Manager m = (Manager)u;
					ManagerController.logIn(m);
				}
				if(u instanceof Dean) {
					Dean d = (Dean)u;
					DeanController.logIn(d);
				}
				if(u instanceof Librarian) {
					Librarian l = (Librarian)u;
					LibrarianController.logIn(l);
				}
				if(u instanceof Admin) {
					Admin a = (Admin) u;
					AdminController.logIn(a);
				}
			}
		}
		
	}
	
    public static <T> void viewProfile(T t) throws IOException {
    	System.out.println("\nMy profile:\n\n" + ((User)t).forProfile());
    	System.out.print("\nEnter 'back' for return: ");
    	
    	String back = br.readLine();
    	if(back.toLowerCase().equals("back")) return;
    }
	
	public static HashMap <Integer, TypeCourse> typeCourse = new HashMap <Integer, TypeCourse>() {
		private static final long serialVersionUID = 302373120887310094L;
		{
		    put(1, TypeCourse.REQUIRED);
		    put(2, TypeCourse.MAJOR);
		    put(3, TypeCourse.MINOR);
		    put(4, TypeCourse.FREE);
		}
	};
	public static HashMap <Integer, Faculty> faculties = new HashMap <Integer, Faculty>() {
		private static final long serialVersionUID = -4722709368014246826L;
		{
		    put(1, Faculty.FIT);
		    put(2, Faculty.BS);
		    put(3, Faculty.ISE);
		}
	};
	public static HashMap <Integer, TypeLesson> typeLesson = new HashMap <>() {
		private static final long serialVersionUID = -6689655258892541009L;
		{
		    put(1, TypeLesson.LECTURE);
		    put(2, TypeLesson.PRACTICE);
		    put(3, TypeLesson.LABORATORY);
		}
	};
}