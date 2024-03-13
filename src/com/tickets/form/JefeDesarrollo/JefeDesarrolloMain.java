package com.tickets.form.JefeDesarrollo;

import com.tickets.model.UserSession;
import com.tickets.util.Conexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JefeDesarrolloMain extends JFrame {

    private JPanel pnlDesarrolloJefe;
    private JButton btnSupervisar;
    private JButton btnLogout;
    private JLabel lblTableData;
    private JLabel lblTitle;

    UserSession user = getUser(); // TEMPORAL

    public JefeDesarrolloMain() throws SQLException {
        super("Jefe de Desarrollo - Homepage");
        setVisible(true);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlDesarrolloJefe);

         // método temporal

        // Estableciendo dato dinámico
        lblTitle.setFont(new Font("Consolas", Font.BOLD, 24));
        lblTitle.setText("¡Bienvenido, " + user.getName() + "!");


        btnSupervisar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new JefeDesarrolloSupervisar(user);
            }
        });
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
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
                    rs.getString("profile_pic"),
                    rs.getInt("role_id"),
                    rs.getDate("created_at")
            );
        }

        return user;
    }
}
