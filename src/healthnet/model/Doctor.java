package healthnet.model;

import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private int doctorId;
    private String doctorName;
    private String specialization;
    
    // Additional UML fields
    private String department;
    private String phone;
    private String email;
    private List<String> availableSlots;

    public Doctor(int doctorId, String doctorName, String specialization) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.availableSlots = new ArrayList<>();
        this.department = "";
        this.phone = "";
        this.email = "";
    }

    public int getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialization() { return specialization; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getAvailability() {
        return availableSlots;
    }

    public void setAvailableSlots(List<String> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void assignPatient(Patient p) {
        // Implementation stub for assigning a patient
    }

    public void updateSchedule() {
        // Implementation stub for updating schedule
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "ID=" + doctorId +
                ", Name='" + doctorName + '\'' +
                ", Specialization='" + specialization + '\'' +
                ", Department='" + department + '\'' +
                '}';
    }
}
