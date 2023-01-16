package unsw.enrolment;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private String zid;
    private ArrayList<Enrolment> enrolments = new ArrayList<Enrolment>();
    private String name;
    private int program;
    private String[] streams;

	public Student(String zid, String name, int program, String[] streams) {
        this.zid = zid;
        this.name = name;
        this.program = program;
        this.streams = streams;
    }

    public boolean isEnrolled(CourseOffering offering) {
        boolean enorlled = enrolments.stream().anyMatch(Enrolment -> Enrolment.isEnrolled(offering));
        return enorlled;
    }

    public void setGrade(Grade grade) {
        enrolments.stream().forEach(Enrolment -> Enrolment.setGrade(grade));
    }

    public void addEnrolment(Enrolment enrolment) {
        enrolments.add(enrolment);
    }

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public int getProgram() {
        return program;
    }

    public int getSizeStream() {
        return streams.length;
    }

    public String getZid() {
        return zid;
    }

    public String getName() {
        return name;
    }
}
