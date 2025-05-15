import java.util.ArrayList;

public class Matrix {

    private final int size;
    ArrayList<ArrayList<Double>> matrix;

    public Matrix(int size)
    {
        this.size = size;

        ArrayList<ArrayList<Double>> matrix = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            ArrayList<Double> row = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                row.add(0.0);
            }
            matrix.add(row);
        }

        this.matrix = matrix;
    }

    public void createTridiagonalMatrix(Vector alfa_coeffs, Vector beta_coeffs, int size)
    {
        for(int i = 0; i < size-1; i++)
        {
            matrix.get(i).set(i, alfa_coeffs.get(i));
            matrix.get(i).set(i+1, beta_coeffs.get(i));
            matrix.get(i+1).set(i, beta_coeffs.get(i));
        }

        matrix.get(size-1).set(size-1, alfa_coeffs.getLast());
    }

    public void printMatrix()
    {
        for(ArrayList<Double> row : matrix)
        {
            for(Double value : row) {
                System.out.print(value + " ");
            }

            System.out.println();
        }
    }

    /*public Matrix multiplyMatrix(Matrix rightMatrix) {
        int n = this.size;
        double[][] left = this.getMatrix();
        double[][] right = rightMatrix.getMatrix();
        double[][] result = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += left[i][k] * right[k][j];
                }
            }
        }
        return new Matrix(result, n);
    }*/


    public void copyMatrix(Matrix matrix) {
        this.matrix = matrix.matrix;
    }

    /*public double[][] getMatrix() {
        return matrix;
    }*/

    public int getSize() {
        return size;
    }

}
