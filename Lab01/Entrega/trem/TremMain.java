package trem;

//import javax.swing.JOptionPane;
/**
 * Classe Principal
 * @author Sperotto
 */
public class TremMain {

    /**
     * Threads para ouvir os três Clientes, C1, C2, C3
     */
    private static Thread C1, C2, C3;
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
        // Inicia Threads para ouvir as portas de Comunicação, 
        // uma para cada Cliente.
        //*********************************************************************
        C1 = new RecivCommand(0);
        C2 = new RecivCommand(1);
        C3 = new RecivCommand(2);
        //*********************************************************************
        // Starta as Threads.
        //*********************************************************************
        C1.start();
        C2.start();
        C3.start();
    }
}
