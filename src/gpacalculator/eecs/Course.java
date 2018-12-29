package gpacalculator.eecs;

import java.util.regex.*;

public class Course implements Comparable<Course> {
	private String name;
	private Grade grade;
	private double credits;

	private static String REGEX = "(?<term>\\w{2}\\d{2})\\t" +
			"(?<code>\\D*\\d{4})\\s*" +
			"(?<credits>\\d\\.\\d{2})\\s" +
			"(?<section>\\w)\\t" +
			"(?<name>[^\\t\\n]*)\\t?" +
			"(?<grade>\\w\\+?)?";

	public Course(String name, Grade grade, double credits) {
		this.name = name;
		this.grade = grade;
		this.credits = credits;
	}

	public <T> Course(String name, T grade, double credits) {
			this(name, new YorkGrade<>(grade), credits);
	}

	public Course(String rawFormat) throws IllegalArgumentException {
		Matcher m = Pattern.compile(REGEX).matcher(rawFormat);

		if (m.find() && m.group("grade") != null) {
			this.name = m.group("code");
			this.grade = new YorkGrade<>(m.group("grade"));
			this.credits = Double.parseDouble(m.group("credits"));
		} else {
			throw new IllegalArgumentException(rawFormat);
		}
	}

	public String getName() {
		return name;
	}

	public Grade getGrade() {
		return grade;
	}

	public double getCredits() {
		return credits;
	}

	public static void setRegex(String regex) {
		Course.REGEX = regex;
	}

	@Override
	public int compareTo(Course c) {
		int gradeResult = this.grade.compareTo(c.grade);

		return (gradeResult == 0)
				? this.getName().compareTo(c.getName())
				: gradeResult;
	}

	@Override
	public String toString() {
		return "Course{" +
				"name='" + getName() + '\'' +
				", grade=" + getGrade().normalize() +
				", credits=" + getCredits() +
				'}';
	}

}
