package editor.main;


import java.awt.event.*;


public class MouseHandler implements MouseListener, MouseWheelListener, MouseMotionListener {
    int mouseX = 0;
    int mouseY = 0;
    int wheelAmount = 0;
    boolean mouseDown = false;
    boolean rightClick = false;
    boolean middleClick = false;
    boolean shortRightClick = false;

    public boolean isShortRightClick() {
        return shortRightClick;
    }

    public void setShortRightClick(boolean shortRightClick) {
        this.shortRightClick = shortRightClick;
    }

    public boolean isMiddleClick() {
        return middleClick;
    }

    public void setMiddleClick(boolean middleClick) {
        this.middleClick = middleClick;
    }

    public boolean isRightClick() {
        return rightClick;
    }

    public int getWheelAmount() {

        return wheelAmount;
    }

    public void setWheelAmount(int wheelAmountIN) {
        wheelAmount = wheelAmountIN;
    }


    public boolean isMouseDown() {

        return mouseDown;
    }

    public int getMouseX() {

        return mouseX;
    }

    public int getMouseY() {

        return mouseY;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            shortRightClick = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = true;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = false;
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            middleClick = true;
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

        wheelAmount = e.getWheelRotation();
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

    }
}
