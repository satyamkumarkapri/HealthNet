package healthnet.graphs;

/**
 * Depth First Search (DFS) for Graph Traversal. Time Complexity: O(V + E)
 */
public class DFS {

    public void traverse(Graph graph, int startVertex) {
        int V = graph.getVertices();
        boolean[] visited = new boolean[V];

        System.out.println("\n--- DFS Traversal ---");
        long startTime = System.nanoTime();

        dfsUtil(graph, startVertex, visited);

        long endTime = System.nanoTime();
        System.out.println("\nExecution Time (ns): " + (endTime - startTime));
    }

    private void dfsUtil(Graph graph, int v, boolean[] visited) {
        visited[v] = true;
        System.out.print(graph.getNodeName(v) + " ");

        for (Graph.Edge edge : graph.getAdjList().get(v)) {
            int dest = edge.destination;
            if (!visited[dest]) {
                dfsUtil(graph, dest, visited);
            }
        }
    }

    public boolean hasCycle(Graph graph) {
        int V = graph.getVertices();
        boolean[] visited = new boolean[V];
        boolean[] recStack = new boolean[V];

        for (int i = 0; i < V; i++) {
            if (isCyclicUtil(graph, i, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCyclicUtil(Graph graph, int i, boolean[] visited, boolean[] recStack) {
        if (recStack[i]) return true;
        if (visited[i]) return false;

        visited[i] = true;
        recStack[i] = true;

        for (Graph.Edge edge : graph.getAdjList().get(i)) {
            if (isCyclicUtil(graph, edge.destination, visited, recStack)) {
                return true;
            }
        }

        recStack[i] = false;
        return false;
    }
}
