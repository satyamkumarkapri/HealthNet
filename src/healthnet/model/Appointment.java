package healthnet.model;

import java.time.LocalDate;

public class Appointment implements Comparable<Appointment> {
    private String appointmentId;
    private String patientName; // kept for legacy if needed
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private String time;
    private String status;
    private String reason;
    private int startTime; // Represented in military time or minutes from 00:00 for simplicity
    private int endTime;

    // Existing constructor updated to support new fields implicitly
    public Appointment(String patientName, Doctor doctor, int startTime, int endTime) {
        this.patientName = patientName;
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentId = java.util.UUID.randomUUID().toString();
        this.date = LocalDate.now();
        this.time = startTime + "";
        this.status = "Scheduled";
        this.reason = "General Checkup";
    }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { 
        this.patient = patient; 
        if(patient != null) this.patientName = patient.getName();
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getPatientName() { return patientName; }
    public Doctor getDoctor() { return doctor; }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }

    public boolean schedule() {
        this.status = "Scheduled";
        return true;
    }

    public boolean cancel() {
        this.status = "Cancelled";
        return true;
    }

    public boolean reschedule(LocalDate newDate, String newTime) {
        this.date = newDate;
        this.time = newTime;
        return true;
    }

    @Override
    public int compareTo(Appointment o) {
        return Integer.compare(this.endTime, o.endTime);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "ID='" + appointmentId + '\'' +
                ", Patient='" + patientName + '\'' +
                ", Doctor=" + (doctor != null ? doctor.getDoctorName() : "None") +
                ", Date=" + date +
                ", Start=" + startTime +
                ", End=" + endTime +
                ", Status='" + status + '\'' +
                '}';
    }
}
