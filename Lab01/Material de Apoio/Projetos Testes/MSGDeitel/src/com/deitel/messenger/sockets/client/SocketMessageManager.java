// Fig. 24.25: SocketMessageManager.java
// SocketMessageManager se comunica com um DeitelMessengerServer utilizando
// Sockets e MulticastSockets.
package com.deitel.messenger.sockets.client;

import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.deitel.messenger.MessageListener;
import com.deitel.messenger.MessageManager;
import static com.deitel.messenger.sockets.SocketMessengerConstants.*;

public class SocketMessageManager implements MessageManager
{
   private Socket clientSocket; // Socket para mensagens enviadas
   private String serverAddress; // Endere�o do DeitelMessengerServer
   private PacketReceiver receiver; // recebe mensagens de multicast
   private boolean connected = false; // status da conex�o
   private ExecutorService serverExecutor; // executor para o servidor
   
   public SocketMessageManager( String address )
   {
      serverAddress = address; // armazena o endere�o de servidor
      serverExecutor = Executors.newCachedThreadPool();
   } // fim do construtor SocketMessageManager
   
   // conecta-se ao servidor e envia mensagens para um dado MessageListener
   public void connect( MessageListener listener ) 
   {
      if ( connected )
         return; // se j� conectado, retorna imediatamente

      try // abre a conex�o de Socket ao DeitelMessengerServer com Socket 
      {
         clientSocket = new Socket(                               
            InetAddress.getByName( serverAddress ), SERVER_PORT );

         // cria um execut�vel para receber as mensagens entrantes     
         receiver = new PacketReceiver( listener );             
         serverExecutor.execute( receiver ); // executa o execut�vel
         connected = true; // atualiza o flag conectado
      } // fim do try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // fim do catch
   } // fim do m�todo connect
   
   // desconecta-se do servidor e remove o registro de um dado MessageListener
   public void disconnect( MessageListener listener ) 
   {
      if ( !connected )
         return; // se n�o conectado, retorna imediatamente
      
      try // p�ra o ouvinte e desconecta-se do servidor
      {     
         // notifica o servidor de que o cliente est� se desconectando
         Runnable disconnecter = new MessageSender( clientSocket, "",  
            DISCONNECT_STRING );                                       
         Future disconnecting = serverExecutor.submit( disconnecter );
         disconnecting.get(); // espera que a mensagem de desconex�o seja enviada
         receiver.stopListening(); // p�ra o receptor
         clientSocket.close(); // fecha o Socket sainte
      } // fim do try
      catch ( ExecutionException exception ) 
      {
         exception.printStackTrace();
      } // fim do catch
      catch ( InterruptedException exception ) 
      {
         exception.printStackTrace();
      } // fim do catch
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // fim do catch
     
      connected = false; // atualiza o flag conectado
   } // fim do m�todo disconnect
   
   // envia mensagem ao servidor
   public void sendMessage( String from, String message ) 
   {
      if ( !connected )
         return; // se n�o conectado, retorna imediatamente
      
      // cria e inicia um novo MessageSender para entregar a mensagem
      serverExecutor.execute(                                 
         new MessageSender( clientSocket, from, message) );   
   } // fim do m�todo sendMessage
} // fim do m�todo SocketMessageManager


/**************************************************************************
 * (C) Copyright 1992-2005 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/