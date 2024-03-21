package com.tickets.form.JefeArea;

import com.tickets.form.Programadores.ProgramadoresDetalle;
import com.tickets.form.Programadores.ProgramadoresMain;
import com.tickets.model.Bitacora;
import com.tickets.model.JefeArea;
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

public class JefeAreaMain extends JFrame {
    private JLabel lblTitle;
    private JTable tblTicketsArea;
    private JButton btnNewTicket;
    private JButton btnCerrar;
    private JPanel pnlSolicitante;
    DefaultTableModel model;
    private HashMap<String, Ticket> ticket_list;

    public JefeAreaMain(UserSession user) throws SQLException {
        super("Jefe de área funcional - Homepage");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlSolicitante);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(getParent());

        lblTitle.setText("¡Bienvenido, " + user.getName() + "!");
        String[] columns = { "Código", "Estado", "Fecha entrega", "Porcentaje avance" };
        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTicketsArea.setModel(model);
        fetch_area_tickets(user.getId());


        btnNewTicket.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new JefeAreaApertura(user, JefeAreaMain.this);
            }
        });


        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });

        tblTicketsArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    select_item(user);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            pnlSolicitante,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void fetch_area_tickets(int user_id) throws SQLException {
        JefeArea.fetchAreaTickets(user_id);

        ticket_list = JefeArea.getAssigned_tickets();

        model.setRowCount(0);

        if(!ticket_list.isEmpty()) {
            for(Map.Entry<String, Ticket> entry : ticket_list.entrySet()) {
                Ticket ticket = entry.getValue();
                model.addRow(new Object[]{ ticket.getCode(), ticket.getState(), ticket.getDue_date(), get_latest_percent(ticket)+"%" });
            }
        } else {
            model.addRow(new Object[]{ "Todavía","no", "hay", "datos..." });
        }
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

    public void select_item (UserSession user) throws SQLException {
        Ticket selectedTicket = ticket_list.get(model.getValueAt(tblTicketsArea.getSelectedRow(), 0).toString());
        if (selectedTicket != null) {
            new JefeAreaDetalle(user, selectedTicket, JefeAreaMain.this);
        } else {
            JOptionPane.showMessageDialog(
                    pnlSolicitante,
                    "No hay datos que mostrar...",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
