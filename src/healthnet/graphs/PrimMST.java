package healthnet.graphs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Prim's Algorithm for Minimum Spanning Tree (MST). Time Complexity: O((V + E) log V)
 */
public class PrimMST {

    public void findMST(Graph graph) {
        int V = graph.getVertices();
        int[] key = new int[V];
        int[] parent = new int[V];
        boolean[] inMST = new boolean[V];

        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        key[0] = 0;
        pq.add(new Graph.Edge(-1, 0, 0));

        long startTime = System.nanoTime();

        while (!pq.isEmpty()) {
            int u = pq.poll().destination;

            inMST[u] = true;

            for (Graph.Edge edge : graph.getAdjList().get(u)) {
                int v = edge.destination;
                int weight = edge.weight;

                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    pq.add(new Graph.Edge(u, v, key[v]));
                    parent[v] = u;
                }
            }
        }

        long endTime = System.nanoTime();

        System.out.println("\n--- Prim's MST ---");
        int totalCost = 0;
        for (int i = 1; i < V; i++) {
            if (parent[i] != -1) {
                System.out.println(graph.getNodeName(parent[i]) + " - " + graph.getNodeName(i) + " \tWeight: " + key[i]);
                totalCost += key[i];
            }
        }
        System.out.println("Total MST Cost: " + totalCost);
        System.out.println("Execution Time (ns): " + (endTime - startTime));
    }
}
