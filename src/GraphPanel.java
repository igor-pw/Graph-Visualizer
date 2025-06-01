import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    private final GraphData graphdata;
    private double scale = 1.0;

    public GraphPanel(GraphData graphdata) {
        this.graphdata = graphdata;
        addMouseWheelListener(e -> {
            if (e.getPreciseWheelRotation() < 0) {
                scale *= 1.1;
            } else {
                scale /= 1.1;
            }
            revalidate();
            repaint();
        });
    }

    @Override
    public Dimension getPreferredSize() {
        if (graphdata.getNodes() == null || graphdata.getNodes().isEmpty()) {
            return new Dimension((int)(800 * scale), (int)(600 * scale));
        }

        int max_X = 0;
        int max_Y = 0;
        for (Node node : graphdata.getNodes()) {
            max_X = Math.max(max_X, node.getX());
            max_Y = Math.max(max_Y, node.getY());
        }

        int width = (int)((20 * max_X + 20 + 50) * scale);
        int height = (int)((20 * max_Y + 20 + 50) * scale);
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale, scale);

        Rectangle rawVisibleRect = getVisibleRect();
        Rectangle visibleRect = new Rectangle(
                (int)(rawVisibleRect.x / scale),
                (int)(rawVisibleRect.y / scale),
                (int)(rawVisibleRect.width / scale),
                (int)(rawVisibleRect.height / scale)
        );

        setBackground(Color.WHITE);

        //Kolory dla grup

        Color[] groupColors = ColorGenerator.generateColors(12345L);

        /*        Color[] groupColors = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW
        };*/

        for (Node node : graphdata.getNodes()) {
            int x1 = 20 * node.getY() + 10;
            int y1 = 20 * node.getX() + 10;
            int group = node.getGroup();

            for (Integer neighborIdx : node.getAdjacencyList()) {
                Node neighbor = graphdata.getNodes().get(neighborIdx);
                if (node.getNr() < neighbor.getNr() && group == neighbor.getGroup() && group >= 0) {

                    int x2 = 20 * neighbor.getY() + 10;
                    int y2 = 20 * neighbor.getX() + 10;
                    if (group >= 0) {
                        g.setColor(groupColors[group % groupColors.length]);
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                    }
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        }

        // Potem rysuj tylko widoczne węzły
        for (Node node : graphdata.getNodes()) {
            Rectangle nodeRect = new Rectangle(20 * node.getY(), 20 * node.getX(), 18, 18);
            if (visibleRect.intersects(nodeRect)) {
                int group = node.getGroup();
                if (group >= 0) {
                    g.setColor(groupColors[group % groupColors.length]);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillOval(20 * node.getY(), 20 * node.getX(), 18, 18);

                //g.setColor(Color.RED);
                //g.drawString(String.valueOf(node.getNr()), 20 * node.getY() + 7, 20 * node.getX() + 15);
            }
        }
        //Rysowanie krawędzie między połączonymi węzłami
        /*for (int i = 0; i < graphdata.getNodes().size(); i++) {
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
                    if(visibleRect.intersectsLine(x1, y1, x2, y1)) {
                        g.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }*/

    /*for (Node node : graphdata.getNodes())
    {
        int x1 = 20 * node.getY() + 10;
        int y1 = 20 * node.getX() + 10;
        Rectangle nodeRect = new Rectangle(20 * node.getY(), 20 * node.getX(), 18, 18);
*/
        // Rysuj tylko jeśli wierzchołek jest widoczny

        /*if (visibleRect.intersects(nodeRect))
        {
            int group = node.getGroup();
            if (group >= 0) {
                g.setColor(groupColors[group % groupColors.length]);
            } else {
                g.setColor(Color.BLACK);
            }

            g.fillOval(20 * node.getY(), 20 * node.getX(), 18, 18);

            //g.setColor(Color.RED);
            //g.drawString(String.valueOf(node.getNr()), 20 * node.getY() + 7, 20 * node.getX() + 15);

            for (Integer neighborIdx : node.getAdjacencyList())
            {
                Node neighbor = graphdata.getNodes().get(neighborIdx);
                // Rysuj krawędź tylko raz
                if (node.getNr() < neighbor.getNr())
                {
                    int x2 = 20 * neighbor.getY() + 10;
                    int y2 = 20 * neighbor.getX() + 10;
                    Rectangle neighborRect = new Rectangle(20 * neighbor.getY(), 20 * neighbor.getX(), 18, 18);
                    // Rysuj krawędź tylko jeśli przynajmniej jeden koniec jest widoczny
                    if (visibleRect.intersects(nodeRect) || visibleRect.intersects(neighborRect))
                    {
                        g.setColor(groupColors[group % groupColors.length]);
                        g.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }*/

        /*
        //Rysowanie węzłów
        for (Node node : graphdata.getNodes())
        {
            Rectangle nodeRect = new Rectangle(20*node.getY(), 20*node.getX(), 18, 18);

            if(visibleRect.intersects(nodeRect))
            {
                int group = node.getGroup();
                if (group >= 0) {
                    g.setColor(groupColors[group % groupColors.length]);
                } else {
                    g.setColor(Color.BLACK);
                }

                g.fillOval(20 * node.getY(), 20 * node.getX(), 18, 18);

                g.setColor(Color.RED);
                g.drawString(String.valueOf(node.getNr()), 20 * node.getY() + 7, 20 * node.getX() + 15);
            }
        }*/

    }


}
