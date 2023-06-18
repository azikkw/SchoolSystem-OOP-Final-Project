package attributes;

public class GpaConverter {
	public static double getGPA(double mark) {
		if(mark > 95) return 4.00;
		if(mark > 90) return 3.67;
		if(mark > 85) return 3.33;
		if(mark > 80) return 3.0;
		if(mark > 75) return 2.67;
		if(mark > 70) return 2.33;
		if(mark > 65) return 2.0;
		if(mark > 60) return 1.67;
		if(mark > 55) return 1.33;
		if(mark > 50) return 1.00;
		return 0;
	}
}
