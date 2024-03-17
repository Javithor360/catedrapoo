package com.tickets.form.SuperAdmin;

import com.tickets.form.SuperAdmin.Clases.User;
import com.tickets.util.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    DefaultTableModel modelo = null;
    HashMap<String, Long> idMap = new HashMap<>();
    User user = null;
    DatosUser personaDatos = null;
    private NuevoEmpleado nuevoEmpleado;
    long rolid;

    public NuevoEmpleado(){
        super("Nuevo Empleado");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pblNuevoEmpleado);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(getParent());

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

        cmbRol.addActionListener( e -> {
            String name = (String) cmbRol.getSelectedItem();
            long id = idMap.get(name);

            rolid = id;


        });
    }

    private void btnGuardar() {

        if (txtNombre.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                new String(pass.getPassword()).isEmpty() ||
                new String(passConf.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pass1 = new String(pass.getPassword());
        String pass2 = new String(passConf.getPassword());
        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            pass.setText("");
            passConf.setText("");
            pass.requestFocus();
            return;
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

        user = new User(name,correo, pass, genero, fecha, rol, creado);
        if(btnGuardar.getText().equals("Guardar")) {
            personaDatos.insert(user);
        }else if (btnGuardar.getText().equals("Editar")) {
        }

        modelo=personaDatos.selectPersona();
        tblDatos.setModel(modelo);

    }

    private void btnVolver(){
        dispose();
        JFrame frame = new SuperAdmin();
        frame.setVisible(true);
    }

}
