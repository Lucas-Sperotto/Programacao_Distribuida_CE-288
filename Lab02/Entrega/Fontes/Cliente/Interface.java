package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Classe que modela a interface gráfica com o usuário
 * @author Sperotto
 */
public final class Interface extends JFrame {

    private static JTextArea displayArea;
    private static JPanel boardPanel;
    private static JPanel panel;
    private static JButton iniciar_controle;
    private static JButton pausar_movimento;
    private static JButton reiniciar_movimento;
    private static JButton liberar_controle;
    private Logica logica;
    private int MyProcess;
    private RicartEAgrawala AL;
    private UDPTremClient SendTrem;

    /**
     * Configura a interface com o usuário.
     * @param P int - Identificador do Processo.
     * @param Host  
     */
    public Interface(int P, String Host) {

        super("Cliente: " + P);

        MyProcess = P;
        AL = new RicartEAgrawala(P);
        logica = new Logica();
        initComponents();
        setSize(350, 218); // configura o tamanho da janela
        setResizable(false);
        setVisible(true); // mostra a janela
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        startTrem();
        //AL.start();
    }

    private void initComponents() {

        displayArea = new JTextArea(4, 30); //configura JTextArea
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);

        iniciar_controle = new JButton("Iniciar Controle");
        iniciar_controle.addActionListener(logica);

        pausar_movimento = new JButton("Pausar Movimento");
        pausar_movimento.addActionListener(logica);

        reiniciar_movimento = new JButton("Reiniciar Movimento");
        reiniciar_movimento.addActionListener(logica);

        liberar_controle = new JButton("Liberar Controle");
        liberar_controle.addActionListener(logica);

        iniciar_controle.setEnabled(true);
        liberar_controle.setEnabled(false);
        reiniciar_movimento.setEnabled(false);
        pausar_movimento.setEnabled(false);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 0, 0));
        boardPanel.add(iniciar_controle);
        boardPanel.add(liberar_controle);
        boardPanel.add(pausar_movimento);
        boardPanel.add(reiniciar_movimento);

        panel = new JPanel(); // configure o painel que irá conter o boardPanel
        panel.add(boardPanel, BorderLayout.CENTER); // adiciona o boardPanel no panel
        add(panel, BorderLayout.CENTER); // adiciona o painel contêiner

    } // fim do construtor Client

    /**
     * Método sincronizado para setar estado do botão iniciar controle
     * @param estado boolean
     */
    public static void setButtonInit(boolean estado) {
        iniciar_controle.setEnabled(estado);
    }

    /**
     * Método para setar estado dos botões de controle da String
     * @param estado boolean
     */
    public static void setButtonMov(boolean estado) {
        pausar_movimento.setEnabled(estado);
        reiniciar_movimento.setEnabled(!estado);
    }

    /**
     * Método que instancia a conexão
     */
    public void startTrem() {
        SendTrem = new UDPTremClient(MyProcess);
    }

    /**
     * Manipula outputArea na thread de despacho de eventos
     * @param messageToDisplay String mensagem a ser impressa
     */
    public static void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {
                        displayArea.append(messageToDisplay); // atualiza a saída
                    } // fim do método run
                } // fim da classe inner
                ); // fim da chamada para SwingUtilities.invokeLater
    } // fim do método displayMessage

    /**
     * Classe interna privada para manipular a lógica presente na interface com usuário
     * bem como as ações.
     */
    private class Logica extends JPanel implements MouseMotionListener, ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == iniciar_controle) {
                initCotrol();
            }

            if (e.getSource() == pausar_movimento) {
                pauseMov();
            }

            if (e.getSource() == reiniciar_movimento) {
                reinitMov();
            }

            if (e.getSource() == liberar_controle) {
                freeCotrol();
            }
        }

        private void initCotrol() {
            iniciar_controle.setEnabled(false);
            boolean Sending = false;
            AL.initCritical();
            Controller.setCriticalSection(true);
            displayMessage("Requisitando Exclusão Mutua....\n");
            while (!Controller.getTokenhere()) {
            }
            while (!Sending) {
                Sending = SendTrem.getControl();
            }          
            liberar_controle.setEnabled(true);

        }

        private void pauseMov() {
            boolean Sending = false;
            displayMessage("Pausando movimento....\n");
            pausar_movimento.setEnabled(false);
            reiniciar_movimento.setEnabled(true);
            while (!Sending) {
                Sending = SendTrem.sendPAUSE();
            }
        }

        private void reinitMov() {

            boolean Sending = false;
            displayMessage("Iniciando Movimento....\n");
            pausar_movimento.setEnabled(true);
            reiniciar_movimento.setEnabled(false);
            while (!Sending) {
                Sending = SendTrem.sendMOV();
            }
        }

        private void freeCotrol() {
           
            iniciar_controle.setEnabled(true);
            liberar_controle.setEnabled(false);
            boolean Sending = false;
            displayMessage("Liberando Controle....\n");
            AL.exitCritical();
            while (!Sending) {
                Sending = SendTrem.freeControl();
            }
            Controller.setControle(true);            
            pausar_movimento.setEnabled(false);
            reiniciar_movimento.setEnabled(false);
             Controller.setCriticalSection(false);
            
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}