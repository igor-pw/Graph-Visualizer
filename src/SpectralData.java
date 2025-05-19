import java.util.ArrayList;

public class SpectralData
{
    private final double learning_rate;
    private final double momentum;
    private double epsilon;
    private double epsilon_margin;
    private final CSR laplace_matrix;
    private final Vector degree_vector;
    private Vector eigenvector;

    public SpectralData(ArrayList<Node> nodes)
    {
        this.laplace_matrix = new CSR();
        this.degree_vector = new Vector(nodes.size());
        this.eigenvector = new Vector();

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

    public void calculateEigenvector(Vector velocity, double prev_epsilon, int depth)
    {
        this.eigenvector.print();

        Vector residual_vector = this.eigenvector.multiplyByCSR(laplace_matrix);

        Vector gradient_vector = residual_vector.multiplyByCSR(laplace_matrix);

        for(int i = 0; i < gradient_vector.size(); i++){
            gradient_vector.set(i, gradient_vector.get(i)*2);
        }

        residual_vector.clear();
        residual_vector.addAll(this.eigenvector);
        this.eigenvector.norm();

        for(int i = 0; i < velocity.size(); i++)
        {
            velocity.set(i, this.momentum*velocity.get(i) + (1.0 - this.momentum) * gradient_vector.get(i));
            this.eigenvector.set(i, this.eigenvector.get(i) - this.learning_rate*velocity.get(i));
        }

        /*
        for(double value : velocity) {
            value = this.momentum*value + (1.0 - this.momentum)*gradient_vector.get(velocity.indexOf(value));
            this.eigenvector.set(velocity.indexOf(value), this.eigenvector.get(velocity.indexOf(value)) - this.learning_rate*value);
        }*/

        this.eigenvector.divide(this.eigenvector.getNorm());
        this.epsilon = 0.0;

        for(int i = 0; i < this.eigenvector.size(); i++) {
            this.epsilon += Math.pow(eigenvector.get(i) - residual_vector.get(i), 2);
        }

        this.epsilon = Math.sqrt(this.epsilon);

        System.out.println("depth: " + depth + " epsilon: " + this.epsilon + " prev_epsilon: " + prev_epsilon + " epsilon_margin: " + this.epsilon_margin);

        residual_vector = null;
        gradient_vector = null;

        if(this.epsilon < Math.pow(10, -3) && this.epsilon >= prev_epsilon) {
            //System.out.println("Koniec");
            this.epsilon_margin = Math.pow(10, -3);
            return;
        }

        /*else if(this.epsilon == prev_epsilon) {
            this.epsilon_margin = Math.pow(10, 3);
            return;
        }*/

        prev_epsilon = this.epsilon;

        if(this.epsilon > this.epsilon_margin && ++depth < 2000) {
            System.out.println("Dalej");
            calculateEigenvector(velocity, prev_epsilon, depth);
        }
    }

    public void printDegreeVector(){
        for(Double value : degree_vector){
            System.out.print(value + " ");
        }

        System.out.println();
    }

}
