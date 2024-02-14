package AdminPanel;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MonthlySummary {
    public void generateMonthlySummary() {
        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb")) {
            // Retrieve data from Customer_Receipt table
            double totalCustomerBill = 0.0;
            String month = "February 2024";

            try (Statement statement = connection.createStatement()) {
                ResultSet customerResultSet = statement.executeQuery("SELECT [Total Bill] FROM Customer_Receipt WHERE [Transaction Month] = '" + month + "'");
                while (customerResultSet.next()) {
                    totalCustomerBill += customerResultSet.getDouble("Total Bill");
                }
            }

            // Retrieve data from Employee_Receipt table
            double totalEmployeePayingAmount = 0.0;
            try (Statement statement = connection.createStatement()) {
                ResultSet employeeResultSet = statement.executeQuery("SELECT [Paying Amount] FROM Employee_Receipt WHERE [Salary Month] = '" + month + "'");
                while (employeeResultSet.next()) {
                    totalEmployeePayingAmount += employeeResultSet.getDouble("Paying Amount");
                }
            }


            // Calculate total
            double total = totalCustomerBill + totalEmployeePayingAmount;

            // Get current date, time, and month
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentDate = now.format(dateFormatter);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String currentTime = now.format(timeFormatter);
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String currentMonth = now.format(monthFormatter);

            // Inserting data into the new table
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Monthly_Records ([Total Expense], [Total Profit], [Total Employee Salary], [Total Restock Expense], [Total Service Profit], [Date], [Current Time], [Record Month]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setDouble(1, total);
                preparedStatement.setDouble(2, total);
                preparedStatement.setDouble(3, totalEmployeePayingAmount);
                preparedStatement.setDouble(4, total);
                preparedStatement.setDouble(5, totalCustomerBill);
                preparedStatement.setString(6, currentDate);
                preparedStatement.setString(7, currentTime);
                preparedStatement.setString(8, currentMonth);
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(null,"Monthly summary generated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}