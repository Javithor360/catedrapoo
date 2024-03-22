package com.tickets.form.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.SQLException;

import com.tickets.form.JefeArea.JefeAreaMain;
import com.tickets.form.JefeDesarrollo.JefeDesarrolloMain;
import com.tickets.form.Probadores.ProbadoresMain;
import com.tickets.form.Programadores.ProgramadoresMain;
import com.tickets.model.UserSession;
import com.tickets.util.Conexion;
import com.tickets.form.SuperAdmin.*;

public class Login extends JFrame {
    private JPasswordField txtpass;
    private JTextField txtUsuario;
    private JButton btnSalir;
    private JButton btnIngresar;
    private JPanel pnlLogin;

    public Login() {
        super("Sistema de Casos - Inicio de sesión");
        setVisible(true);
        setMaximumSize(new Dimension(500, 500));
        setSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlLogin);

        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnIngresar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void btnIngresar() throws SQLException {
        try{
            String user = txtUsuario.getText().trim();
            char[] pass = txtpass.getPassword();
            String contrasena = new String(pass);

            if (user.equals("admin") && contrasena.equals("superadmin")){
                dispose();
                JOptionPane.showMessageDialog(null, "Bienvenido Super Admin!");
                new SuperAdmin().setVisible(true);
            } else {
                Conexion conexion = new Conexion();
                UserSession userInfo;

                String query = "SELECT * FROM users WHERE email = \"" + user + "\" AND password = \"" + contrasena + "\";";
                conexion.setRs(query);

                ResultSet rs = conexion.getRs();
                if (rs.next()) {
                    userInfo = new UserSession(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getDate("birthday"),
                        rs.getInt("role_id"),
                        rs.getDate("created_at")
                    );

                    if (userInfo.getRole_id() == 1) {
                        new JefeDesarrolloMain(userInfo);
                    } else if (userInfo.getRole_id() == 2) {
                       new ProgramadoresMain(userInfo);
                    } else if (userInfo.getRole_id() == 3) {
                        new JefeAreaMain(userInfo);
                    } else if (userInfo.getRole_id() == 4) {
                        new ProbadoresMain(userInfo);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró ningún usuario que coincida con los datos ingresados", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                conexion.closeConnection();
            }

        } catch (SQLException e){
            System.out.println("ERROR:Fallo en SQL: "+ e.getMessage());
            System.exit(0);
        }
    }


}
