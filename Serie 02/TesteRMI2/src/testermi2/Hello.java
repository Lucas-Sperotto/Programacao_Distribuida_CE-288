/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testermi2;

import java.net.*;
import java.rmi.*;

/**
 *
 * @author lev_alunos
 */
public interface Hello extends Remote {

    String sayHello() throws RemoteException;
}
