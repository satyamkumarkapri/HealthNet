package healthnet.graphs;

import java.util.*;

/**
 * Common Graph Representation for Hospitals and Departments
 */
public class Graph {
    
    public static class Edge {
        public int source;
        public int destination;
        public int weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    private int vertices;
    private List<List<Edge>> adjList;
    private Map<Integer, String> nodeNames;

    public Graph(int vertices) {
        this.vertices = vertices;
        adjList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
        nodeNames = new HashMap<>();
    }

    public void setNodeName(int id, String name) {
        nodeNames.put(id, name);
    }

    public String getNodeName(int id) {
        return nodeNames.getOrDefault(id, "Node " + id);
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Edge(source, destination, weight));
        // Note: Can make it undirected if required, by adding:
        // adjList.get(destination).add(new Edge(destination, source, weight));
    }
    
    public void addUndirectedEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Edge(source, destination, weight));
        adjList.get(destination).add(new Edge(destination, source, weight));
    }

    public int getVertices() {
        return vertices;
    }

    public List<List<Edge>> getAdjList() {
        return adjList;
    }

    public void displayGraph() {
        for (int i = 0; i < vertices; i++) {
            System.out.print(getNodeName(i) + " is connected to: ");
            for (Edge edge : adjList.get(i)) {
                System.out.print(getNodeName(edge.destination) + "(dist: " + edge.weight + ") ");
            }
            System.out.println();
        }
    }

    /**
     * Displays a visual ASCII art map of the graph with boxed nodes and arrow connections.
     */
    public void displayVisualMap() {
        String C  = "\033[36m";   // Cyan
        String G  = "\033[32m";   // Green
        String Y  = "\033[33m";   // Yellow
        String W  = "\033[97m";   // Bright White
        String D  = "\033[2m";    // Dim
        String B  = "\033[1m";    // Bold
        String R  = "\033[0m";    // Reset
        String M  = "\033[35m";   // Magenta

        System.out.println();
        System.out.println(B + C + "    +-----------------------------------------------------------+" + R);
        System.out.println(B + C + "    |" + W + "               HOSPITAL ROOM NETWORK MAP                  " + C + "|" + R);
        System.out.println(B + C + "    +-----------------------------------------------------------+" + R);
        System.out.println();

        // Print each node as a box
        for (int i = 0; i < vertices; i++) {
            String name = getNodeName(i);
            int boxWidth = Math.max(name.length() + 4, 20);
            String topBot = repeat2('-', boxWidth);

            System.out.println(G + "    +" + topBot + "+" + R);
            int pad = boxWidth - name.length() - 4;
            int lPad = pad / 2;
            int rPad = pad - lPad;
            System.out.println(G + "    | " + B + W + " [" + i + "] "
                    + repeat2(' ', lPad) + name + repeat2(' ', rPad) + " " + G + "|" + R);
            System.out.println(G + "    +" + topBot + "+" + R);

            // Print connections
            List<Edge> edges = adjList.get(i);
            if (!edges.isEmpty()) {
                for (int j = 0; j < edges.size(); j++) {
                    Edge e = edges.get(j);
                    String destName = getNodeName(e.destination);
                    String arrow;
                    if (j == edges.size() - 1) {
                        arrow = "    " + Y + "    \\----> " + W + destName
                                + D + "  (distance: " + M + e.weight + D + " units)" + R;
                    } else {
                        arrow = "    " + Y + "    |----> " + W + destName
                                + D + "  (distance: " + M + e.weight + D + " units)" + R;
                    }
                    System.out.println(arrow);
                }
            } else {
                System.out.println("    " + D + "    (no outgoing connections)" + R);
            }
            System.out.println();
        }

        // Legend
        System.out.println(D + "    " + repeat2('-', 55) + R);
        System.out.println(D + "    LEGEND:  " + G + "[Box]" + D + " = Room/Location    "
                + Y + "--->" + D + " = Path    " + M + "N" + D + " = Distance" + R);
        System.out.println(D + "    " + repeat2('-', 55) + R);
    }

    private static String repeat2(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
