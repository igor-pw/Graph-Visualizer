import java.util.ArrayList;
import java.util.Stack;

public class GraphData
{
    private final ArrayList<Node> nodes;
    private final ArrayList<ArrayList<Integer>> adjacency_matrix;
    private final int groups;
    private int divide;
    private double margin;
    private int min;
    private int max;

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

    public void setMinMax() {
        this.max = (int)Math.floor((double)nodes.size()/divide*(1+margin));
        this.min = (int)Math.ceil((double)nodes.size()/divide*(1-margin));
    }

    public void assignGroups(SpectralData spectral_data) {

        double div = (double)nodes.size() / divide;
        System.out.println("div: " + div);
        int[] seeds = new int[divide];
        int tmp = 0;


        for(int i = 1; i < divide; i++) {
            int ind_k = (int)Math.round(i*div);
            int ind_p = (int)Math.round(((i-1)*div)+1);
            tmp = ind_k;

            int max_len = -1;
            int node = -1;


            for(int j = ind_p; j <= ind_k; j++) {
                if(j < nodes.size() && nodes.get(j).getConnectedNodes() > max_len) {
                    max_len = nodes.get(j).getConnectedNodes();
                    node = (int)Math.floor(spectral_data.getNodeIndices().get(j));
                }
            }

            seeds[i-1] = node;

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
      //  System.out.println("Node: " + node + " len: " + max_len);

        int[] gr_count = new int[divide];

        for(int i = 0; i < divide; i++) {

            nodes.get(seeds[i]).setGroup(i);
            gr_count[i]++;

        }

        System.out.println(nodes.get(3).getGroup());

        for(int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).getConnectedNodes() > max_len){
                max_len = nodes.get(i).getConnectedNodes();
            }
        }
        max_len ++;
        System.out.println(nodes.get(3).getGroup());

        ArrayList<ArrayList<ArrayList<Integer>>> que = new ArrayList<>();
        for(int i = 0; i < divide; i++) {
            que.add(new ArrayList<>());
            for(int j = 0; j< max_len; j++){
                que.get(i).add(new ArrayList<>());
            }
        }
        System.out.println(nodes.get(3).getGroup());



        int max_gr_size = Math.round(nodes.size()/divide);

        for(int i = 0; i < divide; i++) {
            addToQue(que.get(i), seeds[i]);
        }
        System.out.println(nodes.get(3).getGroup());

        for(int i = 0; i <max_gr_size; i++) {
            for(int j = 0; j < divide; j++) {
                add_from_que(que.get(j), j, max_len, gr_count);
            }
        }
        System.out.println(nodes.get(3).getGroup());

        addFreeNodes(gr_count);


        for(int i = 0; i < divide; i++) {
            int gr_size  = 0;
            for(int j = 0; j < nodes.size(); j++) {
                if(nodes.get(j).getGroup() == i){
                    gr_size++;
                }
                else if(nodes.get(j).getGroup() == -1){

                }
            }


        }
        gainCalculate();

        for(int i = 0; i < 11; i++){
            findLeaves();
            refineGroups(gr_count);
            for(int j = 0; j < nodes.size(); j++){
                nodes.get(j).setLeaf(false);
            }
            gainCalculate();
        }

