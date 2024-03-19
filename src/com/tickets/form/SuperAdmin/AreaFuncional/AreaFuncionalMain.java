package com.tickets.form.SuperAdmin.AreaFuncional;

import com.tickets.form.SuperAdmin.SuperAdmin;
import com.tickets.model.Area;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AreaFuncionalMain extends JFrame {
    private JPanel pnlAreaFuncional;
    private JLabel lblTitle;
    private JButton btnAgregar;
    private JButton btnSalir;
    private JTable tblAreas;

    private HashMap<Integer, Area> areas_list;

    DefaultTableModel model;

    public AreaFuncionalMain() throws SQLException {
        super("Areas Funcionales - Homepage");
        setMinimumSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlAreaFuncional);

        // Datos dinámicos de la tabla
        String[] columns = {"Préfijo", "Nombre", "Jefe de Área", "Jefe de Desarrollo"};

        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblAreas.setModel(model);
        get_all_areas();

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSalir();
            }
        });
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame frame = null;
                try {
                    frame = new AreaFuncionalNueva();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                frame.setVisible(true);
            }
        });
    }

    public void get_all_areas() throws SQLException {
        Area.fetchAllAreas();

        areas_list = Area.getAll_areas();

        model.setRowCount(0);

        if (!areas_list.isEmpty()) {
            for (Map.Entry<Integer, Area> entry : areas_list.entrySet()) {
                Area area = entry.getValue();
                model.addRow(
                        new Object[]{
                                area.getPrefix_code(),
                                area.getName(),
                                area.getBoss_name(),
                                area.getDev_boss_name()
                        });
            }
        } else {
            model.addRow(new Object[]{"Sin","Areas","Disponibles","..."});
        }
    }

    private void btnSalir(){
        dispose();
        JFrame frame = new SuperAdmin();
        frame.setVisible(true);
    }
}
