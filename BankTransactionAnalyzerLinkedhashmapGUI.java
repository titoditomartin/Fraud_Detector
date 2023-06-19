import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

public class BankTransactionAnalyzerLinkedhashmapGUI {
    private JFrame homeFrame;
    private JFrame outputFrame;
    private JTextArea outputTextArea;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BankTransactionAnalyzerLinkedhashmapGUI window = new BankTransactionAnalyzerLinkedhashmapGUI();
                    window.homeFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public BankTransactionAnalyzerLinkedhashmapGUI() {
        initializeHomeFrame();
        initializeOutputFrame();
    }

    private void initializeHomeFrame() {
        homeFrame = new JFrame();
        homeFrame.setTitle("Fraud Bank Transaction Detector");
        homeFrame.setBounds(100, 100, 500, 400);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        homeFrame.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JButton analyzeButton = new JButton("Analyze Transaction");
        analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                int result = fileChooser.showOpenDialog(homeFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    analyzeTransactions(selectedFile);
                    outputFrame.setVisible(true);
                }
            }
        });
        contentPane.add(analyzeButton, BorderLayout.CENTER);
    }

    private void initializeOutputFrame() {
        outputFrame = new JFrame();
        outputFrame.setTitle("Output");
        outputFrame.setBounds(100, 100, 500, 400);
        outputFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outputFrame.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void analyzeTransactions(File file) {
        try {
            Scanner scanner = new Scanner(file);

            Map<String, Integer> locationFrequency = new LinkedHashMap<>();
            Set<String> potentialFraudAmount = new LinkedHashSet<>();

            // Read the transaction data file and calculate location frequencies
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] transactionData = line.split(",");

                // Assuming the location is in the second column
                String location = transactionData[1].trim();

                // Update location frequencies
                if (locationFrequency.containsKey(location)) {
                    locationFrequency.put(location, locationFrequency.get(location) + 1);
                } else {
                    locationFrequency.put(location, 1);
                }

                // Check if the amount exceeds 100,000,000 for the current location
                int transactionAmount = Integer.parseInt(transactionData[2].trim());
                if (transactionAmount > 100000000) {
                    potentialFraudAmount.add(location);
                }
            }

            scanner.close();

            StringBuilder output = new StringBuilder();

            // Detect potential fraudulent transactions based on frequency
            String potentialFraudFrequency = "";
            int maxFrequency = 0;

            for (Map.Entry<String, Integer> entry : locationFrequency.entrySet()) {
                if (entry.getValue() > maxFrequency) {
                    maxFrequency = entry.getValue();
                    potentialFraudFrequency = entry.getKey();
                }
            }

            output.append("\nPotential Fraud Based on Frequency:\n");
            output.append("Location: ").append(potentialFraudFrequency).append("\n");
            output.append("Transaction Frequency: ").append(maxFrequency).append("\n");

            output.append("\nPotential Fraud Based on Amount:\n");
            for (String location : potentialFraudAmount) {
                output.append("Location: ").append(location).append("\n");
            }

            outputTextArea.setText(output.toString());

            // Add further logic to take action based on the detected potential fraud

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
