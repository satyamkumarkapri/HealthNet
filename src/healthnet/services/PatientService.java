package healthnet.services;

import healthnet.model.Patient;
import healthnet.trees.AVLTree;
import healthnet.trees.BST;
import healthnet.utils.CSVStorageManager;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;

public class PatientService {
    private static PatientService instance;
    private BST<Patient> bst;
    private AVLTree<Patient> avl;
    private HashMap<Integer, Patient> patientMap;

    private PatientService() {
        this.bst = new BST<>();
        this.avl = new AVLTree<>();
        this.patientMap = new HashMap<>();
    }

    public static synchronized PatientService getInstance() {
        if (instance == null) {
            instance = new PatientService();
        }
        return instance;
    }

    public void loadFromCSV() {
        List<Patient> list = CSVStorageManager.loadPatients();
        for (Patient p : list) {
            bst.insert(p);
            avl.insert(p);
            patientMap.put(p.getPatientId(), p);
        }
    }

    public void addPatient(int id, String name, int age, String gender, String disease, LocalDate admissionDate, int severityScore) {
        Patient p = new Patient(id, name, age, gender, disease, admissionDate, severityScore);
        bst.insert(p);
        avl.insert(p);
        patientMap.put(id, p);
        CSVStorageManager.savePatients(getAllPatients());
    }
    
    public void addPatient(Patient p) {
        bst.insert(p);
        avl.insert(p);
        patientMap.put(p.getPatientId(), p);
        CSVStorageManager.savePatients(getAllPatients());
    }

    public Patient searchPatient(int id) {
        return patientMap.get(id); // O(1) lookup via HashMap per UML
    }
    
    public Patient searchById(String id) {
        try {
            return searchPatient(Integer.parseInt(id));
        } catch(NumberFormatException e) {
            return null;
        }
    }

    public List<Patient> searchByName(String name) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patientMap.values()) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    public void updatePatient(Patient updatedPatient) {
        deletePatient(updatedPatient.getPatientId());
        addPatient(updatedPatient);
    }

    public void deletePatient(int id) {
        Patient p = patientMap.get(id);
        if (p != null) {
            bst.delete(p);
            avl.delete(p);
            patientMap.remove(id);
            CSVStorageManager.savePatients(getAllPatients());
        }
    }
    
    public void removePatient(String id) {
        try {
            deletePatient(Integer.parseInt(id));
        } catch(NumberFormatException e) {
            // ignore
        }
    }

    public void displayPatientsInOrder() {
        System.out.println("--- Inorder Traversal (AVL) ---");
        List<Patient> list = avl.inorder();
        list.forEach(System.out::println);
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }
    
    public void displayPatientsPreOrder() {
        System.out.println("--- Preorder Traversal (AVL) ---");
        List<Patient> list = avl.preorder();
        list.forEach(System.out::println);
    }

    public void displayPatientsPostOrder() {
        System.out.println("--- Postorder Traversal (AVL) ---");
        List<Patient> list = avl.postorder();
        list.forEach(System.out::println);
    }

    public void showTreeHeights() {
        System.out.println("BST Height: " + bst.height());
        System.out.println("AVL Height: " + avl.height());
    }
}
