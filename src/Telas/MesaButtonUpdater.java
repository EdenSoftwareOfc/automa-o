/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class MesaButtonUpdater extends SwingWorker<Void, Void> {

    private final JPanel panelMesas;
    private final String jdbcURL;
    private final String username;
    private final String password;

    public MesaButtonUpdater(JPanel panelMesas, String jdbcURL, String username, String password) {
        this.panelMesas = panelMesas;
        this.jdbcURL = jdbcURL;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Void doInBackground() {
        while (!isCancelled()) {
            atualizarCoresBotoes();
            try {
                Thread.sleep(1000); // Atualiza a cada 1 segundos (ajuste conforme necessário)
            } catch (InterruptedException e) {
                // Lidar com a interrupção se necessário
            }
        }
        return null;
    }

    private void atualizarCoresBotoes() {
        List<Integer> mesasAtivas = obterMesasAtivas();
        for (Component component : panelMesas.getComponents()) {
            if (component instanceof JButton) {
                JButton buttonMesa = (JButton) component;
                int numMesa = extrairNumeroMesa(buttonMesa.getText());

                if (mesaContemProdutos(jdbcURL, username, password, numMesa)) {
                    buttonMesa.setBackground(Color.GREEN);  // Cor diferente para mesas com produtos
                } else {
                    buttonMesa.setBackground(Color.BLUE);
                }
            }
        }
    }

    private int extrairNumeroMesa(String textoBotao) {
        // Extrair o número da mesa do texto do botão
        return Integer.parseInt(textoBotao.replaceAll("[\\D]", ""));
    }

    private List<Integer> obterMesasAtivas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean mesaContemProdutos(String jdbcURL, String username, String password, int numMesa) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}