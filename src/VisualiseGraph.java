import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>Witaj w wizualizatorze grafów!</h2>" +
                "<p>Wybierz plik i naciśnij jeden z przycisków powyżej,<br>" +
                "aby załadować i zwizualizować graf.</p>" +
                "</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(Color.GRAY);

        placeholder.add(messageLabel, BorderLayout.CENTER);
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
        controlPanel.add(new JLabel("Liczba grup (2-15000):"), gbc);

        gbc.gridx = 3;
        JSpinner groupSpinner = new JSpinner(new SpinnerNumberModel(1000, 2, 15000, 1));
        controlPanel.add(groupSpinner, gbc);

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
            fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki tekstowe (*.txt)", "txt"));

            int result = fileChooser.showOpenDialog((Component) e.getSource());
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        controlPanel.add(chooseFileButton, gbc);

        // Trzecia linia - przyciski akcji
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JButton divideAndVisualizeButton = new JButton("Podziel i zwizualizuj graf");
        controlPanel.add(divideAndVisualizeButton, gbc);

        gbc.gridx = 1;
        JButton visualizeDividedButton = new JButton("Zwizualizuj podzielony graf");
        controlPanel.add(visualizeDividedButton, gbc);

        gbc.gridx = 2;
        JToggleButton formatToggleButton = new JToggleButton("Format: Tekstowy");
        formatToggleButton.addActionListener(e -> {
            if (formatToggleButton.isSelected()) {
                formatToggleButton.setText("Format: Binarny");
            } else {
                formatToggleButton.setText("Format: Tekstowy");
            }
        });
        controlPanel.add(formatToggleButton, gbc);

        gbc.gridx = 3;
        JButton runButton = new JButton("RUN");
        runButton.setBackground(Color.GREEN);
        runButton.setOpaque(true);
        controlPanel.add(runButton, gbc);

        // Zmienna do przechowywania przetworzonych danych
        final GraphData[] processedData = {null};

        // Obsługa przycisków
        divideAndVisualizeButton.addActionListener(e -> {
            if (validateInputs(filePathField, marginField)) {
                GraphData newData = processGraph(filePathField, marginField, groupSpinner);
                if (newData != null) {
                    processedData[0] = newData;
                    visualizeDividedButton.setEnabled(true);
                    // Dodaj panel z grafem
                    insertGraphPanel(mainPanel, newData, parentWindow);
                }
            }
        });

        visualizeDividedButton.addActionListener(e -> {
            if (processedData[0] != null) {
                // Dodaj panel z grafem
                insertGraphPanel(mainPanel, processedData[0], parentWindow);
            } else {
                JOptionPane.showMessageDialog((Component) e.getSource(),
                        "Najpierw musisz przetworzyć graf używając 'Podziel i zwizualizuj graf'",
                        "Błąd", JOptionPane.WARNING_MESSAGE);
            }
        });

        runButton.addActionListener(e -> {
            if (validateInputs(filePathField, marginField)) {
                GraphData newData = processGraph(filePathField, marginField, groupSpinner);
                if (newData != null) {
                    processedData[0] = newData;
                    visualizeDividedButton.setEnabled(true);
                    // Dodaj panel z grafem
                    insertGraphPanel(mainPanel, newData, parentWindow);
                    JOptionPane.showMessageDialog((Component) e.getSource(),
                            "Analiza zakończona pomyślnie!\nStosunek usuniętych połączeń: " +
                                    String.format("%.4f", newData.ratio()),
                            "Sukces", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Początkowo wyłącz przycisk wizualizacji podzielonego grafu
        visualizeDividedButton.setEnabled(false);

        return controlPanel;
    }

    private static void insertGraphPanel(JPanel mainPanel, GraphData graphData, JFrame parentWindow) {
        SwingUtilities.invokeLater(() -> {
            // Usuń obecny panel centralny (placeholder lub stary graf)
            Component centerComponent = ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
            if (centerComponent != null) {
                mainPanel.remove(centerComponent);
            }

            // Utwórz nowy panel z grafem - TUTAJ UŻYWAMY GraphData do stworzenia GraphPanel
            GraphPanel graphPanel = new GraphPanel(graphData);  // GraphPanel przyjmuje GraphData

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

    private static boolean validateInputs(JTextField filePathField, JTextField marginField) {
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

    private static GraphData processGraph(JTextField filePathField, JTextField marginField, JSpinner groupSpinner) {
        try {
            int iterations = 57;
            String filePath = filePathField.getText().trim();
            double margin = Double.parseDouble(marginField.getText());
            int divide = (Integer) groupSpinner.getValue();

            GraphData graphData = ParseData.readFile(filePath);

            for (int i = 0; i < Objects.requireNonNull(graphData).getNodes().size(); i++) {
                graphData.getNodes().get(i).setAdjacencyList(graphData.createAdjacencyList(i));
            }

            SpectralData spectralData = new SpectralData(graphData.getNodes());
            spectralData.getDegreeVector().norm();

            if (graphData.getGroups() > 0) {
                int groups = 0;
                for (int j = 0; j < graphData.getNodes().size(); j++) {
                    if (graphData.getNodes().get(j).getGroup() == -1 && !graphData.getNodes().get(j).getAdjacencyList().isEmpty()) {
                        graphData.inintGroups(graphData.getNodes().get(j), groups);
                        groups++;
                    }
                }
                return graphData;
            }

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