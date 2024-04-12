package Telas;

import java.awt.Dimension;
import org.opencv.core.Core;
import javax.swing.SwingUtilities;

public class controle extends javax.swing.JFrame {

    private Cam painel;

    public controle() {
     setUndecorated(true);
        initComponents();
        initializeOpenCV();
   
        setLocationRelativeTo(null);
    }

    private void initializeOpenCV() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        inic = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        sai = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Audio/giphy (1).gif"))); // NOI18N

        jMenu1.setText("Controles");

        inic.setText("iniciar");
        inic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicActionPerformed(evt);
            }
        });
        jMenu1.add(inic);

        jMenuItem1.setText("Settings");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        sai.setText("sair");
        sai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saiActionPerformed(evt);
            }
        });
        jMenu1.add(sai);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicActionPerformed
        initializePanel();

    }//GEN-LAST:event_inicActionPerformed

  
    private void initializePanel() {
      
        SwingUtilities.invokeLater(() -> {
            if (painel == null) {
                painel = new Cam();
                painel.setSize(new Dimension(400, 300));
                painel.setVisible(false);
                this.add(painel);
            }
            if (!painel.isVisible()) {
                painel.setVisible(false);
            }
        });
    }


    private void saiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saiActionPerformed

        System.exit(0);
    }//GEN-LAST:event_saiActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        config com = new config();
        com.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    public static void main(String args[]) {
        try {
           
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(controle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new controle().setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem inic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem sai;
    // End of variables declaration//GEN-END:variables
}
