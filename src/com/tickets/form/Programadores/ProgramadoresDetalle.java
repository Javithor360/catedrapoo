package com.tickets.form.Programadores;

import com.tickets.model.Bitacora;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class ProgramadoresDetalle extends JFrame {
    private JPanel pnlDetalle;
    private JButton btnVolver;
    private JButton btnEntregar;
    private JLabel lblTitle;
    private JTextField txtProgreso;
    private JTextField txtFechaEntrega;
    private JTextField txtSolicitante;
    private JTextArea txaDescripcion;
    private JTextField txtTitle;
    private JTextField txtProbador;
    private JTextArea txaObservaciones;
    private JTextField txtFechaCreacion;
    private JTextField txtJefeDesarrollo;
    private JTable tblBitacora;
    DefaultTableModel model;

    public ProgramadoresDetalle (UserSession user, Ticket ticket, ProgramadoresMain mainComponent) {
        super("Programadores - Información de caso");
        setVisible(true);
        setSize(900, 500);
        setMinimumSize(new Dimension(900, 500));
        setPreferredSize(new Dimension(900, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlDetalle);

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
        fetch_logs(ticket);

        btnVolver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });

        tblBitacora.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(model.getValueAt(tblBitacora.getSelectedRow(), 0).toString().startsWith("Agregar")) {
                    new ProgramadoresBitacora(user, ticket, ProgramadoresDetalle.this, mainComponent);
                    fetch_logs(ticket);
                }
            }
        });
    }

    // Obtener el valor del porcentaje en el registro más reciente en base a su fecha de creación
    public double get_latest_percent (Ticket t) {
        HashMap<Integer, Bitacora> bMap = t.getLogs();

        List<Bitacora> bitacoras = new ArrayList<>(bMap.values());

        if(!bitacoras.isEmpty()) {
            bitacoras.sort(Comparator.comparing(Bitacora::getCreated_at).reversed());
            return bitacoras.get(0).getPercent();
        } else {
            return 0;
        }
    }

    public void fetch_logs(Ticket t) {
        model.setRowCount(0);

        txtProgreso.setText(t.getState() + " (" + get_latest_percent(t) + "%)");

        if (!t.getLogs().isEmpty()) {
            for (Map.Entry<Integer, Bitacora> entry : t.getLogs().entrySet()) {
                Bitacora bitacora = entry.getValue();
                model.addRow(new Object[]{bitacora.getCreated_at(), bitacora.getPercent(), bitacora.getProgrammer_name(), bitacora.getName(), bitacora.getDescription()});
            }
        } else {
            model.addRow(new Object[]{"Aún", "no", "hay", "bitácoras", "registradas..."});
        }
        model.addRow(new Object[]{"Agregar", "nuevo", "registro", "de", "bitácora..."});
    }

}
