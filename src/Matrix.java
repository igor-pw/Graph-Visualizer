public class Matrix {
    private double[][] matrix;
    private int size;

    public Matrix(int size) {
        matrix = new double[size][size];
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

    public void memMatrix(String[][] content){
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
    }



    public void test(int l){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = 1+(i+j)*l;
            }
        }
        printMatrix();
    }

    public void multiplyMatrix(Matrix matrix) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.matrix[i][j] *= matrix.matrix[i][j];
            }
        }
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
