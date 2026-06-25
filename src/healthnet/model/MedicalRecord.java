package healthnet.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord implements Comparable<MedicalRecord> {
    private int recordId;
    private int patientId;
    private LocalDate visitDate;
    private String diagnosis;
    private String treatment;
    
    // Additional UML fields
    private Patient patient;
    private List<String> medications;
    private String notes;
    private Doctor doctor;

    public MedicalRecord(int recordId, int patientId, LocalDate visitDate, String diagnosis, String treatment) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.visitDate = visitDate;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medications = new ArrayList<>();
        this.notes = "";
    }

    public int getRecordId() { return recordId; }
    public int getPatientId() { return patientId; }
    
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public List<String> getMedications() { return medications; }
    public void setMedications(List<String> medications) { this.medications = medications; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public void addRecord() {
        // Implementation stub
    }

    public List<MedicalRecord> getHistory() {
        // Implementation stub
        return new ArrayList<>();
    }

    public void updateRecord() {
        // Implementation stub
    }

    @Override
    public int compareTo(MedicalRecord o) {
        return Integer.compare(this.recordId, o.recordId);
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "RecordID=" + recordId +
                ", PatientID=" + patientId +
                ", Date=" + visitDate +
                ", Diagnosis='" + diagnosis + '\'' +
                ", Treatment='" + treatment + '\'' +
                '}';
    }
}
