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
            return new Dimension(800, 600); // Domyślny rozmiar, jeśli nie ma węzłów
        }

        // Znajdź maksymalne współrzędne x i y wśród wszystkich węzłów
        int maxX = 0;
        int maxY = 0;
        for (Node node : nodes) {
            maxX = Math.max(maxX, node.getX());
            maxY = Math.max(maxY, node.getY());
        }

        // Dodaj rozmiar węzła (20x20) i trochę marginesu
        return new Dimension(maxX + 20 + 50, maxY + 20 + 50);
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

        // Rysuj krawędzie między połączonymi węzłami
        for (int i = 0; i < nodes.size(); i++) {
            Node node1 = nodes.get(i);
            // Pobierz punkt środkowy węzła
            int x1 = node1.getX() + 10;
            int y1 = node1.getY() + 10;

            // Ustaw kolor krawędzi na podstawie grupy węzła źródłowego
            int group = node1.getGroup();
            if (group >= 0) {
                // Użyj modulo, aby cyklicznie przechodzić przez kolory, jeśli jest więcej grup niż kolorów
                g.setColor(groupColors[group % groupColors.length]);
            } else {
                // Domyślny kolor dla węzłów bez grupy
                g.setColor(Color.BLACK);
            }

            // Rysuj linie do wszystkich połączonych węzłów
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
            // Zwiększ rozmiar węzła z 15x15 do 20x20
            g.fillOval(node.getX(), node.getY(), 20, 20);

            // Narysuj numer węzła dla odniesienia
            // Dostosuj pozycję etykiety, aby pasowała do nowego rozmiaru węzła
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(node.getNr()), node.getX() + 7, node.getY() + 15);
        }

        // Sekcja debugowania: Wypisuje informacje o wszystkich węzłach do konsoli
        // Pomaga w debugowaniu pozycji węzłów i przypisań do grup
        //for (Node node : nodes) node.printNode();
    }
}
