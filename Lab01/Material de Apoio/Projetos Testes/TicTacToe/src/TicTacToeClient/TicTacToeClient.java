/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToeClient;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author lev_alunos
 */
public class TicTacToeClient extends JApplet implements Runnable {

    private JTextField idField;
    private JTextArea displayArea;
    private JPanel boardPanel, panel2;
    private Square board[][], currentSquare;
    private Socket connection;
    private DataInputStream input;
    private DataOutputStream output;
    private Thread outputThread;
    private char myMark;
    private boolean myTurn;

    public void init() {
        Container container = getContentPane();

        displayArea = new JTextArea(4, 30);
        displayArea.setEditable(false);
        container.add(new JScrollPane(displayArea), BorderLayout.SOUTH);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 0, 0));

        board = new Square[3][3];

        for (int row = 0; row < board.length; row++) {

            for (int column = 0; column < board[row].length; column++) {
                board[row][column] = new Square(' ', row * 3 + column);
                boardPanel.add(board[row][column]);
            }
        }

        idField = new JTextField();
        idField.setEditable(false);
        container.add(idField, BorderLayout.NORTH);

        panel2 = new JPanel();
        panel2.add(boardPanel, BorderLayout.CENTER);
        container.add(panel2, BorderLayout.CENTER);
    }

    public void start() {
        try {
            connection = new Socket(InetAddress.getByName("127.0.0.1"), 5000);
            input = new DataInputStream(connection.getInputStream());
            output = new DataOutputStream(connection.getOutputStream());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(1);
        }

        outputThread = new Thread(this);
        outputThread.start();
    }

    public void run() {

        try {
            myMark = input.readChar();
            idField.setText("You are player\"" + myMark + "\"");
            myTurn = (myMark == 'X' ? true : false);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(1);
        }

        while (true) {

            try {
                String message = input.readUTF();
                processMessage(message);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void processMessage(String message) {

        if (message.equals("valid move.")) {
            displayArea.append("Valid move, please wait.\n");

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    currentSquare.setMark(myMark);
                }
            });
        } else if (message.equals("Invalid move, try again")) {
            displayArea.append(message + "\n");
            myTurn = true;
        } else if (message.equals("Opponent moved")) {
            try {
                final int location = input.readInt();
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        int row = location / 3;
                        int colunm = location % 3;

                        board[row][colunm].setMark(myMark == 'X' ? 'O' : 'X');
                        displayArea.append("Opponent moved. your turn.\n");
                    }
                });
                myTurn = true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        } else {
            displayArea.append(message + "\n");
        }
        displayArea.setCaretPosition(displayArea.getText().length());
    }

    public void sendClickedSquare(int location) {
        if (myTurn) {
            try {
                output.writeInt(location);
                myTurn = false;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void setCurrentSquare(Square square) {
        currentSquare = square;
    }

    private class Square extends JPanel {

        private char mark;
        private int location;

        public Square(char squareMark, int squareLocation) {
            mark = squareMark;
            location = squareLocation;

            addMouseListener(new MouseAdapter() {

                public void mouseRealesed(MouseEvent e) {
                    setCurrentSquare(Square.this);
                    sendClickedSquare(getSquareLocation());
                }
            });
        }

        public Dimension getPreferredSize() {
            return new Dimension(30, 30);
        }

        public Dimension getMinimunSize() {
            return getPreferredSize();
        }

        public void setMark(char newMark) {
            mark = newMark;
            repaint();
        }

        public int getSquareLocation() {
            return location;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawRect(0, 0, 29, 29);
            g.drawString(String.valueOf(mark), 11, 20);
        }
    }
}