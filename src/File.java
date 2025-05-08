import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class File
{
    /**
     * Znajduje grupy w grafie na podstawie połączeń między węzłami.
     * Używane, gdy wskaźniki grup nie są jawnie podane w pliku.
     * 
     * @param groupNodes Tablica indeksów węzłów reprezentujących połączenia
     * @return Tablica wskaźników grup
     */
    private static int[] findGroups(int[] groupNodes) {
        if (groupNodes == null || groupNodes.length == 0) {
            return new int[] {0};
        }

        // Debug: Początek procesu automatycznego znajdowania grup
        System.out.println("Finding groups automatically from " + groupNodes.length + " node entries");

        // Używamy prostego podejścia: każda kolejna para węzłów w groupNodes tworzy połączenie
        // Wszystkie połączone węzły będą w tej samej grupie

        // Najpierw budujemy reprezentację grafu jako listę sąsiedztwa
        ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>();
        int maxNodeIndex = -1;

        // Znajdź maksymalny indeks węzła, aby określić rozmiar listy sąsiedztwa
        for (int nodeIndex : groupNodes) {
            maxNodeIndex = Math.max(maxNodeIndex, nodeIndex);
        }

        // Debug: Informacja o maksymalnym indeksie węzła dla określenia rozmiaru listy sąsiedztwa
        System.out.println("Maximum node index found: " + maxNodeIndex);

        // Inicjalizacja listy sąsiedztwa
        for (int i = 0; i <= maxNodeIndex; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        // Dodaj połączenia do listy sąsiedztwa
        int connectionCount = 0;
        for (int i = 0; i < groupNodes.length - 1; i += 2) {
            int node1 = groupNodes[i];
            int node2 = groupNodes[i + 1];

            // Dodaj dwukierunkowe połączenie
            if (node1 <= maxNodeIndex && node2 <= maxNodeIndex) {
                adjacencyList.get(node1).add(node2);
                adjacencyList.get(node2).add(node1);
                connectionCount++;
            }
        }

        // Debug: Informacja o liczbie połączeń dodanych do listy sąsiedztwa
        System.out.println("Added " + connectionCount + " connections to the adjacency list");

        // Użyj DFS do znalezienia spójnych składowych (grup)
        boolean[] visited = new boolean[maxNodeIndex + 1];
        ArrayList<ArrayList<Integer>> groups = new ArrayList<>();

        for (int i = 0; i <= maxNodeIndex; i++) {
            if (!visited[i]) {
                ArrayList<Integer> group = new ArrayList<>();
                dfs(i, adjacencyList, visited, group);
                if (!group.isEmpty()) {
                    groups.add(group);
                }
            }
        }

        // Utwórz wskaźniki grup
        int[] groupPointers = new int[groups.size() + 1];
        groupPointers[0] = 0;

        int index = 0;
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            groupPointers[i + 1] = groupPointers[i] + group.size();

            // Debug: Wyświetl informacje o każdej automatycznie wykrytej grupie
            System.out.println("Group " + i + " contains " + group.size() + " nodes: " + 
                              (group.size() > 0 ? group.get(0) : "") + 
                              (group.size() > 1 ? ", " + group.get(1) : "") + 
                              (group.size() > 2 ? ", " + group.get(2) : "") + 
                              (group.size() > 3 ? ", ..." : ""));

            // Zmień kolejność groupNodes, aby pasowała do struktury grupy
            for (int nodeIndex : group) {
                if (index < groupNodes.length) {
                    groupNodes[index++] = nodeIndex;
                }
            }
        }

        // Debug: Podsumowanie automatycznego wykrywania grup
        System.out.println("Found " + groups.size() + " groups automatically");

        // Debug: Wypisz tablicę wskaźników grup
        System.out.print("Group pointers: ");
        for (int i = 0; i < groupPointers.length; i++) {
            System.out.print(groupPointers[i] + (i < groupPointers.length - 1 ? ", " : ""));
        }
        System.out.println();

        return groupPointers;
    }

    /**
     * Przeszukiwanie w głąb (DFS) do znajdowania spójnych składowych.
     */
    private static void dfs(int node, ArrayList<ArrayList<Integer>> adjacencyList, boolean[] visited, ArrayList<Integer> group) {
        visited[node] = true;
        group.add(node);

        for (int neighbor : adjacencyList.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, adjacencyList, visited, group);
            }
        }
    }

    /**
     * Przeszukiwanie w głąb (DFS) do przypisywania węzłów do grup.
     */
    private static void dfsAssignGroup(int node, ArrayList<ArrayList<Integer>> adjacencyList, boolean[] visited, ArrayList<Node> nodes, int groupId) {
        visited[node] = true;
        nodes.get(node).setGroup(groupId);
        System.out.println("  Assigned node " + node + " to group " + groupId);

        for (int neighbor : adjacencyList.get(node)) {
            if (!visited[neighbor]) {
                dfsAssignGroup(neighbor, adjacencyList, visited, nodes, groupId);
            }
        }
    }

    /**
     * Metoda debugowania: Wypisuje indeksy węzłów należących do każdej grupy w kolejności rosnącej
     * Pomaga w debugowaniu przypisań do grup i weryfikacji poprawności grupowania
     * 
     * @param nodes Lista węzłów do analizy
     */
    public static void printNodeIndicesByGroup(ArrayList<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            System.out.println("No nodes to print");
            return;
        }

        // Znajdź maksymalne ID grupy, aby określić, ile mamy grup
        int maxGroupId = -1;
        for (Node node : nodes) {
            maxGroupId = Math.max(maxGroupId, node.getGroup());
        }

        System.out.println("\n// Debug: Indeksy węzłów według grup (posortowane)");

        // Dla każdej grupy zbierz i posortuj indeksy węzłów
        for (int groupId = 0; groupId <= maxGroupId; groupId++) {
            final int currentGroupId = groupId;

            // Zbierz wszystkie indeksy węzłów należących do tej grupy
            ArrayList<Integer> nodeIndices = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).getGroup() == currentGroupId) {
                    nodeIndices.add(i);
                }
            }

            // Posortuj indeksy węzłów
            java.util.Collections.sort(nodeIndices);

            // Wypisz posortowane indeksy węzłów dla tej grupy
            if (!nodeIndices.isEmpty()) {
                System.out.print("Group " + groupId + " contains " + nodeIndices.size() + " nodes: ");
                for (int i = 0; i < nodeIndices.size(); i++) {
                    System.out.print(nodeIndices.get(i));
                    if (i < nodeIndices.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }

        // Sprawdź węzły bez grupy (group = -1)
        ArrayList<Integer> ungroupedNodeIndices = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getGroup() == -1) {
                ungroupedNodeIndices.add(i);
            }
        }

        // Wypisz węzły bez grupy, jeśli takie istnieją
        if (!ungroupedNodeIndices.isEmpty()) {
            java.util.Collections.sort(ungroupedNodeIndices);
            System.out.print("Ungrouped nodes (" + ungroupedNodeIndices.size() + "): ");
            for (int i = 0; i < ungroupedNodeIndices.size(); i++) {
                System.out.print(ungroupedNodeIndices.get(i));
                if (i < ungroupedNodeIndices.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }
    public static GraphData readFile(String file_path)
    {
        // Sprawdź, czy plik zaczyna się od 'S', co wskazuje na nowy format
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            String firstLine = br.readLine();
            if (firstLine != null && firstLine.startsWith("S")) {
                return readNewFormatFile(file_path);
            } else {
                return readOldFormatFile(file_path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GraphData(new ArrayList<>(), new ArrayList<>());
        }
    }

    private static GraphData readNewFormatFile(String file_path)
    {
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }

            if (lines.size() < 5) {
                System.out.println("Invalid file format: not enough lines");
                return new GraphData(nodes, adjacencyList);
            }

            // Parsuj linię nagłówka (S numGroups width height)
            String[] headerParts = lines.get(0).split(" ");
            if (headerParts.length < 4 || !headerParts[0].equals("S")) {
                System.out.println("Invalid header format");
                return new GraphData(nodes, adjacencyList);
            }

            int numGroups = Integer.parseInt(headerParts[1]);
            int width = Integer.parseInt(headerParts[2]);
            int height = Integer.parseInt(headerParts[3]);

            // Debug: Informacja o sparsowanym nagłówku
            System.out.println("Parsed header: numGroups=" + numGroups + 
                               ", width=" + width + ", height=" + height);

            // Parsuj maksymalną możliwą liczbę węzłów w wierszu
            if (lines.size() < 2) {
                System.out.println("Invalid file format: missing maximum nodes line");
                return new GraphData(nodes, adjacencyList);
            }
            int maxNodes = Integer.parseInt(lines.get(1));
            // Debug: Informacja o maksymalnej liczbie węzłów w wierszu
            System.out.println("Maximum possible nodes in a row: " + maxNodes);

            // Parsuj indeksy węzłów w wierszach
            if (lines.size() < 3) {
                System.out.println("Invalid file format: missing node indices line");
                return new GraphData(nodes, adjacencyList);
            }
            String[] nodeIndicesStr = lines.get(2).split(";");
            int[] nodeIndices = new int[nodeIndicesStr.length];
            for (int i = 0; i < nodeIndicesStr.length; i++) {
                nodeIndices[i] = Integer.parseInt(nodeIndicesStr[i]);
            }
            // Debug: Informacja o liczbie sparsowanych indeksów węzłów
            System.out.println("Parsed " + nodeIndices.length + " node indices");
            if (nodeIndices.length > 0) {
                // Debug: Pokaż przykładowe indeksy węzłów do weryfikacji
                System.out.println("First few node indices: " + 
                                  nodeIndices[0] + 
                                  (nodeIndices.length > 1 ? ", " + nodeIndices[1] : "") + 
                                  (nodeIndices.length > 2 ? ", " + nodeIndices[2] : "") + 
                                  (nodeIndices.length > 3 ? ", " + nodeIndices[3] : "") + 
                                  (nodeIndices.length > 4 ? ", " + nodeIndices[4] : "") + 
                                  (nodeIndices.length > 5 ? ", ..." : ""));
            }

            // Parsuj wskaźniki do pierwszych indeksów
            if (lines.size() < 4) {
                System.out.println("Invalid file format: missing row pointers line");
                return new GraphData(nodes, adjacencyList);
            }
            String[] rowPointersStr = lines.get(3).split(";");
            int[] rowPointers = new int[rowPointersStr.length];
            for (int i = 0; i < rowPointersStr.length; i++) {
                rowPointers[i] = Integer.parseInt(rowPointersStr[i]);
            }
            System.out.println("Parsed " + rowPointers.length + " row pointers");
            if (rowPointers.length > 0) {
                System.out.println("Row pointers: " + 
                                  rowPointers[0] + 
                                  (rowPointers.length > 1 ? ", " + rowPointers[1] : "") + 
                                  (rowPointers.length > 2 ? ", " + rowPointers[2] : "") + 
                                  (rowPointers.length > 3 ? ", " + rowPointers[3] : "") + 
                                  (rowPointers.length > 4 ? ", " + rowPointers[4] : "") + 
                                  (rowPointers.length > 5 ? ", ..." : ""));
            }

            // Parsuj grupy połączonych węzłów
            if (lines.size() < 5) {
                System.out.println("Invalid file format: missing group nodes line");
                return new GraphData(nodes, adjacencyList);
            }
            String[] groupNodesStr = lines.get(4).split(";");
            int[] groupNodes = new int[groupNodesStr.length];
            for (int i = 0; i < groupNodesStr.length; i++) {
                groupNodes[i] = Integer.parseInt(groupNodesStr[i]);
            }
            System.out.println("Parsed " + groupNodes.length + " group node entries");
            if (groupNodes.length > 0) {
                System.out.println("First few group node entries: " + 
                                  groupNodes[0] + 
                                  (groupNodes.length > 1 ? ", " + groupNodes[1] : "") + 
                                  (groupNodes.length > 2 ? ", " + groupNodes[2] : "") + 
                                  (groupNodes.length > 3 ? ", " + groupNodes[3] : "") + 
                                  (groupNodes.length > 4 ? ", " + groupNodes[4] : "") + 
                                  (groupNodes.length > 5 ? ", ..." : ""));
            }

            // Sprawdź węzły grup i parsuj wskaźniki do pierwszych węzłów w grupach
            int[] groupPointers = null;

            if (groupNodes.length == 0) {
                System.out.println("Warning: No group nodes found, all nodes will be in default group -1");
                // Utwórz pojedynczą pustą grupę
                groupPointers = new int[] {0, 0};
            } else if (lines.size() > 5 && !lines.get(5).trim().isEmpty()) {
                String[] groupPointersStr = lines.get(5).split(";");
                groupPointers = new int[groupPointersStr.length];
                for (int i = 0; i < groupPointersStr.length; i++) {
                    groupPointers[i] = Integer.parseInt(groupPointersStr[i]);
                }
                System.out.println("Parsed " + groupPointers.length + " group pointers from file");

                // Sprawdź, czy liczba grup zgadza się z numGroups
                if (groupPointers.length - 1 != numGroups) {
                    System.out.println("Warning: Number of groups in file (" + (groupPointers.length - 1) + 
                                      ") does not match numGroups from header (" + numGroups + ")");
                }
            } else {
                // Jeśli wskaźniki grup nie są podane, musimy użyć numGroups z nagłówka
                System.out.println("No group pointers provided, using numGroups=" + numGroups + " from header");

                // Utwórz wskaźniki grup na podstawie numGroups
                // Podzielimy węzły grup równomiernie między grupy
                groupPointers = new int[numGroups + 1];
                groupPointers[0] = 0;

                int nodesPerGroup = groupNodes.length / numGroups;
                int remainingNodes = groupNodes.length % numGroups;

                for (int i = 0; i < numGroups; i++) {
                    int extraNode = (i < remainingNodes) ? 1 : 0;
                    groupPointers[i + 1] = groupPointers[i] + nodesPerGroup + extraNode;
                }

                System.out.println("Created " + numGroups + " groups with approximately " + 
                                  nodesPerGroup + " nodes per group");
            }

            // Sprawdź wskaźniki wierszy
            if (rowPointers.length == 0) {
                System.out.println("Invalid file format: no row pointers");
                return new GraphData(nodes, adjacencyList);
            }

            // Utwórz węzły na podstawie sparsowanych danych
            // Najpierw utwórz wszystkie węzły z ich pozycjami
            for (int i = 0; i < nodeIndices.length; i++) {
                // Znajdź, do którego wiersza należy ten węzeł
                int row = 0;
                for (int j = 1; j < rowPointers.length; j++) {
                    if (i < rowPointers[j]) {
                        row = j - 1;
                        break;
                    }
                    if (j == rowPointers.length - 1) {
                        row = j;
                    }
                }

                // Oblicz pozycje x i y
                int nodesInCurrentRow = (row < rowPointers.length - 1) ? 
                    rowPointers[row + 1] - rowPointers[row] : 
                    nodeIndices.length - rowPointers[row];

                int positionInRow = i - rowPointers[row];

                // Użyj rzeczywistych indeksów węzłów z formatu
                // nodeIndices[i] zawiera indeks kolumny
                int col = nodeIndices[i]; // Użyj rzeczywistej wartości węzła jako indeksu kolumny
                int x = col * 25 + 25;
                int y = row * 25;

                // Utwórz węzeł z domyślną grupą -1 (zostanie ustawiona później)
                nodes.add(new Node(x, y, -1, i));

                // Debug: Informacja o utworzeniu węzła z informacją o pozycji i wartości
                System.out.println("Created node " + i + " at position (" + x + "," + y + ") in row " + row + 
                                  " (node value: " + nodeIndices[i] + ")");
            }

            // Buduj listę sąsiedztwa z groupNodes (linia 5)
            System.out.println("Building adjacency list from connections...");
            // Wyczyść listę sąsiedztwa i zainicjuj ją ponownie
            adjacencyList.clear();
            for (int i = 0; i < nodes.size(); i++) {
                adjacencyList.add(new ArrayList<>());
            }

            // Parsuj połączenia z groupNodes
            // Na podstawie opisu użytkownika, linia 5 zawiera grupy węzłów połączonych krawędziami
            // Format wydaje się być listą połączeń, gdzie każda wartość reprezentuje węzeł
            // a kolejne wartości reprezentują połączenia
            System.out.println("Parsing connections from groupNodes array of length " + groupNodes.length);

            // Najpierw wydrukujmy kilka pierwszych wartości, aby zrozumieć strukturę
            System.out.print("First few values in groupNodes: ");
            for (int i = 0; i < Math.min(20, groupNodes.length); i++) {
                System.out.print(groupNodes[i] + " ");
            }
            System.out.println();

            // Na podstawie przykładu użytkownika, format wydaje się być:
            // węzeł0, połączony1, połączony2, ..., węzeł1, połączony1, ...
            // Spróbujmy sparsować to w ten sposób
            int currentNode = -1;
            for (int i = 0; i < groupNodes.length; i++) {
                // Sprawdź, czy to nowy węzeł czy połączony węzeł
                if (i == 0 || (i > 0 && groupPointers != null && Arrays.binarySearch(groupPointers, i) >= 0)) {
                    // To jest nowy węzeł
                    currentNode = groupNodes[i];
                    System.out.println("Processing connections for node " + currentNode);
                } else if (currentNode >= 0 && currentNode < nodes.size()) {
                    // To jest połączony węzeł
                    int connectedNode = groupNodes[i];
                    if (connectedNode < nodes.size()) {
                        // Dodaj dwukierunkowe połączenie
                        adjacencyList.get(currentNode).add(connectedNode);
                        adjacencyList.get(connectedNode).add(currentNode);
                        System.out.println("  Added connection: " + currentNode + " <-> " + connectedNode);
                    } else {
                        System.out.println("  Warning: Connected node index out of bounds: " + connectedNode);
                    }
                }
            }

            // Użyj DFS do znalezienia spójnych składowych (grup)
            System.out.println("Finding connected components using DFS...");
            boolean[] visited = new boolean[nodes.size()];
            int groupId = 0;

            // Ogranicz liczbę grup do numGroups z nagłówka
            System.out.println("Using numGroups=" + numGroups + " from header to limit group creation");

            // Najpierw zidentyfikuj izolowane węzły (węzły bez połączeń), ale nie przetwarzaj ich jeszcze
            System.out.println("Identifying isolated nodes (nodes without connections)...");
            ArrayList<Integer> isolatedNodes = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i++) {
                if (adjacencyList.get(i).isEmpty()) {
                    System.out.println("  Node " + i + " is isolated (has no connections)");
                    isolatedNodes.add(i);
                    visited[i] = true; // Oznacz jako odwiedzone, aby pominąć podczas początkowego DFS
                }
            }

            for (int i = 0; i < nodes.size() && groupId < numGroups; i++) {
                if (!visited[i]) {
                    System.out.println("Starting new group " + groupId + " from node " + i);
                    dfsAssignGroup(i, adjacencyList, visited, nodes, groupId);
                    groupId++;
                }
            }

            // Jeśli mamy pozostałe nieodwiedzone węzły, ale już osiągnęliśmy numGroups,
            // przypisz wszystkie pozostałe węzły do ostatniej grupy, ale tylko jeśli mają połączenia
            if (groupId == numGroups) {
                int lastGroupId = numGroups - 1;
                for (int i = 0; i < nodes.size(); i++) {
                    if (!visited[i] && !adjacencyList.get(i).isEmpty()) {
                        System.out.println("Assigning remaining node " + i + " to last group " + lastGroupId);
                        dfsAssignGroup(i, adjacencyList, visited, nodes, lastGroupId);
                    }
                }
            }

            // Teraz przetwórz izolowane węzły
            System.out.println("Processing isolated nodes...");
            for (int isolatedNode : isolatedNodes) {
                if (groupId < numGroups) {
                    // Jeśli nie osiągnęliśmy maksymalnej liczby grup, utwórz nową grupę dla tego izolowanego węzła
                    System.out.println("  Creating new group " + groupId + " for isolated node " + isolatedNode);
                    nodes.get(isolatedNode).setGroup(groupId);
                    groupId++;
                } else {
                    // Jeśli osiągnęliśmy maksymalną liczbę grup, pozostaw grupę jako -1
                    System.out.println("  Maximum number of groups reached, keeping isolated node " + isolatedNode + " with group -1");
                    // Grupa pozostaje -1 jak zainicjowano
                }
            }

            System.out.println("Found " + groupId + " connected components (groups)");

            // Debug: Wypisz indeksy węzłów według grup w kolejności rosnącej
            printNodeIndicesByGroup(nodes);

            // Debug: Podsumowanie załadowanego grafu
            System.out.println("Successfully loaded " + nodes.size() + " nodes with " + 
                               (groupPointers != null ? groupPointers.length - 1 : 0) + " groups");

            // Wypisz indeksy węzłów według grup w kolejności rosnącej
            printNodeIndicesByGroup(nodes);
        } catch (Exception e) {
            System.out.println("Error reading new format file: " + e.getMessage());
            e.printStackTrace();
        }

        return new GraphData(nodes, adjacencyList);
    }

    private static GraphData readOldFormatFile(String file_path)
    {
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>();
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
                            // Uproszczone obliczanie współrzędnych zgodnie z wymaganiem
                            // Użyj col*10 dla x i row*10 dla y
                            nodes.add(new Node(col * 10, row * 10, group, nr));
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
                        // Debug: Informacja o znalezionej grupie w pliku starego formatu
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
                            // Upewnij się, że grupa jest w rozsądnym zakresie (0-9)
                            int validGroup = (group >= 0) ? (group % 10) : group;
                            if (validGroup != group) {
                                // Debug: Informacja o dostosowaniu grupy do prawidłowego zakresu
                                System.out.println("  Adjusted group from " + group + " to " + validGroup);
                            }

                            if(node1 < nodes.size()) {
                                nodes.get(node1).setGroup(validGroup);
                            }
                            if(node2 < nodes.size()) {
                                nodes.get(node2).setGroup(validGroup);
                            }

                            // Debug: Informacja o przetwarzaniu połączenia węzłów
                            System.out.println("Processing connection: " + node1 + "-" + node2);
                        } catch(NumberFormatException e) {
                            // Pomiń linie, które nie zawierają prawidłowych liczb całkowitych
                            // Debug: Informacja o pominięciu nieprawidłowego połączenia
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

        // Wypisz indeksy węzłów według grup w kolejności rosnącej
        printNodeIndicesByGroup(nodes);

        // Inicjalizuj listę sąsiedztwa dla każdego węzła
        for (int i = 0; i < nodes.size(); i++) {
            if (adjacencyList.size() <= i) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        return new GraphData(nodes, adjacencyList);
    }
}
