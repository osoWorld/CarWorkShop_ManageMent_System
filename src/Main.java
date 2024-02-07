import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_PASSWORD = "admin123";
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowLoginGUI());

    }

    private static void createAndShowLoginGUI() {
        JFrame frame = new JFrame("Admin Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(0,0,screenSize.width,screenSize.height);        //N: Use setBounds here

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);

        // Pass the frame itself to placeLoginComponents
        placeLoginComponents(panel, frame);

        frame.setVisible(true);

    }

    private static void placeLoginComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        int panelWidth = 400;
        int panelHeight = 200;

        int centerX = (frame.getWidth() - panelWidth) / 2;
        int centerY = (frame.getHeight() - panelHeight) / 2;

        JPanel loginPanel = new JPanel(null);
        loginPanel.setBounds(centerX, centerY, panelWidth, panelHeight);
        panel.add(loginPanel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 40, 80, 25);
        loginPanel.add(emailLabel);

        JTextField emailText = new JTextField(30);
        emailText.setBounds(150, 40, 300, 30);
        emailText.setFont(new Font("Arial", Font.PLAIN, 24));
        loginPanel.add(emailText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 90, 120, 25);
        loginPanel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(30);
        passwordText.setBounds(150, 90, 300, 30);
        passwordText.setFont(new Font("Arial", Font.PLAIN, 24));
        loginPanel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(160, 150, 150, 40);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 24));
        loginPanel.add(loginButton);       //N:Use login panel instead of panel

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredEmail = emailText.getText();
                char[] enteredPassword = passwordText.getPassword();
                String enteredPasswordString = new String(enteredPassword);

                if (ADMIN_EMAIL.equals(enteredEmail) && ADMIN_PASSWORD.equals(enteredPasswordString)) {
                    JOptionPane.showMessageDialog(null, "Login Successful");

                    // After successful login, open the main menu page
                    openMainMenuPage();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password. Try again.");
                }
            }

            private void openMainMenuPage() {

                JFrame mainMenuFrame = new JFrame("Main Menu");
                mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                mainMenuFrame.setSize(screenSize.width, screenSize.height);

                JPanel mainMenuPanel = new JPanel();
                mainMenuFrame.getContentPane().add(mainMenuPanel);

                placeMainMenuComponents(mainMenuPanel);

                mainMenuFrame.setVisible(true);

            }

            private void placeMainMenuComponents(JPanel mainMenuPanel) {
                mainMenuPanel.setLayout(null);

                JButton checkInButton = new JButton("Car Check In");
                checkInButton.setFont(new Font("Arial", Font.PLAIN, 24));
                checkInButton.setBounds(50, 40, 200, 50);
                mainMenuPanel.add(checkInButton);

                JButton checkOutButton = new JButton("Car Check Out");
                checkOutButton.setFont(new Font("Arial", Font.PLAIN, 24));
                checkOutButton.setBounds(50, 100, 200, 50);
                mainMenuPanel.add(checkOutButton);

                checkInButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "Car Check In selected");
                        // Add code for handling Car Check In
                    }
                });

                checkInButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "Car Check In selected");
                        // Add code for handling Car Check In
                    }
                });

                checkOutButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "Car Check Out selected");
                        // Add code for handling Car Check Out
                    }
                });

            }
        });

    }
}