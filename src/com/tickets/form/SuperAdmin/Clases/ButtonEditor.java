package com.tickets.form.SuperAdmin.Clases;

import com.tickets.form.SuperAdmin.NuevoEmpleado;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String id; // ID de la fila que se estará editando

    public ButtonEditor(final JTable table) {
        super(new JCheckBox());
        this.button = new JButton();
        this.button.addActionListener(e -> {
            // Obtener el ID de la fila seleccionada
            int row = table.getSelectedRow();
            if (row != -1) {
                id = table.getValueAt(row, 0).toString();
                // Abre el nuevo formulario y pasa el ID como parámetro
                    JFrame frame = new NuevoEmpleado();
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