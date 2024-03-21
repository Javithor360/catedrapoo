package com.tickets.form.JefeArea;

import com.tickets.model.JefeArea;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JefeAreaApertura extends JFrame {
    private JPanel pnlApertura;
    private JLabel lblTitle;
    private JTextArea txaDesc;
    private JButton btnCancelar;
    private JButton btnEnviar;
    private JTextField txtName;

    public JefeAreaApertura(UserSession user, JefeAreaMain mainComponent){
        super("Jefe de área funcional - Apertura de caso");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlApertura);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(getParent());


        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });


        btnEnviar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (validateData()) {
                    try {

                        Ticket.createTicket(user, txtName.getText(), txaDesc.getText());
                        JOptionPane.showMessageDialog(
                                pnlApertura,
                                "Se ha añadido el caso correctamente.",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        mainComponent.fetch_area_tickets(user.getId());
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                pnlApertura,
                                "Ocurrió un error durante la ejecución:\n" + ex.getMessage(),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE
                        );
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(pnlApertura, "Rellena los campos de manera correcta para continuar.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public boolean validateData(){
        if(txtName.getText().length() < 10){
            JOptionPane.showMessageDialog(pnlApertura, "El título debe ser más extenso.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(txaDesc.getText().length() < 50){
            JOptionPane.showMessageDialog(pnlApertura, "Debes detallar más la descripción del proyecto.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
