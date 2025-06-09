import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseData
{

    public static void convertBinaryToText(String binaryFilePath, String textFilePath) {
        try (FileInputStream fis = new FileInputStream(binaryFilePath);
             DataInputStream dis = new DataInputStream(fis);
             PrintWriter writer = new PrintWriter(new FileWriter(textFilePath))) {



            byte res = dis.readByte();
            writer.print((char)res + " ");


            int parts = readIntLittleEndian(dis);
            writer.print(parts + " ");


            int cutCount = readIntLittleEndian(dis);
            writer.print(cutCount + " ");


            int marginKept = readIntLittleEndian(dis);
            writer.println(marginKept);


            int maxVertices = readIntLittleEndian(dis);
            writer.println(maxVertices);


            int rCount = readIntLittleEndian(dis);
            for (int i = 0; i < rCount; i++) {
                int value = readIntLittleEndian(dis);
                writer.print(value);
                if (i < rCount - 1) writer.print(";");
            }
            writer.println();


            int fCount = readIntLittleEndian(dis);
            for (int i = 0; i < fCount; i++) {
                int value = readIntLittleEndian(dis);
                writer.print(value);
                if (i < fCount - 1) writer.print(";");
            }
            writer.println();


            long gCount = readLongLittleEndian(dis);
            for (long i = 0; i < gCount; i++) {
                int value = readIntLittleEndian(dis);
                writer.print(value);
                if (i < gCount - 1) writer.print(";");
            }
            writer.println();


            long pCount = readLongLittleEndian(dis);
            for (long i = 0; i < pCount; i++) {
                int value = readIntLittleEndian(dis);
                writer.print(value);
                if (i < pCount - 1) writer.print(";");
            }
            writer.println();

        } catch (IOException e) {
            System.err.println("Błąd podczas konwersji pliku: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static int readIntLittleEndian(DataInputStream dis) throws IOException {
        byte[] bytes = new byte[4];
        dis.readFully(bytes);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    // Pomocnicza metoda do odczytu long w little-endian (dla size_t)
    private static long readLongLittleEndian(DataInputStream dis) throws IOException {
        byte[] bytes = new byte[8];
        dis.readFully(bytes);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public static GraphData readFile(String file_path)
    {
        try(BufferedReader br = new BufferedReader(new FileReader(file_path)))
        {
            String line1 = br.readLine();

            if(line1.startsWith("S") || line1.startsWith("F"))
            {
                String line2 = br.readLine();

                int size = Integer.parseInt(line2);
                String[] parts = line1.trim().split("\\s+");

                int groups = Integer.parseInt(parts[1]);
                return readGraph(br, groups);
            }

            else
            {

                return readGraph(br, 0);
            }
        }
        catch(IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return null;
    }

    private static GraphData readGraph(BufferedReader br, int groups) throws IOException
    {
        String line1 = br.readLine();
        String line2 = br.readLine();

        Scanner sc1 = new Scanner(line1);
        Scanner sc2 = new Scanner(line2);

        sc1.useDelimiter(";");
        sc2.useDelimiter(";");

        int first = sc2.nextInt();
        int node_counter = 0;
        int x = 0;

        ArrayList<Node> nodes = new ArrayList<>();

        while(sc2.hasNextInt())
        {
            int last = sc2.nextInt();

            for(int i = first; i < last; i++)
            {
                if(sc1.hasNextInt())
                {
                    int y = sc1.nextInt();
                    Node node = new Node(x, y, -1, node_counter);
                    nodes.add(node);
                    node_counter++;
                }
            }

            x++;
            first = last;
        }

        nodes.trimToSize();

        sc1.close();
        sc2.close();

        line1 = br.readLine();
        line2 = br.readLine();

        sc1 = new Scanner(line1);
        sc2 = new Scanner(line2);

        sc1.useDelimiter(";");
        sc2.useDelimiter(";");

        ArrayList<ArrayList<Integer>> adjacency_matrix = new ArrayList<>(node_counter);

        for(int i = 0; i < node_counter; i++) {
            ArrayList<Integer> row = new ArrayList<>(node_counter);
            for (int j = 0; j < node_counter; j++) {
                row.add(0);
            }
            adjacency_matrix.add(row);
        }

        first = sc2.nextInt();
        int y;

        while(sc2.hasNextInt())
        {
            int last = sc2.nextInt();
            x = sc1.nextInt();

            //poprawnic zeby odczytywalo polaczenia ostatniego wierzcholka!!!
            for(int i = first; i < last-1; i++)
            {
                if(sc1.hasNextInt())
                {
                    y = sc1.nextInt();
                    adjacency_matrix.get(x).set(y, 1);
                    adjacency_matrix.get(y).set(x, 1);
                }
            }
            first = last;
        }

        sc1.close();
        sc2.close();

        return new GraphData(nodes, adjacency_matrix, groups);
    }
}
