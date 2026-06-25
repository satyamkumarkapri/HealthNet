package healthnet.scheduling;

import healthnet.model.Appointment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Activity Selection algorithm to maximize the number of non-overlapping appointments.
 * Time Complexity: O(N log N) due to sorting.
 */
public class ActivitySelection {

    public List<Appointment> selectActivities(List<Appointment> appointments) {
        List<Appointment> selected = new ArrayList<>();
        if (appointments == null || appointments.isEmpty()) return selected;

        // 1. Sort appointments by end time (Greedy Choice Property)
        Collections.sort(appointments);

        // 2. Select the first appointment
        selected.add(appointments.get(0));
        int lastEndTime = appointments.get(0).getEndTime();

        // 3. Iterate over the remaining appointments
        for (int i = 1; i < appointments.size(); i++) {
            if (appointments.get(i).getStartTime() >= lastEndTime) {
                selected.add(appointments.get(i));
                lastEndTime = appointments.get(i).getEndTime();
            }
        }

        return selected;
    }
}
