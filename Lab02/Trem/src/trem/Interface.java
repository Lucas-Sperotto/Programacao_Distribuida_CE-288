package trem;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Classe que modela interface com o usuário
 * @author Sperotto
 */
public class Interface extends JFrame {

    private static JTextArea displayArea;
    private static JLabel jLabel1;
    private Thread Anima;
    private static int positionX;
    private static int positionY = 0;

    /**
     * Configura a interface com o usuário.
     */
    public Interface() {
        super("Trem");

        positionX = 0;
        positionY = 10;
        Anima = new Thread(new Animacao());
        initComponents();
        Anima.start();
        setSize(350, 218); // configura o tamanho da janela
        setResizable(false);
        setVisible(true); // mostra a janela
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {

        displayArea = new JTextArea(4, 30); //configura JTextArea
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);

        jLabel1 = new JLabel();
        jLabel1.setText("ABC");
        jLabel1.setFont(new java.awt.Font("Comic Sof ratednewans MS", 0, 24));
        jLabel1.setLocation(positionX, positionY);
        jLabel1.setVisible(true);
        add(jLabel1);

    } // fim do construtor Client

    /**
     * Manipula outputArea na thread de despacho de eventos
     * @param messageToDisplay
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
     * Classe interna privada para controle da animação da String
     */
    private class Animacao implements Runnable {

        @Override
        public void run() {

            while (true) {

                if (Controller.getMovimento()) {

                    positionX++;

                    jLabel1.setLocation(positionX, positionY);

                    if (positionX == 350) {
                        positionX = -37;
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        System.err.println(ex);
                    }
                }
            }
        }
    }
}
