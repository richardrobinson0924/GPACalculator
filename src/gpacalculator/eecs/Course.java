package gpacalculator.eecs;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2018 Richard Robinson. All rights reserved.
 * @see "https://github.com/richardrobinson0924/GPACalculator"
 */
public class Course implements Comparable<Course> {

	/**
	 * The name of the course, the number of credits it is worth, and the converted 9-scale grade
	 */
	private String name;
	private int numCredits;
	private double grade;

	/**
	 * A regex pattern for use with scanners of the default input string for extracting the
	 * course code and number of credits with format:
	 * <p>
	 * {@code ... \t LE EECS 1234 3.00 E \t ...}
	 * </p>
	 * in which "EECS 2021" is the course name (capture group 1), and "3" is the number of credits
	 * (group 2).
	 *
	 * Note, the remaining values of the entire default string are otherwise tab-delimted.
	 *
	 * @see java.util.regex.Pattern
	 */
	public static final Pattern p = Pattern.compile(Course.REGEX);
	private static final String REGEX = "\\w{2} (\\w{3,4} {1,2}\\d{4}) {2}(\\d).00 \\w";

	/**
	 * The constructor if the number of credits is not provided. In this case, the default number
	 * of credits is 3.
	 *
	 * @param name        the name of the course
	 * @param beforeGrade the raw grade in letter (String) or percent (double) format
	 * @param <T>         Either a String-based type or a Number-based type
	 */
	public <T> Course(String name, T beforeGrade) {
		this(name, beforeGrade, 3);
	}

	/**
	 * The default constructor for a course. Consists of a name, raw grade, and number of credits.
	 *
	 * @param name        the name of the course
	 * @param beforeGrade the raw grade in letter (String) or percent (double) format
	 * @param numCredits  the number of credits the course is worth
	 * @param <T>         Either a String-based type or a Number-based type
	 * @throws ClassCastException       if `beforeGrade` type not valid
	 * @throws IllegalArgumentException if `beforeGrade` is not a valid grade
	 */
	public <T> Course(String name, T beforeGrade, int numCredits) {
		throwPossibleExceptions(beforeGrade);

		this.name = name;
		this.grade = toGrade(beforeGrade);
		this.numCredits = numCredits;
	}

	/**
	 * I have absolutely no idea why this method is necessary because of generic types and
	 * overloading, but here we are. Any tips?
	 * <p>
	 * Generalized {@code toGrade()} method for specification. This method simply casts and passes
	 * its
	 * argument to its respective overloaded method depending on the type of {@code beforeGrade}
	 *
	 * @param beforeGrade the String or double grade of the course
	 * @param <T>         Either a String-based type or a Number-based type
	 * @return the result of `toGrade(String grade)` or `toGrade(double
	 * grade)` depending on the class
	 * @see #toGrade(Number) overloaded toGrade(N extends Number)
	 * @see #toGrade(String) overloaded toGrade(S extends String)
	 */
	public static <T> int toGrade(T beforeGrade) {
		return beforeGrade.getClass().equals(String.class)
				? toGrade((String) beforeGrade)
				: toGrade((Double) beforeGrade);
	}

	/**
	 * Converts a course's letter grade to its equivalent on the 9.0 scale
	 *
	 * @param letterGrade the grade in letter format  (e.g, {@code A+}) to be converted
	 * @param <S>         the type of the grade, limited to String-based types
	 * @return the converted grade
	 */
	private static <S extends String> int toGrade(S letterGrade) {
		int grade;

		switch (letterGrade.charAt(0)) {
			case 'A':
				grade = 8;
				break;
			case 'B':
				grade = 6;
				break;
			case 'C':
				grade = 4;
				break;
			case 'D':
				grade = 2;
				break;
			case 'E':
				grade = 1;
				break;
			default:
				grade = 0;
				break;
		}

		if (letterGrade.endsWith("+"))
			grade++;
		return grade;
	}

