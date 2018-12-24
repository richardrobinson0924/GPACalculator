package gpacalculator.eecs;

import java.io.File;
import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception {
		String path = "/Users/richardrobinson/Documents/GPACalculator/test.txt";
		Scanner sc = new Scanner(new File(path));

		List<Course> courses = new ArrayList<>();

		while (sc.hasNextLine()) {
			try {
				courses.add(new Course(sc.nextLine()));
			} catch (IllegalArgumentException ignored) {
			}
		}

		courses.add(new Course("LE EECS 2200", 75.0, 3.00));
		Collections.sort(courses);

		double sum = 0;
		int totalCredits = 0;

		for (Course c : courses) {
			System.out.println(c);
			sum += c.getGrade().normalize() * c.getCredits();
			totalCredits += c.getCredits();
		}

		System.out.printf("\nYour GPA is: %.2f", sum / totalCredits);

	}
}
