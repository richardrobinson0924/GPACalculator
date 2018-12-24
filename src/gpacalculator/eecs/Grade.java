package gpacalculator.eecs;

abstract class Grade<T> implements Comparable<Grade> {
	private T rawGrade;
	private static String regex = "";

	Grade(T rawGrade) {
		if (!(rawGrade instanceof String || rawGrade instanceof Number))
			throw new ClassCastException("Grade must be String or Number");

		if (rawGrade instanceof String && !((String) rawGrade).matches(regex))
			throw new IllegalArgumentException("Grade not valid.");

		if (rawGrade instanceof Number && (Double) rawGrade < 0)
			throw new IllegalArgumentException("Grade not valid.");

		this.rawGrade = rawGrade;
	}

	abstract double normalize();

	static void setRegex(String regex) {
		Grade.regex = regex;
	}

	T getRawGrade() {
		return this.rawGrade;
	}

	@Override
	public int compareTo(Grade g) {
		return Double.compare(this.normalize(), g.normalize());
	}

	@Override
	public String toString() {
		return "" + normalize();
	}
}