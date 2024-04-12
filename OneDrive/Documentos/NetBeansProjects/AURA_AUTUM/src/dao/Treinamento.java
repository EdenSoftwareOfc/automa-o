package dao;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Treinamento {

    private String url;
    private String usuario;
    private String senha;

    public Treinamento(String url, String usuario, String senha) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    public void coletarDadosTreinamento(String perguntaUsuario, String respostaAssistente) {
        String sql = "INSERT INTO dados_treinamento (pergunta_usuario, resposta_assistente) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, perguntaUsuario);
            stmt.setString(2, respostaAssistente);
            stmt.executeUpdate();
            System.out.println("Dados de treinamento salvos com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar dados de treinamento: " + e.getMessage());
        }
    }

    public String obterRespostaDaRedeNeural(String perguntaUsuario) {
        String respostaDaRedeNeural = "";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            String sql = "SELECT resposta_assistente FROM dados_treinamento WHERE pergunta_usuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, perguntaUsuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        respostaDaRedeNeural = rs.getString("resposta_assistente");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respostaDaRedeNeural;
    }

    public void coletarFeedbackDoUsuario(String perguntaUsuario, String respostaAssistente, boolean respostaUtil) {

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            String sql = "INSERT INTO feedback (pergunta_usuario, resposta_assistente, resposta_util) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, perguntaUsuario);
                stmt.setString(2, respostaAssistente);
                stmt.setBoolean(3, respostaUtil);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
