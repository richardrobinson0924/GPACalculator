package gpacalculator.eecs;

class YorkGrade<T> extends Grade<T> {

	static {
		Grade.setRegex("[ABCDEF]\\+?");
	}

	YorkGrade(T rawGrade) {
		super(rawGrade);
	}

	@Override
	double normalize() {
		return (getRawGrade() instanceof String)
				? YorkGrade.normalize((String) getRawGrade())
				: YorkGrade.normalize((Number) getRawGrade());
	}

	private static double normalize(String grade) {
		double normalizedGrade;

		switch (grade.charAt(0)) {
			case 'A':
				normalizedGrade = 8;
				break;
			case 'B':
				normalizedGrade = 6;
				break;
			case 'C':
				normalizedGrade = 4;
				break;
			case 'D':
				normalizedGrade = 2;
				break;
			case 'E':
				normalizedGrade = 1;
				break;
			default:
				normalizedGrade = 0;
				break;
		}

		if (grade.endsWith("+"))
			normalizedGrade++;
		return normalizedGrade;
	}

	private static double normalize(Number grade) {
		double percent = (Double) grade;

		if (percent >= 90) return 9;
		if (percent >= 80) return 8;
		if (percent >= 75) return 7;

		int base = 75;
		for (double i = 7; i >= 2; i--) {
			if (percent >= base) return i;
			base -= 5;
		}

		return (double) ((percent >= 40) ? 1 : 0);
	}
}