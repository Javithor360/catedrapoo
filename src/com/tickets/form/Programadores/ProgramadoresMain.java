package com.tickets.form.Programadores;

import javax.swing.*;
import java.awt.*;

public class ProgramadoresMain extends JFrame{
    private JPanel pnlDesarrolloEmpleados;


    public ProgramadoresMain(){
        super("Programadores - Homepage");
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlDesarrolloEmpleados);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(getParent());
    }
}
