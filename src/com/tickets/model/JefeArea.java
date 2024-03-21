package com.tickets.model;

import com.tickets.util.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class JefeArea extends UserSession {

    private static HashMap<String, Ticket> assigned_area_tickets;

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

    public static void fetchAreaTickets(int boss_id) throws SQLException {
        HashMap<String, Ticket> ticketList = new HashMap<>();
        Conexion conexion = null;
        try {
            conexion = new Conexion();
            String ticketsQuery = "SELECT t.id AS ticket_id,  t.code AS ticket_code,  t.name AS ticket_name,  t.description AS ticket_description,  t.due_date AS ticket_due_date,  t.created_at AS ticket_created_at,  s.name AS state,  s.id AS state_id,  u.name AS boss_name,  u2.name AS dev_boss_name,  u3.name AS programmer_name,  u4.name AS tester_name,  a.name AS area_name,  o.description AS observations  FROM tickets t  LEFT JOIN users u ON t.boss_id = u.id  LEFT JOIN users u2 ON t.dev_boss_id = u2.id  LEFT JOIN users u3 ON t.programmer_id = u3.id  LEFT JOIN users u4 ON t.tester_id = u4.id  LEFT JOIN areas a ON t.boss_id = a.boss_id  LEFT JOIN states s ON t.state_id = s.id  LEFT JOIN observations o ON t.id = o.ticket_id AND t.dev_boss_id = o.writer_id  WHERE t.boss_id = "+boss_id+";";
            conexion.setRs(ticketsQuery);

            ResultSet rs = conexion.getRs();
            while (rs.next()) {
                HashMap<Integer, Bitacora> ticketLogs = new HashMap<>();
                Ticket ticket = new Ticket(
                        rs.getInt("ticket_id"),
                        rs.getString("ticket_code"),
                        rs.getString("ticket_name"),
                        rs.getString("ticket_description"),
                        rs.getString("state"),
                        rs.getInt("state_id"),
                        rs.getString("observations"),
                        rs.getString("area_name"),
                        rs.getString("boss_name"),
                        rs.getString("dev_boss_name"),
                        rs.getString("tester_name"),
                        rs.getString("programmer_name"),
                        rs.getString("ticket_due_date"),
                        rs.getString("ticket_created_at")
                );

                // Usar una nueva instancia de Conexion para la consulta de registros de bitácora
                Conexion conexionLogs = new Conexion();
                String logsQuery = "SELECT tl.id AS log_id, tl.code_ticket AS ticket_code, tl.name AS log_name, tl.description AS log_description, tl.percent AS log_percent, u.name AS programmer_name, tl.created_at AS log_created_at FROM ticket_logs tl INNER JOIN users u ON tl.programmer_id = u.id WHERE tl.code_ticket = \"" + ticket.getCode() + "\"";
                conexionLogs.setRs(logsQuery);

                ResultSet rs2 = conexionLogs.getRs();
                while (rs2.next()) {
                    Bitacora logs = new Bitacora(
                            rs2.getInt("log_id"),
                            rs2.getString("ticket_code"),
                            rs2.getString("log_name"),
                            rs2.getString("log_description"),
                            rs2.getDouble("log_percent"),
                            rs2.getString("programmer_name"),
                            rs2.getString("log_created_at")
                    );
                    ticketLogs.put(logs.getId(), logs);
                }
                ticket.setLogs(ticketLogs);
                ticketList.put(ticket.getCode(), ticket);
            }
            setAssigned_area_tickets(ticketList);
            conexion.closeConnection();
        } finally {
            if (conexion != null) {
                conexion.closeConnection();
            }
        }
    }

    public static void setAssigned_area_tickets(HashMap<String, Ticket> assigned_tester_tickets) {
        JefeArea.assigned_area_tickets = assigned_tester_tickets;
    }

    public static HashMap<String, Ticket> getAssigned_tickets() {
        return assigned_area_tickets;
    }

    public static String getPrefix_area_code(int user_id) throws SQLException{
        Conexion conexion = new Conexion();

        try{
            String query = "SELECT a.prefix_code " +
                    "FROM users u " +
                    "JOIN assignments_map am ON u.id = am.boss_id " +
                    "JOIN areas a ON am.area_id = a.id " +
                    "WHERE u.id = "+user_id+";";

            conexion.setRs(query);
            ResultSet rs = conexion.getRs();

            while(rs.next()){
                return rs.getString("prefix_code");
            }
        } finally {
            if (conexion != null) {
                conexion.closeConnection();
            }
        }
        return "NULL";
    }
}
