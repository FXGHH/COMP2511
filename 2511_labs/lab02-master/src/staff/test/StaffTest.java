package staff.test;

import staff.*;

public class StaffTest{
    public void printStaffDetails(StaffMember member) {
        System.out.println(member.toString());
    }
    public static void main(String[] args) {
        StaffMember member = new StaffMember("Jack", 40000, null, null);
        Lecturer lecturer = new Lecturer("Eric", 45000, null, null, "UNSW", 'A');
        StaffTest newStaff = new StaffTest();
        
        newStaff.printStaffDetails(member);
        newStaff.printStaffDetails(lecturer);
        System.out.println(member.equals(lecturer));
    }
}
