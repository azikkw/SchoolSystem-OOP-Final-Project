package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import attributes.DataBase;
import attributes.Research;
import enums.TypeUser;

public class EmployeeController 
{
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void doResearch(Employee employee) throws IOException {
		if(!employee.isResearchStatus()) {
			System.out.print("\nTo return back, enter - back.\n\nHello, you are not a researcher at the moment.\nIf you want to become one, enter 'yes'.\n\nEnter 'yes' or 'back': ");
			String answer = br.readLine();
			if(answer.toLowerCase().equals("back")) return;
			if(answer.toLowerCase().equals("yes")) {
				employee.setResearchStatus(true); System.out.println("\nCongratulations, now you have become a researcher !");
				DataBase.serilaizeUsers();
			}
		}
		else {
			Research research = null;
			System.out.println("\nHello, " + employee.getFirstName() + ". Your h-index: " + employee.gethIndex() + "\nWhat kind of scientific research do you want to surprise the world with today?");
			while(true) {
				System.out.print("\n1. To view your researches, enter - vr\n2. To publish a new research, enter - pr\n3. To return back, enter - back\n\nEnter: ");
				String chosen = br.readLine();
				switch(chosen) {
				case "vr":
					System.out.println("\nYour researches:"); int cnt = 1;
					for(Research r: DataBase.researches) {
						for(User u: r.getResearchers()) {
							if(employee.getId().equals(u.getId())) {
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
						employee.doResearch(research);
						research.getResearchers().add(employee);
					}
					else {
						employee.doResearch(research);
						research.getResearchers().add(employee);
						
						int partNumber = Integer.parseInt(researchPart);
						
						while(partNumber != 0) {
							System.out.print("\nEnter 'ID' of your team member: ");
							String memberId = br.readLine(); boolean found = false;
							for(TypeUser tu: DataBase.users.keySet()) {
								for(User u: DataBase.users.get(tu)) {
									if(u instanceof Student && memberId.toLowerCase().equals(u.getId()) && ((Student)u).isResearchStatus()) {
										Student s = (Student)u; found = true; 
										research.getResearchers().add(s);
										s.doResearch(research);
									}
									if(u instanceof Employee && memberId.toLowerCase().equals(u.getId()) && ((Employee)u).isResearchStatus()) {
										Employee e = (Employee)u; found = false;
										research.getResearchers().add(e);
										e.doResearch(research);
									}
								}
							}
							if(found) partNumber -= 1;
							else System.out.println("\nThis user is not researcher !");
						}
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

	public static void seeResearches(Employee employee) throws IOException {
		System.out.println("\nAll researches:"); int cnt = 1;

		for(Research r: DataBase.researches) {
			System.out.println(cnt + ". " + r); cnt += 1;
		}
		
		while(true) {
			System.out.print("\n1. Enter the name of the study to view it\n2. Enter 'back' to return\n\nEnter name or 'back': ");
			
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
								if(us instanceof Student && us.getId().equals(u.getId())) ((Student)us).sethIndex(((Student)us).gethIndex() + 0.1);
								else if(us instanceof Employee && us.getId().equals(u.getId()) && !us.getId().equals(employee.getId())) ((Employee)us).sethIndex(((Employee)us).gethIndex() + 0.1);
								DataBase.serilaizeUsers();
							}
						}
					} System.out.println();
				}
			}
		}
	}
}