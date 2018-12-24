package gpacalculator.eecs;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2018 Richard Robinson. All rights reserved.
 *
 * @see "https://github.com/richardrobinson0924/GPACalculator"
 */
public class Course implements Comparable<Course> {

	private String name;
	private double numCredits;
	private short grade;

	/**
	 * A regex pattern for use with scanners of the default input string with format:
	 * <p>
	 * {@code AB12⇥LE•EECS•1234••3.00•E⇥CourseTitle⇥A+}
	 * </p>
	 *
	 * @apiNote the remaining values of the entire default string are otherwise tab-delimited.
	 * @see java.util.regex.Pattern
	 */
	private static String REGEX =
		"(?<term>\\w{2}\\d{2})\\t" +
		"(?<code>\\D*\\d{4})\\s*" +
		"(?<credits>\\d\\.\\d{2})\\s" +
		"(?<section>\\w)\\t" +
		"(?<name>[^\\t,\\n]*)\\t?" +
		"(?<grade>\\w\\+?)?";

	public static final Pattern courseFormat = Pattern.compile(REGEX);

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
	public <T> Course(String name, T beforeGrade, double numCredits) {
		throwPossibleExceptions(beforeGrade);

		this.name = name;
		this.grade = toGrade(beforeGrade);
		this.numCredits = numCredits;
	}

	public Course(String raw) throws IllegalArgumentException {
		Matcher m = courseFormat.matcher(raw);

		if (m.find() && m.group("grade") != null) {
			this.name = m.group("code");
			this.grade = toGrade(m.group("grade"));
			this.numCredits = Double.parseDouble(m.group("credits"));
		} else {
			throw new IllegalArgumentException("Does not match pattern");
		}
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
	public static <T> short toGrade(T beforeGrade) {
		return (beforeGrade instanceof String)
				? toGrade((String) beforeGrade)
				: toGrade((Number) beforeGrade);
	}

	/**
	 * Converts a course's letter grade to its equivalent on the 9.0 scale
	 *
	 * @param letterGrade the grade in letter format  (e.g, {@code A+}) to be converted
	 * @param <S>         the type of the grade, limited to String-based types
	 * @return the converted grade
	 */
	private static <S extends String> short toGrade(S letterGrade) {
		short grade;

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
	private static <N extends Number> short toGrade(N percentGen) {
		double percent = (Double) percentGen;

		if (percent >= 90) return 9;
		if (percent >= 80) return 8;

		int base = 75;
		for (short i = 7; i >= 2; i--) {
			if (percent >= base) return i;
			base -= 5;
		}

		return (short) ((percent >= 40) ? 1 : 0);
	}

	/**
	 * Creates and sorts an ArrayList of courses, whose length is the length of the parameters'
	 * arrays. The
	 * method accepts three arrays corresponding to the parameters of {@code Course}
	 *
	 * @param names        the array of the values of the names for the courses
	 * @param beforeGrades the array of the values of the grades for the courses
	 * @param credits      the array of the values of the credits for the courses
	 * @return A list of courses processed from the three arrays of data, matched based on indices
	 * @throws IndexOutOfBoundsException if the length of any parameter array does not match the
	 *                                   other two
	 * @see #compareTo(Course) the compareTo() method used to sort the list of Courses
	 */
	public static List<Course> makeList(List<String> names, List<?> beforeGrades, List<Double> credits) {
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
	public short getGrade() {
		return this.grade;
	}

	/**
	 * Returns the number of credits for the course
	 *
	 * @return the number of credits for the course
	 */
	public double getNumCredits() {
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
	 * DO NOT USE UNLESS COMPLETELY NECESSARY.
	 *
	 * A setter method to set a custom regex pattern for the class. The regex is used to compile
	 * an instance from a formatted string. The new string must contain the capture group names
	 * 'name', 'group', and 'credits'. It may include custom tags as well.
	 *
	 * @param regex the custom regex format to be adjusted
	 * @throws IllegalArgumentException if new regex does not contain required capture group
	 *                                  identifier names. This does not assert the validity of the regex.
	 */
	public static void setRegex(String regex) {
		if (!regex.contains("name") ||
				!regex.contains("grade") ||
				!regex.contains("credits")
		)
			throw new IllegalArgumentException("Does not contain required capture group names");

		Course.REGEX = regex;
	}

	/**
	 * Sorts a list of Courses in increasing order
	 *
	 * @param o the target object
	 * @return Within {1, 0, -1} in accordance with `Double.compare()`
	 */
	@Override
	public int compareTo(Course o) {
		int resultGrade = Short.compare(this.getGrade(), o.getGrade());

		return (resultGrade == 0)
				? Double.compare(this.getNumCredits(), o.getNumCredits())
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

		if (!argClass.equals(String.class) && !(beforeGrade instanceof Number)) {
			throw new ClassCastException("Grade of invalid type. Must be Double or Strings.");
		}

		if ((beforeGrade instanceof Number) && ((Double) beforeGrade < 0 || (Double) beforeGrade > 100)) {
			throw new IndexOutOfBoundsException("Must be within [0, 100]");
		}

		if (argClass.equals(String.class) && !beforeGrade.toString().matches(regex)) {
			throw new IndexOutOfBoundsException("Must be {F, E, D[+], C[+], B[+], A[+]}");
		}
	}

}
