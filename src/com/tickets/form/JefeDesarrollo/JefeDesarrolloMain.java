package com.tickets.form.JefeDesarrollo;

import javax.swing.*;
import java.awt.*;

public class JefeDesarrolloMain extends JFrame {
    private JPanel pnlDesarrolloJefe;

    public JefeDesarrolloMain(String title) {
        super(title);
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlDesarrolloJefe);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(getParent());
    }


    public static void main(String[] args) {
        JFrame frame = new JefeDesarrolloMain("Jefe de Area Desarrollo");
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
