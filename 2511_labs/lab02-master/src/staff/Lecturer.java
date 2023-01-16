package staff;

import java.sql.Date;

public class Lecturer extends StaffMember{
    private String school;
    private char academicStatus;

    /**
	 * Constructor with 6 args 
	 * @param name lecturer name 
	 * @param slary lecturer salary
	 * @param hireDate lecturer hire date
     * @param endDate lecturer end date
     * @param school lecturer's school
     * @param academicStatus the academic status of lecturer
	 */
    public Lecturer(String name, int salary, Date hireDate, Date endDate, String school, char academicStatus) {
        super(name, salary, hireDate, endDate);
        this.school = school;
        this.academicStatus = academicStatus;
    }

    /**
	 * Constructor with 2 args 
	 * @param school lecturer's school
     * @param academicStatus the academic status of lecturer
	 */
    public Lecturer(String school, char academicStatus) {
        this.school = school;
        this.academicStatus = academicStatus;
    }

    /**
	 * The method gets school of the lecturer;
	 * @return string
	 */
    public String getSchool() {
        return school;
    }

    /**
	 * The method sets school of the lecturer;
	 * @param school new school to set
	 * @return void 
	 */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
	 * The method gets academic status of the lecturer;
	 * @return academic status of lecturer
	 */
    public char getAcademicStatus() {
        return academicStatus;
    }

    /**
	 * The method sets academic status of the lecturer;
	 * @param academicStatus new academicStatus to set
	 * @return void 
	 */
    public void setAcademicStatus(char academicStatus) {
        this.academicStatus = academicStatus;
    }

    @Override
    public String toString() {
        return "school name: " + this.school + "  academic level: " + this.academicStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) { return false; }

        if(obj == this) { return true; }

        if (super.equals(obj) == false) {
            return false;
        }
        Lecturer lecturer = (Lecturer) obj;
        if (this.school == lecturer.school && 
            this.academicStatus == lecturer.academicStatus) {
            return true;
        } else {
            return false;
        }
    }
}
