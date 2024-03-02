package AdminPanel;

import UtilsFeatures.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SearchReceipt extends JFrame {
    private JTextPane receiptTextPane;

    public SearchReceipt() {
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Search Receipt");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create "Info" and "Receipt" boxes
        JPanel infoBox = createTitledRectanglePanel(0, 0, getWidth(), getHeight() / 3, "Info");
        JPanel receiptBox = createTitledRectanglePanel(0, getHeight() / 3, getWidth(), getHeight() * 2 / 3, "Receipt");

        // Create components for "Info" box
        JLabel customerLabel = new JLabel("Customer Receipt ID:");
        JTextField customerTextField = new JTextField(20);
        customerLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JLabel employeeLabel = new JLabel("Employee Receipt ID:");
        JTextField employeeTextField = new JTextField(20);
        employeeLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JButton customerCheckButton = new JButton("Customer Check");
        JButton employeeCheckButton = new JButton("Employee Check");
        JButton resetButton = new JButton(" Reset ");
        JButton closeButton = new JButton("Close");

        customerCheckButton.addActionListener(e -> {
            String customerId = customerTextField.getText().trim();

            if (!customerId.isEmpty()) {
                getCustomerInfo(customerId);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid customer ID","Error",JOptionPane.ERROR_MESSAGE);
            }
        });

        employeeCheckButton.addActionListener(e -> {
            String employeeId = employeeTextField.getText().trim();

            if (!employeeId.isEmpty()) {
                getEmployeeInfo(employeeId);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid employee ID","Error",JOptionPane.ERROR_MESSAGE);
            }
        });

        resetButton.addActionListener(e -> {
            resetAll(customerTextField, employeeTextField);
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        // Add components to "Info" box
        infoBox.setLayout(new GridLayout(4, 2, 10, 10));
        infoBox.add(customerLabel);
        infoBox.add(customerTextField);
        infoBox.add(employeeLabel);
        infoBox.add(employeeTextField);
        infoBox.add(customerCheckButton);
        infoBox.add(employeeCheckButton);
        infoBox.add(resetButton);
        infoBox.add(closeButton);

        // Create components for "Receipt" box
        receiptTextPane = new JTextPane();
        receiptTextPane.setEditable(false);
        JScrollPane receiptScrollPane = new JScrollPane(receiptTextPane);

        // Add components to "Receipt" box
        receiptBox.setLayout(new BorderLayout());
        receiptBox.add(receiptScrollPane, BorderLayout.CENTER);

        // Add "Info" and "Receipt" boxes to the main panel
        mainPanel.add(infoBox, BorderLayout.NORTH);
        mainPanel.add(receiptBox, BorderLayout.CENTER);

        // Create "Print" button
        JButton printButton = new JButton("Print");
        printButton.setPreferredSize(new Dimension(120, 28));

        // Add "Print" button to the main panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(printButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils utils = new Utils();
                utils.printItemsReceipt(receiptTextPane);
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTitledRectanglePanel(int x, int y, int width, int height, String title) {
        JPanel titledRectanglePanel = new JPanel();
        titledRectanglePanel.setBounds(x, y, width, height);
        titledRectanglePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)));

        return titledRectanglePanel;
    }

    private void getCustomerInfo(String customerId) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
            Statement statement = connection.createStatement();

            String query = "SELECT [First Name], [Sur Name], [Cnic Number], [Deposit Amount], [Selected Services], [Service Amount], [Booking Details], [Booking Time], [Customer Phone Number], [Total Bill], [Transaction Month], [Date] FROM Customer_Receipt WHERE [Customer ID] = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("First Name");
                String surName = resultSet.getString("Sur Name");
                String cnicNumber = resultSet.getString("Cnic Number");
                String depositAmount = resultSet.getString("Deposit Amount");
                String selectedServices = resultSet.getString("Selected Services");
                String bookingDetails = resultSet.getString("Booking Details");
                String bookingTime = resultSet.getString("Booking Time");
                String customerPhoneNumber = resultSet.getString("Customer Phone Number");
                String totalBill = resultSet.getString("Total Bill");
                String transactionMonth = resultSet.getString("Transaction Month");
                String billingDate = resultSet.getString("Date");


                displayCustomerReceipt(firstName, surName, cnicNumber, depositAmount, selectedServices, bookingDetails, bookingTime, customerPhoneNumber, totalBill, transactionMonth, billingDate);
            } else {
                JOptionPane.showMessageDialog(this, "Customer ID not found in the database.");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCustomerReceipt(String firstName, String surName, String cnicNumber, String depositAmount, String selectedServices, String bookingDetails, String bookingTime, String customerPhoneNumber, String totalBill, String transactionMonth, String billingDate) {

        StyledDocument styledDocument = receiptTextPane.getStyledDocument();

        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        Style defaultStyle = receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = receiptTextPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style largeStyle = receiptTextPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(largeStyle, 18);

        try {
            styledDocument.insertString(styledDocument.getLength(), "\nCustomer Receipt\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n First Name : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), firstName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Sur Name : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), surName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n CNIC Number : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), cnicNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Deposit Amount : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), depositAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Selected Service : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), selectedServices + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Booking Details : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), bookingDetails + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Booking Time : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), bookingTime + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Customer Phone No. : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), customerPhoneNumber + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Total Bill : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), totalBill + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Transaction Month : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), transactionMonth + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Billing Date : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), billingDate + "\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void getEmployeeInfo(String employeeId) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
            Statement statement = connection.createStatement();

            String query = "SELECT [Employee Name], [Employee Salary], [Deduction], [Bonus Amount], [Paying Amount], [Date], [Time], [Salary Month] FROM Employee_Receipt WHERE [Employee ID] = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String employeeName = resultSet.getString("Employee Name");
                String employeeSalary = resultSet.getString("Employee Salary");
                String deduction = resultSet.getString("Deduction");
                String bonusAmount = resultSet.getString("Bonus Amount");
                String payingAmount = resultSet.getString("Paying Amount");
                String date = resultSet.getString("Date");
                String time = resultSet.getString("Time");
                String salaryMonth = resultSet.getString("Salary Month");


                displayEmployeeReceipt(employeeName, employeeSalary, deduction, bonusAmount, payingAmount, date, time, salaryMonth);
            } else {
                JOptionPane.showMessageDialog(this, "Customer ID not found in the database.");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayEmployeeReceipt(String employeeName, String employeeSalary, String deduction, String bonusAmount, String payingAmount, String date, String time, String salaryMonth) {
        StyledDocument styledDocument = receiptTextPane.getStyledDocument();

        // Clear previous content
        styledDocument.setCharacterAttributes(0, styledDocument.getLength(), receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE), true);

        // Create different styles
        Style defaultStyle = receiptTextPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style boldStyle = receiptTextPane.addStyle("Bold", defaultStyle);
        StyleConstants.setBold(boldStyle, true);

        Style largeStyle = receiptTextPane.addStyle("Large", defaultStyle);
        StyleConstants.setFontSize(largeStyle, 18);

        try {
            styledDocument.insertString(styledDocument.getLength(), "\nEmployee Receipt\n", largeStyle);
            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Employee Name : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeName + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " employeeSalary : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), employeeSalary + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Deduction : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), deduction + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Bonus Amount : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), bonusAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Paying Amount : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), payingAmount + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Payment Date : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), date + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), "\n Payment Time : ", boldStyle);
            styledDocument.insertString(styledDocument.getLength(), time + "\n", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), " Salary Month : ", defaultStyle);
            styledDocument.insertString(styledDocument.getLength(), salaryMonth + "\n", defaultStyle);

            styledDocument.insertString(styledDocument.getLength(), "----------------------------------------------------\n", defaultStyle);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void resetAll(JTextField customerTextField, JTextField employeeTextField) {
        customerTextField.setText("");
        employeeTextField.setText("");
        receiptTextPane.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SearchReceipt::new);
    }
}
