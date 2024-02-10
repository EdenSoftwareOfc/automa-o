package Telas;

import static Conexao.ConexaoMySQL.conectar;
import Conexao.ConfiguracaoConexao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.*;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author denyl
 */
public class TelaInicial extends javax.swing.JFrame {

    private final DefaultTableModel tableModel;

    /**
     *
     * Creates new form TelaInicial
     */
    public TelaInicial() {
        initComponents();
        ADICIONAR.setText("<html>N<br>O<br>V<br>A<br><br>M<br>E<br>S<br>A</html>");
        tableModel = (DefaultTableModel) aparecer_dados.getModel();
        preencherTabelaComCardapio();
        redimensionarTabela();
        configurarRenderizador();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Certifique-se de que APARECER_EM_FORMA_DE_MESA tenha o layout apropriado, cor de fundo, etc.
        APARECER_EM_FORMA_DE_MESA.setLayout(new FlowLayout()); // Layout de fluxo simples

        criarBotoesMesa(); // Chame o método para preencher o painel de mesas
        SwingUtilities.invokeLater(() -> criarBotoesMesa());

    }

    private void redimensionarTabela() {
        // Redefinir as larguras das colunas
        TableColumn column = aparecer_dados.getColumnModel().getColumn(0); // Coluna de imagem
        column.setMinWidth(200);
        column.setMaxWidth(200);
        column.setPreferredWidth(200);

        // Redefinir a altura das linhas
        aparecer_dados.setRowHeight(200);

    }

    private void atualizarPainelMesas() {
        APARECER_EM_FORMA_DE_MESA.removeAll(); // Limpa o painel antes de adicionar botões de mesas atualizados

        // Adicione aqui o código para criar botões de mesas com base nas informações atualizadas do banco de dados
        criarBotoesMesa();
        preencherTabelaComCardapio();
        APARECER_EM_FORMA_DE_MESA.revalidate(); // Atualiza o layout do painel
        APARECER_EM_FORMA_DE_MESA.repaint(); // Redesenha o painel
    }

    private void preencherTabelaComCardapio() {
        String jdbcURL = ConfiguracaoConexao.getURL();
        String username = ConfiguracaoConexao.getUsuario();
        String password = ConfiguracaoConexao.getSenha();

        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement statement = connection.createStatement();

            String sql = "SELECT nome, preco, descricao, imagen FROM bebidas";
            ResultSet resultSet = statement.executeQuery(sql);

            // Limpe os dados existentes na tabela
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }

            // Preencha a tabela com os resultados da consulta
            while (resultSet.next()) {
                byte[] imagemBytes = resultSet.getBytes("imagen"); // Lê os dados binários da imagem do banco de dados
                String nome = resultSet.getString("nome");
                double preco = resultSet.getDouble("preco");
                String descricao = resultSet.getString("descricao");
                descricao = quebrarTextoEmVersos(descricao);

                descricao = descricao.replaceAll(";", "<br>"); // Use <br> para quebras de linha em HTML

                if (imagemBytes != null) {
                    ImageIcon imagemIcon = new ImageIcon(imagemBytes);

                    // Redimensione a imagem para 200x200 se necessário
                    if (imagemIcon.getIconWidth() > 200 || imagemIcon.getIconHeight() > 200) {
                        imagemIcon = new ImageIcon(imagemIcon.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
                    }

                    tableModel.addRow(new Object[]{imagemIcon, nome, preco, "<html>" + descricao});
                } else {
                    // Trate o caso em que não há imagem disponível
                    tableModel.addRow(new Object[]{null, nome, preco, descricao});
                }
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
        }
    }

    private String quebrarTextoEmVersos(String texto) {
        // Substitua os caracteres de quebra de linha existentes por uma marcação especial (por exemplo, ";")
        texto = texto.replaceAll("\n", ";");
        return texto;
    }

