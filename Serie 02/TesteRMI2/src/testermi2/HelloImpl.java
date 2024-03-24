/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testermi2;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;

/**
 *
 * @author lev_alunos
 */
public class HelloImpl extends UnicastRemoteObject implements Hello {

    public HelloImpl() throws RemoteException {
        super();
    }

    public String sayHello() {
        return "HelloWorld!";
    }
}
