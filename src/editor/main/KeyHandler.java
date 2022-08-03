package editor.main;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {
    boolean controlDown = false;
    boolean undo = false;
    boolean redo = false;
    boolean shiftDown = false;


    public boolean isRedo() {
        return redo;
    }

    public void setRedo(boolean redo) {
        this.redo = redo;
    }

    public boolean isUndo() {
        return undo;
    }

    public void setUndo(boolean undoIN) {
        undo = undoIN;
    }

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
        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftDown = true;
        }
        if (keyCode == KeyEvent.VK_Z) {
            if (controlDown && !shiftDown) {
                undo = true;
            }
            if (controlDown && shiftDown) {
                redo = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_CONTROL) {
            controlDown = false;
        }
        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftDown = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

