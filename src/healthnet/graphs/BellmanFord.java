package healthnet.graphs;

import java.util.Arrays;

/**
 * Bellman-Ford Algorithm for Shortest Path. Time Complexity: O(V * E)
 */
public class BellmanFord {

    public void findFastestRoute(Graph graph, int source) {
        int V = graph.getVertices();
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        long startTime = System.nanoTime();

        for (int i = 1; i < V; i++) {
            for (int u = 0; u < V; u++) {
                for (Graph.Edge edge : graph.getAdjList().get(u)) {
                    int v = edge.destination;
                    int weight = edge.weight;
                    if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                    }
                }
            }
        }

        // Check for negative-weight cycles
        for (int u = 0; u < V; u++) {
            for (Graph.Edge edge : graph.getAdjList().get(u)) {
                int v = edge.destination;
                int weight = edge.weight;
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    System.out.println("Graph contains negative weight cycle");
                    return;
                }
            }
        }

        long endTime = System.nanoTime();

        System.out.println("\n--- Bellman-Ford Algorithm Results ---");
        System.out.println("Source: " + graph.getNodeName(source));
        for (int i = 0; i < V; i++) {
            if (i != source) {
                System.out.println("To " + graph.getNodeName(i) + " - Distance: " + (dist[i] == Integer.MAX_VALUE ? "INF" : dist[i]));
            }
        }
        System.out.println("Execution Time (ns): " + (endTime - startTime));
    }
}
