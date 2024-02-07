package AdminPanel;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class AdminDashboard extends JFrame {
    private static final int CUSTOMER_DETAILS_WIDTH = 300;
    private static final int TUNING_MENU_WIDTH = 150;
    private static final int RECEIPT_WIDTH = 600; // Increased width for Receipt
    private static final int BUTTONS_WIDTH = 75;
    private HashMap<String, Double> checkBoxAmounts;

    public AdminDashboard() {
        checkBoxAmounts = new HashMap<>();
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

        // Create a rectangle box at the top with the title "Garage Genius"
        JPanel topRectangle = createRectanglePanel(0, 0, getWidth(), 100, "Admin Garage Genius");
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
        JPanel customerDetailsRectangle = createTitledRectanglePanel(0, 100, CUSTOMER_DETAILS_WIDTH, getHeight() - 100, "Employee Payroll");
        customerDetailsRectangle.setLayout(new GridLayout(0, 2, 1, 60)); // Adjust as needed

        // Add labels and text fields with recommended size
        String[] labels = {"Employee ID:", "Employee Name:", "Employee Salary:", "Deduction", "Bonus Amount:"};
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(170, 50));
            JTextField jTextField = new JTextField();
            jTextField.setPreferredSize(new Dimension(160, 20)); // Set the recommended size
            customerDetailsRectangle.add(jLabel);
            customerDetailsRectangle.add(jTextField);
        }

        // Add "Check" and "Pay" buttons
        JButton checkButton = new JButton("Check");
        JButton payButton = new JButton("Pay");

        checkButton.addActionListener(e -> {
            // Your logic for the "Check" button
            // For example, display a message or perform some checks
            JOptionPane.showMessageDialog(this, "Checking employee details");
        });

        payButton.addActionListener(e -> {
            // Your logic for the "Pay" button
            // For example, process payroll and display a message
            JOptionPane.showMessageDialog(this, "Processing payroll");
        });

        customerDetailsRectangle.add(checkButton);
        customerDetailsRectangle.add(payButton);

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
        String[] buttonLabels = {"Order", "Print", "Reset", "Save PDF", "Monthly Expenditure"};
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
//                jButton.addActionListener(e -> printReceipt(receiptTextArea));
            } else if (buttonLabel.equals("Monthly Expenditure")) {
//                jButton.addActionListener(e -> ServiceBooking.showServiceBookingWindow(this));
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



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }

}