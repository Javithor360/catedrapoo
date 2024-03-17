package com.tickets.form.JefeDesarrollo;

import com.tickets.model.JefeDesarrollo;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
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

    public JefeDesarrolloAceptar(UserSession user, Ticket ticket) throws SQLException {
        super("Jefe de Desarrollo - Aceptar nuevo caso");
        setVisible(true);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlAceptarTicket);

        cmbSetData(user.getId(), ticket);

        Object item = cmbProgramadores.getSelectedItem();
        int value = ((ComboItem)item).getValue();
        System.out.println(value);

        lblTitle.setText("Aceptar caso " + ticket.getCode());
        btnSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
    }

    public void cmbSetData(int dev_boss_id, Ticket ticket) throws SQLException {
        JefeDesarrollo.fetchProgramerListNames(dev_boss_id, ticket);

        HashMap<Integer, String> programmers = JefeDesarrollo.getProgrammers_names();
        for (Map.Entry<Integer, String> entry : programmers.entrySet()) {
            cmbProgramadores.addItem(new ComboItem(entry.getValue(), entry.getKey()));
        }
    }

    static class ComboItem
    {
        private String key;
        private int value;

        public ComboItem(String key, int value)
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString()
        {
            return key;
        }

        public String getKey()
        {
            return key;
        }

        public int getValue()
        {
            return value;
        }
    }
}
