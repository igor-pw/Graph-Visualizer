import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String file_path = args[0];

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

    }




}
