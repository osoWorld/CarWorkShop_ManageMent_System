package AdminPanel;

import UtilsFeatures.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AddNewEmployee extends JFrame {
    private static final int CUSTOMER_DETAILS_WIDTH = 300;
    private static final int TUNING_MENU_WIDTH = 150;
    private static final int RECEIPT_WIDTH = 600;
    private static final int BUTTONS_WIDTH = 75;
    private final Map<String, JTextField> employeeDetailsMap;

    private int employeeCounterId;
    private JTextField employeeIdTextField;

    public AddNewEmployee() {
        employeeCounterId = getLastCustomerIdFromDatabase() + 1;
        employeeDetailsMap = new HashMap<>();
        initializeGUI();

        employeeIdTextField.setText(String.valueOf(employeeCounterId));
        employeeIdTextField.setEditable(false);
    }

    private void initializeGUI() {
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

        JLabel titleLabel = new JLabel("Add New Employee in Admin Garage Genius");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topRectangle.add(titleLabel, BorderLayout.SOUTH);

        add(topRectangle, BorderLayout.NORTH);

        // Create the "Customer Details" rectangle
        JPanel customerDetailsRectangle = createTitledRectanglePanel(0, 100, CUSTOMER_DETAILS_WIDTH, getHeight() - 100, "Person Info");
        customerDetailsRectangle.setLayout(new GridLayout(0, 2, 1, 40)); // Adjust as needed

        // Add labels and text fields
        String[] labels = {"Full Name:", "Father Name:", "Phone Number:", "CNIC Number:", "Reference:", "Experience:"};
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(170, 50));
            JTextField jTextField = new JTextField();
            jTextField.setPreferredSize(new Dimension(160, 35)); // Set the recommended size
            customerDetailsRectangle.add(jLabel);
            customerDetailsRectangle.add(jTextField);

            employeeDetailsMap.put(label, jTextField);
        }

        add(customerDetailsRectangle, BorderLayout.WEST);

        // Create the "Tuning Menu" rectangle
        JPanel tuningMenuRectangle = createTitledRectanglePanel(CUSTOMER_DETAILS_WIDTH, 100, TUNING_MENU_WIDTH, getHeight() - 100, "Fill By Admin");
        tuningMenuRectangle.setLayout(new GridLayout(8, 3, 1, 20));     // Adjust as needed

        String[] labelsAdminEmployee = {"Employee Id:", "Employee Status", "Employee Salary:", "Employee Login Email:", "Employee Login Password:"};

        for (String addLabel : labelsAdminEmployee) {
            JLabel jLabel = new JLabel(addLabel);
            jLabel.setPreferredSize(new Dimension(170, 50));
            if (addLabel.equals("Employee Id:")) {
                employeeIdTextField = new JTextField();
                tuningMenuRectangle.add(jLabel);
                tuningMenuRectangle.add(employeeIdTextField);
                employeeDetailsMap.put(addLabel, employeeIdTextField);

            } else {
                JTextField jTextField = new JTextField();
                jTextField.setPreferredSize(new Dimension(160, 35));
                tuningMenuRectangle.add(jLabel);
                tuningMenuRectangle.add(jTextField);

                employeeDetailsMap.put(addLabel, jTextField);
            }

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
        String[] buttonLabels = {"Show Receipt", "Approve Employee", "Reset", "Print Receipt", "Finish"};
        for (String buttonLabel : buttonLabels) {
            JButton jButton = new JButton(buttonLabel);
            jButton.setPreferredSize(new Dimension(60, 74));
            jButton.setFont(new Font("Arial", Font.BOLD, 21));
            buttonsRectangle.add(jButton);

            if (buttonLabel.equals("Approve Employee")) {
                jButton.addActionListener(e -> {
                    getValue();
                });
            } else if (buttonLabel.equals("Show Receipt")) {
                jButton.addActionListener(e -> {
                    showEmployeeDetailsInReceipt(receiptTextArea);
                });
            } else if (buttonLabel.equals("Reset")) {
                jButton.addActionListener(e -> {
                    resetAll(customerDetailsRectangle, tuningMenuRectangle, receiptTextArea);
                });
            } else if (buttonLabel.equals("Print Receipt")) {
                jButton.addActionListener(e -> {
                    Utils utils = new Utils();
                    utils.printItemsReceipt(receiptTextArea);
                });
            } else if (buttonLabel.equals("Finish")) {
                jButton.addActionListener(e -> {
                    new AdminDashboard();
                    dispose();
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

    private void showEmployeeDetailsInReceipt(JTextPane textReceiptPane) {
        // Fetch employee details from the map
        String fullName = employeeDetailsMap.get("Full Name:").getText();
        String fatherName = employeeDetailsMap.get("Father Name:").getText();
        String phoneNumber = employeeDetailsMap.get("Phone Number:").getText();
        String cnicNumber = employeeDetailsMap.get("CNIC Number:").getText();
        String reference = employeeDetailsMap.get("Reference:").getText();
        String experience = employeeDetailsMap.get("Experience:").getText();

        String employeeId = employeeDetailsMap.get("Employee Id:").getText();
        String employeeStatus = employeeDetailsMap.get("Employee Status").getText();
        String employeeSalary = employeeDetailsMap.get("Employee Salary:").getText();
        String employeeLoginEmail = employeeDetailsMap.get("Employee Login Email:").getText();
        String employeeLoginPassword = employeeDetailsMap.get("Employee Login Password:").getText();

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

        Style headingStyle = textReceiptPane.addStyle("Heading", defaultStyle);
        StyleConstants.setBold(headingStyle, true);
        StyleConstants.setFontSize(headingStyle, 14);

        Style spacing = textReceiptPane.addStyle("Spacing", defaultStyle);
        StyleConstants.setFontSize(spacing, 2);

        // Insert text with different styles
        try {
            styledDocument.insertString(styledDocument.getLength(), "\n     Garage Genius\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Employee Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), " Full Name : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), fullName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Father Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), fatherName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Phone Number : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), phoneNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " CNIC Number : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), cnicNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Reference : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), reference + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Experience : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), experience + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", spacing);
            styledDocument.insertString(styledDocument.getLength(), "Admin Details:\n", headingStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Id : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeId + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Status : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeStatus + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Salary : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeSalary + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Login Email : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeLoginEmail + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Employee Login Password : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeLoginPassword + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);


            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            styledDocument.insertString(styledDocument.getLength(), "\n\n\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Joining Date : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), formattedDateTime + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n", defaultStyle);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void resetAll(JPanel customerDetailRectangle, JPanel tuningMenuRectangle, JTextPane textReceiptArea) {

        for (Component component : tuningMenuRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

        textReceiptArea.setText("");

        for (Component component : customerDetailRectangle.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setText("");
            }
        }

        employeeCounterId = getLastCustomerIdFromDatabase() + 1;
    }

    private void getValue() {
        String fullName = employeeDetailsMap.get("Full Name:").getText();
        String fatherName = employeeDetailsMap.get("Father Name:").getText();
        String phoneNumber = employeeDetailsMap.get("Phone Number:").getText();
        String cnicNumber = employeeDetailsMap.get("CNIC Number:").getText();
        String reference = employeeDetailsMap.get("Reference:").getText();
        String experience = employeeDetailsMap.get("Experience:").getText();

        String employeeId = employeeDetailsMap.get("Employee Id:").getText();
        String employeeStatus = employeeDetailsMap.get("Employee Status").getText();
        String employeeSalary = employeeDetailsMap.get("Employee Salary:").getText();
        String employeeLoginEmail = employeeDetailsMap.get("Employee Login Email:").getText();
        String employeeLoginPassword = employeeDetailsMap.get("Employee Login Password:").getText();


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        saveToDatabase(employeeId, fullName, fatherName, phoneNumber, cnicNumber, reference, experience, employeeStatus, employeeSalary, employeeLoginEmail, employeeLoginPassword, formattedDateTime);
    }

    private void saveToDatabase(String employeeId, String fullName, String fatherName, String phoneNumber, String cnicNumber, String reference, String experience, String employeeStatus, String employeeSalary, String employeeLoginEmail, String employeeLoginPassword, String joiningDate) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
            String sql = "INSERT INTO [Employee_Details] ([Employee ID], [Employee Full Name], [Employee Father], [Phone Number], [CNIC NUMBER], [Reference], [Experience], [Employee Status], [Employee Salary], [Employee Login Email], [Employee Login Password], [Joining Date]) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employeeId);
            preparedStatement.setString(2, fullName);
            preparedStatement.setString(3, fatherName);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, cnicNumber);
            preparedStatement.setString(6, reference);
            preparedStatement.setString(7, experience);
            preparedStatement.setString(8, employeeStatus);
            preparedStatement.setString(9, employeeSalary);
            preparedStatement.setString(10, employeeLoginEmail);
            preparedStatement.setString(11, employeeLoginPassword);
            preparedStatement.setString(12, joiningDate);

            preparedStatement.executeUpdate();

            try {
                String sqlAuth = "INSERT INTO [Auth] ([Email], [Password]) " +
                        "VALUES (?, ?)";

                PreparedStatement preparedStatementAuth = connection.prepareStatement(sqlAuth);
                preparedStatementAuth.setString(1, employeeLoginEmail);
                preparedStatementAuth.setString(2, employeeLoginPassword);

                preparedStatementAuth.executeUpdate();

                try {
                    String sqlEmpReceipt = "INSERT INTO [Employee_Receipt] ([Employee ID], [Employee Name], [Employee Salary]) " +
                            "VALUES (?, ?, ?)";

                    PreparedStatement preparedStatementEmpReceipt = connection.prepareStatement(sqlEmpReceipt);
                    preparedStatementAuth.setString(1, employeeId);
                    preparedStatementAuth.setString(2, fullName);
                    preparedStatementAuth.setString(3, employeeSalary);

                    preparedStatementEmpReceipt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Employee Data saved to database successfully!");

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
            String sql = "SELECT MAX([Employee ID]) FROM [Employee_RECEIPT]";
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
        SwingUtilities.invokeLater(() -> new AddNewEmployee());
    }
}
