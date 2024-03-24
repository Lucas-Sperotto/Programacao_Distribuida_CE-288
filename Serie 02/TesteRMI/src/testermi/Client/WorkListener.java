package testermi.Client;


public interface WorkListener extends java.rmi.Remote {

    public void workCompleted(WorkRequest work, Object result) throws java.rmi.RemoteException;
}