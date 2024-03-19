package com.tickets.model;

import com.tickets.util.Conexion;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class JefeDesarrollo extends UserSession {

    public JefeDesarrollo(int id, String name, String email, String gender, Date birthday, Integer role_id, Date created_at) {
        super(id, name, email, gender, birthday, role_id, created_at);
    }

    private static HashMap<String, Ticket> tickets_request;
    private static HashMap<String, Ticket> all_tickets;
    private static HashMap<Integer, String> programmers_names;
    private static HashMap<Integer, String> testers_names;
    private static HashMap<Integer, JefeDesarrollo> available_dev_bosses;

    public static void fetchNewTickets(int dev_boss_id) throws SQLException {
        HashMap<String, Ticket> ticketList = new HashMap<>();

        Conexion conexion = new Conexion();
        String query = "SELECT t.id AS ticket_id, t.code AS ticket_code, t.name AS ticket_name, t.description AS ticket_description, t.created_at AS ticket_created_at, s.name AS state, u.name AS boss_name, u2.name AS dev_boss_name, a.name AS area_name FROM tickets t LEFT JOIN users u ON t.boss_id = u.id LEFT JOIN users u2 ON t.dev_boss_id = u2.id LEFT JOIN areas a ON t.boss_id = a.boss_id LEFT JOIN states s ON t.state_id = s.id WHERE t.dev_boss_id = " + dev_boss_id + " AND t.state_id = 1;";
        conexion.setRs(query);

        ResultSet rs = conexion.getRs();
        while (rs.next()) {
            Ticket ticket = new Ticket(
                    rs.getInt("ticket_id"),
                    rs.getString("ticket_code"),
                    rs.getString("ticket_name"),
                    rs.getString("ticket_description"),
                    rs.getString("state"),
                    rs.getString("area_name"),
                    rs.getString("boss_name"),
                    rs.getString("dev_boss_name"),
                    rs.getString("ticket_created_at")
            );
            ticketList.put(ticket.getCode(), ticket);
        }
        setTickets_request(ticketList);
        conexion.closeConnection();
    }

    public static void fetchAllTickets (int dev_boss_id) throws SQLException {
        HashMap<String, Ticket> ticketList = new HashMap<>();

        Conexion conexion = null;
        try {
            conexion = new Conexion();
            String ticketsQuery = "SELECT t.id AS ticket_id,t.code AS ticket_code, t.name AS ticket_name, t.description AS ticket_description, t.created_at AS ticket_created_at, t.due_date AS ticket_due_date, s.name AS state, u.name AS boss_name, u2.name AS dev_boss_name,u3.name AS programmer_name, u4.name AS tester_name, a.name AS area_name, o.description AS observations FROM tickets t LEFT JOIN users u ON t.boss_id = u.id LEFT JOIN users u2 ON t.dev_boss_id = u2.id LEFT JOIN users u3 ON t.programmer_id = u3.id LEFT JOIN users u4 ON t.tester_id = u4.id LEFT JOIN areas a ON t.boss_id = a.boss_id LEFT JOIN states s ON t.state_id = s.id LEFT JOIN observations o ON t.id = o.ticket_id AND t.dev_boss_id = o.writer_id WHERE t.dev_boss_id = " + dev_boss_id + " AND t.state_id != 1;";
            conexion.setRs(ticketsQuery);

            ResultSet rs = conexion.getRs();
            while(rs.next()) {
                HashMap<Integer, Bitacora> ticketLogs = new HashMap<>();
                Ticket ticket = new Ticket(
                        rs.getInt("ticket_id"),
                        rs.getString("ticket_code"),
                        rs.getString("ticket_name"),
                        rs.getString("ticket_description"),
                        rs.getString("state"),
                        rs.getString("observations"),
                        rs.getString("area_name"),
                        rs.getString("boss_name"),
                        rs.getString("dev_boss_name"),
                        rs.getString("tester_name"),
                        rs.getString("programmer_name"),
                        rs.getString("ticket_due_date"),
                        rs.getString("ticket_created_at")
                );

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
            setAll_tickets(ticketList);
            conexion.closeConnection();
        } finally {
            if(conexion != null) {
                conexion.closeConnection();
            }
        }
    }

    public static void fetchProgramerListNames (int dev_boss_id, Ticket t) throws SQLException {
        HashMap<Integer, String> programmers = new HashMap<>();

        Conexion conexion = new Conexion();
        String query = "SELECT u.name AS programmer_name, u.id AS programmer_id FROM tickets t INNER JOIN assignments_map a ON t.dev_boss_id = a.boss_id INNER JOIN users_groups ug ON a.users_group_id = ug.group_id INNER JOIN users u ON ug.user_id = u.id WHERE t.dev_boss_id = " + dev_boss_id + " AND u.role_id = 2 AND t.id = " + t.getId() + ";";
        conexion.setRs(query);

        ResultSet rs = conexion.getRs();
        while (rs.next()) {
            int programmer_id = rs.getInt("programmer_id");
            String programmer_name = rs.getString("programmer_name");
            programmers.put(programmer_id, programmer_name);
        }
        setProgrammers_names(programmers);
        conexion.closeConnection();
    }

    public static void fetchTestersListNames (int dev_boss_id, Ticket t) throws SQLException {
        HashMap<Integer, String> testers = new HashMap<>();

        Conexion conexion = new Conexion();
        String query = "SELECT u.name AS tester_name, u.id AS tester_id FROM tickets t INNER JOIN assignments_map a ON t.boss_id = a.boss_id INNER JOIN users_groups ug ON a.users_group_id = ug.group_id INNER JOIN users u ON ug.user_id = u.id WHERE t.dev_boss_id = " + dev_boss_id + " AND u.role_id = 4 AND t.id = " + t.getId() + ";";
        conexion.setRs(query);

        ResultSet rs = conexion.getRs();
        while (rs.next()) {
            int tester_id = rs.getInt("tester_id");
            String tester_name = rs.getString("tester_name");
            testers.put(tester_id, tester_name);
        }
        setTesters_names(testers);
        conexion.closeConnection();
    }

    public static void acceptTicket (Ticket t, String observations, int dev_boss_id) throws SQLException {
        Conexion conexion = new Conexion();
        PreparedStatement stmt = null;

        String queryUpdate = "UPDATE tickets SET programmer_id = " + t.getProgrammer_id() + ", tester_id = " + t.getTester_id() + ", due_date = \"" + t.getDue_date() + "\", state_id = 3 WHERE id = " + t.getId() + ";";
        String queryInsert = "INSERT INTO observations (id, name, description, ticket_id, writer_id) VALUES (null, '', \"" + observations + "\", " + t.getId() + ", " + dev_boss_id + ");";

        stmt = conexion.setQuery(queryUpdate);
        stmt.executeUpdate();
        stmt.close();

        stmt = conexion.setQuery(queryInsert);
        stmt.executeUpdate();
        stmt.close();

        conexion.closeConnection();
    }

    public static void denyTicket (Ticket t, String observations, int dev_boss_id) throws SQLException {
        Conexion conexion = new Conexion();
        PreparedStatement stmt = null;

        String queryUpdate = "UPDATE tickets SET state_id = 2 WHERE id = " + t.getId() + ";";
        String queryInsert = "INSERT INTO observations (id, name, description, ticket_id, writer_id) VALUES (null, '', \"" + observations + "\", " + t.getId() + ", " + dev_boss_id + ");";

        stmt = conexion.setQuery(queryUpdate);
        stmt.executeUpdate();
        stmt.close();

        stmt = conexion.setQuery(queryInsert);
        stmt.executeUpdate();
        stmt.close();

        conexion.closeConnection();
    }

    public static HashMap<String, Ticket> getTickets_request() {
        return tickets_request;
    }

    public static void setTickets_request(HashMap<String, Ticket> new_tickets_request) {
        tickets_request = new_tickets_request;
    }

    public static HashMap<Integer, String> getProgrammers_names() {
        return programmers_names;
    }

    public static void setProgrammers_names(HashMap<Integer, String> programmers_names) {
        JefeDesarrollo.programmers_names = programmers_names;
    }

    public static HashMap<Integer, String> getTesters_names() {
        return testers_names;
    }

    public static void setTesters_names(HashMap<Integer, String> testers_names) {
        JefeDesarrollo.testers_names = testers_names;
    }

    public static HashMap<String, Ticket> getAll_tickets() {
        return all_tickets;
    }

    public static void setAll_tickets(HashMap<String, Ticket> all_tickets) {
        JefeDesarrollo.all_tickets = all_tickets;
    }

    public static void fetchAvailableBosses() throws SQLException {
        HashMap<Integer, JefeDesarrollo> availableDevBossList = new HashMap<>();

        Conexion conexion = new Conexion();

        //  Jefes de área que no están previamente asignados a alguna área
        String query = "SELECT u.id AS ID, u.name AS Nombre, u.email AS Email FROM users u LEFT JOIN areas a ON u.id = a.boss_id " +
                "WHERE u.role_id = (SELECT id FROM roles WHERE name = 'Jefe de Área Funcional') " +
                "AND a.id IS NULL";

        conexion.setRs(query);

        ResultSet rs = conexion.getRs();

        while (rs.next()) {
            JefeDesarrollo jefeDesarrollo= new JefeDesarrollo(
                    rs.getInt("ID"),
                    rs.getString("Nombre"),
                    rs.getString("Email"),
                    null,
                    null,
                    null,
                    null
            );

            availableDevBossList.put(jefeDesarrollo.getId(), jefeDesarrollo);
        }
        setAvailables_dev_bosses(availableDevBossList);
        conexion.closeConnection();
    }

    public static void setAvailables_dev_bosses(HashMap<Integer, JefeDesarrollo> available_dev_bosses) {
        JefeDesarrollo.available_dev_bosses = available_dev_bosses;
    }
    public static HashMap<Integer, JefeDesarrollo> getAvailables_dev_bosses() {
        return available_dev_bosses;
    }
}
