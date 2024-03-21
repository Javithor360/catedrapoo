package com.tickets.form.Probadores;

import com.tickets.model.Bitacora;
import com.tickets.model.Probador;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class ProbadoresDetalle extends JFrame {
    private JPanel pnlDetalleCaso;
    private JLabel lblTitle;
    private JButton btnAceptar;
    private JButton btnRechazar;
    private JButton btnRegresar;
    private JTextField txtProgreso;
    private JTextField txtFechaEntrega;
    private JTextField txtFechaCreacion;
    private JTextArea txaDescripcion;
    private JTextField txtTitle;
    private JTextField txtSolicitante;
    private JTextField txtJefeDesarrollo;
    private JTextArea txaObservaciones;
    private JTable tblBitacora;
    DefaultTableModel model;

    public ProbadoresDetalle(UserSession user, Ticket ticket, ProbadoresMain mainComponent) {
        super("Probadores - Información de caso");
        setVisible(true);
        setSize(900, 500);
        setMinimumSize(new Dimension(900, 500));
        setPreferredSize(new Dimension(900, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlDetalleCaso);

        lblTitle.setText("Detalles del caso " + ticket.getCode());
        txtFechaCreacion.setText(ticket.getCreated_at());
        txtFechaEntrega.setText(ticket.getDue_date());
        txtSolicitante.setText(ticket.getBoss_name() + " (Depto. " + ticket.getRequester_area_name() + ")");
        txtJefeDesarrollo.setText(ticket.getDev_boss_name());
        txtTitle.setText(ticket.getName());
        txaDescripcion.setText(ticket.getDescription());
        txaObservaciones.setText(ticket.getObservations());

        if(ticket.getState_id() == 4) { // Activar botones para aceptar o rechazar el caso SOLO SI el estado esta en espera de respuesta
            btnAceptar.setEnabled(true);  btnRechazar.setEnabled(true);
        }

        // Establecer el modelo de la tabla de bitácoras
        String[] columns = { "Fecha", "Progreso", "Autor", "Título", "Descripción" };
        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas sean no editables
                return false;
            }
        };

        tblBitacora.setModel(model);
        fetch_logs(ticket); // Conseguir la bitácota del ticket correspondiente
        btnRegresar.addMouseListener(new MouseAdapter() { // Regresar a la ventana anterior
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });

        btnAceptar.addMouseListener(new MouseAdapter() { // Acción del botón aceptar
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(btnAceptar.isEnabled()){
                    try{ // Actualizar el estado del ticket a FINALIZADO
                        Probador.updateStateTicket(7, ticket.getId(), user.getId());

                        JOptionPane.showMessageDialog(pnlDetalleCaso, // Muestra el mensaje si la actualización es exitosa
                                "Se ha actualizado el estado del proyecto correctamente.", "Actualizado correctamente", JOptionPane.INFORMATION_MESSAGE);
                        mainComponent.fecth_tester_tickets(user.getId());
                        dispose();

                    }catch(SQLException ex){
                        JOptionPane.showMessageDialog(
                                pnlDetalleCaso,
                                "Ocurrió un error durante la ejecución:\n" + ex.getMessage(),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE
                        );
                        throw new RuntimeException(ex);
                    }
                }else{ errorMessage(ticket); }
            }
        });
        btnRechazar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(btnRechazar.isEnabled()){

                    new ProbadoresRechazo(user, ticket, ProbadoresDetalle.this, mainComponent);
                    fetch_logs(ticket);
                    dispose();
                }else{ errorMessage(ticket); }
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
    // Enlistado de la bitácora del caso
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
    }

    public void errorMessage(Ticket ticket){
        String message = (ticket.getState_id() != 7 || ticket.getState_id() != 5) ? "El desarrollo de este proyecto sigue en proceso. \n Echa un vistazo a la bitácora para conocer más detalles." : "El desarrollo de este proyecto ya ha finalizado." ;
        JOptionPane.showMessageDialog(pnlDetalleCaso,
            message,
            "ERROR", JOptionPane.ERROR_MESSAGE); }
}
