package com.tickets.form.JefeDesarrollo;

import com.tickets.model.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JefeDesarrolloNewRequest extends JFrame {
    private JPanel pnlDesarrolloRequests;
    private JButton btnAceptar;
    private JButton btnRechazar;
    private JLabel lblRequester;
    private JLabel lblDescription;
    private JTextArea txaObservations;
    private JButton btnClose;
    private JLabel lblTitle;
    private JLabel lblTitleCase;

    public JefeDesarrolloNewRequest(Ticket ticket) {
        super("Solicitud de caso");
        setVisible(true);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlDesarrolloRequests);

        lblTitle.setText("Caso " + ticket.getCode());
        lblTitleCase.setText(ticket.getName());
        lblRequester.setText(ticket.getBoss_name() + " | Departamento: " + ticket.getRequester_area_name());
        lblDescription.setText(ticket.getDescription());
        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
    }
}