package healthnet.model;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String deptId;
    private String name;
    private Doctor head;
    private String phone;
    private String location;
    private List<Doctor> doctors;

    public Department(String deptId, String name, Doctor head, String phone, String location) {
        this.deptId = deptId;
        this.name = name;
        this.head = head;
        this.phone = phone;
        this.location = location;
        this.doctors = new ArrayList<>();
        if (head != null) {
            this.doctors.add(head);
        }
    }

    public void addDoctor(Doctor d) {
        if (d != null && !doctors.contains(d)) {
            doctors.add(d);
        }
    }

    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors);
    }

    public String getDeptId() {
        return deptId;
    }

    public String getName() {
        return name;
    }

    public Doctor getHead() {
        return head;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public void setHead(Doctor head) {
        this.head = head;
        addDoctor(head);
    }

    @Override
    public String toString() {
        String headName = (head != null) ? head.getDoctorName() : "None";
        return String.format("Department[%s] %s - Head: %s, Phone: %s, Location: %s", 
                deptId, name, headName, phone, location);
    }
}
