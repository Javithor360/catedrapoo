package com.tickets.model;

import com.tickets.util.Conexion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class JefeArea extends UserSession {

    // Atributos y Constructor ====================
    private static HashMap<Integer, JefeArea> available_bosses;

    public JefeArea(int id, String name, String email, String gender, Date birthday, Integer role_id, Date created_at) {
        super(id, name, email, gender, birthday, role_id, created_at);
    }

    // Métodos ===================================

    public static void fetchAvailableBosses() throws SQLException {
        HashMap<Integer, JefeArea> availableBossList = new HashMap<>();

        Conexion conexion = new Conexion();

        //  Jefes de área que no están previamente asignados a alguna área
        String query = "SELECT u.id AS ID, u.name AS Nombre, u.email AS Email FROM users u LEFT JOIN areas a ON u.id = a.boss_id " +
                "WHERE u.role_id = (SELECT id FROM roles WHERE name = 'Jefe de Área Funcional') " +
                "AND a.id IS NULL";

        conexion.setRs(query);

        ResultSet rs = conexion.getRs();

        while (rs.next()) {
            JefeArea jefeArea= new JefeArea(
                    rs.getInt("ID"),
                    rs.getString("Nombre"),
                    rs.getString("Email"),
                    null,
                    null,
                    null,
                    null
            );

            availableBossList.put(jefeArea.getId(), jefeArea);
        }
        setAvailables_bosses(availableBossList);
        conexion.closeConnection();
    }

    public static void setAvailables_bosses(HashMap<Integer, JefeArea> availables_bosses) {
        JefeArea.available_bosses = availables_bosses;
    }
    public static HashMap<Integer, JefeArea> getAvailables_bosses() {
        return available_bosses;
    }
}
