package gpacalculator.eecs;

abstract class Grade<T> implements Comparable<Grade> {
	private T rawGrade;
	private static String regex = "";

	/**
	 * The only constructor for the class. Only accepts valid grades of type String or type
	 * Number, and throws an exception for all other cases
	 *
	 * @param rawGrade a valid String or Number
	 * @throws ClassCastException       if rawGrade is not a String or a Number
	 * @throws IllegalArgumentException if the grade is not a valid grade
	 */
	Grade(T rawGrade) {
		if (!(rawGrade instanceof String || rawGrade instanceof Number))
			throw new ClassCastException("Grade must be String or Number");

		if (rawGrade instanceof String && !((String) rawGrade).matches(regex))
			throw new IllegalArgumentException("Grade not valid.");

		if (rawGrade instanceof Number && (Double) rawGrade < 0)
			throw new IllegalArgumentException("Grade not valid.");

		this.rawGrade = rawGrade;
	}

	/**
	 * Abstract method to map raw letter and percentage grades to a GPA scale. Implementations
	 * of this method should typically call an overloaded static method depending on the type of
	 * rawGrade (either String or Number)
	 *
	 * @return the scaled numeric GPA of the grade
	 */
	abstract double normalize();

	/**
	 * Sets the regex static class parameter, which is used by the constructor to check validity if
	 * the parameter is a string, and throws an exception if necessary
	 *
	 * @param regex A character set of possible letter grades
	 */
	static void setRegex(String regex) {
		Grade.regex = regex;
	}

	/**
	 * Returns the raw grade of this instance
	 *
	 * @return the raw grade of this instance
	 */
	T getRawGrade() {
		return this.rawGrade;
	}

	/**
	 * Allows sorting of Grade objects by comparing the GPA normalized values of the two grades
	 * via {@code Double.compare()}.
	 *
	 * @param g the Grade to be compared to
	 * @return the return value described by {@code Double.compare()}
	 * @see java.lang.Double
	 */
	@Override
	public int compareTo(Grade g) {
		return Double.compare(this.normalize(), g.normalize());
	}

	/**
	 * Returns the normalized version of the grade as a string
	 *
	 * @return the normalized grade
	 */
	@Override
	public String toString() {
		return "" + rawGrade;
	}
}