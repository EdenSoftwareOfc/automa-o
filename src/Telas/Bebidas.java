/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import Conexao.ConfiguracaoConexao;
import java.awt.Component;
import java.awt.Image;
import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author denyl
 */
public class Bebidas extends javax.swing.JFrame {

    /**
     * Creates new form Bebidas
     */
    public Bebidas() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        preencherTabela();
        adicionar.setText("<html>A<br>D<br>I<br>C<br>I<br>O<br>N<br>A<br>R</html>");
        adicionar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        adicionar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TABELA = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        OBS = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MENU BEBIDAS");

        jPanel1.setBackground(new java.awt.Color(0, 204, 0));

        adicionar.setFont(new java.awt.Font("Tahoma", 1, 44)); // NOI18N
        adicionar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 102), 3, true));
        adicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adicionarActionPerformed(evt);
            }
        });

        TABELA.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(TABELA);

        OBS.setColumns(20);
        OBS.setLineWrap(true);
        OBS.setRows(5);
        OBS.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 0, 0), 2, true), "OBS:"));
        jScrollPane1.setViewportView(OBS);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

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

        pack();
    }// </editor-fold>//GEN-END:initComponents
// Função para gerar um ID de mesa com base no número da mesa


    private void adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarActionPerformed
        // Perguntar ao usuário qual é o número da mesa
        String numeroMesa = JOptionPane.showInputDialog(this, "Digite o número da mesa:");

        // Garantir que o número da mesa não está vazio
        if (numeroMesa == null || numeroMesa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Número da mesa não fornecido. Operação cancelada.");
            return; // Saia do método se o número da mesa não for fornecido
        }

        int mesaId = obterOuCriarMesaId(numeroMesa);

        String jdbcURL = ConfiguracaoConexao.getURL();
        String username = ConfiguracaoConexao.getUsuario();
        String password = ConfiguracaoConexao.getSenha();

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            DefaultTableModel tableModel = (DefaultTableModel) TABELA.getModel();

            // Verifica se há linhas selecionadas na tabela
            int[] linhasSelecionadas = TABELA.getSelectedRows();
            if (linhasSelecionadas.length > 0) {
                for (int i : linhasSelecionadas) {
                    // Obtém os valores da linha selecionada
                    String obs = OBS.getText().trim();
                    String nome = (String) tableModel.getValueAt(i, 1); // Suponhamos que o nome esteja na coluna 1
                    double preco = (double) tableModel.getValueAt(i, 2); // Suponhamos que o preço esteja na coluna 2
                    int qtd = (int) tableModel.getValueAt(i, 3); // Suponhamos que a quantidade de itens esteja na coluna 3

                    try {
                        if (obs.isEmpty()) {
                            throw new IllegalArgumentException("PREENCHA O CAMPO OBSERVAÇÕES");
                        }

                        // Insere os valores na tabela "pedidos" com o número da mesa
                        String sql = "INSERT INTO pedidos (num_mesa, nome, preco, qtd, obs) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setString(1, numeroMesa);
                            preparedStatement.setString(2, nome);
                            preparedStatement.setDouble(3, preco);
                            preparedStatement.setInt(4, qtd);
                            preparedStatement.setString(5, obs);
                            preparedStatement.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }


                        JOptionPane.showMessageDialog(this, "Seu pedido foi salvo na \nMesa: " + numeroMesa+ "\n ATUALIZI-A PARA VISUALIZAR!!!");
                    } catch (IllegalArgumentException ex) {
                        // Trata o caso em que o campo 'obs' está vazio
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma linha selecionada.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_adicionarActionPerformed

    private int obterOuCriarMesaId(String numeroMesa) {
        int idMesa = -1;
        String jdbcURL = ConfiguracaoConexao.getURL();
        String username = ConfiguracaoConexao.getUsuario();
        String password = ConfiguracaoConexao.getSenha();

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            // Verificar se o número da mesa já existe na tabela
            idMesa = obterMesaId(numeroMesa);

            if (idMesa == -1) {
                // O número da mesa não existe, criar uma nova entrada na tabela
                String sql = "INSERT INTO pedidos (num_mesa) VALUES (?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, numeroMesa);
                    int linhasAfetadas = preparedStatement.executeUpdate();
                    if (linhasAfetadas > 0) {
                        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            idMesa = generatedKeys.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return idMesa;
    }

    private int obterMesaId(String numeroMesa) {
        int idMesa = -1;
        String jdbcURL = ConfiguracaoConexao.getURL();
        String username = ConfiguracaoConexao.getUsuario();
        String password = ConfiguracaoConexao.getSenha();

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String sql = "SELECT num_mesa FROM pedidos WHERE num_mesa = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, numeroMesa);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        idMesa = resultSet.getInt("num_mesa");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return idMesa;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
            java.util.logging.Logger.getLogger(Bebidas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bebidas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bebidas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bebidas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Bebidas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea OBS;
    private javax.swing.JTable TABELA;
    private javax.swing.JButton adicionar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    public class ImageRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            if (value instanceof ImageIcon) {
                ImageIcon imagemIcon = (ImageIcon) value;
                Image imagemOriginal = imagemIcon.getImage();
                int desiredWidth = 200;
                int desiredHeight = 200;

                // Redimensiona a imagem para o tamanho desejado
                Image imagemRedimensionada = imagemOriginal.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
                ImageIcon imagemRedimensionadaIcon = new ImageIcon(imagemRedimensionada);
                label.setIcon(imagemRedimensionadaIcon);

                // Define a altura da linha para o tamanho da imagem
                if (table.getRowHeight(row) != desiredHeight) {
                    table.setRowHeight(row, desiredHeight);
                }
            }
            return label;
        }
    }

    private void preencherTabela() {
        String jdbcURL = ConfiguracaoConexao.getURL();
        String username = ConfiguracaoConexao.getUsuario();
        String password = ConfiguracaoConexao.getSenha();

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String sql = "SELECT nome, preco, imagen FROM bebidas";
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {

                DefaultTableModel tableModel = (DefaultTableModel) TABELA.getModel();

                // Defina os nomes das colunas da tabela
                tableModel.setColumnIdentifiers(new String[]{"Imagem", "Nome", "Preço", "QTD"});

                while (resultSet.next()) {
                    // 1. Obtenha a imagem do banco de dados e redimensione
                    byte[] imagemBytes = resultSet.getBytes("imagen");
                    ImageIcon imagemIcon = new ImageIcon(imagemBytes);
                    Image imagemRedimensionada = imagemIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    ImageIcon imagemRedimensionadaIcon = new ImageIcon(imagemRedimensionada);

                    // 2. Obtenha o nome e preço do banco de dados
                    String nome = resultSet.getString("nome");
                    double preco = resultSet.getDouble("preco");

                    // 3. Adicione os dados à tabela
                    TABELA.setDefaultEditor(Object.class, null);
                    TABELA.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    int qtd = 0;
                    tableModel.addRow(new Object[]{imagemRedimensionadaIcon, nome, preco, qtd});
                    TABELA.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
                    TABELA.getColumnModel().getColumn(3).setCellEditor(new SpinnerEditor(0, 100));
                }
            }
            // Ajuste o intervalo conforme necessário

        } catch (SQLException ex) {
        }
    }

}
