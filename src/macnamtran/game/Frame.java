package macnamtran.game;
import javax.swing.JFrame;

import macnamtran.game.graphics.Screen;

public class Frame {
    
    public Frame() {
        
        JFrame frame = new JFrame();
        Screen s = new Screen();
        
        frame.add(s);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Snake");
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);     
        
    }
    public static void main(String[] args) {
        new Frame();
        
    }
}
