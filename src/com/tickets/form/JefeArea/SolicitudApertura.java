package com.tickets.form.JefeArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SolicitudApertura extends JFrame {
    private JPanel SolicitudApertura;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton regresarButton;
    private JButton aperturarCasoButton;
    public SolicitudApertura(){
        this.setMinimumSize(new Dimension(600, 500));
        this.setTitle("Apertura nuevo caso");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(getParent());
        aperturarCasoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


}