        int[] dfs_check = dfsCheck(gr_count);
        for(int i = 0; i < divide; i++){
            if(dfs_check[i] != gr_count[i]){
                System.out.println("OJOJ, wygląda na to że gr." + i + " nie jest spójna       "+"dfs: " + dfs_check[i] + " gr_count: " + gr_count[i]);
            }
        }
        for(int i = 0; i < divide; i++){
            if(gr_count[i] <=1){
                System.out.println("Womp Womp, gr." + i + "ma za mało wieszchołków :(");
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

    void add_from_que(ArrayList<ArrayList<Integer>> que, int group, int max_len, int[] gr_count){
        int succes = 0;
        for(int i = 0 ; i < max_len && succes != 1; i++){
            if(que.get(i).size() > 0){
                int node = is_valid(que.get(i));
                if(node != -1){
                    addToQue(que, node);
                    nodes.get(node).setGroup(group);
                    gr_count[group]++;
                    ++succes;
                    return;
                }
            }
        }
    }

    int is_valid(ArrayList<Integer> que){
       while(que.size() != 0){
           int node = que.getFirst();
           if(nodes.get(node).getGroup() == -1){
               que.removeFirst();
               return node;
           }
           que.removeFirst();
       }

       return -1;
    }

    public void addFreeNodes(int[] gr_count){
        int fixed = 0;
        while(fixed != 1){
            fixed = 1;
            for(Node node : nodes) {
                if(node.getGroup() == -1) {
                    int smallest_gr = 100000;
                    int node_nr = -1;
                    for(int i = 0; i < node.getConnectedNodes(); i++){
                        int adj_node = node.getAdjacencyList().get(i);
                        int gr = nodes.get(adj_node).getGroup();
                        if(gr != -1 && gr_count[gr] < smallest_gr) {
                            smallest_gr = gr_count[gr];
                            node_nr = adj_node;
                        }
                    }
                    if(smallest_gr != 100000 && node_nr != -1) {
                        int group = nodes.get(node_nr).getGroup();
                        node.setGroup(group);
                        fixed = 0;
                        gr_count[group]++;
                    }
                }
            }
        }
    }

    public void gainCalculate(){
        int[] dif = new int[divide];
        for(int i = 0; i< nodes.size(); i++){
            for(int j = 0; j < nodes.get(i).getConnectedNodes(); j++){
                dif[nodes.get(j).getGroup()]++;
            }
            int min = 100000;
            int gr = -1;
            for(int j =0; j<divide; j++){
                if(dif[j] != 0){
                    int tmp = dif[nodes.get(i).getGroup()] - dif[j];
                    if(tmp < min){
                        min = tmp;
                        gr = j;
                    }
                }
            }
            if(gr == nodes.get(i).getGroup()) {
                nodes.get(i).setGain(dif[gr],gr);
            }
            else{
                nodes.get(i).setGain(min,gr);
            }

        }

    }

    public void findLeaves(){
        for(int i = 0; i < nodes.size(); i++){
            int counter = 0;
            for(int j =0; j < nodes.get(i).getConnectedNodes(); j++){
                if(nodes.get(i).getGroup() == nodes.get(nodes.get(i).getAdjacencyList().get(j)).getGroup()){
                    counter++;
                }
            }
            if(counter == 1){
                nodes.get(i).setLeaf(true);
            }
        }
    }

    public void refineGroups(int[] gr_count){
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).isLeaf() && nodes.get(i).getGain() < 0 && gr_count[nodes.get(i).getGroup()] > min ){
                int group_gain = nodes.get(i).getGrGain();
                if(gr_count[group_gain] < max && group_gain >= 0){
                    gr_count[nodes.get(i).getGroup()]--;
                    nodes.get(i).setGroup(group_gain);
                    nodes.get(i).setGain(0,-1);
                    gr_count[group_gain]++;
                }
            }
        }
    }

    public int[] dfsCheck(int[] gr_count) {
        int[] res = new int[divide];
        for(int i = 0; i < divide; i++){
            int[] hbs = new int[nodes.size()];
            int node = -1;
            for(int j = 0; j < nodes.size() && node == -1; j++){
                if(nodes.get(j).getGroup() == i){
                    node = j;
                    break;
                }
            }
            if(node != -1){
                res[i] = dfs(hbs, node, i);
            }
        }
        return res;
    }

    public int dfs(int[] hbs, int start, int gr) {
        int count = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        hbs[start] = 1;
        while (!stack.isEmpty()) {
            int node = stack.pop();
            count++;
            for (int i = 0; i < nodes.get(node).getConnectedNodes(); i++) {
                int nb = nodes.get(node).getAdjacencyList().get(i);
                if (nodes.get(nb).getGroup() == gr && hbs[nb] == 0) {
                    hbs[nb] = 1;
                    stack.push(nb);
                }
            }
        }
        return count;
    }

    /*public int dfs(int[] hbs, int node, int gr){
        int count = 1;
        hbs[node]=1;
        for(int i =0; i<nodes.get(node).getConnectedNodes(); i++){
            int nb = nodes.get(node).getAdjacencyList().get(i);
            if(nodes.get(nb).getGroup() == gr && hbs[nb] == 0){
                count += dfs(hbs,nb,gr);
            }
        }

        return count;
    }*/

    public double ratio(){

        double before = 0;
        double after = 0;
        for(int i = 0; i < nodes.size(); i++){
            int node = i;
            for(int j = 0; j < nodes.get(i).getConnectedNodes(); j++){
                int nb_node = nodes.get(i).getAdjacencyList().get(j);
                if(nb_node > node){
                    before++;
                    if(nodes.get(nb_node).getGroup() == nodes.get(node).getGroup()){
                        after++;
                    }
                }

            }
        }
        return 1- after/before;
    }


}
