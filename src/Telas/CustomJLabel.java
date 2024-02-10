/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

public class CustomJLabel extends JPanel {

    private JLabel label;

    public CustomJLabel(String text) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 200));
        setMinimumSize(new Dimension(200, 200));
        setMaximumSize(new Dimension(200, 200));
        setSize(200, 200);

        setBackground(Color.GREEN);
        setOpaque(true);

        label = new JLabel(text);
       // label.setAlignmentX(Component.CENTER_ALIGNMENT); // Centralize horizontalmente
       // label.setAlignmentY(Component.CENTER_ALIGNMENT); // Centralize verticalmente

        add(Box.createVerticalGlue());
        add(label);
        add(Box.createVerticalGlue());
    }
}
