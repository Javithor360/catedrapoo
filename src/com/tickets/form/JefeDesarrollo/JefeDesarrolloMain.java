package com.tickets.form.JefeDesarrollo;

import com.tickets.model.JefeDesarrollo;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;
import com.tickets.util.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
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
    UserSession user = getUser(); // TEMPORAL

    public JefeDesarrolloMain() throws SQLException {
        super("Jefe de Desarrollo - Homepage");
        setVisible(true);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlDesarrolloJefe);

        // Estableciendo dato dinámico
        lblTitle.setFont(new Font("Consolas", Font.BOLD, 24));
        lblTitle.setText("¡Bienvenido, " + user.getName() + "!");

        String[] columns = { "Código", "Solicitante", "Título" };
        model = new DefaultTableModel(null, columns);
        tblTicketsReq.setModel(model);

        btnSupervisar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    new JefeDesarrolloSupervisar(user);
                } catch (SQLException ex) {
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

        fetch_tickets_request(); // Llenar la tabla con los datos al inicio
        tblTicketsReq.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    selectItem(user, e);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static UserSession getUser() throws SQLException {
        Conexion conexion = new Conexion();
        UserSession user = null;

        String query = "SELECT * FROM users WHERE `email` = 'jose.aguilar@correo.com' AND `password` = '1234'";
        conexion.setRs(query);

        ResultSet rs = conexion.getRs();
        if(rs.next()) {
            user = new UserSession(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getDate("birthday"),
                    rs.getInt("role_id"),
                    rs.getDate("created_at")
            );
        }

        return user;
    }

    public void fetch_tickets_request() throws SQLException {
        // Obteniendo la información
        JefeDesarrollo.fetchNewTickets(user.getId());

        // Preparando la información
        tickets_request = JefeDesarrollo.getTickets_request();

        // Limpiando el modelo existente
        model.setRowCount(0);

        // Iterando sobre el HashMap y agregando datos al modelo
        for (Map.Entry<String, Ticket> entry : tickets_request.entrySet()) {
            Ticket ticket = entry.getValue();
            model.addRow(new Object[]{ticket.getCode(), ticket.getBoss_name(), ticket.getName()});
        }
    }

    public void selectItem(UserSession user, MouseEvent e) throws SQLException {
//        String codigo = model.getValueAt(tblTicketsReq.getSelectedRow(), 0).toString();
//        Ticket element = tickets_request.get(codigo);
//        System.out.println("Tu padre: " + codigo);
//        System.out.println(element);
        Ticket selectedTicket = tickets_request.get(model.getValueAt(tblTicketsReq.getSelectedRow(), 0).toString());
        new JefeDesarrolloNewRequest(user, selectedTicket);
    }
}
