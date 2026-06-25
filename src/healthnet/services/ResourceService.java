package healthnet.services;

import healthnet.dp.FractionalKnapsack;
import healthnet.dp.Knapsack01;
import healthnet.graphs.Graph;
import healthnet.model.Resource;
import healthnet.patterns.observer.Observer;
import healthnet.patterns.observer.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class ResourceService implements Subject {

    private static ResourceService instance;

    // Data structures as per UML
    private PriorityQueue<Resource> resourceHeap; // MaxHeap based on availability
    private HashMap<String, Resource> resourceMap;
    private Graph allocationGraph; // Uses Graph<String> internally or directly healthnet.graphs.Graph
    private List<Observer> observers;

    // Singleton Pattern
    private ResourceService() {
        resourceHeap = new PriorityQueue<>((r1, r2) -> Integer.compare(r2.getAvailability(), r1.getAvailability()));
        resourceMap = new HashMap<>();
        allocationGraph = new Graph(100);
        observers = new ArrayList<>();
    }

    public static synchronized ResourceService getInstance() {
        if (instance == null) {
            instance = new ResourceService();
        }
        return instance;
    }

    public void addResource(Resource r) {
        resourceMap.put(r.getResourceId(), r);
        resourceHeap.add(r);
        // We can add location to graph if we build mapping logic
    }

    public boolean allocateResource(String type, int count) {
        // Find best resource by type
        Resource best = null;
        for (Resource r : resourceMap.values()) {
            if (r.getType().equalsIgnoreCase(type) && r.getAvailability() >= count) {
                if (best == null || r.getAvailability() > best.getAvailability()) {
                    best = r;
                }
            }
        }
        
        if (best != null) {
            resourceHeap.remove(best);
            boolean success = best.allocate(count);
            resourceHeap.add(best);

            if (best.getAvailability() < (best.getTotalCount() * 0.2)) {
                notifyObservers("ALERT: Low availability for " + best.getType() + " at " + best.getLocation() + " (Only " + best.getAvailability() + " left)");
            }
            return success;
        }
        return false;
    }

    public void releaseResource(String type, int count) {
        // Release from first available matching resource for simplicity
        for (Resource r : resourceMap.values()) {
            if (r.getType().equalsIgnoreCase(type)) {
                resourceHeap.remove(r);
                r.release(count);
                resourceHeap.add(r);
                break; // Just released into the first one
            }
        }
    }

    public int getAvailableResources(String type) {
        int total = 0;
        for (Resource r : resourceMap.values()) {
            if (r.getType().equalsIgnoreCase(type)) {
                total += r.getAvailability();
            }
        }
        return total;
    }

    public Resource emergencyAllocate(String type) {
        // Force allocation even if it dips low
        for (Resource r : resourceMap.values()) {
            if (r.getType().equalsIgnoreCase(type) && r.getAvailability() > 0) {
                resourceHeap.remove(r);
                r.allocate(1);
                resourceHeap.add(r);
                notifyObservers("EMERGENCY ALLOCATION: " + type + " allocated from " + r.getLocation() + ". Remaining: " + r.getAvailability());
                return r;
            }
        }
        notifyObservers("CRITICAL: Failed emergency allocation for " + type + ". No availability.");
        return null;
    }

    public List<Resource> getAllResources() {
        return new ArrayList<>(resourceMap.values());
    }

    public void planEquipmentBudget(int budget, Knapsack01.Item[] items) {
        System.out.println("\n--- Equipment Budget Planning (0/1 Knapsack) ---");
        Knapsack01 knapsack = new Knapsack01();
        long start = System.nanoTime();
        int maxBenefit = knapsack.allocate(budget, items);
        long end = System.nanoTime();
        
        System.out.println("Maximum Total Benefit: " + maxBenefit);
        System.out.println("Time Complexity: O(N*W) - Execution Time (ns): " + (end - start));
    }

    public void allocateOxygen(int totalOxygen, FractionalKnapsack.PatientRequest[] requests) {
        System.out.println("\n--- Oxygen Allocation (Fractional Knapsack) ---");
        FractionalKnapsack fk = new FractionalKnapsack();
        long start = System.nanoTime();
        double maxBenefit = fk.allocate(totalOxygen, requests);
        long end = System.nanoTime();

        System.out.println("Total Severity Addressed (Benefit): " + String.format("%.2f", maxBenefit));
        System.out.println("Time Complexity: O(N log N) - Execution Time (ns): " + (end - start));
    }

    // Subject Implementation
    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }
}
