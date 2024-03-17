package com.tickets;

import com.tickets.form.Global.Login;
import com.tickets.form.JefeDesarrollo.JefeDesarrolloMain;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
//        JFrame frame = new Login("Login");
//        frame.setVisible(true);
//        frame.setLocationRelativeTo(null);

//       PARA ACCEDER A LAS VISTAS DE JEFE DE DESARROLLO MIENTRAS SE CORRIGE EL LOGIN
         new JefeDesarrolloMain();
    }
}
