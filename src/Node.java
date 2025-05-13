public class Node
{
    private final int x, y, nr;
    private int group;

    public Node(int x, int y, int group, int nr)
    {
        this.x = x;
        this.y = y;
        this.group = group;
        this.nr = nr;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getGroup() { return group; }
    public void setGroup(int group) { this.group = group; }
    public int getNr() { return nr; }

    public void printNode()
    {
        System.out.println(nr + ": " + x + " x " + y + " group: " + group);
    }
}