    private void configurarRenderizador() {
        aparecer_dados.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer()); // Define o renderizador apenas para a primeira coluna
        aparecer_dados.getColumnModel().getColumn(1).setCellRenderer(new CustomTableCellRenderer(24)); // Coluna "nome" com tamanho de fonte 24
        aparecer_dados.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRenderer(24)); // Coluna "preco" com tamanho de fonte 24

    }

    private class CustomTableCellRenderer extends DefaultTableCellRenderer {

        private int fontSize;

        public CustomTableCellRenderer(int fontSize) {
            this.fontSize = fontSize;
            setHorizontalAlignment(JLabel.CENTER); // Configura o alinhamento central
        }

        @Override
        public void setValue(Object value) {
            if (value != null) {
                setFont(new Font(getFont().getName(), Font.BOLD, fontSize));
                setText(value.toString());
            }
        }
    }
    private final boolean mesaPreenchida = false; // Variável de controle

    public List<Integer> obterMesasAtivas() {
        List<Integer> mesasAtivas = new ArrayList<>();
        String jdbcURL = ConfiguracaoConexao.getURL();
        String username = ConfiguracaoConexao.getUsuario();
        String password = ConfiguracaoConexao.getSenha();

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String sql = "SELECT num_mesa FROM mesas_ativas";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int numMesa = resultSet.getInt("num_mesa");
                        mesasAtivas.add(numMesa);
                    }
                }
            }
        } catch (SQLException ex) {
        }

        return mesasAtivas;
    }

    public boolean mesaContemProdutos(String jdbcURL, String username, String password, int numMesa) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String sql = "SELECT COUNT(*) FROM pedidos WHERE num_mesa = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, numMesa);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int quantidadeProdutos = resultSet.getInt(1);
                        return quantidadeProdutos > 0;  // Retorna true se houver produtos, false caso contrário
                    }
                }
            }
        } catch (SQLException ex) {
        }

        return false;
    }

    @SuppressWarnings("empty-statement")
    private void criarBotoesMesa() {
        String jdbcURL = "jdbc:mysql://localhost:3306/ValleCockteis";
        String username = "root";
        String password = "1234";

        List<Integer> mesasAtivas = obterMesasAtivas();
        JPanel panelMesas = new JPanel(new GridLayout(6, 7)); // Organiza os botões em 7 colunas, adicionando novas fileiras conforme necessário

        MesaButtonUpdater buttonUpdater = new MesaButtonUpdater(panelMesas, jdbcURL, username, password);
        buttonUpdater.execute();

        for (int numMesa : mesasAtivas) {
            JButton buttonMesa = new JButton("MESA " + numMesa);
            buttonMesa.setPreferredSize(new Dimension(160, 120));
            if (mesaContemProdutos(jdbcURL, username, password, numMesa)) {
                buttonMesa.setBackground(Color.RED);  // Cor diferente para mesas com produtos
            } else {
                buttonMesa.setBackground(Color.GREEN);
            }

            buttonMesa.addActionListener((var e) -> {
                String[] opcoes = {"Adicionar Produto", "Itens da Mesa"};
                String escolha = (String) JOptionPane.showInputDialog(this, "Escolha uma opção:", "Opções",
                        JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

                if (escolha != null) {
                    if (escolha.equals("Adicionar Produto")) {
                        Bebidas beb = new Bebidas();
                        beb.setVisible(true);
                    } else if (escolha.equals("Itens da Mesa")) {
                        // Código para abrir a janela "Itens da Mesa" aqui
                        JFrame frame = new JFrame("Itens da Mesa " + numMesa);
                        frame.setSize(500, 500);

                        DefaultTableModel tableModel = new DefaultTableModel();
                        JTable table = new JTable(tableModel);
                        JScrollPane scrollPane = new JScrollPane(table);

                        tableModel.addColumn("Nome");
                        tableModel.addColumn("Preço");
                        tableModel.addColumn("QTD");

                        double valorTotal = 0.0;// Variável para rastrear o valor total

                        String url = ConfiguracaoConexao.getURL();
                        String usuario = ConfiguracaoConexao.getUsuario();
                        String senha = ConfiguracaoConexao.getSenha();

                        Connection con = conectar(url, usuario, senha);

                        try (Connection connection = DriverManager.getConnection(jdbcURL, usuario, senha)) {
                            String sql = "SELECT nome, preco, qtd FROM pedidos WHERE num_mesa = ?";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                preparedStatement.setInt(1, numMesa);

                                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                    while (resultSet.next()) {
                                        String nome = resultSet.getString("nome");
                                        double preco = resultSet.getDouble("preco");
                                        int quant = resultSet.getInt("qtd");
                                        tableModel.addRow(new Object[]{nome, preco, quant});

                                        valorTotal += preco; // Atualize o valor total
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        frame.getContentPane().add(scrollPane);

                        JButton buttonFinalizar = new JButton("Finalizar");
                        JButton buttonImprimir = new JButton("Imprimir");

                        JPanel buttonPanel = new JPanel();
                        buttonPanel.add(buttonFinalizar);
                        buttonPanel.add(buttonImprimir);
                        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                        buttonFinalizar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent finalizarEvent) {
                                double valorTotal = 0.0;

                                for (int row = 0; row < tableModel.getRowCount(); row++) {
                                    double preco = (double) tableModel.getValueAt(row, 1); // Suponhamos que o preço esteja na coluna 1
                                    int quantidade = (int) tableModel.getValueAt(row, 2); // Suponhamos que a quantidade esteja na coluna 2
                                    valorTotal += preco * quantidade;
                                }
                                String mensagemValorTotal = "Valor Total: R$" + String.format("%.2f", valorTotal);

                                String[] opcoesPagamento = {"Débito", "Crédito", "PIX", "Dinheiro"};
                                String formaPagamentoSelecionada = (String) JOptionPane.showInputDialog(
                                        frame, mensagemValorTotal + "\n Selecione a forma de pagamento:", "Forma de Pagamento",
                                        JOptionPane.QUESTION_MESSAGE, null, opcoesPagamento, opcoesPagamento[0]);

                                if (formaPagamentoSelecionada != null) {
                                    String mensagem = mensagemValorTotal + "\nForma de pagamento: " + formaPagamentoSelecionada;

                                    try (Connection connection = DriverManager.getConnection(jdbcURL, usuario, senha)) {
                                        String insertSql = "INSERT INTO pagamentos (mesa_id, forma_pagamento, valor_total) VALUES (?, ?, ?)";
                                        String deleteSql = "DELETE FROM pedidos WHERE num_mesa = ?";
                                        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                                                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                                            connection.setAutoCommit(false); // Desativa o modo de confirmação automática

                                            insertStatement.setInt(1, numMesa); // Use o número da mesa atual
                                            insertStatement.setString(2, formaPagamentoSelecionada);
                                            insertStatement.setDouble(3, valorTotal); // Utilize o valor total calculado
                                            insertStatement.executeUpdate();

                                            deleteStatement.setInt(1, numMesa); // Use o número da mesa atual para exclusão
                                            deleteStatement.executeUpdate();

                                            connection.commit(); // Confirma as alterações
                                            connection.setAutoCommit(true); // Reativa o modo de confirmação automática

                                            atualizarPainelMesas();
                                            frame.dispose();
                                        } catch (SQLException ex) {
                                            connection.rollback(); // Desfaz as alterações em caso de erro
                                            ex.printStackTrace();
                                        }
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    System.out.println("Nenhuma forma de pagamento selecionada.");
                                }
                            }
                        });

                        buttonImprimir.addActionListener((ActionEvent imprimirEvent) -> {

                        });

                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                    }
                }
            });
            panelMesas.add(buttonMesa);
        }

        // Adiciona o painel de mesas ao JTabbedPane existente
        APARECER_EM_FORMA_DE_MESA.add("Mesas", panelMesas);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        aparecer_cardapio = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        aparecer_dados = new javax.swing.JTable();
        Criar_config = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        NOTURNO = new javax.swing.JToggleButton();
        Select_image_for_theme = new javax.swing.JButton();
        login_usu = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Login = new javax.swing.JTextField();
        senha = new javax.swing.JPasswordField();
        botao_confirmar = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        APARECER_EM_FORMA_DE_MESA = new javax.swing.JPanel();
        ADICIONAR = new javax.swing.JButton();
        atualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MENU INICIAL");
        setBackground(new java.awt.Color(0, 255, 0));
        setForeground(new java.awt.Color(0, 204, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });
        jTabbedPane1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTabbedPane1InputMethodTextChanged(evt);
            }
        });
        jTabbedPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabbedPane1KeyPressed(evt);
            }
        });

        aparecer_dados.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 2, true));
        aparecer_dados.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        aparecer_dados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Imagem", "Nome", "Preço R$", "Descrição"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        aparecer_dados.setEnabled(false);
        jScrollPane4.setViewportView(aparecer_dados);

        javax.swing.GroupLayout aparecer_cardapioLayout = new javax.swing.GroupLayout(aparecer_cardapio);
        aparecer_cardapio.setLayout(aparecer_cardapioLayout);
        aparecer_cardapioLayout.setHorizontalGroup(
            aparecer_cardapioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
        );
        aparecer_cardapioLayout.setVerticalGroup(
            aparecer_cardapioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CARDAPIO", aparecer_cardapio);

        Criar_config.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));

        NOTURNO.setText("NIGHT MOD");
        NOTURNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NOTURNOActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(NOTURNO, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(NOTURNO)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Select_image_for_theme.setText("jButton1");
        Select_image_for_theme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Select_image_for_themeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Criar_configLayout = new javax.swing.GroupLayout(Criar_config);
        Criar_config.setLayout(Criar_configLayout);
        Criar_configLayout.setHorizontalGroup(
            Criar_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Criar_configLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 435, Short.MAX_VALUE)
                .addComponent(Select_image_for_theme)
                .addGap(342, 342, 342))
        );
        Criar_configLayout.setVerticalGroup(
            Criar_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Criar_configLayout.createSequentialGroup()
                .addGroup(Criar_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Criar_configLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Criar_configLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(Select_image_for_theme)))
                .addContainerGap(508, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CONFIGURACÕES", Criar_config);

        login_usu.setBackground(new java.awt.Color(0, 0, 0));

        jLabel4.setFont(new java.awt.Font("Sylfaen", 1, 36)); // NOI18N
        jLabel4.setText("LOGIN:");

        jLabel5.setFont(new java.awt.Font("Sylfaen", 1, 36)); // NOI18N
        jLabel5.setText("SENHA:");

        Login.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        senha.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N

        botao_confirmar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botao_confirmar.setText("ENTRAR");
        botao_confirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_confirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(senha, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Login, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(botao_confirmar)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Login, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(senha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botao_confirmar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout login_usuLayout = new javax.swing.GroupLayout(login_usu);
        login_usu.setLayout(login_usuLayout);
        login_usuLayout.setHorizontalGroup(
            login_usuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, login_usuLayout.createSequentialGroup()
                .addContainerGap(467, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(359, 359, 359))
        );
        login_usuLayout.setVerticalGroup(
            login_usuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(login_usuLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(123, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("ADMINISTRAÇÃO", login_usu);

        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 6, true));

        jButton2.setFont(new java.awt.Font("Rockwell", 3, 90)); // NOI18N
        jButton2.setText("FECHAR");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 1083, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("SAIR", jPanel10);

        APARECER_EM_FORMA_DE_MESA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        APARECER_EM_FORMA_DE_MESA.setLayout(new java.awt.GridLayout(1, 0));
        jTabbedPane1.addTab("Mesas", APARECER_EM_FORMA_DE_MESA);

        jTabbedPane1.setSelectedIndex(4);

        ADICIONAR.setFont(new java.awt.Font("Tahoma", 1, 44)); // NOI18N
        ADICIONAR.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 153, 0)));
        ADICIONAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ADICIONARActionPerformed(evt);
            }
        });

        atualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        atualizar.setToolTipText("ATUALIZAR PAINEL");
        atualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ADICIONAR, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                    .addComponent(atualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ADICIONAR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(atualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabbedPane1KeyPressed
        System.exit(1);   // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed


    private void botao_confirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_confirmarActionPerformed
        String loginDigitado = Login.getText(); // Substitua campoLogin pelo nome do seu campo de login.
        char[] senhaDigitada = senha.getPassword(); // Substitua campoSenha pelo nome do seu campo de senha.

        // Verifique se as credenciais estão corretas (substitua "login" e "senha" pelos valores corretos).
        if ("ALMEIDA".equals(loginDigitado) && "ALMEIDA".equals(new String(senhaDigitada))) {
            // As credenciais estão corretas, então você pode abrir a nova tela.
            Menu_Admin neu = new Menu_Admin();// Substitua MinhaNovaTela pelo nome da sua classe de tela.
            neu.setVisible(true);
        } else {
            // Caso as credenciais não sejam corretas, você pode exibir uma mensagem de erro, por exemplo.
            JOptionPane.showMessageDialog(this, "Login ou senha incorretos", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
        }
        Login.setText("");
        senha.setText("");

    }//GEN-LAST:event_botao_confirmarActionPerformed


    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jTabbedPane1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTabbedPane1InputMethodTextChanged

    }//GEN-LAST:event_jTabbedPane1InputMethodTextChanged

    private void ADICIONARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ADICIONARActionPerformed
        Bebidas bebi = new Bebidas();
        bebi.setVisible(true);

        atualizarPainelMesas();
    }//GEN-LAST:event_ADICIONARActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        atualizarPainelMesas();

    }//GEN-LAST:event_formWindowOpened

    private void atualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atualizarActionPerformed
        atualizarPainelMesas();
    }//GEN-LAST:event_atualizarActionPerformed

    private void NOTURNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NOTURNOActionPerformed

        if (NOTURNO.isSelected()) {
            // Se o botão de alternância NOTURNO estiver selecionado, defina a cor de fundo desejada.
            NOTURNO.setBackground(Color.BLACK); // Exemplo: define a cor de fundo como preto.
            NOTURNO.setForeground(Color.white);
            APARECER_EM_FORMA_DE_MESA.setBackground(Color.BLACK);
            APARECER_EM_FORMA_DE_MESA.setForeground(Color.WHITE);
            ADICIONAR.setBackground(Color.BLACK);
            ADICIONAR.setForeground(Color.WHITE);
            Criar_config.setBackground(Color.BLACK);
            Criar_config.setForeground(Color.WHITE);
            Login.setBackground(Color.BLACK);
            Login.setForeground(Color.WHITE);
            login_usu.setBackground(Color.BLACK);
            login_usu.setForeground(Color.WHITE);
            aparecer_cardapio.setBackground(Color.BLACK);
            aparecer_cardapio.setBackground(Color.WHITE);
            aparecer_dados.setBackground(Color.BLACK);
            aparecer_dados.setForeground(Color.WHITE);
            atualizar.setBackground(Color.BLACK);
            atualizar.setForeground(Color.WHITE);
            botao_confirmar.setBackground(Color.BLACK);
            botao_confirmar.setForeground(Color.WHITE);
            jButton2.setBackground(Color.BLACK);
            jButton2.setForeground(Color.WHITE);
            jLabel4.setBackground(Color.BLACK);
            jLabel4.setForeground(Color.WHITE);
            jLabel5.setBackground(Color.BLACK);
            jLabel5.setForeground(Color.WHITE);
            jPanel1.setBackground(Color.BLACK);
            jPanel1.setForeground(Color.WHITE);
            jPanel10.setBackground(Color.BLACK);
            jPanel10.setForeground(Color.WHITE);
            jPanel11.setBackground(Color.BLACK);
            jPanel11.setForeground(Color.WHITE);
            jPanel3.setBackground(Color.BLACK);
            jPanel3.setForeground(Color.WHITE);
            jScrollPane4.setBackground(Color.BLACK);
            jScrollPane4.setForeground(Color.WHITE);
            jTabbedPane1.setBackground(Color.BLACK);
            jTabbedPane1.setForeground(Color.WHITE);
            login_usu.setBackground(Color.BLACK);
            login_usu.setForeground(Color.WHITE);
            senha.setBackground(Color.BLACK);
            senha.setForeground(Color.WHITE);
            jTabbedPane1.setBackground(Color.BLACK);
            jTabbedPane1.setForeground(Color.WHITE);
        } else {
            NOTURNO.setBackground(Color.WHITE); // Exemplo: define a cor de fundo como preto.
            NOTURNO.setForeground(Color.BLACK);
            APARECER_EM_FORMA_DE_MESA.setBackground(Color.WHITE);
            APARECER_EM_FORMA_DE_MESA.setForeground(Color.BLACK);
            ADICIONAR.setBackground(Color.WHITE);
            ADICIONAR.setForeground(Color.BLACK);
            Criar_config.setBackground(Color.WHITE);
            Criar_config.setForeground(Color.BLACK);
            Login.setBackground(Color.WHITE);
            Login.setForeground(Color.BLACK);
            login_usu.setBackground(Color.WHITE);
            login_usu.setForeground(Color.BLACK);
            aparecer_cardapio.setBackground(Color.WHITE);
            aparecer_cardapio.setBackground(Color.BLACK);
            aparecer_dados.setBackground(Color.WHITE);
            aparecer_dados.setForeground(Color.BLACK);
            atualizar.setBackground(Color.WHITE);
            atualizar.setForeground(Color.BLACK);
            botao_confirmar.setBackground(Color.WHITE);
            botao_confirmar.setForeground(Color.BLACK);
            jButton2.setBackground(Color.WHITE);
            jButton2.setForeground(Color.BLACK);
            jLabel4.setBackground(Color.WHITE);
            jLabel4.setForeground(Color.BLACK);
            jLabel5.setBackground(Color.WHITE);
            jLabel5.setForeground(Color.BLACK);
            jPanel1.setBackground(Color.WHITE);
            jPanel1.setForeground(Color.BLACK);
            jPanel10.setBackground(Color.WHITE);
            jPanel10.setForeground(Color.BLACK);
            jPanel11.setBackground(Color.WHITE);
            jPanel11.setForeground(Color.BLACK);
            jPanel3.setBackground(Color.WHITE);
            jPanel3.setForeground(Color.BLACK);
            jScrollPane4.setBackground(Color.WHITE);
            jScrollPane4.setForeground(Color.BLACK);
            jTabbedPane1.setBackground(Color.WHITE);
            jTabbedPane1.setForeground(Color.BLACK);
            login_usu.setBackground(Color.WHITE);
            login_usu.setForeground(Color.BLACK);
            senha.setBackground(Color.WHITE);
            senha.setForeground(Color.BLACK);
            jTabbedPane1.setForeground(Color.WHITE);
            jTabbedPane1.setBackground(Color.BLACK);

        }


    }//GEN-LAST:event_NOTURNOActionPerformed

    private void Select_image_for_themeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Select_image_for_themeActionPerformed
 JFileChooser fileChooser = new JFileChooser();
    int resultado = fileChooser.showOpenDialog(this);

    if (resultado == JFileChooser.APPROVE_OPTION) {
        String caminhoDoArquivo = fileChooser.getSelectedFile().getAbsolutePath();
        if (imagemSelecionadaListener != null) {
            imagemSelecionadaListener.onImagemSelecionada(caminhoDoArquivo);
        }
    }

    }//GEN-LAST:event_Select_image_for_themeActionPerformed

    private ImagemSelecionadaListener imagemSelecionadaListener;

    public void setImagemSelecionadaListener(ImagemSelecionadaListener listener) {
        this.imagemSelecionadaListener = listener;
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
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ADICIONAR;
    private javax.swing.JPanel APARECER_EM_FORMA_DE_MESA;
    private javax.swing.JPanel Criar_config;
    private javax.swing.JTextField Login;
    private javax.swing.JToggleButton NOTURNO;
    private javax.swing.JButton Select_image_for_theme;
    private javax.swing.JPanel aparecer_cardapio;
    private javax.swing.JTable aparecer_dados;
    private javax.swing.JButton atualizar;
    private javax.swing.JButton botao_confirmar;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel login_usu;
    private javax.swing.JPasswordField senha;
    // End of variables declaration//GEN-END:variables

}
