package com.tickets.form.SuperAdmin.Grupos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class GruposMapeo extends JFrame {
    private JPanel pnlGrupoMapeo;
    private JButton button1;
    private JTable tblDisponibles;
    private JTable tblIntegrantes;
    DefaultTableModel model;

    public GruposMapeo() throws SQLException {
        super("Grupos - ");
        setMinimumSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlGrupoMapeo);

        llenarTabla();

        tblDisponibles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                obtenerDatosDisponibles();
            }
        });
    }

    /*
        Paso 1: Llenar las tablas con los usuarios sin grupo y los usuarios que pertenecen al grupo
        Paso 2: Obtener el id del usuario al que se moverá de grupo, ya sea para agregarlo o eliminarlo
        Paso 3: Ejecutar la lógica interna para hacer el cambio en la base de datos
        Paso 4: Refrescar la tabla con los cambios realizados
     */

    public void obtenerDatosDisponibles() {
        // Obteniendo el valor del id del usuario que se moverá de grupo
        System.out.printf(model.getValueAt(tblDisponibles.getSelectedRow(),0).toString());

        int id = (int) model.getValueAt(tblDisponibles.getSelectedRow(),0);
        /*
            Lógica y métodos para mover al usuario
            en base al id obtenido
            try {
                mover_grupo(id);

                llenarTabla();
            } catch (Exception ex) {
                // ...
            }
         */

        llenarTabla();
    }

    public void llenarTabla() {
        // Datos dinámicos de la tabla
        String[] columns = {"ID", "Nombre"};

        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas sean no editables
                return false;
            }
        };

        model.addRow(new Object[]{
                1,
                "Oscar"
        });
        model.addRow(new Object[]{
                2,
                "Elías"
        });

        tblDisponibles.setModel(model);
        tblIntegrantes.setModel(model);
    }
}
