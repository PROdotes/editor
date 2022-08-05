package editor.main;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {
    EditorPanel editorPanel;
    boolean controlDown = false;
    boolean shiftDown = false;

    public KeyHandler(EditorPanel editorPanelIN) {

        editorPanel = editorPanelIN;
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
            editorPanel.controlToggle();
        }
        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftDown = true;
        }
        if (keyCode == KeyEvent.VK_Z) {
            if (controlDown && !shiftDown) {
                editorPanel.undo();
            }
            if (controlDown && shiftDown) {
                editorPanel.redo();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_CONTROL) {
            controlDown = false;
            editorPanel.controlToggle();
        }
        if (keyCode == KeyEvent.VK_SHIFT) {
            shiftDown = false;
        }
        if (keyCode == KeyEvent.VK_G) {
            editorPanel.toggleGrid();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}

