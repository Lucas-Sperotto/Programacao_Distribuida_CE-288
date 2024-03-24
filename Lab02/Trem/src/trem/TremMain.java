package trem;

//import javax.swing.JOptionPane;
/**
 * Classe Principal
 * @author Sperotto
 */
public class TremMain {

    /**
     * Uma Thread para receber comandos dos tres processos
     */
    private static Thread OuveCliente;
    /**
     * Variavel Objeto para a Interface Gráfica
     */
    private static Interface Inter;

    /**
     * Método Principal
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //String ServerHost = "127.0.0.1";

        //*********************************************************************
        // Pede IP para a Interface TREM.
        //*********************************************************************
        //ServerHost = JOptionPane.showInputDialog("Entre com seu Endereço de IP:", "127.0.0.1");

        //*********************************************************************
        // Inicia Interface Gráfica.
        //*********************************************************************
        Inter = new Interface();

        //*********************************************************************
        // Inicia Thread para ouvir a porta de Comunicação, 
        // dos Clientes.
        //*********************************************************************
        OuveCliente = new RecivCommand();
        //*********************************************************************
        // Starta a Thread.
        //*********************************************************************
        OuveCliente.start();
    }
}
