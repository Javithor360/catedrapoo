package com.tickets.model;

import com.tickets.util.Conexion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class JefeDesarrollo {
    private static HashMap<String, Ticket> tickets_request;
    private static HashMap<Integer, String> programmers_names;

    public static void fetchNewTickets(int dev_boss_id) throws SQLException {
        HashMap<String, Ticket> ticketList = new HashMap<>();

        Conexion conexion = new Conexion();
        String query = "SELECT t.id AS ticket_id, t.code AS ticket_code, t.name AS ticket_name, t.description AS ticket_description, t.created_at AS ticket_created_at, s.name AS state, u.name AS boss_name, u2.name AS dev_boss_name, a.name AS area_name FROM tickets t LEFT JOIN users u ON t.boss_id = u.id LEFT JOIN users u2 ON t.dev_boss_id = u2.id LEFT JOIN areas a ON t.boss_id = a.boss_id LEFT JOIN states s ON t.state_id = s.id WHERE t.dev_boss_id = " + dev_boss_id + ";";
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
                    null,
                    null,
                    null,
                    rs.getDate("ticket_created_at")
            );
            ticketList.put(ticket.getCode(), ticket);
        }
        setTickets_request(ticketList);
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
}
