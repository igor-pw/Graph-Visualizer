import java.util.Objects;

public class Main {

    public static void main(String[] args) {
/*
        int iterations = 57;

        String file_path = args[0];

        GraphData graph_data = ParseData.readFile(file_path);

        for (int i = 0; i < Objects.requireNonNull(graph_data).getNodes().size(); i++) {
            graph_data.getNodes().get(i).setAdjacencyList(graph_data.createAdjacencyList(i));
        }

        SpectralData spectral_data = new SpectralData(graph_data.getNodes());
        spectral_data.getDegreeVector().norm();

        //spectral_data.getLaplaceMatrix().printCSR();

        if(graph_data.getGroups() > 0)
        {
            int groups = 0;
            for (int j = 0; j < graph_data.getNodes().size(); j++) {
                if (graph_data.getNodes().get(j).getGroup() == -1 && !graph_data.getNodes().get(j).getAdjacencyList().isEmpty()) {
                    graph_data.inintGroups(graph_data.getNodes().get(j), groups);
                    groups++;
                }
            }

            VisualiseGraph.Visualise(graph_data);

            return;
        }

        Vector initial_vector = new Vector(spectral_data.getDegreeVector().size());
        initial_vector = initial_vector.createInitialVector(spectral_data.getDegreeVector());

        Vector prev_initial_vector = new Vector(initial_vector.size());

        Vector alfa_coeffs = new Vector(initial_vector.size());
        Vector beta_coeffs = new Vector(initial_vector.size());

        for(int i = 0; i < alfa_coeffs.size(); i++)
        {
            alfa_coeffs.set(i, 0.0);
            beta_coeffs.set(i, 0.0);
        }

        spectral_data.calculateCoeffs(initial_vector, prev_initial_vector, alfa_coeffs, beta_coeffs, 0, iterations);

        Matrix tridiagonal_matrix = new Matrix(iterations);
        tridiagonal_matrix.createTridiagonalMatrix(alfa_coeffs, beta_coeffs, iterations);

	    Matrix orthogonal_matrix = new Matrix(iterations);

        alfa_coeffs = null;
        beta_coeffs = null;

        Vector eigenvalue_vector = new Vector(iterations, 0.0);
        eigenvalue_vector.createEigenvalueVector(tridiagonal_matrix, orthogonal_matrix, 0);

        //eigenvalue_vector.print();

        tridiagonal_matrix = null;
        orthogonal_matrix = null;

        double eigenvalue = eigenvalue_vector.getFirst();

       for(int i = 1; i < eigenvalue_vector.size(); i++)
       {
            if(eigenvalue_vector.get(i) < eigenvalue && eigenvalue_vector.get(i) > 0) {
                eigenvalue = eigenvalue_vector.get(i);
            }
       }

       for(int i = 0; i < spectral_data.getLaplaceMatrix().getValues().size(); i++)
       {
           if(spectral_data.getLaplaceMatrix().getValues().get(i) > 0) {
               spectral_data.getLaplaceMatrix().getValues().set(i, spectral_data.getLaplaceMatrix().getValues().get(i) - eigenvalue);
           }
       }

       eigenvalue_vector = null;

       //System.out.println("min eigenvalue: " + eigenvalue);

       eigenvalue = 1.0;

       for(int i = 0; i < spectral_data.getLaplaceMatrix().getValues().size(); i++)
       {
           if(spectral_data.getLaplaceMatrix().getValues().get(i) > 0) {
               spectral_data.getLaplaceMatrix().getValues().set(i, spectral_data.getLaplaceMatrix().getValues().get(i) - eigenvalue);
           }
       }

        Vector velocity = new Vector(spectral_data.getEigenvector().size(), 0.0);
        spectral_data.setEigenvector(initial_vector);

        do
        {
            spectral_data.calculateEigenvector(velocity, 0.0, 0);
        } while (spectral_data.getEpsilon() > spectral_data.getEpsilonMargin());

        //spectral_data.getEigenvector().print();

        graph_data.setEigenvalues(spectral_data.getEigenvector());

        spectral_data.sortEigenvector();
        System.out.println();
        //spectral_data.getEigenvector().print();

        int divide = 1000;
        double margin = 0.1;

        ColorGenerator.setGroupCount(divide);

        graph_data.setParameters(divide,margin);

        graph_data.assignGroups(spectral_data);

        //graph_data.printNodes();
*/
         VisualiseGraph.Visualise();

        //System.out.println("Stosunek usuniętych połączeń: " + graph_data.ratio());
    }
}
