package com.tickets.form.SuperAdmin.AreaFuncional;

import com.tickets.form.SuperAdmin.SuperAdmin;
import com.tickets.model.Area;
import com.tickets.model.JefeArea;
import com.tickets.model.JefeDesarrollo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

public class AreaFuncionalNueva extends JFrame {
    private JPanel pblNuevoEmpleado;
    private JPanel pnlAreaNew;
    private JLabel lblTitulo;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblPass;
    private JLabel lblGenero;
    private JTextField txtPrefix;
    private JTextField txtNombre;
    private JButton btnGuardar;
    private JLabel lblConfirm;
    private JButton btnVolver;
    private JButton btnLimpiar;
    private JComboBox cmbDevBoss;
    private JComboBox cmbBoss;
    private JLabel lblGroup;

    private HashMap<Integer, JefeArea> bosses_list;
    private HashMap<Integer, JefeDesarrollo> dev_bosses_list;

    public AreaFuncionalNueva() throws SQLException {
        super("Áreas Funcionales - Agregar");
        setMinimumSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlAreaNew);

        // Llenar ComboBox con opciones de jefes de area disponibles
        boolean flag1 = llenarCMBJefesArea();

        // Llenar ComboBox con opciones de jefes de area desarrollo
        boolean flag2 = llenarCMBJefesDesarrollo();

        // Habilitar btnGuardar
        btnGuardar.setEnabled(flag1 && flag2);

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    btnVolver();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    // ===============================================================

    public void get_disp_bosses() throws SQLException {
        JefeArea.fetchAvailableBosses();
        bosses_list = JefeArea.getAvailables_bosses();
    }

    // LLenar el JComboBox con los jefes de área disponibles
    private boolean llenarCMBJefesArea() throws SQLException {
        boolean hayJefes = false;

        // Obtener los jefes de área disponibles desde la base de datos
        get_disp_bosses();

        if (!bosses_list.isEmpty()) {
            Collection<String> values = bosses_list.values().stream()
                    .map(JefeArea::getName)
                    .collect(Collectors.toList()); // Obtener los nombres de los jefes de área disponibles
            String[] bossArray = values.toArray(new String[0]); // Convertir la lista de nombres a un arreglo

            // Asignar los valores al JComboBox
            cmbBoss.setModel(new DefaultComboBoxModel<>(bossArray));
            cmbBoss.setEnabled(true); // Habilitar el JComboBox
            hayJefes = true;
        } else {
            // Deshabilitar opciones de guardado
            cmbBoss.setModel(new DefaultComboBoxModel<>(new String[] {"Sin jefes de área disponibles"}));
            cmbBoss.setEnabled(false); // Deshabilitar el JComboBox
            hayJefes = false;
        }

        return hayJefes;
    }

    // ===============================================================

    public void get_disp_dev_bosses() throws SQLException {
        JefeDesarrollo.fetchAvailableDevBosses();
        dev_bosses_list = JefeDesarrollo.getAvailables_dev_bosses();
    }

    // LLenar el JComboBox con los jefes de Desarollo disponibles
    private boolean llenarCMBJefesDesarrollo() throws SQLException {
        boolean hayJefesDesarollo = false;
        // Obtener los jefes de desarrollo desde la base de datos
        get_disp_dev_bosses();

        if(!dev_bosses_list.isEmpty()) {
            Collection<String> values = dev_bosses_list.values().stream()
                    .map(JefeDesarrollo::getName)
                    .collect(Collectors.toList()); // Obtener los nombres de los jefes de área disponibles
            String[] bossArray = values.toArray(new String[0]); // Convertir la lista de nombres a un arreglo

            // Asignar los valores al JComboBox
            cmbDevBoss.setModel(new DefaultComboBoxModel<>(bossArray));
            cmbDevBoss.setEnabled(true); // Habilitar el JComboBox

            hayJefesDesarollo = true;
        } else {
            // Deshabilitar opciones de guardado
            cmbDevBoss.setModel(new DefaultComboBoxModel<>(new String[] {"Sin jefes de desarollo disponibles"}));
            cmbDevBoss.setEnabled(false); // Deshabilitar el JComboBox
            hayJefesDesarollo = false;
        }

        return hayJefesDesarollo;
    }

    private void btnVolver() throws SQLException {
        dispose();
        JFrame frame = new AreaFuncionalMain();
        frame.setVisible(true);
    }
}