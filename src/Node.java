public class Node
{
    private int x, y, group, nr;

    public Node(int x, int y, int group, int nr)
    {
        this.x = x;
        this.y = y;
        this.group = group;
        this.nr = nr;
    }

    /**
     * Debug method: Prints node information to console
     * Outputs the node number, coordinates, and group
     */
    public void printNode()
    {
        System.out.println(nr + ": " + x + " x " + y + "group: " + group);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getGroup() { return group; }
    public void setGroup(int group) { this.group = group; }
    public int getNr() { return nr; }

}
