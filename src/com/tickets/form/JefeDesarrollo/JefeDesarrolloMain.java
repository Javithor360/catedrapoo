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

public class JefeDesarrolloMain extends JFrame {

    private JPanel pnlDesarrolloJefe;
    private JButton btnSupervisar;
    private JButton btnLogout;
    private JLabel lblTitle;
    private JTable tblTicketsReq;
    private HashMap<String, Ticket> tickets_request;
    DefaultTableModel model;

    public JefeDesarrolloMain(UserSession user) throws SQLException {
        super("Jefe de Desarrollo - Homepage");
        setVisible(true);
        setMaximumSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlDesarrolloJefe);

        // Estableciendo dato dinámico
        lblTitle.setFont(new Font("Consolas", Font.BOLD, 24));
        lblTitle.setText("¡Bienvenido, " + user.getName() + "!");

        String[] columns = { "Código", "Solicitante", "Título" };
        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas sean no editables
                return false;
            }
        };
        tblTicketsReq.setModel(model);
        fetch_tickets_request(user.getId()); // Llenar la tabla con los datos al inicio

        btnSupervisar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    new JefeDesarrolloSupervisar(user);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            pnlDesarrolloJefe,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });

        tblTicketsReq.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    selectItem(user);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            pnlDesarrolloJefe,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void fetch_tickets_request(int user_id) throws SQLException {
        // Obteniendo la información
        JefeDesarrollo.fetchNewTickets(user_id);

        // Preparando la información
        tickets_request = JefeDesarrollo.getTickets_request();

        // Limpiando el modelo existente
        model.setRowCount(0);

        if(!tickets_request.isEmpty()) {
            // Iterando sobre el HashMap y agregando datos al modelo
            for (Map.Entry<String, Ticket> entry : tickets_request.entrySet()) {
                Ticket ticket = entry.getValue();
                model.addRow(new Object[]{ticket.getCode(), ticket.getBoss_name(), ticket.getName()});
            }
        } else {
            model.addRow(new Object[]{ "No", "hay", "datos..." });
        }
    }

    public void selectItem(UserSession user) throws SQLException {
        Ticket selectedTicket = tickets_request.get(model.getValueAt(tblTicketsReq.getSelectedRow(), 0).toString());
        if(selectedTicket != null) {
            new JefeDesarrolloNewRequest(user, selectedTicket, JefeDesarrolloMain.this);
        } else {
            JOptionPane.showMessageDialog(
                    pnlDesarrolloJefe,
                    "No hay datos que mostrar...",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
