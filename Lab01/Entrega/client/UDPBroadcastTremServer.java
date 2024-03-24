package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que estabelece conexão com os clientes aatravés de Datagramas
 * portas configuradas como 4000 + id do Cliente
 * @author Sperotto
 */
public class UDPBroadcastTremServer {

    private DatagramSocket serverSocket;
    private int comando;
    private int Id;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private InetAddress IPAddress;
    private byte[] receiveData;
    private byte[] sendData;

    /**
     * Construtor Padrão.
     * @param Id int - indice da Thread/Cliente.
     */
    public UDPBroadcastTremServer(int Id) {
        try {
            
            this.Id = Id;
            receiveData = new byte[1];
            sendData = new byte[1];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try {
                serverSocket = new DatagramSocket((1000 + this.Id));
                System.out.println("Ouvindo porta de comunicação: " + (1000 + this.Id));
            } catch (SocketException ex) {
                System.err.println("Porta UDP Ocupada!!!!!");
                Logger.getLogger(UDPBroadcastTremServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Método que recebe a a informação para movimentar ou pausar o movimento da String.
     * @return boolean - true para movimentar, false para parar movimento.
     */
    public boolean recivEstado() {

        int port;
        String sentence;

        try {
            serverSocket.receive(receivePacket);
            System.out.println("Recebeu Pacote com o Estado: ");
        } catch (IOException ex) {
            System.err.println("Falha no recebimento do pacote");
            Logger.getLogger(UDPBroadcastTremServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        sentence = Byte.toString(receiveData[0]);
        comando = Integer.parseInt(sentence);
        switch (comando) {
            case 0: {
                Interface.setButtonInit(false);
                Interface.displayMessage("Cliente C0 Requisitou Serviço.\nAguarde Liberação...\n");
                break;
            }
            case 1: {
                Interface.setButtonInit(false);
                Interface.displayMessage("Cliente C1 Requisitou Serviço.\nAguarde Liberação...\n");
                break;
            }
            case 2: {
                Interface.setButtonInit(false);
                Interface.displayMessage("Cliente C2 Requisitou Serviço.\nAguarde Liberação...\n");
                break;

            }
            case 3: {
                Interface.setButtonInit(true);
                Interface.displayMessage("Serviço Liberado...\n");
                break;

            }
            default: {
                System.err.println("Estado não interpretado");
            }
        }


        IPAddress = receivePacket.getAddress();
        port = receivePacket.getPort();
        String capitalizedSentence = sentence.toUpperCase();
        sendData = capitalizedSentence.getBytes();
        
        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        
        System.out.println("Criou pacote para envio da confirmação");

        try {
            serverSocket.send(sendPacket);//replica pacote para o remetente de forma a confirmar o recebimento
            System.out.println("Enviou confirmação");
        } catch (IOException ex) {
            Logger.getLogger(UDPBroadcastTremServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (comando == 1) {
            return true;
        } else {
            return false;
        }
    }
}
