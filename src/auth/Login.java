package auth;

import AdminPanel.AdminDashboard;
import Dashboard.MainDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private String adminEmail = "admin@admin.com";
    private String adminPassword = "adminPass";
    public Login() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Car Workshop Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to be maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false); // Set it too false to show window decorations (close, minimize, etc.)


        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/main_login.jpg")));
        setContentPane(new JLabel(imageIcon));

        // Create a transparent panel for login components
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int leftBorderSize = 350; // Adjust the left border size as needed
                int rightBorderSize = 350; // Adjust the right border size as needed
                g.setColor(new Color(255, 255, 255, 150)); // White color with 150 alpha (transparency)
                g.fillRect(leftBorderSize, 140, getWidth() - leftBorderSize - rightBorderSize, getHeight() - 280);
            }
        };

        // Set layout for the login panel
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setOpaque(false);

        // Create constraints for components
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Add title label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginPanel.add(titleLabel, constraints);

        // Increment y coordinate for the next row
        constraints.gridy++;

        // Add email label and text field
        JLabel emailLabel = new JLabel("Email:");
        loginPanel.add(emailLabel, constraints);

        constraints.gridx++;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        loginPanel.add(emailField, constraints);

        // Increment y coordinate for the next row
        constraints.gridy++;

        // Add password label and password field
        constraints.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        loginPanel.add(passwordLabel, constraints);

        constraints.gridx++;
        passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, constraints);

        // Increment y coordinate for the next row
        constraints.gridy++;

        // Add a JComboBox for role selection
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2; // Make the JComboBox span two columns

        JLabel roleLabel = new JLabel("Sign in as:");
        loginPanel.add(roleLabel, constraints);

        constraints.gridy++;

        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Administrator", "Employee"});
        loginPanel.add(roleComboBox, constraints);

        // Increment y coordinate for the next row
        constraints.gridy++;


        // Add login button
        constraints.gridx = 0;
        constraints.gridwidth = 2; // Make the button span two columns
        constraints.fill = GridBagConstraints.NONE;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoginButtonClick(emailField, passwordField, roleComboBox.getSelectedItem().toString());
            }
        });
        loginPanel.add(loginButton, constraints);

        // Set layout for the content pane
        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.CENTER);

        setVisible(true);

    }

    private void handleLoginButtonClick(JTextField email, JTextField password, String selectedRole) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (selectedRole.equals("Administrator")) {
                    adminAuthenticate(email,password);
                } else if (selectedRole.equals("Employee")) {
                    employeeAuthenticate(email,password);
                }
            }
        });
    }

    private void adminAuthenticate(JTextField email, JTextField password){
        if (email.getText().equals(adminEmail) && password.getText().equals(adminPassword)){
            JOptionPane.showMessageDialog(null, "Admin Login Successful","Success",JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new AdminDashboard();
        }else {
            JOptionPane.showMessageDialog(null, "Wrong Email or Password","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void employeeAuthenticate(JTextField email, JTextField password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:ucanaccess://E://Garage Genius Database//Garage_Genius.accdb");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from Auth");

            boolean loginSuccessful = false;  // Add a flag to track login status

            while (resultSet.next()) {
                if (email.getText().equals(resultSet.getString(1)) && password.getText().equals(resultSet.getString(2))) {
                    JOptionPane.showMessageDialog(null, "Login Successful","Success",JOptionPane.INFORMATION_MESSAGE);
                    loginSuccessful = true;  // Set the flag to true
                    dispose();
                    new MainDashboard();
                    break;
                }
            }

            if (!loginSuccessful) {
                JOptionPane.showMessageDialog(null, "Wrong Email or Password","Error",JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException sqe) {
            throw new RuntimeException(sqe);
        } finally {
            try {
                if (connection != null) {
                    connection.close();  // Close the connection in the finally block
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login();
            }
        });
    }
}