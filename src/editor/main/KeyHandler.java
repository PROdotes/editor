package editor.main;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {
    boolean controlDown = false;

    public boolean isControlDown() {
        return controlDown;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (keyCode == KeyEvent.VK_CONTROL) {
            controlDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_CONTROL) {
            controlDown = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

