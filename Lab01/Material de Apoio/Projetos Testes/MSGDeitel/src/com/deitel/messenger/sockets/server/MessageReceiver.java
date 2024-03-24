// Fig. 24.22: MessageReceiver java
// MessageReceiver � um execut�vel que ouve mensagens a partir de um
// cliente particular e envia mensagens para um MessageListener.
package com.deitel.messenger.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import com.deitel.messenger.MessageListener;
import static com.deitel.messenger.sockets.SocketMessengerConstants.*;

public class MessageReceiver implements Runnable {

    private BufferedReader input; // fluxo de entrada
    private MessageListener messageListener; // ouvinte da mensagem
    private boolean keepListening = true; // quando false, termina o execut�vel

    // Construtor de MessageReceiver
    public MessageReceiver(MessageListener listener, Socket clientSocket) {
        // configura o ouvinte ao qual novas mensagens devem ser enviadas
        messageListener = listener;

        try {
            // configura o tempo limite para leitura do cliente            
            clientSocket.setSoTimeout(5000); // cinco segundos

            // cria BufferedReader para ler mensagens entrantes
            input = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
        } // fim do try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // fim do catch
    } // fim do construtor MessageReceiver

    // ouve novas mensagens e as envia para o MessageListener
    public void run() {
        String message; // String para mensagens entrantes

        // ouve mensagens at� ser parado
        while (keepListening) {
            try {
                message = input.readLine(); // l� a mensagem do cliente  
            } // fim do try
            catch (SocketTimeoutException socketTimeoutException) {
                continue; // continua para a pr�xima itera��o para se manter ouvindo
            } // fim do catch
            catch (IOException ioException) {
                ioException.printStackTrace();
                break;
            } // fim do catch

            // assegura que a mensagem n�o seja nula
            if (message != null) {
                // divide a mensagem em tokens para recuperar o nome do usu�rio e corpo da mensagem
                StringTokenizer tokenizer = new StringTokenizer(
                        message, MESSAGE_SEPARATOR);

                // ignora as mensagens que n�o cont�m um nome de usu�rio
                // e um corpo de mensagem
                if (tokenizer.countTokens() == 2) {
                    // envia a mensagem para MessageListener         
                    messageListener.messageReceived(
                            tokenizer.nextToken(), // nome de usu�rio     
                            tokenizer.nextToken()); // corpo de mensagem
                } // fim do if
                else {
                    // se recebeu mensagem de desconex�o, p�ra de ouvir
                    if (message.equalsIgnoreCase(
                            MESSAGE_SEPARATOR + DISCONNECT_STRING)) {
                        stopListening();
                    }
                } // fim de else
            } // fim do if
        } // fim do while

        try {
            input.close(); // fecha o BufferedReader (tamb�m fecha o Socket)
        } // fim do try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // fim do catch
    } // fim do m�todo run

    // p�ra de ouvir mensagens entrantes
    public void stopListening() {
        keepListening = false;
    } // fim do m�todo stopListening
} // fim da classe MessageReceiver

