package com.tickets.form.JefeArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SolicitanteMain extends JFrame {

    private JPanel pnlSolicitante;
    private JTable table1;
    private JButton regresarButton;
    private JButton aperturarCasoButton;
    public SolicitanteMain() {
        super("Jefe de área funcional - Homepage");
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlSolicitante);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(getParent());
        aperturarCasoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    SolicitudApertura ap = new SolicitudApertura();
                    ap.setVisible(true);
            }
        });
    }
}
