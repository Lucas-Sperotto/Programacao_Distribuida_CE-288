package testermi.Server;


public interface StringEnumeration extends java.rmi.Remote {

    public boolean hasMoreItems() throws java.rmi.RemoteException;

    public String nextItem() throws java.rmi.RemoteException;
}