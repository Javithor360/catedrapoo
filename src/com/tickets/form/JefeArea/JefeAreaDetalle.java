package com.tickets.form.JefeArea;

import com.tickets.model.Bitacora;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class JefeAreaDetalle extends JFrame {
    private JLabel lblTitle;
    private JButton btnVolver;
    private JTextField txtProgreso;
    private JTextField txtFechaEntrega;
    private JTextField txtFechaCreacion;
    private JTextArea txaDescripcion;
    private JTextField txtTitle;
    private JTextField txtSolicitante;
    private JTextField txtProbador;
    private JTextField txtJefeDesarrollo;
    private JTextArea txaObservaciones;
    private JTable tblBitacora;
    private JPanel pnlDetalleJA;

    DefaultTableModel model;

    public JefeAreaDetalle(UserSession user, Ticket ticket, JefeAreaMain mainComponent){
        super("Jefe de Área - Información de caso");
        setVisible(true);
        setSize(900, 500);
        setMinimumSize(new Dimension(900, 500));
        setPreferredSize(new Dimension(900, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlDetalleJA);

        lblTitle.setText("Detalles del caso " + ticket.getCode());
        txtFechaCreacion.setText(ticket.getCreated_at());
        txtFechaEntrega.setText(ticket.getDue_date());
        txtSolicitante.setText(ticket.getBoss_name() + " (Depto. " + ticket.getRequester_area_name() + ")");
        txtProbador.setText(ticket.getTester_name());
        txtJefeDesarrollo.setText(ticket.getDev_boss_name());
        txtTitle.setText(ticket.getName());
        txaDescripcion.setText(ticket.getDescription());
        txaObservaciones.setText(ticket.getObservations());

        String[] columns = { "Fecha", "Progreso", "Autor", "Título", "Descripción" };
        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas sean no editables
                return false;
            }
        };

        tblBitacora.setModel(model);
        fetch_logs(ticket, mainComponent);

        btnVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
    }

    public void fetch_logs(Ticket t, JefeAreaMain mainComp) {
        model.setRowCount(0);

        txtProgreso.setText(t.getState() + " (" + mainComp.get_latest_percent(t) + "%)");

        if (!t.getLogs().isEmpty()) {
            for (Map.Entry<Integer, Bitacora> entry : t.getLogs().entrySet()) {
                Bitacora bitacora = entry.getValue();
                model.addRow(new Object[]{bitacora.getCreated_at(), bitacora.getPercent(), bitacora.getProgrammer_name(), bitacora.getName(), bitacora.getDescription()});
            }
        } else {
            model.addRow(new Object[]{"Aún", "no", "hay", "bitácoras", "registradas..."});
        }
    }
}
