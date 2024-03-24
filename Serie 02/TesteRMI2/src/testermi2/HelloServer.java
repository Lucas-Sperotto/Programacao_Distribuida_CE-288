/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testermi2;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
/**
 *
 * @author lev_alunos
 */
public class HelloServer {
    public static void main (String args [ ]) {
        //Cria e instala o security manager
        //System.setSecurityManager(new RMISecurityManager() );
        try {
            //Cria HelloImpl
            HelloImpl obj = new HelloImpl();
            Naming.rebind("HelloServer", obj);
            System.out.println("Hello Server pronto.");
        } catch(Exception e) {
            System.out.println("HelloServer erro"+ e.getMessage());
        }
    }
}
