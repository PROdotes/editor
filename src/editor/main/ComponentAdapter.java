package editor.main;


import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class ComponentAdapter implements ComponentListener {
    boolean isResized = false;
    int width = 0;
    int height = 0;


    public boolean isResized() {

        return isResized;
    }

    public void setResized(boolean resized) {

        isResized = resized;
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        isResized = true;
        width = e.getComponent().getWidth();
        height = e.getComponent().getHeight();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        isResized = true;
        width = e.getComponent().getWidth();
        height = e.getComponent().getHeight();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

}
