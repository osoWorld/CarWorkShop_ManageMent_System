package AdminPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.print.*;

public class EmployeePayment extends JFrame {
    private JTextPane employeeDetailsPane;

    public EmployeePayment(String employeeDetails) {
        initializeUI(employeeDetails);
    }

    private void initializeUI(String employeeDetails) {
        setTitle("Employee Payment");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create titled rectangle panel for the JTextPane
        JPanel detailsPanel = createTitledRectanglePanel(0, 0, getWidth(), getHeight(), "Employee Details");
        detailsPanel.setLayout(new BorderLayout());

        // Use the class-level variable instead of declaring a local one
        employeeDetailsPane = new JTextPane();
        employeeDetailsPane.setEditable(false);
        employeeDetailsPane.setText(employeeDetails);

        JScrollPane scrollPane = new JScrollPane(employeeDetailsPane);
        detailsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the titled rectangle panel to the main panel
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton confirmButton = new JButton("Confirm Payment");
        JButton printButton = new JButton("Print");

        buttonPanel.add(confirmButton);
        buttonPanel.add(printButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(e -> handleConfirmButtonClick());
        printButton.addActionListener(e -> printEmployeeReceipt(employeeDetailsPane));

        add(mainPanel);
        setVisible(true);
    }

    private void handleConfirmButtonClick() {
        // Update the employee details pane to show "Paid" text
        StyledDocument styledDocument = employeeDetailsPane.getStyledDocument();
        Style defaultStyle = employeeDetailsPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = employeeDetailsPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        try {
            styledDocument.insertString(styledDocument.getLength(), "\n\nPayment Status: Paid", boldStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private JPanel createTitledRectanglePanel(int x, int y, int width, int height, String title) {
        JPanel titledRectanglePanel = new JPanel();
        titledRectanglePanel.setBounds(x, y, width, height);
        titledRectanglePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)));

        return titledRectanglePanel;
    }

    private void printEmployeeReceipt(JTextPane textReceiptArea) {

        String receiptText = textReceiptArea.getText();

        if (!receiptText.isEmpty()) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Billing System");
            // Create a PageFormat with the size of the paper
            PageFormat pageFormat = job.defaultPage();
            Paper paper = new Paper();
            paper.setSize(300, 500); // Set your preferred size
            pageFormat.setPaper(paper);

            // Set the printable area to the size of the JTextArea
            double width = pageFormat.getWidth();
            double height = pageFormat.getHeight();
            textReceiptArea.setSize((int) width, (int) height);

            // Create a Printable from the JTextArea
            Printable printable = textReceiptArea.getPrintable(null, null);

            // Set the Printable and PageFormat for the PrinterJob
            job.setPrintable(printable, pageFormat);

            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Example usage:
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeePayment("Sample Employee Details"));
    }
}