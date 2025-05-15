import java.util.ArrayList;

public class SpectralData
{
    private final CSR laplace_matrix;
    private final Vector degree_vector;

    public SpectralData(ArrayList<Node> nodes)
    {
        this.laplace_matrix = new CSR();
        this.degree_vector = new Vector(nodes.size());

        laplace_matrix.getRowPointer().add(0);

        int prev_row_counter = 0;

        for(Node node : nodes)
        {
            degree_vector.add((double)node.getAdjacencyList().size());
            laplace_matrix.getValues().add((double)node.getAdjacencyList().size());
            laplace_matrix.getColIndex().add(node.getNr());

            for(int i = 0; i < node.getAdjacencyList().size(); i++)
            {

                laplace_matrix.getValues().add(-1.0);
                laplace_matrix.getColIndex().add(node.getAdjacencyList().get(i));

            }

            laplace_matrix.getRowPointer().add(prev_row_counter + node.getAdjacencyList().size() + 1);
            prev_row_counter += node.getAdjacencyList().size() + 1;
        }

        laplace_matrix.getValues().trimToSize();
        laplace_matrix.getColIndex().trimToSize();
        laplace_matrix.getRowPointer().trimToSize();
    }

    public CSR getLaplaceMatrix() { return laplace_matrix; }
    public Vector getDegreeVector() { return degree_vector; }

    public void calculateCoeffs(Vector initial_vector, Vector prev_initial_vector, Vector alfa_coeffs, Vector beta_coeffs, int depth, int k)
    {
        Vector residual_vector = new Vector(initial_vector.size());
        residual_vector = initial_vector.multiplyByCSR(laplace_matrix);

        if(depth > 0){
            residual_vector.subtract(prev_initial_vector, beta_coeffs.get(depth-1));
        }

        alfa_coeffs.add(residual_vector.multiplyByVec(initial_vector));

        residual_vector.norm();

        beta_coeffs.add(residual_vector.getNorm());

        prev_initial_vector.addAll(initial_vector);

        initial_vector.divide(beta_coeffs.get(depth));

        if(depth < k) {
            calculateCoeffs(initial_vector, prev_initial_vector, alfa_coeffs, beta_coeffs, ++depth, k);
        }

        residual_vector = null;
    }

    public void printDegreeVector(){
        for(Double value : degree_vector){
            System.out.print(value + " ");
        }

        System.out.println();
    }

}
