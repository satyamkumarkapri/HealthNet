package healthnet.utils;

import healthnet.model.Doctor;
import healthnet.model.Patient;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CSVStorageManager {

    private static final String PATIENTS_FILE = "patients.csv";
    private static final String DOCTORS_FILE = "doctors.csv";

    // --- PATIENTS ---
    public static void savePatients(List<Patient> patients) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PATIENTS_FILE))) {
            // Write Header
            writer.println("patientId,name,age,gender,disease,admissionDate,severityScore,bloodGroup,phone,address,registrationDate");
            
            for (Patient p : patients) {
                writer.println(String.join(",",
                    String.valueOf(p.getPatientId()),
                    clean(p.getName()),
                    String.valueOf(p.getAge()),
                    clean(p.getGender()),
                    clean(p.getDisease()),
                    p.getAdmissionDate() != null ? p.getAdmissionDate().toString() : "",
                    String.valueOf(p.getSeverityScore()),
                    clean(p.getBloodGroup()),
                    clean(p.getPhone()),
                    clean(p.getAddress()),
                    p.getRegistrationDate() != null ? p.getRegistrationDate().toString() : ""
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving patients to CSV: " + e.getMessage());
        }
    }

    public static List<Patient> loadPatients() {
        List<Patient> patients = new ArrayList<>();
        File file = new File(PATIENTS_FILE);
        if (!file.exists()) return patients;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 7) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    String gender = parts[3];
                    String disease = parts[4];
                    LocalDate admissionDate = parts[5].isEmpty() ? LocalDate.now() : LocalDate.parse(parts[5]);
                    int severity = Integer.parseInt(parts[6]);
                    
                    Patient p = new Patient(id, name, age, gender, disease, admissionDate, severity);
                    
                    if (parts.length >= 11) {
                        p.setBloodGroup(parts[7]);
                        p.setPhone(parts[8]);
                        p.setAddress(parts[9]);
                        p.setRegistrationDate(parts[10].isEmpty() ? null : LocalDate.parse(parts[10]));
                    }
                    patients.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading patients from CSV: " + e.getMessage());
        }
        return patients;
    }

    // --- DOCTORS ---
    public static void saveDoctors(List<Doctor> doctors) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DOCTORS_FILE))) {
            // Write Header
            writer.println("doctorId,doctorName,specialization,department,phone,email");
            
            for (Doctor d : doctors) {
                writer.println(String.join(",",
                    String.valueOf(d.getDoctorId()),
                    clean(d.getDoctorName()),
                    clean(d.getSpecialization()),
                    clean(d.getDepartment()),
                    clean(d.getPhone()),
                    clean(d.getEmail())
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving doctors to CSV: " + e.getMessage());
        }
    }

    public static List<Doctor> loadDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        File file = new File(DOCTORS_FILE);
        if (!file.exists()) return doctors;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String spec = parts[2];
                    
                    Doctor d = new Doctor(id, name, spec);
                    if (parts.length >= 6) {
                        d.setDepartment(parts[3]);
                        d.setPhone(parts[4]);
                        d.setEmail(parts[5]);
                    }
                    doctors.add(d);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading doctors from CSV: " + e.getMessage());
        }
        return doctors;
    }

    // Replace commas with space to avoid CSV format breaking
    private static String clean(String input) {
        if (input == null) return "";
        return input.replace(",", " ");
    }
}
