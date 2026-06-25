package healthnet.graphs;

import java.util.*;

/**
 * Dijkstra's Algorithm for Shortest Path. Time Complexity: O((V + E) log V)
 */
public class Dijkstra {

    public void findFastestRoute(Graph graph, int source) {
        int V = graph.getVertices();
        int[] dist = new int[V];
        boolean[] sptSet = new boolean[V];
        int[] parent = new int[V];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        pq.add(new Graph.Edge(-1, source, 0));

        long startTime = System.nanoTime();

        while (!pq.isEmpty()) {
            int u = pq.poll().destination;

            if (sptSet[u]) continue;
            sptSet[u] = true;

            for (Graph.Edge edge : graph.getAdjList().get(u)) {
                int v = edge.destination;
                int weight = edge.weight;

                if (!sptSet[v] && dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    pq.add(new Graph.Edge(u, v, dist[v]));
                }
            }
        }

        long endTime = System.nanoTime();

        System.out.println("\n--- Dijkstra's Algorithm Results ---");
        System.out.println("Source: " + graph.getNodeName(source));
        for (int i = 0; i < V; i++) {
            if (i != source) {
                System.out.print("To " + graph.getNodeName(i) + " - Distance: " + (dist[i] == Integer.MAX_VALUE ? "INF" : dist[i]) + " | Path: ");
                printPath(parent, i, graph);
                System.out.println();
            }
        }
        System.out.println("Execution Time (ns): " + (endTime - startTime));
    }

    private void printPath(int[] parent, int j, Graph graph) {
        if (parent[j] == -1) {
            System.out.print(graph.getNodeName(j));
            return;
        }
        printPath(parent, parent[j], graph);
        System.out.print(" -> " + graph.getNodeName(j));
    }
}
