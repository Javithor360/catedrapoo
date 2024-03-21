package com.tickets.form.SuperAdmin.Grupos;

import com.tickets.form.Global.Login;
import com.tickets.model.Grupo;
import com.tickets.model.MapeoAsignacion;
import com.tickets.form.SuperAdmin.Clases.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GruposMapeo extends JFrame {
    private JPanel pnlGrupoMapeo;
    private JButton btnSalir;
    private JTable tblDisponibles;
    private JTable tblIntegrantes;
    private JLabel lblIntegrantes;
    private JPanel lblDisponibles;
    private JLabel titleGrupoMapeo;
    private int grupoId;
    private HashMap<Integer, User> dispUsers_list;
    private HashMap<Integer, User> inGroup_list;
    DefaultTableModel model;
    DefaultTableModel modelIntegrantes;

    public GruposMapeo(String id) throws SQLException {
        super("Grupo N° " + id);
        setMinimumSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlGrupoMapeo);

        setGrupoId(Integer.parseInt(id));
        llenarTabla();

        tblDisponibles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    obtenerDatosDisponibles();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        tblIntegrantes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    obtenerDatosEnGrupo();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnSalir.addActionListener(new ActionListener() {
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

    /*
        Paso 2: Obtener el id del usuario al que se moverá de grupo, ya sea para agregarlo o eliminarlo
        Paso 3: Ejecutar la lógica interna para hacer el cambio en la base de datos
        Paso 4: Refrescar la tabla con los cambios realizados
     */

    public void obtenerDatosDisponibles() throws SQLException {
        // Obteniendo el valor del id del usuario que se moverá de grupo
        int id = (int) model.getValueAt(tblDisponibles.getSelectedRow(), 0);

        try {
            Grupo insertDispUserToGrup = new Grupo(id, getGrupoId());
            insertDispUserToGrup.insertUser(insertDispUserToGrup);
            llenarTabla();
        } catch (Exception ex) {
            System.out.printf(String.valueOf(ex));
        }
    }

    public void obtenerDatosEnGrupo() throws SQLException {
        // Obteniendo el valor del id del usuario que se moverá de grupo
        int id = (int) modelIntegrantes.getValueAt(tblIntegrantes.getSelectedRow(), 0);

        try {
            Grupo deleteUserFromGroup = new Grupo(id, getGrupoId());
            deleteUserFromGroup.deleteUser(deleteUserFromGroup);
            llenarTabla();
        } catch (Exception ex) {
            System.out.printf(String.valueOf(ex));
        }
    }

    // ===============================================================

    public void llenarTabla() throws SQLException {
        // Datos dinámicos de la tabla
        String[] columns = {"ID", "Nombre", "Email"};

        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas sean no editables
                return false;
            }
        };

        tblDisponibles.setModel(model);
        get_disp_users();

        modelIntegrantes = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que todas las celdas sean no editables
                return false;
            }
        };

        tblIntegrantes.setModel(modelIntegrantes);
        get_inGroup_users();
    }

    // ===============================================================
    public void get_disp_users() throws SQLException {
        MapeoAsignacion.fetchDispUsers();
        dispUsers_list = MapeoAsignacion.getDisp_users();

        model.setRowCount(0);

        if (!dispUsers_list.isEmpty()) {
            for (Map.Entry<Integer, User> entry : dispUsers_list.entrySet()) {
                User usuario = entry.getValue();
                model.addRow(
                        new Object[]{
                                usuario.getId(),
                                usuario.getName(),
                                usuario.getEmail(),
                        });
            }
        } else {
            model.addRow(new Object[]{"Sin", "Usuarios", "Disponibles"});
        }
    }

    // ===============================================================
    public void get_inGroup_users() throws SQLException {
        MapeoAsignacion.fetchUserGroup(getGrupoId());
        inGroup_list = MapeoAsignacion.getIngroup_users();

        modelIntegrantes.setRowCount(0);

        if (!inGroup_list.isEmpty()) {
            for (Map.Entry<Integer, User> entry : inGroup_list.entrySet()) {
                User usuario = entry.getValue();
                modelIntegrantes.addRow(
                        new Object[]{
                                usuario.getId(),
                                usuario.getName(),
                                usuario.getEmail(),
                        });
            }
        } else {
            modelIntegrantes.addRow(new Object[]{"Sin", "Integrantes", "del Grupo"});
        }
    }

    // ===============================================================
    public int getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }

}
