/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testermi.Client;

import java.rmi.*;

public class MyClient extends java.rmi.server.UnicastRemoteObject implements WorkListener {

    public void workCompleted(WorkRequest request, Object result)
            throws RemoteException {
        System.out.println("Async work result = "
                + result);
    }

    public static void main(String args[]) throws
            java.rmi.RemoteException {
        System.setSecurityManager(new RMISecurityManager());
        new MyClient(args[0]);


    }

    public MyClient(String host) throws java.rmi.RemoteException {
        try {
            System.out.println("Client starter...");
            Server server = (Server) Naming.lookup("rmi://" + host + "/NiftyServer");
            System.out.println(server.getDate());
            System.out.println(server.execute(new MyCalculation(2)));
            StringEnumeration se = server.getList();
            while (se.hasMoreItems()) {
                System.out.println(se.nextItem());
            }
            server.asyncExecute(new MyCalculation(10), this);
        } catch (java.io.IOException e) {
            System.out.println("Bad URL or I/O error!");
        } catch (NotBoundException e) {
            System.out.println("NiftyServer isn't registered!");
        }
    }
}