package Conexao;

import Telas.TelaInicial;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL extends JFrame {

    private JTextField serverField;
    private JTextField databaseField;
    private JTextField userField;
    private JPasswordField passwordField;

    public ConexaoMySQL() {
        super("Configuração de Conexão MySQL");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        serverField = new JTextField("localhost");
        databaseField = new JTextField("ValleCockteis");
        userField = new JTextField("root");
        passwordField = new JPasswordField("1234");
        JButton connectButton = new JButton("Conectar");

        add(new JLabel("Servidor MySQL:"));
        add(serverField);
        add(new JLabel("Banco de Dados:"));
        add(databaseField);
        add(new JLabel("Usuário:"));
        add(userField);
        add(new JLabel("Senha:"));
        add(passwordField);
        add(new JLabel(""));
        add(connectButton);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String server = serverField.getText();
                String database = databaseField.getText();
                String user = userField.getText();
                String password = new String(passwordField.getPassword());

                // Configurar as informações de conexão
                ConfiguracaoConexao.setConfiguracao(server, database, user, password);

                // Tente estabelecer a conexão
                Connection connection = conectar(ConfiguracaoConexao.getURL(), ConfiguracaoConexao.getUsuario(), ConfiguracaoConexao.getSenha());
                if (connection != null) {
                    // Conexão bem-sucedida, faça o que precisa
                    System.out.println("Conectado com sucesso!");
                    // Feche a janela de configuração
                    Telas.TelaInicial teli = new TelaInicial();
                    teli.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao conectar ao banco de dados, Por favor reinicie.");
                }
            }
        });

        setLocationRelativeTo(null); // Centralize a janela na tela
        setVisible(true);
    }

    public static Connection conectar(String url, String usuario, String senha) {
        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            return conexao;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConexaoMySQL();
            }
        });
    }
}
