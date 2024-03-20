package com.tickets.model;

import com.tickets.util.Conexion;

import java.sql.ResultSet;
import java.sql.SQLException;
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

}
