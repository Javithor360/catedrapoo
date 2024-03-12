package com.tickets.form.Programadores;

import javax.swing.*;
import java.awt.*;

public class ProgramadoresMain extends JFrame{
    private JPanel pnlDesarrolloEmpleados;


    public ProgramadoresMain(String title){
        super(title);
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(pnlDesarrolloEmpleados);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(getParent());
    }


    public static void main(String[] args){
        JFrame frame = new ProgramadoresMain("Área de desarrollo");
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
