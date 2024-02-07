package auth;

import AdminPanel.AdminDashboard;
import Dashboard.MainDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public Login() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Car Workshop Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame to be maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false); // Set it too false to show window decorations (close, minimize, etc.)


        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/resources/main_login.jpg"));   //To get Image from resources
        // Set the background image of the frame
        setContentPane(new JLabel(imageIcon)); // Replace with the actual image file

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
                // Add your login logic here
                // For simplicity, display a message
                handleLoginButtonClick(roleComboBox.getSelectedItem().toString());
            }
        });
        loginPanel.add(loginButton, constraints);

        // Set layout for the content pane
        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.CENTER);

        setVisible(true);

    }

    private void handleLoginButtonClick(String selectedRole) {

//        JOptionPane.showMessageDialog(Login.this, "Login button clicked!");

        // Close the current login frame
        dispose();

        // Open the appropriate Dashboard based on the selected role
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (selectedRole.equals("Administrator")){
                    new AdminDashboard();
                } else if (selectedRole.equals("Employee")) {
                    new MainDashboard();
                }
            }
        });
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