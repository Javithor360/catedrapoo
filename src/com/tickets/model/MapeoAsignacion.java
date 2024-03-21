package com.tickets.model;

import com.tickets.util.Conexion;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MapeoAsignacion {

    private int id;
    private int boss_id;
    private String nombre_jefe;
    private int area_id;
    private String nombre_area;
    private int users_group_id;
    private String nombre_grupo;
    private int total_integrantes;
    private static HashMap<Integer, MapeoAsignacion> all_asignaciones;

    public MapeoAsignacion(int id, int boss_id, String nombre_jefe, int area_id, String nombre_area, int users_group_id, String nombre_grupo, int total_integrantes) {
        this.id = id;
        this.boss_id = boss_id;
        this.nombre_jefe = nombre_jefe;
        this.area_id = area_id;
        this.nombre_area = nombre_area;
        this.users_group_id = users_group_id;
        this.nombre_grupo = nombre_grupo;
        this.total_integrantes = total_integrantes;
    }

    public MapeoAsignacion(int boss_id, int area_id, int users_group_id){
        this.boss_id = boss_id;
        this.area_id = area_id;
        this.users_group_id = users_group_id;
    }

    // Getters ===================================
    public int getId() {
        return id;
    }
    public int getBoss_id() {
        return boss_id;
    }
    public String getNombre_jefe() {
        return nombre_jefe;
    }
    public int getArea_id() {
        return area_id;
    }
    public String getNombre_area() {
        return nombre_area;
    }
    public int getUsers_group_id() {
        return users_group_id;
    }
    public String getNombre_grupo() {
        return nombre_grupo;
    }
    public int getTotal_integrantes() {
        return total_integrantes;
    }

    // Métodos ===================================
    public static void fetchAllAsignaciones() throws SQLException {
        HashMap<Integer, MapeoAsignacion> mapeoList = new HashMap<>();

        Conexion conexion = new Conexion();
        String query = "SELECT ma.id AS ID, " +
                "ma.boss_id AS JefeID, " +
                "u1.name AS NombreJefe, " +
                "ma.area_id AS AreaID, " +
                "a.name AS NombreArea, " +
                "ma.users_group_id AS GrupoID, " +
                "g.name AS NombreGrupo " +
                "FROM assignments_map ma " +
                "LEFT JOIN users u1 ON ma.boss_id = u1.id " +
                "LEFT JOIN areas a ON ma.area_id = a.id " +
                "LEFT JOIN grupos g ON ma.users_group_id = g.id;";
        conexion.setRs(query);
        ResultSet rs = conexion.getRs();
        // ===================================================================
        Conexion conexionCount = new Conexion();
        String queryTotalIntegrantes = "SELECT users_group_id AS GrupoID, " +
                "COUNT(*) AS TotalIntegrantes " +
                "FROM assignments_map " +
                "GROUP BY users_group_id;";
        conexionCount.setRs(queryTotalIntegrantes);
        ResultSet rsCount = conexionCount.getRs();

        while (rs.next() && rsCount.next()) {
            MapeoAsignacion mapeo = new MapeoAsignacion(
                    rs.getInt("ID"),
                    rs.getInt("JefeID"),
                    rs.getString("NombreJefe"),
                    rs.getInt("AreaID"),
                    rs.getString("NombreArea"),
                    rs.getInt("GrupoID"),
                    rs.getString("NombreGrupo"),
                    rsCount.getInt("TotalIntegrantes")
            );

            mapeoList.put(mapeo.getId(), mapeo);
        }

        setAll_asignaciones(mapeoList);
        conexion.closeConnection();
        conexionCount.closeConnection();
    }

    public static HashMap<Integer, MapeoAsignacion> getAll_asignaciones() {
        return all_asignaciones;
    }

    public static void setAll_asignaciones(HashMap<Integer, MapeoAsignacion> all_asignaciones) {
        MapeoAsignacion.all_asignaciones = all_asignaciones;
    }

    // BDD ===================================
    private final String insert =
            "INSERT INTO assignments_map (boss_id, area_id, users_group_id) VALUES (?, ?, ?)";

    public String insert(MapeoAsignacion mapeo) {
        String mensaje = "";

        try{
            Conexion conexion = new Conexion();

            PreparedStatement pstmt = conexion.setQuery(insert);

            pstmt.setInt(1, mapeo.getBoss_id());
            pstmt.setInt(2, mapeo.getArea_id());
            pstmt.setInt(3, mapeo.getUsers_group_id());

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
