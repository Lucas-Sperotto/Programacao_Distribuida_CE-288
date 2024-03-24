package client;

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
 * Classe que cria comunicação com o processo trem e modela envios de 
 * operações
 * @author Sperotto
 */
public class UDPTremClient {

    private String serverHostname;
    private int Id;
    private DatagramSocket clientSocket;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private InetAddress IPAddress;
    private Integer Comando;
    private byte[] receiveData;
    private byte[] sendData;

    /**
     * Construtor
     * @param Id int id do processo
     */
    public UDPTremClient(int Id) {

        try {
            this.Id = Id;
            serverHostname = "127.0.0.1";
            receiveData = new byte[1];
            sendData = new byte[1];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                clientSocket = new DatagramSocket();
            } catch (SocketException ex) {
                Logger.getLogger(UDPTremClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * construtor
     * @param Id int id do processo
     * @param Host String endereço host
     */
    public UDPTremClient(int Id, String Host) {
        try {
            this.Id = Id;
            serverHostname = Host;
            receiveData = new byte[1];
            sendData = new byte[1];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                clientSocket = new DatagramSocket();
            } catch (SocketException ex) {
                Logger.getLogger(UDPTremClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Solicita serviço
     * @return boolean true para mensagem enviada false para falha
     */
    public boolean getControl() {

        try {

            IPAddress = InetAddress.getByName(serverHostname);

            Comando = new Integer(2);

            sendData[0] = Comando.byteValue();

            System.out.println("Enviando Comando CONTROL.");

            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4000 + Id);

            clientSocket.send(sendPacket);

            System.out.println("Comando CONTROL Enviado, Aguardando Retorno!!!!");

            clientSocket.setSoTimeout(10000);

            try {
                clientSocket.receive(receivePacket);
                System.out.println("Recebido Retorno!!!!");

            } catch (SocketTimeoutException ste) {
                System.err.println("Tempo limite excedido: Assumindo Mensagem como perdida.");
                System.err.println(ste);
                return false;
            }
            String resp = Byte.toString(receiveData[0]);
            int resposta = Integer.parseInt(resp);
            System.err.println(resposta);
            if (resposta == 7) {
                Interface.setButtonMov(true);
            }
            if (resposta == 8) {
                Interface.setButtonMov(false);
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

    /**
     * envia mensagem para movimentar string
     * @return boolean true para mensagem enviada false para falha
     */
    public boolean sendMOV() {

        try {


            IPAddress = InetAddress.getByName(serverHostname);

            Comando = new Integer(1);

            sendData[0] = Comando.byteValue();

            System.out.println("Enviando Comando MOV.");

            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4000 + Id);

            clientSocket.send(sendPacket);

            System.out.println("Comando MOV Enviado, Aguardando Retorno!!!!");

            clientSocket.setSoTimeout(10000);

            try {
                clientSocket.receive(receivePacket);
                System.out.println("Recebido Retorno!!!!");

            } catch (SocketTimeoutException ste) {
                System.err.println("Tempo limite excedido: Assumindo Mensagem como perdida.");
                System.err.println(ste);
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

    /**
     * Envia mensagem para pausar string
     * @return boolean true para mensagem enviada false para falha
     */
    public boolean sendPAUSE() {

        try {

            IPAddress = InetAddress.getByName(serverHostname);

            Comando = new Integer(0);

            sendData[0] = Comando.byteValue();

            System.out.println("Enviando Comando PAUSE.");

            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4000 + Id);

            clientSocket.send(sendPacket);

            System.out.println("Comando PAUSE Enviado, Aguardando Retorno!!!!");

            clientSocket.setSoTimeout(10000);

            try {
                clientSocket.receive(receivePacket);
                System.out.println("Recebido Retorno!!!!");

            } catch (SocketTimeoutException ste) {
                System.err.println("Tempo limite excedido: Assumindo Ping como perdido.");
                System.err.println(ste);
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

    /**
     * Libera controle do serviço
     * @return boolean true para mensagem enviada false para falha
     */
    public boolean freeControl() {

        try {


            IPAddress = InetAddress.getByName(serverHostname);

            Comando = new Integer(3);

            sendData[0] = Comando.byteValue();

            System.out.println("Enviando Comando FREE.");

            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4000 + Id);

            clientSocket.send(sendPacket);

            System.out.println("Comando FREE Enviado, Aguardando Retorno!!!!");

            clientSocket.setSoTimeout(10000);

            try {
                clientSocket.receive(receivePacket);
                System.out.println("Recebido Retorno!!!!");

            } catch (SocketTimeoutException ste) {
                System.err.println("Tempo limite excedido: Assumindo Mensagem como perdida.");
                System.err.println(ste);
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
