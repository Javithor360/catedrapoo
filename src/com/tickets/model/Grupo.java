package com.tickets.model;

import com.tickets.util.Conexion;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Grupo {
    // Atributos ===================================
    private int id;
    private String nombre;
    private int idUser;
    private String tipo;
    private static HashMap<Integer, Grupo> all_grupos;

    public Grupo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Grupo(String nombre) {
        this.nombre = nombre;
    }

    public Grupo(int idUser, int id){
        this.idUser = idUser;
        this.id =id;
    }

    // Getters ====================================
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public int getIdUser() {
        return idUser;
    }
    public String getTipo() {
        return tipo;
    }

    // Métodos ===================================

    public static void fetchAllGroups() throws SQLException {
        HashMap<Integer, Grupo> grupoList = new HashMap<>();

        Conexion conexion = new Conexion();
        String query = "SELECT * FROM `groups`";
        conexion.setRs(query);

        ResultSet rs = conexion.getRs();

        while (rs.next()) {
            Grupo grupo = new Grupo(
                    rs.getInt("id"),
                    rs.getString("name")
            );

            grupoList.put(grupo.getId(), grupo);
        }

        setAll_grupos(grupoList);
        conexion.closeConnection();
    }

    public static HashMap<Integer, Grupo> getAll_grupos() {
        return all_grupos;
    }

    public static void setAll_grupos(HashMap<Integer, Grupo> all_grupos) {
        Grupo.all_grupos = all_grupos;
    }

    // Guardar en la BD ============================

    private final String insert =
            "INSERT INTO `groups` (name) VALUES (?)";

    public String insert(Grupo grupo) {
        String mensaje = "";

        try {
            Conexion conexion = new Conexion();

            PreparedStatement pstmt = conexion.setQuery(insert);

            pstmt.setString(1, getNombre());
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                mensaje = "Registro insertado correctamente.";
            } else {
                mensaje = "Error al insertar el registro";
                JOptionPane.showMessageDialog(null, mensaje);
            }

            conexion.closeConnection();

        } catch (SQLException e) {
            System.out.println("ERROR:Fallo en SQL INSERT: " + e.getMessage());
            System.exit(0);
        }

        return mensaje;
    }

    // Insertar Integrante a un Grupo ================
    private final String insertUser =
            "INSERT INTO users_groups (user_id, group_id) VALUES (?, ?)";

    public String insertUser(Grupo insertDispUserToGrup) {
        String mensaje = "";

        try {
            Conexion conexion = new Conexion();
            PreparedStatement pstmt = conexion.setQuery(insertUser);

            pstmt.setInt(1, getIdUser());
            pstmt.setInt(2, getId());
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                mensaje = "Registro insertado correctamente.";
            } else {
                mensaje = "Error al insertar el registro";
                JOptionPane.showMessageDialog(null, mensaje);
            }

            conexion.closeConnection();

        } catch (SQLException e) {
            System.out.println("ERROR:Fallo en SQL INSERT: " + e.getMessage());
            System.exit(0);
        }

        return mensaje;
    }

    // Eliminar al integrante del grupo ================
    private final String deleteUser =
            "DELETE FROM users_groups WHERE user_id = ? AND group_id = ?";

    public String deleteUser(Grupo userToRemoveFromGroup) {
        String mensaje = "";

        try {
            Conexion conexion = new Conexion();
            PreparedStatement pstmt = conexion.setQuery(deleteUser);

            pstmt.setInt(1, getIdUser());
            pstmt.setInt(2, getId());
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                mensaje = "Usuario eliminado del grupo correctamente.";
            } else {
                mensaje = "No se encontró al usuario en el grupo especificado.";
                JOptionPane.showMessageDialog(null, mensaje);
            }

            conexion.closeConnection();

        } catch (SQLException e) {
            System.out.println("ERROR: Fallo en SQL DELETE: " + e.getMessage());
            e.printStackTrace(); // Manejar la excepción adecuadamente
        }

        return mensaje;
    }


}
