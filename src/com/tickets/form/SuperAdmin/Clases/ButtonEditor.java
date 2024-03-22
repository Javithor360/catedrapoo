package com.tickets.form.SuperAdmin.Clases;

import com.tickets.form.SuperAdmin.Grupos.GruposMapeo;
import com.tickets.form.SuperAdmin.NuevoEmpleado;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String id; // ID de la fila que se estará editando
    private String type;

    public ButtonEditor(final JTable table) {
        super(new JCheckBox());
        this.button = new JButton();
        this.button.addActionListener(e -> {
            // Obtener el ID de la fila seleccionada
            int row = table.getSelectedRow();
            if (row != -1) {
                id = table.getValueAt(row, 0).toString();

                // Obtener el valor de la celda
                type = table.getValueAt(row, 2).toString();

                // Dividir la cadena en un array de subcadenas usando el espacio como delimitador
                String[] parts = type.split("\\s+");

                // Obtener la primera palabra (primer elemento del array)
                String firstWord = parts[0];

                // Abre el nuevo formulario y pasa el ID como parámetro
                JFrame frame = null;
                try {
                    frame = new GruposMapeo(id, firstWord);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                frame.setVisible(true);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Configurar el texto y otras propiedades del botón
        button.setText("Editando");
        return button;
    }
}
