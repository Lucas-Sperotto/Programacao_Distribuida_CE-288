package client;

/**
 * Classe que modela os algoritmos e processos descritos no artigo de Ricart e Agrawala
 * @author Sperotto
 */
public class RicartEAgrawala {

    private Thread P2, P3;
    private UDPConnection PortRef;
    private invokerCriticalSection P1;
    //BANCO DE DADOS COMPARTILHADO 
    //CONSTANTES
    /**
     * Número exclusivo do nó
     */
    private int me;//private static final int me = 1;
    /**
     * O número de nós na rede
     */
    private static final int N = 3;
    //INTEIROS
    /**
     * O número de seqüência escolhida por um pedido proveniente neste nó
     */
    private int Our_Sequence_Number;
    /**
     * O maior número de seqüência de visto em qualquer mensagem de REQUEST enviada ou recebida
     */
    private int Highest_Sequence_Number = 0;
    /**
     * O número de mensagens REPLY em espera
     */
    private int Outstanding_Reply_Count;
    //BOOLEAN
    /**
     * Verdadeiro quando este nó está solicitando acesso à sua seção crítica
     */
    private boolean Requesting_Critical_Section = false;
    /**
     * [j] é TRUE quando este nó esta adiando um REPLY para um REQUEST de j
     */
    private boolean[] Reply_Deferred;
    //SEMÁFORO BINÁRIO
    /**
     * Intertravamento acesso às variáveis ​​acima compartilhada quando necessário
     * esta variavel hão é usada... substituido por métodos sinchronized
     */
    private byte Shared_vars = 1;

    /**
     * Construtor padrão
     * @param MyProcess 
     */
    public RicartEAgrawala(int MyProcess) {

        this.me = MyProcess;//seta Id do processo
        this.PortRef = new UDPConnection(this.me);// cria conexão
        this.Reply_Deferred = new boolean[N];// aloca vetor
        for (int j = 0; j < N; j++) {
            Reply_Deferred[j] = false;// inicializa vetor
        }
        this.Outstanding_Reply_Count = 0;// inicializa variavel
        this.Our_Sequence_Number = 0;// inicializa variavel
        P1 = new invokerCriticalSection();//instancia classe que obtem exclusão
        P3 = new recieveReply();// instancia processo que recebe Reply
        P3.start();
        P2 = new recieveRequest();// instancia processo que recebe request
        P2.start();
    }

    /**
     * Inicia pedido de exclusão
     */
    public void initCritical() {
        P1.initCriticalSection();
    }

    /**
     * Libera exclusão
     */
    public void exitCritical() {
        P1.exitCriticalSection();
    }

    /**
     * @return the Highest_Sequence_Number
     */
    private synchronized int getHighest_Sequence_Number() {
        return Highest_Sequence_Number;
    }

    /**
     * @param Highest_Sequence_Number the Highest_Sequence_Number to set
     */
    private synchronized void setHighest_Sequence_Number(int Highest_Sequence_Number) {
        this.Highest_Sequence_Number = Highest_Sequence_Number;
    }

    /**
     * @return the Outstanding_Reply_Count
     */
    private synchronized int getOutstanding_Reply_Count() {
        return Outstanding_Reply_Count;
    }

    /**
     * @param Outstanding_Reply_Count the Outstanding_Reply_Count to set
     */
    private synchronized void setOutstanding_Reply_Count(int Outstanding_Reply_Count) {
        this.Outstanding_Reply_Count = Outstanding_Reply_Count;
    }

    /**
     * @return the Requesting_Critical_Section
     */
    private synchronized boolean isRequesting_Critical_Section() {
        return Requesting_Critical_Section;
    }

    /**
     * @param Requesting_Critical_Section the Requesting_Critical_Section to set
     */
    private synchronized void setRequesting_Critical_Section(boolean Requesting_Critical_Section) {
        this.Requesting_Critical_Section = Requesting_Critical_Section;
    }

    /**
     * @return the Our_Sequence_Number
     */
    private synchronized int getOur_Sequence_Number() {
        return Our_Sequence_Number;
    }

    /**
     * @param Our_Sequence_Number the Our_Sequence_Number to set
     */
    private synchronized void setOur_Sequence_Number(int Our_Sequence_Number) {
        this.Our_Sequence_Number = Our_Sequence_Number;
    }

    /**
     * PROCESSO QUE INVOCA EXCLUSÃO MÚTUA PARA ESTE NÓ
     */
    private class invokerCriticalSection {

