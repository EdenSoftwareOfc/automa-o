package Telas;

import java.sql.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

public class Dialogos {

    private String nome;

    public Dialogos(String nome) {
        this.nome = nome;
    }

    public String saudacao() {
        return "Olá!!!, Eu sou " + nome + ". Como posso ajudar você hoje?";
    }

    public String despedida() {
        return "Até logo! Se precisar de mais alguma coisa, estarei aqui.";
    }

    public String desculpas() {
        return "Peço desculpas pelo inconveniente. Vamos resolver isso imediatamente.";
    }

    public String sugestao() {
        return "Eu sugiro que você tente...";
    }

    public String instrucao() {
        return "Para fazer isso, siga estas instruções:";
    }

    public String informacaoAdicional() {
        return "Além disso, você pode querer saber que...";
    }

    public String negacao() {
        return "Desculpe, parece que houve um mal-entendido. Vamos tentar novamente.";
    }

    public String elogio() {
        return "Obrigado! Fico feliz em poder ajudar.";
    }

    public String explicacao() {
        return "Deixe-me explicar melhor...";
    }

    public String agradecimento() {
        return "Obrigado por me consultar. Tenha um ótimo dia!";
    }

    public String confirmacao() {
        return "Entendi. Confirme se isso está correto.";
    }

    public String reconhecerComando(String comando) {
        comando = comando.toLowerCase();
        if (comando.contains("horas")) {
            return mostrarHora();
        } else if (comando.contains("tempo")) {
            return mostrarTempo();
        } else if (comando.contains("abrir")) {
            return abrirApp(comando);
        } else if (comando.contains("fechar")) {
            return fecharApp(comando);
        } else if (comando.contains("agradecer")) {
            return agradecimento();
        } else if (comando.contains("confirmar")) {
            return confirmacao();
        } else if (comando.contains("negar")) {
            return negacao();
        } else if (comando.contains("elogiar")) {
            return elogio();
        } else if (comando.contains("explicar")) {
            return explicacao();
        } else if (comando.contains("instruir")) {
            return instrucao();
        } else if (comando.contains("sugerir")) {
            return sugestao();
        } else if (comando.contains("desculpar")) {
            return desculpas();
        } else if (comando.contains("saudacao")) {
            return saudacao();
        } else if (comando.contains("despedir")) {
            return despedida();
        } else if (comando.contains("informar")) {
            return informacaoAdicional();
        } else {
            return ("não entendi, poderia repetir?.");
        }
    }

    private String mostrarHora() {
        // Lógica para obter a hora atual
        Calendar agora = Calendar.getInstance();
        int hora = agora.get(Calendar.HOUR_OF_DAY);
        int minuto = agora.get(Calendar.MINUTE);
        return "Agora são " + hora + " horas e " + minuto + " minutos.";
    }

    private String mostrarTempo() {
        // Lógica para obter o tempo atual
        return "No momento não sou capaz de acessar dados deste tipo.";
    }
    private String url = "jdbc:mysql://localhost:3306/OSIRIS";
    private String usuario = "root";
    private String senha = "1234";

    private String abrirApp(String comando) {
        int indexAbrir = comando.indexOf("abrir");
        if (indexAbrir >= 0) {
            String nomeApp = comando.substring(indexAbrir + 6).trim(); // Remover "abrir" do comando
            try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
                String sql = "SELECT caminho_ou_url FROM aplicativos WHERE nome = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nomeApp);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String caminhoOuURL = rs.getString("caminho_ou_url");
                            if (isURL(caminhoOuURL)) {
                                openURL(caminhoOuURL);
                                return "Abrindo " + nomeApp + "...";
                            } else {
                                Runtime.getRuntime().exec(caminhoOuURL);
                                return "Abrindo " + nomeApp + "...";
                            }
                        } else {
                            return "Desculpe, o aplicativo " + nomeApp + " não foi encontrado.";
                        }
                    }
                }
            } catch (SQLException | IOException e) {
                return "Desculpe, ocorreu um erro ao abrir " + nomeApp + ". Por favor, tente novamente.";
            }
        }
        return null;
    }

    private String fecharApp(String comando) {
        String nomeApp = comando.substring(comando.indexOf("fechar") + 6).trim(); // Remover "fechar" do comando
        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            String sql = "SELECT caminho_ou_pid FROM aplicativos WHERE nome = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nomeApp);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String caminhoOuPID = rs.getString("caminho_ou_pid");
                        Runtime.getRuntime().exec("cmd.exe /c taskkill /f /im " + caminhoOuPID);
                        return "Fechando " + nomeApp + "...";
                    } else {
                        return "Desculpe, o aplicativo " + nomeApp + " não foi encontrado.";
                    }
                }
            }
        } catch (SQLException | IOException e) {
            return "Desculpe, ocorreu um erro ao fechar " + nomeApp + ". Por favor, tente novamente.";
        }
    }

    private boolean isURL(String str) {
        try {
            new URI(str);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private void openURL(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void salvarDadosTreinamento(String perguntaUsuario, String respostaAssistente) {
        // Aqui você precisa implementar a lógica para salvar os dados de treinamento no banco de dados
        // Por exemplo, inserir os dados na tabela "dados_treinamento" do banco de dados
        // Utilize a conexão com o banco de dados para executar uma instrução SQL de inserção
        String url = "jdbc:mysql://localhost:3306/seu_banco_de_dados";
        String usuario = "seu_usuario";
        String senha = "sua_senha";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            String sql = "INSERT INTO dados_treinamento (pergunta_usuario, resposta_assistente) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, perguntaUsuario);
                stmt.setString(2, respostaAssistente);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String gerarResposta(String pergunta) {
        // Converter a pergunta para minúsculas para facilitar a comparação
        pergunta = pergunta.toLowerCase();

        // Verificar se a pergunta contém algumas palavras-chave comuns
        if (pergunta.contains("horas")) {
            return mostrarHora();
        } else if (pergunta.contains("tempo")) {
            return mostrarTempo();
        } else if (pergunta.contains("notícias")) {
            return "Desculpe, não estou atualmente configurado para fornecer notícias.";
        } else {
            return "Desculpe, não entendi a pergunta. Por favor, tente novamente.";
        }
    }

}
