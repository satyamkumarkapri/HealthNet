package healthnet.model;

import java.time.LocalDate;

public class Patient implements Comparable<Patient> {
    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String disease;
    private LocalDate admissionDate;
    private int severityScore;
    
    // Additional UML fields
    private String bloodGroup;
    private String phone;
    private String address;
    private LocalDate registrationDate;

    public Patient(int patientId, String name, int age, String gender, String disease, LocalDate admissionDate, int severityScore) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
        this.admissionDate = admissionDate;
        this.severityScore = severityScore;
        // Default empty strings/null for backward compatibility
        this.bloodGroup = "";
        this.phone = "";
        this.address = "";
        this.registrationDate = admissionDate;
    }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

    public int getSeverityScore() { return severityScore; }
    public void setSeverityScore(int severityScore) { this.severityScore = severityScore; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public String getDetails() {
        return toString();
    }
    
    public void updateDetails() {
        // Method placeholder to match UML, updates happen via setters or service.
    }

    @Override
    public int compareTo(Patient o) {
        return Integer.compare(this.patientId, o.patientId);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "ID=" + patientId +
                ", Name='" + name + '\'' +
                ", Age=" + age +
                ", Gender='" + gender + '\'' +
                ", Disease='" + disease + '\'' +
                ", AdmissionDate=" + admissionDate +
                ", Severity=" + severityScore +
                ", BloodGroup='" + bloodGroup + '\'' +
                ", Phone='" + phone + '\'' +
                ", Address='" + address + '\'' +
                '}';
    }
}
