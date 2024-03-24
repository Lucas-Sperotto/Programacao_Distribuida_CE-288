package trem;

/**
 * Classe para garantir algumas variaveis sincronizadas.
 * @author Sperotto
 */
public  class Controller {
    
    private static boolean Move = false;
    
    /**
     * Construtor Padrão para Controller
     */
    public Controller(){
        Controller.Move = true;
    }
    
    /**
     * Seta estado do movimento da String
     * @param CriticalSection boolean true para movimentando false para parada
     */
    public static synchronized void setMovimento(boolean CriticalSection){
        Controller.Move = CriticalSection;
    }
    /**
     * retorna estado do movimento da string
     * @return boolean true para movimentando false para parada
     */
    public static synchronized boolean getMovimento(){
        return Controller.Move;
    }
}
