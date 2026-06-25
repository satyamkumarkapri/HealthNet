package healthnet;

import healthnet.services.AppointmentService;
import healthnet.services.DoctorService;
import healthnet.services.MedicalRecordService;
import healthnet.services.PatientService;
import healthnet.services.ResourceService;
import healthnet.utils.ConsoleColors;
import healthnet.utils.ConsoleUI;

/**
 * Main entry point for the HEALTHNET application.
 * Displays the startup banner, loads sample data, and launches the CLI.
 */
public class Main {

    public static void main(String[] args) {
        // Launch the main interactive console
        // Initialize services first to get the accurate stats if needed, 
        // ConsoleUI constructor calls SampleDataLoader.
        ConsoleUI ui = new ConsoleUI();

        // Display the ASCII art startup banner
        ConsoleColors.printBanner();

        // Initialization sequence
        ConsoleColors.printLoading("Initializing HealthNet system");
        ConsoleColors.printLoading("Loading sample healthcare data");
        ConsoleColors.printLoading("Building BST, AVL, B-Tree, B+ Tree indexes");
        ConsoleColors.printLoading("Configuring hospital network graph");
        System.out.println();

        // Print system stats
        ConsoleColors.printSuccess("System initialized successfully!");
        System.out.println();
        
        int pCount = PatientService.getInstance().getAllPatients().size();
        int dCount = DoctorService.getInstance().getAllDoctors().size();
        int aCount = AppointmentService.getInstance().getRequestedAppointments().size();
        int rCount = MedicalRecordService.getInstance().getAllRecords().size();
        int resCount = ResourceService.getInstance().getAllResources().size();
        
        ConsoleColors.printInfo(pCount + " patients loaded  |  " + dCount + " doctors configured");
        ConsoleColors.printInfo(aCount + " appointments     |  " + rCount + " medical records");
        ConsoleColors.printInfo(resCount + " hospital resources|  6 departments");
        ConsoleColors.printInfo("30 days of analytics data (Segment Tree & BIT)");
        System.out.println();
        ConsoleColors.printDoubleDivider();

        ui.start();
    }
}
