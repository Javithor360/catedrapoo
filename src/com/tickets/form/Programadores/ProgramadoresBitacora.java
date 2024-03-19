package com.tickets.form.Programadores;

import com.tickets.model.Bitacora;
import com.tickets.model.Programador;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgramadoresBitacora extends JFrame {
    private JPanel pnlBitacora;
    private JButton btnAgregar;
    private JButton btnRegresar;
    private JTextArea txaDescripcion;
    private JTextField txtProgreso;
    private JLabel lblTitle;
    private JTextField txtName;

    public ProgramadoresBitacora (UserSession user, Ticket ticket, ProgramadoresDetalle parentComponent, ProgramadoresMain mainComponent) {
        super("Programadores - Nueva bitácora");
        setVisible(true);
        setMaximumSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlBitacora);

        lblTitle.setText("Agregando Bitácora - Caso " + ticket.getCode());

        btnRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });

        btnAgregar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    if(insertData(ticket, user)) {
                        JOptionPane.showMessageDialog(
                                pnlBitacora,
                                "Registro de bitácora creado exitosamente.",
                                "ÉXITO",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        parentComponent.fetch_logs(ticket);
                        dispose();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            pnlBitacora,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public boolean insertData(Ticket t, UserSession user) {
        if (validateData()) {
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                Bitacora bitacora = new Bitacora(
                        t.getCode(),
                        txtName.getText(),
                        txaDescripcion.getText(),
                        Double.parseDouble(txtProgreso.getText()),
                        user.getName(),
                        dtf.format(now)
                );

                Programador.newLog(bitacora, user.getId());
                t.addBitacora(bitacora);

                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        pnlBitacora,
                        "Ocurrió un error durante la ejecución:\n" + e.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE
                );
                throw new RuntimeException(e);
            }
        } else {
            JOptionPane.showMessageDialog(
                    pnlBitacora,
                    "Completa todos los campos de manera correcta para continuar",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    public boolean validateData() {
        if (txtName.getText().length() < 10) {
            JOptionPane.showMessageDialog(
                    pnlBitacora,
                    "La longitud del título debe ser más extensa",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        if (txaDescripcion.getText().length() < 50) {
            JOptionPane.showMessageDialog(
                    pnlBitacora,
                    "La longitud de la descripción debe ser más extensa",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        double progreso = Double.parseDouble(txtProgreso.getText());
        if (progreso < 0 || progreso > 100) {
            JOptionPane.showMessageDialog(
                    pnlBitacora,
                    "El valor del progreso debe comprender un rango entre 0 y 100",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }


}
