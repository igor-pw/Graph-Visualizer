import java.util.ArrayList;

public class Vector extends ArrayList<Double> {
    private double norm;

    public Vector() {
        super();
    }

    public Vector(int size) {
        super(size);
    }

    public Vector(int size, double value)
    {
        super(size);
        for(int i = 0; i < size; i++) {
            this.add(value);
        }
    }
    public double getNorm() { return norm; }

    public void norm(){
        double norm = 0;
        for(Double value : this){
            norm += value*value;
        }
        norm = Math.sqrt(norm);
        this.norm = norm;
    }

    public Vector createInitialVector(Vector vector)
    {
        Vector initial_vector = new Vector(vector.size());

        for(Double value : vector){
            initial_vector.add(value/vector.getNorm());
        }

        return initial_vector;
    }

    public Vector multiplyByCSR(CSR csr)
    {
        Vector result_vector = new Vector(this.size());

        for(int i = 0; i < this.size(); i++)
        {
            double value = 0.0;
            for(int j = csr.getRowPointer().get(i); j < csr.getRowPointer().get(i+1); j++) {
                value += csr.getValues().get(j)*this.get(csr.getColIndex().get(j));
            }

            result_vector.add(value);
        }

        return result_vector;
    }

    public void createEigenvalueVector(Matrix tridiagonal_matrix, Matrix orthogonal_matrix, int k)
    {
        Matrix givens_matrix = new Matrix(tridiagonal_matrix.getSize());
        givens_matrix.createGivensMatrix(tridiagonal_matrix, k);

        //givens_matrix.printMatrix();

        Matrix upper_triangular_matrix = givens_matrix.multiplyMatrix(tridiagonal_matrix);

        //upper_triangular_matrix.printMatrix();

        tridiagonal_matrix.setValues(upper_triangular_matrix.getValues());
        upper_triangular_matrix = null;

        givens_matrix.transposeMatrix();

        if(k == 0){
            orthogonal_matrix.setValues(givens_matrix.getValues());
        }

        else {
            orthogonal_matrix = orthogonal_matrix.multiplyMatrix(givens_matrix);
        }

        givens_matrix = null;
        k++;

        if(k < this.size()-1) {
            this.createEigenvalueVector(tridiagonal_matrix, orthogonal_matrix, k);
        }

        else if(k == this.size()-1)
        {
            for(int i = 0; i < this.size(); i++){
                this.set(i, tridiagonal_matrix.getValues().get(i).get(i));
            }
        }
    }

    /*public void multiplyMatrixByVector(Matrix matrix )
    {
        ArrayList<Double> result_vec = new ArrayList<Double>(this.size());
        double value = 0;

        for(int i = 0; i < this.size(); i++){
            for(int j = 0; j < this.size(); j++){
                value += this.get(j)*matrix.getMatrix()[i][j];
            }
            result_vec.add(value);
            value=0;
        }
        this.clear();
        this.addAll(result_vec);
    }*/

    public double multiplyByVec(Vector vector)
    {
        double result = 0.0;
        for(int i = 0; i < this.size(); i++){
            result += this.get(i)*vector.get(i);
        }

        return result;
    }

    public void subtract(Vector vector, double coeff){
        for(int i = 0; i < this.size(); i++){
            this.set(i, this.get(i)-coeff*vector.get(i));
        }
    }

    public void divide(double coeff){
        this.replaceAll(value -> value / coeff);
    }

    public void print()
    {
        for(Double value : this) {
            System.out.print(value + " ");
        }

        System.out.println();
    }

}
