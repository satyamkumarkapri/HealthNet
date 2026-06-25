package healthnet.services;

import healthnet.model.Doctor;
import healthnet.utils.CSVStorageManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class DoctorService {
    private static DoctorService instance;
    private HashMap<Integer, Doctor> doctorMap;
    private PriorityQueue<Doctor> availabilityPQ; // Track doctors by most available slots

    private DoctorService() {
        this.doctorMap = new HashMap<>();
        // MaxHeap based on slots or just arbitrary priority for demo
        this.availabilityPQ = new PriorityQueue<>((d1, d2) -> {
            int s1 = d1.getAvailability() != null ? d1.getAvailability().size() : 0;
            int s2 = d2.getAvailability() != null ? d2.getAvailability().size() : 0;
            if (s1 != s2) return Integer.compare(s2, s1);
            return Integer.compare(d2.getDoctorId(), d1.getDoctorId());
        }); 
        
        // No pre-loaded doctors as per user request
    }

    public static synchronized DoctorService getInstance() {
        if (instance == null) {
            instance = new DoctorService();
        }
        return instance;
    }

    public void loadFromCSV() {
        List<Doctor> list = CSVStorageManager.loadDoctors();
        for (Doctor d : list) {
            doctorMap.put(d.getDoctorId(), d);
            availabilityPQ.add(d);
        }
    }

    public void addDoctor(Doctor d) {
        doctorMap.put(d.getDoctorId(), d);
        availabilityPQ.add(d);
        CSVStorageManager.saveDoctors(getAllDoctors());
    }

    public void updateDoctor(Doctor d) {
        Doctor existing = doctorMap.get(d.getDoctorId());
        if (existing != null) {
            availabilityPQ.remove(existing);
        }
        doctorMap.put(d.getDoctorId(), d);
        availabilityPQ.add(d);
        CSVStorageManager.saveDoctors(getAllDoctors());
    }

    public void deleteDoctor(int id) {
        Doctor d = doctorMap.remove(id);
        if (d != null) {
            availabilityPQ.remove(d);
            CSVStorageManager.saveDoctors(getAllDoctors());
        }
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctorMap.values());
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorMap.values().stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }
    
    public List<Doctor> findBySpecialization(String specialization) {
        return getDoctorsBySpecialization(specialization);
    }

    public Doctor getDoctorById(int id) {
        return doctorMap.get(id);
    }
    
    public Doctor findById(String id) {
        try {
            return getDoctorById(Integer.parseInt(id));
        } catch(NumberFormatException e) {
            return null;
        }
    }

    public List<Doctor> getAvailableDoctors(LocalDate date) {
        // Return doctors who have availability. Currently simulating using availability size.
        return doctorMap.values().stream()
                .filter(d -> d.getAvailability() != null && !d.getAvailability().isEmpty())
                .collect(Collectors.toList());
    }

    public void updateAvailability(int id, List<String> slots) {
        Doctor d = doctorMap.get(id);
        if (d != null) {
            availabilityPQ.remove(d);
            d.setAvailableSlots(slots);
            availabilityPQ.add(d);
        }
    }
}
