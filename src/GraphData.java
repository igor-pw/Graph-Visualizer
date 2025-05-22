import java.util.ArrayList;

public class GraphData
{
    private final ArrayList<Node> nodes;
    private final ArrayList<ArrayList<Integer>> adjacency_matrix;
    private final int groups;
    private int divide;
    private double margin;

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

    public double getNodeEigenvalue(int n){
        return nodes.get(n).getEigenvalue();
    }

    public void printAdjacencyMatrix()
    {
        for(ArrayList<Integer> row : adjacency_matrix)
            System.out.println(row);
    }

    public void setEigenvalues(Vector eigenvalues)
    {
        for(int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setEigenvalue(eigenvalues.get(i));
            
        }
    }

    public void setParameters( int divide, double margin)
    {
        this.margin = margin;
        this.divide = divide;
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

    public void printAdjacencyList(){
        for(Node node : nodes) {
            System.out.printf("Node " + node.getNr() + ":   ");
            node.printAdjacencyList();
        }
    }

    public void inintGroups(Node node, int group_nr)
    {
        if (node.getGroup() == -1 && !node.getAdjacencyList().isEmpty())
        {
            System.out.println("Zmieniono grupe");
            node.setGroup(group_nr);
            for (int i = 0; i < node.getAdjacencyList().size(); i++) {
                inintGroups(nodes.get(node.getAdjacencyList().get(i)) ,group_nr);
            }

        }
    }

    public void assignGroups(SpectralData spectral_data) {
        double div = (double)nodes.size() / divide;
        System.out.println("div: " + div);
        int[] seeds = new int[divide]; // Zmiana na int zamiast double
        int tmp = 0;

        // Dla pierwszych (divide-1) grup
        for(int i = 1; i < divide; i++) {
            int ind_k = (int)Math.round(i*div);
            int ind_p = (int)Math.round(((i-1)*div)+1);
            tmp = ind_k;

            int max_len = -1;
            int node = -1;

            // Szukaj węzła z największą liczbą połączeń w zakresie grupy
            for(int j = ind_p; j <= ind_k; j++) {
                if(j < nodes.size() && nodes.get(j).getConnectedNodes() > max_len) {
                    max_len = nodes.get(j).getConnectedNodes();
                    node = (int)Math.floor(spectral_data.getNodeIndices().get(j));
                }
            }

            seeds[i-1] = node;
            System.out.println("Node: " + node + " len: " + max_len);
        }

        int max_len = -1;
        int node = -1;

        for(int i = tmp + 1; i < nodes.size(); i++) {
            if(i < nodes.size() && nodes.get(i).getConnectedNodes() > max_len) {
                max_len = nodes.get(i).getConnectedNodes();
                node = (int)Math.floor(spectral_data.getNodeIndices().get(i));
            }
        }

        seeds[divide-1] = node;
        System.out.println("Node: " + node + " len: " + max_len);

        for(int i = 0; i < divide; i++) {
            nodes.get(seeds[i]).setGroup(i);
        }

        for(int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).getConnectedNodes() > max_len){
                max_len = nodes.get(i).getConnectedNodes();
            }
        }
        max_len ++;

        ArrayList<ArrayList<ArrayList<Integer>>> que = new ArrayList<>();
        for(int i = 0; i < divide; i++) {
            que.add(new ArrayList<>());
            for(int j = 0; j< max_len; j++){
                que.get(i).add(new ArrayList<>());
            }
        }



        int max_gr_size = Math.round(nodes.size()/divide);

        for(int i = 0; i < divide; i++) {
            addToQue(que.get(i), seeds[i]);
        }

        for(int i = 0; i <max_gr_size; i++) {
            for(int j = 0; j < divide; j++) {
                add_from_que(que.get(j), j, max_len);
            }
        }



    }

    void addToQue(ArrayList<ArrayList<Integer>> que, int node){
        for(int i =0; i< nodes.get(node).getConnectedNodes(); i++){
            int adj_node = nodes.get(node).getAdjacencyList().get(i);
            int adj_node_con = nodes.get(adj_node).getConnectedNodes();
            que.get(adj_node_con).addLast(adj_node);
        }
    }

    void add_from_que(ArrayList<ArrayList<Integer>> que, int group, int max_len){
        int succes = 0;
        for(int i = 0 ; i < max_len && succes != 1; i++){
            if(que.get(i).size() > 0){
                int node = is_valid(que.get(i));
                if(node != -1){
                    addToQue(que, node);
                    nodes.get(node).setGroup(group);
                    ++succes;
                    return;
                }
            }
        }
    }

    int is_valid(ArrayList<Integer> que){
       while(que.size() != 0){
           int node = que.removeFirst();
           if(nodes.get(node).getGroup() == -1){
               return node;
           }
       }

       return -1;
    }

    public void addFreeNodes(){
        int fixed = 0;
        while(fixed != 1){
            fixed = 1;
            for(Node node : nodes) {
                if(node.getGroup() == -1) {
                    int smallest_gr = 99999999;
                    int node_nr = -1;
                    for(int i = 0; i < node.getConnectedNodes(); i++) {
                      //dodam jeszcze chyba jednego arraylista do ilosci wiezchołków w grupach
                    }
                }
            }
        }
    }


}
