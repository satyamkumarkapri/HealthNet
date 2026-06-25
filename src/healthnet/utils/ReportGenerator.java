package healthnet.utils;

import healthnet.model.Patient;
import healthnet.model.MedicalRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReportGenerator {

    public static void generatePatientReport(String filename, List<Patient> patients, String reportTitle) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("==================================================");
            writer.println("             HEALTHNET SYSTEM REPORT              ");
            writer.println("==================================================");
            writer.println("Report Type: " + reportTitle);
            writer.println("Total Patients: " + patients.size());
            writer.println("--------------------------------------------------");
            
            for (Patient p : patients) {
                writer.printf("ID: %-5d | Name: %-15s | Age: %-3d | Gender: %-2s | Disease: %-20s | Admission: %s | Severity: %d%n",
                        p.getPatientId(), p.getName(), p.getAge(), p.getGender(), 
                        p.getDisease(), p.getAdmissionDate(), p.getSeverityScore());
            }
            
            writer.println("==================================================");
            writer.println("END OF REPORT");
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }

    public static void generateRecordReport(String filename, List<MedicalRecord> records, String reportTitle) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("==================================================");
            writer.println("             HEALTHNET SYSTEM REPORT              ");
            writer.println("==================================================");
            writer.println("Report Type: " + reportTitle);
            writer.println("Total Records: " + records.size());
            writer.println("--------------------------------------------------");
            
            for (MedicalRecord r : records) {
                writer.printf("Record ID: %-5d | Patient ID: %-5d | Date: %s | Diagnosis: %-20s%n",
                        r.getRecordId(), r.getPatientId(), r.getVisitDate(), r.getDiagnosis());
            }
            
            writer.println("==================================================");
            writer.println("END OF REPORT");
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}
