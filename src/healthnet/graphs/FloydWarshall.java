package healthnet.graphs;

import java.util.Arrays;

/**
 * Floyd-Warshall Algorithm for All-Pairs Shortest Path. Time Complexity: O(V^3)
 */
public class FloydWarshall {
    final static int INF = 99999;

    public void calculateDistances(Graph graph) {
        int V = graph.getVertices();
        int[][] dist = new int[V][V];

        // Initialize distances
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = INF;
                }
            }
        }

        for (int u = 0; u < V; u++) {
            for (Graph.Edge edge : graph.getAdjList().get(u)) {
                dist[u][edge.destination] = edge.weight;
            }
        }

        long startTime = System.nanoTime();

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        long endTime = System.nanoTime();

        System.out.println("\n--- Floyd-Warshall Algorithm Results ---");
        System.out.println("All-Pairs Shortest Path Matrix:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (dist[i][j] == INF)
                    System.out.print("INF ");
                else
                    System.out.print(dist[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println("Execution Time (ns): " + (endTime - startTime));
    }
}
