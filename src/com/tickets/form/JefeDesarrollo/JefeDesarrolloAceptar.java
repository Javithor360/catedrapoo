package com.tickets.form.JefeDesarrollo;

import com.tickets.model.JefeDesarrollo;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;
import com.tickets.util.ComboItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class JefeDesarrolloAceptar extends JFrame {
    private JPanel pnlAceptarTicket;
    private JButton btnAceptar;
    private JButton btnSalir;
    private JComboBox cmbProgramadores;
    private JComboBox cmbProbadores;
    private JTextField txtFecha;
    private JLabel lblTitle;
    private JTextArea txtObservaciones;

    public JefeDesarrolloAceptar(UserSession user, Ticket ticket, String observaciones, JefeDesarrolloNewRequest parentComponent, JefeDesarrolloMain mainComponent) throws SQLException {
        super("Jefe de Desarrollo - Aceptar nuevo caso");
        setVisible(true);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlAceptarTicket);

        fillInputs(user.getId(), ticket, observaciones);


//        System.out.println(value);

        btnSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });

        btnAceptar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    insertData(ticket, user.getId());
                    JOptionPane.showMessageDialog(pnlAceptarTicket, "El caso ha sido aceptado y asignado correctamente.");
                    dispose();
                    parentComponent.dispose();
                    mainComponent.fetch_tickets_request(user.getId());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            pnlAceptarTicket,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void fillInputs(int dev_boss_id, Ticket ticket, String observaciones) throws SQLException {
        JefeDesarrollo.fetchProgramerListNames(dev_boss_id, ticket);
        JefeDesarrollo.fetchTestersListNames(dev_boss_id, ticket);

        HashMap<Integer, String> programmers = JefeDesarrollo.getProgrammers_names();
        for (Map.Entry<Integer, String> entry : programmers.entrySet()) {
            cmbProgramadores.addItem(new ComboItem(entry.getValue(), entry.getKey()));
        }

        HashMap<Integer, String> testers = JefeDesarrollo.getTesters_names();
        for (Map.Entry<Integer, String> entry : testers.entrySet()) {
            cmbProbadores.addItem(new ComboItem(entry.getValue(), entry.getKey()));
        }

        lblTitle.setText("Aceptar caso " + ticket.getCode());
        txtObservaciones.setText(observaciones);
        txtFecha.setText(LocalDate.now().plusWeeks(1).toString());
    }

    public void insertData(Ticket t, int dev_boss_id) throws SQLException {
        int programmer_id = ((ComboItem)cmbProgramadores.getSelectedItem()).getValue();
        int tester_id = ((ComboItem)cmbProbadores.getSelectedItem()).getValue();
        String observations = txtObservaciones.getText();
        LocalDate due_date = LocalDate.parse(txtFecha.getText());

        t.setProgrammer_id(programmer_id);
        t.setTester_id(tester_id);
        t.setDue_date(due_date);

        JefeDesarrollo.acceptTicket(t, observations, dev_boss_id);
    }
}
