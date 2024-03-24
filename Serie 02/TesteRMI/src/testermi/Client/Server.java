package testermi.Client;


import java.util.Date;

public interface Server extends java.rmi.Remote {

    public Date getDate() throws java.rmi.RemoteException;

    public Object execute(WorkRequest work) throws java.rmi.RemoteException;

    StringEnumeration getList() throws java.rmi.RemoteException;

    void asyncExecute(WorkRequest work, WorkListener listener) throws java.rmi.RemoteException;
}