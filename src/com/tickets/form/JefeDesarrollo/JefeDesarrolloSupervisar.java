package com.tickets.form.JefeDesarrollo;

import com.tickets.model.JefeDesarrollo;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JefeDesarrolloSupervisar extends JFrame {
    private JPanel pnlSupervisar;
    private JButton btnClose;
    private JTable tblTicketDisplay;
    private HashMap<String, Ticket> ticket_list;
    DefaultTableModel model;

    public JefeDesarrolloSupervisar(UserSession user) throws SQLException {
        super("Jefe de Desarrollo - Supervisar");
        setVisible(true);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        setMaximumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlSupervisar);

        String[] columns = { "Código", "Programador", "Estado" };

        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas sean no editables
                return false;
            }
        };

        tblTicketDisplay.setModel(model);
        get_all_tickets(user.getId());

        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
        tblTicketDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    selectItem(user);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            pnlSupervisar,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void get_all_tickets (int user_id) throws SQLException {
        JefeDesarrollo.fetchAllTickets(user_id);

        ticket_list = JefeDesarrollo.getAll_tickets();

        model.setRowCount(0);

        if(!ticket_list.isEmpty()) {
            for (Map.Entry<String, Ticket> entry : ticket_list.entrySet()) {
                Ticket ticket = entry.getValue();
                model.addRow(new Object[]{ ticket.getCode(), (ticket.getProgrammer_name() != null ? ticket.getProgrammer_name() : "Sin asignación..."), ticket.getState() });
            }
        } else {
            model.addRow(new Object[]{ "No", "hay", "datos..." });
        }
    }

    public void selectItem (UserSession user) throws SQLException {
        Ticket selectedTicket = ticket_list.get(model.getValueAt(tblTicketDisplay.getSelectedRow(), 0).toString());
        if(selectedTicket != null) {
            new JefeDesarrolloDetalle(user, selectedTicket);
        } else {
            JOptionPane.showMessageDialog(
                    pnlSupervisar,
                    "No hay datos que mostrar",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
