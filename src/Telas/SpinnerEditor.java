/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import java.awt.Component;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class SpinnerEditor extends DefaultCellEditor {
   private JSpinner spinner;
    private JComponent component = new JTextField();

    public SpinnerEditor(int minValue, int maxValue) {
        super(new JTextField());
        spinner = new JSpinner();

        SpinnerModel model = new SpinnerNumberModel(0, minValue, maxValue, 1);
        spinner.setModel(model);
        spinner.setEditor(new JSpinner.DefaultEditor(spinner));

        // Para evitar que o editor seja fechado quando se clica nele
        spinner.setFocusable(false);
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }
}
