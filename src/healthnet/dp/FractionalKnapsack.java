package healthnet.dp;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Fractional Knapsack Implementation for continuous hospital resources (e.g. Oxygen units)
 * Greedy Approach. Time Complexity: O(N log N)
 */
public class FractionalKnapsack {

    public static class PatientRequest {
        public String patientName;
        public int severityScore; // acts as value
        public int requiredUnits; // acts as weight

        public PatientRequest(String patientName, int severityScore, int requiredUnits) {
            this.patientName = patientName;
            this.severityScore = severityScore;
            this.requiredUnits = requiredUnits;
        }
    }

    public double allocate(int totalUnits, PatientRequest[] requests) {
        // Sort by value/weight ratio
        Arrays.sort(requests, new Comparator<PatientRequest>() {
            @Override
            public int compare(PatientRequest o1, PatientRequest o2) {
                double r1 = (double) o1.severityScore / o1.requiredUnits;
                double r2 = (double) o2.severityScore / o2.requiredUnits;
                return Double.compare(r2, r1); // descending order
            }
        });

        double totalBenefit = 0.0;
        int currentCapacity = totalUnits;

        System.out.println("Fractional Allocation Report:");
        for (PatientRequest req : requests) {
            if (currentCapacity == 0) break;

            if (req.requiredUnits <= currentCapacity) {
                currentCapacity -= req.requiredUnits;
                totalBenefit += req.severityScore;
                System.out.println(req.patientName + " allocated " + req.requiredUnits + " units (Full)");
            } else {
                double fraction = (double) currentCapacity / req.requiredUnits;
                totalBenefit += req.severityScore * fraction;
                System.out.println(req.patientName + " allocated " + currentCapacity + " units (Partial - " + String.format("%.2f", fraction * 100) + "%)");
                currentCapacity = 0;
            }
        }

        return totalBenefit;
    }
}
