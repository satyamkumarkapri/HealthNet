package healthnet.model;

public class Resource implements Comparable<Resource> {
    private String resourceId;
    private String type; // e.g., Bed, Ventilator, Oxygen
    private String description;
    private int totalCount;
    private int availableCount;
    private String location; // e.g., Ward A, ICU, Storage Room

    public Resource(String resourceId, String type, String description, int totalCount, String location) {
        this.resourceId = resourceId;
        this.type = type;
        this.description = description;
        this.totalCount = totalCount;
        this.availableCount = totalCount; // Initially all are available
        this.location = location;
    }

    public boolean allocate(int count) {
        if (availableCount >= count) {
            availableCount -= count;
            return true;
        }
        return false;
    }

    public void release(int count) {
        availableCount += count;
        if (availableCount > totalCount) {
            availableCount = totalCount;
        }
    }

    public int getAvailability() {
        return availableCount;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("Resource[%s] %s (%s) - Available: %d/%d at %s", 
                resourceId, type, description, availableCount, totalCount, location);
    }

    @Override
    public int compareTo(Resource other) {
        // For MaxHeap: Compare based on availability. 
        // A resource with higher availability has higher priority (or vice versa, depending on use case).
        // Let's sort by availableCount ascending so that MaxHeap returns the one with MOST availability.
        return Integer.compare(this.availableCount, other.availableCount);
    }
}
