import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class VisualiseGraph extends JFrame {

    public static void Visualise() {
        JFrame Okno = new JFrame("Graf");
        Okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Okno.setSize(1600, 950);

        // Panel główny z BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel kontrolny na górze - przekazujemy referencję do mainPanel
        JPanel controlPanel = createControlPanel(mainPanel, Okno);
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Początkowo dodaj tylko panel z komunikatem
        JPanel placeholderPanel = createPlaceholderPanel();
        mainPanel.add(placeholderPanel, BorderLayout.CENTER);

        Okno.add(mainPanel);
        Okno.setVisible(true);
    }

    private static JPanel createPlaceholderPanel() {
        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setBackground(Color.WHITE);
        return placeholder;
    }

    private static JPanel createControlPanel(JPanel mainPanel, JFrame parentWindow) {
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Opcje analizy grafów"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Pierwsza linia - margines i grupy
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Margines (0.0-1.0):"), gbc);

        gbc.gridx = 1;
        JTextField marginField = new JTextField("0.1", 8);
        controlPanel.add(marginField, gbc);

        gbc.gridx = 2;
        controlPanel.add(new JLabel("Liczba grup :"), gbc);

        gbc.gridx = 3;
        JTextField groupField = new JTextField("2", 8);
        controlPanel.add(groupField, gbc);

        // Druga linia - wybór pliku
        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Ścieżka pliku:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField filePathField = new JTextField(20);
        controlPanel.add(filePathField, gbc);

        gbc.gridx = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JButton chooseFileButton = new JButton("Wybierz plik...");
        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

            int result = fileChooser.showOpenDialog((Component) e.getSource());
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        controlPanel.add(chooseFileButton, gbc);




        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;

        JToggleButton formatToggleButton = new JToggleButton("Format: csrrg");
        formatToggleButton.addActionListener(e -> {
            if (formatToggleButton.isSelected()) {
                formatToggleButton.setText("Format: Binarny");
            } else {
                formatToggleButton.setText("Format: Tekstowy");
            }
        });
        formatToggleButton.setEnabled(false);
        controlPanel.add(formatToggleButton, gbc);

        gbc.gridx = 1;
        JToggleButton divideAndVisualizeButton = new JToggleButton("Podziel i zwizualizuj graf");
        divideAndVisualizeButton.addActionListener(e -> {
            if (divideAndVisualizeButton.isSelected()) {
                divideAndVisualizeButton.setText("Zwizualizuj podzielony graf");
                formatToggleButton.setEnabled(true);
                formatToggleButton.setSelected(true);
                formatToggleButton.setText("Format: Binarny");
                marginField.setEnabled(false);
                groupField.setEnabled(false);

            } else {
                divideAndVisualizeButton.setText("Podziel i zwizualizuj graf");
                formatToggleButton.setEnabled(false);
                formatToggleButton.setText("Format: csrrg");
                marginField.setEnabled(true);
                groupField.setEnabled(true);

            }
        });
        controlPanel.add(divideAndVisualizeButton, gbc);

        gbc.gridx = 3;
        JButton runButton = new JButton("RUN");
        runButton.setBackground(Color.GREEN);
        runButton.setOpaque(true);
        controlPanel.add(runButton, gbc);

        // Obsługa przycisków
        runButton.addActionListener(e -> {
            if (validateInputs(filePathField, marginField, formatToggleButton, divideAndVisualizeButton)) {
                if (!divideAndVisualizeButton.isSelected()) {

                    GraphData newData = processGraph(filePathField, marginField, groupField);
                    if (newData != null) {
                        // Dodaj panel z grafem
                        insertGraphPanel(mainPanel, newData, parentWindow);
                        JOptionPane.showMessageDialog((Component) e.getSource(),
                            "Analiza zakończona pomyślnie!\nStosunek usuniętych połączeń: " +
                                    String.format("%.4f", newData.ratio()),
                            "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else {
                    if (formatToggleButton.isSelected()) {
                        ParseData.convertBinaryToText(filePathField.getText(), "output.txt");
                        GraphData newData = visualiseGraph("output.txt");
                        if (newData != null) {

                            // Dodaj panel z grafem
                            insertGraphPanel(mainPanel, newData, parentWindow);
                            JOptionPane.showMessageDialog((Component) e.getSource(),
                                    "Wizualizacja zakończona pomyślnie ",
                                    "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        GraphData newData = visualiseGraph(filePathField.getText());
                        if (newData != null) {

                            // Dodaj panel z grafem
                            insertGraphPanel(mainPanel, newData, parentWindow);
                            JOptionPane.showMessageDialog((Component) e.getSource(),
                                    "Wizualizacja zakończona pomyślnie ",
                                    "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });



        return controlPanel;
    }

    private static void insertGraphPanel(JPanel mainPanel, GraphData graphData, JFrame parentWindow) {
        SwingUtilities.invokeLater(() -> {
            // Usuwa obecny panel i daje nowy z wizualizacją
            Component centerComponent = ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
            if (centerComponent != null) {
                mainPanel.remove(centerComponent);
            }

            GraphPanel graphPanel = new GraphPanel(graphData);

            // Dodaj scroll pane z nowym panelem grafu
            JScrollPane scrollPane = new JScrollPane(graphPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            // Dodaj do centrum głównego panelu
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Odśwież okno
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }

    private static boolean validateInputs(JTextField filePathField, JTextField marginField, JToggleButton fileType, JToggleButton mode ) {
        // Sprawdź ścieżkę pliku
        String filePath = filePathField.getText().trim();
        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Proszę wybrać plik", "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            JOptionPane.showMessageDialog(null, "Wybrany plik nie istnieje", "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if ( (mode.isSelected() && filePath.endsWith(".csrrg")) || (mode.isSelected() && (filePath.endsWith(".txt")) && fileType.isSelected()) || (mode.isSelected() && filePath.endsWith(".bin") && !fileType.isSelected())) {
            JOptionPane.showMessageDialog(null, "Zły format pliku do wykonywanych akcji", "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if ( !mode.isSelected() && !filePath.endsWith(".csrrg") ) {
            JOptionPane.showMessageDialog(null, "Nie poprawny format pliku do podziału", "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Sprawdź margines
        try {
            double margin = Double.parseDouble(marginField.getText());
            if (margin < 0.0 || margin > 1.0) {
                JOptionPane.showMessageDialog(null, "Margines musi być w zakresie 0.0-1.0", "Błąd", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Nieprawidłowy format marginesu", "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public static GraphData visualiseGraph(String filePathField) {

        GraphData graphData = ParseData.readFile(filePathField);

        for (int i = 0; i < Objects.requireNonNull(graphData).getNodes().size(); i++) {
            graphData.getNodes().get(i).setAdjacencyList(graphData.createAdjacencyList(i));
        }

        SpectralData spectralData = new SpectralData(graphData.getNodes());
        spectralData.getDegreeVector().norm();

        if (graphData.getGroups() > 0) {
            int groups = 0;
            for (int j = 0; j < graphData.getNodes().size(); j++) {
                if (graphData.getNodes().get(j).getGroup() == -1 && !graphData.getNodes().get(j).getAdjacencyList().isEmpty() && groups <= graphData.getGroups()) {
                    graphData.inintGroups(graphData.getNodes().get(j), groups);
                    groups++;
                }
            }
            ColorGenerator.setGroupCount(graphData.getGroups());
            return graphData;
        }
        return null;
    }

    private static GraphData processGraph(JTextField filePathField, JTextField marginField, JTextField groupField) {
        try {
            int iterations = 57;
            String filePath = filePathField.getText().trim();
            double margin = Double.parseDouble(marginField.getText());
            int divide = Integer.parseInt(groupField.getText());

            GraphData graphData = ParseData.readFile(filePath);

            for (int i = 0; i < Objects.requireNonNull(graphData).getNodes().size(); i++) {
                graphData.getNodes().get(i).setAdjacencyList(graphData.createAdjacencyList(i));
            }

            SpectralData spectralData = new SpectralData(graphData.getNodes());
            spectralData.getDegreeVector().norm();


            Vector initialVector = new Vector(spectralData.getDegreeVector().size());
            initialVector = initialVector.createInitialVector(spectralData.getDegreeVector());

            Vector prevInitialVector = new Vector(initialVector.size());
            Vector alfaCoeffs = new Vector(initialVector.size());
            Vector betaCoeffs = new Vector(initialVector.size());

            for (int i = 0; i < alfaCoeffs.size(); i++) {
                alfaCoeffs.set(i, 0.0);
                betaCoeffs.set(i, 0.0);
            }

            spectralData.calculateCoeffs(initialVector, prevInitialVector, alfaCoeffs, betaCoeffs, 0, iterations);

            Matrix tridiagonalMatrix = new Matrix(iterations);
            tridiagonalMatrix.createTridiagonalMatrix(alfaCoeffs, betaCoeffs, iterations);

            Matrix orthogonalMatrix = new Matrix(iterations);

            alfaCoeffs = null;
            betaCoeffs = null;

            Vector eigenvalueVector = new Vector(iterations, 0.0);
            eigenvalueVector.createEigenvalueVector(tridiagonalMatrix, orthogonalMatrix, 0);

            tridiagonalMatrix = null;
            orthogonalMatrix = null;

            double eigenvalue = eigenvalueVector.getFirst();

            for (int i = 1; i < eigenvalueVector.size(); i++) {
                if (eigenvalueVector.get(i) < eigenvalue && eigenvalueVector.get(i) > 0) {
                    eigenvalue = eigenvalueVector.get(i);
                }
            }

            for (int i = 0; i < spectralData.getLaplaceMatrix().getValues().size(); i++) {
                if (spectralData.getLaplaceMatrix().getValues().get(i) > 0) {
                    spectralData.getLaplaceMatrix().getValues().set(i,
                            spectralData.getLaplaceMatrix().getValues().get(i) - eigenvalue);
                }
            }

            eigenvalueVector = null;
            eigenvalue = 1.0;

            for (int i = 0; i < spectralData.getLaplaceMatrix().getValues().size(); i++) {
                if (spectralData.getLaplaceMatrix().getValues().get(i) > 0) {
                    spectralData.getLaplaceMatrix().getValues().set(i,
                            spectralData.getLaplaceMatrix().getValues().get(i) - eigenvalue);
                }
            }

            Vector velocity = new Vector(spectralData.getEigenvector().size(), 0.0);
            spectralData.setEigenvector(initialVector);

            do {
                spectralData.calculateEigenvector(velocity, 0.0, 0);
            } while (spectralData.getEpsilon() > spectralData.getEpsilonMargin());

            graphData.setEigenvalues(spectralData.getEigenvector());
            spectralData.sortEigenvector();

            ColorGenerator.setGroupCount(divide);
            graphData.setParameters(divide, margin);
            graphData.assignGroups(spectralData);

            return graphData;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Błąd podczas przetwarzania: " + ex.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}