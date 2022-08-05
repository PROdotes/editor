package editor.main;


import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class ComponentAdapter implements ComponentListener {
    EditorPanel editorPanel;

    public ComponentAdapter(EditorPanel editorPanelIN) {

        editorPanel = editorPanelIN;
    }


    @Override
    public void componentResized(ComponentEvent e) {

        editorPanel.handleResize(e.getComponent().getWidth(), e.getComponent().getHeight());

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

}
