import java.util.ArrayList;

public class Vector extends ArrayList<Double>
{
    private double norm;

    public void vectorNorm(){
        double norm = 0;
        for(Double value : this){
            norm += value*value;
        }
        norm = Math.sqrt(norm);
        this.norm = norm;
    }

    public void multiplyMatrixByVector(Matrix matrix )
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
    }

    public void multiplyVecByVec(Vector vec){
        for(int i = 0; i < this.size(); i++){
            this.set(i, this.get(i)*vec.get(i));
        }
    }

    public void subtractVector(Vector vec){
        for(int i = 0; i < this.size(); i++){
            this.set(i, this.get(i)-vec.get(i));
        }
    }

    public void divideVector(Vector vec){
        for(int i = 0; i < this.size(); i++){
            this.set(i, this.get(i)/vec.get(i));
        }
    }

    public double getNorm() { return norm; }
}
