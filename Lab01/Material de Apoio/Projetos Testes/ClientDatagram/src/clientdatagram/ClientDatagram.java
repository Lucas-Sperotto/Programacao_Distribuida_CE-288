// Testa a classe Client.
package clientdatagram;

import javax.swing.JFrame;

public class ClientDatagram {

    public static void main(String args[]) {

        Client application = new Client(); // cria o cliente
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.waitForPackets(); // executa o aplicativo cliente
    } // fim de main
}  // fim da classe ClientTest