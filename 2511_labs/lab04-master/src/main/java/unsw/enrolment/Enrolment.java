package unsw.enrolment;
import java.util.List;

import unsw.enrolment.exceptions.InvalidEnrolmentException;

public class Enrolment {

    private CourseOffering offering;
    private Grade grade;
    private Student student;

    public Enrolment(CourseOffering offering, Student student) {
        this.offering = offering;
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public CourseOffering getOffering() {
        return offering;
    }

    public boolean hasPassedCourse() {
        if (grade == null) {
            return false;
        } 
        return grade.canPass();
    }

    public Course getCourse() {
        return offering.getCourse();
    }

    public String getTerm() {
        return offering.getTerm();
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        if (offering.equals(grade.getOffering())) {
            this.grade = grade;
        }
    }

    public boolean isEnrolled(CourseOffering offering) {
        return this.getOffering().equals(offering);
    }

    public boolean canEnrol(Course prereq) {
        if (getCourse().equals(prereq) && getGrade() != null) {
            if (getGrade().canPass()) {
                return true;
            }
        }
        return false;
    }
}
