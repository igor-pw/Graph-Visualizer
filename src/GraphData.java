import java.util.ArrayList;

/**
 * Class to hold both the nodes and the adjacency list for a graph
 */
public class GraphData {
    private final ArrayList<Node> nodes;
    private final ArrayList<ArrayList<Integer>> adjacencyList;

    public GraphData(ArrayList<Node> nodes, ArrayList<ArrayList<Integer>> adjacencyList) {
        this.nodes = nodes;
        this.adjacencyList = adjacencyList;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<ArrayList<Integer>> getAdjacencyList() {
        return adjacencyList;
    }
}