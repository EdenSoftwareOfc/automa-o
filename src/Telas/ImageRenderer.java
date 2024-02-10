/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import javax.swing.*;

import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author denyl
 */
public class ImageRenderer extends DefaultTableCellRenderer {
     @Override
    protected void setValue(Object value) {
        if (value instanceof ImageIcon) {
            setIcon((ImageIcon) value);
            setText("");
        } else {
            super.setValue(value);
        }
    }
}
