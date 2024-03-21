package com.tickets.form.SuperAdmin.Grupos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class GruposMapeo extends JFrame {
    private JPanel pnlGrupoMapeo;
    private JButton button1;
    private JTable table1;
    private JTable table2;

    DefaultTableModel model;

    GruposMapeo() throws SQLException {
        super("Grupos - ");
        setMinimumSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlGrupoMapeo);

        // Datos dinámicos de la tabla
        String[] columns = {"ID", "Area","Grupo N°","Cantidad"};
    }
}
