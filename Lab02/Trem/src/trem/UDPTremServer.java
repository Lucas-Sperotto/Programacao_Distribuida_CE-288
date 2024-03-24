package trem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Classe que estabelece conexão com os clientes aatravés de Datagramas
 * portas configuradas como 4000 + id do Cliente
 * @author Sperotto
 */
public class UDPTremServer {

    private DatagramSocket serverSocket;
    private int[] comando;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private InetAddress IPAddress;
    private byte[] receiveData;
    private byte[] sendData;

    /**
     * Construtor Padrão.
     */
    public UDPTremServer() {
        
        try {
            comando = new int[2];
            receiveData = new byte[2];
            sendData = new byte[2];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket = new DatagramSocket((4000));
                System.out.println("Ouvindo porta de comunicação: " + (4000));
            } catch (SocketException ex) {
                System.err.println("Porta UDP Ocupada!!!!!\n" + ex);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Método que recebe a a informação para movimentar ou pausar o movimento da String.
     * @return int - 0 para parar, 1 para movimentar, 2 para iniciar controle 3 para liberar controle.
     */
    public int[] recivComando() {

        int port;

        String sentence;

        try {
            serverSocket.receive(receivePacket);
            System.out.print("Recebeu Pacote com o Comando: ");
        } catch (IOException ex) {
            System.err.println("Falha no recebimento do pacote\n" + ex);
        }
        Integer mov;

        sentence = Byte.toString(receiveData[0]);
        comando[0] = Integer.parseInt(sentence);
        sentence = Byte.toString(receiveData[1]);
        comando[1] = Integer.parseInt(sentence);
        String capitalizedSentence;
        switch (comando[0]) {
            case 0: {
                System.out.print("STOP\n");
                capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                break;
            }
            case 1: {
                System.out.print("MOV\n");
                break;
            }
            case 2: {
                if (Controller.getMovimento()) {
                    mov = new Integer(7);
                    sendData[0] = mov.byteValue();
                } else {
                    mov = new Integer(8);
                    sendData[0] = mov.byteValue();
                }
                System.out.print("CONTROL\n");
                break;
            }
            case 3: {
                capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                System.out.print("FREE\n");
                break;
            }
            default: {
                capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                System.err.print("Comando Não Intrepretado\n");
                break;
            }
        }
        IPAddress = receivePacket.getAddress();
        port = receivePacket.getPort();

        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        System.out.println("Criou pacote para envio da confirmação");

        try {
            serverSocket.send(sendPacket);//replica pacote para o remetente de forma a confirmar o recebimento
            System.out.println("Enviou confirmação");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return comando;
    }
}
