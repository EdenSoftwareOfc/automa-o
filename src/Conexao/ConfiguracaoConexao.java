/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

/**
 *
 * @author denyl
 */
public class ConfiguracaoConexao {
    private static String server;
    private static String database;
    private static String user;
    private static String password;

    public static void setConfiguracao(String server, String database, String user, String password) {
        ConfiguracaoConexao.server = server;
        ConfiguracaoConexao.database = database;
        ConfiguracaoConexao.user = user;
        ConfiguracaoConexao.password = password;
    }

    public static String getURL() {
        return "jdbc:mysql://" + server + ":3306/" + database;
    }

    public static String getUsuario() {
        return user;
    }

    public static String getSenha() {
        return password;
    }
}