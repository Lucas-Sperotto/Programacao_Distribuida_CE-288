// Testa a classe Server.
package serverdatagram;

import javax.swing.JFrame;

public class ServerDatagram {

    public static void main(String args[]) {

        Server application = new Server(); // cria o servidor
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.waitForPackets(); // executa o aplicativo servidor
    } // fim de main
} // fim da classe ServerTest