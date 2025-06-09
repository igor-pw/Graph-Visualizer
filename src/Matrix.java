import java.util.ArrayList;
import java.util.stream.Collectors;

public class Matrix {

    private final int size;
    private ArrayList<ArrayList<Double>> values;

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

        this.values = matrix;
    }

    public ArrayList<ArrayList<Double>> getValues() { return values; }
    public int getSize() { return size; }

    public void setValues(ArrayList<ArrayList<Double>> values) { this.values = values; }

    public void createTridiagonalMatrix(Vector alfa_coeffs, Vector beta_coeffs, int size)
    {
        for(int i = 0; i < size-1; i++)
        {
            values.get(i).set(i, alfa_coeffs.get(i));
            values.get(i).set(i+1, beta_coeffs.get(i));
            values.get(i+1).set(i, beta_coeffs.get(i));
        }

        values.get(size-1).set(size-1, alfa_coeffs.getLast());
    }

    public void createGivensMatrix(Matrix tridiagonal_matrix, int x)
    {
	    double radius = Math.sqrt(Math.pow(tridiagonal_matrix.values.get(x).get(x), 2) + Math.pow(tridiagonal_matrix.values.get(x+1).get(x), 2));
        double sinus = tridiagonal_matrix.values.get(x+1).get(x)/radius;
        double cosinus = tridiagonal_matrix.values.get(x).get(x)/radius;

        for(int i = 0; i < size; i++){
            this.values.get(i).set(i, 1.0);
        }

        this.values.get(x).set(x, cosinus);
        this.values.get(x+1).set(x+1, cosinus);
        this.values.get(x).set(x+1, sinus);
        this.values.get(x+1).set(x, -sinus);
    }


    public Matrix multiplyByMatrix(Matrix right_matrix)
      {
        Matrix result_matrix = new Matrix(size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result_matrix.values.get(i).set(j, result_matrix.values.get(i).get(j) + this.values.get(i).get(k) * right_matrix.values.get(k).get(j));
                }
            }
        }
        return result_matrix;
    }

    public void transposeMatrix()
    {
        for(int i = 0; i < size; i++)
        {
            for(int j = i; j < size; j++)
            {
                double temp = this.values.get(i).get(j);
                this.values.get(i).set(j, this.values.get(j).get(i));
                this.values.get(j).set(i, temp);
            }
        }
    }

}
