package com.tickets.form.Probadores;

import javax.swing.*;
import java.awt.*;

public class ProbadoresMain extends JFrame {
    private JPanel pnlProbadores;
    private JButton regresarButton;
    private JLabel lblTitle;

    public ProbadoresMain () {
        super("Empleado de área funcional - Homepage");
        setSize(500, 500);
        setVisible(true);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlProbadores);
    }
}
