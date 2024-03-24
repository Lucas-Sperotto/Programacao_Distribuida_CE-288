package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Classe que cria a conexão com os clientes e troca as mensagens
 * @author Sperotto
 */
public class UDPConnection {

    private int MyId;
    private String serverHostname;
    private DatagramSocket clientSocket1;
    private DatagramSocket clientSocket2;
    private DatagramSocket serverReqSocket;
    private DatagramSocket serverRepSocket;
    private DatagramPacket receiveReqPacket;
    private DatagramPacket receiveRepPacket;
    private DatagramPacket sendReqPacket;
    private DatagramPacket sendRepPacket;
    private byte[] sendReqData;
    private byte[] sendRepData;
    private byte[] receiveReqData;
    private byte[] receiveRepData;

    /**
     * Construtor com o Id do processo
     * @param Id int ID do processo
     */
    public UDPConnection(int Id) {

        try {
            serverHostname = "127.0.0.1";
            MyId = Id;
            sendReqData = new byte[2];
            sendRepData = new byte[1];
            receiveReqData = new byte[2];
            receiveRepData = new byte[1];

            receiveReqPacket = new DatagramPacket(receiveReqData, receiveReqData.length);
            receiveRepPacket = new DatagramPacket(receiveRepData, receiveRepData.length);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Construtor com o Id do processo e endereço host
     * @param Host String endereço host
     * @param Id int ID do processo
     */
    public UDPConnection(String Host, int Id) {

        try {
            serverHostname = Host;
            MyId = Id;
            sendReqData = new byte[2];
            sendRepData = new byte[1];
            receiveReqData = new byte[2];
            receiveRepData = new byte[1];

            receiveReqPacket = new DatagramPacket(receiveReqData, receiveReqData.length);
            receiveRepPacket = new DatagramPacket(receiveRepData, receiveRepData.length);

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Envia mensagem de Request
     * @param req REQUEST mensagem a ser enviada
     * @param destino int cliente destino da mensagem
     * @return
     */
    public boolean Send_Message(RicartEAgrawala.REQUEST req, int destino) {

        try {
            DatagramPacket receivePacket;
            clientSocket1 = new DatagramSocket();
            InetAddress IPAddress;

            try {
                IPAddress = InetAddress.getByName(serverHostname);
            } catch (UnknownHostException ex) {
                System.err.println(ex);
                return false;
            }

            System.out.println("Tentando se conectar com " + IPAddress + ") via porta UDP: " + (5000 + destino));
            Integer J = new Integer(req.j);
            Integer K = new Integer(req.k);
            sendReqData[0] = K.byteValue();
            sendReqData[1] = J.byteValue();
            sendReqPacket = new DatagramPacket(sendReqData, sendReqData.length, IPAddress, (5000 + destino));

            try {
                clientSocket1.send(sendReqPacket);
            } catch (IOException ex) {
                System.err.println(ex);
                clientSocket1.close();
                return false;
            }
            clientSocket1.setSoTimeout(10000);
            receivePacket = new DatagramPacket(sendReqData, sendReqData.length);
            try {
                clientSocket1.receive(receivePacket);
                System.out.println("REQUEST Enviado para " + destino + "!!!!");
                System.out.println("Recebido Retorno de REQUEST!!!!");
                clientSocket1.close();
                return true;

            } catch (SocketTimeoutException ste) {
                System.err.println("Tempo limite excedido: Assumindo Mensagem como perdida.");
                System.err.println(ste);
                clientSocket1.close();
                return false;
            }
        } catch (Exception ex) {
            System.err.println(ex);
            clientSocket1.close();
            return false;
        }
    }

    /**
     * Envia mensagem de Reply
     * @param destino int cliente de destino da mensagem
     * @return boolean true para mensagem enviada false para falha
     */
    public boolean Send_Message(int destino) {

        try {
            DatagramPacket receivePacket;
            clientSocket2 = new DatagramSocket();
            InetAddress IPAddress;

            try {
                IPAddress = InetAddress.getByName(serverHostname);
            } catch (UnknownHostException ex) {
                System.err.println(ex);
                return false;
            }

            System.out.println("Tentando se conectar com " + IPAddress + ") via porta UDP: " + (2000 + destino));
            Integer REPLY = new Integer(1);
            sendRepData[0] = REPLY.byteValue();
            sendRepPacket = new DatagramPacket(sendRepData, sendRepData.length, IPAddress, (2000 + destino));

            try {
                clientSocket2.send(sendRepPacket);

            } catch (IOException ex) {
                System.err.println(ex);
                clientSocket2.close();
                return false;
            }
            clientSocket2.setSoTimeout(10000);
            receivePacket = new DatagramPacket(sendRepData, sendRepData.length);
            try {
                clientSocket2.receive(receivePacket);
                System.out.println("REPLY Enviado para " + destino + "!!!!");
                System.out.println("Recebido Retorno do REPLY !!!!");
                clientSocket2.close();
                return true;

            } catch (SocketTimeoutException ste) {
                System.err.println("Tempo limite excedido: Assumindo Mensagem como perdida.");
                System.err.println(ste);
                clientSocket2.close();
                return false;
            }
        } catch (Exception ex) {
            System.err.println(ex);
            clientSocket2.close();
            return false;
        }
    }

    /**
     * Recebe mensagem de request
     * @param req REQUEST mensagem de request
     */
    public void recieveRequest(RicartEAgrawala.REQUEST req) {
        try {

            int k;
            int j;
            String K;
            String J;
            System.out.println("Ouvindo REQUEST");
            try {
                serverReqSocket = new DatagramSocket((5000 + MyId));
                serverReqSocket.receive(receiveReqPacket);
            } catch (IOException ex) {
                System.err.println(ex);
            }
            InetAddress IPAddress = receiveReqPacket.getAddress();
            int port = receiveReqPacket.getPort();

            DatagramPacket sendPacket = new DatagramPacket(receiveReqPacket.getData(), receiveReqPacket.getData().length, IPAddress, port);
            System.out.println("Recebeu REQUEST de: " + port + "\nCriou pacote para envio da confirmação");

            try {
                serverReqSocket.send(sendPacket);//replica pacote para o remetente de forma a confirmar o recebimento
                serverReqSocket.close();
                System.out.println("Enviou confirmação de REQUEST");
            } catch (IOException ex) {
                System.err.println(ex);
            }
            K = Byte.toString(receiveReqData[0]);
            k = Integer.parseInt(K);
            J = Byte.toString(receiveReqData[1]);
            j = Integer.parseInt(J);
            req.j = j;
            req.k = k;

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Recebe mensagem de Reply
     * @return boolean true se recebeu false caso ocorra algum erro
     */
    public boolean recieveReply() {

        try {

            System.out.println("Ouvindo REPLY");
            try {
                serverRepSocket = new DatagramSocket((2000 + MyId));
                serverRepSocket.receive(receiveRepPacket);
            } catch (IOException ex) {
                System.err.println(ex);
                return false;
            }
            InetAddress IPAddress = receiveRepPacket.getAddress();
            int port = receiveRepPacket.getPort();

            DatagramPacket sendPacket = new DatagramPacket(receiveRepPacket.getData(), receiveRepPacket.getData().length, IPAddress, port);
            System.out.println("Recebeu REPLY de: " + port + "\nCriou pacote para envio da confirmação");

            try {
                serverRepSocket.send(sendPacket);//replica pacote para o remetente de forma a confirmar o recebimento
                System.out.println("Enviou confirmação de REPLY");
                serverRepSocket.close();
                return true;
            } catch (IOException ex) {
                System.err.println(ex);
                return false;
            }
        } catch (Exception ex) {
            System.err.println(ex);
            return false;
        }
    }
}
