package Ext;

import javax.swing.*;
import java.awt.*;

public class DASHBOARD  {
    JFrame frm1;
    JPanel contentPanel;
    JPanel sidePanel;
    CardLayout cardLayout;

    JPanel card1;
    JPanel card2;
    JPanel card3;

    JLabel title;


    public void title(){

        frm1 = new JFrame();

        Font titlefont = new Font("Tahoma", Font.BOLD, 30);
        title = new JLabel("STORE MANAGEMENT SYSTEM");
        title.setFont(titlefont);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.X_AXIS));
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(title,BorderLayout.NORTH);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        frm1.add(sidePanel,BorderLayout.NORTH);

        frm1.setSize(1540, 800);
        frm1.setLayout(null);
        frm1.setTitle("STORE MANAGEMENT SYSTEM");
        frm1.setLocationRelativeTo(null);
        frm1.setVisible(true);
    }
    public static void main(String[] args) {
        DASHBOARD m= new DASHBOARD();
        m.title();
    }
}