package trem;

/**
 * Classe que implementa datagrama como e recebe comando para animar a String
 * @author Sperotto
 */
public class RecivCommand extends Thread {

    private int RecivId;
    private UDPTremServer Reciv;
    private static UDPBroadastTremClient SendEstado = new UDPBroadastTremClient();

    /**
     * Construtor Padrão
     */
    public RecivCommand() {
        this.Reciv = new UDPTremServer();
    }

    /**
     * Método que dispara a Thread, atribui o movimento a String de acordo com a
     * mensagem recebida.
     */
    @Override
    public void run() {

        boolean ok = false;
        int[] comando = new int[2];
        while (true) {
             ok = false;
            comando = Reciv.recivComando();
            switch (comando[0]) {
                case 0: {
                    Controller.setMovimento(false);
                    Interface.displayMessage("Recebeu de C" + comando[1] + " comando: STOP\n");
                    break;
                }
                case 1: {
                    Controller.setMovimento(true);
                    Interface.displayMessage("Recebeu de C" + comando[1] + " comando: MOV\n");
                    break;
                }
                case 2: {
                    Interface.displayMessage("C" + comando[1] + " Requisitou Serviço\n");
                    while (!ok) {
                        ok = SendEstado.sendEstado(comando[1]);
                    }
                    break;
                }
                case 3: {
                    Interface.displayMessage("C" + comando[1] + " Liberou Serviço\n");
                    ok = false;
                    while (!ok) {
                        ok = SendEstado.sendEstado(3);
                    }
                    break;
                }
                default: {
                    System.err.print("Comando Não Intrepretado\n");
                    break;
                }
            }
        }
    }
}
