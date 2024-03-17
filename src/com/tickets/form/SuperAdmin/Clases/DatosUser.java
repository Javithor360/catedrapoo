package com.tickets.form.SuperAdmin.Clases;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.tickets.util.Conexion;
public class DatosUser {

    private final String select
            = "SELECT p.id,p.name, p.email, p.gender, o.name, p.birthday, p.created_at FROM users p INNER JOIN roles o ON p.role_id = o.id ";

    private final String insert
            = "INSERT INTO users (name, email, password, gender, birthday, role_id, created_at";
    DefaultTableModel dtm = new DefaultTableModel();
    PreparedStatement stmt = null;


    public String insert(User personaDatos) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
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

            pstmt.executeUpdate();

            pstmt.close();
            conexion.closeConnection();

            mensaje = "Registro insertado correctamente en la tabla users.";

            JOptionPane.showMessageDialog(null, mensaje);

        }catch (SQLException e){
            System.out.println("ERROR:Fallo en SQL: "+ e.getMessage());
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
            e.printStackTrace();
        }
        return dtm;
    }

}
