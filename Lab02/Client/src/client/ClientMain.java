package client;

import javax.swing.JOptionPane;

/**
 * Classe Principal
 * @author Sperotto
 */
public class ClientMain {

    private static Interface Inter;

    /**
     * Método Principal
     * @param args the command line arguments
     */
    public static void main(String[] args) {
System.out.println(Math.toDegrees( Math.atan(-2.0)));
//        String MyProc;
//        String ServerHost = "127.0.0.1";
//
//        //ServerHost = JOptionPane.showInputDialog("Entre com seu Endereço de IP:", "127.0.0.1");
//        MyProc = JOptionPane.showInputDialog("Entre com o ID do seu Processo:", "Digite 0, 1 ou 2");
//
//        Inter = new Interface(Integer.parseInt(MyProc), ServerHost);
//        RecivEstado RE = new RecivEstado(Integer.parseInt(MyProc));
//        RE.start();
    }
}
