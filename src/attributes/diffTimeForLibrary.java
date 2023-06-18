package attributes;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;


public class diffTimeForLibrary implements Serializable 
{
	private static final long serialVersionUID = 9081350926669302634L;

	public static void main(String[] args) 
	{
		LocalDateTime bday = LocalDateTime.of(2022, Month.DECEMBER, 21, 23, 59); 
		LocalDateTime today = LocalDateTime.now( ); 
		Duration age = Duration.between(bday, today);
		
		System.out.println(age.isNegative());
//		int years = age..getYears(); 
//		int months = age.getMonths();
//		int days = age.getDays();
		
//		System.out.println("number of days: " + days);
//		System.out.println("number of years: " + years);
//	    System.out.println("number of months: " + months);
	}
}
