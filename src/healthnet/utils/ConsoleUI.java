package healthnet.utils;

import healthnet.dp.*;
import healthnet.graphs.*;
import healthnet.model.*;
import healthnet.patterns.strategy.*;
import healthnet.services.*;
import healthnet.sorting.*;
import healthnet.trees.*;
import healthnet.utils.ReportGenerator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main Console User Interface for the HealthNet system.
 * Provides a comprehensive CLI with 10 modules covering all DSA implementations.
 */
import healthnet.model.Resource;

public class ConsoleUI implements healthnet.patterns.observer.Observer {

    private final Scanner scanner;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final ResourceService resourceService;
    private final DoctorService doctorService;
    private final MedicalRecordService medicalRecordService;
    private final Graph hospitalGraph;
    private final Graph departmentGraph;
    private SegmentTree segmentTree;
    private FenwickTree fenwickTree;
    private int[] dailyCounts;

    // ═══════════════════════════════════════════════════════════════════════
    //  CONSTRUCTOR & INITIALIZATION
    // ═══════════════════════════════════════════════════════════════════════

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.patientService = PatientService.getInstance();
        this.appointmentService = AppointmentService.getInstance();
        this.resourceService = ResourceService.getInstance();
        this.resourceService.registerObserver(this);
        this.doctorService = DoctorService.getInstance();
        this.medicalRecordService = MedicalRecordService.getInstance();

        // Initialize pre-configured hospital room and department graphs
        this.hospitalGraph = initHospitalGraph();
        this.departmentGraph = initDepartmentGraph();

        // Load sample data into all services
        SampleDataLoader.loadAll();

