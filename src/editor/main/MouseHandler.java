package editor.main;


import java.awt.event.*;


public class MouseHandler implements MouseListener, MouseWheelListener, MouseMotionListener {
    EditorPanel editorPanel;
    boolean leftClick = false;
    boolean rightClick = false;
    boolean middleClick = false;
    int mouseX = 0, mouseY = 0;

    public MouseHandler(EditorPanel editorPanelIN) {

        editorPanel = editorPanelIN;
    }

    public int getMouseX() {

        return mouseX;
    }

    public int getMouseY() {

        return mouseY;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            editorPanel.handleLeftClick(e.getX(), e.getY());
            if (e.getClickCount() == 2) {
                editorPanel.doubleClick(e.getX(), e.getY());
            }
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            editorPanel.toggleZoom();
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            editorPanel.handleRightClick(e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = true;
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            middleClick = true;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = false;
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            middleClick = false;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightClick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        editorPanel.handleMouseWheel(e.getWheelRotation(), e.getX(), e.getY());

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (leftClick) {
            editorPanel.handleLeftClick(e.getX(), e.getY());
        }
        if (rightClick) {
            editorPanel.handleRightClick(e.getX(), e.getY());
        }
        if (middleClick) {
            editorPanel.toggleZoom();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        editorPanel.handleMouseMoved(e.getX(), e.getY());
        mouseX = e.getX();
        mouseY = e.getY();
    }


}
