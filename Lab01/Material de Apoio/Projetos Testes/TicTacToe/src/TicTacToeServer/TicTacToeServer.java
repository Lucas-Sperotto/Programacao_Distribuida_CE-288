/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToeServer;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author Sperotto
 */
public class TicTacToeServer extends JFrame {

    private byte board[];
    private JTextArea outputArea;
    private Player players[];
    private ServerSocket server;
    private int currentPlayer;

    // configura servidor
    public TicTacToeServer() {
        super("Tic-Tac-Toe Server");

        board = new byte[9];
        players = new Player[2];
        currentPlayer = 0;

        //configura ServerSocket
        try {
            server = new ServerSocket(5000, 2);
        } // processa erros
        catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(1);
        }

        // configura Jtext area
        outputArea = new JTextArea();
        getContentPane().add(outputArea, BorderLayout.CENTER);
        outputArea.setText("Server awaiting connections\n");

        setSize(300, 300);
        setVisible(true);
    }

    public void execute() {

        for (int i = 0; i < players.length; i++) {

            try {
                players[i] = new Player(server.accept(), i);
                players[i].start();

            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }
        synchronized (players[0]) {
            players[0].setSuspended(false);
            players[0].notify();
        }
    }

    public void display(String message) {

        outputArea.append(message + "\n");
    }

    public synchronized boolean validMove(int location, int player) {

        boolean moveDone = false;

        while (player != currentPlayer) {

            try {
                wait();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        if (!isOccupied(location)) {
            board[location] = (byte) (currentPlayer == 0 ? 'X' : 'O');
            currentPlayer = (currentPlayer + 1) % 2;
            players[currentPlayer].otherPlayerMoved(location);
            notify();
            return true;
        } else {
            return false;
        }
    }

    public boolean isOccupied(int location) {

        if (board[location] == 'X' || board[location] == 'O') {
            return true;
        } else {
            return false;
        }
    }

    public boolean gameOver() {
        return false;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TicTacToeServer application = new TicTacToeServer();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.execute();
    }

    
    
    
    
    private class Player extends Thread {

        private Socket connection;
        private DataInputStream input;
        private DataOutputStream output;
        private int playerNumber;
        private char mark;
        protected boolean suspended = true;

        public Player(Socket socket, int number) {

            playerNumber = number;

            mark = (playerNumber == 0 ? 'X' : 'O');

            connection = socket;

            try {
                input = new DataInputStream(connection.getInputStream());
                output = new DataOutputStream(connection.getOutputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }

        public void otherPlayerMoved(int location) {

            try {
                output.writeUTF("Oponente Movendo");
                output.writeInt(location);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }

        public void run() {

            try {
                display("Player " + (playerNumber == 0 ? 'X' : 'O') + " conected");
                output.writeChar(mark);
                output.writeUTF("Player " + (playerNumber == 0 ? "X connected\n" : "O connected, please wait\n"));
                if (mark == 'X') {
                    output.writeUTF("Waithing for another player");

                    try {
                        synchronized (this) {
                            while (suspended) {
                                wait();
                            }
                        }
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    output.writeUTF("Other Player connected. Your Move");
                }
                while (!gameOver()) {
                    int location = input.readInt();
                    if (validMove(location, playerNumber)) {
                        display("loc: " + location);
                        output.writeUTF("Valid move.");
                    } else {
                        output.writeUTF("Invalid move. Try again");
                    }
                }
                connection.close();

            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }

        public void setSuspended(boolean status) {

            suspended = status;
        }
    }
}
