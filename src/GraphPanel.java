import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    private final GraphData graphdata;

    public GraphPanel(GraphData graphdata){
        this.graphdata = graphdata;
    }

    @Override
    public Dimension getPreferredSize() {
        if (graphdata.getNodes() == null || graphdata.getNodes().isEmpty()) {
            return new Dimension(800, 600);
        }

        //Szukanie maksymalnych współrzędnych x i y wśród wszystkich węzłów
        int max_X = 0;
        int max_Y = 0;
        for (Node node : graphdata.getNodes()) {
            max_X = Math.max(max_X, node.getX());
            max_Y = Math.max(max_Y, node.getY());
        }

        //Dodanie rozmiar węzła z marginesem
        return new Dimension(20*max_X + 20 + 50, 20*max_Y + 20 + 50);
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
        for (int i = 0; i < graphdata.getNodes().size(); i++) {
            Node node1 = graphdata.getNodes().get(i);

            int x1 = 20*node1.getY() + 10;
            int y1 = 20*node1.getX() + 10;

            int group = node1.getGroup();
            if (group >= 0) {
                g.setColor(groupColors[group % groupColors.length]);
            } else {
                // Domyślny kolor dla węzłów bez grupy
                g.setColor(Color.BLACK);
            }

            for (int j = 0; j < graphdata.getAdjacencyMatrix().size(); j++)
            {
                if (graphdata.getAdjacencyMatrix().get(i).get(j) == 1 && j > i) {
                    Node node2 = graphdata.getNodes().get(j);
                    int x2 = 20 * node2.getY() + 10;
                    int y2 = 20 * node2.getX() + 10;
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        }


        //Rysowanie węzłów
        for (Node node : graphdata.getNodes()) {
            int group = node.getGroup();
            if (group >= 0) {
                g.setColor(groupColors[group % groupColors.length]);
            } else {
                g.setColor(Color.BLACK);
            }
            g.fillOval(20*node.getY(), 20*node.getX(), 18, 18);

            g.setColor(Color.RED);
            g.drawString(String.valueOf(node.getNr()), 20*node.getY() + 7, 20*node.getX() + 15);
        }

    }
}
