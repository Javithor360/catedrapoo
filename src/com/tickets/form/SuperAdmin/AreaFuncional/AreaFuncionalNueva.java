package com.tickets.form.SuperAdmin.AreaFuncional;

import com.tickets.form.SuperAdmin.NuevoEmpleado;
import com.tickets.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.HashMap;

public class AreaFuncionalNueva extends JFrame {
    private JPanel pblNuevoEmpleado;
    private JPanel pnlAreaNew;
    private JLabel lblTitulo;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblPass;
    private JLabel lblGrupos;
    private JTextField txtPrefix;
    private JTextField txtNombre;
    private JButton btnGuardar;
    private JLabel lblConfirm;
    private JButton btnVolver;
    private JButton btnLimpiar;
    private JComboBox cmbDevBoss;
    private JComboBox cmbBoss;
    private JLabel lblNumGrupo;
    private JLabel lblNumGrupoProgra;
    private JLabel lblGrupoProgramadores;
    private int NumGrupoEmpleado;
    private int NumGrupoProgramadores;

    private HashMap<Integer, JefeArea> bosses_list;
    private HashMap<Integer, JefeDesarrollo> dev_bosses_list;

    public AreaFuncionalNueva() throws SQLException {
        super("Áreas Funcionales - Agregar");
        setMinimumSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlAreaNew);

        fillGrupos();

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
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validaryGuardar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void fillGrupos() throws SQLException {
        Grupo.fetchAllGroups();

        HashMap<Integer, Grupo> gruposExistentes = Grupo.getAll_grupos();
        // Obtener el número de grupos ya existentes
        int size = gruposExistentes.size();
        // Predefinir sus próximos números de grupo
        setNumGrupoEmpleado(size + 1);
        setNumGrupoProgramadores(size + 2);

        lblNumGrupo.setText(String.valueOf(getNumGrupoEmpleado()));
        lblNumGrupoProgra.setText(String.valueOf(getNumGrupoProgramadores()));
    }

    public void validarPrefix() {
        String prefijo = txtPrefix.getText();
        if (prefijo.length() != 3) {
            JOptionPane.showMessageDialog(null,
                    "El prefijo debe tener una extensión de 3 dígitos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtPrefix.setText("");
        }
    }

    public int extraerJefe(String cmbSeleccionado) {
        // Encontrar la posición del primer punto
        int dotIndex = cmbSeleccionado.indexOf('.');
        // Extraer los caracteres hasta el primer punto
        String idExtraido = cmbSeleccionado.substring(0, dotIndex);

        // Convertir la cadena extraída a entero (si es necesario)
        int id = Integer.parseInt(idExtraido);

        return id;
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
                    .map(JefeArea::getItem)
                    .collect(Collectors.toList()); // Obtener los nombres de los jefes de área disponibles
            String[] bossArray = values.toArray(new String[0]); // Convertir la lista de nombres a un arreglo

            // Asignar los valores al JComboBox
            cmbBoss.setModel(new DefaultComboBoxModel<>(bossArray));
            cmbBoss.setEnabled(true); // Habilitar el JComboBox
            hayJefes = true;
        } else {
            // Deshabilitar opciones de guardado
            cmbBoss.setModel(new DefaultComboBoxModel<>(new String[]{"Sin jefes de área disponibles"}));
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

        if (!dev_bosses_list.isEmpty()) {
            Collection<String> values = dev_bosses_list.values().stream()
                    .map(JefeDesarrollo::getItem)
                    .collect(Collectors.toList()); // Obtener los nombres de los jefes de área disponibles
            String[] bossArray = values.toArray(new String[0]); // Convertir la lista de nombres a un arreglo

            // Asignar los valores al JComboBox
            cmbDevBoss.setModel(new DefaultComboBoxModel<>(bossArray));
            cmbDevBoss.setEnabled(true); // Habilitar el JComboBox

            hayJefesDesarollo = true;
        } else {
            // Deshabilitar opciones de guardado
            cmbDevBoss.setModel(new DefaultComboBoxModel<>(new String[]{"Sin jefes de desarollo disponibles"}));
            cmbDevBoss.setEnabled(false); // Deshabilitar el JComboBox
            hayJefesDesarollo = false;
        }

        return hayJefesDesarollo;
    }

    // ===============================================================

    public int getNumGrupoEmpleado() {
        return NumGrupoEmpleado;
    }

    public void setNumGrupoEmpleado(int numGrupoEmpleado) {
        NumGrupoEmpleado = numGrupoEmpleado;
    }

    public int getNumGrupoProgramadores() {
        return NumGrupoProgramadores;
    }

    public void setNumGrupoProgramadores(int numGrupoProgramadores) {
        NumGrupoProgramadores = numGrupoProgramadores;
    }

    // Acciones =======================================================
    public void validaryGuardar() throws SQLException {
        if (txtNombre.getText().isEmpty() ||
                txtPrefix.getText().isEmpty() ||
                cmbBoss.getSelectedIndex() == -1 ||
                cmbDevBoss.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        validarPrefix();

        String name = txtNombre.getText();
        String prefijo = txtPrefix.getText();
        int id_boss =  extraerJefe(cmbBoss.getSelectedItem().toString());
        int id_dev_boss =  extraerJefe(cmbDevBoss.getSelectedItem().toString());
        int NuevaAreaID = -1; // Valor por defecto en caso de error

        // Crear nueva Area ======================================================
        Area nuevaArea = new Area(name,prefijo,id_boss,id_dev_boss); // Instancia
        NuevaAreaID = nuevaArea.insert(nuevaArea); // Guardar en la BD

        // Crear sus Respectivos Grupos ==========================================
        Grupo grupoEmpleados = new Grupo("Empleados " + name);
        grupoEmpleados.insert(grupoEmpleados);
        Grupo grupoProgramadores = new Grupo("Programadores para " + prefijo);
        grupoProgramadores.insert(grupoProgramadores);

        // Asignar los Jefes a los respectivos Grupos ============================
        MapeoAsignacion JefeGrupoEmpleado = new MapeoAsignacion(id_boss, NuevaAreaID, getNumGrupoEmpleado());
        JefeGrupoEmpleado.insert(JefeGrupoEmpleado);
        MapeoAsignacion JefeGrupoDesarrollo = new MapeoAsignacion(id_dev_boss, NuevaAreaID, getNumGrupoProgramadores());
        JefeGrupoDesarrollo.insert(JefeGrupoDesarrollo);

        // Reiniciar FORM
        reiniciarForm();
    }
    private void reiniciarForm() throws SQLException {
        dispose();
        JFrame frame = new AreaFuncionalNueva();
        frame.setVisible(true);
    }
    private void btnVolver() throws SQLException {
        dispose();
        JFrame frame = new AreaFuncionalMain();
        frame.setVisible(true);
    }
}