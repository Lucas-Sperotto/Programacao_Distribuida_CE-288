/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testermi3;

import java.rmi.*;

/**
 *
 * @author lev_alunos
 */
public interface InterfaceServidorMat extends Remote {

    public double soma(double a, double b) throws RemoteException;

    public double subtrai(double a, double b) throws RemoteException;

    public double multiplica(double a, double b) throws RemoteException;

    public double divide(double a, double b) throws RemoteException;
}
