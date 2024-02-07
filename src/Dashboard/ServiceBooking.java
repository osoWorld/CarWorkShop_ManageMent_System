package Dashboard;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServiceBooking extends JFrame {
    private static JCheckBox serviceBookingCheckBox = new JCheckBox("Service Booking");
    private static JComboBox<String> timeComboBox = new JComboBox<>(new String[]{"12 hours", "48 hours", "72 hours","96 hours","120 hours"});
    private static JTextField phoneNumberTextField = new JTextField();
    private static ServiceBookingInfo serviceBookingInfo;

    public static void showServiceBookingWindow(JFrame parentFrame) {
        JFrame serviceBookingFrame = new JFrame("Service Booking");
        serviceBookingFrame.setSize(400, 300);
        serviceBookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        serviceBookingFrame.setLocationRelativeTo(parentFrame);
        serviceBookingFrame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel serviceBookingRectangle = createTitledRectanglePanel(10, 10, 380, 180, "Service Booking", new GridLayout(3, 2, 10, 10));


        serviceBookingRectangle.add(serviceBookingCheckBox);

        JLabel timeLabel = new JLabel("Time:");

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(timeLabel);
        timePanel.add(timeComboBox);
        serviceBookingRectangle.add(timePanel);

        JLabel phoneNumberLabel = new JLabel("Customer Ph. Number:  ");

        phoneNumberTextField.setPreferredSize(new Dimension(200,30));
        JPanel phoneNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phoneNumberPanel.add(phoneNumberLabel);
        phoneNumberPanel.add(phoneNumberTextField);
        serviceBookingRectangle.add(phoneNumberPanel);

        mainPanel.add(serviceBookingRectangle, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {

            // Retrieve information from the service booking window
            boolean isServiceBookingSelected = serviceBookingCheckBox.isSelected();
            String selectedTime = (String) timeComboBox.getSelectedItem();
            String customerPhoneNumber = phoneNumberTextField.getText();

            // Store the information in the ServiceBookingInfo class
            serviceBookingInfo = new ServiceBookingInfo(isServiceBookingSelected, selectedTime, customerPhoneNumber);

            // Close the service booking window
            serviceBookingFrame.dispose();
        });

        mainPanel.add(submitButton, BorderLayout.SOUTH);

        serviceBookingFrame.add(mainPanel);
        serviceBookingFrame.setVisible(true);
    }

    private static JPanel createTitledRectanglePanel(int x, int y, int width, int height, String title, LayoutManager layout) {
        JPanel titledRectanglePanel = new JPanel();
        titledRectanglePanel.setBounds(x, y, width, height);
        titledRectanglePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)));
        titledRectanglePanel.setLayout(layout);

        return titledRectanglePanel;
    }

    public static void resetServiceBooking() {
        serviceBookingCheckBox.setSelected(false);
        timeComboBox.setSelectedIndex(0);
        phoneNumberTextField.setText("");
    }

    public static ServiceBookingInfo getServiceBookingInfo() {
        return serviceBookingInfo;
    }

    public static class ServiceBookingInfo {
        private boolean isServiceBookingSelected;
        private String selectedTime;
        private String customerPhoneNumber;

        public ServiceBookingInfo(boolean isServiceBookingSelected, String selectedTime, String customerPhoneNumber) {
            this.isServiceBookingSelected = isServiceBookingSelected;
            this.selectedTime = selectedTime;
            this.customerPhoneNumber = customerPhoneNumber;
        }

        public boolean isServiceBookingSelected() {
            return isServiceBookingSelected;
        }

        public String getSelectedTime() {
            return selectedTime;
        }

        public String getCustomerPhoneNumber() {
            return customerPhoneNumber;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showServiceBookingWindow(null));
    }
}
