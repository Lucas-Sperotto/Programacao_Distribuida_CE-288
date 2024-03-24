package testermi.Client;


public class StringEnumerator extends java.rmi.server.UnicastRemoteObject implements StringEnumeration {

    String[] list;
    int index = 0;

    public StringEnumerator(String[] list) throws java.rmi.RemoteException {
        this.list = list;
    }

    public boolean hasMoreItems() throws java.rmi.RemoteException {
        return index < list.length;
    }

    public String nextItem() throws java.rmi.RemoteException {
        return list[index++];
    }
}