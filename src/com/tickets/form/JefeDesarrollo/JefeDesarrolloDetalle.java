package com.tickets.form.JefeDesarrollo;

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
        txtEstado.setText(ticket.getState() + "(" + get_latest_percent(ticket) + "%)");
        txtSolicitante.setText(ticket.getBoss_name() + " (Depto. " + ticket.getRequester_area_name() + " )");
        txtEncargado.setText((ticket.getProgrammer_name() != null ? ticket.getProgrammer_name() : "Sin asignación..."));
        txtProbador.setText((ticket.getTester_name() != null ? ticket.getTester_name() : "Sin asignación..."));
        txtFecha.setText("Creado el: " + ticket.getCreated_at() + " | Fecha de entrega: " + ticket.getDue_date().toString());
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

        // aquí iría un método para obtener la bitácora y sus observaciones

        btnRegresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
    }

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

    public void fetch_logs (Ticket t) {
        model.setRowCount(0);

        if(!t.getLogs().isEmpty()) {
            for (Map.Entry<Integer, Bitacora> entry : t.getLogs().entrySet()) {
                Bitacora bitacora = entry.getValue();
                model.addRow(new Object[]{ bitacora.getCreated_at(), bitacora.getPercent(), bitacora.getProgrammer_name(), bitacora.getName(), bitacora.getDescription() });
            }
        } else {
            model.addRow(new Object[]{ "Aún", "no", "hay", "bitácoras", "registradas..." });
        }
    }
}
