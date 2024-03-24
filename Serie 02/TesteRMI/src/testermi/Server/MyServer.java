package testermi.Server;

import java.util.*;
import java.rmi.RMISecurityManager;

public class MyServer extends java.rmi.server.UnicastRemoteObject implements Server {

    public MyServer() throws java.rmi.RemoteException {
    }

    public Date getDate() throws java.rmi.RemoteException {
        return new Date();
    }

    public Object execute(WorkRequest work) throws java.rmi.RemoteException {
        return work.execute();
    }

    public StringEnumeration getList() throws java.rmi.RemoteException {
        return new StringEnumerator(new String[]{"Foo", "Bar", "Gee"});
    }

    public void asyncExecute(WorkRequest request, WorkListener listener) throws java.rmi.RemoteException {
        Object result = request.execute();
        listener.workCompleted(request, result);
    }

    public static void main(String args[]) {
        System.setSecurityManager(new RMISecurityManager());
        try {
            Server server = new MyServer();
            java.rmi.Naming.rebind("NiftyServer", server);
        } catch (java.io.IOException e) {
            System.out.println("Got the exception" + e);
            e.printStackTrace();
        }
    }
}