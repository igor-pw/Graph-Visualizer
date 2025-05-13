import java.util.ArrayList;

public class CSR
{
    private ArrayList<Double> values;
    private ArrayList<Integer> col_ind;
    private ArrayList<Integer> row_ptr;

    public CSR(ArrayList<Double> values, ArrayList<Integer> col_ind, ArrayList<Integer> row_ptr)
    {
        this.values = values;
        this.col_ind = col_ind;
        this.row_ptr = row_ptr;
    }
}
