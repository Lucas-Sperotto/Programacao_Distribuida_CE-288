package client;

/**
 * Classe para garantir algumas variaveis sincronizadas.
 * @author Sperotto
 */
public class Controller {

    private static boolean CriticalSection = false;
    private static boolean Tokenhere = false;
    private static boolean Controle = false;

    /**
     * Construtor
     */
    public Controller() {
        Controller.CriticalSection = false;
        Controller.Tokenhere = false;
        Controller.Controle = false;
    }

    /**
     * Seta variavel booleana para seção critica
     * @param CriticalSection
     */
    public static synchronized void setCriticalSection(boolean CriticalSection) {
        Controller.CriticalSection = CriticalSection;
    }

    /**
     * Método para verificar seção crítica
     * @return
     */
    public static synchronized boolean getCriticalSection() {
        return Controller.CriticalSection;
    }

    /**
     * Método para setar se o tonken está presente
     * @param Tokenhere
     */
    public static synchronized void setTokenhere(boolean Tokenhere) {
        Controller.Tokenhere = Tokenhere;
    }

    /**
     * Metodo para verificar se o token esta presente
     * @return
     */
    public static synchronized boolean getTokenhere() {
        return Controller.Tokenhere;
    }

    /**
     * Seta controle 
     * @param Controle
     */
    public static synchronized void setControle(boolean Controle) {
        Controller.Controle = Controle;
    }

    /**
     * recupera controle
     * @return
     */
    public static synchronized boolean getControle() {
        return Controller.Controle;
    }
}
