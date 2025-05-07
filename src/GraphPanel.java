import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    private final ArrayList<Node> nodes;

    public GraphPanel(ArrayList<Node> nodes)
    {
        this.nodes = nodes;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        setBackground(Color.WHITE);

        // Zdefiniuj kolory dla różnych grup
        Color[] groupColors = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, 
            Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW
        };

        // Narysuj każdy węzeł kolorem opartym na jego grupie
        for (Node node : nodes) {
            int group = node.getGroup();
            if (group >= 0) {
                // Użyj modulo, aby cyklicznie przechodzić przez kolory, jeśli jest więcej grup niż kolorów
                g.setColor(groupColors[group % groupColors.length]);
            } else {
                // Domyślny kolor dla węzłów bez grupy
                g.setColor(Color.BLACK);
            }
            g.fillOval(node.getX(), node.getY(), 15, 15);

            // Narysuj numer węzła dla odniesienia
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(node.getGroup()), node.getX() + 5, node.getY() + 12);
        }

        // Odkomentuj do debugowania
        for (Node node : nodes) node.printNode();
    }
}