        // Initialize daily patient count data for Segment Tree & Fenwick Tree
        this.dailyCounts = new int[]{
            12, 8, 15, 20, 5, 18, 22, 10, 14, 7,
            19, 25, 11, 16, 9, 21, 13, 6, 17, 23,
             8, 15, 20, 12, 14, 18, 10, 22, 16, 19
        };
        this.segmentTree = new SegmentTree(dailyCounts);
        this.fenwickTree = new FenwickTree(dailyCounts);
    }

    private Graph initHospitalGraph() {
        Graph g = new Graph(8);
        g.setNodeName(0, "Reception / Lobby");
        g.setNodeName(1, "Cardiology (Heart)");
        g.setNodeName(2, "Ophthalmology (Eye)");
        g.setNodeName(3, "Neurology (Brain)");
        g.setNodeName(4, "Orthopedics (Bone)");
        g.setNodeName(5, "Radiology (X-Ray/MRI)");
        g.setNodeName(6, "Emergency Room (ER)");
        g.setNodeName(7, "Pharmacy");
        // Reception connects to many rooms
        g.addEdge(0, 1, 5);  g.addEdge(0, 2, 3);
        g.addEdge(0, 6, 2);  g.addEdge(0, 7, 4);
        // Cardiology
        g.addEdge(1, 5, 6);  g.addEdge(1, 6, 3);
        // Ophthalmology
        g.addEdge(2, 5, 4);  g.addEdge(2, 7, 5);
        // Neurology
        g.addEdge(3, 5, 2);  g.addEdge(3, 1, 7);
        // Orthopedics
        g.addEdge(4, 5, 3);  g.addEdge(4, 6, 4);
        // Radiology
        g.addEdge(5, 7, 2);
        // Emergency Room
        g.addEdge(6, 1, 3);  g.addEdge(6, 3, 5);
        g.addEdge(6, 5, 4);
        // Pharmacy
        g.addEdge(7, 0, 4);
        return g;
    }

    private Graph initDepartmentGraph() {
        Graph g = new Graph(6);
        g.setNodeName(0, "Emergency");
        g.setNodeName(1, "ICU");
        g.setNodeName(2, "Radiology");
        g.setNodeName(3, "Neurology");
        g.setNodeName(4, "Cardiology");
        g.setNodeName(5, "Pharmacy");
        g.addUndirectedEdge(0, 1, 4);
        g.addUndirectedEdge(0, 2, 4);
        g.addUndirectedEdge(1, 2, 2);
        g.addUndirectedEdge(2, 3, 3);
        g.addUndirectedEdge(2, 5, 2);
        g.addUndirectedEdge(2, 4, 4);
        g.addUndirectedEdge(3, 5, 3);
        g.addUndirectedEdge(4, 5, 3);
        return g;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MAIN MENU & LOOP
    // ═══════════════════════════════════════════════════════════════════════

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":  handlePatientManagement();     break;
                case "2":  handleAppointmentScheduling(); break;
                case "3":  handleMedicalRecordsIndexing(); break;
                case "4":  handleResourceAllocation();    break;
                case "5":  handleEmergencyRouting();       break;
                case "6":  handleNetworkAnalysis();        break;
                case "7":  handleAnalytics();              break;
                case "8":  handleNameCorrection();         break;
                case "9":  handleRangeQueryAnalytics();    break;
                case "10": handleDoctorManagement();       break;
                case "0":
                    System.out.println();
                    ConsoleColors.printDoubleDivider();
                    ConsoleColors.printSuccess("Thank you for using HealthNet. Stay healthy!");
                    ConsoleColors.printDoubleDivider();
                    running = false;
                    break;
                default:
                    ConsoleColors.printError("Invalid choice. Please enter 0-10.");
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        ConsoleColors.printHeader("HEALTHNET - Hospital Resource Management System");
        ConsoleColors.printMenuItemWithTag("1", "Patient Management",            "BST & AVL Trees");
        ConsoleColors.printMenuItemWithTag("2", "Appointment Scheduling",        "Greedy - Activity Selection");
        ConsoleColors.printMenuItemWithTag("3", "Medical Records Indexing",      "B-Tree & B+ Tree");
        ConsoleColors.printMenuItemWithTag("4", "Hospital Resource Allocation",  "Knapsack DP");
        ConsoleColors.printMenuItemWithTag("5", "Emergency Routing System",      "Shortest Path Algorithms");
        ConsoleColors.printMenuItemWithTag("6", "Department Network Analysis",   "Graph Traversal & MST");
        ConsoleColors.printMenuItemWithTag("7", "Analytics & Reports",           "Sorting Algorithms");
        ConsoleColors.printMenuItemWithTag("8", "Patient Name Correction",       "Edit Distance & LCS");
        ConsoleColors.printMenuItemWithTag("9", "Range Query Analytics",         "Segment Tree & Fenwick Tree");
        ConsoleColors.printMenuItemWithTag("10", "Doctor Management",            "HashMap & Priority Queue");
        ConsoleColors.printMenuItem("0", "Exit");
        ConsoleColors.printDivider();
        ConsoleColors.printPrompt("Enter your choice: ");
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 1: PATIENT MANAGEMENT (BST & AVL Trees)
    // ═══════════════════════════════════════════════════════════════════════

    private void handlePatientManagement() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("PATIENT MANAGEMENT - BST & AVL Trees");
            ConsoleColors.printMenuItem("1", "Add New Patient");
            ConsoleColors.printMenuItem("2", "Search Patient by ID");
            ConsoleColors.printMenuItem("3", "Update Patient Record");
            ConsoleColors.printMenuItem("4", "Delete Patient Record");
            ConsoleColors.printMenuItem("5", "Display Patients (InOrder Traversal)");
            ConsoleColors.printMenuItem("6", "Display Patients (PreOrder Traversal)");
            ConsoleColors.printMenuItem("7", "Display Patients (PostOrder Traversal)");
            ConsoleColors.printMenuItem("8", "Compare BST vs AVL Tree Heights");
            ConsoleColors.printMenuItem("9", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1": addPatient(); break;
                case "2": searchPatient(); break;
                case "3": updatePatient(); break;
                case "4": deletePatient(); break;
                case "5":
                    ConsoleColors.printSubHeader("InOrder Traversal (Sorted by Patient ID)");
                    patientService.displayPatientsInOrder();
                    pressEnterToContinue();
                    break;
                case "6":
                    ConsoleColors.printSubHeader("PreOrder Traversal (Root -> Left -> Right)");
                    patientService.displayPatientsPreOrder();
                    pressEnterToContinue();
                    break;
                case "7":
                    ConsoleColors.printSubHeader("PostOrder Traversal (Left -> Right -> Root)");
                    patientService.displayPatientsPostOrder();
                    pressEnterToContinue();
                    break;
                case "8":
                    ConsoleColors.printSubHeader("BST vs AVL Tree Height Comparison");
                    patientService.showTreeHeights();
                    ConsoleColors.printInfo("AVL trees maintain O(log N) height through rotations.");
                    pressEnterToContinue();
                    break;
                case "9": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    private void addPatient() {
        ConsoleColors.printSubHeader("Add New Patient");

        int id = readPositiveInt("Patient ID (positive integer): ");

        String name = readNonEmpty("Patient Name: ");

        int age = readIntInRange("Age (1-120): ", 1, 120);

        String gender = readGender("Gender (M/F): ");

        String disease = readNonEmpty("Primary Disease/Condition: ");

        int severityScore = readIntInRange("Severity Score (1-100): ", 1, 100);

        String bloodGroup = readLine("Blood Group (optional): ");
        String phone = readLine("Phone (optional): ");
        String address = readLine("Address (optional): ");

        Patient p = new Patient(id, name, age, gender, disease, LocalDate.now(), severityScore);
        p.setBloodGroup(bloodGroup);
        p.setPhone(phone);
        p.setAddress(address);
        patientService.addPatient(p);
        ConsoleColors.printSuccess("Patient '" + name + "' (ID: " + id + ") added to BST and AVL Tree.");
        pressEnterToContinue();
    }

    private void searchPatient() {
        ConsoleColors.printSubHeader("Search Patient by ID");
        int id = readPositiveInt("Enter Patient ID: ");
        ConsoleColors.printLoading("Searching in AVL Tree (O(log N))");
        Patient p = patientService.searchPatient(id);
        if (p != null) {
            ConsoleColors.printSuccess("Patient found!");
            System.out.println("    " + p);
        } else {
            ConsoleColors.printError("Patient with ID " + id + " not found.");
        }
        pressEnterToContinue();
    }

    private void updatePatient() {
        ConsoleColors.printSubHeader("Update Patient Record");
        int id = readPositiveInt("Enter Patient ID to update: ");
        Patient p = patientService.searchPatient(id);
        if (p == null) {
            ConsoleColors.printError("Patient with ID " + id + " not found.");
            pressEnterToContinue();
            return;
        }
        ConsoleColors.printInfo("Current Record: " + p);
        System.out.println();
        ConsoleColors.printInfo("Press Enter to keep current value.");

        String name = readLine("New Name [" + p.getName() + "]: ");
        if (name.isEmpty()) name = p.getName();

        String ageStr = readLine("New Age [" + p.getAge() + "]: ");
        int age;
        if (ageStr.isEmpty()) {
            age = p.getAge();
        } else {
            try {
                age = Integer.parseInt(ageStr);
                if (age < 1 || age > 120) {
                    ConsoleColors.printWarning("Age out of range (1-120). Keeping current value: " + p.getAge());
                    age = p.getAge();
                }
            } catch (NumberFormatException e) {
                ConsoleColors.printWarning("Invalid number. Keeping current value: " + p.getAge());
                age = p.getAge();
            }
        }

        String gender = readLine("New Gender (M/F) [" + p.getGender() + "]: ");
        if (gender.isEmpty()) {
            gender = p.getGender();
        } else {
            gender = gender.toUpperCase();
            if (!gender.equals("M") && !gender.equals("F")) {
                ConsoleColors.printWarning("Invalid gender. Keeping current value: " + p.getGender());
                gender = p.getGender();
            }
        }

        String disease = readLine("New Disease [" + p.getDisease() + "]: ");
        if (disease.isEmpty()) disease = p.getDisease();
        
        int severity = readIntInRange("New Severity Score (1-100) [" + p.getSeverityScore() + "]: ", 1, 100);

        String bloodGroup = readLine("New Blood Group [" + p.getBloodGroup() + "]: ");
        if (bloodGroup.isEmpty()) bloodGroup = p.getBloodGroup();

        String phone = readLine("New Phone [" + p.getPhone() + "]: ");
        if (phone.isEmpty()) phone = p.getPhone();

        String address = readLine("New Address [" + p.getAddress() + "]: ");
        if (address.isEmpty()) address = p.getAddress();

        Patient updated = new Patient(id, name, age, gender, disease, p.getAdmissionDate(), severity);
        updated.setBloodGroup(bloodGroup);
        updated.setPhone(phone);
        updated.setAddress(address);
        updated.setRegistrationDate(p.getRegistrationDate());
        
        patientService.updatePatient(updated);
        ConsoleColors.printSuccess("Patient record updated (delete + re-insert in BST & AVL).");
        pressEnterToContinue();
    }

    private void deletePatient() {
        ConsoleColors.printSubHeader("Delete Patient Record");
        int id = readPositiveInt("Enter Patient ID to delete: ");
        Patient p = patientService.searchPatient(id);
        if (p == null) {
            ConsoleColors.printError("Patient with ID " + id + " not found.");
            pressEnterToContinue();
            return;
        }
        ConsoleColors.printWarning("Deleting: " + p);
        String confirm = readLine("Are you sure? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            patientService.deletePatient(id);
            ConsoleColors.printSuccess("Patient removed from BST and AVL Tree.");
        } else {
            ConsoleColors.printInfo("Deletion cancelled.");
        }
        pressEnterToContinue();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 10: DOCTOR MANAGEMENT
    // ═══════════════════════════════════════════════════════════════════════

    private void handleDoctorManagement() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("DOCTOR MANAGEMENT - HashMap & Priority Queue");
            ConsoleColors.printMenuItem("1", "Add New Doctor");
            ConsoleColors.printMenuItem("2", "Search Doctor by ID");
            ConsoleColors.printMenuItem("3", "Update Doctor Record");
            ConsoleColors.printMenuItem("4", "Delete Doctor Record");
            ConsoleColors.printMenuItem("5", "Display All Doctors");
            ConsoleColors.printMenuItem("6", "View Doctor's Appointments");
            ConsoleColors.printMenuItem("7", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1": addDoctor(); break;
                case "2": searchDoctor(); break;
                case "3": updateDoctor(); break;
                case "4": deleteDoctor(); break;
                case "5": displayDoctors(); break;
                case "6": viewDoctorAppointments(); break;
                case "7": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    private void addDoctor() {
        ConsoleColors.printSubHeader("Add New Doctor");
        int id = readPositiveInt("Doctor ID (positive integer): ");
        if (doctorService.getDoctorById(id) != null) {
            ConsoleColors.printError("Doctor ID already exists!");
            pressEnterToContinue();
            return;
        }
        String name = readNonEmpty("Doctor Name: ");
        String specialization = readNonEmpty("Specialization: ");
        String department = readLine("Department (optional): ");
        String phone = readLine("Phone (optional): ");
        String email = readLine("Email (optional): ");

        Doctor d = new Doctor(id, name, specialization);
        d.setDepartment(department);
        d.setPhone(phone);
        d.setEmail(email);
        
        doctorService.addDoctor(d);
        ConsoleColors.printSuccess("Doctor '" + name + "' added successfully.");
        pressEnterToContinue();
    }

    private void searchDoctor() {
        ConsoleColors.printSubHeader("Search Doctor by ID");
        int id = readPositiveInt("Enter Doctor ID: ");
        Doctor d = doctorService.getDoctorById(id);
        if (d != null) {
            ConsoleColors.printSuccess("Doctor found!");
            System.out.println("    " + d);
        } else {
            ConsoleColors.printError("Doctor with ID " + id + " not found.");
        }
        pressEnterToContinue();
    }

    private void updateDoctor() {
        ConsoleColors.printSubHeader("Update Doctor Record");
        int id = readPositiveInt("Enter Doctor ID to update: ");
        Doctor d = doctorService.getDoctorById(id);
        if (d == null) {
            ConsoleColors.printError("Doctor with ID " + id + " not found.");
            pressEnterToContinue();
            return;
        }
        ConsoleColors.printInfo("Current Record: " + d);
        String name = readLine("New Name [" + d.getDoctorName() + "]: ");
        if (name.isEmpty()) name = d.getDoctorName();
        
        String spec = readLine("New Specialization [" + d.getSpecialization() + "]: ");
        if (spec.isEmpty()) spec = d.getSpecialization();

        String dept = readLine("New Department [" + d.getDepartment() + "]: ");
        if (dept.isEmpty()) dept = d.getDepartment();

        String phone = readLine("New Phone [" + d.getPhone() + "]: ");
        if (phone.isEmpty()) phone = d.getPhone();

        Doctor updated = new Doctor(id, name, spec);
        updated.setDepartment(dept);
        updated.setPhone(phone);
        updated.setEmail(d.getEmail());
        updated.setAvailableSlots(d.getAvailability());
        
        doctorService.updateDoctor(updated);
        ConsoleColors.printSuccess("Doctor record updated.");
        pressEnterToContinue();
    }

    private void deleteDoctor() {
        ConsoleColors.printSubHeader("Delete Doctor Record");
        int id = readPositiveInt("Enter Doctor ID to delete: ");
        Doctor d = doctorService.getDoctorById(id);
        if (d == null) {
            ConsoleColors.printError("Doctor with ID " + id + " not found.");
            pressEnterToContinue();
            return;
        }
        ConsoleColors.printWarning("Deleting: " + d);
        String confirm = readLine("Are you sure? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            doctorService.deleteDoctor(id);
            ConsoleColors.printSuccess("Doctor removed.");
        } else {
            ConsoleColors.printInfo("Deletion cancelled.");
        }
        pressEnterToContinue();
    }

    private void displayDoctors() {
        ConsoleColors.printSubHeader("All Doctors");
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            ConsoleColors.printWarning("No doctors registered.");
        } else {
            for (Doctor d : doctors) {
                System.out.println("    " + d);
            }
        }
        pressEnterToContinue();
    }

    private String formatTime(int time) {
        return String.format("%02d:%02d", time / 100, time % 100);
    }

    private void viewDoctorAppointments() {
        ConsoleColors.printSubHeader("View Doctor's Appointments");
        int docId = readPositiveInt("Enter Doctor ID: ");
        Doctor d = doctorService.getDoctorById(docId);
        if (d == null) {
            ConsoleColors.printError("Doctor with ID " + docId + " not found.");
            pressEnterToContinue();
            return;
        }
        
        List<Appointment> allAppointments = appointmentService.getRequestedAppointments();
        boolean found = false;
        
        ConsoleColors.printInfo("Appointments for " + d.getDoctorName() + ":");
        for (Appointment a : allAppointments) {
            if (a.getDoctor() != null && a.getDoctor().getDoctorId() == docId) {
                System.out.println("    - Patient: " + a.getPatientName() + " | Time: " + formatTime(a.getStartTime()) + " to " + formatTime(a.getEndTime()));
                found = true;
            }
        }
        
        if (!found) {
            ConsoleColors.printWarning("No appointments booked for this doctor.");
        }
        pressEnterToContinue();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 2: APPOINTMENT SCHEDULING (Greedy + Topological Sort)
    // ═══════════════════════════════════════════════════════════════════════

    private void handleAppointmentScheduling() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("APPOINTMENT SCHEDULING - Greedy Algorithm");
            ConsoleColors.printMenuItem("1", "Add Appointment Request");
            ConsoleColors.printMenuItem("2", "Generate Optimal Schedule (Activity Selection)");
            ConsoleColors.printMenuItem("3", "View All Appointment Requests");
            ConsoleColors.printMenuItem("4", "Schedule Treatment Procedures (Topological Sort)");
            ConsoleColors.printMenuItem("5", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1": addAppointment(); break;
                case "2": generateSchedule(); break;
                case "3": viewAllAppointments(); break;
                case "4": topologicalSortDemo(); break;
                case "5": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    private void addAppointment() {
        ConsoleColors.printSubHeader("Add Appointment Request");
        int patientId = readPositiveInt("Enter Patient ID: ");
        Patient p = patientService.searchPatient(patientId);
        if (p == null) {
            ConsoleColors.printError("Patient not found. Please register the patient first (Module 1).");
            pressEnterToContinue();
            return;
        }
        ConsoleColors.printInfo("Patient: " + p.getName());
        System.out.println();

        // Show available doctors
        ConsoleColors.printInfo("Available Doctors:");
        List<Doctor> allDoctors = doctorService.getAllDoctors();
        for (Doctor d : allDoctors) {
            System.out.println("    " + d.getDoctorId() + ". " + d.getDoctorName() + " (" + d.getSpecialization() + ")");
        }
        System.out.println();

        int docId = readInt("Enter Doctor ID: ");
        Doctor doctor = doctorService.getDoctorById(docId);
        if (doctor == null) {
            ConsoleColors.printError("Invalid Doctor ID. Please choose from the list above.");
            pressEnterToContinue();
            return;
        }

        int startTime = readTimeInput("Start Time (e.g., 09:00 for 9:00 AM, 14:30 for 2:30 PM): ");
        int endTime = readTimeInput("End Time (e.g., 09:30 for 9:30 AM, 15:00 for 3:00 PM): ");

        if (endTime <= startTime) {
            ConsoleColors.printError("End time must be after start time.");
            pressEnterToContinue();
            return;
        }

        appointmentService.addAppointment(p.getName(), doctor, startTime, endTime);
        ConsoleColors.printSuccess("Appointment request added: " + p.getName() + " with " + doctor.getDoctorName()
                + " [" + startTime + " - " + endTime + "]");
        pressEnterToContinue();
    }

    private void generateSchedule() {
        ConsoleColors.printSubHeader("Generate Optimal Schedule - Activity Selection (Greedy)");
        ConsoleColors.printInfo("Available Doctors:");
        List<Doctor> allDoctors = doctorService.getAllDoctors();
        for (Doctor d : allDoctors) {
            System.out.println("    " + d.getDoctorId() + ". " + d.getDoctorName() + " (" + d.getSpecialization() + ")");
        }
        System.out.println();

        int docId = readInt("Enter Doctor ID to generate schedule for: ");
        Doctor doctor = doctorService.getDoctorById(docId);
        if (doctor != null) {
            ConsoleColors.printLoading("Applying Activity Selection (sort by end-time, greedy pick)");
            System.out.println();
            appointmentService.generateOptimalScheduleForDoctor(doctor);
        } else {
            ConsoleColors.printError("Doctor not found.");
        }
        pressEnterToContinue();
    }

    private void viewAllAppointments() {
        ConsoleColors.printSubHeader("All Appointment Requests");
        List<Appointment> appointments = appointmentService.getRequestedAppointments();
        if (appointments.isEmpty()) {
            ConsoleColors.printWarning("No appointment requests found.");
            pressEnterToContinue();
            return;
        }
        ConsoleColors.printTableHeader("Patient", "Doctor", "Start", "End");
        for (Appointment a : appointments) {
            ConsoleColors.printTableRow(
                a.getPatientName(),
                a.getDoctor().getDoctorName(),
                formatTime(a.getStartTime()),
                formatTime(a.getEndTime())
            );
        }
        System.out.println();
        ConsoleColors.printInfo("Total: " + appointments.size() + " appointment request(s)");
        pressEnterToContinue();
    }

    private void topologicalSortDemo() {
        ConsoleColors.printSubHeader("Treatment Procedure Scheduling - Topological Sort (Kahn's Algorithm)");
        ConsoleColors.printInfo("Sample treatment dependency graph:");
        System.out.println();
        System.out.println("    Blood Test       --> Diagnosis");
        System.out.println("    X-Ray            --> Diagnosis");
        System.out.println("    Diagnosis        --> Treatment Plan");
        System.out.println("    Treatment Plan   --> Medication");
        System.out.println("    Treatment Plan   --> Surgery Prep");
        System.out.println("    Surgery Prep     --> Surgery");
        System.out.println("    Surgery          --> Recovery");
        System.out.println("    Medication       --> Follow-up");
        System.out.println();

        Map<String, List<String>> adjList = new LinkedHashMap<>();
        adjList.put("Blood Test",     Arrays.asList("Diagnosis"));
        adjList.put("X-Ray",          Arrays.asList("Diagnosis"));
        adjList.put("Diagnosis",      Arrays.asList("Treatment Plan"));
        adjList.put("Treatment Plan", Arrays.asList("Medication", "Surgery Prep"));
        adjList.put("Surgery Prep",   Arrays.asList("Surgery"));
        adjList.put("Surgery",        Arrays.asList("Recovery"));
        adjList.put("Medication",     Arrays.asList("Follow-up"));
        adjList.put("Recovery",       new ArrayList<>());
        adjList.put("Follow-up",      new ArrayList<>());

        ConsoleColors.printLoading("Running Kahn's Algorithm (BFS-based Topological Sort)");
        System.out.println();

        long startTime = System.nanoTime();
        TopologicalSort ts = new TopologicalSort();
        List<String> order = ts.sort(adjList);
        long endTime = System.nanoTime();

        if (!order.isEmpty()) {
            ConsoleColors.printSuccess("Valid procedure ordering found:");
            System.out.println();
            for (int i = 0; i < order.size(); i++) {
                System.out.println("    Step " + (i + 1) + ":  " + order.get(i));
            }
            System.out.println();
            ConsoleColors.printInfo("Time Complexity: O(V + E)");
            ConsoleColors.printInfo("Execution Time: " + (endTime - startTime) + " ns");
        }
        pressEnterToContinue();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 3: MEDICAL RECORDS INDEXING (B-Tree & B+ Tree)
    // ═══════════════════════════════════════════════════════════════════════

    private void handleMedicalRecordsIndexing() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("MEDICAL RECORDS INDEXING - B-Tree & B+ Tree");
            ConsoleColors.printMenuItem("1", "Add Medical Record");
            ConsoleColors.printMenuItem("2", "Search Record by ID");
            ConsoleColors.printMenuItem("3", "Update Medical Record");
            ConsoleColors.printMenuItem("4", "View B-Tree Structure (Level-Order)");
            ConsoleColors.printMenuItem("5", "View B+ Tree Leaves (Linked-List)");
            ConsoleColors.printMenuItem("6", "Range Query on Records (B+ Tree)");
            ConsoleColors.printMenuItem("7", "Cleanup Old Records (30-Day Retention)");
            ConsoleColors.printMenuItem("8", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    ConsoleColors.printSubHeader("Add Medical Record");
                    int pid = readPositiveInt("Patient ID: ");
                    String diag = readNonEmpty("Diagnosis: ");
                    String treat = readNonEmpty("Treatment: ");
                    MedicalRecord rec = medicalRecordService.addRecord(pid, LocalDate.now(), diag, treat);
                    ConsoleColors.printSuccess("Record added with ID: " + rec.getRecordId() + " (indexed in B-Tree & B+ Tree)");
                    pressEnterToContinue();
                    break;
                case "2":
                    ConsoleColors.printSubHeader("Search Medical Record");
                    int rid = readPositiveInt("Enter Record ID: ");
                    ConsoleColors.printLoading("Searching in B-Tree index (O(log N))");
                    MedicalRecord found = medicalRecordService.searchRecordById(rid);
                    if (found != null) {
                        ConsoleColors.printSuccess("Record found!");
                        System.out.println("    " + found);
                    } else {
                        ConsoleColors.printError("Record with ID " + rid + " not found.");
                    }
                    pressEnterToContinue();
                    break;
                case "3":
                    ConsoleColors.printSubHeader("Update Medical Record");
                    int uid = readPositiveInt("Enter Record ID to update: ");
                    MedicalRecord existing = medicalRecordService.searchRecordById(uid);
                    if (existing != null) {
                        ConsoleColors.printInfo("Current: " + existing);
                        String newDiag = readNonEmpty("New Diagnosis: ");
                        String newTreat = readNonEmpty("New Treatment: ");
                        medicalRecordService.updateRecord(uid, newDiag, newTreat);
                        ConsoleColors.printSuccess("Record updated successfully.");
                    } else {
                        ConsoleColors.printError("Record not found.");
                    }
                    pressEnterToContinue();
                    break;
                case "4":
                    ConsoleColors.printSubHeader("B-Tree Structure (Level-Order Traversal)");
                    medicalRecordService.displayBTree();
                    System.out.println();
                    ConsoleColors.printInfo("B-Tree maintains sorted keys across levels for O(log N) search.");
                    pressEnterToContinue();
                    break;
                case "5":
                    ConsoleColors.printSubHeader("B+ Tree Leaf Nodes (Linked-List Traversal)");
                    medicalRecordService.displayBPlusTree();
                    System.out.println();
                    ConsoleColors.printInfo("B+ Tree links leaf nodes for efficient sequential access and range queries.");
                    pressEnterToContinue();
                    break;
                case "6":
                    ConsoleColors.printSubHeader("Range Query on B+ Tree");
                    int start = readPositiveInt("Start Record ID: ");
                    int end = readPositiveInt("End Record ID: ");
                    if (end < start) {
                        ConsoleColors.printError("End ID must be >= Start ID.");
                        pressEnterToContinue();
                        break;
                    }
                    ConsoleColors.printLoading("Executing range query on B+ Tree");
                    List<Integer> results = medicalRecordService.rangeQuery(start, end);
                    if (results.isEmpty()) {
                        ConsoleColors.printWarning("No records found in range [" + start + ", " + end + "].");
                    } else {
                        ConsoleColors.printSuccess("Record IDs in range [" + start + ", " + end + "]: " + results);
                        ConsoleColors.printInfo("Time Complexity: O(log N + K) where K = number of results");
                    }
                    pressEnterToContinue();
                    break;
                case "7":
                    ConsoleColors.printSubHeader("Cleanup Old Records (30-Day Retention Policy)");
                    ConsoleColors.printLoading("Scanning for records older than 30 days");
                    medicalRecordService.cleanupOldRecords();
                    pressEnterToContinue();
                    break;
                case "8": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 4: HOSPITAL RESOURCE ALLOCATION (Knapsack DP)
    // ═══════════════════════════════════════════════════════════════════════

    private void handleResourceAllocation() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("HOSPITAL RESOURCE ALLOCATION - Knapsack DP");
            ConsoleColors.printMenuItem("1", "Plan Equipment Budget (0/1 Knapsack)");
            ConsoleColors.printMenuItem("2", "Allocate Oxygen Supply (Fractional Knapsack)");
            ConsoleColors.printMenuItem("3", "Allocate Physical Resource");
            ConsoleColors.printMenuItem("4", "Release Physical Resource");
            ConsoleColors.printMenuItem("5", "Emergency Resource Allocation");
            ConsoleColors.printMenuItem("6", "View All Physical Resources");
            ConsoleColors.printMenuItem("7", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    ConsoleColors.printSubHeader("Equipment Budget Planning - 0/1 Knapsack (Dynamic Programming)");
                    ConsoleColors.printInfo("Available Hospital Equipment:");
                    ConsoleColors.printTableHeader("Equipment", "Cost ($)", "Benefit");
                    ConsoleColors.printTableRow("Ventilator A",   "5,000",  "100");
                    ConsoleColors.printTableRow("ICU Bed B",      "3,000",  "80");
                    ConsoleColors.printTableRow("Defibrillator C","2,000",  "60");
                    ConsoleColors.printTableRow("ECG Machine D",  "1,500",  "40");
                    ConsoleColors.printTableRow("MRI Scanner E",  "8,000",  "150");
                    ConsoleColors.printTableRow("Ultrasound F",   "2,500",  "55");
                    System.out.println();

                    int budget = readPositiveInt("Enter total budget ($): ");
                    Knapsack01.Item[] items = {
                        new Knapsack01.Item("Ventilator A",   5000, 100),
                        new Knapsack01.Item("ICU Bed B",      3000,  80),
                        new Knapsack01.Item("Defibrillator C",2000,  60),
                        new Knapsack01.Item("ECG Machine D",  1500,  40),
                        new Knapsack01.Item("MRI Scanner E",  8000, 150),
                        new Knapsack01.Item("Ultrasound F",   2500,  55)
                    };
                    System.out.println();
                    ConsoleColors.printLoading("Running 0/1 Knapsack DP algorithm");
                    System.out.println();
                    resourceService.planEquipmentBudget(budget, items);
                    System.out.println();
                    ConsoleColors.printInfo("Time Complexity: O(N * W) where N = items, W = budget capacity");
                    pressEnterToContinue();
                    break;

                case "2":
                    ConsoleColors.printSubHeader("Oxygen Supply Allocation - Fractional Knapsack (Greedy)");
                    ConsoleColors.printInfo("Patient Oxygen Requests:");
                    ConsoleColors.printTableHeader("Patient", "Severity", "Needs (L)");
                    ConsoleColors.printTableRow("Patient A (Critical)", "100", "10");
                    ConsoleColors.printTableRow("Patient B (Stable)",   "60",  "20");
                    ConsoleColors.printTableRow("Patient C (Severe)",  "120",  "30");
                    ConsoleColors.printTableRow("Patient D (Moderate)",  "80", "15");
                    System.out.println();

                    int capacity = readPositiveInt("Enter total Oxygen available (Liters): ");
                    FractionalKnapsack.PatientRequest[] requests = {
                        new FractionalKnapsack.PatientRequest("Patient A (Critical)", 100, 10),
                        new FractionalKnapsack.PatientRequest("Patient B (Stable)",    60, 20),
                        new FractionalKnapsack.PatientRequest("Patient C (Severe)",   120, 30),
                        new FractionalKnapsack.PatientRequest("Patient D (Moderate)",  80, 15)
                    };
                    System.out.println();
                    ConsoleColors.printLoading("Running Fractional Knapsack (Greedy - sort by value/weight ratio)");
                    System.out.println();
                    resourceService.allocateOxygen(capacity, requests);
                    System.out.println();
                    ConsoleColors.printInfo("Time Complexity: O(N log N) for sorting by value/weight ratio");
                    pressEnterToContinue();
                    break;
                case "3":
                    ConsoleColors.printSubHeader("Allocate Physical Resource");
                    String allocateType = readLine("Enter Resource Type (e.g., Bed, Oxygen, Ventilator): ");
                    int allocateCount = readPositiveInt("Enter quantity to allocate: ");
                    if (resourceService.allocateResource(allocateType, allocateCount)) {
                        ConsoleColors.printSuccess("Successfully allocated " + allocateCount + " " + allocateType + "(s).");
                    } else {
                        ConsoleColors.printError("Failed to allocate. Not enough available.");
                    }
                    pressEnterToContinue();
                    break;
                case "4":
                    ConsoleColors.printSubHeader("Release Physical Resource");
                    String releaseType = readLine("Enter Resource Type (e.g., Bed, Oxygen, Ventilator): ");
                    int releaseCount = readPositiveInt("Enter quantity to release: ");
                    resourceService.releaseResource(releaseType, releaseCount);
                    ConsoleColors.printSuccess("Released " + releaseCount + " " + releaseType + "(s).");
                    pressEnterToContinue();
                    break;
                case "5":
                    ConsoleColors.printSubHeader("Emergency Resource Allocation");
                    String emergencyType = readLine("Enter Resource Type (e.g., Bed, Oxygen, Ventilator): ");
                    Resource allocated = resourceService.emergencyAllocate(emergencyType);
                    if (allocated != null) {
                        ConsoleColors.printSuccess("Emergency allocation successful.");
                    }
                    pressEnterToContinue();
                    break;
                case "6":
                    ConsoleColors.printSubHeader("View All Physical Resources");
                    for (Resource r : resourceService.getAllResources()) {
                        System.out.println("  - " + r.toString());
                    }
                    pressEnterToContinue();
                    break;
                case "7": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 5: EMERGENCY ROUTING (Shortest Path Algorithms)
    // ═══════════════════════════════════════════════════════════════════════

    private void handleEmergencyRouting() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("EMERGENCY ROUTING - Hospital Room Navigation");
            ConsoleColors.printMenuItem("1", "View Hospital Room Map (Visual)");
            ConsoleColors.printMenuItem("2", "Find Fastest Route (Dijkstra's Algorithm)");
            ConsoleColors.printMenuItem("3", "Find Fastest Route (Bellman-Ford Algorithm)");
            ConsoleColors.printMenuItem("4", "All-Pairs Shortest Paths (Floyd-Warshall)");
            ConsoleColors.printMenuItem("5", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    ConsoleColors.printSubHeader("Hospital Room Network (Visual Map)");
                    hospitalGraph.displayVisualMap();
                    pressEnterToContinue();
                    break;
                case "2":
                    ConsoleColors.printSubHeader("Dijkstra's Algorithm - O((V+E) log V)");
                    printHospitalRoomList();
                    int src2 = readIntInRange("Enter Source Room ID (0-7): ", 0, 7);
                    ConsoleColors.printLoading("Computing shortest paths using Dijkstra (Priority Queue)");
                    new Dijkstra().findFastestRoute(hospitalGraph, src2);
                    pressEnterToContinue();
                    break;
                case "3":
                    ConsoleColors.printSubHeader("Bellman-Ford Algorithm - O(V * E)");
                    printHospitalRoomList();
                    int src3 = readIntInRange("Enter Source Room ID (0-7): ", 0, 7);
                    ConsoleColors.printLoading("Computing shortest paths using Bellman-Ford (handles negative weights)");
                    new BellmanFord().findFastestRoute(hospitalGraph, src3);
                    pressEnterToContinue();
                    break;
                case "4":
                    ConsoleColors.printSubHeader("Floyd-Warshall Algorithm - O(V^3)");
                    ConsoleColors.printLoading("Computing all-pairs shortest paths matrix");
                    new FloydWarshall().calculateDistances(hospitalGraph);
                    pressEnterToContinue();
                    break;
                case "5": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    private void printHospitalRoomList() {
        ConsoleColors.printInfo("Hospital Rooms:");
        for (int i = 0; i < hospitalGraph.getVertices(); i++) {
            System.out.println("    " + i + ". " + hospitalGraph.getNodeName(i));
        }
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 6: DEPARTMENT NETWORK ANALYSIS (Graph Algorithms)
    // ═══════════════════════════════════════════════════════════════════════

    private void handleNetworkAnalysis() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("DEPARTMENT NETWORK ANALYSIS - Graph Algorithms");
            ConsoleColors.printMenuItem("1", "View Department Network (Visual Map)");
            ConsoleColors.printMenuItem("2", "BFS Traversal from Department");
            ConsoleColors.printMenuItem("3", "DFS Traversal from Department");
            ConsoleColors.printMenuItem("4", "Detect Cycles in Workflow");
            ConsoleColors.printMenuItem("5", "Minimum Spanning Tree (Prim's Algorithm)");
            ConsoleColors.printMenuItem("6", "Minimum Spanning Tree (Kruskal's Algorithm)");
            ConsoleColors.printMenuItem("7", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    ConsoleColors.printSubHeader("Department Network (Visual Map)");
                    departmentGraph.displayVisualMap();
                    pressEnterToContinue();
                    break;
                case "2":
                    ConsoleColors.printSubHeader("BFS Traversal - O(V + E)");
                    printDepartmentList();
                    int bfsStart = readIntInRange("Enter Starting Department ID (0-5): ", 0, 5);
                    ConsoleColors.printLoading("Running Breadth-First Search");
                    new BFS().traverse(departmentGraph, bfsStart);
                    pressEnterToContinue();
                    break;
                case "3":
                    ConsoleColors.printSubHeader("DFS Traversal - O(V + E)");
                    printDepartmentList();
                    int dfsStart = readIntInRange("Enter Starting Department ID (0-5): ", 0, 5);
                    ConsoleColors.printLoading("Running Depth-First Search");
                    new DFS().traverse(departmentGraph, dfsStart);
                    pressEnterToContinue();
                    break;
                case "4":
                    ConsoleColors.printSubHeader("Cycle Detection - DFS with Recursion Stack");
                    ConsoleColors.printLoading("Running cycle detection algorithm");
                    boolean hasCycle = new DFS().hasCycle(departmentGraph);
                    System.out.println();
                    if (hasCycle) {
                        ConsoleColors.printWarning("Cycle detected in the department workflow!");
                    } else {
                        ConsoleColors.printSuccess("No cycles detected. The workflow is acyclic (DAG).");
                    }
                    pressEnterToContinue();
                    break;
                case "5":
                    ConsoleColors.printSubHeader("Prim's MST - O((V+E) log V)");
                    ConsoleColors.printLoading("Computing Minimum Spanning Tree using Prim's algorithm");
                    new PrimMST().findMST(departmentGraph);
                    System.out.println();
                    ConsoleColors.printInfo("MST connects all departments with minimum total cable/wiring cost.");
                    pressEnterToContinue();
                    break;
                case "6":
                    ConsoleColors.printSubHeader("Kruskal's MST - O(E log E)");
                    ConsoleColors.printLoading("Computing Minimum Spanning Tree using Kruskal's algorithm (Union-Find)");
                    new KruskalMST().findMST(departmentGraph);
                    System.out.println();
                    ConsoleColors.printInfo("Kruskal's uses Union-Find (Disjoint Set) with path compression.");
                    pressEnterToContinue();
                    break;
                case "7": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    private void printDepartmentList() {
        ConsoleColors.printInfo("Hospital Departments:");
        for (int i = 0; i < 6; i++) {
            System.out.println("    " + i + ". " + departmentGraph.getNodeName(i));
        }
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 7: ANALYTICS & REPORTS (Sorting Algorithms)
    // ═══════════════════════════════════════════════════════════════════════

    private void handleAnalytics() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("ANALYTICS & REPORTS - Strategy Pattern");
            ConsoleColors.printMenuItem("1", "Sort Patients by Admission Date (Merge Sort) & Export");
            ConsoleColors.printMenuItem("2", "Sort Patients by Severity (Quick Sort) & Export");
            ConsoleColors.printMenuItem("3", "Emergency Queue by Severity (Heap Sort)");
            ConsoleColors.printMenuItem("4", "Sort Medical Records by ID (Counting Sort) & Export");
            ConsoleColors.printMenuItem("5", "Sort Medical Records by ID (Radix Sort) & Export");
            ConsoleColors.printMenuItem("6", "Benchmark All Sorting Algorithms");
            ConsoleColors.printMenuItem("7", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1": sortByAdmissionDate(); break;
                case "2": sortRecordsByQuickSort(); break;
                case "3": emergencyQueueHeapSort(); break;
                case "4": sortAgesCountingSort(); break;
                case "5": sortIdsRadixSort(); break;
                case "6": benchmarkAllSorts(); break;
                case "7": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    private void sortByAdmissionDate() {
        ConsoleColors.printSubHeader("Merge Sort - Sort Patients by Admission Date - O(N log N)");
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            ConsoleColors.printWarning("No patients available. Add patients first.");
            pressEnterToContinue();
            return;
        }
        Patient[] arr = patients.toArray(new Patient[0]);
        ConsoleColors.printLoading("Running Merge Sort (divide & conquer, stable)");
        long t1 = System.nanoTime();
        new MergeSort().sort(arr, 0, arr.length - 1);
        long t2 = System.nanoTime();
        System.out.println();
        ConsoleColors.printSuccess("Patients sorted by admission date:");
        for (Patient p : arr) {
            System.out.println("    " + p);
        }
        System.out.println();
        ConsoleColors.printInfo("Execution Time: " + (t2 - t1) + " ns");
        ConsoleColors.printInfo("Time Complexity: O(N log N) | Space: O(N)");
        
        ReportGenerator.generatePatientReport("Patients_MergeSort_Report.txt", Arrays.asList(arr), "Sorted by Admission Date (Merge Sort)");
        ConsoleColors.printSuccess("Report exported to: Patients_MergeSort_Report.txt");
        pressEnterToContinue();
    }

    private void sortRecordsByQuickSort() {
        ConsoleColors.printSubHeader("Strategy Pattern: Quick Sort - Sort Patients by Severity - O(N log N) avg");
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            ConsoleColors.printWarning("No patients available.");
            pressEnterToContinue();
            return;
        }
        Patient[] arr = patients.toArray(new Patient[0]);
        
        SortingContext<Patient> context = new SortingContext<>();
        context.setStrategy(new PatientQuickSortStrategy());
        
        ConsoleColors.printLoading("Running Quick Sort via Strategy (in-place, partition-based)");
        long t1 = System.nanoTime();
        context.sortItems(arr);
        long t2 = System.nanoTime();
        
        System.out.println();
        ConsoleColors.printSuccess("Patients sorted by Severity:");
        for (Patient p : arr) {
            System.out.println("    " + p);
        }
        System.out.println();
        ConsoleColors.printInfo("Execution Time: " + (t2 - t1) + " ns");
        ConsoleColors.printInfo("Time Complexity: O(N log N) avg | Space: O(log N)");
        
        ReportGenerator.generatePatientReport("Patients_QuickSort_Report.txt", Arrays.asList(arr), "Sorted by Severity (Quick Sort)");
        ConsoleColors.printSuccess("Report exported to: Patients_QuickSort_Report.txt");
        
        pressEnterToContinue();
    }

    private void emergencyQueueHeapSort() {
        ConsoleColors.printSubHeader("Strategy Pattern: Heap Sort - Emergency Queue by Severity - O(N log N)");
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            ConsoleColors.printWarning("No patients available.");
            pressEnterToContinue();
            return;
        }
        Patient[] queue = patients.toArray(new Patient[0]);

        SortingContext<Patient> context = new SortingContext<>();
        context.setStrategy(new PatientHeapSortStrategy());

        ConsoleColors.printLoading("Running Heap Sort via Strategy (max-heap build + extract)");
        long t1 = System.nanoTime();
        context.sortItems(queue);
        long t2 = System.nanoTime();
        System.out.println();

        ConsoleColors.printSuccess("Emergency Queue (Highest Priority First):");
        ConsoleColors.printTableHeader("Priority", "Patient ID", "Severity");
        for (int i = 0; i < queue.length; i++) {
            ConsoleColors.printTableRow(
                "#" + (i + 1),
                String.valueOf(queue[i].getPatientId()),
                String.valueOf(queue[i].getSeverityScore())
            );
        }
        System.out.println();
        ConsoleColors.printInfo("Execution Time: " + (t2 - t1) + " ns");
        ConsoleColors.printInfo("Time Complexity: O(N log N) | Space: O(1) in-place");
        pressEnterToContinue();
    }

    private void sortAgesCountingSort() {
        ConsoleColors.printSubHeader("Counting Sort - Sort Medical Records by ID - O(N + K)");
        List<MedicalRecord> records = medicalRecordService.getAllRecords();
        if (records.isEmpty()) {
            ConsoleColors.printWarning("No medical records available.");
            pressEnterToContinue();
            return;
        }
        MedicalRecord[] arr = records.toArray(new MedicalRecord[0]);

        ConsoleColors.printLoading("Running Counting Sort (non-comparison based)");
        long t1 = System.nanoTime();
        new CountingSort().sort(arr);
        long t2 = System.nanoTime();
        System.out.println();

        ConsoleColors.printSuccess("Records sorted by ID (Counting Sort):");
        for (MedicalRecord r : arr) {
            System.out.println("    " + r);
        }
        ConsoleColors.printInfo("Execution Time: " + (t2 - t1) + " ns");
        ConsoleColors.printInfo("Time Complexity: O(N + K) where K = max ID value");
        
        ReportGenerator.generateRecordReport("Records_CountingSort_Report.txt", Arrays.asList(arr), "Sorted by ID (Counting Sort)");
        ConsoleColors.printSuccess("Report exported to: Records_CountingSort_Report.txt");
        pressEnterToContinue();
    }

    private void sortIdsRadixSort() {
        ConsoleColors.printSubHeader("Strategy Pattern: Radix Sort - Sort Medical Records by ID - O(d * (N + K))");
        List<MedicalRecord> records = medicalRecordService.getAllRecords();
        if (records.isEmpty()) {
            ConsoleColors.printWarning("No medical records available.");
            pressEnterToContinue();
            return;
        }
        MedicalRecord[] arr = records.toArray(new MedicalRecord[0]);

        SortingContext<MedicalRecord> context = new SortingContext<>();
        context.setStrategy(new RecordRadixSortStrategy());

        ConsoleColors.printLoading("Running Radix Sort via Strategy (LSD, digit-by-digit)");
        long t1 = System.nanoTime();
        context.sortItems(arr);
        long t2 = System.nanoTime();
        System.out.println();

        ConsoleColors.printSuccess("Records sorted by ID (Radix Sort):");
        for (MedicalRecord r : arr) {
            System.out.println("    " + r);
        }
        ConsoleColors.printInfo("Execution Time: " + (t2 - t1) + " ns");
        ConsoleColors.printInfo("Time Complexity: O(d * (N + K)) where d = number of digits");
        
        ReportGenerator.generateRecordReport("Records_RadixSort_Report.txt", Arrays.asList(arr), "Sorted by ID (Radix Sort)");
        ConsoleColors.printSuccess("Report exported to: Records_RadixSort_Report.txt");
        pressEnterToContinue();
    }

    private void benchmarkAllSorts() {
        ConsoleColors.printSubHeader("Benchmark All Sorting Algorithms");
        List<Patient> patients = patientService.getAllPatients();
        List<MedicalRecord> records = medicalRecordService.getAllRecords();

        if (patients.isEmpty() || records.isEmpty()) {
            ConsoleColors.printWarning("No data available. Add patients and records first.");
            pressEnterToContinue();
            return;
        }

        ConsoleColors.printLoading("Running all 5 sorting algorithms");
        System.out.println();
        ConsoleColors.printTableHeader("Algorithm", "Data", "Time (ns)");

        // 1. Merge Sort
        Patient[] mergeArr = patients.toArray(new Patient[0]);
        long t1 = System.nanoTime();
        new MergeSort().sort(mergeArr, 0, mergeArr.length - 1);
        long t2 = System.nanoTime();
        ConsoleColors.printTableRow("Merge Sort", "Patients by Date", String.valueOf(t2 - t1));

        // 2. Quick Sort
        Patient[] quickArr = patients.toArray(new Patient[0]);
        SortingContext<Patient> quickContext = new SortingContext<>();
        quickContext.setStrategy(new PatientQuickSortStrategy());
        t1 = System.nanoTime();
        quickContext.sortItems(quickArr);
        t2 = System.nanoTime();
        ConsoleColors.printTableRow("Quick Sort", "Patients by Severity", String.valueOf(t2 - t1));

        // 3. Heap Sort
        Patient[] heapArr = patients.toArray(new Patient[0]);
        SortingContext<Patient> heapContext = new SortingContext<>();
        heapContext.setStrategy(new PatientHeapSortStrategy());
        t1 = System.nanoTime();
        heapContext.sortItems(heapArr);
        t2 = System.nanoTime();
        ConsoleColors.printTableRow("Heap Sort", "Emergency Queue", String.valueOf(t2 - t1));

        // 4. Counting Sort
        MedicalRecord[] countArr = records.toArray(new MedicalRecord[0]);
        t1 = System.nanoTime();
        new CountingSort().sort(countArr);
        t2 = System.nanoTime();
        ConsoleColors.printTableRow("Counting Sort", "Records by ID", String.valueOf(t2 - t1));

        // 5. Radix Sort
        MedicalRecord[] radixArr = records.toArray(new MedicalRecord[0]);
        SortingContext<MedicalRecord> radixContext = new SortingContext<>();
        radixContext.setStrategy(new RecordRadixSortStrategy());
        t1 = System.nanoTime();
        radixContext.sortItems(radixArr);
        t2 = System.nanoTime();
        ConsoleColors.printTableRow("Radix Sort", "Records by ID", String.valueOf(t2 - t1));

        System.out.println();
        ConsoleColors.printInfo("Note: Benchmark times vary with JVM warm-up and data size.");
        pressEnterToContinue();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 8: PATIENT NAME CORRECTION (Edit Distance & LCS)
    // ═══════════════════════════════════════════════════════════════════════

    private void handleNameCorrection() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("PATIENT NAME CORRECTION - Dynamic Programming");
            ConsoleColors.printMenuItem("1", "Suggest Correct Name (Edit Distance)");
            ConsoleColors.printMenuItem("2", "Find Longest Common Subsequence (LCS)");
            ConsoleColors.printMenuItem("3", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    ConsoleColors.printSubHeader("Edit Distance - Name Correction (O(M x N))");
                    List<Patient> patients = patientService.getAllPatients();
                    if (patients.isEmpty()) {
                        ConsoleColors.printWarning("No patients registered. Add patients first.");
                        pressEnterToContinue();
                        break;
                    }
                    String input = readNonEmpty("Enter misspelled/partial name: ");
                    ConsoleColors.printLoading("Computing Edit Distances against " + patients.size() + " patient names");
                    System.out.println();

                    EditDistance ed = new EditDistance();
                    LCS lcs = new LCS();
                    String bestMatch = "";
                    int minDist = Integer.MAX_VALUE;

                    ConsoleColors.printTableHeader("Patient Name", "Edit Distance", "LCS Length");
                    for (Patient p : patients) {
                        int dist = ed.calculate(input, p.getName());
                        int lcsLen = lcs.calculate(input, p.getName());
                        ConsoleColors.printTableRow(p.getName(), String.valueOf(dist), String.valueOf(lcsLen));
                        if (dist < minDist) {
                            minDist = dist;
                            bestMatch = p.getName();
                        }
                    }
                    System.out.println();
                    ConsoleColors.printSuccess("Best match: \"" + bestMatch + "\" (Edit Distance: " + minDist + ")");
                    ConsoleColors.printInfo("Edit Distance = minimum insertions + deletions + substitutions");
                    pressEnterToContinue();
                    break;

                case "2":
                    ConsoleColors.printSubHeader("Longest Common Subsequence (O(M x N))");
                    String s1 = readNonEmpty("Enter first string: ");
                    String s2 = readNonEmpty("Enter second string: ");
                    ConsoleColors.printLoading("Computing LCS using DP table");
                    int lcsResult = new LCS().calculate(s1, s2);
                    System.out.println();
                    ConsoleColors.printSuccess("LCS Length between \"" + s1 + "\" and \"" + s2 + "\": " + lcsResult);
                    ConsoleColors.printInfo("LCS finds the longest subsequence common to both strings.");
                    pressEnterToContinue();
                    break;

                case "3": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  MODULE 9: RANGE QUERY ANALYTICS (Segment Tree & Fenwick Tree)
    // ═══════════════════════════════════════════════════════════════════════

    private void handleRangeQueryAnalytics() {
        boolean back = false;
        while (!back) {
            ConsoleColors.printHeader("RANGE QUERY ANALYTICS - Segment Tree & Fenwick Tree");
            ConsoleColors.printMenuItem("1", "View Daily Patient Admission Counts (30 Days)");
            ConsoleColors.printMenuItem("2", "Range Sum Query (Segment Tree)");
            ConsoleColors.printMenuItem("3", "Prefix / Range Sum (Fenwick Tree - BIT)");
            ConsoleColors.printMenuItem("4", "Update Daily Patient Count");
            ConsoleColors.printMenuItem("5", "Back to Main Menu");
            ConsoleColors.printDivider();

            String choice = readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    ConsoleColors.printSubHeader("Daily Patient Admission Counts (30 Days)");
                    System.out.println();
                    for (int i = 0; i < dailyCounts.length; i++) {
                        String bar = repeatChar('#', Math.min(dailyCounts[i], 50));
                        System.out.printf("    Day %2d: %3d  %s%s%s%n",
                            (i + 1), dailyCounts[i],
                            ConsoleColors.CYAN, bar, ConsoleColors.RESET);
                    }
                    System.out.println();
                    int total = 0;
                    for (int c : dailyCounts) total += c;
                    ConsoleColors.printInfo("Total days: " + dailyCounts.length + " | Total patients: " + total);
                    pressEnterToContinue();
                    break;

                case "2":
                    ConsoleColors.printSubHeader("Segment Tree - Range Sum Query - O(log N)");
                    int sl = readIntInRange("Start day (1-" + dailyCounts.length + "): ", 1, dailyCounts.length);
                    int sr = readIntInRange("End day (" + sl + "-" + dailyCounts.length + "): ", sl, dailyCounts.length);
                    ConsoleColors.printLoading("Querying Segment Tree");
                    long st1 = System.nanoTime();
                    int segResult = segmentTree.query(sl - 1, sr - 1);
                    long st2 = System.nanoTime();
                    System.out.println();
                    ConsoleColors.printSuccess("Total patients from Day " + sl + " to Day " + sr + ": " + segResult);
                    ConsoleColors.printInfo("Execution Time: " + (st2 - st1) + " ns");
                    ConsoleColors.printInfo("Time Complexity: O(log N) per query");
                    pressEnterToContinue();
                    break;

                case "3":
                    ConsoleColors.printSubHeader("Fenwick Tree (Binary Indexed Tree) - O(log N)");
                    ConsoleColors.printMenuItem("a", "Prefix Sum (Day 1 to Day X)");
                    ConsoleColors.printMenuItem("b", "Range Sum (Day X to Day Y)");
                    String subChoice = readLine("Enter choice (a/b): ");

                    if (subChoice.equalsIgnoreCase("a")) {
                        int day = readIntInRange("Enter day (1-" + dailyCounts.length + "): ", 1, dailyCounts.length);
                        ConsoleColors.printLoading("Computing prefix sum via Fenwick Tree");
                        long ft1 = System.nanoTime();
                        int prefSum = fenwickTree.prefixSum(day - 1);
                        long ft2 = System.nanoTime();
                        System.out.println();
                        ConsoleColors.printSuccess("Cumulative patients from Day 1 to Day " + day + ": " + prefSum);
                        ConsoleColors.printInfo("Execution Time: " + (ft2 - ft1) + " ns");
                    } else if (subChoice.equalsIgnoreCase("b")) {
                        int fl = readIntInRange("Start day (1-" + dailyCounts.length + "): ", 1, dailyCounts.length);
                        int fr = readIntInRange("End day (" + fl + "-" + dailyCounts.length + "): ", fl, dailyCounts.length);
                        ConsoleColors.printLoading("Computing range sum via Fenwick Tree");
                        long ft1 = System.nanoTime();
                        int fenResult = fenwickTree.rangeSum(fl - 1, fr - 1);
                        long ft2 = System.nanoTime();
                        System.out.println();
                        ConsoleColors.printSuccess("Total patients from Day " + fl + " to Day " + fr + ": " + fenResult);
                        ConsoleColors.printInfo("Execution Time: " + (ft2 - ft1) + " ns");
                    } else {
                        ConsoleColors.printError("Invalid choice. Please enter 'a' or 'b'.");
                    }
                    ConsoleColors.printInfo("Time Complexity: O(log N) per query");
                    pressEnterToContinue();
                    break;

                case "4":
                    ConsoleColors.printSubHeader("Update Daily Patient Count");
                    int uday = readIntInRange("Enter day to update (1-" + dailyCounts.length + "): ", 1, dailyCounts.length);
                    ConsoleColors.printInfo("Current count for Day " + uday + ": " + dailyCounts[uday - 1]);
                    int newCount = readPositiveInt("Enter new count: ");
                    int delta = newCount - dailyCounts[uday - 1];
                    dailyCounts[uday - 1] = newCount;
                    segmentTree.update(uday - 1, newCount);
                    fenwickTree.update(uday - 1, delta);
                    ConsoleColors.printSuccess("Day " + uday + " updated to " + newCount + " patients.");
                    ConsoleColors.printInfo("Both Segment Tree and Fenwick Tree updated in O(log N).");
                    pressEnterToContinue();
                    break;

                case "5": back = true; break;
                default: ConsoleColors.printError("Invalid choice."); break;
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  VALIDATED INPUT UTILITY METHODS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Reads an integer from user input with retry on invalid format.
     */
    private int readInt(String prompt) {
        while (true) {
            ConsoleColors.printPrompt(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                ConsoleColors.printError("Invalid number. Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a positive integer (> 0) with retry.
     */
    private int readPositiveInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val > 0) return val;
            ConsoleColors.printError("Value must be a positive integer (> 0).");
        }
    }

    /**
     * Reads an integer within [min, max] range with retry.
     */
    private int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val >= min && val <= max) return val;
            ConsoleColors.printError("Value must be between " + min + " and " + max + ".");
        }
    }

    /**
     * Reads gender input, only accepts M or F (case-insensitive), with retry.
     */
    private String readGender(String prompt) {
        while (true) {
            String input = readLine(prompt).toUpperCase();
            if (input.equals("M") || input.equals("F")) return input;
            ConsoleColors.printError("Invalid gender. Please enter only 'M' (Male) or 'F' (Female).");
        }
    }

    /**
     * Reads a non-empty string with retry.
     */
    private String readNonEmpty(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (!input.isEmpty()) return input;
            ConsoleColors.printError("This field cannot be empty. Please enter a value.");
        }
    }

    /**
     * Reads a valid time input in HH:mm format (00:00-23:59) with retry,
     * and returns it as an integer (e.g., 09:00 -> 900).
     */
    private int readTimeInput(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (input.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                String[] parts = input.split(":");
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                return hours * 100 + minutes;
            }
            ConsoleColors.printError("Invalid time. Use HH:mm format (e.g., 09:00, 14:30). Hours: 00-23, Minutes: 00-59.");
        }
    }

    /**
     * Reads a line of text from user input.
     */
    private String readLine(String prompt) {
        ConsoleColors.printPrompt(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Pauses execution until user presses Enter.
     */
    private void pressEnterToContinue() {
        System.out.println();
        ConsoleColors.printPrompt("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Repeats a character n times and returns the string.
     */
    private String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public void update(String message) {
        System.out.println();
        ConsoleColors.printWarning("🔔 OBSERVER ALERT: " + message);
        System.out.println();
    }
}
