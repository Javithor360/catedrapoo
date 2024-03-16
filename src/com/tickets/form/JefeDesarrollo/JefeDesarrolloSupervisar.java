package com.tickets.form.JefeDesarrollo;

import com.tickets.model.JefeDesarrollo;
import com.tickets.model.Ticket;
import com.tickets.model.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.*;

public class JefeDesarrolloSupervisar extends JFrame {
    private JPanel pnlSupervisar;
    private JButton btnClose;
    private JLabel lblTableData;

    public JefeDesarrolloSupervisar(UserSession user) throws SQLException {
        super("Jefe de Desarrollo - Supervisar");
        setVisible(true);
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlSupervisar);
        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
    }
}
