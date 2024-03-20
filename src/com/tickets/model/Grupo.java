package com.tickets.model;

import com.tickets.util.Conexion;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

public class Grupo {
    // Atributos ===================================
    private int id;
    private String nombre;
    private static HashMap<Integer, Grupo> all_grupos;

    public Grupo (int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Grupo (String nombre){
        this.nombre = nombre;
    }

    // Getters ====================================
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    // Métodos ===================================

    public static void fetchAllGroups() throws SQLException {
        HashMap<Integer, Grupo> grupoList = new HashMap<>();

        Conexion conexion = new Conexion();
        String query = "SELECT * FROM grupos";
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
            "INSERT INTO grupos (name) VALUES (?)";

    public String insert(Grupo grupo) {
        String mensaje = "";

        try{
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
            System.out.println("ERROR:Fallo en SQL INSERT: "+ e.getMessage());
            System.exit(0);
        }

        return mensaje;
    }

}