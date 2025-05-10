import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String file_path = args[0];

        String[][] content = File.readGraph(file_path);
        int nodes =content[1].length;
        Node[] node = new Node[nodes];
        System.out.println("Nodes: " + nodes);
        Matrix A_matrix = new Matrix(nodes);

        // można przenieść gdzieś indziej jako funkcja
        int dim = Integer.parseInt(content[0][0]);
        int line = 0;
        int num = 0;
        for(int i=1;i< content[2].length;i++){
            int am = Integer.parseInt(content[2][i]) - Integer.parseInt(content[2][i-1]);
            if(am != 0){
                for(int j=Integer.parseInt(content[2][i-1]);j<Integer.parseInt(content[2][i]);j++) {
                    node[num] = new Node(Integer.parseInt(content[1][j]),line,-1,num);
                    num++;
                }
            }
            line++;
        }

        A_matrix.memMatrix(content);



       // A_matrix.printMatrix();


        //



        /*
       // VisualiseDividedGraph.VisualiseGraph(file_path);
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
