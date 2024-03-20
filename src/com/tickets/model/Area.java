package com.tickets.model;

import com.tickets.util.Conexion;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

public class Area {

    // Atributos ===================================
    private int id;
    private String prefix_code;
    private String name;
    private int id_boss;
    private String boss_name;
    private int id_dev_boss;
    private String dev_boss_name;
    private Timestamp created_at;
    private static HashMap<Integer, Area> all_areas;

    public Area(int id, String prefix_code, String name, String boss_name, String dev_boss_name, Timestamp created_at) {
        this.id = id;
        this.prefix_code = prefix_code;
        this.name = name;
        this.boss_name = boss_name;
        this.dev_boss_name = dev_boss_name;
        this.created_at = created_at;
    }

    public Area(String name, String prefix_code, int id_boss, int id_dev_boss) {
        this.name = name;
        this.prefix_code = prefix_code;
        this.id_boss = id_boss;
        this.id_dev_boss = id_dev_boss;
    }

    // Getters ====================================
    public int getId() {
        return id;
    }
    public String getName(){
        return name;
    }
    public String getPrefix_code() {
        return prefix_code;
    }
    public String getBoss_name() {
        return boss_name;
    }
    public String getDev_boss_name() {
        return dev_boss_name;
    }
    public int getId_boss() {
        return id_boss;
    }
    public int getId_dev_boss() {
        return id_dev_boss;
    }
    public Timestamp getCreated_at() {
        return created_at;
    }

    // Métodos ===================================
    public static void fetchAllAreas() throws SQLException {
        HashMap<Integer, Area> areaList = new HashMap<>();

        Conexion conexion = new Conexion();
        String query = "SELECT a.id as ID, a.prefix_code AS Prefijo, a.name AS Nombre, u1.name AS Jefe_Area, u2.name AS Jefe_Dev_Area, a.created_at as Creado FROM areas a LEFT JOIN users u1 ON a.boss_id = u1.id LEFT JOIN users u2 ON a.dev_boss_id = u2.id";
        conexion.setRs(query);

        ResultSet rs = conexion.getRs();

        while (rs.next()) {
            Area area = new Area(
                    rs.getInt("ID"),
                    rs.getString("Prefijo"),
                    rs.getString("Nombre"),
                    rs.getString("Jefe_Area"),
                    rs.getString("Jefe_Dev_Area"),
                    rs.getTimestamp("Creado")
            );

            areaList.put(area.getId(), area);
        }
        setAll_areas(areaList);
        conexion.closeConnection();
    }

    public static void setAll_areas(HashMap<Integer, Area> all_areas) {
        Area.all_areas = all_areas;
    }
    public static HashMap<Integer, Area> getAll_areas() {
        return all_areas;
    }

    // BDD ===================================
    private final String insert =
            "INSERT INTO areas (prefix_code, name, boss_id, dev_boss_id, created_at) VALUES (?, ?, ?, ?, ?)";

    public int insert(Area area) {
        // Valor por defecto en caso de error
        int nuevoId = -1;

        String mensaje = "";

            try{
                Conexion conexion = new Conexion();

                PreparedStatement pstmt = conexion.setQuery(insert, Statement.RETURN_GENERATED_KEYS );

                pstmt.setString(1, area.getPrefix_code());
                pstmt.setString(2, area.getName());
                pstmt.setString(3, String.valueOf(area.getId_boss()));
                pstmt.setString(4, String.valueOf(area.getId_dev_boss()));
                pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    ResultSet nuevoID = pstmt.getGeneratedKeys();
                    if (nuevoID.next()) {
                        nuevoId = nuevoID.getInt(1); // Obtener el nuevo ID asignado por la base de datos
                    }
                    mensaje = "Registro insertado correctamente.";
                } else {
                    mensaje = "Error al insertar el registro";
                }

                JOptionPane.showMessageDialog(null, mensaje);

                conexion.closeConnection();

            } catch (SQLException e) {
                System.out.println("ERROR:Fallo en SQL INSERT: "+ e.getMessage());
                System.exit(0);
            }

        return nuevoId;
    }

}
