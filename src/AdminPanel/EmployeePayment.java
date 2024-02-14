package AdminPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.print.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        JButton printButton = new JButton("Print ");
        printButton.setPreferredSize(new Dimension(110,28));

        buttonPanel.add(printButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


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

            // Add current date and day
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE)");
            String formattedDate = now.format(dateFormatter);
            styledDocument.insertString(styledDocument.getLength(), "\nDate: " + formattedDate, defaultStyle);

            // Add current time
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String formattedTime = now.format(timeFormatter);
            styledDocument.insertString(styledDocument.getLength(), "\nTime: " + formattedTime, defaultStyle);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void calculateFinalSalary(){
        try {
            StyledDocument styledDocument = employeeDetailsPane.getStyledDocument();

            // Retrieve the existing employee details
            String employeeDetails = styledDocument.getText(0, styledDocument.getLength());

            // Split the lines
            String[] lines = employeeDetails.split("\\n");

            // Initialize variables
            String employeeId = "";
            String employeeName = "";
            double employeeSalary = 0.0;
            double deductionAmount = 0.0;
            double bonusAmount = 0.0;

            // Extract information from lines
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "Employee ID":
                            employeeId = value;
                            break;
                        case "Employee Name":
                            employeeName = value;
                            break;
                        case "Employee Salary":
                            if (!value.isEmpty() && value.matches("^\\d*\\.?\\d*$")) {
                                employeeSalary = Double.parseDouble(value);
                            }
                            break;
                        case "Deduction":
                            if (!value.isEmpty() && value.matches("^\\d*\\.?\\d*$")) {
                                deductionAmount = Double.parseDouble(value);
                            }
                            break;
                        case "Bonus Amount":
                            if (!value.isEmpty() && value.matches("^\\d*\\.?\\d*$")) {
                                bonusAmount = Double.parseDouble(value);
                            }
                            break;
                        // Add additional cases for other keys as needed
                    }
                }
            }

            // Calculate final salary
            double finalSalary = employeeSalary + bonusAmount - deductionAmount;

            // Insert the final salary information into the JTextPane
            Style defaultStyle = employeeDetailsPane.getStyle(StyleContext.DEFAULT_STYLE);
            Style boldStyle = employeeDetailsPane.addStyle("Bold", defaultStyle);
            StyleConstants.setBold(boldStyle, true);

            styledDocument.insertString(styledDocument.getLength(), "\nEmployee Id:" + employeeId, defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\nEmployee Name:" + employeeName, defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\nEmployee Salary: $" + employeeSalary, defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n\nDeduction: $" + deductionAmount, defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\nBonus Amount: $" + bonusAmount, defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\nFinal Salary: $" + String.format("%.2f", finalSalary), defaultStyle);

            // Update the JTextPane with the modified StyledDocument
            employeeDetailsPane.setStyledDocument(styledDocument);
        } catch (BadLocationException | NumberFormatException ex){
            ex.printStackTrace();
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