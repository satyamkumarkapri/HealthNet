package healthnet.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Breadth First Search (BFS) for Graph Traversal. Time Complexity: O(V + E)
 */
public class BFS {

    public void traverse(Graph graph, int startVertex) {
        int V = graph.getVertices();
        boolean[] visited = new boolean[V];

        Queue<Integer> queue = new LinkedList<>();
        visited[startVertex] = true;
        queue.add(startVertex);

        System.out.println("\n--- BFS Traversal ---");
        long startTime = System.nanoTime();

        while (!queue.isEmpty()) {
            int u = queue.poll();
            System.out.print(graph.getNodeName(u) + " ");

            for (Graph.Edge edge : graph.getAdjList().get(u)) {
                int v = edge.destination;
                if (!visited[v]) {
                    visited[v] = true;
                    queue.add(v);
                }
            }
        }

        long endTime = System.nanoTime();
        System.out.println("\nExecution Time (ns): " + (endTime - startTime));
    }
}
