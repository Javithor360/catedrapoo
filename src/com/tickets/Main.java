package com.tickets;

import com.tickets.form.Global.Login;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new Login("Login");
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // PARA ACCEDER A LAS VISTAS DE JEFE DE DESARROLLO MIENTRAS SE CORRIGE EL LOGIN
        // new JefeDesarrolloMain();
    }
}
