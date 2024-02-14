package Dashboard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.print.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class MainDashboard extends JFrame {
    private static final int CUSTOMER_DETAILS_WIDTH = 300;
    private static final int TUNING_MENU_WIDTH = 150;
    private static final int RECEIPT_WIDTH = 600;   // Increased width for Receipt
    private static final int BUTTONS_WIDTH = 75;
    private HashMap<String, Double> checkBoxAmounts;
    private int customerIdCounter = 56;
    private JPanel customerDetailsRectangle;

    private JTextField customerIdField;

    public MainDashboard() {
        checkBoxAmounts = new HashMap<>();
        initializeUI();

        customerIdCounter = getLastCustomerIdFromDatabase() + 1;
        customerIdField.setText(String.valueOf(customerIdCounter));
        customerIdField.setEditable(false);

    }

    private void initializeUI() {
        setTitle("Garage Genius Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to be maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        // Set background color to white
        getContentPane().setBackground(Color.WHITE);

        // Create a rectangle box at the top with the title "Garage Genius"
        JPanel topRectangle = createRectanglePanel(0, 0, getWidth(), 100, "Garage Genius");
        topRectangle.setLayout(new BorderLayout());

        // Add an icon (assuming you have an ImageIcon named garageIcon)
        JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/garage_icon.png"))); // Replace with your icon file
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(iconLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Garage Genius");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(titleLabel, BorderLayout.SOUTH);

        add(topRectangle, BorderLayout.NORTH);

        // Create the "Customer Details" rectangle
        customerDetailsRectangle = createTitledRectanglePanel(0, 100, CUSTOMER_DETAILS_WIDTH, getHeight() - 100, "Customer Details");
        customerDetailsRectangle.setLayout(new GridLayout(0, 2, 1, 60)); // Adjust as needed

        // Add labels and text fields with recommended size
        String[] labels = {"Customer ID:", "First Name:", "Surname:", "CNIC Number:", "Deposit Amount:"};
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
//            jLabel.setPreferredSize(new Dimension(170,50));
            if (label.equals("Customer ID:")) {
                customerIdField = new JTextField();
                customerIdField.setPreferredSize(new Dimension(160, 20));
                customerDetailsRectangle.add(jLabel);
                customerDetailsRectangle.add(customerIdField);
            } else {
                JTextField jTextField = new JTextField();
                jTextField.setPreferredSize(new Dimension(160, 20)); // Set the recommended size
                customerDetailsRectangle.add(jLabel);
                customerDetailsRectangle.add(jTextField);
            }

        }

        add(customerDetailsRectangle, BorderLayout.WEST);

        // Create the "Tuning Menu" rectangle
        JPanel tuningMenuRectangle = createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH, 100, TUNING_MENU_WIDTH, getHeight() - 100, "Tuning Menu");
        tuningMenuRectangle.setLayout(new GridLayout(8, 3, 1, 20)); // Adjust as needed

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
        JPanel receiptRectangle = createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH + TUNING_MENU_WIDTH, 100, RECEIPT_WIDTH, getHeight() - 100, "Receipt");
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
        String[] buttonLabels = {"Total", "Print", "Reset", "Save PDF", "Service Booking"};
        for (String buttonLabel : buttonLabels) {
            JButton jButton = new JButton(buttonLabel);
            jButton.setPreferredSize(new Dimension(60, 74));
            jButton.setFont(new Font("Arial", Font.BOLD, 21));
            buttonsRectangle.add(jButton);

            if (buttonLabel.equals("Total")) {
                jButton.addActionListener(e -> calculateTotal(tuningMenuRectangle, receiptTextArea, customerDetailsRectangle));
            } else if (buttonLabel.equals("Reset")) {
                jButton.addActionListener(e -> resetAll(customerDetailsRectangle, tuningMenuRectangle, receiptTextArea));
            } else if (buttonLabel.equals("Print")) {
                jButton.addActionListener(e -> printReceipt(receiptTextArea));
            } else if (buttonLabel.equals("Service Booking")) {
                jButton.addActionListener(e -> ServiceBooking.showServiceBookingWindow(this));
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

    private void calculateTotal(JPanel tuningMenuRectangle, JTextPane textReceiptPane, JPanel customerDetailsRectangle) {
        double total = 0.0;

        customerIdField.setText(String.valueOf(customerIdCounter));

        JTextField customerIdField = findTextField("Customer ID:", customerDetailsRectangle);
        JTextField firstNameField = findTextField("First Name:", customerDetailsRectangle);
        JTextField surNameField = findTextField("Surname:", customerDetailsRectangle);
        JTextField nicNumberField = findTextField("CNIC Number:", customerDetailsRectangle);
        JTextField depositAmountField = findTextField("Deposit Amount:", customerDetailsRectangle);

        // Auto-increment Customer ID
        customerIdCounter++;

        String customerId = customerIdField.getText();
        String firstName = firstNameField.getText();
        String surName = surNameField.getText();
        String nicNumber = nicNumberField.getText();
        String depositAmountText = depositAmountField.getText();

        double depositAmount = 0.0;
        if (!depositAmountText.isEmpty()) {
            depositAmount = Double.parseDouble(depositAmountText);
            total -= depositAmount;
        }

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
            styledDocument.insertString(styledDocument.getLength(), "\n     Garage Genius\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Customer Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), " Customer Id : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), customerId + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " First Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), firstName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Sur Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), surName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " CNIC Number : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), nicNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Deposit Amount : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), String.format("%.2f", depositAmount) + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

            // Insert selected checkboxes and their prices
            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Selected Services:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);

            StringBuilder selectedServices = new StringBuilder();

            for (Component component : tuningMenuRectangle.getComponents()) {
                if (component instanceof JCheckBox checkBox && checkBox.isSelected()) {
                    String checkBoxText = checkBox.getText();
                    if (checkBoxAmounts.containsKey(checkBoxText)) {
                        double checkBoxPrice = checkBoxAmounts.get(checkBoxText);
                        styledDocument.insertString(styledDocument.getLength(), " " + checkBoxText + " - $ " + checkBoxPrice + "\n", defaultStyle);
                        total += checkBoxPrice;

                        // Append selected services to the StringBuilder
                        selectedServices.append(checkBoxText).append(", ");
                    }
                }
            }

            // Remove the trailing comma and space
            String selectedServicesString = selectedServices.toString().replaceAll(", $", "");

            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Booking Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);

            ServiceBooking.ServiceBookingInfo serviceBookingInfo = ServiceBooking.getServiceBookingInfo();
            String selectedTime = (serviceBookingInfo != null) ? serviceBookingInfo.getSelectedTime() : "";
            String customerPhoneNumber = (serviceBookingInfo != null) ? serviceBookingInfo.getCustomerPhoneNumber() : "";

            if (serviceBookingInfo != null && serviceBookingInfo.isServiceBookingSelected()) {
                String serviceBookingDetails = String.format(" Service Booking :  Yes\n Time: %s\n Customer Ph. Number: %s",
                        serviceBookingInfo.getSelectedTime(), serviceBookingInfo.getCustomerPhoneNumber());

                styledDocument.insertString(styledDocument.getLength(), serviceBookingDetails, defaultStyle);
            }

            styledDocument.insertString(styledDocument.getLength(), "\n----------------------------------------------------\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "   Total : ", totalStyle);
            styledDocument.insertString(styledDocument.getLength(), String.format("%.2f", total) + "\n", totalStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n\n " + formattedDateTime, defaultStyle);


            // Get the current month and year
            DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            String currentMonthYear = now.format(monthYearFormatter);

            //Save to Database
            saveDataToDatabase(customerIdCounter, firstName, surName, nicNumber, depositAmount, selectedServicesString, total, "Booking Details", selectedTime, customerPhoneNumber, total, currentMonthYear, formattedDateTime);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private JTextField findTextField(String labelText, JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel && labelText.equals(((JLabel) component).getText())) {
                int index = panel.getComponentZOrder(component);
                if (index < panel.getComponentCount() - 1) {
                    Component nextComponent = panel.getComponent(index + 1);
                    if (nextComponent instanceof JTextField) {
                        return (JTextField) nextComponent;
                    }
                }
            }
        }
        return null;
    }

    private void resetAll(JPanel customerDetailRectangle, JPanel tuningMenuRectangle, JTextPane textReceiptArea) {

        for (Component component : tuningMenuRectangle.getComponents()) {
            if (component instanceof JCheckBox checkBox) {
                checkBox.setSelected(false);
            }
        }

        textReceiptArea.setText("");

        JTextField customerIdField = findTextField("Customer ID:", customerDetailRectangle);
        // Keep the current customer ID value
        int currentCustomerId = Integer.parseInt(customerIdField.getText());


        for (Component component : customerDetailRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

        // Update the customer ID field with the next value
        customerIdField.setText(String.valueOf(currentCustomerId + 1));

        ServiceBooking.resetServiceBooking();
    }

    private void printReceipt(JTextPane textReceiptArea) {

        String receiptText = textReceiptArea.getText();

        if (!receiptText.isEmpty()) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Billing System");

            // Create a PageFormat with the size of the paper
            PageFormat pageFormat = job.defaultPage();
            Paper paper = new Paper();
            paper.setSize(300, 500); // Adjust your preferred size
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

    private void saveDataToDatabase(int customerId, String firstName, String surName, String nicNumber, double depositAmount, String selectedServices, double serviceAmount, String bookingDetails, String bookingTime, String customerPhoneNumber, double totalBill, String transactionMonth, String billingDate) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
            String sql = "INSERT INTO [Customer_Receipt] ([Customer ID], [First Name], [Sur Name], [CNIC NUMBER], [Deposit Amount], [Selected Services], [Service Amount], [Booking Details], [Booking Time], [Customer Phone Number], [Total Bill], [Transaction Month], [Date]) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, surName);
            preparedStatement.setString(4, nicNumber);
            preparedStatement.setDouble(5, depositAmount);
            preparedStatement.setString(6, selectedServices);
            preparedStatement.setDouble(7, serviceAmount);
            preparedStatement.setString(8, bookingDetails);
            preparedStatement.setString(9, bookingTime);
            preparedStatement.setString(10, customerPhoneNumber);
            preparedStatement.setDouble(11, totalBill);
            preparedStatement.setString(12, transactionMonth);
            preparedStatement.setString(13, billingDate);

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data saved to database successfully!", "Database", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException sqlE) {
            throw new RuntimeException(sqlE);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getLastCustomerIdFromDatabase() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
            String sql = "SELECT MAX([Customer ID]) FROM [CUSTOMER_RECEIPT]";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException sqlE) {
            throw new RuntimeException(sqlE);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Return 0 if there is no customer ID in the database
        return 0;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboard());
    }
}