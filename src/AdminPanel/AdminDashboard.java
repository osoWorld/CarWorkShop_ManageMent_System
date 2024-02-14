package AdminPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.print.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboard extends JFrame {
    private static final int CUSTOMER_DETAILS_WIDTH = 300;
    private static final int TUNING_MENU_WIDTH = 150;
    private static final int RECEIPT_WIDTH = 600; // Increased width for Receipt
    private static final int BUTTONS_WIDTH = 75;
    private final HashMap<String, Double> checkBoxAmounts;
    private final Map<String, JTextField> employeeDetailsMap; // to store employee details text fields

    public AdminDashboard() {
        checkBoxAmounts = new HashMap<>();
        employeeDetailsMap = new HashMap<>();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Garage Genius Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to be maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        // Set background color to white
        getContentPane().setBackground(Color.WHITE);

        // Create a rectangle box at the top
        JPanel topRectangle = createRectanglePanel(0, 0, getWidth(), 100, "Admin Garage Genius");
        topRectangle.setLayout(new BorderLayout());

        // Add an icon
        JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/garage_icon.png"))); // Replace with your icon file
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(iconLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Admin Garage Genius");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(titleLabel, BorderLayout.SOUTH);

        add(topRectangle, BorderLayout.NORTH);

        // Create the "Customer Details" rectangle
        JPanel customerDetailsRectangle = createTitledRectanglePanel(0, 100, CUSTOMER_DETAILS_WIDTH, getHeight() - 100, "Employee Payroll");
        customerDetailsRectangle.setLayout(new GridLayout(0, 2, 1, 40)); // Adjust as needed

        // Add labels and text fields
        String[] labels = {"Employee ID:", "Employee Name:", "Employee Salary:", "Deduction", "Bonus Amount:", "Total Salary:"};
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(170, 50));
            JTextField jTextField = new JTextField();
            jTextField.setPreferredSize(new Dimension(160, 35)); // Set the recommended size
            customerDetailsRectangle.add(jLabel);
            customerDetailsRectangle.add(jTextField);

            if (label.equals("Total Salary:") || label.equals("Employee ID:") || label.equals("Employee Salary:")) {
                jTextField.setEditable(false);
            }

            employeeDetailsMap.put(label, jTextField);
        }

        // Add "Check" and "Pay" buttons
        JButton checkButton = new JButton("Check");
        JButton payButton = new JButton("Pay");

        JButton totalButton = new JButton("Total Salary");
        JButton addEmployee = new JButton("Add A New Employee");

        checkButton.addActionListener(e -> {

            getEmployeeDataFromDatabase();
        });

        payButton.addActionListener(e -> {
            payCheckSend();

        });

        totalButton.addActionListener(e -> {

            double totalSalary = calculateTotalSalary();
            JTextField totalSalaryField = employeeDetailsMap.get("Total Salary:");
            totalSalaryField.setText(String.format("%.2f", totalSalary));
        });

        addEmployee.addActionListener(e -> {

        });

        customerDetailsRectangle.add(checkButton);
        customerDetailsRectangle.add(payButton);
        customerDetailsRectangle.add(totalButton);
        customerDetailsRectangle.add(addEmployee);

        add(customerDetailsRectangle, BorderLayout.WEST);

        // Create the "Tuning Menu" rectangle
        JPanel tuningMenuRectangle = createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH, 100, TUNING_MENU_WIDTH, getHeight() - 100, "Restock Items");
        tuningMenuRectangle.setLayout(new GridLayout(8, 3, 1, 20));     // Adjust as needed

        // Add checkboxes
        String[] checkboxes = {"Stereo System", "Paint", "GPS", "Leather Coating", "Rims", "Tyres", "Exhaust", "Alignment", "Oil Change"
                , "Battery Replacement", "Brake repair", "Catalytic converter", "Transmission repair", "Wiring System", "Thermostat replacement"
                , "A/C repair", "Timing belt", "Ignition system", "Dent repair", "Panel replacement", "Fluid checks", "Air Filters"
                , "Climate control", "Engine repair"};

        for (String checkbox : checkboxes) {
            JCheckBox checkBoxes = new JCheckBox(checkbox);
            checkBoxes.setPreferredSize(new Dimension(100, 60));

            checkBoxesAmount(checkbox);

            tuningMenuRectangle.add(checkBoxes);
        }

        add(tuningMenuRectangle, BorderLayout.CENTER);

        // Create the "Receipt" rectangle
        JPanel receiptRectangle = createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH + TUNING_MENU_WIDTH, 100, RECEIPT_WIDTH, getHeight() - 100, "Items Receipt");
        receiptRectangle.setLayout(new BorderLayout());

        // Create a JTextArea for receipt details
        JTextPane receiptTextArea = new JTextPane();
        receiptTextArea.setPreferredSize(new Dimension(280, 80));
        receiptTextArea.setEditable(false);

        // Add JTextArea to a JScrollPane for scrolling if needed
        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        receiptRectangle.add(scrollPane, BorderLayout.CENTER);

        add(receiptRectangle, BorderLayout.EAST);

        // Create the "Buttons" rectangle
        JPanel buttonsRectangle = createRectanglePanel(CUSTOMER_DETAILS_WIDTH + TUNING_MENU_WIDTH + RECEIPT_WIDTH, getHeight() / 2, BUTTONS_WIDTH, getHeight() / 2, "");
        buttonsRectangle.setLayout(new GridLayout(1, 4, 10, 10)); // Adjust as needed

        // Add buttons
        String[] buttonLabels = {"Order", "Print", "Reset", "Save PDF", "Monthly Summary"};
        for (String buttonLabel : buttonLabels) {
            JButton jButton = new JButton(buttonLabel);
            jButton.setPreferredSize(new Dimension(60, 74));
            jButton.setFont(new Font("Arial", Font.BOLD, 21));
            buttonsRectangle.add(jButton);

            if (buttonLabel.equals("Order")) {
                jButton.addActionListener(e -> calculateTotal(tuningMenuRectangle, receiptTextArea));
            } else if (buttonLabel.equals("Reset")) {
                jButton.addActionListener(e -> resetAll(customerDetailsRectangle, tuningMenuRectangle, receiptTextArea));
            } else if (buttonLabel.equals("Print")) {
                jButton.addActionListener(e -> printItemsReceipt(receiptTextArea));
            } else if (buttonLabel.equals("Monthly Summary")) {
                jButton.addActionListener(e -> {
                    MonthlySummary summary = new MonthlySummary();
                    summary.generateMonthlySummary();
                });
            }

        }

        add(buttonsRectangle, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createRectanglePanel(int x, int y, int width, int height, String title) {
        JPanel rectanglePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };

        rectanglePanel.setBounds(x, y, width, height);
        rectanglePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adjust as needed

        if (!title.isEmpty()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            rectanglePanel.add(titleLabel, BorderLayout.NORTH);
        }

        return rectanglePanel;
    }

    private JPanel createTitledRectanglePanel(int x, int y, int width, int height, String title) {
        JPanel titledRectanglePanel = new JPanel();
        titledRectanglePanel.setBounds(x, y, width, height);
        titledRectanglePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)));

        return titledRectanglePanel;
    }

    private void checkBoxesAmount(String checkbox) {
        if (checkbox.equals("Stereo System")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Paint")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("GPS")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Leather Coating")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Rims")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Tyres")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Exhaust")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Alignment")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Oil Change")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Battery Replacement")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Brake repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Catalytic converter")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Transmission repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Wiring System")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Thermostat replacement")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("A/C repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Timing belt")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Ignition system")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Dent repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Panel replacement")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Fluid checks")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Air Filters")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Climate control")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Engine repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else {
            checkBoxAmounts.put(checkbox, 10.0);
        }
    }

    private void calculateTotal(JPanel tuningMenuRectangle, JTextPane textReceiptPane) {
        double total = 0.0;

        for (Component component : tuningMenuRectangle.getComponents()) {
            if (component instanceof JCheckBox checkBox && checkBox.isSelected()) {
                String checkBoxText = checkBox.getText();
                if (checkBoxAmounts.containsKey(checkBoxText)) {
                    total += checkBoxAmounts.get(checkBoxText);
                }
            }
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // Create a styled document
        StyledDocument styledDocument = textReceiptPane.getStyledDocument();

        // Clear previous content
        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), textReceiptPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        // Create different styles
        Style defaultStyle = textReceiptPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style largeStyle = textReceiptPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(largeStyle, 18); // Adjust the size as needed
        StyleConstants.setBold(largeStyle, true);

        Style totalStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(totalStyle, true);
        StyleConstants.setFontSize(totalStyle, 15);

        Style headingStyle = textReceiptPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(headingStyle, true);
        StyleConstants.setFontSize(headingStyle, 14);

        Style spacing = textReceiptPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(spacing, 2);

        // Insert text with different styles
        try {
            styledDocument.insertString(styledDocument.getLength(), "\n     Admin Garage Genius\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "------------------------------------------------------------\n", defaultStyle);

            // Insert selected checkboxes and their prices
            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Refill Stock :\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);

            for (Component component : tuningMenuRectangle.getComponents()) {
                if (component instanceof JCheckBox checkBox && checkBox.isSelected()) {
                    String checkBoxText = checkBox.getText();
                    if (checkBoxAmounts.containsKey(checkBoxText)) {
                        double checkBoxPrice = checkBoxAmounts.get(checkBoxText);
                        styledDocument.insertString(styledDocument.getLength(), " " + checkBoxText + " - $ " + checkBoxPrice + "\n", defaultStyle);
                        total += checkBoxPrice;
                    }
                }
            }

            styledDocument.insertString(styledDocument.getLength(), "------------------------------------------------------------\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "   Total : ", totalStyle);
            styledDocument.insertString(styledDocument.getLength(), String.format("%.2f", total) + "\n", totalStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n\n " + formattedDateTime, defaultStyle);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void resetAll(JPanel customerDetailRectangle, JPanel tuningMenuRectangle, JTextPane textReceiptArea) {

        for (Component component : tuningMenuRectangle.getComponents()) {
            if (component instanceof JCheckBox checkBox) {
                checkBox.setSelected(false);
            }
        }

        textReceiptArea.setText("");

        for (Component component : customerDetailRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

    }

    private void payCheckSend() {
        StringBuilder employeeDetails = new StringBuilder("Employee Details:\n\n");
        for (Map.Entry<String, JTextField> entry : employeeDetailsMap.entrySet()) {
            String label = entry.getKey();
            String value = entry.getValue().getText();
            employeeDetails.append(label).append(": ").append(value).append("\n");
        }

        // Append Bonus Amount to the employee details
        String bonusAmount = employeeDetailsMap.get("Bonus Amount:").getText();
        employeeDetails.append("Bonus Amount: ").append(bonusAmount).append("\n");

        // Get the current date, day, and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE)");
        String formattedDate = now.format(dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = now.format(timeFormatter);

        // Append the current date, day, and time to the employee details
        employeeDetails.append("Date: ").append(formattedDate).append("\n");
        employeeDetails.append("Time: ").append(formattedTime).append("\n");

        // Get the current month and year
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String currentMonthYear = now.format(monthYearFormatter);

        // Append the current month and year to the employee details
        employeeDetails.append("Salary Month: ").append(currentMonthYear).append("\n");

        // Open the EmployeePayment window with the employee details
        new EmployeePayment(employeeDetails.toString());

        addValuesInDatabase(bonusAmount, formattedDate, formattedTime, currentMonthYear);
    }

    private void printItemsReceipt(JTextPane textReceiptArea) {

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

    private void getEmployeeDataFromDatabase() {
        String employeeName = employeeDetailsMap.get("Employee Name:").getText();

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
             Statement statement = connection.createStatement()) {

            String query = "SELECT [Employee ID], [Employee Salary] FROM Employee_Receipt WHERE [Employee Name] = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, employeeName);
                ResultSet resultSet = preparedStatement.executeQuery();
                // Process the result set
                while (resultSet.next()) {
                    String employeeID = resultSet.getString("Employee ID");
                    String employeeSalary = resultSet.getString("Employee Salary");
                    // Set the retrieved values to your JTextFields
                    employeeDetailsMap.get("Employee ID:").setText(employeeID);
                    employeeDetailsMap.get("Employee Salary:").setText(employeeSalary);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the SQL exception appropriately
        }
    }

    private double calculateTotalSalary() {
        double employeeSalary = getDoubleValue(employeeDetailsMap.get("Employee Salary:"));
        double deductionAmount = getDoubleValue(employeeDetailsMap.get("Deduction"));
        double bonusAmount = getDoubleValue(employeeDetailsMap.get("Bonus Amount:"));

        // Calculate total salary
        double totalSalary = employeeSalary - deductionAmount + bonusAmount;

        return totalSalary;
    }

    private double getDoubleValue(JTextField textField) {
        String text = textField.getText().trim();
        if (!text.isEmpty() && text.matches("^\\d*\\.?\\d*$")) {
            return Double.parseDouble(text);
        }
        return 0.0;
    }

    private void addValuesInDatabase(String bonusAmount, String formattedDate, String formattedTime, String currentMonthYear) {
        // insert the values into the Employee_Receipt table
        String employeeName = employeeDetailsMap.get("Employee Name:").getText();
        String employeeID = employeeDetailsMap.get("Employee ID:").getText();
        String deduction = employeeDetailsMap.get("Deduction").getText();
        String totalSalary = employeeDetailsMap.get("Total Salary:").getText();

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
             Statement statement = connection.createStatement()) {

            // Check if there is an entry for the current month
            String checkQuery = "SELECT * FROM Employee_Receipt WHERE [Employee Name] = ? AND [Salary Month] = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, employeeName);
                checkStatement.setString(2, currentMonthYear);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Entry for the current month already exists
                    JOptionPane.showMessageDialog(null, "An entry for the current month already exists for employee: " + employeeName);
                } else {
                    // Insert a new row for the current month
                    String insertQuery = "INSERT INTO Employee_Receipt ([Employee ID], [Employee Name], [Deduction], [Bonus Amount], [Paying Amount], [Date], [Time], [Salary Month]) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, employeeID);
                        insertStatement.setString(2, employeeName);
                        insertStatement.setString(3, deduction);
                        insertStatement.setString(4, bonusAmount);
                        insertStatement.setString(5, totalSalary);
                        insertStatement.setString(6, formattedDate);
                        insertStatement.setString(7, formattedTime);
                        insertStatement.setString(8, currentMonthYear);

                        int rowsAffected = insertStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "New entry added to Employee_Receipt table.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to add new entry to Employee_Receipt table.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}