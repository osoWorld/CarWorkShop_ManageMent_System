package AdminPanel;

import UtilsFeatures.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

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


        printButton.addActionListener(e -> {
            Utils utils = new Utils();
            utils.printItemsReceipt(employeeDetailsPane);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeePayment("Sample Employee Details"));
    }
}