package Telas;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.sql.*;
import org.opencv.core.Mat;
import java.util.List;

public final class Cam extends javax.swing.JPanel implements Runnable {

    private VideoCapture video;
    private Mat frame;
    private BufferedImage buff;
    private final CascadeClassifier faceCascade;
    private Voice voice;

    public Cam() {
        initComponents();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        faceCascade = new CascadeClassifier("C:\\opencv\\sources\\data\\haarcascades_cuda\\haarcascade_frontalface_default.xml");
        initializeFreeTTS();

        iniciarReconhecimentoVoz();
        assistente = new Dialogos("Osíris");

        new Thread(this).start();
        speak(assistente.saudacao());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buff == null) {
            return;
        }
        g.drawImage(buff, 0, 0, buff.getWidth(), buff.getHeight(), null);
    }

    @Override
    public void run() {
        this.video = new VideoCapture(0);
        this.frame = new Mat();
        if (video.isOpened()) {
            while (true) {
                video.read(frame);
                if (!frame.empty()) {
                    detectAndRecognizeFaces(frame);
                    MatToBufferedImage(frame);
                    this.repaint();
                }
            }
        }

    }

    private boolean mensagemFalada = false;
private String ultimaPessoaReconhecida = null;

    private void detectAndRecognizeFaces(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);

        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 5, 0, new Size(10, 10), new Size(300, 300));

        boolean pessoaReconhecida = false;
        String nomeReconhecido = null;

        for (Rect rect : faces.toArray()) {
            Mat detectedFace = new Mat(grayFrame, rect);

            nomeReconhecido = getNomeFromDatabase(detectedFace);

            if (nomeReconhecido != null) {

                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
                 if (!nomeReconhecido.equals(ultimaPessoaReconhecida)) {
                System.out.println("Olá, " + nomeReconhecido + "!");
                speak("Olá, " + nomeReconhecido + "!");
                ultimaPessoaReconhecida = nomeReconhecido; 
            }
                pessoaReconhecida = true;
                break;
            } else {

                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
            }
        }

        if (!pessoaReconhecida && !mensagemFalada) {

            System.out.println("Nenhuma pessoa reconhecida");
            speak("Nenhuma pessoa reconhecida");
            mensagemFalada = true;
        }
    }

    private String getNomeFromDatabase(Mat detectedFace) {
        String url = "jdbc:mysql://localhost:3306/OSIRIS";
        String usuario = "root";
        String senha = "1234";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha)) {
            String sql = "SELECT nome, imagem FROM pessoas";
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                ResultSet resultSet = pstmt.executeQuery();

                List<Mat> facesFromDatabase = new ArrayList<>();
                List<String> namesFromDatabase = new ArrayList<>();

                while (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    byte[] imagemBytes = resultSet.getBytes("imagem");
                    Mat faceFromDatabase = bytesToMat(imagemBytes);

                    facesFromDatabase.add(faceFromDatabase);
                    namesFromDatabase.add(nome);
                }

                FaceComparator faceComparator = new FaceComparator();
                String nomeReconhecido = faceComparator.compareFaces(detectedFace, facesFromDatabase, namesFromDatabase);

                return nomeReconhecido;
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao consultar o banco de dados: " + ex.getMessage());
        }

        return null;
    }

    private List<Mat> getFacesFromDatabase() {
        List<Mat> faces = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/OSIRIS";
        String usuario = "root";
        String senha = "1234";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha)) {
            String sql = "SELECT imagem FROM reconhecimento";
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                ResultSet resultSet = pstmt.executeQuery();

                while (resultSet.next()) {
                    byte[] imagemBytes = resultSet.getBytes("imagem");
                    Mat face = bytesToMat(imagemBytes);
                    faces.add(face);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao recuperar faces do banco de dados: " + ex.getMessage());
        }

        return faces;
    }

    private Mat bytesToMat(byte[] bytes) {
        Mat mat = new Mat();
        mat.put(0, 0, bytes);
        return mat;
    }

    private void MatToBufferedImage(Mat mat) {
        int altura = mat.width();
        int largura = mat.height();
        int canais = mat.channels();

        byte[] source = new byte[altura * largura * canais];
        mat.get(0, 0, source);
        buff = new BufferedImage(altura, largura, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] saida = ((DataBufferByte) buff.getRaster().getDataBuffer()).getData();
        System.arraycopy(source, 0, saida, 0, source.length);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
  private Voice getVoice(String voiceName) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = null;

        Voice[] voices = voiceManager.getVoices();

        for (Voice v : voices) {
            if (v.getName().equalsIgnoreCase(voiceName)) {
                voice = v;
                break;
            }
        }

        if (voice != null) {
            voice.allocate();
            return voice;
        } else {
            System.err.println("A voz especificada não foi encontrada.");
            return null;
        }
    }

    private Dialogos assistente;

    private void initializeFreeTTS() {
        voice = getVoice("kevin16");

        if (voice != null) {

            voice.setRate(160);
            voice.setPitch(170);
            voice.setVolume(7.0f);

            voice.setPitchRange(3);
            voice.setPitchShift(1);
            assistente = new Dialogos("Assistente");
        } else {
            System.err.println("A voz especificada não foi encontrada.");
        }
    }

    private boolean reconhecimentoVozAtivo = false;

    private void iniciarReconhecimentoVoz() {
        if (!reconhecimentoVozAtivo) {
            reconhecimentoVozAtivo = true;
            new Thread(this::initializeSphinx4).start();
        }
    }

    public void initializeSphinx4() {
        Configuration config = new Configuration();
        config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        config.setDictionaryPath("src\\Grammars\\9365.dic");
        config.setLanguageModelPath("src\\Grammars\\9365.lm");
        try {
            LiveSpeechRecognizer rec = new LiveSpeechRecognizer(config);
            rec.startRecognition(true);

            Desktop desk = Desktop.getDesktop();

            boolean aguardandoComando = false;

            while (reconhecimentoVozAtivo) {
                SpeechResult result = rec.getResult();

                if (result != null) {
                    String command = result.getHypothesis().toLowerCase();

                    if (aguardandoComando) {

                        String resposta = assistente.reconhecerComando(command);

                        speak(resposta);

                        aguardandoComando = false;
                    } else {

                        aguardandoComando = true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao inicializar o reconhecimento Sphinx4: " + e.getMessage());
        }
    }

    String reconhecerComandoDeVoz() throws IOException {

        return assistente.reconhecerComando(comandoReconhecido());
    }

    void apresentarResposta(String resposta) throws IOException, URISyntaxException {

        speak(resposta);
    }

    private void speak(String text) {
        if (voice != null) {
            voice.speak(text);
        } else {
            System.err.println("A voz não foi inicializada corretamente.");
        }

    }

    private String comandoReconhecido() {

        return "Comando reconhecido";
    }

    void salvarDadosTreinamento(String perguntaUsuario, String respostaAssistente) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dados_treinamento.txt", true))) {

            writer.write(perguntaUsuario + ": " + respostaAssistente);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de treinamento: " + e.getMessage());
        }
    }
}
