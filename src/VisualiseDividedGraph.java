import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VisualiseDividedGraph extends JFrame {

   public static void VisualiseGraph(String file_path)
   {
       JFrame Okno = new JFrame("Graf");
       Okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       Okno.setSize(1600, 900);

       GraphData graphData = File.readFile(file_path);
       ArrayList<Node> nodes = graphData.getNodes();
       ArrayList<ArrayList<Integer>> adjacencyList = graphData.getAdjacencyList();

       //Dodanie panelu do rysowania
       GraphPanel panel = new GraphPanel(nodes, adjacencyList);

       //Dodanie suwaka
       JScrollPane scrollPane = new JScrollPane(panel);
       scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

       Okno.add(scrollPane);

       Okno.setVisible(true);
   }
}
