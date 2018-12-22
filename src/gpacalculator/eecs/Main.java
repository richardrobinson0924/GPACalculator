package gpacalculator.eecs;

import java.io.*;;
import java.util.*;
import java.util.regex.*;


public class Main {

    public static void main(String[] args) throws Exception {
        List<String> names    = new ArrayList<>();
        List<String> grades  = new ArrayList<>();
        List<Integer> credits = new ArrayList<>();

        String path = "/Users/richardrobinson/Documents/GPACalculator/test.txt";
        Scanner sc = new Scanner(new File(path));

        while (sc.hasNextLine()) {
            String[] s = sc.nextLine().split("\t");
            if (s.length == 4) {
                Matcher m = Course.courseFormat.matcher(s[1]);
                if (m.find()) {
                    names.add(m.group(1));
                    credits.add(Integer.parseInt(m.group(2)));
                }
                grades.add(s[3]);
            }
        }

        System.out.println(names);
        System.out.println(grades);
        System.out.println(credits);
        List<Course> courses = Course.makeList(names, grades, credits);
        courses.add(new Course("EECS 2200", 75.0, 3));

        double sum = 0;
        int totalCredits = 0;

        for (Course c : courses) {
            System.out.println(c);
            sum += c.getGrade() * c.getNumCredits();
            totalCredits += c.getNumCredits();
        }

        System.out.println(sum/totalCredits);
    }
}
