/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testermi2;

import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

/**
 *
 * @author lev_alunos
 */
public class HelloClient {

    public static void main(String args[]) {
        //Cria e instala o security manager
        //    System.setSecurityManager (new RMISecurityManager () );
        try {
            Hello obj = (Hello) Naming.lookup("rmi://vip03/HelloServer");
            System.out.println(obj.sayHello());
        } catch (Exception e) {
            System.out.println("HelloClient erro" + e.getMessage());
        }
        System.exit(0);
    }
}