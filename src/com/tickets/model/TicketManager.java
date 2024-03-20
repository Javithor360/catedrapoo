package com.tickets.model;

import com.tickets.util.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketManager {
    public static void checkTickets() {
        try {
            Conexion conexion = new Conexion();

            String sql = "SELECT id FROM tickets WHERE (state_id = 3 OR state_id = 6) AND due_date < CURDATE()";

            PreparedStatement pstmt = conexion.setQuery(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int ticketId = rs.getInt("id");
                updateTicketState(conexion, ticketId);
            }

            rs.close();
            pstmt.close();
            conexion.closeConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateTicketState (Conexion conexion, int ticketId) throws SQLException {
        String updateSql = "UPDATE tickets SET state_id = 5 WHERE id = ?";
        PreparedStatement updateStmt = conexion.setQuery(updateSql);

        System.out.println("---------- Ticket #" + ticketId + " ha sido categorizado como Vencido. --------------");

        updateStmt.setInt(1, ticketId);
        updateStmt.executeUpdate();
        updateStmt.close();
    }
}
