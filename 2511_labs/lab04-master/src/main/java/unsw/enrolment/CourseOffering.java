package unsw.enrolment;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import unsw.enrolment.exceptions.InvalidEnrolmentException;

public class CourseOffering {

    private Course course;
    private String term;
    private List<Enrolment> enrolments = new ArrayList<Enrolment>();

    public CourseOffering(Course course, String term) {
        this.course = course;
        this.term = term;
        this.course.addOffering(this);
    }

    public Course getCourse() {
        return course;
    }

    public String getCourseCode() {
        return course.getCourseCode();
    }

    public List<Course> getCoursePrereqs() {
        return course.getPrereqs();
    }

    public String getTerm() {
        return term;
    }

    public Enrolment addEnrolment(Student student) throws InvalidEnrolmentException {
        // System.out.println(checkValidEnrolment(student));
        if (checkValidEnrolment(student)) {
            Enrolment enrolment = new Enrolment(this, student);
            enrolments.add(enrolment);
            student.addEnrolment(enrolment);
            return enrolment;
        } else {
            throw new InvalidEnrolmentException("student has not satisfied the prerequisites");
        }
    }

    private boolean checkValidEnrolment(Student student) {
        List<Course> prereqs = getCoursePrereqs();
        for (Course prereq : prereqs) {
            List<Enrolment> studentEnrolments = student.getEnrolments();
            boolean valid = false;
            valid = studentEnrolments.stream().anyMatch(Enrolment -> Enrolment.canEnrol(prereq));
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    public List<Student> studentsEnrolledInCourse() {
        List<Student> students = enrolments.stream().map(Enrolment::getStudent).collect(Collectors.toList());

        // Comparator<Student> myComp = new Comparator<Student>() {
        //     @Override
        //     public int compare(Student s1, Student s2) {
        //         if (s1.getProgram() != s2.getProgram()) {
        //             return s1.getProgram() - s2.getProgram();

        //         } else if (s1.getSizeStream() != s2.getSizeStream()) {
        //             return s1.getSizeStream() - s2.getSizeStream();

        //         } else if (s1.getName() != s2.getName()) {
        //             return s1.getName().compareTo(s2.getName());

        //         } 
        //         return s1.getZid().compareTo(s2.getZid());
        //     }
		// } ;
        // Collections.sort(students , myComp);
        // Stream<Student> sorted = students.stream().sorted(Comparator.comparing(Student::getProgram));
        
        List<Student> sorted =  students.stream().sorted(Comparator.comparing(Student::getProgram)
                                                 .thenComparing(Student::getSizeStream)
                                                 .thenComparing(Student::getName)
                                                 .thenComparing(Student::getZid)).collect(Collectors.toList());
        return sorted;
    }
}