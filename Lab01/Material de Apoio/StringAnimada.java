import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class NotHelloWorldPanel extends JPanel {
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawString("Java Source and Support", 75, 100);
  }
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setTitle("NotHelloWorld");
    frame.setSize(300, 200);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    Container contentPane = frame.getContentPane();
    contentPane.add(new NotHelloWorldPanel());

    frame.show();
  }
}