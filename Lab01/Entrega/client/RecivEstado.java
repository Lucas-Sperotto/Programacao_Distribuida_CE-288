package client;

/**
 * Classe que implementa datagrama como e recebe comando para animar a String
 * @author Sperotto
 */
public class RecivEstado extends Thread {

    private int Id;
    private static UDPBroadcastTremServer recivEstado;

    /**
     * Construtor Padrão
     * @param Id int - Id da Thread/Cliente
     */
    public RecivEstado(int Id) {

        this.Id = Id;
        RecivEstado.recivEstado = new UDPBroadcastTremServer(this.Id);
    }

    /**
     * Método que dispara a Thread, atribui o movimento a String de acordo com a
     * mensagem recebida.
     */
    @Override
    public void run() {
        
        while (true) {
            recivEstado.recivEstado();
        }
    }
}
