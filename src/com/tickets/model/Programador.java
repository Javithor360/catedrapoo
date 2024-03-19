package com.tickets.model;

import com.tickets.util.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Programador {
    private static HashMap<String, Ticket> assigned_tickets;

    public static void fetchTickets(int programmer_id) throws SQLException {
        HashMap<String, Ticket> ticketList = new HashMap<>();
        Conexion conexion = null;
        try {
            conexion = new Conexion();
            String ticketsQuery = "SELECT t.id AS ticket_id, t.code AS ticket_code, t.name AS ticket_name, t.description AS ticket_description, t.due_date AS ticket_due_date, t.created_at AS ticket_created_at, s.name AS state, u.name AS boss_name, u2.name AS dev_boss_name, u3.name AS programmer_name, u4.name AS tester_name, a.name AS area_name, o.description AS observations FROM tickets t LEFT JOIN users u ON t.boss_id = u.id LEFT JOIN users u2 ON t.dev_boss_id = u2.id LEFT JOIN users u3 ON t.programmer_id = u3.id LEFT JOIN users u4 ON tester_id = u4.id LEFT JOIN areas a ON t.boss_id = a.boss_id LEFT JOIN states s ON t.state_id = s.id LEFT JOIN observations o ON t.id = o.ticket_id AND t.dev_boss_id = o.writer_id WHERE t.programmer_id = " + programmer_id + " AND t.state_id != 1;";
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
                // rs2.close(); // Cerrar el ResultSet de la consulta de registros de bitácora
                ticket.setLogs(ticketLogs);
                ticketList.put(ticket.getCode(), ticket);
            }
            setAssigned_tickets(ticketList);
            conexion.closeConnection();
        } finally {
            if (conexion != null) {
                conexion.closeConnection();
            }
        }
    }

    public static HashMap<String, Ticket> getAssigned_tickets() {
        return assigned_tickets;
    }

    public static void setAssigned_tickets(HashMap<String, Ticket> assigned_tickets) {
        Programador.assigned_tickets = assigned_tickets;
    }
}
