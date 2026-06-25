package healthnet.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Kruskal's Algorithm for Minimum Spanning Tree (MST). Time Complexity: O(E log E)
 */
public class KruskalMST {

    class Subset {
        int parent, rank;
    }

    public void findMST(Graph graph) {
        int V = graph.getVertices();
        List<Graph.Edge> allEdges = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            for (Graph.Edge edge : graph.getAdjList().get(i)) {
                // To avoid duplicate edges in undirected graph
                if (edge.source < edge.destination) {
                    allEdges.add(edge);
                }
            }
        }

        long startTime = System.nanoTime();

        Collections.sort(allEdges, Comparator.comparingInt(e -> e.weight));

        Subset[] subsets = new Subset[V];
        for (int i = 0; i < V; i++) {
            subsets[i] = new Subset();
            subsets[i].parent = i;
            subsets[i].rank = 0;
        }

        List<Graph.Edge> result = new ArrayList<>();
        int e = 0;
        int i = 0;

        while (e < V - 1 && i < allEdges.size()) {
            Graph.Edge nextEdge = allEdges.get(i++);
            int x = find(subsets, nextEdge.source);
            int y = find(subsets, nextEdge.destination);

            if (x != y) {
                result.add(nextEdge);
                union(subsets, x, y);
                e++;
            }
        }

        long endTime = System.nanoTime();

        System.out.println("\n--- Kruskal's MST ---");
        int totalCost = 0;
        for (Graph.Edge edge : result) {
            System.out.println(graph.getNodeName(edge.source) + " - " + graph.getNodeName(edge.destination) + " \tWeight: " + edge.weight);
            totalCost += edge.weight;
        }
        System.out.println("Total MST Cost: " + totalCost);
        System.out.println("Execution Time (ns): " + (endTime - startTime));
    }

    private int find(Subset[] subsets, int i) {
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }
        return subsets[i].parent;
    }

    private void union(Subset[] subsets, int x, int y) {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        if (subsets[xroot].rank < subsets[yroot].rank) {
            subsets[xroot].parent = yroot;
        } else if (subsets[xroot].rank > subsets[yroot].rank) {
            subsets[yroot].parent = xroot;
        } else {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }
}
