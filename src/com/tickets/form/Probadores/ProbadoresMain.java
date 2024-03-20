package com.tickets.form.Probadores;

import com.tickets.model.Ticket;
import com.tickets.model.UserSession;
import com.tickets.model.Probador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProbadoresMain extends JFrame {
    private JPanel pnlProbadores;
    private JTable tblCheckCases;
    private JButton btnBack;
    private JLabel lblTitle;
    private HashMap<String, Ticket> ticket_list;
    private DefaultTableModel model;

    public ProbadoresMain (UserSession user) throws SQLException {
        super("Empleado de área funcional - Homepage");
        setSize(500, 500);
        setVisible(true);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlProbadores);

        // Estableciendo dato dinámico
        lblTitle.setFont(new Font("Consolas", Font.BOLD, 24));
        lblTitle.setText("¡Bienvenido, " + user.getName() + "!");

        // Estableciendo los títulos de columnas de la tabla
        String[] columns = { "Código", "Estado", "Fecha entrega" };

        model = new DefaultTableModel(null, columns) {
            @Override
            // Evitando la selección en la tabla
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCheckCases.setModel(model); // Añadiendo el modelo
        fecth_tester_tickets(user.getId()); // Buscando tickets asignados al probador

        btnBack.addMouseListener(new MouseAdapter() { // Cierra la ventana actual
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });

        tblCheckCases.addMouseListener(new MouseAdapter() { // Evento al clickear algun campo en una tabla
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    select_item(user);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            pnlProbadores,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
    }
        public void fecth_tester_tickets(int user_id) throws SQLException { // Método buscar y establecer datos de los tickets

            Probador.fetchTesterTickets(user_id); // Método realizando query de datos
            ticket_list = Probador.getAssigned_tester_tickets(); // Método asignando los datos a un HashMap

            model.setRowCount(0); //  Reiniciando tabla

            if(!ticket_list.isEmpty()) { // Si hay datos... mapea la tabla con los datos correspondientes
                for(Map.Entry<String, Ticket> entry : ticket_list.entrySet()) {
                    Ticket ticket = entry.getValue();
                    model.addRow(new Object[]{ ticket.getCode(), ticket.getState(), ticket.getDue_date() });
                }
            } else { // Sino... agrega un mensaje indicando que no se han encontrado datos disponibles
                model.addRow(new Object[]{ "No", "hay", "datos..." });
            }
        }

        public void select_item (UserSession user) throws SQLException {
            Ticket selectedTicket = ticket_list.get(model.getValueAt(tblCheckCases.getSelectedRow(), 0).toString());
            if (selectedTicket != null) {
                System.out.println(selectedTicket.getCode());
                //new ProgramadoresDetalle(user, selectedTicket, ProbadoresMain.this);
            } else {
                JOptionPane.showMessageDialog(
                        pnlProbadores,
                        "No hay datos que mostrar...",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
}