	/**
	 * Converts a course's percent grade to its equivalent on the 9.0 scale
	 *
	 * @param percentGen the grade in percent format (e.g, {@code 75}) to be converted
	 * @param <N>        the type of the grade, limited to types which extend {@code Number} class
	 * @return the converted grade
	 */
	private static <N extends Number> int toGrade(N percentGen) {
		Double percent = (Double) percentGen;

		if (percent >= 90) return 9;
		if (percent >= 80) return 8;

		int base = 75;
		for (int i = 7; i >= 2; i--) {
			if (percent >= base) return i;
			base -= 5;
		}

		return (percent >= 40) ? 1 : 0;
	}

	/**
	 * Creates and sorts an ArrayList of courses, whose length is the length of the parameters'
	 * arrays. The
	 * method accepts three arrays corresponding to the parameters of {@code Course}
	 *
	 * @param names        the array of the values of the names for the courses
	 * @param beforeGrades the array of the values of the grades for the courses
	 * @param credits      the array of the values of the credits for the courses
	 * @param <T>          the type, one of {Double, String}, of the grades
	 * @return A list of courses processed from the three arrays of data, matched based on indices
	 * @throws IndexOutOfBoundsException if the length of any parameter array does not match the
	 *                                   other two
	 * @see #compareTo(Course) the compareTo() method used to sort the list of Courses
	 */
	public static <T> List<Course> makeList(List<String> names, List<T> beforeGrades,
	                                        List<Integer> credits) {
		if (names.size() != beforeGrades.size() || names.size() != credits.size()) {
			throw new IndexOutOfBoundsException("One of the params has a different length");
		}

		List<Course> list = new ArrayList<>();

		for (int i = 0; i < names.size(); i++) {
			Course course = new Course(names.get(i), beforeGrades.get(i), credits.get(i));
			list.add(course);
		}

		Collections.sort(list);
		return list;
	}

	/**
	 * Returns the grade of the course
	 *
	 * @return the grade of the course on 9.0 scale
	 */
	public double getGrade() {
		return this.grade;
	}

	/**
	 * Returns the number of credits for the course
	 *
	 * @return the number of credits for the course
	 */
	public int getNumCredits() {
		return this.numCredits;
	}

	/**
	 * Returns the name of the course
	 *
	 * @return the name of the course
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sorts a list of Courses in increasing order
	 *
	 * @param o the target object
	 * @return Within {1, 0, -1} in accordance with `Double.compare()`
	 */
	@Override
	public int compareTo(Course o) {
		int resultGrade = Double.compare(this.getGrade(), o.getGrade());

		return (resultGrade == 0)
				? Integer.compare(this.getNumCredits(), o.getNumCredits())
				: resultGrade;
	}

	/**
	 * Formats a Course object as a string in the following format:
	 * <p>
	 * {@code Course{name='EECS 1234', numCredits=3, grade='A+'}}
	 * </p>
	 *
	 * @return the formatted course as a string
	 */
	@Override
	public String toString() {
		return "Course{name='" + getName() +
				"\', numCredits=" + getNumCredits() +
				", grade=\'" + getGrade() + "\'}";
	}

	/**
	 * A helper method to throw possible exceptions for incorrect usages of `beforeGrade`
	 *
	 * @param beforeGrade the raw grade of the course
	 * @param <T>         the type, one of {Double, String}, of the grades
	 */
	private <T> void throwPossibleExceptions(T beforeGrade) {
		Class argClass = beforeGrade.getClass();
		String regex = "[ABCDEF]\\+?";

		if (!argClass.equals(String.class) && !argClass.equals(Double.class)) {
			throw new ClassCastException("Grade of invalid type. Must be Double or Strings.");
		}

		if (argClass.equals(Double.class) && ((Double) beforeGrade < 0 || (Double) beforeGrade > 100)) {
			throw new IndexOutOfBoundsException("Must be within [0, 100]");
		}

		if (argClass.equals(String.class) && !beforeGrade.toString().matches(regex)) {
			throw new IndexOutOfBoundsException("Must be {F, E, D[+], C[+], B[+], A[+]}");
		}
	}

}
