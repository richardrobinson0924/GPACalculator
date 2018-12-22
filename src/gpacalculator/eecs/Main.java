package gpacalculator.eecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        String[] names = {"EECS", "PHYS", "MATH"};
        Double[] grades = {80.1, 75.1, 90.5};
        int[] credits = {3, 3, 4};

        List<Course> courses = Course.makeList(names, grades, credits);

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
