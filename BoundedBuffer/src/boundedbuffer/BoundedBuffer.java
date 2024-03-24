/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package boundedbuffer;

/**
 *
 * @author Sperotto
 */
public class BoundedBuffer {

    private UDPConnection putChar;// ENTRYPORT putchar: char REPLY signaltype ;
    private UDPConnection getChar;//ENTRYPORT getchar: signaltype REPLY char ;
    private static final int poolsize = 100;
    private char[] pool = new char[poolsize];
    private int inp, outp;//: 1..poolsize;
    private int count;// : 0..poolsize;
    int signal = 1;

    public BoundedBuffer() {
        putChar = new UDPConnection(0);
        getChar = new UDPConnection(1);
        inp = 1;
        outp = 1;
        count = 0;
    }

    public void boundedbuffer() {

        while (true) {
            if (count < poolsize) {
                //RECEIVE pool[inp] FROM putchar REPLY signal =>
                pool[inp] = putChar.recieve(signal);
                inp = (inp % poolsize) + 1;
                count += 1;
            } else {
                if (count > 0) {
                    //RECEIVE signal FROM getchar REPLY pool[outp] =>
                    signal = getChar.recieve(pool[outp]);
                    outp = (outp % poolsize) + 1;
                    count -= 1;
                }
            }
        }
    }
}