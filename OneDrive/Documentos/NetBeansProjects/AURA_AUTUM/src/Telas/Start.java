package Telas;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import java.sql.*;

public class Start extends javax.swing.JFrame {

    public Start() {
        setUndecorated(true);
        initComponents();
        carregarImagem();
        setLocationRelativeTo(null);

        barra2.setValue(barra1.getValue());

        Thread progressThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    File audioFile = new File("src//Audio//aqui.wav");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();

                    for (int i = 0; i <= 100; i++) {
                        barra1.setValue(i);
                        barra2.setValue(i);

                        if (i == 100) {
                            Thread.sleep(clip.getMicrosecondLength() / 1000);
                            new controle().setVisible(true);
                            dispose();
                            break;
                        }

                        Thread.sleep(90);
                    }

                    clip.stop();
                    clip.close();
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        progressThread.start();
    }

    private void carregarImagem() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/OSIRIS", "root", "1234");

            String sql = "SELECT image FROM imagens WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] imageData = rs.getBytes("image");
                ImageIcon imageIcon = new ImageIcon(imageData);

                Image image = imageIcon.getImage().getScaledInstance(icon.getWidth(), icon.getHeight(), Image.SCALE_SMOOTH);

                ImageIcon scaledIcon = new ImageIcon(image);
                icon.setIcon(scaledIcon);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        icon = new javax.swing.JLabel();
        barra1 = new javax.swing.JProgressBar();
        barra2 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusable(false);
        setFocusableWindowState(false);
        setResizable(false);

        jPanel1.setLayout(null);

        icon.setBackground(new java.awt.Color(255, 153, 51));
        icon.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(icon);
        icon.setBounds(0, 0, 510, 320);

        barra1.setBackground(new java.awt.Color(102, 102, 102));
        barra1.setForeground(new java.awt.Color(0, 0, 0));
        barra1.setValue(10);
        barra1.setAlignmentX(0.9F);
        barra1.setAlignmentY(0.7F);
        barra1.setAutoscrolls(true);
        barra1.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);
        barra1.setDoubleBuffered(true);
        barra1.setFocusCycleRoot(true);
        barra1.setFocusTraversalPolicyProvider(true);
        barra1.setIndeterminate(true);
        barra1.setInheritsPopupMenu(true);
        barra1.setStringPainted(true);
        jPanel1.add(barra1);
        barra1.setBounds(0, 240, 440, 10);

        barra2.setBackground(new java.awt.Color(102, 102, 102));
        barra2.setForeground(new java.awt.Color(0, 0, 0));
        barra2.setValue(10);
        barra2.setAlignmentX(0.9F);
        barra2.setAlignmentY(0.7F);
        barra2.setAutoscrolls(true);
        barra2.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);
        barra2.setDoubleBuffered(true);
        barra2.setFocusCycleRoot(true);
        barra2.setFocusTraversalPolicyProvider(true);
        barra2.setIndeterminate(true);
        barra2.setInheritsPopupMenu(true);
        barra2.setStringPainted(true);
        jPanel1.add(barra2);
        barra2.setBounds(0, 10, 440, 10);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(455, 291));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Start().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barra1;
    private javax.swing.JProgressBar barra2;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
