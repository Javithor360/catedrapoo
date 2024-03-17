package com.tickets.form.JefeDesarrollo;

import com.tickets.model.JefeDesarrollo;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

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

    public JefeDesarrolloNewRequest(UserSession user, Ticket ticket, JefeDesarrolloMain mainComponent) {
        super("Jefe de Desarrollo - Solicitud de caso");
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
        btnAceptar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(txaObservations.getText().length() > 10) {
                    try {
                        new JefeDesarrolloAceptar(user, ticket, txaObservations.getText(), JefeDesarrolloNewRequest.this, mainComponent);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                                pnlDesarrolloRequests,
                                "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE
                        );
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            pnlDesarrolloRequests,
                            "Para aceptar una solicitud de caso necesitas agregar las debidas observaciones...",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        btnRechazar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(txaObservations.getText().length() > 50) {
                    try {
                        denyTicketRequest(ticket, user.getId());
                        JOptionPane.showMessageDialog(
                                pnlDesarrolloRequests,
                                "El caso " + ticket.getCode() + " ha sido rechazado.",
                                "ÉXITO",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        dispose();
                        mainComponent.fetch_tickets_request();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                                pnlDesarrolloRequests,
                                "Ocurrió un error durante la ejecución: " + new RuntimeException(ex).getMessage(),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE
                        );
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            pnlDesarrolloRequests,
                            "Para rechazar esta solitud de caso, necesitas especificar el motivo del rechazo...",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    public void denyTicketRequest (Ticket t, int dev_boss_id) throws SQLException {
        String observations = txaObservations.getText();

        JefeDesarrollo.denyTicket(t, observations, dev_boss_id);
    }
}
