package com.tickets.form.SuperAdmin.Grupos;

import com.tickets.form.SuperAdmin.Clases.ButtonEditor;
import com.tickets.form.SuperAdmin.Clases.ButtonRenderer;
import com.tickets.form.SuperAdmin.SuperAdmin;
import com.tickets.model.Grupo;
import com.tickets.model.MapeoAsignacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GruposMain extends JFrame {
    private JPanel pnlGrupos;
    private JLabel lblTitle;
    private JButton btnVolver;
    private JTable tblGrupos;

    private HashMap<Integer, MapeoAsignacion> grupos_list;

    DefaultTableModel model;

    public GruposMain() throws SQLException {
        super("Grupos - HomePage");
        setMinimumSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlGrupos);

        // Datos dinámicos de la tabla
        String[] columns = {"ID", "Area", "Nombre Grupo", "Jefe", "Cantidad", "Acción"};
        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return true;
            }
        };

        tblGrupos.setModel(model);
        get_all_grupos();
        addBtnEditColumn();

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame frame = new SuperAdmin();
                frame.setVisible(true);
            }
        });
    }

    public void get_all_grupos() throws SQLException {
        MapeoAsignacion.fetchAllAsignaciones();

        grupos_list = MapeoAsignacion.getAll_asignaciones();

        model.setRowCount(0);

        if (!grupos_list.isEmpty()) {
            for (Map.Entry<Integer, MapeoAsignacion> entry : grupos_list.entrySet()) {
                MapeoAsignacion grupo = entry.getValue();
                model.addRow(
                        new Object[]{
                                grupo.getId(),
                                grupo.getNombre_area(),
                                grupo.getNombre_grupo(),
                                grupo.getNombre_jefe(),
                                (grupo.getTotal_integrantes() - 1 )
                        });
            }
        } else {
            model.addRow(new Object[]{"Sin", "Areas", "Disponibles"});
        }
    }

    private void addBtnEditColumn() {
        // Índice de la columna de acción
        int actionColumnIndex = 5;

        // Configurar el renderizador de celda para la columna de acción
        tblGrupos.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(new ButtonRenderer());

        // Configurar el editor de celda para la columna de acción
        tblGrupos.getColumnModel().getColumn(actionColumnIndex).setCellEditor(new ButtonEditor(tblGrupos));
    }
}
