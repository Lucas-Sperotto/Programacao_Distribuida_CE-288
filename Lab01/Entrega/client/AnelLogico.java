package client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que modela o anel lógico e tratamentos para o token
 * @author Sperotto
 */
public class AnelLogico extends Thread {

    private int nbping;
    private int nbpong;
    private int token;
    private boolean pinghere, ponghere;
    private UDPAnelConnection PortRef;
    private int MyProcess;

    /**
     * Construtor do anel
     * @param MyProcess
     */
    public AnelLogico(int MyProcess) {
        this.MyProcess = MyProcess;
        nbping = 1;
        nbpong = -1;
        token = 0;
        pinghere = false;
        ponghere = false;
        PortRef = new UDPAnelConnection(MyProcess);
    }

    @Override
    public void run() {
        cicloAnel();
    }

    /**
     * Método que ouve e envia ping-pong, seta e libera exclusão
     */
    private synchronized void cicloAnel() {

        int t = 0;
        boolean coninicial = true;
        boolean Sending = false;

        while (true) {


            if (this.MyProcess == 0 && coninicial) {
                coninicial = false;
                Controller.setTokenhere(false);
                while (!Sending) {
                    Sending = PortRef.sendPingPong(nbping);
                }
                Sending = false;

                while (!Sending) {
                    Sending = PortRef.sendPingPong(nbpong);
                }
                Sending = false;
            }

            if (!Controller.getCriticalSection()) {
                if (Controller.getControle()) {
                    Sending = false;
                    Controller.setControle(false);
                    while (!Sending) {
                        Sending = PortRef.sendPingPong(nbping);
                    }
                    Sending = false;
                }

                t = PortRef.recivPingPong();
                processPingPong(t);

//                try {
//                    sleep(1000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(AnelLogico.class.getName()).log(Level.SEVERE, null, ex);
//                }

                if (pinghere) {
                    token = nbping;
                    Controller.setTokenhere(false);
                    System.out.println("Enviando ping");
                    while (!Sending) {
                        Sending = PortRef.sendPingPong(nbping);
                    }

                    pinghere = false;
                    Sending = false;
                }
                if (ponghere) {
                    token = nbpong;
                    System.out.println("Enviando pong");
                    while (!Sending) {
                        Sending = PortRef.sendPingPong(nbpong);
                    }
                    ponghere = false;
                    Sending = false;
                }

            } else {
                if (pinghere && !ponghere) {
                    t = PortRef.recivPingPong();
                    processPingPong(t);
                }
                if (!pinghere && ponghere) {
                    token = nbpong;
                    System.out.println("Enviando pong");
                    while (!Sending) {
                        Sending = PortRef.sendPingPong(nbpong);
                    }
                    ponghere = false;
                    Sending = false;
                }
                if (!pinghere || !ponghere) {
                    t = PortRef.recivPingPong();
                    processPingPong(t);
                }
            }
        }
    }

    /**
     * Método que processa as informações do ping e pong
     */
    private synchronized void processPingPong(int t) {

        if (t > 0) {
            pinghere = true;
            Controller.setTokenhere(true);
            nbping = t;
            if (ponghere) {
                nbping = (nbping % 4) + 1;
                nbpong = (nbpong % 4) - 1;
            } else {
                if (token != nbping) {
                    token = nbping;
                } else {
                    nbping = (nbping % 4) + 1;
                    nbpong = -(nbping);
                    ponghere = true;//regenerou ele esta aki
                }
            }
        } else {
            ponghere = true;
            nbpong = t;
            if (pinghere) {
                nbping = (nbping % 4) + 1;
                nbpong = (nbpong % 4) - 1;
            } else {
                if (token != nbpong) {
                    token = nbpong;
                } else {
                    nbpong = (nbpong % 4) + 1;
                    nbping = -(nbpong);
                    pinghere = true;//regenerou ele esta aki
                }
            }
        }
    }
}