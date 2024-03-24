package trem;

/**
 * Classe que implementa datagrama como e recebe comando para animar a String
 * @author Sperotto
 */
public class RecivCommand extends Thread {

    private int Id;
    private UDPTremServer Recv;
    private static UDPBroadastTremClient SendEstado = new UDPBroadastTremClient();

    /**
     * Construtor Padrão
     * @param Id int - Id da Thread/Cliente
     */
    public RecivCommand(int Id) {

        this.Id = Id;
        this.Recv = new UDPTremServer(this.Id);
    }

    /**
     * Método que dispara a Thread, atribui o movimento a String de acordo com a
     * mensagem recebida.
     */
    @Override
    public void run() {

        boolean ok = false;
        int comando;
        while (true) {
             ok = false;
            comando = Recv.recivComando();
            switch (comando) {
                case 0: {
                    Controller.setMovimento(false);
                    Interface.displayMessage("Recebeu de C" + this.Id + " comando: STOP\n");
                    break;
                }
                case 1: {
                    Controller.setMovimento(true);
                    Interface.displayMessage("Recebeu de C" + this.Id + " comando: MOV\n");
                    break;
                }
                case 2: {
                    Interface.displayMessage("C" + this.Id + " Requisitou Serviço\n");
                    while (!ok) {
                        ok = SendEstado.sendEstado(Id);
                    }
                    break;
                }
                case 3: {
                    Interface.displayMessage("C" + this.Id + " Liberou Serviço\n");
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
