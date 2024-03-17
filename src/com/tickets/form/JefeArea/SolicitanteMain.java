package com.tickets.form.JefeArea;

import javax.swing.*;
import java.awt.*;

public class SolicitanteMain extends JFrame {

    private JPanel pnlSolicitante;

    public SolicitanteMain() {
        super("Jefe de área funcional - Homepage");
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlSolicitante);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(getParent());
    }
}
