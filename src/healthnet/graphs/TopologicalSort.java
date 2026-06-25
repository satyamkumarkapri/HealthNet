package healthnet.graphs;

import java.util.*;

/**
 * Topological Sort using Kahn's Algorithm (BFS-based).
 * Used for scheduling treatment procedures with dependencies.
 *
 * Given a directed acyclic graph (DAG) of procedures and their
 * prerequisite dependencies, this algorithm produces a valid
 * linear ordering such that every procedure comes after its prerequisites.
 *
 * Time Complexity: O(V + E)
 * Space Complexity: O(V + E)
 */
public class TopologicalSort {

    /**
     * Performs topological sort on a named-node DAG.
     *
     * @param adjList adjacency list where each key maps to its dependents.
     *                e.g., "Blood Test" -> ["Diagnosis"] means Diagnosis depends on Blood Test.
     * @return a valid topological ordering, or an empty list if a cycle is detected
     */
    public List<String> sort(Map<String, List<String>> adjList) {
        // Step 1: Compute in-degree for each node
        Map<String, Integer> inDegree = new LinkedHashMap<>();

        // Initialize all nodes from keys
        for (String node : adjList.keySet()) {
            inDegree.putIfAbsent(node, 0);
        }

        // Count incoming edges
        for (Map.Entry<String, List<String>> entry : adjList.entrySet()) {
            for (String neighbor : entry.getValue()) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Step 2: Initialize queue with nodes having in-degree 0
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Step 3: BFS — process nodes and reduce in-degrees
        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String node = queue.poll();
            result.add(node);

            if (adjList.containsKey(node)) {
                for (String neighbor : adjList.get(node)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Step 4: Check for cycles
        if (result.size() != inDegree.size()) {
            System.out.println("  Cycle detected! Cannot determine a valid procedure order.");
            return Collections.emptyList();
        }

        return result;
    }
}
