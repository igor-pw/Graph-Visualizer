import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;

public class File
{
    public static ArrayList<Node> readFile(String file_path)
    {
        ArrayList<Node> nodes = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file_path))) {

            String line;
            int row = 1; // Początkowa pozycja y do rysowania tekstu
            int nr = 0;
            int group = -1;

            while ((line = br.readLine()) != null)
            {
                Scanner sc = new Scanner(line);

                int col = 1;

                while(sc.hasNextInt())
                {
                    if(sc.hasNextInt())
                    {
                        int number = sc.nextInt();
                        if(number == 1)
                        {
                            nodes.add(new Node(col * 15, row * 15, group, nr));
                            nr++;
                        }
                        col++;
                    }

                    System.out.println(line);
                }

                col = 0;
                row++;

                if(line.startsWith("grupa"))
                {
                    sc.next(); // Pomiń token "grupa"
                    if (sc.hasNextInt()) {
                        group = sc.nextInt();
                        System.out.println("znaleziono group: " + group);
                    }
                }
                else if(line.contains("-")) // Przetwarzaj linie połączeń
                {
                    String[] parts = line.split("-");
                    if(parts.length == 2) {
                        try {
                            int node1 = Integer.parseInt(parts[0]);
                            int node2 = Integer.parseInt(parts[1]);

                            // Ustaw grupę dla obu węzłów, jeśli istnieją na liście węzłów
                            if(node1 < nodes.size()) {
                                nodes.get(node1).setGroup(group);
                            }
                            if(node2 < nodes.size()) {
                                nodes.get(node2).setGroup(group);
                            }

                            System.out.println("Processing connection: " + node1 + "-" + node2);
                        } catch(NumberFormatException e) {
                            // Pomiń linie, które nie zawierają prawidłowych liczb całkowitych
                            System.out.println("Skipping invalid connection: " + line);
                        }
                    }
                }

            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return nodes;
    }
}
