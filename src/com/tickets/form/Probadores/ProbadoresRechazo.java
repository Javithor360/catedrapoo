package com.tickets.form.Probadores;

import com.tickets.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProbadoresRechazo extends JFrame {
    private JPanel pnlRechazo;
    private JLabel lblTitle;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JTextArea txaRazon;
    private JTextField txtActualDate;
    private JTextField txtNewDate;

    public ProbadoresRechazo(UserSession user, Ticket ticket, ProbadoresDetalle parentComponent, ProbadoresMain mainComponent) {
        super("Probadores - Rechazo de proyecto");
        setVisible(true);
        setMaximumSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(getParent());
        setContentPane(pnlRechazo);

        lblTitle.setText("Rechazando proyecto - Caso "+ticket.getCode());

        // Imprimiendo fechas del proyecto
        txtActualDate.setText(ticket.getDue_date());

        // Formateador para la cadena de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Convertir la cadena de fecha en un LocalDateTime
        LocalDateTime newDate = LocalDateTime.parse(ticket.getDue_date(), formatter);
        newDate = newDate.plusWeeks(1);
        txtNewDate.setText(String.valueOf(newDate.format(formatter)));

        btnConfirmar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    if(insertData(ticket,user)){
                        JOptionPane.showMessageDialog(
                                pnlRechazo,
                                "Se ha rechazado el proyecto correctamente.",
                                "ÉXITO",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        parentComponent.fetch_logs(ticket);
                        mainComponent.fecth_tester_tickets(user.getId());
                        dispose();
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(
                            pnlRechazo,
                            "Ocurrió un error durante la ejecución:\n" + new RuntimeException(ex).getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw new RuntimeException(ex);
                }
            }
        });

        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
            }
        });
    }

    public boolean insertData(Ticket t, UserSession user){
        if(validateData()){
            try{
                DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                String descripcion = "FECHA DE ENTREGA ORIGINAL: "+t.getDue_date()+"\n" +
                                     "FECHA DE ENTREGA ACTUALIZADA:"+txtNewDate.getText()+"\n"+txaRazon.getText();

                Bitacora bitacora = new Bitacora(
                        t.getCode(),
                        "PROYECTO RECHAZADO",
                        descripcion,
                        0,
                        user.getName(),
                        dft.format(now)
                );

                Probador.updateStateTicket(6, t.getId(), user.getId());
                Programador.newLog(bitacora,user.getId());
                t.addBitacora(bitacora);


                return true;
            }catch(Exception ex){
                JOptionPane.showMessageDialog(
                        pnlRechazo,
                        "Ocurrió un error durante la ejecución:\n" + ex.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE
                );
                throw new RuntimeException(ex);
            }
        }else{
            JOptionPane.showMessageDialog(
                    pnlRechazo,
                    "Completa la razón del rechazo de manera correcta para continuar",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    public boolean validateData(){
        if (txaRazon.getText().length() < 50) {
            JOptionPane.showMessageDialog(
                    pnlRechazo,
                    "La longitud de la razón debe ser más extensa",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }
}
