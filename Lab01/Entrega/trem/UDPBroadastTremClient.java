package trem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsabel por enviar por broadcast a todos os clientes a requisição de um serviço 
 * por outro cliente.
 * @author Sperotto
 */
public class UDPBroadastTremClient {

    private String serverHostname;
    private DatagramSocket clientSocket1;
    private DatagramSocket clientSocket2;
    private DatagramSocket clientSocket3;
    private DatagramPacket sendPacket1;
    private DatagramPacket sendPacket2;
    private DatagramPacket sendPacket3;
    private DatagramPacket receivePacket1;
    private DatagramPacket receivePacket2;
    private DatagramPacket receivePacket3;
    private InetAddress IPAddress;
    private Integer Info;
    private byte[] sendData;
    private byte[] receiveData;

    /**
     * Construtor padrão, host = 127.0.0.1
     */
    public UDPBroadastTremClient() {
        
        try {
            serverHostname = "127.0.0.1";
            sendData = new byte[1];
            receiveData = new byte[1];
            receivePacket1 = new DatagramPacket(receiveData, receiveData.length);
            receivePacket2 = new DatagramPacket(receiveData, receiveData.length);
            receivePacket3 = new DatagramPacket(receiveData, receiveData.length);
            try {
                clientSocket1 = new DatagramSocket();
                clientSocket2 = new DatagramSocket();
                clientSocket3 = new DatagramSocket();
            } catch (SocketException ex) {
                Logger.getLogger(UDPBroadastTremClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Construtor
     * @param Host String com o endereço IP do host
     */
    public UDPBroadastTremClient(String Host) {
        
        try {
            serverHostname = Host;
            sendData = new byte[1];
            receiveData = new byte[1];
            receivePacket1 = new DatagramPacket(receiveData, receiveData.length);
            receivePacket2 = new DatagramPacket(receiveData, receiveData.length);
            receivePacket3 = new DatagramPacket(receiveData, receiveData.length);
            try {
                clientSocket1 = new DatagramSocket();
                clientSocket2 = new DatagramSocket();
                clientSocket3 = new DatagramSocket();
            } catch (SocketException ex) {
                Logger.getLogger(UDPBroadastTremClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * 
     * @param IdClientUso int Id do cliente que sequisitou serviço
     * @return true se a mensagem foi enviada false se o envio falhou
     */
    public boolean sendEstado(int IdClientUso) {

        try {

            IPAddress = InetAddress.getByName(serverHostname);

            Info = new Integer(IdClientUso);

            sendData[0] = Info.byteValue();

            System.out.println("Preparando envio do Broadcast.");

            sendPacket1 = new DatagramPacket(sendData, sendData.length, IPAddress, 1000);
            sendPacket2 = new DatagramPacket(sendData, sendData.length, IPAddress, 1001);
            sendPacket3 = new DatagramPacket(sendData, sendData.length, IPAddress, 1002);

            clientSocket1.send(sendPacket1);
            clientSocket2.send(sendPacket2);
            clientSocket3.send(sendPacket3);

            System.out.println("Broadcast Enviado, Aguardando Retorno!!!!");

            clientSocket1.setSoTimeout(10000);
            clientSocket2.setSoTimeout(10000);
            clientSocket3.setSoTimeout(10000);


            try {
                clientSocket1.receive(receivePacket1);
                clientSocket2.receive(receivePacket2);
                clientSocket3.receive(receivePacket3);
            } catch (SocketTimeoutException ste) {
                System.err.println(ste);
                System.err.println("Tempo limite excedido: Assumindo Broadcast como perdido.");
                return false;
            }
            return true;
        } catch (UnknownHostException ex) {
            System.err.println(ex);
            return false;
        } catch (IOException ex) {
            System.err.println(ex);
            return false;
        }
    }
}
