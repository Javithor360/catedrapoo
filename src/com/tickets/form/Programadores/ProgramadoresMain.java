package com.tickets.form.Programadores;

import com.tickets.model.Bitacora;
import com.tickets.model.Programador;
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

public class ProgramadoresMain extends JFrame{
    private JPanel pnlDesarrolloEmpleados;
    private JButton btnCerrar;
    private JTable tblTickets;
    private JLabel lblTitle;
    private HashMap<String, Ticket> ticket_list;
    DefaultTableModel model;

    public ProgramadoresMain(UserSession user) throws SQLException {
        super("Programadores - Homepage");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlDesarrolloEmpleados);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(getParent());

        lblTitle.setText("¡Bienvenido, " + user.getName() + "!");

        String[] columns = { "Código", "Estado", "Fecha entrega" };
        model = new DefaultTableModel(null, columns) {
          @Override
          public boolean isCellEditable(int row, int column) {
              return false;
          }
        };
        tblTickets.setModel(model);
        fetch_tickets(user.getId());

        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });

        tblTickets.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    select_item(user);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            pnlDesarrolloEmpleados,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void fetch_tickets(int user_id) throws SQLException {
        Programador.fetchTickets(user_id);

        ticket_list = Programador.getAssigned_tickets();

        model.setRowCount(0);

        if(!ticket_list.isEmpty()) {
            for(Map.Entry<String, Ticket> entry : ticket_list.entrySet()) {
                Ticket ticket = entry.getValue();
                model.addRow(new Object[]{ ticket.getCode(), ticket.getState(), ticket.getDue_date() });
            }
        } else {
            model.addRow(new Object[]{ "No", "hay", "datos..." });
        }
    }

    public void select_item (UserSession user) throws SQLException {
        Ticket selectedTicket = ticket_list.get(model.getValueAt(tblTickets.getSelectedRow(), 0).toString());
        if (selectedTicket != null) {
            new ProgramadoresDetalle(user, selectedTicket, ProgramadoresMain.this);
        } else {
            JOptionPane.showMessageDialog(
                    pnlDesarrolloEmpleados,
                    "No hay datos que mostrar...",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
