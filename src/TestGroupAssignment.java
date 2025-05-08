import java.util.ArrayList;

public class TestGroupAssignment {
    public static void main(String[] args) {
        // Path to the test file
        String filePath = "data/test_example.txt";
        
        System.out.println("Reading file: " + filePath);
        
        // Read the file and get the nodes
        GraphData graphData = File.readFile(filePath);
        ArrayList<Node> nodes = graphData.getNodes();
        
        System.out.println("\nSummary of node groups:");
        
        // Count nodes in each group
        int maxGroup = -1;
        for (Node node : nodes) {
            maxGroup = Math.max(maxGroup, node.getGroup());
        }
        
        int[] groupCounts = new int[maxGroup + 1];
        for (Node node : nodes) {
            if (node.getGroup() >= 0) {
                groupCounts[node.getGroup()]++;
            }
        }
        
        // Print summary
        for (int i = 0; i <= maxGroup; i++) {
            System.out.println("Group " + i + ": " + groupCounts[i] + " nodes");
        }
        
        // Check if node 0 and its connections (72, 39, 4) are in the same group
        if (nodes.size() > 72) {
            System.out.println("\nChecking if node 0 and its connections are in the same group:");
            int group0 = nodes.get(0).getGroup();
            System.out.println("Node 0 is in group: " + group0);
            
            if (nodes.size() > 72) {
                System.out.println("Node 72 is in group: " + nodes.get(72).getGroup() + 
                                  (nodes.get(72).getGroup() == group0 ? " (same as node 0)" : " (different from node 0)"));
            }
            
            if (nodes.size() > 39) {
                System.out.println("Node 39 is in group: " + nodes.get(39).getGroup() + 
                                  (nodes.get(39).getGroup() == group0 ? " (same as node 0)" : " (different from node 0)"));
            }
            
            if (nodes.size() > 4) {
                System.out.println("Node 4 is in group: " + nodes.get(4).getGroup() + 
                                  (nodes.get(4).getGroup() == group0 ? " (same as node 0)" : " (different from node 0)"));
            }
        }
    }
}