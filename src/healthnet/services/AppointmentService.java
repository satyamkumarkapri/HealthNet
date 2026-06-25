package healthnet.services;

import healthnet.model.Appointment;
import healthnet.model.Doctor;
import healthnet.scheduling.ActivitySelection;
import healthnet.trees.BST;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AppointmentService {
    private static AppointmentService instance;
    private List<Appointment> requestedAppointments;
    private ActivitySelection activitySelection;
    
    private BST<Appointment> appointmentBST;
    private TreeMap<LocalDate, List<Appointment>> calendar;

    private AppointmentService() {
        this.requestedAppointments = new ArrayList<>();
        this.activitySelection = new ActivitySelection();
        this.appointmentBST = new BST<>();
        this.calendar = new TreeMap<>();
    }

    public static synchronized AppointmentService getInstance() {
        if (instance == null) {
            instance = new AppointmentService();
        }
        return instance;
    }

    public void addAppointment(String patientName, Doctor doctor, int startTime, int endTime) {
        Appointment a = new Appointment(patientName, doctor, startTime, endTime);
        bookAppointment(a);
    }
    
    public boolean bookAppointment(Appointment a) {
        requestedAppointments.add(a);
        appointmentBST.insert(a);
        
        LocalDate date = a.getDate();
        if(date == null) date = LocalDate.now();
        
        calendar.putIfAbsent(date, new ArrayList<>());
        calendar.get(date).add(a);
        return true;
    }
    
    public boolean cancelAppointment(String id) {
        Appointment toCancel = null;
        for (Appointment a : requestedAppointments) {
            if (id.equals(a.getAppointmentId())) {
                toCancel = a;
                break;
            }
        }
        if (toCancel != null) {
            toCancel.cancel();
            appointmentBST.delete(toCancel);
            if(toCancel.getDate() != null && calendar.containsKey(toCancel.getDate())) {
                calendar.get(toCancel.getDate()).remove(toCancel);
            }
            requestedAppointments.remove(toCancel);
            return true;
        }
        return false;
    }
    
    public List<Appointment> getByDate(LocalDate date) {
        return calendar.getOrDefault(date, new ArrayList<>());
    }
    
    public boolean checkConflict(String did, LocalDate date, String time) {
        int docId;
        try {
            docId = Integer.parseInt(did);
        } catch(NumberFormatException e) {
            return false;
        }
        
        List<Appointment> dayAppts = getByDate(date);
        for(Appointment a : dayAppts) {
            if(a.getDoctor() != null && a.getDoctor().getDoctorId() == docId && time.equals(a.getTime())) {
                return true; // conflict exists
            }
        }
        return false;
    }
    
    public List<Appointment> getUpcomingAppointments(String pid) {
        // pid is patientName for now or parse as int for actual Patient object
        return requestedAppointments.stream()
                .filter(a -> pid.equals(a.getPatientName()) || (a.getPatient() != null && pid.equals(String.valueOf(a.getPatient().getPatientId()))))
                .collect(Collectors.toList());
    }

    public List<Appointment> getRequestedAppointments() {
        return new ArrayList<>(requestedAppointments);
    }

    public void generateOptimalScheduleForDoctor(Doctor doctor) {
        System.out.println("Generating Optimal Schedule for " + doctor.getDoctorName() + " using Greedy Approach...");
        long start = System.nanoTime();
        
        List<Appointment> doctorRequests = requestedAppointments.stream()
                .filter(a -> a.getDoctor() != null && a.getDoctor().getDoctorId() == doctor.getDoctorId())
                .collect(Collectors.toList());

        List<Appointment> selected = activitySelection.selectActivities(doctorRequests);
        
        long end = System.nanoTime();

        System.out.println("Maximum Patients Served: " + selected.size());
        System.out.println("Scheduled Appointments:");
        for (Appointment appt : selected) {
            System.out.println(appt);
        }
        
        System.out.println("Time Complexity: O(N log N) - Execution Time (ns): " + (end - start));
    }
}
