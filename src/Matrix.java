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

    public void calculateEigenvalues(Matrix orthogonal_matrix, int n, int i)
    {
        Matrix givens_matrix = new Matrix(n);
        givens_matrix.createGivensMatrix(this, n, i);
        Matrix upper_triangular_matrix = new Matrix(n);
        upper_triangular_matrix.multiplyMatrixByMatrix(givens_matrix, this, n);
        this.copyMatrix(upper_triangular_matrix);
        givens_matrix.transposeMatrix();
        upper_triangular_matrix = null;
        if(i == 0){
            orthogonal_matrix.copyMatrix(givens_matrix);
        }
        else{
            Matrix new_orthogonal_matrix = new Matrix(n);
            new_orthogonal_matrix.multiplyMatrixByMatrix(orthogonal_matrix, givens_matrix, n);
            orthogonal_matrix.copyMatrix(new_orthogonal_matrix);
            new_orthogonal_matrix = null;
        }

        givens_matrix = null;
        i++;

        if(i<n-1)
            this.calculateEigenvalues(orthogonal_matrix, n, i);
        else if(i == n-1)
        {
            Matrix new_tridiagonal_matrix = new Matrix(n);
            new_tridiagonal_matrix.multiplyMatrixByMatrix(this, orthogonal_matrix, n);
            this.copyMatrix(new_tridiagonal_matrix);
            new_tridiagonal_matrix = null;
        }
    }

    public void transposeMatrix(){
        for(int i = 0; i < this.size; i++){
            for(int j = i; j < this.size; j++){
                double temp = this.matrix.get(i).get(j);
                this.matrix.get(i).set(j, this.matrix.get(j).get(i));
                this.matrix.get(j).set(i, temp);
            }
        }
    }

    public void createGivensMatrix(Matrix tridiagonal_matrix, int n, int x)
    {
        double r = Math.sqrt(Math.pow(tridiagonal_matrix.getValue(x, x), 2) + Math.pow(tridiagonal_matrix.getValue(x+1, x), 2));
        double sin = tridiagonal_matrix.getValue(x+1, x) / r;  // Zmienione z (x, x+1) na (x+1, x)
        double cos = tridiagonal_matrix.getValue(x, x) / r;
        for(int i = 0; i<n; i++){
            this.matrix.get(i).set(i,1.0);
        }
        this.matrix.get(x).set(x,cos);
        this.matrix.get(x+1).set(x+1,cos);
        this.matrix.get(x).set(x+1,sin);
        this.matrix.get(x+1).set(x,-sin);

    }

    public void multiplyMatrixByMatrix(Matrix martix_a, Matrix matrix_b, int n){
        for(int i = 0; i < n ; i ++){
            for(int j = 0; j < n; j++){
                double sum = 0;
                for(int k = 0; k < n; k++){
                    sum += martix_a.getValue(i, k) * matrix_b.getValue(k, j);
                }
                this.matrix.get(i).set(j, sum);
            }
        }

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

    public double getValue(int y, int x){
        return matrix.get(y).get(x);
    }

}
