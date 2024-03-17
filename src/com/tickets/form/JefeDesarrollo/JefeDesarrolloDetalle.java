package com.tickets.form.JefeDesarrollo;

import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JefeDesarrolloDetalle extends JFrame {
    private JPanel pnlDetalle;
    private JButton btnRegresar;
    private JTextField txtEstado;
    private JTextField txtEncargado;
    private JTextField txtProbador;
    private JTextField txtFecha;
    private JTextArea txaObservaciones;
    private JTable tblBitacora;
    private JLabel lblTitle;
    private JTextField txtSolicitante;
    DefaultTableModel model;

    public JefeDesarrolloDetalle (UserSession user, Ticket ticket) {
        super("Jefe de Desarrollo - Monitoreo de ticket");
        setVisible(true);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlDetalle);

        lblTitle.setText("Caso " + ticket.getCode());
        txtEstado.setText(ticket.getState());
        txtSolicitante.setText(ticket.getBoss_name() + " (Depto. " + ticket.getRequester_area_name() + " )");
        txtEncargado.setText((ticket.getProgrammer_name() != null ? ticket.getProgrammer_name() : "Sin asignación..."));
        txtProbador.setText((ticket.getTester_name() != null ? ticket.getTester_name() : "Sin asignación..."));
        txtFecha.setText("Creado el: " + ticket.getCreated_at() + " | Fecha de entrega: " + ticket.getDue_date().toString());
        txaObservaciones.setText(ticket.getObservations());

        String[] columns = { "Autor", "Fecha", "Progeso", "Descripción" };
        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas sean no editables
                return false;
            }
        };
        tblBitacora.setModel(model);
        // aquí iría un método para obtener la bitácora y sus observaciones

        btnRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
    }
}
