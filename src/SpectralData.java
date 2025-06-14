import java.util.ArrayList;
import java.util.Collections;

public class SpectralData
{
    private final double learning_rate;
    private final double momentum;
    private double epsilon;
    private double epsilon_margin;
    private final CSR laplace_matrix;
    private final Vector degree_vector;
    private Vector eigenvector;
    private Vector nodeIndices;


    public SpectralData(ArrayList<Node> nodes)
    {
        this.laplace_matrix = new CSR();
        this.degree_vector = new Vector(nodes.size());
        this.eigenvector = new Vector(nodes.size());

        this.learning_rate = 0.001;
        this.momentum = 0.8;
        this.epsilon = 0.0;
        this.epsilon_margin = Math.pow(10, -12);

        if(degree_vector.size() > 5000) {
            this.epsilon_margin = Math.pow(10, -5);
        }

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
    public Vector getNodeIndices() {
        return nodeIndices;
    }


    public CSR getLaplaceMatrix() { return laplace_matrix; }
    public Vector getDegreeVector() { return degree_vector; }
    public Vector getEigenvector() { return eigenvector; }
    public double getEpsilon() { return epsilon; }
    public double getEpsilonMargin() { return epsilon_margin; }

    public void setEigenvector(Vector initial_vector) { this.eigenvector = initial_vector; }

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

    public void calculateEigenvector(Vector velocity, double prev_epsilon, int depth) {
        // Inicjalizacja velocity zerami, jeśli jest pusty
        if (velocity.size() != this.eigenvector.size()) {
            velocity.clear();
            for (int i = 0; i < this.eigenvector.size(); i++) {
                velocity.add(0.0);
            }
        }

        Vector old_eigenvector = new Vector(this.eigenvector.size());
        for (int i = 0; i < this.eigenvector.size(); i++) {
            old_eigenvector.add(this.eigenvector.get(i));
        }

        Vector r_vec = this.eigenvector.multiplyByCSR(laplace_matrix);
        Vector gradient = r_vec.multiplyByCSR(laplace_matrix);
        for (int i = 0; i < gradient.size(); i++)
            gradient.set(i, gradient.get(i) * 2);

        double vec_norm = 0.0;
        for (int i = 0; i < this.eigenvector.size(); i++) {
            velocity.set(i, this.momentum * velocity.get(i) + (1.0 - this.momentum) * gradient.get(i));
            this.eigenvector.set(i, this.eigenvector.get(i) - this.learning_rate * velocity.get(i));
            vec_norm += this.eigenvector.get(i) * this.eigenvector.get(i);
        }

        vec_norm = Math.sqrt(vec_norm);
        this.eigenvector.divide(vec_norm);

        this.epsilon = 0.0;
        for (int i = 0; i < this.eigenvector.size(); i++)
            this.epsilon += Math.pow(this.eigenvector.get(i) - old_eigenvector.get(i), 2);
        this.epsilon = Math.sqrt(this.epsilon);

        if (this.epsilon < Math.pow(10, -3) && this.epsilon >= prev_epsilon)
        {
            this.epsilon_margin = Math.pow(10, -3);
            return;
        }

        prev_epsilon = this.epsilon;

        if (this.epsilon > this.epsilon_margin && depth + 1 < 2000) {
            calculateEigenvector(velocity, prev_epsilon, depth + 1);
        }
    }

    public void sortEigenvector() {
        ArrayList<EigenvaluePair> razem = new ArrayList<>();
        for (int i = 0; i < eigenvector.size(); i++) {
            razem.add(new EigenvaluePair(eigenvector.get(i), i));
        }

        Collections.sort(razem);

        Vector sortedEigenvector = new Vector(eigenvector.size());
        Vector nodeIndices = new Vector(eigenvector.size());

        for (EigenvaluePair dwie : razem) {
            sortedEigenvector.add(dwie.getValue());
            nodeIndices.add((double) dwie.getNodeIndex());
        }
        this.eigenvector = sortedEigenvector;
        this.nodeIndices = nodeIndices;
    }


}
