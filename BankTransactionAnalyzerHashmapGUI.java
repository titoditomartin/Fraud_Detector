import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

public class BankTransactionAnalyzerHashmapGUI {
    private JFrame frame;
    private JTextArea outputTextArea;
    private JButton analyzeButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BankTransactionAnalyzerHashmapGUI window = new BankTransactionAnalyzerHashmapGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public BankTransactionAnalyzerHashmapGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Fraud Bank Transaction Detector");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        analyzeButton = new JButton("Analyze Transactions");
        analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    analyzeTransactions(selectedFile);
                }
            }
        });
        contentPane.add(analyzeButton, BorderLayout.CENTER);
    }

    private void analyzeTransactions(File file) {
        try {
            Scanner scanner = new Scanner(file);

            Map<String, Integer> locationFrequency = new HashMap<>();
            Set<String> potentialFraudAmount = new HashSet<>();

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

            // Create a new JFrame for the output
            JFrame outputFrame = new JFrame();
            outputFrame.setTitle("Fraud Bank Transaction Detector - Output");
            outputFrame.setBounds(100, 100, 500, 400);
            outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel outputContentPane = new JPanel();
            outputContentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            outputFrame.setContentPane(outputContentPane);
            outputContentPane.setLayout(new BorderLayout(0, 0));

            outputTextArea = new JTextArea();
            outputTextArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(outputTextArea);
            outputContentPane.add(scrollPane, BorderLayout.CENTER);

            outputTextArea.setText(output.toString());

            outputFrame.setVisible(true);

            // Add further logic to take action based on the detected potential fraud

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
