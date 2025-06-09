import javax.swing.*;
import java.awt.*;


public class GraphPanel extends JPanel {

    private GraphData graphdata;
    private double scale = 1.0;


    // Konstruktor z parametrem (zachowane dla kompatybilności)
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
        if (graphdata == null || graphdata.getNodes() == null || graphdata.getNodes().isEmpty()) {
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
        setBackground(Color.WHITE);

        // Jeśli brak danych, nie rysuj grafu - tylko puste białe tło
        if (graphdata == null || graphdata.getNodes() == null || graphdata.getNodes().isEmpty()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale, scale);

        Rectangle rawVisibleRect = getVisibleRect();
        Rectangle visibleRect = new Rectangle(
                (int)(rawVisibleRect.x / scale),
                (int)(rawVisibleRect.y / scale),
                (int)(rawVisibleRect.width / scale),
                (int)(rawVisibleRect.height / scale)
        );

        //Kolory dla grup
        Color[] groupColors = ColorGenerator.generateColors(12345L);

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
            }
        }
    }
}
