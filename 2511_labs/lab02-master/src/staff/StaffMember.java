package staff;

import java.sql.Date;

/**
 * A staff member
 * @author Robert Clifton-Everest
 *
 */
public class StaffMember {
    private String namString;
    private int salary;
    private Date hireDate;
    private Date endDate;

    /**
	 * Constructor with 0 args
	 */
    public StaffMember() {
    }

    /**
	 * Constructor with 4 args 
	 * @param name staff name 
	 * @param slary staff salary
	 * @param hireDate staff hire date
     * @param endDate staff end date
	 */
    public StaffMember(String name, int salary, Date hireDate, Date endDate) {
        this.namString = name;
        this.salary = salary;
        this.hireDate = hireDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "name: " + this.namString + "  salary: " + this.salary + "  hire date: " + this.hireDate + "  end date: " + this.endDate;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) { return false; }

        if(obj == this) { return true; }
        if(this.getClass() != obj.getClass()){
            return false;
        }
        StaffMember otherMember = (StaffMember) obj;
        if (this.namString == otherMember.namString && 
            this.salary == otherMember.salary &&
            this.hireDate == otherMember.hireDate &&
            this.endDate == otherMember.endDate) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * The method sets name of the staff;
	 * @param namString name of the staff
	 * @return void 
	 */
    public void setNamString(String namString) {
        this.namString = namString;
    }

    /**
	 * The method sets salary of the staff;
	 * @param salary the salary of the staff can get
	 * @return void 
	 */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
	 * The method sets hire date of the staff;
	 * @param hireDate the start hire Date of the staff
	 * @return void 
	 */
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    /**
	 * The method sets end hire date of the staff;
	 * @param setEndDate the end hire Date of the staff
	 * @return void 
	 */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
	 * The method gets name of the staff;
	 * @return name of the staff
	 */
    public String getNamString() {
        return namString;
    }

    /**
	 * The method gets salary of the staff;
	 * @return salary of the staff
	 */
    public int getSalary() {
        return salary;
    }

    /**
	 * The method gets hire date of the staff;
	 * @return hire date of the staff
	 */
    public Date getHireDate() {
        return hireDate;
    }

    /**
	 * The method gets hire end date of the staff;
	 * @return hire end date of the staff
	 */
    public Date getEndDate() {
        return endDate;
    }

}
