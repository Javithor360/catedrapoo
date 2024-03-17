package com.tickets.form.SuperAdmin;

import com.tickets.form.SuperAdmin.Clases.User;
import com.tickets.util.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.tickets.form.SuperAdmin.Clases.DatosUser;
public class NuevoEmpleado extends JFrame{
    private JPanel pnlAdmin;
    private JButton btnGuardar;
    private JButton btnVolver;
    private JLabel lblTitulo;
    private JPanel pblNuevoEmpleado;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblPass;
    private JLabel lblGenero;
    private JLabel lblCumple;
    private JLabel lblRol;
    private JComboBox cmbRol;
    private JComboBox cmbGenero;
    private JTextField txtEmail;
    private JTextField txtNombre;
    private JTextField txtFechaNacc;
    private JPasswordField pass;
    private JPasswordField passConf;
    private JLabel lblConfirm;
    private JTable tblDatos;
    private JButton btnLimpiar;
    private JTextField txtId;
    private JButton btnEliminar;

    DefaultTableModel modelo = null;


    HashMap<String, Long> idMap = new HashMap<>();
    User user = null;
    DatosUser personaDatos = new DatosUser();
    private NuevoEmpleado nuevoEmpleado;
    long rolid;

    public NuevoEmpleado(){
        super("Nuevo Empleado");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pblNuevoEmpleado);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(getParent());

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo = personaDatos.selectPersona();
        tblDatos.setModel(modelo);

        txtId.setVisible(false);
        btnEliminar.setVisible(false);

        try{
            Conexion conexion = new Conexion();

            String sql = "SELECT * FROM roles";
            conexion.setRs(sql);


            ResultSet rs = conexion.getRs();
            while (rs.next()) {
                long id = rs.getInt("id");
                String name = rs.getString("name");

               cmbRol.addItem(name);

               idMap.put(name, id);

                String firstRoleName = idMap.keySet().iterator().next();
                rolid = idMap.get(firstRoleName);
                cmbRol.setSelectedItem(firstRoleName);

            }

            conexion.closeConnection();

        }catch (SQLException e){
            System.out.println("ERROR:Fallo en SQL: "+ e.getMessage());
            System.exit(0);
        }

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnVolver();
            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnGuardar();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLimpiar();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnEliminar();
            }
        });

        tblDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tblObtenerDato(e);
            }
        });

        cmbRol.addActionListener( e -> {
            String name = (String) cmbRol.getSelectedItem();

            long id = idMap.get(name);
            rolid = id;

        });
    }

    private void btnGuardar() {

        String pass1 = "";

        int id = 0;

        if (btnGuardar.getText().equals("Guardar")){
            if (txtNombre.getText().isEmpty() ||
                    txtEmail.getText().isEmpty() ||
                    new String(pass.getPassword()).isEmpty() ||
                    new String(passConf.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


        }else if (btnGuardar.getText().equals("Editar")){
            if (txtNombre.getText().isEmpty() ||
                    txtEmail.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String txtid = txtId.getText();

             id = Integer.parseInt(txtid);
        }

        if (btnGuardar.getText().equals("Guardar")) {
            pass1 = new String(pass.getPassword());
            String pass2 = new String(passConf.getPassword());
            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                pass.setText("");
                passConf.setText("");
                pass.requestFocus();
                return;
            }
        }



        String name = txtNombre.getText();
        String correo = txtEmail.getText();
        String pass = pass1;
        String genero = (String) cmbGenero.getSelectedItem();
        String fechaNac = txtFechaNacc.getText();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Date fecha;
        try {
            fecha = sdf.parse(fechaNac);

            // Validar el año
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);
            int year = cal.get(Calendar.YEAR);
            if (year < 1980) {
                JOptionPane.showMessageDialog(this, "El año de la fecha no puede ser anterior a 1980.", "Error", JOptionPane.ERROR_MESSAGE);
                txtFechaNacc.setText("");
                txtFechaNacc.requestFocus();
                return;
            }

            // Validar el mes
            int month = cal.get(Calendar.MONTH) + 1; // Se suma 1 porque los meses van de 0 a 11
            if (month <= 0 || month > 12) {
                JOptionPane.showMessageDialog(this, "El mes debe estar entre 1 y 12.", "Error", JOptionPane.ERROR_MESSAGE);
                txtFechaNacc.setText("");
                txtFechaNacc.requestFocus();
                return;
            }

            // Validar los días
            int day = cal.get(Calendar.DAY_OF_MONTH);
            if (day <= 0 || day > 31) {
                JOptionPane.showMessageDialog(this, "El día debe estar entre 1 y 31.", "Error", JOptionPane.ERROR_MESSAGE);
                txtFechaNacc.setText("");
                txtFechaNacc.requestFocus();
                return;
            }

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "La fecha debe estar en formato 'yyyy-MM-dd'.", "Error", JOptionPane.ERROR_MESSAGE);
            txtFechaNacc.setText("");
            txtFechaNacc.requestFocus();
            return;
        }


        int rol = ((int)rolid);
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp time = new Timestamp(currentTimeMillis);
        Timestamp creado = time;

        if(btnGuardar.getText().equals("Guardar")) {
            user = new User(name,correo, pass, genero, fecha, rol, creado);

        }else if (btnGuardar.getText().equals("Editar")) {
            user = new User(id,name,correo, genero, fecha, rol);

        }

        if(btnGuardar.getText().equals("Guardar")) {
            personaDatos.insert(user);
            this.btnLimpiar();
        }else if (btnGuardar.getText().equals("Editar")) {
            personaDatos.update(user);
            this.btnLimpiar();
        }

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }

        // Volver a cargar los datos en el DefaultTableModel
        modelo = personaDatos.selectPersona();
        tblDatos.setModel(modelo);

    }

    private void btnLimpiar() {
        txtId.setText("");
        txtId.setVisible(false);
        btnEliminar.setVisible(false);
        txtNombre.setText("");
        txtEmail.setText("");
        txtFechaNacc.setText("");
        cmbGenero.setSelectedIndex(0);
        cmbGenero.setSelectedIndex(0);
        pass.setText("");
        passConf.setText("");
        pass.setEnabled(true);
        passConf.setEnabled(true);
        btnGuardar.setText("Guardar");
    }

    private void tblObtenerDato(MouseEvent e) {
        txtId.setVisible(true);
        btnEliminar.setVisible(true);
        int fila = tblDatos.rowAtPoint(e.getPoint());
        int columna = tblDatos.columnAtPoint(e.getPoint());
        if ((fila > -1) && (columna > -1)){
            txtId.setText(modelo.getValueAt(fila,0).toString());
            txtNombre.setText(modelo.getValueAt(fila,1).toString());
            txtEmail.setText(modelo.getValueAt(fila,2).toString());
            pass.setEnabled(false);
            passConf.setEnabled(false);
            cmbGenero.setSelectedItem(modelo.getValueAt(fila,3).toString());
            cmbRol.setSelectedItem(modelo.getValueAt(fila,4).toString());
            txtFechaNacc.setText(modelo.getValueAt(fila,5).toString());
            btnGuardar.setText("Editar");
        }
    }

    private void btnEliminar(){
        int id = Integer.parseInt(txtId.getText());

        int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este registro?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            personaDatos.delete(id);
            btnLimpiar();
            modelo = personaDatos.selectPersona();
            tblDatos.setModel(modelo);
        } else {
            JOptionPane.showMessageDialog(null, "La eliminación ha sido cancelada.");
            this.btnLimpiar();
        }

    }

    private void btnVolver(){
        dispose();
        JFrame frame = new SuperAdmin();
        frame.setVisible(true);
    }

}
