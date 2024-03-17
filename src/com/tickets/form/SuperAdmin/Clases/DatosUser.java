package com.tickets.form.SuperAdmin.Clases;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.tickets.util.Conexion;
public class DatosUser {

    private final String select
            = "SELECT p.id,p.name, p.email, p.gender, o.name, p.birthday, p.created_at FROM users p INNER JOIN roles o ON p.role_id = o.id ";

    private final String insert =
           "INSERT INTO users (name, email, password, gender, birthday, role_id, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final String update =
            "UPDATE users SET name=?, email=?, gender=?, birthday=?, role_id=? WHERE id=?";

    private final String delete = "DELETE FROM users WHERE id = ?";

    public String insert(User personaDatos) {
        String mensaje = "" ;
        try{
            Conexion conexion = new Conexion();

            PreparedStatement pstmt = conexion.setQuery(insert);

            pstmt.setString(1, personaDatos.getName());
            pstmt.setString(2, personaDatos.getEmail());
            pstmt.setString(3, personaDatos.getPassword());
            pstmt.setString(4, personaDatos.getGender());
            pstmt.setDate(5, new java.sql.Date(personaDatos.getBirthday().getTime()));
            pstmt.setInt(6, personaDatos.getRole());
            pstmt.setTimestamp(7, new java.sql.Timestamp(personaDatos.getCreatedAt().getTime()));

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                mensaje = "Registro insertado correctamente.";
            } else {
                mensaje = "Error al insertar el registro";
            }

            JOptionPane.showMessageDialog(null, mensaje);

            conexion.closeConnection();

        }catch (SQLException e){
            System.out.println("ERROR:Fallo en SQL INSERT: "+ e.getMessage());
            System.exit(0);
        }

        return mensaje;
    }

    public String update(User personaDatos) {
        PreparedStatement stmt = null;

        String mensaje = "" ;
        int rows = 0;
        try {
            Conexion conexion = new Conexion();
            stmt = conexion.setQuery(update);
            stmt.setString(1, personaDatos.getName());
            stmt.setString(2, personaDatos.getEmail());
            stmt.setString(3, personaDatos.getGender());
            stmt.setDate(4, new java.sql.Date(personaDatos.getBirthday().getTime()));
            stmt.setInt(5, personaDatos.getRole());
            stmt.setInt(6, personaDatos.getId());
            rows = stmt.executeUpdate();

            if (rows > 0) {
                mensaje = "Registro editado correctamente";
            } else {
                mensaje = "Error al insertar el registro";
            }
            JOptionPane.showMessageDialog(null, mensaje);
        } catch (SQLException e) {
            System.out.println("ERROR: Fallo en SQL de UPDATE: " + e.getMessage());
            System.exit(0);
        }
        return mensaje;
    }
    public String delete(int idPersona) {
        String mensaje = "" ;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            Conexion conn = new Conexion();
            stmt = conn.setQuery(delete);
            stmt.setInt(1, idPersona);
            rows = stmt.executeUpdate();
            if (rows > 0) {
                mensaje = "Registro Eliminado correctamente!";
            } else {
                mensaje = "Error al eliminar el registro";
            }
            JOptionPane.showMessageDialog(null, mensaje);
        } catch (SQLException e) {
            System.out.println("ERROR: Fallo en SQL de UPDATE: " + e.getMessage());
            System.exit(0);
        }
        return mensaje;
    }

    public DefaultTableModel selectPersona(){
        DefaultTableModel dtm = new DefaultTableModel();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Conexion conn = new Conexion();
            stmt = conn.setQuery(select);
            rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int numberOfColumns = meta.getColumnCount();
            //Formando encabezado
            for (int i = 1; i <= numberOfColumns; i++) {
                dtm.addColumn(meta.getColumnLabel(i));
            }
            //Creando las filas para el JTable
            while (rs.next()) {
                Object[] fila = new Object[numberOfColumns];
                for (int i = 0; i < numberOfColumns; i++) {
                    fila[i]=rs.getObject(i+1);
                }
                dtm.addRow(fila);
            }

            conn.closeConnection();
        } catch (SQLException e) {
            System.out.println("ERROR:Fallo en SQL SELECT: "+ e.getMessage());
            System.exit(0);
        }
        return dtm;
    }

}
