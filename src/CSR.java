import java.util.ArrayList;

public class CSR
{
    private ArrayList<Double> values;
    private ArrayList<Integer> col_index;
    private ArrayList<Integer> row_pointer;

    public CSR()
    {
        this.values = new ArrayList<>();
        this.col_index = new ArrayList<>();
        this.row_pointer = new ArrayList<>();
    }

    public ArrayList<Double> getValues() { return values; }
    public ArrayList<Integer> getColIndex() { return col_index; }
    public ArrayList<Integer> getRowPointer() { return row_pointer; }
    public CSR getCSR() { return this; }

    private void printValues()
    {
        for(Double value : values)
            System.out.print(value + " ");

        System.out.println();
    }

    private void printColIndex()
    {
        for(Integer index : col_index)
            System.out.print(index + " ");

        System.out.println();
    }

    private void printRowPointer()
    {
        for(Integer index : row_pointer)
            System.out.print(index + " ");

        System.out.println();
    }

    public void printCSR()
    {
        printValues();
        printColIndex();
        printRowPointer();
    }
}
