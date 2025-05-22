public class EigenvaluePair implements Comparable<EigenvaluePair> {
    private final double value;
    private final int nodeIndex;
    
    public EigenvaluePair(double value, int nodeIndex) {
        this.value = value;
        this.nodeIndex = nodeIndex;
    }
    
    public double getValue() {
        return value;
    }
    
    public int getNodeIndex() {
        return nodeIndex;
    }
    
    @Override
    public int compareTo(EigenvaluePair other) {return Double.compare(this.value, other.value);
    }
}