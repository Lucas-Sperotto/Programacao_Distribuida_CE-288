package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que envia informações no anel
 * @author Sperotto
 */
public class UDPAnelConnection {
    
    private int MyId;
    private int Token;
    private String serverHostname;
    private DatagramSocket serverSocket;
    private DatagramSocket clientSocket;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private byte[] receiveData;
    private byte[] sendData;
    private boolean isPing = false;

    /**
     * Construtor com o Id do processo
     * @param Id int ID do processo
     */
    public UDPAnelConnection(int Id) {
        
        try {
            serverHostname = "127.0.0.1";
            MyId = Id;
            
            receiveData = new byte[1];
            sendData = new byte[1];
            
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            try {
                clientSocket = new DatagramSocket();
                serverSocket = new DatagramSocket((5000 + MyId));
            } catch (SocketException ex) {
                System.err.println("Porta UDP Ocupada!!!!!");
                Logger.getLogger(UDPAnelConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Construtor com o Id do processo e endereço host
     * @param Host String endereço host
     * @param Id int ID do processo
     */
    public UDPAnelConnection(String Host, int Id) {
        
        try {
            serverHostname = Host;
            MyId = Id;
            
            receiveData = new byte[1];
            sendData = new byte[1];
            
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            try {
                clientSocket = new DatagramSocket();
                serverSocket = new DatagramSocket((5000 + MyId));
            } catch (SocketException ex) {
                System.err.println("Porta UDP Ocupada!!!!!");
                Logger.getLogger(UDPAnelConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Envia Ping ou pong
     * @param token int ping ou pong
     * @return boolean true para mensagem enviada false para falha
     */
    public boolean sendPingPong(int token) {
        
        try {            
            InetAddress IPAddress;
            
            try {
                IPAddress = InetAddress.getByName(serverHostname);
            } catch (UnknownHostException ex) {
                Logger.getLogger(UDPAnelConnection.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            
            System.out.println("Tentando se conectar com " + IPAddress + ") via porta UDP: " + (5000 + ((MyId + 1) % 3)));           
            Integer Ping = new Integer(token);            
            sendData[0] = Ping.byteValue();           
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, (5000 + ((MyId + 1) % 3)));
            
            try {
                clientSocket.send(sendPacket);
            } catch (IOException ex) {
                Logger.getLogger(UDPAnelConnection.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            
            System.out.println("Ping/Pong Enviado!!!!");           
            return true;
            
        } catch (Exception ex) {
            System.err.println(ex);
            return false;
        }
    }

    /**
     * Recebe ping ou pong
     * @return int pong ou pong
     */
    public int recivPingPong() {
        
        try {
            
            String sentence;   
            isPing = false;
            
            while (!isPing) {
                
                try {
                    serverSocket.receive(receivePacket);
                } catch (IOException ex) {
                    Logger.getLogger(UDPAnelConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
                sentence = Byte.toString(receiveData[0]);
                Token = Integer.parseInt(sentence);
                isPing = true;
            }             
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return Token;
    }
}
