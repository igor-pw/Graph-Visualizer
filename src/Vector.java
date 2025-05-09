
public class Vector {
    private double vec[];
    private int size;
    private double norm;

    public Vector(int size) {
        this.size=size;
        vec = new double[size];
    }

    public void test(int l){
        for(int i=0;i<size;i++){
            vec[i]=(i+1)*l;
        }
        printVec();
    }

    public void printVec(){
        for(int i=0;i<size;i++){
            System.out.print(vec[i]+" ");
        }
    }

    public double vectorNorm(){
        double norm=0;
        for(int i=0;i<size;i++){
            norm+=vec[i]*vec[i];
        }
        norm = Math.sqrt(norm);
        this.norm= norm;
        return norm;
    }

    public void multiplyMatrixByVector(Matrix matrix ){
        double tmpVec[];
        tmpVec = new double[size];

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                tmpVec[i]+=vec[j]*matrix.getMatrix()[i][j];
            }
        }
        vec=tmpVec;
    }

    public void multiplyVecByVec(Vector V){
        for(int i=0;i<size;i++) {
            vec[i] *= V.vec[i];
        }
    }

    public void subtractVector(Vector v){
        for(int i=0;i<size;i++){
            vec[i]-=v.vec[i];
        }
    }

    public void divideVector(Vector V){
        for(int i=0;i<size;i++){
            vec[i]/=V.vec[i];
        }
    }

    public void copyVector(Vector V){
        for (int i = 0; i < size; i++) {
            vec=V.vec;
        }
    }

    public double[] getVec(){
        return vec;
    }

}