        /**
         * Método para obter exclusão mútua
         */
        public void initCriticalSection() {
            boolean enviada = false;
            int index = 0;

            //Solicitação de Entrada à nossa seção crítica
            //P (Shared_vats)
            //Escolher um número de seqüência
            setRequesting_Critical_Section(true);//RequestingCritical_Section := TRUE;
            setOur_Sequence_Number(getHighest_Sequence_Number() + 1);//Our_Sequence_Number := Highest_Sequence_Number + l;
            //V (Shared_vars);
            setOutstanding_Reply_Count(N - 1);//Outstanding_ReplyCount := N - l;
            //Enviar uma mensagem REQUEST contendo nosso número de seqüência eo nosso número de nó para todos os outros nós
            for (int j = 0; j < N; j++) {//FORj := I STEP l UNTIL N DO IFj # me THEN
                if (j != me) {
                    enviada = false;
                    while (!enviada) {

                        enviada = PortRef.Send_Message(new REQUEST(getOur_Sequence_Number(), me), j);//Send_Message(REQUEST(Our_Sequence_Number, me),j);
                        index++;
                    }//End while
                }//End IF
            }// End for

            //Agora espera por um REPLY de cada um dos outros nós;
            while (getOutstanding_Reply_Count() != 0) {
            }//End while
            //*********************************************************************
            //Seção Crítica de Processamento pode ser feita neste momento
            //*********************************************************************
            System.out.println("Obteve Exclusão");
            Controller.setTokenhere(true);// seta obtenção da seção crítica
        }// END initcritiCalSection

        /**
         * Método para liberar exclusão mútua
         */
        private void exitCriticalSection() {
            boolean enviada = false;
            setRequesting_Critical_Section(false);//RequestingCritical_Section := FALSE;
            for (int j = 0; j < N; j++) {//FOR j := l STEP 1 UNTIL N DO
                if (Reply_Deferred[j]) {//IF Reply_Deferred[j] THEN
                    Reply_Deferred[j] = false;//Reply_Deferred[j] := FALSE;
                    //Enviar REPLY para o nó j.
                    enviada = false;
                    while (!enviada) {
                        enviada = PortRef.Send_Message(j);//Send_Message (REPLY, j);
                    }//End while
                }//End If
            }//End For
        }//End exitCriticalSection
    }//END CLASS invokerCriticalSection

    /**
     * PROCESSO QUE RECEVE MENSAGENS DE REQUEST(k, j)
     */
    private class recieveRequest extends Thread {

        private int k;// k é o número de seqüência começam solicitado
        private int j;//j é o número do nó que faz o pedido
        private REQUEST req;

        @Override
        public void run() {

            req = new REQUEST(0, 0);
            while (true) {
                boolean enviada = false;
                PortRef.recieveRequest(req);
                k = req.k;
                j = req.j;
                //verdadeiro quando não podemos responder imediatamente
                boolean Defer_it;//BOOLEAN Defer it ;  
                setHighest_Sequence_Number(Math.max(getHighest_Sequence_Number(), k));//Highest_Sequence_Number := Maximum (Highest_Sequence_Number, k);
                //P (Shared_vars);
                //Defer it := Requesting_Critical_Section AND ((k > Our_sequence_Number) OR (k = Our_Sequence_Number ANDj > me));
                if (isRequesting_Critical_Section() && ((k > getOur_Sequence_Number()) || ((k == getOur_Sequence_Number()) && (j > me)))) {
                    Defer_it = true;
                } else {
                    Defer_it = false;
                }
                //V (Shared_vars);
                //Defer_it será TRUE se temos prioridade sobre pedido do nó j
                if (Defer_it) {//IF Defer it THEN Reply_Deferred[j] := TRUE ELSE
                    Reply_Deferred[j] = true;
                } else {
                    enviada = false;
                    while (!enviada) {
                        System.out.println("Enviando Reply para " + j);
                        enviada = PortRef.Send_Message(j);//Send_Message (REPLY, j);   
                    }//End while
                }//End if-else
            }//End while
        }// End run
    }// End recieveRequest

    /**
     * PROCESSO QUE RECEBE MENSAGENS DE REPLY
     */
    private class recieveReply extends Thread {

        @Override
        public void run() {

            while (true) {
                PortRef.recieveReply();
                setOutstanding_Reply_Count(getOutstanding_Reply_Count() - 1);//Outstanding_Reply_Count := Outstanding_Reply_Count - 1;
            }// End while
        }// End run
    }// End recieveReply

    /**
     * CLASSE QUE MODELA UMA MENSAGEM DE REQUEST
     */
    public class REQUEST {

        /**
         * 
         */
        public int k;
        /**
         * 
         */
        public int j;

        /**
         * Construtor Padrão
         * @param k
         * @param j  
         */
        public REQUEST(int k, int j) {
            this.k = k;
            this.j = j;
        }// End construtor
    }//End REQUEST
}
