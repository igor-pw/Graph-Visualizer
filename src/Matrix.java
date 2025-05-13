public class Matrix {
    private double[][] matrix;
    private final int size;

    public Matrix(int size) {
        matrix = new double[size][size];
        this.size = size;
    }

    public Matrix(double[][] matrix, int size) {
        this.matrix = matrix;
        this.size = size;
    }

    public void printMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    /*public void memMatrix(String[][] content){
        for(int i = 1; i< content[4].length; i++){
            int am = Integer.parseInt(content[4][i]) - Integer.parseInt(content[4][i-1]);
            if(am != 0){
                for(int j=Integer.parseInt(content[4][i-1])+1;j<Integer.parseInt(content[4][i]);j++) {
                    //if (j < content[4].length)
                    matrix[Integer.parseInt(content[3][Integer.parseInt(content[4][i-1])])][Integer.parseInt(content[3][j])] = 1;
                    matrix[Integer.parseInt(content[3][j])][Integer.parseInt(content[3][Integer.parseInt(content[4][i-1])])] = 1;
                }
            }
        }
    }*/

    public Matrix multiplyMatrix(Matrix rightMatrix) {
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
    }


    public void copyMatrix(Matrix matrix) {
        this.matrix = matrix.matrix;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public int getSize() {
        return size;
    }

}
