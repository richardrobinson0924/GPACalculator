package gpacalculator.eecs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

	private int[] expected = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

	@Test
	void percentGradeToGPA() {
		int[] actual = new int[10];
		Double[] percentsGrades = {100.0, 89.0, 79.0, 74.0, 69.0, 64.0, 59.0, 54.0, 49.0, 0.0};

		for (int i = 0; i < 10; i++) {
			actual[i] = Course.toGrade(percentsGrades[i]);
		}

		assertArrayEquals(actual, expected);
	}

	@Test
	void letterGradeToGPA() {
		int[] actual = new int[10];
		String[] letterGrades = { "A+", "A", "B+", "B", "C+", "C", "D+", "D", "E", "F"};

		for (int i = 0; i < 10; i++) {
			actual[i] = Course.toGrade(letterGrades[i]);
		}

		assertArrayEquals(actual, expected);
	}
}