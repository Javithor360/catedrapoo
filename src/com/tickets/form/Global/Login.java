package com.tickets.form.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.SQLException;
import com.tickets.form.JefeArea.SolicitanteMain;
import com.tickets.form.JefeDesarrollo.JefeDesarrolloMain;
import com.tickets.form.Programadores.ProgramadoresMain;
import com.tickets.util.Conexion;

public class Login extends JFrame {
    private JPasswordField txtpass;
    private JTextField txtUsuario;
    private JButton btnSalir;
    private JButton btnIngresar;
    private JPanel pnlLogin;

    public Login(String title){
        super(title);
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlLogin);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(getParent());

        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnIngresar();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void btnIngresar(){
        try{
            Conexion conexion = new Conexion();

            String usuario = txtUsuario.getText().trim();
            char[] pass = txtpass.getPassword();
            String contrasena = new String(pass);

            String consulta = "SELECT * FROM empleados WHERE usuario = '" + usuario + "' AND contrasena = '" + contrasena + "'";

            conexion.setQuery(consulta);

            ResultSet resultSet = conexion.getRs();

            if (resultSet.next()) {
                int area = resultSet.getInt("areaId");
                int jefeArea = resultSet.getInt("jefeArea");

                if (area == 1 && (jefeArea == 1 || resultSet.wasNull())) {
                    dispose();
                    JOptionPane.showMessageDialog(null, "Bienvenido Jefe de Area");
                    new JefeDesarrolloMain("Área Desarrollo Jefe").setVisible(true);
                } else if (area == 1 && (jefeArea == 0 || resultSet.wasNull())){
                    dispose();
                    JOptionPane.showMessageDialog(null, "Bienvenido al Area de Desarrollo");
                    new ProgramadoresMain("Área Desarrollo").setVisible(true);
                }else if (area == 3 && (jefeArea == 3 || resultSet.wasNull())) {
                    dispose();
                    JOptionPane.showMessageDialog(null, "Bienvenido al área Solicitante");
                    new SolicitanteMain().setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
                dispose();
                new SolicitanteMain().setVisible(true);
            }

        }catch (SQLException e){
            System.out.println("ERROR:Fallo en SQL: "+ e.getMessage());
            System.exit(0);
        }
    }


}
