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

       ArrayList<Node> nodes = File.readFile(file_path);

       //Dodanie panelu do rysowania
       GraphPanel panel = new GraphPanel(nodes);
       Okno.add(panel);

       Okno.setVisible(true);
   }
}
