/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author denyl
 */
public class CustomTableModel extends DefaultTableModel {

    private List<Boolean> selectedRows = new ArrayList<>();

    public CustomTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
        for (int i = 0; i < rowCount; i++) {
            selectedRows.add(false);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) { // 3 é o índice da coluna de seleção (0-indexed).
            return Boolean.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 3) { // Torna apenas a coluna de seleção editável.
            return true;
        }
        return false; // Torna todas as outras células não editáveis.
    }

    public boolean isRowSelected(int row) {
        return selectedRows.get(row);
    }

    public void setRowSelected(int row, boolean selected) {
        selectedRows.set(row, selected);
    }
}
