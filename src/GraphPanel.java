import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    private final ArrayList<Node> nodes;
    private final ArrayList<ArrayList<Integer>> adjacencyList;

    public GraphPanel(ArrayList<Node> nodes, ArrayList<ArrayList<Integer>> adjacencyList)
    {
        this.nodes = nodes;
        this.adjacencyList = adjacencyList;
    }

    @Override
    public Dimension getPreferredSize() {
        if (nodes == null || nodes.isEmpty()) {
            return new Dimension(800, 600);
        }

        //Szukanie maksymalnych współrzędnych x i y wśród wszystkich węzłów
        int max_X = 0;
        int max_Y = 0;
        for (Node node : nodes) {
            max_X = Math.max(max_X, node.getX());
            max_Y = Math.max(max_Y, node.getY());
        }

        //Dodanie rozmiar węzła z marginesem
        return new Dimension(max_X + 20 + 50, max_Y + 20 + 50);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        setBackground(Color.WHITE);

        //Kolory dla grup
        Color[] groupColors = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, 
            Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW
        };

        //Rysowanie krawędzie między połączonymi węzłami
        for (int i = 0; i < nodes.size(); i++) {
            Node node1 = nodes.get(i);

            int x1 = node1.getX() + 10;
            int y1 = node1.getY() + 10;

            int group = node1.getGroup();
            if (group >= 0) {
                g.setColor(groupColors[group % groupColors.length]);
            } else {
                // Domyślny kolor dla węzłów bez grupy
                g.setColor(Color.BLACK);
            }

            if (i < adjacencyList.size()) {
                for (Integer connectedNodeIndex : adjacencyList.get(i)) {
                    if (connectedNodeIndex < nodes.size()) {
                        Node node2 = nodes.get(connectedNodeIndex);
                        int x2 = node2.getX() + 10;
                        int y2 = node2.getY() + 10;
                        g.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }

        //Rysowanie węzłów
        for (Node node : nodes) {
            int group = node.getGroup();
            if (group >= 0) {
                g.setColor(groupColors[group % groupColors.length]);
            } else {
                g.setColor(Color.BLACK);
            }
            g.fillOval(node.getX(), node.getY(), 20, 20);

            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(node.getNr()), node.getX() + 7, node.getY() + 15);
        }

    }
}
