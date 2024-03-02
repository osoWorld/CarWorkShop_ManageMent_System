package Ext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LOGIN implements ActionListener {
    JFrame frm;
    JLabel label;
    JLabel intro1;
    JLabel intro2;
    JLabel intro3;
    JLabel intro4;
    JLabel uname;
    JLabel pass;
    JTextField txt1;
    JTextField txt2;
    Choice a;
    Choice b;
    JPanel login;
    JButton lgnbtn;
    JButton clr;

    public void intgui() {

        frm = new JFrame();

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/resources/shopping.png"));
        frm.setContentPane(new JLabel(imageIcon));

        label = new JLabel();

        Font font1 = new Font("Tahoma", Font.BOLD, 30);
        Font font2 = new Font("Tahoma", Font.BOLD, 20);
        Font font3 = new Font("Tahoma", Font.BOLD, 17);
        Font font4 = new Font("Tahoma",Font.BOLD,15);

        login =new JPanel();

        intro1 = new JLabel("E-Inventory ");
        intro2 = new JLabel("Online Store Managment System");
        intro3 = new JLabel("Welcome! ");
        intro4 = new JLabel("Please login your account ");
        uname = new JLabel("USERNAME: ");
        pass = new JLabel("PASSWORD:");

        lgnbtn = new JButton("LOGIN");
        clr=new JButton("CLEAR");

        txt1 = new JTextField("");
        txt2 = new JTextField("");

        a=new Choice();
        b=new Choice();

        a.add("ADMINISTRATOR ");
        a.add("EMPLOYEE");

        intro1.setFont(font1);
        intro2.setFont(font2);
        intro3.setFont(font2);
        uname.setFont(font3);
        pass.setFont(font3);
        lgnbtn.setFont(font4);
        clr.setFont(font4);

        intro1.setBounds(150, 90, 200, 40);
        intro2.setBounds(150, 125, 330, 40);
        intro3.setBounds(170, 200, 150, 20);
        intro4.setBounds(170, 220, 190, 20);
        uname.setBounds(220, 300, 170, 20);
        pass.setBounds(220, 350, 170, 20);

        txt1.setBounds(350, 300, 180, 23);
        txt2.setBounds(350, 350, 180, 23);

        a.setBounds(280, 410, 180, 20);

        lgnbtn.setBounds(250, 500, 100, 28);
        clr.setBounds(400, 500, 100, 28);


        Color customColor = new Color(194, 86, 235);
        intro3.setForeground(customColor);
        lgnbtn.setBackground(customColor);
        lgnbtn.setForeground(Color.white);
        clr.setBackground(customColor);
        clr.setForeground(Color.white);

        frm.add(label);
        frm.add(intro1);
        frm.add(intro2);
        frm.add(intro3);
        frm.add(intro4);
        frm.add(uname);
        frm.add(pass);
        frm.add(txt1);
        frm.add(txt2);
        frm.add(a);
        frm.add(lgnbtn);
        frm.add(clr);



        frm.setSize(1540, 800);
        frm.setLayout(null);
        frm.setTitle("STORE MANAGEMENT SYSTEM");
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);

        lgnbtn.addActionListener(this);

    }


    public static void main(String[] args) {
        LOGIN m= new LOGIN();
        m.intgui();

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        if(e.getSource()==lgnbtn)
        {
            JOptionPane.showMessageDialog(null,"login successfully" );
            System.exit(0);
        }
    }
}