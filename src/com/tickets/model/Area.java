package com.tickets.model;

import com.tickets.util.Conexion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Area {
    private int id;
    private String prefix_code;
    private String name;
    private String boss_name;
    private String dev_boss_name;
    private String created_at;
    private static HashMap<Integer, Area> all_areas;

    public Area(int id, String prefix_code, String name, String boss_name, String dev_boss_name, String created_at) {
        this.id = id;
        this.prefix_code = prefix_code;
        this.name = name;
        this.boss_name = boss_name;
        this.dev_boss_name = dev_boss_name;
        this.created_at = created_at;
    }

    // Atributos ===================================

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
    public String getCreated_at() {
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
                    rs.getString("Creado")
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
}
