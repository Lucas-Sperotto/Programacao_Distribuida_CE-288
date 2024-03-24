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
public class ArrancaServidor {

    public static void main(String argv[]) {
        try {
            System.out.println("Arrancando servidor...");
            Naming.rebind("ServidorMat_1", new ServidorMat());
        } catch (Exception e) {
            System.out.println("Ocorreu um problema no arranque do servidor.\n" + e.toString());
        }
    }
}
