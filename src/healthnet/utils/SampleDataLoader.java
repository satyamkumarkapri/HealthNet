package healthnet.utils;

import healthnet.model.Doctor;
import healthnet.services.AppointmentService;
import healthnet.services.DoctorService;
import healthnet.services.MedicalRecordService;
import healthnet.services.PatientService;
import healthnet.services.ResourceService;

import java.time.LocalDate;

/**
 * Loads realistic sample healthcare data into all services on startup.
 * This ensures the system boots with demonstrable data for every module.
 */
public class SampleDataLoader {

    /**
     * Loads sample patients, appointments, and medical records.
     * @return an array: [patientCount, appointmentCount, recordCount]
     */
    public static int[] loadAll() {
        PatientService ps = PatientService.getInstance();
        AppointmentService as = AppointmentService.getInstance();
        MedicalRecordService mrs = MedicalRecordService.getInstance();
        DoctorService ds = DoctorService.getInstance();
        ResourceService rs = ResourceService.getInstance();
        
        int patients = loadPatients(ps);
        int doctors = loadDoctors(ds);
        int appointments = loadAppointments(as, ds);
        int records = loadMedicalRecords(mrs);
        int resources = loadResources(rs);
        return new int[]{patients, appointments, records, resources};
    }

    private static int loadDoctors(DoctorService ds) {
        ds.loadFromCSV();
        if (ds.getAllDoctors().isEmpty()) {
            ds.addDoctor(new Doctor(201, "Dr. Alice Smith", "Cardiology"));
            ds.addDoctor(new Doctor(202, "Dr. Bob Jones", "Neurology"));
            ds.addDoctor(new Doctor(203, "Dr. Carol White", "Pediatrics"));
            ds.addDoctor(new Doctor(204, "Dr. David Brown", "Orthopedics"));
            ds.addDoctor(new Doctor(205, "Dr. Eve Davis", "General Medicine"));
        }
        return ds.getAllDoctors().size();
    }

    private static int loadPatients(PatientService ps) {
        ps.loadFromCSV();
        return ps.getAllPatients().size();
    }

    private static int loadAppointments(AppointmentService as, DoctorService ds) {
        // No pre-loaded appointments
        return 0;
    }

    private static int loadMedicalRecords(MedicalRecordService mrs) {
        // No pre-loaded medical records
        return 0;
    }

    private static int loadResources(healthnet.services.ResourceService rs) {
        rs.addResource(new healthnet.model.Resource("R1", "Bed", "ICU Bed with monitor", 20, "ICU Ward"));
        rs.addResource(new healthnet.model.Resource("R2", "Ventilator", "Advanced life support", 5, "ICU Ward"));
        rs.addResource(new healthnet.model.Resource("R3", "Oxygen", "Oxygen Cylinder", 50, "Storage A"));
        rs.addResource(new healthnet.model.Resource("R4", "Bed", "General Ward Bed", 100, "General Ward"));
        rs.addResource(new healthnet.model.Resource("R5", "Wheelchair", "Manual Wheelchair", 15, "Entrance"));
        return 5;
    }
}
