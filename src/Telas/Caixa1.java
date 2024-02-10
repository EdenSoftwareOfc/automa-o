
package Telas;

import java.sql.*;
import Conexao.ConfiguracaoConexao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author denyl
 */
public class Caixa1 extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;

    public Caixa1() {
        initComponents();
        popularTabela();
    }

    private void popularTabela() {
        String jdbcURL = ConfiguracaoConexao.getURL();
        String username = ConfiguracaoConexao.getUsuario();
        String password = ConfiguracaoConexao.getSenha();

        try {
            Connection conn = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM pagamentos";
            ResultSet resultSet = statement.executeQuery(sql);

            DefaultTableModel model = (DefaultTableModel) caixa.getModel();
            while (resultSet.next()) {
                String formaPagamento = resultSet.getString("forma_pagamento");
                double valor = resultSet.getDouble("valor_total");

                // Verifique se a forma de pagamento já existe na tabela
                boolean formaPagamentoEncontrada = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (formaPagamento.equals(model.getValueAt(i, 0))) {
                        formaPagamentoEncontrada = true;
                        // Some o valor existente com o novo valor
                        double valorExistente = (double) model.getValueAt(i, 1);
                        model.setValueAt(valorExistente + valor, i, 1);
                        break;
                    }
                }

                // Se a forma de pagamento não foi encontrada, adicione-a à tabela
                if (!formaPagamentoEncontrada) {
                    model.addRow(new Object[]{formaPagamento, valor});
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        caixa = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        CALCULAR_VALOR = new javax.swing.JButton();
        finalizar_caixa = new javax.swing.JButton();

        setBackground(new java.awt.Color(0, 204, 0));
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        setClosable(true);
        setTitle("CAIXA");

        caixa.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        caixa.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        caixa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Forma de pagamento", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        caixa.setEnabled(false);
        jScrollPane1.setViewportView(caixa);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/money.png"))); // NOI18N
        jLabel1.setText("VALOR NO CAIXA");

        CALCULAR_VALOR.setFont(new java.awt.Font("Century Schoolbook", 1, 12)); // NOI18N
        CALCULAR_VALOR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/money.png"))); // NOI18N
        CALCULAR_VALOR.setText("RESULTADO DO CAIXA");
        CALCULAR_VALOR.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.orange, java.awt.Color.red, java.awt.Color.darkGray, java.awt.Color.darkGray));
        CALCULAR_VALOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CALCULAR_VALORActionPerformed(evt);
            }
        });

        finalizar_caixa.setFont(new java.awt.Font("Century Schoolbook", 1, 12)); // NOI18N
        finalizar_caixa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        finalizar_caixa.setText("FINALIZAR CAIXA");
        finalizar_caixa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.orange, java.awt.Color.red, java.awt.Color.darkGray, java.awt.Color.darkGray));
        finalizar_caixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalizar_caixaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(CALCULAR_VALOR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(finalizar_caixa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CALCULAR_VALOR, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(finalizar_caixa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CALCULAR_VALORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CALCULAR_VALORActionPerformed
        DefaultTableModel model = (DefaultTableModel) caixa.getModel();
        double valorTotal = 0.0;

        // Percorra as linhas da tabela e some os valores
        for (int i = 0; i < model.getRowCount(); i++) {
            double valor = (double) model.getValueAt(i, 1);
            valorTotal += valor;
        }

        // Exiba o valor total em um JOptionPane
        JOptionPane.showMessageDialog(null, "Valor Total no Caixa: R$" + valorTotal, "Resultado do Caixa", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_CALCULAR_VALORActionPerformed

    private void finalizar_caixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalizar_caixaActionPerformed
        String jdbcURL = ConfiguracaoConexao.getURL();
        String username = ConfiguracaoConexao.getUsuario();
        String password = ConfiguracaoConexao.getSenha();

        try {
            Connection conn = DriverManager.getConnection(jdbcURL, username, password);

            // Use uma declaração DELETE para remover todos os dados da tabela
            String deleteSQL = "DELETE FROM pagamentos";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Todos os dados da tabela pagamentos foram excluídos com sucesso.");
                DefaultTableModel model = (DefaultTableModel) caixa.getModel();
                model.setRowCount(0); // Isso remove todas as linhas da tabela

            } else {
                System.out.println("Nenhum dado foi excluído da tabela pagamentos.");
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }//GEN-LAST:event_finalizar_caixaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CALCULAR_VALOR;
    private javax.swing.JTable caixa;
    private javax.swing.JButton finalizar_caixa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
