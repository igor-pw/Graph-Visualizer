import javax.swing.*;

public class VisualiseGraph extends JFrame {

   public static void Visualise(GraphData graphdata)
   {
       JFrame Okno = new JFrame("Graf");
       Okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       Okno.setSize(1600, 900);

       //Dodanie panelu do rysowania
       GraphPanel panel = new GraphPanel(graphdata);

       //Dodanie suwaka
       JScrollPane scrollPane = new JScrollPane(panel);
       scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


       Okno.add(scrollPane);

       Okno.setVisible(true);
   }
}
