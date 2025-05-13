import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String file_path = args[0];

        String[][] content = File.readGraph(file_path);

        // Check if content[1] exists and has elements
        if (content[1] == null || content[1].length == 0) {
            System.err.println("Error: content[1] is null or empty. Cannot proceed.");
            return;
        }

        // Calculate the maximum possible node index from content[2]
        int maxNodeIndex = 0;
        for (int i = 0; i < content[2].length; i++) {
            try {
                int index = Integer.parseInt(content[2][i]);
                if (index > maxNodeIndex) {
                    maxNodeIndex = index;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing content[2][" + i + "]: " + e.getMessage());
            }
        }

        // Use the larger of content[1].length and maxNodeIndex for the node array size
        int nodes = Math.max(content[1].length, maxNodeIndex + 1);
        System.out.println("content[1].length = " + content[1].length + ", maxNodeIndex = " + maxNodeIndex);
        System.out.println("Using nodes = " + nodes);

        Node[] node = new Node[nodes];
        Matrix A_matrix = new Matrix(nodes);

        // można przenieść gdzieś indziej jako funkcja
        // Parse the dimension from the header line which has format "S numGroups width height"
        String[] headerParts = content[0][0].split(" ");
        int dim = Integer.parseInt(headerParts[1]);
        int line = 0;
        int num = 0;
        for(int i=1;i< content[2].length;i++){
            int am = Integer.parseInt(content[2][i]) - Integer.parseInt(content[2][i-1]);
            if(am != 0){
                // Calculate the range of indices to process
                int startIdx = Integer.parseInt(content[2][i-1]);
                int endIdx = Integer.parseInt(content[2][i]);

                // Debug output
                System.out.println("Processing range: " + startIdx + " to " + endIdx + 
                                  " (content[1].length = " + content[1].length + ")");

                // Process only valid indices
                for(int j = startIdx; j < endIdx; j++) {
                    // Skip invalid indices
                    if (j >= content[1].length || j < 0) {
                        System.out.println("Skipping index " + j + " (out of bounds)");
                        continue;
                    }

                    try {
                        // Special case: if content[1] has only one element, use it for all nodes
                        if (content[1].length == 1) {
                            // Use the single value from content[1] for all nodes
                            node[num] = new Node(Integer.parseInt(content[1][0]), line, -1, num);
                            System.out.println("Created node " + num + " using content[1][0] = " + content[1][0]);
                        } else {
                            // Normal case: use the value from content[1][j]
                            node[num] = new Node(Integer.parseInt(content[1][j]), line, -1, num);
                        }
                        num++;
                    } catch (Exception e) {
                        System.err.println("Error processing index " + j + ": " + e.getMessage());
                    }
                }
            }
            line++;
        }

        //A_matrix.memMatrix(content);

        // Visualize the graph
        VisualiseDividedGraph.VisualiseGraph(file_path);

        /*
        // A_matrix.printMatrix();
        System.out.println();
        Matrix matrix_a = new Matrix(10);
        matrix_a.test(1);
        System.out.println();
        Matrix matrix_b = new Matrix(10);
        matrix_b.test(2);
        System.out.println();
        matrix_a.multiplyMatrix(matrix_b);
        matrix_a.printMatrix();
        System.out.println();
        matrix_b.copyMatrix(matrix_a);
        matrix_b.printMatrix();
        System.out.println();
        Vector vector_a = new Vector(10);
        vector_a.test(1);
        System.out.println();
        Vector vector_b = new Vector(10);
        vector_b.test(2);
        System.out.println();
        vector_a.multiplyMatrixByVector(matrix_a);
        vector_a.printVec();
        System.out.println();
        vector_b.multiplyVecByVec(vector_a);
        vector_b.printVec();
        System.out.println();
        vector_b.divideVector(vector_a);
        vector_b.printVec();
        System.out.println();
        vector_b.subtractVector(vector_a);
        vector_b.printVec();
        System.out.println();
        vector_b.copyVector(vector_a);
        vector_b.printVec();
        System.out.println();
        */


    }




}
