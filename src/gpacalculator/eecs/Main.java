package gpacalculator.eecs;

import java.io.File;
import java.util.*;

public class Main {

	public static void main(String... args) throws Exception {
		String path = "test.txt";
		Scanner sc = new Scanner(new File(path));

		List<Course> courses = new ArrayList<>();

		while (sc.hasNextLine()) try {
			courses.add(new Course(sc.nextLine()));
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

		Collections.sort(courses);

		double sum = 0;
		int totalCredits = 0;

		for (Course c : courses) {
			//System.out.println(c + " : " + c.getGrade());
			sum += c.getGrade().normalize() * c.getCredits();
			totalCredits += c.getCredits();
		}

		System.out.printf("Your GPA is: %.2f", sum / totalCredits);

	}
}
