package com.tickets.form.SuperAdmin;

import com.tickets.form.Global.Login;
import com.tickets.form.SuperAdmin.AreaFuncional.AreaFuncionalMain;
import com.tickets.form.SuperAdmin.Grupos.GruposMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SuperAdmin extends JFrame{
    private JPanel pnlAdmin;
    private JButton btnNuevo;
    private JButton btnSalir;
    private JButton btnArea;
    private JButton btnGrupo;
    private JLabel lblTitulo;

    public SuperAdmin(){
        super("Gestion de Personal");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlAdmin);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(getParent());

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSalir();
            }
        });
        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame frame = new NuevoEmpleado();
                frame.setVisible(true);
            }
        });
        btnArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame frame = null;
                try {
                    frame = new AreaFuncionalMain();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                frame.setVisible(true);
            }
        });
        btnGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame frame = null;
                try {
                    frame = new GruposMain();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                frame.setVisible(true);
            }
        });
    }

    private void btnSalir(){
        dispose();
        JFrame frame = new Login();
        frame.setVisible(true);
    }

}
