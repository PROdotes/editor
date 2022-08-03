package editor.main;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class MouseHandler implements MouseListener, MouseWheelListener {
    int mouseX = 0;
    int mouseY = 0;
    int wheelAmount = 0;
    boolean mouseDown = false;



    public int getWheelAmount() {

        return wheelAmount;
    }

    public void setWheelAmount(int wheelAmountIN) {
        wheelAmount = wheelAmountIN;
    }



    public void setMouseDown(boolean mouseDown) {

        this.mouseDown = mouseDown;
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

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseX = e.getX();
            mouseY = e.getY();
            mouseDown = true;
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
            mouseX = e.getX(); mouseY = e.getY();
    }

}
