package com.tickets;

import com.tickets.form.Global.Login;
import com.tickets.form.JefeDesarrollo.JefeDesarrolloMain;
import com.tickets.util.ScheduledExecutor;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        new Login();

        ScheduledExecutor.checkTicketDate();
    }
}
