package healthnet.services;

import healthnet.model.MedicalRecord;
import healthnet.trees.BPlusTree;
import healthnet.trees.BTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalRecordService {
    private static MedicalRecordService instance;
    private List<MedicalRecord> records;
    private BTree<Integer> recordBTree; // renamed from bTree to match UML
    private BPlusTree<Integer> patientIndex; // renamed from bPlusTree to match UML
    private int nextRecordId = 1;

    private MedicalRecordService() {
        this.records = new ArrayList<>();
        this.recordBTree = new BTree<>(3);
        this.patientIndex = new BPlusTree<>(3);
    }

    public static synchronized MedicalRecordService getInstance() {
        if (instance == null) {
            instance = new MedicalRecordService();
        }
        return instance;
    }

    public MedicalRecord addRecord(int patientId, java.time.LocalDate visitDate, String diagnosis, String treatment) {
        MedicalRecord record = new MedicalRecord(nextRecordId++, patientId, visitDate, diagnosis, treatment);
        addRecord(record);
        return record;
    }
    
    public boolean addRecord(MedicalRecord r) {
        records.add(r);
        recordBTree.insert(r.getRecordId());
        patientIndex.insert(r.getPatientId()); // Patient index logic per UML mapping
        return true;
    }

    public List<MedicalRecord> getAllRecords() {
        return records;
    }

    public void displayBTree() {
        recordBTree.traverse();
    }

    public void displayBPlusTree() {
        patientIndex.traverse();
    }

    public List<Integer> rangeQuery(int start, int end) {
        return patientIndex.rangeQuery(start, end);
    }

    public MedicalRecord searchRecordById(int recordId) {
        return records.stream()
                .filter(r -> r.getRecordId() == recordId)
                .findFirst()
                .orElse(null);
    }
    
    public MedicalRecord searchByRecordId(String id) {
        try {
            return searchRecordById(Integer.parseInt(id));
        } catch(NumberFormatException e) {
            return null;
        }
    }
    
    public List<MedicalRecord> searchByPatient(String pid) {
        try {
            int patientId = Integer.parseInt(pid);
            return records.stream()
                    .filter(r -> r.getPatientId() == patientId)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }

    public void updateRecord(int recordId, String newDiagnosis, String newTreatment) {
        MedicalRecord record = searchRecordById(recordId);
        if (record != null) {
            record.setDiagnosis(newDiagnosis);
            record.setTreatment(newTreatment);
            record.setVisitDate(java.time.LocalDate.now()); // Update the visit date to today
        }
    }
    
    public boolean updateRecord(MedicalRecord r) {
        MedicalRecord record = searchRecordById(r.getRecordId());
        if(record != null) {
            record.setDiagnosis(r.getDiagnosis());
            record.setTreatment(r.getTreatment());
            record.setNotes(r.getNotes());
            record.setMedications(r.getMedications());
            return true;
        }
        return false;
    }
    
    public boolean deleteRecord(String id) {
        try {
            int recordId = Integer.parseInt(id);
            MedicalRecord toRemove = searchRecordById(recordId);
            if(toRemove != null) {
                records.remove(toRemove);
                // BTree / BPlusTree delete not easily supported in simple implementations, skipping
                return true;
            }
        } catch(NumberFormatException e) {
            return false;
        }
        return false;
    }

    public void cleanupOldRecords() {
        java.time.LocalDate threshold = java.time.LocalDate.now().minusDays(30);
        
        List<MedicalRecord> toDelete = new ArrayList<>();
        for (MedicalRecord r : records) {
            if (r.getVisitDate().isBefore(threshold)) {
                toDelete.add(r);
            }
        }
        
        records.removeAll(toDelete);
        System.out.println("Cleaned up " + toDelete.size() + " records older than 30 days.");
    }
}
