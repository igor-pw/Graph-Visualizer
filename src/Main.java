import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        int iterations = 50;

        String file_path = args[0];

        GraphData graph_data = ParseData.readFile(file_path);

        for (int i = 0; i < Objects.requireNonNull(graph_data).getNodes().size(); i++) {
            graph_data.getNodes().get(i).setAdjacencyList(graph_data.createAdjacencyList(i));
        }

        SpectralData spectral_data = new SpectralData(graph_data.getNodes());
        spectral_data.getDegreeVector().norm();

        spectral_data.getLaplaceMatrix().printCSR();

        if(graph_data.getGroups() > 0)
        {
            int groups = 0;
            for (int j = 0; j < graph_data.getNodes().size(); j++) {
                if (graph_data.getNodes().get(j).getGroup() == -1 && !graph_data.getNodes().get(j).getAdjacencyList().isEmpty()) {
                    graph_data.assignGroups(graph_data.getNodes().get(j), groups);
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

        alfa_coeffs = null;
        beta_coeffs = null;

    }
}