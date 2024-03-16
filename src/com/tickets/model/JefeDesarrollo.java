package com.tickets.model;

import com.tickets.util.Conexion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class JefeDesarrollo {
    private HashMap<String, Ticket> tickets_request;

    public void fetchNewTickets(int dev_boss_id) throws SQLException {
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
            System.out.println(ticket.getCode());
        }
        setTickets_request(ticketList);
        System.out.println(getTickets_request());
    }

    public HashMap<String, Ticket> getTickets_request() {
        return tickets_request;
    }

    public void setTickets_request(HashMap<String, Ticket> tickets_request) {
        this.tickets_request = tickets_request;
    }
}
