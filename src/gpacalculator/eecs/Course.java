package gpacalculator.eecs;

import java.util.regex.*;

public class Course implements Comparable<Course> {
	private String name;
	private Grade grade;
	private double credits;

	private static String REGEX_FORMAT = "(?<term>\\w{2}\\d{2})\\t" +
			"(?<code>\\D*\\d{4})\\s*" +
			"(?<credits>\\d\\.\\d{2})\\s" +
			"(?<section>\\w)\\t" +
			"(?<name>[^\\t,\\n]*)\\t?" +
			"(?<grade>\\w\\+?)?";

	private Course(String name, Grade grade, double credits) {
		this.name = name;
		this.grade = grade;
		this.credits = credits;
	}

	<T> Course(String name, T grade, double credits) {
		this(name, new YorkGrade<>(grade), credits);
	}

	Course(String rawFormat) throws IllegalArgumentException {
		Matcher m = Pattern.compile(REGEX_FORMAT).matcher(rawFormat);

		if (m.find() && m.group("grade") != null) {
			this.name = m.group("code");
			this.grade = new YorkGrade<>(m.group("grade"));
			this.credits = Double.parseDouble(m.group("credits"));
		} else {
			throw new IllegalArgumentException("Does not match pattern");
		}
	}

	public String getName() {
		return name;
	}

	Grade getGrade() {
		return grade;
	}

	double getCredits() {
		return credits;
	}

	public static void setRegexFormat(String regexFormat) {
		Course.REGEX_FORMAT = regexFormat;
	}

	@Override
	public int compareTo(Course o) {
		return this.grade.compareTo(o.grade);
	}

	@Override
	public String toString() {
		return "Course{" +
				"name='" + getName() + '\'' +
				", grade=" + getGrade() +
				", credits=" + getCredits() +
				'}';
	}

}
