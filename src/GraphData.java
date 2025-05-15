import java.util.ArrayList;

public class GraphData
{
    private final ArrayList<Node> nodes;
    private final ArrayList<ArrayList<Integer>> adjacency_matrix;
    private final int groups;

    public GraphData(ArrayList<Node> nodes, ArrayList<ArrayList<Integer>> adjacency_matrix, int groups)
    {
        this.nodes = nodes;
        this.adjacency_matrix = adjacency_matrix;
        this.groups = groups;
    }

    public int getGroups() { return this.groups; }

    public void printNodes()
    {
        for(Node node : nodes)
            node.printNode();
    }

    public void printAdjacencyMatrix()
    {
        for(ArrayList<Integer> row : adjacency_matrix)
            System.out.println(row);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<ArrayList<Integer>> getAdjacencyMatrix() {
        return adjacency_matrix;
    }

    public ArrayList<Integer> createAdjacencyList(int node_nr)
    {
        ArrayList<Integer> adjacency_list = new ArrayList<>();
        for(int i = 0; i < adjacency_matrix.size(); i++)
        {
            if(adjacency_matrix.get(node_nr).get(i) == 1 && i != node_nr) {
                adjacency_list.add(i);
            }
        }

        adjacency_list.trimToSize();
        return adjacency_list;
    }

    public void assignGroups(Node node, int group_nr)
    {
        if (node.getGroup() == -1 && !node.getAdjacencyList().isEmpty())
        {
            System.out.println("Zmieniono grupe");
            node.setGroup(group_nr);
            for (int i = 0; i < node.getAdjacencyList().size(); i++) {
                assignGroups(nodes.get(node.getAdjacencyList().get(i)) ,group_nr);
            }

        }
    }

}
