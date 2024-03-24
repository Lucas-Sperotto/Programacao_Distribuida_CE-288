package testermi.client;

import java.rmi.*;

public class MyClient {

    public static void main(String args[]) {
        System.setSecurityManager(new RMISecurityManager());
        new MyClient(args[0]);
    }

    public MyClient(String host) {
        try {
            System.out.println("Client started...");
            Server server = (Server) Naming.lookup("rmi://" + host + "/NiftyServer");
            System.out.println(server.getDate());
            System.out.println(server.execute(new MyCalculation(2)));
        } catch (java.io.IOException e) {
            System.out.println("Bad URL or I/O error!");
        } catch (NotBoundException e) {
            System.out.println("NiftyServer isn't registered!");
        }
    }
}